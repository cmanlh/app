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

package com.thirdparty.controller;

import com.lifeonwalden.app.gateway.auth.controller.BaseLoginController;
import com.lifeonwalden.app.gateway.bean.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController extends BaseLoginController {

    @RequestMapping(value = "open/login", method = RequestMethod.POST)
    public Response login(@RequestParam(value = "userId") String userId, HttpServletRequest request) {
        return doLogin(userId, request);
    }

    @RequestMapping(value = "auth/logout")
    public Response logout() {
        return doLogout();
    }
}
