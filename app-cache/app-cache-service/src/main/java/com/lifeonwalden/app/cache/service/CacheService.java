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

import java.util.List;

public interface CacheService {

    /**
     * list names of  caches
     *
     * @return
     */
    List<String> listCache();

    /**
     * list keys of cache $name
     *
     * @param name
     * @return
     */
    List<String> listKey(String name);

    /**
     * evict stored data of key $key from cache $name
     *
     * @param name
     * @param key
     * @return
     */
    boolean evict(String name, String key);

    /**
     * clear cache $name
     *
     * @param name
     * @return
     */
    boolean clear(String name);
}
