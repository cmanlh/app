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

import com.lifeonwalden.app.util.logger.LoggerUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class BaseAuthorizationFilter extends AuthorizationFilter {
    private final static Logger logger = LoggerUtil.getLogger(BaseAuthorizationFilter.class);

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        logger.debug("isAccessAllowed");

        Subject subject = getSubject(request, response);

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String uri = httpServletRequest.getRequestURI();
        String contextPath = httpServletRequest.getContextPath();

        if (StringUtils.startsWithIgnoreCase(uri, contextPath.concat("/open/"))) {
            logger.debug("isAccessAllowed : user : {}, mappedValue : {}, resource : {}, isAllowed : OPEN RESOURCE", subject.getPrincipal(),
                    mappedValue, uri);

            if (StringUtils.equalsIgnoreCase(uri, contextPath.concat("/open/sso/preLogin"))) {
                ssoPreLogin(httpServletRequest);
            }

            return true;
        } else {
            if (isPermitted(subject, uri)) {
                logger.debug("isAccessAllowed : user : {}, mappedValue : {}, resource : {}, isAllowed : TRUE", subject.getPrincipal(), mappedValue,
                        uri);

                return true;
            } else {
                logger.debug("isAccessAllowed : user : {}, mappedValue : {}, resource : {}, isAllowed : FALSE", subject.getPrincipal(), mappedValue,
                        uri);

                if (uri.isEmpty() || !StringUtils.containsIgnoreCase(uri, ".do")) {
                    logger.error("Invalid request : user : {}, mappedValue : {}, resource : {}", subject.getPrincipal(), mappedValue,
                            uri);
                }

                return false;
            }
        }
    }

    protected void ssoPreLogin(HttpServletRequest request) {
    }

    protected boolean isPermitted(Subject subject, String uri) {
        return null != subject.getPrincipal() && subject.isPermitted(uri);
    }
}