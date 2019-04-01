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

import org.apache.commons.lang3.ArrayUtils;

public class CharUtil {
    private static char[] CHAR_SET = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
            'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', '+', '/'};

    private static int[] CHAR_MAP = new int[128];

    static {
        for (int i = 0; i < CHAR_SET.length; i++) {
            CHAR_MAP[CHAR_SET[i]] = i;
        }
    }

    public static String bytesToU64(byte[] buffer) {
        StringBuilder u64Buffer = new StringBuilder();
        int leftBitLength = (buffer.length * 8) % 32;
        int uint32Count = (int) Math.round(Math.floor(buffer.length / 4));
        if (0 == leftBitLength) {
            for (int i = 0; i < uint32Count; i++) {
                u64Buffer.append("=").append(intByteToU64(ArrayUtils.subarray(buffer, i * 4, i * 4 + 4)));
            }
        } else {
            u64Buffer.append("=-".concat(String.valueOf(uint32Count + 2)));
            if (uint32Count > 0) {
                for (int i = 0; i < uint32Count; i++) {
                    u64Buffer.append("=").append(intByteToU64(ArrayUtils.subarray(buffer, i * 4, i * 4 + 4)));
                }
            }

            for (int i = uint32Count * 4; i < buffer.length; i++) {
                u64Buffer.append("=").append(uintToU64(buffer[i]));
            }
        }
        return u64Buffer.toString();
    }

    public static byte[] u64ToBytes(String u64Text) {
        String[] u64Array = u64Text.split("=");
        if (u64Array[1].indexOf('-') > -1) {
            int uInt8Index = Integer.parseInt(u64Array[1].substring(1));
            if (2 == uInt8Index) {
                byte[] buffer = new byte[u64Array.length - 2];
                int startIdx = 0;
                for (int i = 2; i < u64Array.length; i++) {
                    buffer[startIdx++] = (byte) (u64ToUint(u64Array[i]) & 255);
                }

                return buffer;
            } else {
                byte[] buffer = new byte[(uInt8Index - 2) * 4];
                for (int i = 2; i < uInt8Index; i++) {
                    byte[] tmp = u64ToIntByte(u64Array[i]);

                    int index = (i - 2) * 4;
                    buffer[index] = tmp[0];
                    buffer[index + 1] = tmp[1];
                    buffer[index + 2] = tmp[2];
                    buffer[index + 3] = tmp[3];
                }
                int size = u64Array.length;
                int uInt8IndexStart = (uInt8Index - 2) * 4;
                for (int k = uInt8Index; k < size; k++) {
                    buffer[uInt8IndexStart++] = (byte) (u64ToUint(u64Array[k]) & 255);
                }

                return buffer;
            }
        } else {
            int size = u64Array.length;
            byte[] buffer = new byte[(size - 1) * 4];
            for (int i = 1; i < size; i++) {
                byte[] tmp = u64ToIntByte(u64Array[i]);

                int index = (i - 1) * 4;
                buffer[index] = tmp[0];
                buffer[index + 1] = tmp[1];
                buffer[index + 2] = tmp[2];
                buffer[index + 3] = tmp[3];
            }

            return buffer;
        }
    }

    public static String textToU64(String text) {
        StringBuilder buffer = new StringBuilder();
        int size = text.length();

        for (int i = 0; i < size; i++) {
            buffer.append("=").append(uintToU64(text.codePointAt(i)));
        }

        return buffer.toString();
    }

    public static String u64ToText(String u64Text) {
        StringBuilder buffer = new StringBuilder();
        String[] charArray = u64Text.split("=");
        int size = charArray.length;
        for (int i = 1; i < size; i++) {
            buffer.append(u64ToChar(charArray[i]));
        }

        return buffer.toString();
    }

    public static String uintToU64(int val) {
        StringBuilder buffer = new StringBuilder();
        int mask = 63;
        do {
            buffer.append(CHAR_SET[val & mask]);
            val >>>= 6;
        } while (val != 0);

        return buffer.toString();
    }

    public static char u64ToChar(String u64) {
        int len = u64.length(), val = 0;
        for (int i = 0; i < len; i++) {
            val |= CHAR_MAP[u64.charAt(i)] << (i * 6);
        }

        return (char) val;
    }

    public static int u64ToUint(String u64) {
        int len = u64.length(), val = 0;
        for (int i = 0; i < len; i++) {
            val |= CHAR_MAP[u64.charAt(i)] << (i * 6);
        }

        return val;
    }

    public static byte[] u64ToIntByte(String u64) {
        int len = u64.length(), val = 0;
        for (int i = 0; i < len; i++) {
            val |= CHAR_MAP[u64.charAt(i)] << (i * 6);
        }

        byte[] buffer = new byte[4];
        buffer[0] = (byte) (val >>> 24);
        buffer[1] = (byte) (val >>> 16 & 255);
        buffer[2] = (byte) (val >>> 8 & 255);
        buffer[3] = (byte) (val & 255);

        return buffer;
    }

    public static String intByteToU64(byte[] val) {
        int codePoint = 0;
        codePoint |= (val[0] & 255) << 24;
        codePoint |= (val[1] & 255) << 16;
        codePoint |= (val[2] & 255) << 8;
        codePoint |= val[3] & 255;

        StringBuilder buffer = new StringBuilder();
        int mask = 63;
        do {
            buffer.append(CHAR_SET[codePoint & mask]);
            codePoint >>>= 6;
        } while (codePoint != 0);

        return buffer.toString();
    }
}
