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

import java.time.Duration;
import java.util.Map;
import java.util.Set;

/**
 * {@link RedisCacheWriter} provides low level access to Redis commands ({@code SET, SETNX, GET, EXPIRE,...}) used for
 * caching. <br />
 * The {@link RedisCacheWriter} may be shared by multiple cache implementations and is responsible for writing / reading
 * binary data to / from Redis. The implementation honors potential cache lock flags that might be set.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @author CManLH
 */
public interface RedisCacheWriter {

    /**
     * Write the given key/value pair to Redis an set the expiration time if defined.
     *
     * @param name  The cache name must not be {@literal null}.
     * @param key   The key for the cache entry. Must not be {@literal null}.
     * @param value The value stored for the key. Must not be {@literal null}.
     * @param ttl   Optional expiration time. Can be {@literal null}.
     */
    void put(byte[] name, byte[] key, byte[] value, Duration ttl);

    /**
     * Get the binary value representation from Redis stored for the given key.
     *
     * @param name must not be {@literal null}.
     * @param key  must not be {@literal null}.
     * @return {@literal null} if key does not exist.
     */
    byte[] get(byte[] name, byte[] key);

    /**
     * Write the given value to Redis if the key does not already exist.
     *
     * @param name  The cache name must not be {@literal null}.
     * @param key   The key for the cache entry. Must not be {@literal null}.
     * @param value The value stored for the key. Must not be {@literal null}.
     * @param ttl   Optional expiration time. Can be {@literal null}.
     * @return {@literal null} if the value has been written, the value stored for the key if it already exists.
     */
    byte[] putIfAbsent(byte[] name, byte[] key, byte[] value, Duration ttl);

    /**
     * Remove the given key from Redis.
     *
     * @param name The cache name must not be {@literal null}.
     * @param key  The key for the cache entry. Must not be {@literal null}.
     */
    void remove(byte[] name, byte[] key);

    /**
     * Write the given cache to Redis an set the expiration time if defined.
     *
     * @param name
     * @param value
     * @param ttl
     */
    void put(byte[] name, Map<byte[], byte[]> value, Duration ttl);

    /**
     * Get the binary value representation from Redis stored for the given key.
     *
     * @param name
     * @return
     */
    Map<byte[], byte[]> get(byte[] name);

    /**
     * Write the given cache to Redis if the cache does not already exist.
     *
     * @param name
     * @param value
     * @param ttl
     * @return
     */
    Map<byte[], byte[]> putIfAbsent(byte[] name, Map<byte[], byte[]> value, Duration ttl);

    /**
     * clean cache
     *
     * @param name The cache name must not be {@literal null}.
     */
    void clean(byte[] name);

    /**
     * list keys of cache $name
     *
     * @param name
     * @return
     */
    Set<byte[]> listKey(byte[] name);
}
