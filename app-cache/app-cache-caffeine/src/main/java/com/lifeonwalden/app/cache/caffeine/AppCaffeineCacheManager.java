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

package com.lifeonwalden.app.cache.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.lifeonwalden.app.cache.service.CacheService;
import com.lifeonwalden.app.util.logger.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AppCaffeineCacheManager extends CaffeineCacheManager implements CacheService {
    private final static Logger logger = LoggerUtil.getLogger(AppCaffeineCacheManager.class);

    public AppCaffeineCacheManager(long duration, String timeUnit) {
        super();
        this.setCaffeine(Caffeine.newBuilder().expireAfterWrite(duration, TimeUnit.valueOf(timeUnit)));
    }

    @Override
    public List<String> listCache() {
        return new ArrayList<>(this.getCacheNames());
    }

    //TODO need to rewrite org.springframework.cache.caffeine.CaffeineCache
    @Override
    public List<String> listKey(String name) {
        return null;
    }

    @Override
    public boolean evict(String name, String key) {
        Cache cache = this.getCache(name);
        if (null != cache) {
            cache.evict(key);
            return true;
        }
        return false;
    }

    @Override
    public boolean clear(String name) {
        Cache cache = this.getCache(name);
        if (null != cache) {
            cache.clear();
            return true;
        }
        return false;
    }
}
