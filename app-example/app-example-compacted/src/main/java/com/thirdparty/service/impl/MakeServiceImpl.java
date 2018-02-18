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
import com.lifeonwalden.app.util.logger.LoggerUtil;
import com.thirdparty.bean.Enable;
import com.thirdparty.bean.EnableParam;
import com.thirdparty.service.MakeService;
import com.thirdparty.service.SessionService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MakeServiceImpl implements MakeService, InitializingBean {
    private final static Logger logger = LoggerUtil.getLogger(MakeServiceImpl.class);

    @Autowired
    private SessionService sessionService;

    private Map<String, Enable> cache = new HashMap<>();

    @Override
    @Cacheable(cacheManager = CacheManager.CACHE_MANAGER, cacheNames = CacheName.MAKE, key = "#param.like1")
    public Enable get(EnableParam param) {
        LoggerUtil.debug(logger, "get", param);

        logger.info("current principal : {}", sessionService.getPrincipal());

        return this.cache.get(param.getLike1());
    }

    @Override
    @CachePut(cacheManager = CacheManager.CACHE_MANAGER, cacheNames = CacheName.MAKE, key = "#param.like1")
    public boolean update(EnableParam param) {
        LoggerUtil.debug(logger, "update", param);

        logger.info("current principal : {}", sessionService.getPrincipal());

        Enable item = this.cache.get(String.valueOf(param.getLike1()));
        if (null != item) {
            item.setLikeFake(param.getLikeFake());

            return true;
        }
        return false;
    }

    @Override
    @Cacheable(cacheManager = CacheManager.CACHE_MANAGER, cacheNames = CacheName.MAKE_LIST, key = "#param.likeFake")
    public List<Enable> query(EnableParam param) {
        LoggerUtil.debug(logger, "query", param);

        logger.info("current principal : {}", sessionService.getPrincipal());

        List<Enable> result = new ArrayList<>();
        this.cache.values().forEach(item -> {
            if (null != item.getLikeFake() && item.getLikeFake() > param.getLikeFake()) {
                result.add(item);
            }
        });

        return result;
    }

    @Override
    @Cacheable(cacheManager = CacheManager.CACHE_MANAGER, cacheNames = CacheName.MAKE_LIST, key = CacheSpecialKey.FULL_CACHE_FETCHING)
    public Map<String, List<Enable>> queryMapping() {
        LoggerUtil.debug(logger, "queryMapping");
        Map<String, List<Enable>> mapping = new HashMap<>();

        logger.info("current principal : {}", sessionService.getPrincipal());

        this.cache.values().forEach(item -> {
            List<Enable> list = mapping.get(String.valueOf(item.getLikeFake()));
            if (null == list) {
                list = new ArrayList<>();
                mapping.put(String.valueOf(item.getLikeFake()), list);
            }
            list.add(item);
        });

        return mapping;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Enable enable = create("1", 1, "pay a", 1);
        this.cache.put(enable.getLike1(), enable);

        enable = create("2", 1, "pay b", 2);
        this.cache.put(enable.getLike1(), enable);

        enable = create("3", 2, "sell a", 1);
        this.cache.put(enable.getLike1(), enable);

        enable = create("4", 1, "sell b", 2);
        this.cache.put(enable.getLike1(), enable);
    }

    private Enable create(String like1, Integer likeFake, String notLike1, Integer notLikeFake) {
        Enable enable = new Enable();
        enable.setLike1(like1).setLikeFake(likeFake).setNotLike1(notLike1).setNotLikeFake(notLikeFake);

        return enable;
    }
}
