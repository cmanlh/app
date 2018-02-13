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
package com.lifeonwadlen.app.cache.redis;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis cache manager
 *
 * @author CManLH
 */
public class RedisCacheManager implements CacheManager {
    private final RedisCacheWriter cacheWriter;
    private final AppRedisCacheConfiguration defaultCacheConfig;
    private final Map<String, Cache> cacheMap = new ConcurrentHashMap<>(16);
    private boolean dynamic = true;


    /**
     * @param cacheWriter
     * @param defaultCacheConfiguration
     */
    public RedisCacheManager(RedisCacheWriter cacheWriter, AppRedisCacheConfiguration defaultCacheConfiguration) {
        Assert.notNull(cacheWriter, "CacheWriter must not be null!");
        Assert.notNull(defaultCacheConfiguration, "DefaultCacheConfiguration must not be null!");

        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    /**
     * Configuration hook for creating {@link AppRedisCache} with given name and {@code cacheConfig}.
     *
     * @param name        must not be {@literal null}.
     * @param cacheConfig can be {@literal null}.
     * @return never {@literal null}.
     */
    protected AppRedisCache createRedisCache(String name, AppRedisCacheConfiguration cacheConfig) {
        return new AppRedisCache(name, cacheWriter, cacheConfig != null ? cacheConfig : defaultCacheConfig);
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = this.cacheMap.get(name);
        if (cache == null && this.dynamic) {
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(name);
                if (cache == null) {
                    cache = createRedisCache(name, defaultCacheConfig);
                    this.cacheMap.put(name, cache);
                }
            }
        }
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }
}
