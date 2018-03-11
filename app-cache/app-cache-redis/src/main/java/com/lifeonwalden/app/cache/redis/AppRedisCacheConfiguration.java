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

import com.lifeonwalden.app.cache.redis.serialization.SerializationPair;
import com.lifeonwalden.app.cache.redis.serialization.impl.KeySerializationPair;
import com.lifeonwalden.app.cache.redis.serialization.impl.ValueSerializationPair;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class AppRedisCacheConfiguration {
    private final Duration ttl;
    private final boolean cacheNullValues;

    private final SerializationPair<Object> keySerializationPair;
    private final SerializationPair<Object> valueSerializationPair;

    public AppRedisCacheConfiguration(long duration, String timeUnit, Boolean cacheNullValues,
                                      SerializationPair<Object> keySerializationPair, SerializationPair<Object> valueSerializationPair) {
        this.ttl = Duration.ofMillis(TimeUnit.valueOf(timeUnit.toUpperCase()).toMillis(duration));
        this.cacheNullValues = cacheNullValues;
        this.keySerializationPair = keySerializationPair;
        this.valueSerializationPair = valueSerializationPair;
    }

    public AppRedisCacheConfiguration(long duration, String timeUnit) {
        this(duration, timeUnit, true, new KeySerializationPair(), new ValueSerializationPair());
    }

    public boolean getAllowCacheNullValues() {
        return cacheNullValues;
    }

    public SerializationPair<Object> getKeySerializationPair() {
        return keySerializationPair;
    }

    public SerializationPair<Object> getValueSerializationPair() {
        return valueSerializationPair;
    }

    public Duration getTtl() {
        return ttl;
    }
}
