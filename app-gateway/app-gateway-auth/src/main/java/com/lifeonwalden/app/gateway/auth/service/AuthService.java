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

package com.lifeonwalden.app.gateway.auth.service;

import java.util.List;

public interface AuthService {
    /**
     * get authorized permissions
     *
     * @param principal
     * @return
     */
    List<String> getPermissions(String principal);

    /**
     * check the principal is exist or not
     *
     * @param principal
     * @return
     */
    boolean isExist(String principal);
}
