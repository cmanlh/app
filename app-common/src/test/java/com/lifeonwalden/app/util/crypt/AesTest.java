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
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;

public class AesTest {

    @Test
    public void aesKey() {
        String key = Aes.generateWithEncode(256);
        String iv = CharUtil.bytesToU64(RandomUtils.nextBytes(16));

        String targetText = "要求加密字符串:hello world!";
        Assert.assertTrue(targetText.equals(Aes.decrypt(key, iv, Aes.encrypt(key, iv, targetText))));
    }
}
