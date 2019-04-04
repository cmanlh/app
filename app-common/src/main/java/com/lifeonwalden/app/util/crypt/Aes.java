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

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface Aes {
    static String generateIv() {
        return CharUtil.bytesToU64(RandomUtils.nextBytes(16));
    }

    static String generateWithEncode(int size) {
        return CharUtil.bytesToU64(generateKey(size).getEncoded());
    }

    static SecretKey generateKey(int size) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(size);

            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    static String encode(SecretKey key) {
        return CharUtil.bytesToU64(key.getEncoded());
    }

    static SecretKey decode(String keyData) {
        return new SecretKeySpec(CharUtil.u64ToBytes(keyData), "AES");
    }

    static String encrypt(String key, String iv, String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            byte[] ivArray = CharUtil.u64ToBytes(iv);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(ivArray.length * 8, ivArray);
            cipher.init(Cipher.ENCRYPT_MODE, decode(key), gcmParameterSpec);

            return CharUtil.bytesToU64(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    static String decrypt(String key, String iv, String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            byte[] ivArray = CharUtil.u64ToBytes(iv);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(ivArray.length * 8, ivArray);
            cipher.init(Cipher.DECRYPT_MODE, decode(key), gcmParameterSpec);

            return new String(cipher.doFinal(CharUtil.u64ToBytes(encryptedText)), StandardCharsets.UTF_8);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }
}
