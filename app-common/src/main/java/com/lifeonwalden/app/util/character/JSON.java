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

package com.lifeonwalden.app.util.character;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.lifeonwalden.app.util.date.DateUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

public class JSON {
    public final static ObjectMapper mobileMapper = new ObjectMapper();
    public final static ObjectMapper defaultMapper = new ObjectMapper();
    public final static ObjectMapper loggerMapper = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule("DateFormat");
        module.addSerializer(Date.class, new StdSerializer<Date>(Date.class) {
            private static final long serialVersionUID = -3282010210198556897L;

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(String.valueOf(value.getTime()));
            }
        });
        module.addSerializer(BigDecimal.class, new StdSerializer<BigDecimal>(BigDecimal.class) {
            private static final long serialVersionUID = -6469200275819994117L;

            @Override
            public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(value.toPlainString());
            }
        });
        defaultMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        defaultMapper.registerModule(module);

        SimpleModule mobileModule = new SimpleModule("DateFormat");
        mobileModule.addSerializer(Date.class, new StdSerializer<Date>(Date.class) {
            private static final long serialVersionUID = -209783685850606761L;

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(DateUtil.formatDate(value, DateUtil.FULL_VIEW_DATE));
            }
        });
        mobileModule.addSerializer(BigDecimal.class, new StdSerializer<BigDecimal>(BigDecimal.class) {
            private static final long serialVersionUID = -1232767766775708212L;

            @Override
            public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(value.toPlainString());
            }
        });
        mobileMapper.registerModule(mobileModule);


        SimpleModule logModule = new SimpleModule("logModule");
        logModule.addSerializer(Date.class, new StdSerializer<Date>(Date.class) {
            private static final long serialVersionUID = -7411132246652088371L;

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(DateUtil.formatDate(value, DateUtil.VIEW_DHMS));
            }
        });
        loggerMapper.registerModule(logModule);
    }

    public static String log(Object object) {
        try {
            return loggerMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public static String writeValueAsString(Object object) {
        try {
            return defaultMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        try {
            return defaultMapper.readValue(content, valueTypeRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return defaultMapper.readValue(content, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
