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

import com.lifeonwalden.app.microservice.consumer.service.ServiceLookup;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {
    @Autowired
    private ServiceLookup serviceLookup;

    protected <T> T getService(Class<T> clazz) {
        return serviceLookup.get(clazz);
    }
}