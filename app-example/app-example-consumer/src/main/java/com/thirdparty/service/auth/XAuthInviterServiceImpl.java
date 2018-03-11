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

package com.thirdparty.service.auth;

import com.lifeonwalden.app.gateway.auth.service.XAuthInviterService;
import com.lifeonwalden.app.util.logger.LoggerUtil;
import com.thirdparty.service.AuthService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XAuthInviterServiceImpl extends AuthServiceImpl implements XAuthInviterService {
    private final static Logger logger = LoggerUtil.getLogger(XAuthInviterServiceImpl.class);

    @Autowired
    private AuthService authService;

    @Override
    public String getPreLoginCode(String principal) {
        LoggerUtil.debug(logger, "getPreLoginCode", principal);

        return authService.getPreLoginCode(principal);
    }

    @Override
    public void notifyPreLoginAccepted(String acceptedCode) {
        LoggerUtil.debug(logger, "notifyPreLoginAccepted", acceptedCode);

        authService.notifyPreLoginAccepted(acceptedCode);
    }
}
