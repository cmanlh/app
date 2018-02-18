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

import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * {@link AppRedisCacheWriter} implementation capable of reading/writing binary data from/to Redis in {@literal standalone}
 * and {@literal cluster} environments. Works upon a given {@link RedisConnectionFactory} to obtain the actual
 * {@link RedisConnection}. <br />
 * {@link DefaultAppRedisCacheWriter} can be used in
 * {@link AppRedisCacheWriter# locking} or
 * {@link AppRedisCacheWriter# non-locking} mode. While
 * {@literal non-locking} aims for maximum performance it may result in overlapping, non atomic, command execution for
 * operations spanning multiple Redis interactions like {@code putIfAbsent}. The {@literal locking} counterpart prevents
 * command overlap by setting an explicit lock key and checking against presence of this key which leads to additional
 * requests and potential command wait times.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @author CManLH
 */
public class DefaultAppRedisCacheWriter implements AppRedisCacheWriter {
    private final RedisConnectionFactory connectionFactory;
    private final Duration sleepTime;
    private final byte[] REDIS_LOCK_HOLDER = "~~CACHE~LOCK~~".getBytes(StandardCharsets.UTF_8);

    /**
     * @param connectionFactory must not be {@literal null}.
     */
    DefaultAppRedisCacheWriter(RedisConnectionFactory connectionFactory) {
        this(connectionFactory, 0, TimeUnit.MILLISECONDS.toString());
    }

    /**
     * @param connectionFactory must not be {@literal null}.
     * @param sleepTime         sleep time between lock request attempts. Must not be {@literal null}. Use {@link Duration#ZERO}
     *                          to disable locking.
     */
    DefaultAppRedisCacheWriter(RedisConnectionFactory connectionFactory, long sleepTime, String timeUnit) {
        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
        Assert.notNull(sleepTime, "SleepTime must not be null!");

        this.connectionFactory = connectionFactory;
        this.sleepTime = Duration.ofMillis(TimeUnit.valueOf(timeUnit.toUpperCase()).toMillis(sleepTime));
    }

    private static boolean shouldExpireWithin(Duration ttl) {
        return ttl != null && !ttl.isZero() && !ttl.isNegative();
    }

    @Override
    public void put(byte[] name, byte[] key, byte[] value, Duration ttl) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");

        execute(name, connection -> {
            connection.hSet(name, key, value);

            if (shouldExpireWithin(ttl)) {
                connection.expire(name, Expiration.from(ttl.toMillis(), TimeUnit.MILLISECONDS).getExpirationTimeInSeconds());
            }

            return "OK";
        });
    }

    @Override
    public byte[] get(byte[] name, byte[] key) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");

        return execute(name, connection -> connection.hGet(name, key));
    }

    @Override
    public byte[] putIfAbsent(byte[] name, byte[] key, byte[] value, Duration ttl) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");

        return execute(name, connection -> {
            if (isLockingCacheWriter()) {
                doLock(name, connection);
            }
            try {
                if (connection.hSetNX(name, key, value)) {
                    if (shouldExpireWithin(ttl)) {
                        connection.pExpire(name, ttl.toMillis());
                    }
                    return null;
                }

                return connection.hGet(name, key);
            } finally {
                if (isLockingCacheWriter()) {
                    doUnlock(name, connection);
                }
            }
        });
    }

    @Override
    public void remove(byte[] name, byte[] key) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");

        execute(name, connection -> connection.del(name, key));
    }

    @Override
    public void put(byte[] name, Map<byte[], byte[]> value, Duration ttl) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(value, "Value must not be null!");

        execute(name, connection -> {
            if (isLockingCacheWriter()) {
                doLock(name, connection);
            }
            try {
                connection.openPipeline();
                value.forEach((_key, _value) -> connection.hSet(name, _key, _value));

                if (shouldExpireWithin(ttl)) {
                    connection.expire(name, Expiration.from(ttl.toMillis(), TimeUnit.MILLISECONDS).getExpirationTimeInSeconds());
                }
                connection.closePipeline();

                return "OK";
            } finally {
                if (isLockingCacheWriter()) {
                    doUnlock(name, connection);
                }
            }
        });
    }

    @Override
    public Map<byte[], byte[]> get(byte[] name) {
        Assert.notNull(name, "Name must not be null!");

        return execute(name, connection -> connection.hGetAll(name));
    }

    @Override
    public Map<byte[], byte[]> putIfAbsent(byte[] name, Map<byte[], byte[]> value, Duration ttl) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(value, "Value must not be null!");

        return execute(name, connection -> {
            if (isLockingCacheWriter()) {
                doLock(name, connection);
            }
            try {
                if (!connection.exists(name)) {
                    connection.openPipeline();
                    value.forEach((_key, _value) -> connection.hSet(name, _key, _value));

                    if (shouldExpireWithin(ttl)) {
                        connection.pExpire(name, ttl.toMillis());
                    }
                    connection.closePipeline();
                    return null;
                }

                return connection.hGetAll(name);
            } finally {
                if (isLockingCacheWriter()) {
                    doUnlock(name, connection);
                }
            }
        });
    }

    @Override
    public void clean(byte[] name) {
        Assert.notNull(name, "Name must not be null!");

        execute(name, connection -> connection.del(name));
    }

    @Override
    public Set<byte[]> listKey(byte[] name) {
        Assert.notNull(name, "Name must not be null!");

        return execute(name, connection -> connection.hKeys(name));

    }

    @Override
    public long estimatedSize(byte[] name) {
        return execute(name, connection -> connection.hLen(name));
    }

    @Override
    public boolean exist(byte[] name) {
        return execute(name, connection -> connection.exists(name));
    }

    /**
     * Explicitly set a write lock on a cache.
     *
     * @param name the name of the cache to lock.
     */
    void lock(byte[] name) {
        execute(name, connection -> doLock(name, connection));
    }

    /**
     * Explicitly remove a write lock from a cache.
     *
     * @param name the name of the cache to unlock.
     */
    void unlock(byte[] name) {
        executeLockFree(connection -> doUnlock(name, connection));
    }

    private Boolean doLock(byte[] name, RedisConnection connection) {
        return connection.hSetNX(REDIS_LOCK_HOLDER, name, new byte[0]);
    }

    private Long doUnlock(byte[] name, RedisConnection connection) {
        return connection.hDel(REDIS_LOCK_HOLDER, name);
    }

    boolean doCheckLock(byte[] name, RedisConnection connection) {
        return connection.hExists(REDIS_LOCK_HOLDER, name);
    }

    /**
     * @return {@literal true} if {@link AppRedisCacheWriter} uses locks.
     */
    private boolean isLockingCacheWriter() {
        return !sleepTime.isZero() && !sleepTime.isNegative();
    }

    private <T> T execute(byte[] name, Function<RedisConnection, T> callback) {

        RedisConnection connection = connectionFactory.getConnection();
        try {
            checkAndPotentiallyWaitUntilUnlocked(name, connection);
            return callback.apply(connection);
        } finally {
            connection.close();
        }
    }

    private void executeLockFree(Consumer<RedisConnection> callback) {
        RedisConnection connection = connectionFactory.getConnection();

        try {
            callback.accept(connection);
        } finally {
            connection.close();
        }
    }

    private void checkAndPotentiallyWaitUntilUnlocked(byte[] name, RedisConnection connection) {
        if (!isLockingCacheWriter()) {
            return;
        }

        try {
            while (doCheckLock(name, connection)) {
                Thread.sleep(sleepTime.toMillis());
            }
        } catch (InterruptedException ex) {
            // Re-interrupt current thread, to allow other participants to react.
            Thread.currentThread().interrupt();

            throw new PessimisticLockingFailureException(String.format("Interrupted while waiting to unlock cache %s", name),
                    ex);
        }
    }
}
