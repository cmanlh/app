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

package com.lifeonwalden.app.microservice.provider.filter;

import com.alibaba.dubbo.config.spring.ServiceBean;
import com.alibaba.dubbo.rpc.*;
import com.lifeonwalden.app.microservice.constant.PrincipalInfoConstant;
import com.lifeonwalden.app.microservice.provider.service.PrincipalService;
import com.lifeonwalden.app.util.logger.LoggerUtil;
import org.apache.logging.log4j.Logger;

public class PrincipalProviderFilter implements Filter {
    private final static Logger logger = LoggerUtil.getLogger(PrincipalProviderFilter.class);

    private PrincipalService principalService = ServiceBean.getSpringContext().getBean(PrincipalService.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        logger.debug("{} call method {}", invoker.getInterface().getName(), invocation.getMethodName());

        String principal = invocation.getAttachments().get(PrincipalInfoConstant.PRINCIPLE_KEY);
        if (null != principal && principal.length() > 0) {
            principalService.setPrincipal(principal);
        }

        return invoker.invoke(invocation);
    }
}
