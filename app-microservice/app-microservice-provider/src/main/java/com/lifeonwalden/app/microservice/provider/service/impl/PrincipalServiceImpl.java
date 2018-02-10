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

package com.lifeonwalden.app.microservice.provider.service.impl;

import com.lifeonwalden.app.microservice.provider.service.PrincipalService;
import com.lifeonwalden.app.util.logger.LoggerUtil;
import org.apache.logging.log4j.Logger;

public class PrincipalServiceImpl implements PrincipalService {
    private final static Logger logger = LoggerUtil.getLogger(PrincipalServiceImpl.class);

    private ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Override
    public String getPrinciple() {
        logger.debug("getPrinciple");

        return threadLocal.get();
    }

    @Override
    public boolean setPrincipal(String principal) {
        logger.debug("setPrincipal {}", principal);

        if (null != getPrinciple()) {
            return false;
        }
        threadLocal.set(principal);

        return true;
    }
}
