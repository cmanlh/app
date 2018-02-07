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

package com.lifeonwalden.app.gateway.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class SiteController {
    private final static Logger logger = LogManager.getLogger(SiteController.class);

    @RequestMapping(path = "/**/*.do")
    public String dispatch(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();

        if (!StringUtils.endsWithIgnoreCase(uri, ".do")) {
            try {
                response.sendRedirect("open/sys/error404");
            } catch (IOException e) {
                logger.error("redirect failed", e);

                return "";
            }
        }

        String contextPath = request.getContextPath();
        int startIdx = StringUtils.isBlank(contextPath) ? 1 : 1 + contextPath.length();

        return StringUtils.substring(StringUtils.substringBefore(uri, "."), startIdx);
    }
}
