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

package com.thirdparty.service.impl;

import com.lifeonwalden.app.cache.constant.CacheSpecialKey;
import com.lifeonwalden.app.example.common.constant.CacheManager;
import com.lifeonwalden.app.example.common.constant.CacheName;
import com.lifeonwalden.app.util.date.DateUtil;
import com.lifeonwalden.app.util.logger.LoggerUtil;
import com.lifeonwalden.app.util.map.MapUtil;
import com.thirdparty.bean.DatabaseField;
import com.thirdparty.bean.DatabaseFieldParam;
import com.thirdparty.service.StoreService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StoreServiceImpl implements StoreService, InitializingBean {
    private final static Logger logger = LoggerUtil.getLogger(StoreServiceImpl.class);

    private Map<String, DatabaseField> cache = new HashMap<>();

    @Override
    @Cacheable(cacheManager = CacheManager.CACHE_MANAGER, cacheNames = CacheName.DB, key = "#param.requiredField")
    public DatabaseField get(DatabaseFieldParam param) {
        LoggerUtil.debug(logger, "get", param);

        return this.cache.get(param.getRequiredField());
    }

    @Override
    @CacheEvict(cacheManager = CacheManager.CACHE_MANAGER, cacheNames = CacheName.DB, key = "#param.requiredField")
    public boolean delete(DatabaseFieldParam param) {
        LoggerUtil.debug(logger, "delete", param);

        return null != cache.remove(param.getRequiredField());
    }

    @Override
    @CachePut(cacheManager = CacheManager.CACHE_MANAGER, cacheNames = CacheName.DB, key = "#param.requiredField")
    public boolean insert(DatabaseFieldParam param) {
        LoggerUtil.debug(logger, "insert", param);

        cache.put(param.getRequiredField(), MapUtil.shallowCopy(param, DatabaseField.class));
        return true;
    }

    @Override
    @CachePut(cacheManager = CacheManager.CACHE_MANAGER, cacheNames = CacheName.DB, key = "#param.requiredField")
    public boolean update(DatabaseFieldParam param) {
        LoggerUtil.debug(logger, "update", param);

        DatabaseField item = this.cache.get(param.getRequiredField());
        if (null != item) {
            MapUtil.merge(item, param);

            return true;
        }
        return false;
    }

    @Override
    @Cacheable(cacheManager = CacheManager.CACHE_MANAGER, cacheNames = CacheName.DB, key = CacheSpecialKey.FULL_CACHE_FETCHING)
    public Map<String, DatabaseField> queryAll() {
        LoggerUtil.debug(logger, "queryAll");

        return cache;
    }

    @Override
    @Cacheable(cacheManager = CacheManager.CACHE_MANAGER, cacheNames = CacheName.DB_LIST, key = "#param.createUser")
    public List<DatabaseField> query(DatabaseFieldParam param) {
        LoggerUtil.debug(logger, "query", param);

        List<DatabaseField> result = new ArrayList<>();
        this.cache.values().forEach(item -> {
            if (null != item.getCreateUser() && item.getCreateUser().equals(param.getCreateUser())) {
                result.add(item);
            }
        });

        return result;
    }

    @Override
    @Cacheable(cacheManager = CacheManager.CACHE_MANAGER, sync = true, cacheNames = CacheName.DB_LIST, key = CacheSpecialKey.FULL_CACHE_FETCHING)
    public Map<String, List<DatabaseField>> queryMapping() {
        LoggerUtil.debug(logger, "queryMapping");
        Map<String, List<DatabaseField>> mapping = new HashMap<>();

        this.cache.values().forEach(item -> {
            List<DatabaseField> list = mapping.get(item.getCreateUser());
            if (null == list) {
                list = new ArrayList<>();
                mapping.put(item.getCreateUser(), list);
            }
            list.add(item);
        });

        return mapping;
    }

    @Override
    @Cacheable(cacheManager = CacheManager.CACHE_MANAGER, sync = true, cacheNames = CacheName.DB_LIST, key = CacheSpecialKey.CACHE_REFRESHING)
    public Map<String, List<DatabaseField>> refreshMapping() {
        LoggerUtil.debug(logger, "refreshMapping");
        return queryMapping();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        DatabaseField dbField = create("1", DateUtil.parseDate("20170120", DateUtil.FULL_SHORT_DATE), DateUtil.parseDate("20170220", DateUtil.FULL_SHORT_DATE), DateUtil.parseDate("20170121", DateUtil.FULL_SHORT_DATE), "admin", 0, "flush a", DateUtil.parseDate("20170120", DateUtil.FULL_SHORT_DATE), DateUtil.parseDate("20170220", DateUtil.FULL_SHORT_DATE), DateUtil.parseDate("20170121", DateUtil.FULL_SHORT_DATE), "test");
        this.cache.put(dbField.getRequiredField(), dbField);

        dbField = create("2", DateUtil.parseDate("20170111", DateUtil.FULL_SHORT_DATE), DateUtil.parseDate("20170115", DateUtil.FULL_SHORT_DATE), DateUtil.parseDate("20170121", DateUtil.FULL_SHORT_DATE), "admin", 1, "flush b", DateUtil.parseDate("20170120", DateUtil.FULL_SHORT_DATE), DateUtil.parseDate("20170220", DateUtil.FULL_SHORT_DATE), DateUtil.parseDate("20170121", DateUtil.FULL_SHORT_DATE), "guest");
        this.cache.put(dbField.getRequiredField(), dbField);

        dbField = create("3", DateUtil.parseDate("20160111", DateUtil.FULL_SHORT_DATE), DateUtil.parseDate("20160115", DateUtil.FULL_SHORT_DATE), DateUtil.parseDate("20160121", DateUtil.FULL_SHORT_DATE), "test", 0, "push a", DateUtil.parseDate("20170120", DateUtil.FULL_SHORT_DATE), DateUtil.parseDate("20170220", DateUtil.FULL_SHORT_DATE), DateUtil.parseDate("20170121", DateUtil.FULL_SHORT_DATE), "guest");
        this.cache.put(dbField.getRequiredField(), dbField);

        dbField = create("4", DateUtil.parseDate("20150111", DateUtil.FULL_SHORT_DATE), DateUtil.parseDate("20150115", DateUtil.FULL_SHORT_DATE), DateUtil.parseDate("20150121", DateUtil.FULL_SHORT_DATE), "guest", 0, "push a", DateUtil.parseDate("20170120", DateUtil.FULL_SHORT_DATE), DateUtil.parseDate("20170220", DateUtil.FULL_SHORT_DATE), DateUtil.parseDate("20170121", DateUtil.FULL_SHORT_DATE), "admin");
        this.cache.put(dbField.getRequiredField(), dbField);
    }

    private DatabaseField create(String requiredField, Date createTime, Date createTimeEnd, Date createTimeStart, String createUser, int logicalDel, String optionalField, Date updateTime, Date updateTimeEnd, Date updateTimeStart, String updateUser) {
        DatabaseField dbField = new DatabaseField();
        dbField.setCreateTime(createTime).setCreateTimeEnd(createTimeEnd).setCreateTimeStart(createTimeStart).setCreateUser(createUser);
        dbField.setLogicalDel(logicalDel).setOptionalField(optionalField).setRequiredField(requiredField);
        dbField.setUpdateTime(updateTime).setUpdateTimeEnd(updateTimeEnd).setUpdateTimeStart(updateTimeStart).setUpdateUser(updateUser);

        return dbField;
    }
}
