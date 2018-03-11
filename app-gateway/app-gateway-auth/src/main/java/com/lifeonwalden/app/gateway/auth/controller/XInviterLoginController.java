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

package com.lifeonwalden.app.gateway.auth.controller;

import com.lifeonwalden.app.gateway.auth.service.XAuthInviterService;
import com.lifeonwalden.app.gateway.auth.service.XAuthService;
import com.lifeonwalden.app.gateway.bean.Response;
import com.lifeonwalden.app.gateway.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public class XInviterLoginController extends BaseLoginController {
    @Autowired
    private XAuthInviterService xAuthInviterService;

    @Override
    protected Response doLogin(String principle, HttpServletRequest request) throws AuthenticationException {
        Response response = super.doLogin(principle, request);

        return response.setResult(xAuthInviterService.getPreLoginCode(principle));
    }

    @RequestMapping(path = "auth/sso/notifyPreLoginAccepted")
    public Response notifyPreLoginAccepted(@RequestParam(value = XAuthService.SSO_PRE_LOGIN_ACCEPTED_CODE) String ssoPreLoginAcceptedCode) {
        if (StringUtils.isEmpty(ssoPreLoginAcceptedCode)) {
            return ResponseUtil.fail();
        }
        xAuthInviterService.notifyPreLoginAccepted(ssoPreLoginAcceptedCode);

        return ResponseUtil.success();
    }
}
