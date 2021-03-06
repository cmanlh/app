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

package com.lifeonwalden.app.cache.service;

import com.lifeonwalden.app.cache.service.bean.CacheManagementBean;

import java.util.List;

public interface CacheManagementService {

    List<CacheManagementBean> listCacheManagement();

    List<String> listCache(String cacheManagementId);

    List<String> listKey(String cacheManagementId, String cacheName);

    boolean evict(String cacheManagementId, String cacheName, String key);

    boolean clear(String cacheManagementId, String cacheName);
}
