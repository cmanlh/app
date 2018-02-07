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

package com.lifeonwalden.app.gateway.auth.bean;

import java.io.Serializable;
import java.util.Date;

public class AuditBean implements Serializable {
    private static final long serialVersionUID = -580015212976607853L;

    private String principle;

    private String host;

    private Date date;

    private String operation;

    private String content;

    public String getPrinciple() {
        return principle;
    }

    public AuditBean setPrinciple(String principle) {
        this.principle = principle;

        return this;
    }

    public String getHost() {
        return host;
    }

    public AuditBean setHost(String host) {
        this.host = host;

        return this;
    }

    public Date getDate() {
        return date;
    }

    public AuditBean setDate(Date date) {
        this.date = date;

        return this;
    }

    public String getOperation() {
        return operation;
    }

    public AuditBean setOperation(String operation) {
        this.operation = operation;

        return this;
    }

    public String getContent() {
        return content;
    }

    public AuditBean setContent(String content) {
        this.content = content;

        return this;
    }
}
