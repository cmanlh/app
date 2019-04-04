/*
 *    Copyright 2019 CManLH
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

package com.lifeonwalden.app.util.crypt.bean;

import java.io.Serializable;

public class KeyPairBean implements Serializable {
    private static final long serialVersionUID = -6777773840156189237L;
    private String privateKey;

    private String publicKey;

    public String getPrivateKey() {
        return privateKey;
    }

    public KeyPairBean setPrivateKey(String privateKey) {
        this.privateKey = privateKey;

        return this;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public KeyPairBean setPublicKey(String publicKey) {
        this.publicKey = publicKey;

        return this;
    }
}
