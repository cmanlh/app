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

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.lifeonwalden.app.cache.service.CacheService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * {@link CacheManager} implementation that lazily builds {@link AppCaffeineCache}
 * instances for each {@link #getCache} request. Also supports a 'static' mode
 * where the set of cache names is pre-defined through {@link #setCacheNames},
 * with no dynamic creation of further cache regions at runtime.
 * <p>
 * <p>The configuration of the underlying cache can be fine-tuned through a
 * {@link Caffeine} builder or {@link CaffeineSpec}, passed into this
 * CacheManager through {@link #setCaffeine}/{@link #setCaffeineSpec}.
 * A {@link CaffeineSpec}-compliant expression value can also be applied
 * via the {@link #setCacheSpecification "cacheSpecification"} bean property.
 * <p>
 * <p>Requires Caffeine 2.1 or higher.
 * <p>
 * Add feature:
 * allowed to fetch the whole cache as map
 *
 * @author Ben Manes
 * @author Juergen Hoeller
 * @author Stephane Nicoll
 * @author CManLH
 * @see AppCaffeineCache
 */
public class AppCaffeineCacheManager implements CacheManager, CacheService {
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>(16);
    private boolean dynamic = true;
    private Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
    private CacheLoader<Object, Object> cacheLoader;
    private boolean allowNullValues = true;

    public AppCaffeineCacheManager() {
    }

    /**
     * Construct a static AppCaffeineCacheManager,
     * managing caches for the specified cache names only.
     */
    public AppCaffeineCacheManager(String... cacheNames) {
        setCacheNames(Arrays.asList(cacheNames));
    }

    /**
     * Construct a AppCaffeineCacheManager managing caches with expireAfterWrite feature
     *
     * @param duration
     * @param timeUnit
     */
    public AppCaffeineCacheManager(long duration, String timeUnit) {
        this.setCaffeine(Caffeine.newBuilder().expireAfterWrite(duration, TimeUnit.valueOf(timeUnit.toUpperCase())));
    }

    /**
     * Set the Caffeine to use for building each individual
     * {@link AppCaffeineCache} instance.
     *
     * @see #createNativeCaffeineCache
     * @see com.github.benmanes.caffeine.cache.Caffeine#build()
     */
    public void setCaffeine(Caffeine<Object, Object> caffeine) {
        Assert.notNull(caffeine, "Caffeine must not be null");
        doSetCaffeine(caffeine);
    }

    /**
     * Set the {@link CaffeineSpec} to use for building each individual
     * {@link AppCaffeineCache} instance.
     *
     * @see #createNativeCaffeineCache
     * @see com.github.benmanes.caffeine.cache.Caffeine#from(CaffeineSpec)
     */
    public void setCaffeineSpec(CaffeineSpec caffeineSpec) {
        doSetCaffeine(Caffeine.from(caffeineSpec));
    }

    /**
     * Set the Caffeine cache specification String to use for building each
     * individual {@link AppCaffeineCache} instance. The given value needs to
     * comply with Caffeine's {@link CaffeineSpec} (see its javadoc).
     *
     * @see #createNativeCaffeineCache
     * @see com.github.benmanes.caffeine.cache.Caffeine#from(String)
     */
    public void setCacheSpecification(String cacheSpecification) {
        doSetCaffeine(Caffeine.from(cacheSpecification));
    }

    /**
     * Set the Caffeine CacheLoader to use for building each individual
     * {@link AppCaffeineCache} instance, turning it into a LoadingCache.
     *
     * @see #createNativeCaffeineCache
     * @see com.github.benmanes.caffeine.cache.Caffeine#build(CacheLoader)
     * @see com.github.benmanes.caffeine.cache.LoadingCache
     */
    public void setCacheLoader(CacheLoader<Object, Object> cacheLoader) {
        if (!ObjectUtils.nullSafeEquals(this.cacheLoader, cacheLoader)) {
            this.cacheLoader = cacheLoader;
            refreshKnownCaches();
        }
    }

    /**
     * Return whether this cache manager accepts and converts {@code null} values
     * for all of its caches.
     */
    public boolean isAllowNullValues() {
        return this.allowNullValues;
    }

    /**
     * Specify whether to accept and convert {@code null} values for all caches
     * in this cache manager.
     * <p>Default is "true", despite Caffeine itself not supporting {@code null} values.
     * An internal holder object will be used to store user-level {@code null}s.
     */
    public void setAllowNullValues(boolean allowNullValues) {
        if (this.allowNullValues != allowNullValues) {
            this.allowNullValues = allowNullValues;
            refreshKnownCaches();
        }
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }

    /**
     * Specify the set of cache names for this CacheManager's 'static' mode.
     * <p>The number of caches and their names will be fixed after a call to this method,
     * with no creation of further cache regions at runtime.
     * <p>Calling this with a {@code null} collection argument resets the
     * mode to 'dynamic', allowing for further creation of caches again.
     */
    public void setCacheNames(Collection<String> cacheNames) {
        if (cacheNames != null) {
            for (String name : cacheNames) {
                this.cacheMap.put(name, createCaffeineCache(name));
            }
            this.dynamic = false;
        } else {
            this.dynamic = true;
        }
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = this.cacheMap.get(name);
        if (cache == null && this.dynamic) {
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(name);
                if (cache == null) {
                    cache = createCaffeineCache(name);
                    this.cacheMap.put(name, cache);
                }
            }
        }
        return cache;
    }

    /**
     * Create a new CaffeineCache instance for the specified cache name.
     *
     * @param name the name of the cache
     * @return the Spring CaffeineCache adapter (or a decorator thereof)
     */
    protected Cache createCaffeineCache(String name) {
        return new AppCaffeineCache(name, createNativeCaffeineCache(name), isAllowNullValues());
    }

    /**
     * Create a native Caffeine Cache instance for the specified cache name.
     *
     * @param name the name of the cache
     * @return the native Caffeine Cache instance
     */
    protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name) {
        if (this.cacheLoader != null) {
            return this.cacheBuilder.build(this.cacheLoader);
        } else {
            return this.cacheBuilder.build();
        }
    }

    private void doSetCaffeine(Caffeine<Object, Object> cacheBuilder) {
        if (!ObjectUtils.nullSafeEquals(this.cacheBuilder, cacheBuilder)) {
            this.cacheBuilder = cacheBuilder;
            refreshKnownCaches();
        }
    }

    /**
     * Create the known caches again with the current state of this manager.
     */
    private void refreshKnownCaches() {
        for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet()) {
            entry.setValue(createCaffeineCache(entry.getKey()));
        }
    }

    @Override
    public List<String> listCache() {
        return new ArrayList<>(this.getCacheNames());
    }

    @Override
    public List<String> listKey(String name) {
        AppCaffeineCache appCaffeineCache = (AppCaffeineCache) getCache(name);
        if (null == appCaffeineCache) {
            return null;
        }

        Set<Object> keySet = appCaffeineCache.getNativeCache().asMap().keySet();
        return Arrays.asList(keySet.toArray(new String[keySet.size()]));
    }

    @Override
    public boolean evict(String name, String key) {
        Cache cache = this.getCache(name);
        if (null != cache) {
            cache.evict(key);
            return true;
        }
        return false;
    }

    @Override
    public boolean clear(String name) {
        Cache cache = this.getCache(name);
        if (null != cache) {
            cache.clear();
            return true;
        }
        return false;
    }
}
