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

package com.lifeonwalden.app.jms.producer;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.*;
import java.util.List;

public class Producer {
    private JmsTemplate jmsTemplate;

    public Producer(ConnectionFactory connectionFactory, Destination destination, MessageConverter messageConverter) {
        jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setDefaultDestination(destination);
        jmsTemplate.setMessageConverter(messageConverter);
        jmsTemplate.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        jmsTemplate.setDeliveryPersistent(false);
        jmsTemplate.setExplicitQosEnabled(true);
    }

    public void send(Object object) {
        if (null != object) {
            Class<?> clazz = object.getClass();

            if (clazz.isArray() || List.class.isInstance(object)) {
                throw new RuntimeException("不接受数组和List类型参数.");
            }
        }

        jmsTemplate.send(session -> jmsTemplate.getMessageConverter().toMessage(object, session));
    }
}
