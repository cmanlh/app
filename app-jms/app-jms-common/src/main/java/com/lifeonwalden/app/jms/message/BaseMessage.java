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

package com.lifeonwalden.app.jms.message;

import com.lifeonwalden.app.jms.constant.JmsHeaders;
import com.lifeonwalden.app.util.map.MapBeanUtil;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BaseMessage implements Message {
    protected Map<String, Object> headers = new HashMap<>();
    protected Map<String, Object> properties = new HashMap<>();
    protected Object body;

    @Override
    public String getJMSMessageID() throws JMSException {
        if (headers.containsKey(JmsHeaders.MESSAGE_ID)) {
            return (String) headers.get(JmsHeaders.MESSAGE_ID);
        }
        return null;
    }

    @Override
    public void setJMSMessageID(String id) throws JMSException {
        headers.put(JmsHeaders.MESSAGE_ID, id);
    }

    @Override
    public long getJMSTimestamp() throws JMSException {
        if (headers.containsKey(JmsHeaders.TIMESTAMP)) {
            return (long) headers.get(JmsHeaders.TIMESTAMP);
        }
        return 0;
    }

    @Override
    public void setJMSTimestamp(long timestamp) throws JMSException {
        headers.put(JmsHeaders.TIMESTAMP, timestamp);
    }

    @Override
    public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
        String correlationId = getJMSCorrelationID();
        if (null != correlationId) {
            return Base64.getDecoder().decode(correlationId.getBytes(StandardCharsets.UTF_8));
        }
        return null;
    }

    @Override
    public void setJMSCorrelationIDAsBytes(byte[] correlationID) throws JMSException {
        setJMSCorrelationID(new String(Base64.getEncoder().encode(correlationID), StandardCharsets.UTF_8));
    }

    @Override
    public String getJMSCorrelationID() throws JMSException {
        if (headers.containsKey(JmsHeaders.CORRELATION_ID)) {
            return (String) headers.get(JmsHeaders.CORRELATION_ID);
        }
        return null;
    }

    @Override
    public void setJMSCorrelationID(String correlationID) throws JMSException {
        headers.put(JmsHeaders.CORRELATION_ID, correlationID);
    }

    @Override
    public Destination getJMSReplyTo() throws JMSException {
        if (headers.containsKey(JmsHeaders.REPLY_TO)) {
            return (Destination) headers.get(JmsHeaders.REPLY_TO);
        }
        return null;
    }

    @Override
    public void setJMSReplyTo(Destination replyTo) throws JMSException {
        headers.put(JmsHeaders.REPLY_TO, replyTo);
    }

    @Override
    public Destination getJMSDestination() throws JMSException {
        if (headers.containsKey(JmsHeaders.DESTINATION)) {
            return (Destination) headers.get(JmsHeaders.DESTINATION);
        }
        return null;
    }

    @Override
    public void setJMSDestination(Destination destination) throws JMSException {
        headers.put(JmsHeaders.DESTINATION, destination);
    }

    @Override
    public int getJMSDeliveryMode() throws JMSException {
        if (headers.containsKey(JmsHeaders.DELIVERY_MODE)) {
            return (int) headers.get(JmsHeaders.DELIVERY_MODE);
        }
        return 0;
    }

    @Override
    public void setJMSDeliveryMode(int deliveryMode) throws JMSException {
        headers.put(JmsHeaders.DELIVERY_MODE, deliveryMode);
    }

    @Override
    public boolean getJMSRedelivered() throws JMSException {
        if (headers.containsKey(JmsHeaders.REDELIVERED)) {
            return (boolean) headers.get(JmsHeaders.REDELIVERED);
        }
        return false;
    }

    @Override
    public void setJMSRedelivered(boolean redelivered) throws JMSException {
        headers.put(JmsHeaders.REDELIVERED, redelivered);
    }

    @Override
    public String getJMSType() throws JMSException {
        if (headers.containsKey(JmsHeaders.TYPE)) {
            return (String) headers.get(JmsHeaders.TYPE);
        }
        return null;
    }

    @Override
    public void setJMSType(String type) throws JMSException {
        headers.put(JmsHeaders.TYPE, type);
    }

    @Override
    public long getJMSExpiration() throws JMSException {
        if (headers.containsKey(JmsHeaders.EXPIRATION)) {
            return (long) headers.get(JmsHeaders.EXPIRATION);
        }
        return 0;
    }

    @Override
    public void setJMSExpiration(long expiration) throws JMSException {
        headers.put(JmsHeaders.EXPIRATION, expiration);
    }

    @Override
    public long getJMSDeliveryTime() throws JMSException {
        if (headers.containsKey(JmsHeaders.DELIVERY_TIME)) {
            return (long) headers.get(JmsHeaders.DELIVERY_TIME);
        }
        return 0;
    }

    @Override
    public void setJMSDeliveryTime(long deliveryTime) throws JMSException {
        headers.put(JmsHeaders.DELIVERY_TIME, deliveryTime);
    }

    @Override
    public int getJMSPriority() throws JMSException {
        if (headers.containsKey(JmsHeaders.PRIORITY)) {
            return (int) headers.get(JmsHeaders.PRIORITY);
        }
        return 5;
    }

    @Override
    public void setJMSPriority(int priority) throws JMSException {
        headers.put(JmsHeaders.PRIORITY, priority);
    }

    @Override
    public void clearProperties() throws JMSException {
        properties.clear();
    }

    @Override
    public boolean propertyExists(String name) throws JMSException {
        return properties.containsKey(name);
    }

    @Override
    public boolean getBooleanProperty(String name) throws JMSException {
        return MapBeanUtil.getBoolean(properties, name, false);
    }

    @Override
    public byte getByteProperty(String name) throws JMSException {
        return 0;
    }

    @Override
    public short getShortProperty(String name) throws JMSException {
        return 0;
    }

    @Override
    public int getIntProperty(String name) throws JMSException {
        return 0;
    }

    @Override
    public long getLongProperty(String name) throws JMSException {
        return 0;
    }

    @Override
    public float getFloatProperty(String name) throws JMSException {
        return 0;
    }

    @Override
    public double getDoubleProperty(String name) throws JMSException {
        return 0;
    }

    @Override
    public String getStringProperty(String name) throws JMSException {
        return null;
    }

    @Override
    public Object getObjectProperty(String name) throws JMSException {
        return properties.get(name);
    }

    @Override
    public Enumeration getPropertyNames() throws JMSException {
        return null;
    }

    @Override
    public void setBooleanProperty(String name, boolean value) throws JMSException {

    }

    @Override
    public void setByteProperty(String name, byte value) throws JMSException {

    }

    @Override
    public void setShortProperty(String name, short value) throws JMSException {

    }

    @Override
    public void setIntProperty(String name, int value) throws JMSException {

    }

    @Override
    public void setLongProperty(String name, long value) throws JMSException {

    }

    @Override
    public void setFloatProperty(String name, float value) throws JMSException {

    }

    @Override
    public void setDoubleProperty(String name, double value) throws JMSException {

    }

    @Override
    public void setStringProperty(String name, String value) throws JMSException {

    }

    @Override
    public void setObjectProperty(String name, Object value) throws JMSException {

    }

    @Override
    public void acknowledge() throws JMSException {

    }

    @Override
    public void clearBody() throws JMSException {

    }

    @Override
    public <T> T getBody(Class<T> c) throws JMSException {
        return null;
    }

    @Override
    public boolean isBodyAssignableTo(Class c) throws JMSException {
        return false;
    }
}
