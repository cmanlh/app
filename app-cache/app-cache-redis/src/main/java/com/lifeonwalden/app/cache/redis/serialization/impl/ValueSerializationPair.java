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

package com.lifeonwalden.app.cache.redis.serialization.impl;

import com.lifeonwalden.app.cache.redis.serialization.SerializationPair;
import com.lifeonwalden.app.util.logger.LoggerUtil;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class ValueSerializationPair implements SerializationPair<Object> {
    private final static Logger logger = LoggerUtil.getLogger(ValueSerializationPair.class);

    @Override
    public byte[] serialize(Object target) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(target);
            oos.flush();
        } catch (IOException e) {
            logger.error("Failed to serialize object", e);

            throw new RuntimeException(e);
        }

        return baos.toByteArray();
    }

    @Override
    public Object deserialize(byte[] target) {
        ByteArrayInputStream bais = new ByteArrayInputStream(target);
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (IOException | SecurityException | ClassNotFoundException e) {
            logger.error("Failed to deserialize object", e);

            throw new RuntimeException(e);
        }
    }
}
