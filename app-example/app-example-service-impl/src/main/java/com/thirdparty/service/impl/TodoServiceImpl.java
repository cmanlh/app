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

import com.lifeonwalden.app.example.common.constant.CacheManager;
import com.lifeonwalden.app.example.common.constant.CacheName;
import com.lifeonwalden.app.util.date.DateUtil;
import com.lifeonwalden.app.util.logger.LoggerUtil;
import com.thirdparty.bean.Todo;
import com.thirdparty.service.TodoService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TodoServiceImpl implements TodoService, InitializingBean {
    private final static Logger logger = LoggerUtil.getLogger(TodoServiceImpl.class);

    private Map<String, Todo> cache = new HashMap<>();

    @Override
    @Cacheable(cacheManager = CacheManager.CACHE_MANAGER, cacheNames = CacheName.TODO, key = "#id")
    public Todo get(String id) {
        LoggerUtil.debug(logger, "get", id);

        return this.cache.get(id);
    }

    @Override
    public boolean updateStatus(Todo todo) {
        LoggerUtil.debug(logger, "updateStatus", todo);

        Todo item = this.cache.get(todo.getId());
        if (null != item) {
            item.setStatus(todo.getStatus());

            return true;
        }
        return false;
    }

    @Override
    @Cacheable(cacheManager = CacheManager.CACHE_MANAGER, cacheNames = CacheName.TODO_LIST, key = "#containsContent")
    public List<Todo> query(String containsContent) {
        LoggerUtil.debug(logger, "query", containsContent);

        List<Todo> result = new ArrayList<>();
        this.cache.values().forEach(todo -> {
            if (null != todo.getContent() && todo.getContent().contains(containsContent)) {
                result.add(todo);
            }
        });

        return result;
    }

    @Override
    @Cacheable(cacheManager = CacheManager.CACHE_MANAGER, cacheNames = CacheName.TODO_ALL, key = "#root.methodName")
    public Map<Integer, List<Todo>> queryMapping() {
        LoggerUtil.debug(logger, "queryMapping");
        Map<Integer, List<Todo>> mapping = new HashMap<>();

        this.cache.values().forEach(todo -> {
            List<Todo> list = mapping.get(todo.getStatus());
            if (null == list) {
                list = new ArrayList<>();
                mapping.put(todo.getStatus(), list);
            }
            list.add(todo);
        });

        return mapping;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Todo todo = create("1", "clear a", DateUtil.parseDate("20171220", DateUtil.FULL_SHORT_DATE), 0);
        this.cache.put(todo.getId(), todo);

        todo = create("2", "clear b", DateUtil.parseDate("20171221", DateUtil.FULL_SHORT_DATE), 1);
        this.cache.put(todo.getId(), todo);

        todo = create("3", "pay a", DateUtil.parseDate("20180121", DateUtil.FULL_SHORT_DATE), 0);
        this.cache.put(todo.getId(), todo);

        todo = create("4", "pay b", DateUtil.parseDate("20180123", DateUtil.FULL_SHORT_DATE), 1);
        this.cache.put(todo.getId(), todo);
    }

    private Todo create(String id, String content, Date date, int status) {
        Todo todo = new Todo();
        todo.setContent(content);
        todo.setId(id);
        todo.setDate(date);
        todo.setStatus(status);

        return todo;
    }
}
