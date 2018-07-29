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

package com.lifeonwalden.app.gateway.auth.session;

import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.util.CollectionUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface AppSessionHelper {
    int ID_BIT_MASK = 1 << 0;
    int START_TIMESTAMP_BIT_MASK = 1 << 1;
    int STOP_TIMESTAMP_BIT_MASK = 1 << 2;
    int LAST_ACCESS_TIME_BIT_MASK = 1 << 3;
    int TIMEOUT_BIT_MASK = 1 << 4;
    int EXPIRED_BIT_MASK = 1 << 5;
    int HOST_BIT_MASK = 1 << 6;
    int ATTRIBUTES_BIT_MASK = 1 << 7;

    static void writeObject(ObjectOutputStream out, SimpleSession session) throws IOException {
        out.writeObject(session);
    }

    static SimpleSession readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        return (SimpleSession) in.readObject();
    }

    static boolean isFieldPresent(short bitMask, int fieldBitMask) {
        return (bitMask & fieldBitMask) != 0;
    }

    static short getAlteredFieldsBitMask(SimpleSession session) {
        int bitMask = 0;
        bitMask = session.getId() != null ? bitMask | ID_BIT_MASK : bitMask;
        bitMask = session.getStartTimestamp() != null ? bitMask | START_TIMESTAMP_BIT_MASK : bitMask;
        bitMask = session.getStopTimestamp() != null ? bitMask | STOP_TIMESTAMP_BIT_MASK : bitMask;
        bitMask = session.getLastAccessTime() != null ? bitMask | LAST_ACCESS_TIME_BIT_MASK : bitMask;
        bitMask = session.getTimeout() != 0l ? bitMask | TIMEOUT_BIT_MASK : bitMask;
        bitMask = session.isExpired() ? bitMask | EXPIRED_BIT_MASK : bitMask;
        bitMask = session.getHost() != null ? bitMask | HOST_BIT_MASK : bitMask;
        bitMask = !CollectionUtils.isEmpty(session.getAttributes()) ? bitMask | ATTRIBUTES_BIT_MASK : bitMask;
        return (short) bitMask;
    }
}
