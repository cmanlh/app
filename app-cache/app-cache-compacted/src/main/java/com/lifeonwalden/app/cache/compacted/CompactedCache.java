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

package com.lifeonwalden.app.cache.compacted;

import com.lifeonwalden.app.cache.constant._CacheSpecialKey;
import com.lifeonwalden.app.cache.redis.AppRedisCacheConfiguration;
import com.lifeonwalden.app.cache.redis.AppRedisCacheWriter;
import com.lifeonwalden.app.cache.redis.serialization.impl.ValueSerializationPair;
import com.lifeonwalden.app.util.logger.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.NullValue;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class CompactedCache extends AbstractValueAdaptingCache {
    private final static Logger logger = LoggerUtil.getLogger(CompactedCache.class);

    private static final byte[] BINARY_NULL_VALUE = new ValueSerializationPair().serialize(NullValue.INSTANCE);
    private final String name;
    private final NativeCache nativeCache;

    public CompactedCache(String name, com.github.benmanes.caffeine.cache.Cache<Object, Object> caffieineCache,
                          AppRedisCacheWriter redisWriter, AppRedisCacheConfiguration redisConfig) {
        super(redisConfig.getAllowCacheNullValues());
        this.name = name;
        this.nativeCache = new NativeCacheImpl(name, caffieineCache, redisWriter, redisConfig);
    }

    @Override
    protected Object lookup(Object key) {
        if (_CacheSpecialKey.CACHE_REFRESHING.equals(key)) {
            return null;
        } else if (_CacheSpecialKey.FULL_CACHE_FETCHING.equals(key)) {
            if (this.nativeCache.estimatedSize() == 0) {
                return null;
            }
            return nativeCache.fetchAll();
        } else {
            return this.nativeCache.get(key);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public NativeCache getNativeCache() {
        return nativeCache;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        if (_CacheSpecialKey.FULL_CACHE_FETCHING.equals(key) || _CacheSpecialKey.CACHE_REFRESHING.equals(key)) {
            if (nativeCache.estimatedSize() <= 0 || _CacheSpecialKey.CACHE_REFRESHING.equals(key)) {
                try {
                    this.put(key, valueLoader.call());
                } catch (Exception ex) {
                    logger.error("Failed to load cache data.", ex);

                    throw new ValueRetrievalException(key, valueLoader, ex);
                }
            }
            return (T) fromStoreValue(nativeCache.fetchAll());
        } else {
            return (T) fromStoreValue(this.nativeCache.get(key, new CompactedCache.LoadFunction(valueLoader)));
        }
    }

    @Override
    public void put(Object key, Object value) {
        if (_CacheSpecialKey.CACHE_REFRESHING.equals(key) || _CacheSpecialKey.FULL_CACHE_FETCHING.equals(key)) {
            Map<?, ?> map = (Map<?, ?>) value;
            map.forEach((_key, _value) -> this.nativeCache.put(_key, toStoreValue(_value)));
        } else {
            this.nativeCache.put(key, toStoreValue(value));
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        if (_CacheSpecialKey.CACHE_REFRESHING.equals(key) || _CacheSpecialKey.FULL_CACHE_FETCHING.equals(key)) {
            if (this.nativeCache.estimatedSize() > 0 && _CacheSpecialKey.FULL_CACHE_FETCHING.equals(key)) {
                return null;
            } else {
                this.put(key, value);
                return toValueWrapper(nativeCache.fetchAll());
            }
        } else {
            CompactedCache.PutIfAbsentFunction callable = new CompactedCache.PutIfAbsentFunction(value);
            Object result = this.nativeCache.get(key, callable);
            return (callable.called ? null : toValueWrapper(result));
        }
    }

    @Override
    public void evict(Object key) {
        nativeCache.remove(key);
    }

    @Override
    public void clear() {
        nativeCache.clean();
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
