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

package com.lifeonwalden.app.cache.service.impl;

import com.lifeonwalden.app.cache.service.CacheManagementService;
import com.lifeonwalden.app.cache.service.bean.CacheManagementBean;
import com.lifeonwalden.app.util.logger.LoggerUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheManagementServiceImpl implements CacheManagementService, InitializingBean {
    private final static Logger logger = LoggerUtil.getLogger(CacheManagementServiceImpl.class);

    private Map<String, CacheManagementBean> index = new HashMap<>();

    private List<CacheManagementBean> cacheManagementList;

    private List<String> NULL = new ArrayList<>();

    public List<CacheManagementBean> getCacheManagementList() {
        return cacheManagementList;
    }

    public void setCacheManagementList(List<CacheManagementBean> cacheManagementList) {
        this.cacheManagementList = cacheManagementList;
    }

    @Override
    public List<CacheManagementBean> listCacheManagement() {
        logger.debug("listCache");

        return getCacheManagementList();
    }

    @Override
    public List<String> listCache(String cacheManagementId) {
        logger.debug("listCache for {}", cacheManagementId);

        CacheManagementBean cacheManagementBean = this.index.get(cacheManagementId);

        if (null != cacheManagementBean) {
            return cacheManagementBean.getCache().listCache();
        } else {
            return NULL;
        }
    }

    @Override
    public List<String> listKey(String cacheManagementId, String cacheName) {
        logger.debug("listKey for {} : {}", cacheManagementId, cacheName);

        CacheManagementBean cacheManagementBean = this.index.get(cacheManagementId);

        if (null != cacheManagementBean) {
            List<String> keyList = cacheManagementBean.getCache().listKey(cacheName);

            return keyList == null ? NULL : keyList;
        } else {
            return NULL;
        }
    }

    @Override
    public boolean evict(String cacheManagementId, String cacheName, String key) {
        logger.debug("listKey for {} : {} : {}", cacheManagementId, cacheName, key);
        CacheManagementBean cacheManagementBean = this.index.get(cacheManagementId);

        if (null != cacheManagementBean) {
            return cacheManagementBean.getCache().evict(cacheName, key);
        } else {
            return false;
        }
    }

    @Override
    public boolean clear(String cacheManagementId, String cacheName) {
        logger.debug("listKey for {} : {} : {}", cacheManagementId, cacheName);

        CacheManagementBean cacheManagementBean = this.index.get(cacheManagementId);

        if (null != cacheManagementBean) {
            return cacheManagementBean.getCache().clear(cacheName);
        } else {
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        getCacheManagementList().forEach(cacheManagement -> {
            if (StringUtils.isAnyEmpty(cacheManagement.getId(), cacheManagement.getName())) {
                String errMsg = "Cache has to have an unique id and name";
                logger.error(errMsg);
                throw new RuntimeException(errMsg);
            }

            if (null == cacheManagement.getCache()) {
                String errMsg = "Please reference to a valid cache";
                logger.error(errMsg);
                throw new RuntimeException(errMsg);
            }

            if (this.index.containsKey(cacheManagement.getId())) {
                String errMsg = "Duplicated Id : ".concat(cacheManagement.getId());
                logger.error(errMsg);
                throw new RuntimeException(errMsg);
            }

            this.index.put(cacheManagement.getId(), cacheManagement);
        });
    }
}
