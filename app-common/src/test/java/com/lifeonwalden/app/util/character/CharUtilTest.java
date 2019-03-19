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

package com.lifeonwalden.app.util.character;

import org.junit.Assert;
import org.junit.Test;

public class CharUtilTest {

    @Test
    public void textToU64() {
        String srcText = "The Prototype Kit provides a simple way to make interactive prototypes that look like pages on GOV.UK. These prototypes can be used to show ideas to people you work with, and to do user research";
        String u64 = CharUtil.textToU64(srcText);

        Assert.assertTrue(srcText.equals(CharUtil.u64ToText(u64)));
    }

    @Test
    public void u64ToText() {
        String srcU64 = "=gKF=GvF=wVG=uNG=EkF=GQH=0B=vB=vB=sB=zB";
        String text = CharUtil.u64ToText(srcU64);

        Assert.assertTrue(srcU64.equals(CharUtil.textToU64(text)));
    }

    @Test
    public void u64ToByte() {
        String srcText = "The Prototype Kit provides a simple way to make interactive prototypes that look like pages on GOV.UK. These prototypes can be used to show ideas to people you work with, and to do user research";
        String u64 = CharUtil.textToU64(srcText);

        byte[] encodeByte = CharUtil.u64ToBytes(u64);

        Assert.assertTrue(srcText.equals(CharUtil.u64ToText(CharUtil.bytesToU64(encodeByte))));
    }

    @Test
    public void u64ToJsByte() {
        String u64 = "=dtC=etC=ftC=gtC=htC";
        String byteSerial = "11101,11102,11103,11104,11105,";
        StringBuilder binaryString = new StringBuilder();

        byte[] decodedByte = CharUtil.u64ToBytes(u64);
        int size = decodedByte.length / 4;
        for (int i = 0; i < size; i++) {
            int codePoint = 0;
            codePoint |= decodedByte[i * 4] << 24;
            codePoint |= decodedByte[i * 4 + 1] << 16;
            codePoint |= decodedByte[i * 4 + 2] << 8;
            codePoint |= decodedByte[i * 4 + 3];
            binaryString.append(String.valueOf(codePoint)).append(",");
        }

        System.out.println(binaryString.toString());
        Assert.assertTrue(byteSerial.equals(binaryString.toString()));
    }

}
