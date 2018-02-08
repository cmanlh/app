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
package com.thirdparty.service;

import com.thirdparty.bean.Todo;

import java.util.List;
import java.util.Map;

public interface TodoService {
    Todo get(String id);

    boolean updateStatus(Todo todo);

    List<Todo> query(String containsContent);

    Map<Integer, List<Todo>> queryMapping();
}