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

import org.junit.Assert;
import org.junit.Test;

public class DigestTest {
    @Test
    public void digest() {
        String text = "SHA512信息摘要测试~a!b@c#d$e%f^c&d*e(f)h`i1j2k3l4m5l6u7v8w9x0y-z=+";

        String clientDigest = "=hdXHaD=cuWFQD=JtUttC=TQ3iQB=E9WbVC=/IPicC=b6lBpD=iO6IVD=zFADQB=6E8MfD=ToSmhC=dJAA9D=xSNyVD=K315dB=nrp/m=Xw735";
        String serverDigest = Digest.digest(text);

        Assert.assertTrue(clientDigest.equals(serverDigest));
    }

    @Test
    public void digestSHA256() {
        String text = "SHA256信息摘要测试";

        String clientDigest = "=3e4o1D=/XnD3C=KdgbKC=E/+B5D=hZR67B=3vE8OC=noA5/B=nr2Zc";
        String serverDigest = Digest.digest("SHA-256", text);

        Assert.assertTrue(clientDigest.equals(serverDigest));
    }
}
