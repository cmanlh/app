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

package com.lifeonwalden.app.gateway.util;

import com.lifeonwalden.app.gateway.bean.Response;
import com.lifeonwalden.app.gateway.constant.ResponseCode;
import org.apache.commons.lang3.StringUtils;

public interface ResponseUtil {
    static Response success(String msg, Object result) {
        Response response = new Response();
        response.setCode(ResponseCode.SUCCESS);
        if (StringUtils.isNotBlank(msg)) {
            response.setMsg(msg);
        } else {
            response.setMsg("success");
        }

        if (null != result) {
            response.setResult(result);
        }

        return response;
    }

    static Response success(String msg) {
        return success(msg, null);
    }

    static Response success(Object result) {
        return success(null, result);
    }

    static Response success() {
        return success(null, null);
    }

    static Response fail(String msg, Object result) {
        Response response = new Response();
        response.setCode(ResponseCode.FAIL);
        if (StringUtils.isNotBlank(msg)) {
            response.setMsg(msg);
        } else {
            response.setMsg("fail");
        }

        if (null != result) {
            response.setResult(result);
        }

        return response;
    }

    static Response fail(String msg) {
        return fail(msg, null);
    }

    static Response fail(Object result) {
        return fail(null, result);
    }

    static Response fail() {
        return fail(null, null);
    }

    static Response anyone(String code, String msg, Object result) {
        Response response = new Response();
        response.setCode(code);
        response.setMsg(msg);
        response.setResult(result);

        return response;
    }
}
