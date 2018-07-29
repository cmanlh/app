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

package com.lifeonwalden.app.gateway.auth.session;

import com.lifeonwalden.app.util.logger.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;

public class RedisSessionDao extends CachingSessionDAO {
    private final static Logger logger = LoggerUtil.getLogger(RedisSessionDao.class);
    private RedisConnectionFactory redisConnectionFactory;
    private String keyPrefix = "sessionId";

    public RedisConnectionFactory getRedisConnectionFactory() {
        return redisConnectionFactory;
    }

    public void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    protected Serializable doCreate(Session session) {
        LoggerUtil.debug(logger, "doCreate", session);

        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        storeSession(session);

        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        LoggerUtil.debug(logger, "doReadSession", sessionId);

        byte[] sessionData = redisConnectionFactory.getConnection().get(keyPrefix.concat(sessionId.toString()).getBytes(Charset.forName("UTF-8")));

        if (null == sessionData || sessionData.length == 0) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(sessionData))) {
            return AppSessionHelper.readObject(ois);
        } catch (IOException e) {
            logger.error("Failed to read session from Redis.", e);
        } catch (ClassNotFoundException e) {
            logger.error("Failed to read session from Redis.", e);
        }

        return null;
    }

    @Override
    protected void doDelete(Session session) {
        LoggerUtil.debug(logger, "doDelete", session);

        if (session == null) {
            throw new NullPointerException("session argument cannot be null.");
        }
        Serializable id = session.getId();
        if (id != null) {
            redisConnectionFactory.getConnection().del(keyPrefix.concat(session.getId().toString()).getBytes(Charset.forName("UTF-8")));
        }
    }

    @Override
    protected void doUpdate(Session session) {
        LoggerUtil.debug(logger, "doUpdate", session);

        storeSession(session);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        LoggerUtil.debug(logger, "getActiveSessions");

        return Collections.emptyList();
    }

    private void storeSession(Session session) {
        LoggerUtil.debug(logger, "storeSession", session);

        if (session.getId() == null) {
            throw new NullPointerException("id argument cannot be null.");
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(256); ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream)) {
            AppSessionHelper.writeObject(oos, (SimpleSession) session);
            redisConnectionFactory.getConnection().set(keyPrefix.concat(session.getId().toString()).getBytes(Charset.forName("UTF-8")), byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            logger.error("Failed to store session to Redis.", e);
        }
    }
}
