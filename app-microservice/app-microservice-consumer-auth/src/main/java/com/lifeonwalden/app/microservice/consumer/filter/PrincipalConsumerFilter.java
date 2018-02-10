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

package com.lifeonwalden.app.microservice.consumer.filter;

import com.alibaba.dubbo.rpc.*;
import com.lifeonwalden.app.microservice.constant.PrincipalInfoConstant;
import org.apache.shiro.SecurityUtils;

public class PrincipalConsumerFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        invocation.getAttachments().put(PrincipalInfoConstant.PRINCIPLE_KEY, (String) SecurityUtils.getSubject().getPrincipal());

        return invoker.invoke(invocation);
    }
}
