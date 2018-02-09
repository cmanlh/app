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

import com.lifeonwalden.app.util.logger.LoggerUtil;
import com.thirdparty.bean.Enable;
import com.thirdparty.bean.EnableParam;
import com.thirdparty.service.MakeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MakeServiceImpl implements MakeService, InitializingBean {
    private final static Logger logger = LogManager.getLogger(MakeServiceImpl.class);

    private Map<String, Enable> cache = new HashMap<>();

    @Override
    public Enable get(EnableParam param) {
        LoggerUtil.debug(logger, "get", param);

        return this.cache.get(param.getLike1());
    }

    @Override
    public boolean update(EnableParam param) {
        LoggerUtil.debug(logger, "update", param);

        Enable item = this.cache.get(String.valueOf(param.getLike1()));
        if (null != item) {
            item.setLikeFake(param.getLikeFake());

            return true;
        }
        return false;
    }

    @Override
    public List<Enable> query(EnableParam param) {
        LoggerUtil.debug(logger, "query", param);

        List<Enable> result = new ArrayList<>();
        this.cache.values().forEach(item -> {
            if (null != item.getLikeFake() && item.getLikeFake() > param.getLikeFake()) {
                result.add(item);
            }
        });

        return result;
    }

    @Override
    public Map<String, List<Enable>> queryMapping() {
        LoggerUtil.debug(logger, "queryMapping");
        Map<String, List<Enable>> mapping = new HashMap<>();

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
