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

package com.lifeonwalden.app.util.map;

import com.lifeonwalden.app.util.date.DateUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface MapBeanUtil {
    static <T> T get(Map<String, Object> map, String key, Class<T> clazz) {
        if (null == map) {
            return null;
        }

        Object value = map.get(key);
        if (null != value) {
            return (T) value;
        }

        return null;
    }

    static <T> T get(Map<String, Object> map, String key, Class<T> clazz, T defaultValue) {
        T value = get(map, key, clazz);
        return null == value ? defaultValue : value;
    }

    static Boolean getBoolean(Map<String, Object> map, String key) {
        if (null == map) {
            return null;
        }
        Object value = map.get(key);
        if (null != value) {
            return value instanceof Boolean ? (Boolean) value : Boolean.valueOf(value.toString());
        }

        return null;
    }

    static Boolean getBoolean(Map<String, Object> map, String key, Boolean defaultValue) {
        Boolean value = getBoolean(map, key);
        return null == value ? defaultValue : value;
    }

    static Date getDate(Map<String, Object> map, String key) {
        if (null == map) {
            return null;
        }

        Object value = map.get(key);
        if (null != value) {
            if (value.getClass().isPrimitive() || value instanceof Long) {
                return new Date((long) value);
            } else if (value instanceof String) {
                return DateUtil.parseDate((String) value, DateUtil.FULL_VIEW_DATE);
            } else if (value instanceof Date) {
                return (Date) value;
            } else {
                throw new RuntimeException("invalid parameter type.");
            }
        }

        return null;
    }

    static Date getDate(Map<String, Object> map, String key, Date defaultValue) {
        Date value = getDate(map, key);
        return null == value ? defaultValue : value;
    }

    static BigDecimal getBigDecimal(Map<String, Object> map, String key) {
        if (null == map) {
            return null;
        }

        Object value = map.get(key);
        if (null != value) {
            if (value.getClass().isPrimitive() && value instanceof Long) {
                return BigDecimal.valueOf((Long) value);
            } else if (value.getClass().isPrimitive() && value instanceof Double) {
                return BigDecimal.valueOf((Double) value);
            } else if (value instanceof String) {
                return new BigDecimal((String) value);
            } else if (value instanceof BigDecimal) {
                return (BigDecimal) value;
            } else {
                throw new RuntimeException("invalid parameter type.");
            }
        }

        return null;
    }

    static BigDecimal getBigDecimal(Map<String, Object> map, String key, BigDecimal defaultValue) {
        BigDecimal value = getBigDecimal(map, key);
        return null == value ? defaultValue : value;
    }
}
