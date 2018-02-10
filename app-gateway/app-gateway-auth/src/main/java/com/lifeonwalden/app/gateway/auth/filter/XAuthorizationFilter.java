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

package com.lifeonwalden.app.gateway.auth.filter;

import com.lifeonwalden.app.gateway.auth.service.XAuthService;
import com.lifeonwalden.app.util.logger.LoggerUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * SSO authorization filter
 */
public class XAuthorizationFilter extends BaseAuthorizationFilter {
    private final static Logger logger = LoggerUtil.getLogger(XAuthorizationFilter.class);

    @Autowired
    protected XAuthService xAuthService;

    @Override
    protected boolean ssoRequest(HttpServletRequest request) {
        String ssoSessionId = request.getParameter(XAuthService.SSO_SESSION_ID);
        if (StringUtils.isEmpty(ssoSessionId)) {
            return false;
        }

        String principal = this.xAuthService.getXPrincipal(request.getRemoteAddr(), ssoSessionId);
        if (StringUtils.isEmpty(principal)) {
            return false;
        }

        SecurityUtils.getSubject().login(new UsernamePasswordToken(principal, "", request.getRemoteAddr()));
        if (SecurityUtils.getSubject().isPermitted(request.getRequestURI())) {
            return true;
        }

        return false;
    }
}
