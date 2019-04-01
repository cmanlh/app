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

package com.lifeonwalden.app.util.crypt;

import com.lifeonwalden.app.util.character.CharUtil;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

public interface KeyTool {

    static String generateWithEncode(String algorithm, int size) {
        return CharUtil.bytesToU64(generateKey(algorithm, size).getEncoded());
    }

    static SecretKey generateKey(String algorithm, int size) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            keyGenerator.init(size);

            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    static String encode(SecretKey key) {
        return CharUtil.bytesToU64(key.getEncoded());
    }

    static SecretKey decode(String algorithm, String keyData) {
        return new SecretKeySpec(CharUtil.u64ToBytes(keyData), algorithm);
    }
}
