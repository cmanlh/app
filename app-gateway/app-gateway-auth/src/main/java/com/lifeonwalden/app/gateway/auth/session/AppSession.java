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
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class AppSession implements Session {
    private final static Logger logger = LoggerUtil.getLogger(AppSession.class);

    private Serializable id;
    private Date startTimestamp;
    private Date stopTimestamp;
    private Date lastAccessTime;
    private long timeout;
    private String host;
    private ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<>();

    public AppSession() {
        this.timeout = DefaultSessionManager.DEFAULT_GLOBAL_SESSION_TIMEOUT;
        this.startTimestamp = new Date();
        this.lastAccessTime = this.startTimestamp;
    }

    public AppSession(String host) {
        this();
        this.host = host;
    }

    @Override
    public Serializable getId() {
        return this.id;
    }

    @Override
    public Date getStartTimestamp() {
        return this.startTimestamp;
    }

    @Override
    public Date getLastAccessTime() {
        return this.lastAccessTime;
    }

    @Override
    public long getTimeout() throws InvalidSessionException {
        return this.timeout;
    }

    @Override
    public void setTimeout(long maxIdleTimeInMillis) throws InvalidSessionException {
        this.timeout = maxIdleTimeInMillis;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public void touch() throws InvalidSessionException {
        this.lastAccessTime = new Date();
    }

    @Override
    public void stop() throws InvalidSessionException {
        if (this.stopTimestamp == null) {
            this.stopTimestamp = new Date();
        }
    }

    @Override
    public Collection<Object> getAttributeKeys() throws InvalidSessionException {
        return this.attributes.keySet();
    }

    @Override
    public Object getAttribute(Object key) throws InvalidSessionException {
        return attributes.get(key);
    }

    @Override
    public void setAttribute(Object key, Object value) throws InvalidSessionException {
        if (null == value) {
            removeAttribute(key);
        } else {
            this.attributes.put(key, value);
        }
    }

    @Override
    public Object removeAttribute(Object key) throws InvalidSessionException {
        return this.attributes.remove(key);
    }
}
