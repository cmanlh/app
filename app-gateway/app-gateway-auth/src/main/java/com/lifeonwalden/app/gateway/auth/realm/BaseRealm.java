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

package com.lifeonwalden.app.gateway.auth.realm;

import com.lifeonwalden.app.gateway.auth.service.AuthService;
import com.lifeonwalden.app.util.logger.LoggerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

public class BaseRealm extends AuthorizingRealm {
    private final static Logger logger = LogManager.getLogger(BaseRealm.class);

    protected AntPathMatcher pathMatcher = new AntPathMatcher();

    @Autowired
    protected AuthService authService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LoggerUtil.debug(logger, "doGetAuthorizationInfo", principals);

        List<String> permissionList = authService.getPermissions((String) principals.getPrimaryPrincipal());

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermissions(permissionList);

        return authorizationInfo;
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        LoggerUtil.debug(logger, "isPermitted", principals, permission);

        AuthorizationInfo info = getAuthorizationInfo(principals);
        if (null == info) {
            return false;
        }

        Collection<String> permissions = info.getStringPermissions();
        if (CollectionUtils.isNotEmpty(permissions)) {
            for (String pattern : permissions) {
                if (pathMatcher.match(pattern, permission)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        LoggerUtil.debug(logger, "doGetAuthenticationInfo", token);

        if (null != token && authService.isExist((String) token.getPrincipal())) {
            return new SimpleAuthenticationInfo(token.getPrincipal(), "", this.getName());
        } else {
            throw new AuthenticationException(token.getPrincipal() + "is not a valid user.");
        }
    }

    public void clearCache() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            cache.clear();
        }
    }
}
