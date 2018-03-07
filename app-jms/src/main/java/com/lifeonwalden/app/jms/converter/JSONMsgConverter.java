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

package com.lifeonwalden.app.jms.converter;

import com.lifeonwalden.app.jms.bean.CrossMsg;
import com.lifeonwalden.app.util.character.JSON;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.List;

public class JSONMsgConverter implements MessageConverter {

    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        CrossMsg msg = new CrossMsg();
        if (null != object) {
            Class<?> clazz = object.getClass();
            if (clazz.isArray() || List.class.isInstance(object)) {
                throw new RuntimeException("不接受数组和List类型参数.");
            }
            msg.setClazz(clazz);
            msg.setData(JSON.writeValueAsString(object));
        }
        return session.createTextMessage(JSON.writeValueAsString(msg));
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        CrossMsg msg = JSON.readValue(((TextMessage) message).getText(), CrossMsg.class);
        if (msg.getClazz() == null) {
            return null;
        }

        return JSON.readValue(msg.getData(), msg.getClazz());
    }

}
