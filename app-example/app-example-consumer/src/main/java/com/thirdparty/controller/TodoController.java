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
import com.thirdparty.bean.Todo;
import com.thirdparty.service.TodoService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public class TodoController extends BaseController {

    @RequestMapping(value = "query")
    public Response query(@RequestParam("content") String content) {
        return ResponseUtil.success(getService(TodoService.class).query(content));
    }

    @RequestMapping(value = "get")
    public Response get(@RequestParam("id") String id) {
        return ResponseUtil.success(getService(TodoService.class).get(id));
    }

    @RequestMapping(value = "queryMapping")
    public Response queryMapping() {
        return ResponseUtil.success(getService(TodoService.class).queryMapping());
    }

    @RequestMapping(value = "queryAll")
    public Response queryAll() {
        return ResponseUtil.success(getService(TodoService.class).queryAll());
    }

    @RequestMapping(value = "updateStatus", method = RequestMethod.POST)
    public Response updateStatus(@ModelAttribute Todo todo) {
        return ResponseUtil.success(getService(TodoService.class).updateStatus(todo));
    }

    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Response insert(@ModelAttribute Todo todo) {
        return ResponseUtil.success(getService(TodoService.class).insert(todo));
    }

    @RequestMapping(value = "delete")
    public Response delete(@RequestParam("id") String id) {
        return ResponseUtil.success(getService(TodoService.class).delete(id));
    }

    @RequestMapping(value = "err")
    public Response err() {
        throw new RuntimeException("System error");
    }
}
