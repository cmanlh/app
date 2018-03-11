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

import java.time.Duration;
import java.util.Map;
import java.util.Set;

public interface AppRedisCacheWriter {

    void put(byte[] name, byte[] key, byte[] value, Duration ttl);

    byte[] get(byte[] name, byte[] key);

    byte[] putIfAbsent(byte[] name, byte[] key, byte[] value, Duration ttl);

    void remove(byte[] name, byte[] key);

    void put(byte[] name, Map<byte[], byte[]> value, Duration ttl);

    Map<byte[], byte[]> get(byte[] name);

    Map<byte[], byte[]> putIfAbsent(byte[] name, Map<byte[], byte[]> value, Duration ttl);

    void clean(byte[] name);

    Set<byte[]> listKey(byte[] name);

    long estimatedSize(byte[] name);

    boolean exist(byte[] name);
}
