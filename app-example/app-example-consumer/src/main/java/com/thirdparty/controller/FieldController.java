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
import com.thirdparty.bean.DatabaseFieldParam;
import com.thirdparty.service.StoreService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store/")
public class FieldController extends BaseController {

    @RequestMapping(value = "queryAll")
    public Response queryAll() {
        return ResponseUtil.success(getService(StoreService.class).queryAll());
    }

    @RequestMapping(value = "query")
    public Response query(@ModelAttribute DatabaseFieldParam databaseField) {
        return ResponseUtil.success(getService(StoreService.class).query(databaseField));
    }

    @RequestMapping(value = "get")
    public Response get(@ModelAttribute DatabaseFieldParam databaseField) {
        return ResponseUtil.success(getService(StoreService.class).get(databaseField));
    }

    @RequestMapping(value = "delete")
    public Response delete(@ModelAttribute DatabaseFieldParam databaseField) {
        return ResponseUtil.success(getService(StoreService.class).delete(databaseField));
    }

    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Response insert(@ModelAttribute DatabaseFieldParam databaseField) {
        return ResponseUtil.success(getService(StoreService.class).insert(databaseField));
    }

    @RequestMapping(value = "queryMapping")
    public Response queryMapping() {
        return ResponseUtil.success(getService(StoreService.class).queryMapping());
    }

    @RequestMapping(value = "refreshMapping")
    public Response refreshMapping() {
        return ResponseUtil.success(getService(StoreService.class).refreshMapping());
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Response update(@ModelAttribute DatabaseFieldParam databaseField) {
        return ResponseUtil.success(getService(StoreService.class).update(databaseField));
    }
}
