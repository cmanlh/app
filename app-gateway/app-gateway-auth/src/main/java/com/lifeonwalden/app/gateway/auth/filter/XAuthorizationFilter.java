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

import com.lifeonwalden.app.gateway.auth.service.XAuthAnswsererService;
import com.lifeonwalden.app.gateway.auth.service.XAuthService;
import com.lifeonwalden.app.gateway.auth.util.RemoteAddressUtil;
import com.lifeonwalden.app.util.logger.LoggerUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * SSO authorization filter
 */
public class XAuthorizationFilter extends BaseAuthorizationFilter {
    private final static Logger logger = LoggerUtil.getLogger(XAuthorizationFilter.class);

    @Autowired
    protected XAuthAnswsererService xAuthService;

    @Override
    protected void ssoPreLogin(HttpServletRequest request) {
        String ssoPreLoginCode = request.getParameter(XAuthService.SSO_PRE_LOGIN_CODE);
        if (StringUtils.isEmpty(ssoPreLoginCode)) {
            return;
        }

        String principal = this.xAuthService.getXPrincipal(ssoPreLoginCode);
        if (StringUtils.isEmpty(principal)) {
            return;
        }

        SecurityUtils.getSubject().login(new UsernamePasswordToken(principal, "", RemoteAddressUtil.getIpAddr(request)));
        SecurityUtils.getSubject().getSession().setAttribute(XAuthService.SSO_PRE_LOGIN_ACCEPTED_CODE, xAuthService.getAcceptedCode());
    }

    @Override
    protected boolean isPermitted(Subject subject, String uri, HttpServletRequest request) {
        if (null == subject.getPrincipal()) {
            return false;
        }
        return null != subject && xAuthService.isPermitted() && subject.isPermitted(uri);
    }
}
