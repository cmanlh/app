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
package com.lifeonwalden.app.cache.redis;

import com.lifeonwalden.app.cache.service.CacheService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis cache manager
 *
 * @author CManLH
 */
public class AppRedisCacheManager implements CacheManager, CacheService {
    private final AppRedisCacheWriter cacheWriter;
    private final AppRedisCacheConfiguration defaultCacheConfig;
    private final Map<String, Cache> cacheMap = new ConcurrentHashMap<>(16);
    private boolean dynamic = true;


    public AppRedisCacheManager(AppRedisCacheWriter cacheWriter, AppRedisCacheConfiguration defaultCacheConfiguration) {
        Assert.notNull(cacheWriter, "CacheWriter must not be null!");
        Assert.notNull(defaultCacheConfiguration, "DefaultCacheConfiguration must not be null!");

        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

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

    @Override
    public List<String> listCache() {
        return new ArrayList<>(getCacheNames());
    }

    @Override
    public List<String> listKey(String name) {
        return ((AppRedisCache) getCache(name)).listKey();
    }

    @Override
    public boolean evict(String name, String key) {
        getCache(name).evict(key);

        return true;
    }

    @Override
    public boolean clear(String name) {
        getCache(name).clear();

        return true;
    }
}
