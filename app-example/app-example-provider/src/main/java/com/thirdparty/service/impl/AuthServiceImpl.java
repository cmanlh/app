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

package com.thirdparty.service.impl;

import com.lifeonwalden.app.util.character.IdGenerator;
import com.lifeonwalden.app.util.logger.LoggerUtil;
import com.thirdparty.service.AuthService;
import com.thirdparty.service.SessionService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    private final static Logger logger = LoggerUtil.getLogger(AuthServiceImpl.class);

    @Autowired
    private SessionService sessionService;

    private Map<String, String> redis = new HashMap<>();

    @Override
    public String getPrincipal() {
        logger.debug("getPrincipal");

        return sessionService.getPrincipal();
    }

    @Override
    public String getPreLoginCode(String principal) {
        String preLoginCode = IdGenerator.getId();

        redis.put(preLoginCode, principal);

        return preLoginCode;
    }

    @Override
    public void notifyPreLoginAccepted(String acceptedCode) {
        redis.put(acceptedCode, Boolean.TRUE.toString());
    }

    @Override
    public String getXPrincipal(String preLoginCode) {
        return redis.get(preLoginCode);
    }

    @Override
    public String getAcceptedCode() {
        String acceptedCode = IdGenerator.getId();

        redis.put(acceptedCode, Boolean.FALSE.toString());

        return acceptedCode;
    }

    @Override
    public boolean isPermitted(String acceptedCode) {
        return Boolean.valueOf(redis.get(acceptedCode));
    }
}
