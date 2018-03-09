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

/**
 * Authentication service that supports sso
 */
public interface XAuthService extends AuthService {
    String SSO_SRC_SYS_ID = "ssoSrcSysId";
    String SSO_PRE_LOGIN_PASSED = "ssoPreLoginPassed";

    /**
     * fetch sso principal
     *
     * @param remoteAddr
     * @param ssoSessionId
     * @return
     */
    String getXPrincipal(String remoteAddr, String ssoSessionId);

    /**
     * generate sso request id and save it
     *
     * @param remoteAddr
     * @param principal
     * @return
     */
    String getXRequestId(String remoteAddr, String principal);

    /**
     * is permitted to do x request
     *
     * @return
     */
    boolean isPermitted();
}
