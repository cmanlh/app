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
        String key = KeyTool.generateWithEncode("AES", 256);
        String iv = CharUtil.bytesToU64(RandomUtils.nextBytes(12));

        String targetText = "要求加密字符串:hello world!";
        Assert.assertTrue(targetText.equals(Aes.decrypt(key, iv, Aes.encrypt(key, iv, targetText))));
    }

    @Test
    public void cs() {
        String key = "=eKeVLB=g36OWD=zeptW=HBHXhC=x7XZG=nkGsgD=G+kIsB=kolE/D";
        String iv = "=msqgeC=YQv1wD=beUwWB";

        String targetText = "要求加密字符串:hello world!";
        Assert.assertTrue(targetText.equals(Aes.decrypt(key, iv, Aes.encrypt(key, iv, targetText))));

        //=hl7/6B=RaLTfC=qJuoeC=SWIMU=2tQKRD=3AMK2B=eOG8m
        //=hl7/6B=RaLTfC=qJuoeC=gOrFu=bP+XeB=7QEKQC
        System.out.println(Aes.encrypt(key, iv, "hello world!3315"));
        System.out.println(Aes.encrypt(key, iv, "he"));

        CharUtil.u64ToBytes("=eOG8m");
        CharUtil.u64ToBytes("=Ci0gz");

        //=hl7/6B=RaLTfC=qJuoeC=SWIMU=2tQKRD=3AMK2B=eOG8m
        //=O6wLZD=d6cl1B=5qJYvC=Wv4dfC=nN2t5C=XRDdOD=a9Vhj

        System.out.println(Aes.decrypt(key, iv, "=-6=COYta=4xIgwB=qdYi0D=NnkxAC=/=gC"));

    }
}
