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

package com.thirdparty.service;

import com.lifeonwalden.app.gateway.auth.service.XAuthService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class XAuthServiceImpl implements XAuthService {
    private final String SESSION_ID = XAuthService.SSO_SESSION_ID.concat("Consumer");

    @Override
    public String getXPrincipal(String remoteAddr, String ssoSessionId) {
        if ("127.0.0.1".equals(remoteAddr) && SESSION_ID.equals(ssoSessionId)) {
            return "adminConsumer";
        }

        return null;
    }

    @Override
    public String getXSessionId(String remoteAddr, String principal) {
        if ("127.0.0.1".equals(remoteAddr) && "adminConsumer".equals(principal)) {
            return SESSION_ID;
        }
        return null;
    }

    @Override
    public List<String> getPermissions(String principal) {
        return Arrays.asList("/consumer/todo/**", "/consumer/store/**", "/consumer/make/**", "/consumer/auth/**");
    }

    @Override
    public boolean isExist(String principal) {
        return "adminConsumer".equals(principal);
    }
}
