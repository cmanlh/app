package com.thirdparty.controller;

import com.lifeonwalden.app.gateway.bean.Response;
import com.lifeonwalden.app.gateway.util.ResponseUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/todo/")
public class TodoController {
    @RequestMapping(value = "query")
    public Response query() {
        return ResponseUtil.success();
    }

    @RequestMapping(value = "err")
    public Response err() {
        throw new RuntimeException("System error");
    }
}
