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

import com.lifeonwalden.app.cache.redis.AppRedisCacheConfiguration;
import com.lifeonwalden.app.cache.redis.AppRedisCacheWriter;
import com.lifeonwalden.app.util.map.MapUtil;

import java.util.*;
import java.util.function.Function;

public class NativeCacheImpl implements NativeCache {
    private final byte[] name;
    private final com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache;
    private final AppRedisCacheWriter redisWriter;
    private final AppRedisCacheConfiguration redisConfig;

    public NativeCacheImpl(String name, com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache,
                           AppRedisCacheWriter redisWriter, AppRedisCacheConfiguration redisConfig) {
        this.caffeineCache = caffeineCache;
        this.redisWriter = redisWriter;
        this.redisConfig = redisConfig;
        this.name = redisConfig.getKeySerializationPair().serialize(name);
    }

    @Override
    public Object fetchAll() {
        if (this.caffeineCache.estimatedSize() > 0) {
            return MapUtil.shallowCopy(this.caffeineCache.asMap(), HashMap.class);
        } else {
            if (redisWriter.exist(name)) {
                Map<Object, Object> mapping = new HashMap<>();
                redisWriter.get(name).forEach((_key, _value) -> {
                    Object __key = redisConfig.getKeySerializationPair().deserialize(_key);
                    Object __value = redisConfig.getValueSerializationPair().deserialize(_value);
                    this.caffeineCache.put(__key, __value);
                    mapping.put(__key, __value);
                });
                return mapping;
            } else {
                return null;
            }
        }
    }

    @Override
    public Object get(Object key) {
        Object cached = caffeineCache.getIfPresent(key);
        if (null != cached) {
            return cached;
        }

        byte[] byteCached = redisWriter.get(name, redisConfig.getKeySerializationPair().serialize(key));
        if (null != byteCached) {
            Object __value = redisConfig.getValueSerializationPair().deserialize(byteCached);
            caffeineCache.put(key, __value);
            return __value;
        } else {
            return null;
        }
    }

    @Override
    public Object get(Object key, Function<Object, Object> mappingFunction) {
        Object cached = get(key);
        if (null != cached) {
            return cached;
        }

        Object target = mappingFunction.apply(key);
        caffeineCache.put(key, target);
        redisWriter.put(name, redisConfig.getKeySerializationPair().serialize(key), redisConfig.getValueSerializationPair().serialize(target), redisConfig.getTtl());
        return target;
    }

    @Override
    public void put(Object key, Object value) {
        caffeineCache.put(key, value);
        redisWriter.put(name, redisConfig.getKeySerializationPair().serialize(key), redisConfig.getValueSerializationPair().serialize(value), redisConfig.getTtl());
    }

    @Override
    public void remove(Object key) {
        caffeineCache.invalidate(key);
        redisWriter.remove(name, redisConfig.getKeySerializationPair().serialize(key));
    }

    @Override
    public void clean() {
        caffeineCache.invalidateAll();
        redisWriter.clean(name);
    }

    @Override
    public long estimatedSize() {
        long estimatedSize = caffeineCache.estimatedSize();
        return estimatedSize > 0 ? estimatedSize : redisWriter.estimatedSize(name);
    }

    @Override
    public List<String> listKeys() {
        Set<Object> keySet = caffeineCache.asMap().keySet();
        if (null == keySet || keySet.size() == 0) {
            List<String> keyList = new ArrayList<>();
            redisWriter.listKey(name).forEach(key -> keyList.add(String.valueOf(redisConfig.getKeySerializationPair().deserialize(key))));
            return keyList;
        } else {
            List<String> keyList = new ArrayList<>();
            keyList.forEach(key -> keyList.add(String.valueOf(key)));
            return keyList;
        }
    }
}
