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

import com.lifeonwalden.app.gateway.auth.util.RemoteAddressUtil;
import com.lifeonwalden.app.gateway.bean.Response;
import com.lifeonwalden.app.gateway.util.ResponseUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

import javax.servlet.http.HttpServletRequest;

public class BaseLoginController {
    protected Response doLogin(String principle, HttpServletRequest request) throws AuthenticationException {
        AuthenticationToken token = new UsernamePasswordToken(principle, "", RemoteAddressUtil.getIpAddr(request));
        SecurityUtils.getSubject().login(token);

        return ResponseUtil.success();
    }

    protected Response doLogout() {
        SecurityUtils.getSubject().logout();

        return ResponseUtil.success();
    }
}
