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

package com.thirdparty.service.auth;

import com.lifeonwalden.app.gateway.auth.service.XAuthAnswsererService;
import com.lifeonwalden.app.gateway.auth.service.XAuthService;
import com.lifeonwalden.app.util.logger.LoggerUtil;
import com.thirdparty.service.AuthService;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XAuthAnswererServiceImpl extends AuthServiceImpl implements XAuthAnswsererService {
    private final static Logger logger = LoggerUtil.getLogger(XAuthAnswererServiceImpl.class);

    @Autowired
    private AuthService authService;

    @Override
    public String getXPrincipal(String preLoginCode) {
        return authService.getXPrincipal(preLoginCode);
    }

    @Override
    public String getAcceptedCode() {
        return authService.getAcceptedCode();
    }

    @Override
    public boolean isPermitted() {
        Object acceptedResult = SecurityUtils.getSubject().getSession().getAttribute(XAuthService.SSO_PRE_LOGIN_ACCEPTED_CODE);
        if (null == acceptedResult) {
            return true;
        }

        if (authService.isPermitted((String) acceptedResult)) {
            SecurityUtils.getSubject().getSession().removeAttribute(XAuthService.SSO_PRE_LOGIN_ACCEPTED_CODE);

            return true;
        }

        return false;
    }
}
