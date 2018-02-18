/*
 *    Copyright 2018 CManLH
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.lifeonwalden.app.cache.caffeine;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.lifeonwalden.app.cache.constant._CacheSpecialKey;
import com.lifeonwalden.app.util.logger.LoggerUtil;
import com.lifeonwalden.app.util.map.MapUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.lang.UsesJava8;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * Spring {@link org.springframework.cache.Cache} adapter implementation
 * on top of a Caffeine {@link com.github.benmanes.caffeine.cache.Cache} instance.
 * <p>
 * <p>Requires Caffeine 2.1 or higher.
 *
 * @author Ben Manes
 * @author Juergen Hoeller
 * @author Stephane Nicoll
 * @author CManLH
 */
@UsesJava8
public class AppCaffeineCache extends AbstractValueAdaptingCache {
    private final static Logger logger = LoggerUtil.getLogger(AppCaffeineCache.class);

    private final String name;

    private final com.github.benmanes.caffeine.cache.Cache<Object, Object> cache;

    /**
     * Create a {@link AppCaffeineCache} instance with the specified name and the
     * given internal {@link com.github.benmanes.caffeine.cache.Cache} to use.
     *
     * @param name  the name of the cache
     * @param cache the backing Caffeine Cache instance
     */
    public AppCaffeineCache(String name, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache) {
        this(name, cache, true);
    }

    /**
     * Create a {@link AppCaffeineCache} instance with the specified name and the
     * given internal {@link com.github.benmanes.caffeine.cache.Cache} to use.
     *
     * @param name            the name of the cache
     * @param cache           the backing Caffeine Cache instance
     * @param allowNullValues whether to accept and convert {@code null}
     *                        values for this cache
     */
    public AppCaffeineCache(String name, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache,
                            boolean allowNullValues) {
        super(allowNullValues);
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(cache, "Cache must not be null");
        this.name = name;
        this.cache = cache;
    }


    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public final com.github.benmanes.caffeine.cache.Cache<Object, Object> getNativeCache() {
        return this.cache;
    }

    @Override
    public ValueWrapper get(Object key) {
        if (this.cache instanceof LoadingCache) {
            Object value = ((LoadingCache<Object, Object>) this.cache).get(key);
            return toValueWrapper(value);
        }
        return super.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key, final Callable<T> valueLoader) {
        if (_CacheSpecialKey.FULL_CACHE_FETCHING.equals(key) || _CacheSpecialKey.CACHE_REFRESHING.equals(key)) {
            if (cache.estimatedSize() <= 0 || _CacheSpecialKey.CACHE_REFRESHING.equals(key)) {
                try {
                    this.put(key, valueLoader.call());
                } catch (Exception ex) {
                    logger.error("Failed to load cache data.", ex);

                    throw new ValueRetrievalException(key, valueLoader, ex);
                }
            }
            return (T) fromStoreValue(MapUtil.shallowCopy(this.cache.asMap(), HashMap.class));
        } else {
            return (T) fromStoreValue(this.cache.get(key, new AppCaffeineCache.LoadFunction(valueLoader)));
        }
    }

    @Override
    protected Object lookup(Object key) {
        if (_CacheSpecialKey.CACHE_REFRESHING.equals(key)) {
            return null;
        } else if (_CacheSpecialKey.FULL_CACHE_FETCHING.equals(key)) {
            if (this.cache.estimatedSize() == 0) {
                return null;
            }
            return MapUtil.shallowCopy(this.cache.asMap(), HashMap.class);
        } else {
            return this.cache.getIfPresent(key);
        }
    }

    @Override
    public void put(Object key, Object value) {
        if (_CacheSpecialKey.CACHE_REFRESHING.equals(key) || _CacheSpecialKey.FULL_CACHE_FETCHING.equals(key)) {
            Map<?, ?> map = (Map<?, ?>) value;
            map.forEach((_key, _value) -> this.cache.put(_key, toStoreValue(_value)));
        } else {
            this.cache.put(key, toStoreValue(value));
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, final Object value) {
        if (_CacheSpecialKey.CACHE_REFRESHING.equals(key) || _CacheSpecialKey.FULL_CACHE_FETCHING.equals(key)) {
            if (this.cache.estimatedSize() > 0 && _CacheSpecialKey.FULL_CACHE_FETCHING.equals(key)) {
                return null;
            } else {
                this.put(key, value);
                return toValueWrapper(MapUtil.shallowCopy(this.cache.asMap(), HashMap.class));
            }
        } else {
            AppCaffeineCache.PutIfAbsentFunction callable = new AppCaffeineCache.PutIfAbsentFunction(value);
            Object result = this.cache.get(key, callable);
            return (callable.called ? null : toValueWrapper(result));
        }
    }

    @Override
    public void evict(Object key) {
        this.cache.invalidate(key);
    }

    @Override
    public void clear() {
        this.cache.invalidateAll();
    }


    private class PutIfAbsentFunction implements Function<Object, Object> {

        private final Object value;

        private boolean called;

        public PutIfAbsentFunction(Object value) {
            this.value = value;
        }

        @Override
        public Object apply(Object key) {
            this.called = true;
            return toStoreValue(this.value);
        }
    }


    private class LoadFunction implements Function<Object, Object> {

        private final Callable<?> valueLoader;

        public LoadFunction(Callable<?> valueLoader) {
            this.valueLoader = valueLoader;
        }

        @Override
        public Object apply(Object o) {
            try {
                return toStoreValue(valueLoader.call());
            } catch (Exception ex) {
                throw new ValueRetrievalException(o, valueLoader, ex);
            }
        }
    }
}
