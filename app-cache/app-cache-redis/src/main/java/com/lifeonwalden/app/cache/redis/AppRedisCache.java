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

import com.lifeonwalden.app.cache.constant._CacheSpecialKey;
import com.lifeonwalden.app.cache.redis.serialization.impl.ValueSerializationPair;
import com.lifeonwalden.app.util.logger.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.NullValue;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * {@link org.springframework.cache.Cache} implementation using for Redis as underlying store.
 * <p/>
 * Use {@link AppRedisCacheManager} to create {@link AppRedisCache} instances.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @author CManLH
 * @see AppRedisCacheConfiguration
 * @see AppRedisCacheWriter
 */
public class AppRedisCache extends AbstractValueAdaptingCache {
    private final static Logger logger = LoggerUtil.getLogger(AppRedisCache.class);

    private static final byte[] BINARY_NULL_VALUE = new ValueSerializationPair().serialize(NullValue.INSTANCE);

    private final byte[] name;
    private final AppRedisCacheWriter cacheWriter;
    private final AppRedisCacheConfiguration cacheConfig;

    /**
     * Create new {@link AppRedisCache}.
     *
     * @param name        must not be {@literal null}.
     * @param cacheWriter must not be {@literal null}.
     * @param cacheConfig must not be {@literal null}.
     */
    public AppRedisCache(String name, AppRedisCacheWriter cacheWriter, AppRedisCacheConfiguration cacheConfig) {
        super(cacheConfig.getAllowCacheNullValues());
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(cacheWriter, "CacheWriter must not be null!");
        Assert.notNull(cacheConfig, "CacheConfig must not be null!");

        this.cacheConfig = cacheConfig;
        this.name = serializeKey(name);
        this.cacheWriter = cacheWriter;
    }

    private static <T> T valueFromLoader(Object key, Callable<T> valueLoader) {
        try {
            return valueLoader.call();
        } catch (Exception e) {
            throw new ValueRetrievalException(key, valueLoader, e);
        }
    }

    @Override
    protected Object lookup(Object key) {
        if (_CacheSpecialKey.CACHE_REFRESHING.equals(key)) {
            return null;
        } else if (_CacheSpecialKey.FULL_CACHE_FETCHING.equals(key)) {
            if (!cacheWriter.exist(name) || cacheWriter.estimatedSize(name) == 0) {
                return null;
            }

            Map<String, Object> mapping = new HashMap<>();
            cacheWriter.get(name).forEach((_key, _value) -> mapping.put(deserializeKey(_key), deserializeValue(_value)));
            return mapping;
        } else {
            byte[] value = cacheWriter.get(name, serializeKey((String) key));

            if (value == null) {
                return null;
            }

            return deserializeValue(value);
        }
    }

    @Override
    public String getName() {
        return deserializeKey(name);
    }

    @Override
    public AppRedisCacheWriter getNativeCache() {
        return this.cacheWriter;
    }

    @Override
    public synchronized <T> T get(Object key, Callable<T> valueLoader) {
        if (_CacheSpecialKey.FULL_CACHE_FETCHING.equals(key) || _CacheSpecialKey.CACHE_REFRESHING.equals(key)) {
            if (cacheWriter.estimatedSize(name) <= 0 || _CacheSpecialKey.CACHE_REFRESHING.equals(key)) {
                try {
                    this.put(key, valueLoader.call());
                } catch (Exception ex) {
                    logger.error("Failed to load cache data.", ex);

                    throw new ValueRetrievalException(key, valueLoader, ex);
                }

            }
            Map<String, Object> mapping = new HashMap<>();
            cacheWriter.get(name).forEach((_key, _value) -> mapping.put(deserializeKey(_key), deserializeValue(_value)));
            return (T) fromStoreValue(mapping);
        } else {
            ValueWrapper result = get(key);
            if (result != null) {
                return (T) result.get();
            }
            T value = valueFromLoader(key, valueLoader);
            put(key, value);

            return value;
        }
    }

    @Override
    public void put(Object key, Object value) {
        if (_CacheSpecialKey.CACHE_REFRESHING.equals(key) || _CacheSpecialKey.FULL_CACHE_FETCHING.equals(key)) {
            Map<?, ?> map = (Map<?, ?>) value;
            map.forEach((_key, _value) -> cacheWriter.put(name, serializeKey((String) _key), serializeValue(toStoreValue(_value)), cacheConfig.getTtl()));
        } else {
            Object cacheValue = preProcessCacheValue(value);

            if (!isAllowNullValues() && cacheValue == null) {
                throw new IllegalArgumentException(String.format(
                        "Cache '%s' does not allow 'null' values. Avoid storing null via '@Cacheable(unless=\"#result == null\")' or configure AppRedisCache to allow 'null' via AppRedisCacheConfiguration.",
                        name));
            }

            cacheWriter.put(name, serializeKey((String) key), serializeValue(cacheValue), cacheConfig.getTtl());
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        if (_CacheSpecialKey.CACHE_REFRESHING.equals(key) || _CacheSpecialKey.FULL_CACHE_FETCHING.equals(key)) {
            if ((!cacheWriter.exist(name) || cacheWriter.estimatedSize(name) > 0) && _CacheSpecialKey.FULL_CACHE_FETCHING.equals(key)) {
                return null;
            } else {
                this.put(key, value);
                Map<String, Object> mapping = new HashMap<>();
                cacheWriter.get(name).forEach((_key, _value) -> mapping.put(deserializeKey(_key), deserializeValue(_value)));

                return toValueWrapper(mapping);
            }
        } else {
            Object cacheValue = preProcessCacheValue(value);
            if (!isAllowNullValues() && cacheValue == null) {
                return get(key);
            }

            byte[] result = cacheWriter.putIfAbsent(name, serializeKey((String) key), serializeValue(cacheValue),
                    cacheConfig.getTtl());
            if (result == null) {
                return null;
            }
            return new SimpleValueWrapper(fromStoreValue(deserializeValue(result)));
        }
    }

    @Override
    public void evict(Object key) {
        cacheWriter.remove(name, serializeKey((String) key));
    }

    @Override
    public void clear() {
        cacheWriter.clean(name);
    }

    /**
     * Get {@link AppRedisCacheConfiguration} used.
     *
     * @return immutable {@link AppRedisCacheConfiguration}. Never {@literal null}.
     */
    public AppRedisCacheConfiguration getCacheConfiguration() {
        return cacheConfig;
    }

    /**
     * Customization hook called before passing object to
     * {@link org.springframework.data.redis.serializer.RedisSerializer}.
     *
     * @param value can be {@literal null}.
     * @return preprocessed value. Can be {@literal null}.
     */
    protected Object preProcessCacheValue(Object value) {
        if (value != null) {
            return value;
        }

        return isAllowNullValues() ? NullValue.INSTANCE : null;
    }

    protected byte[] serializeKey(String key) {
        return this.cacheConfig.getKeySerializationPair().serialize(key);
    }

    protected String deserializeKey(byte[] key) {
        return this.cacheConfig.getKeySerializationPair().deserialize(key);
    }

    protected byte[] serializeValue(Object value) {
        if (isAllowNullValues() && value instanceof NullValue) {
            return BINARY_NULL_VALUE;
        }

        return cacheConfig.getValueSerializationPair().serialize(value);
    }

    protected Object deserializeValue(byte[] value) {
        if (isAllowNullValues() && ObjectUtils.nullSafeEquals(value, BINARY_NULL_VALUE)) {
            return NullValue.INSTANCE;
        }

        return cacheConfig.getValueSerializationPair().deserialize(value);
    }

    public List<String> listKey() {
        List<String> keyList = new ArrayList<>();
        getNativeCache().listKey(name).forEach(key -> keyList.add(deserializeKey(key)));

        return keyList;
    }
}
