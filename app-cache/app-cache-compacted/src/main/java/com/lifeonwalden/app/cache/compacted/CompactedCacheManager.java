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

import com.github.benmanes.caffeine.cache.Caffeine;
import com.lifeonwalden.app.cache.redis.AppRedisCacheConfiguration;
import com.lifeonwalden.app.cache.redis.AppRedisCacheWriter;
import com.lifeonwalden.app.cache.service.CacheService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CompactedCacheManager implements CacheManager, CacheService {
    private final AppRedisCacheWriter redisWriter;
    private final AppRedisCacheConfiguration redisConfig;
    private final Map<String, Cache> cacheMap = new ConcurrentHashMap<>(16);
    private Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder();
    private boolean dynamic = true;

    public CompactedCacheManager(AppRedisCacheWriter redisWriter, AppRedisCacheConfiguration redisConfig) {
        this.redisWriter = redisWriter;
        this.redisConfig = redisConfig;
    }

    @Override
    public List<String> listCache() {
        return new ArrayList<>(cacheMap.keySet());
    }

    @Override
    public List<String> listKey(String name) {
        return ((CompactedCache) getCache(name)).getNativeCache().listKeys();
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

    protected CompactedCache createRedisCache(String name, AppRedisCacheConfiguration cacheConfig) {
        AppRedisCacheConfiguration instanse = cacheConfig != null ? cacheConfig : redisConfig;
        if (instanse.getTtl().toMillis() == 0) {
            return new CompactedCache(name, caffeineBuilder.build(), redisWriter, instanse);
        } else {
            return new CompactedCache(name, Caffeine.newBuilder().expireAfterWrite(instanse.getTtl().toMillis(), TimeUnit.MILLISECONDS).build(), redisWriter, instanse);
        }
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = this.cacheMap.get(name);
        if (cache == null && this.dynamic) {
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(name);
                if (cache == null) {
                    cache = createRedisCache(name, redisConfig);
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
