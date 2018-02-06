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

import com.lifeonwalden.app.gateway.bean.Response;
import com.lifeonwalden.app.gateway.spring.propertyeditor.DatePropertyEditor;
import com.lifeonwalden.app.gateway.util.ResponseUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.FormattedMessage;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice(annotations = RestController.class)
public class CommonController {
    private final static Logger logger = LogManager.getLogger(CommonController.class);

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder dataBinder) throws Exception {
        logger.debug("initBinder");

        dataBinder.registerCustomEditor(Date.class, new DatePropertyEditor());
    }

    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public Response handle(HttpServletRequest req, Throwable e) {
        logger.error(new FormattedMessage("Error happened in request url: {}", req.getRequestURI()), e);

        return ResponseUtil.fail("System Error happened, please contact system administrator");
    }
}
