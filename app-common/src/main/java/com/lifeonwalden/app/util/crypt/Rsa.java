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
import com.lifeonwalden.app.util.crypt.bean.KeyPairBean;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.security.rsa.RSAPrivateKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;
import sun.security.util.DerValue;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.RSAKeyGenParameterSpec;

public interface Rsa {
    static KeyPair generateKey(int size) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            long publicExponent = 1 << 16;
            publicExponent |= 1;
            RSAKeyGenParameterSpec rsaKeyGenParameterSpec = new RSAKeyGenParameterSpec(size, BigInteger.valueOf(publicExponent));
            keyPairGenerator.initialize(rsaKeyGenParameterSpec);

            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    static KeyPairBean generateKeyWithEncode(int size) {
        KeyPair keyPair = generateKey(size);

        return new KeyPairBean()
                .setPrivateKey(CharUtil.bytesToU64(keyPair.getPrivate().getEncoded()))
                .setPublicKey(CharUtil.bytesToU64(keyPair.getPublic().getEncoded()));
    }

    static String decrypt(String privateKey, String encryptedText) {
        Security.addProvider(new BouncyCastleProvider());

        try {
            Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, RSAPrivateKeyImpl.parseKey(new DerValue(CharUtil.u64ToBytes(privateKey))));

            return new String(cipher.doFinal(CharUtil.u64ToBytes(encryptedText)), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    static String encrypt(String publicKey, String originalText) {
        Security.addProvider(new BouncyCastleProvider());

        try {
            Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new RSAPublicKeyImpl(CharUtil.u64ToBytes(publicKey)));

            return CharUtil.bytesToU64(cipher.doFinal(originalText.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }
}
