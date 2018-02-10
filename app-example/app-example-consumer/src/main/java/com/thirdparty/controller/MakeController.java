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

import com.lifeonwalden.app.gateway.bean.Response;
import com.lifeonwalden.app.gateway.util.ResponseUtil;
import com.thirdparty.bean.EnableParam;
import com.thirdparty.service.MakeService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/make/")
public class MakeController extends BaseController {

    @RequestMapping(value = "query")
    public Response query(@ModelAttribute EnableParam enable) {
        return ResponseUtil.success(getService(MakeService.class).query(enable));
    }

    @RequestMapping(value = "get")
    public Response get(@ModelAttribute EnableParam enable) {
        return ResponseUtil.success(getService(MakeService.class).get(enable));
    }

    @RequestMapping(value = "queryMapping")
    public Response queryMapping() {
        return ResponseUtil.success(getService(MakeService.class).queryMapping());
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Response update(@ModelAttribute EnableParam enable) {
        return ResponseUtil.success(getService(MakeService.class).update(enable));
    }
}