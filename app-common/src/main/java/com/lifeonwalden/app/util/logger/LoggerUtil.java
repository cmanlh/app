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

package com.lifeonwalden.app.util.logger;

import com.lifeonwalden.app.util.character.JSON;
import org.apache.logging.log4j.Logger;

public interface LoggerUtil {
    static void info(Logger logger, String methodName, Object... params) {
        if (null != params && params.length > 0) {
            logger.info(methodName + " : {} ", JSON.log(params));
        } else {
            logger.info(methodName + " : {}");
        }
    }

    static void error(Logger logger, String methodName, Object... params) {
        if (null != params && params.length > 0) {
            logger.error(methodName + " : {} ", JSON.log(params));
        } else {
            logger.error(methodName + " : {}");
        }
    }

    static void debug(Logger logger, String methodName, Object... params) {
        if (null != params && params.length > 0) {
            logger.debug(methodName + " : {} ", JSON.log(params));
        } else {
            logger.debug(methodName + " : {}");
        }
    }
}
