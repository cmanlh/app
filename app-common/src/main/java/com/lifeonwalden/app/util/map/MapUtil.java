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

import java.util.*;

public interface MapUtil {
    /**
     * shallow copy the src map to new one
     *
     * @param src
     * @param toClass
     * @param <K>
     * @param <V>
     * @param <T>
     * @return
     */
    static <K, V, T extends Map<K, V>> T shallowCopy(Map<K, V> src, Class<T> toClass) {
        T target = null;
        try {
            target = toClass.newInstance();
            for (Map.Entry<K, V> entry : src.entrySet()) {
                target.put(entry.getKey(), entry.getValue());
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return target;
    }

    /**
     * merge src mapping key/value that are not exist in origin mapping to origin mapping
     *
     * @param origin
     * @param src
     * @param allowedNull true allow to copy null value to origin mapping, false is not
     * @param <K>
     * @param <V>
     * @return
     */
    static <K, V> Map<K, V> merge(Map<K, V> origin, Map<K, V> src, boolean allowedNull) {
        src.forEach((key, value) -> {
            if (null == origin.get(key)) {
                if (allowedNull) {
                    origin.put(key, value);
                } else {
                    if (null != value) {
                        origin.put(key, value);
                    }
                }
            }
        });

        return origin;
    }

    /**
     * merge src mapping key/value that are not exist in origin mapping to origin mapping
     *
     * @param origin
     * @param src
     * @param <K>
     * @param <V>
     * @return
     */
    static <K, V> Map<K, V> merge(Map<K, V> origin, Map<K, V> src) {
        return merge(origin, src, true);
    }


    /**
     * merge src mapping key/value that are not exist in origin mapping to origin mapping without some keys
     *
     * @param origin
     * @param src
     * @param allowedNull    true allow to copy null value to origin mapping, false is not
     * @param withoutKeyList
     * @param <K>
     * @param <V>
     * @return
     */
    static <K, V> Map<K, V> mergeWithout(Map<K, V> origin, Map<K, V> src, boolean allowedNull, List<K> withoutKeyList) {
        Set<K> withoutKeySet = new TreeSet<>();
        withoutKeySet.addAll(withoutKeyList);

        src.forEach((key, value) -> {
            if (withoutKeySet.contains(key)) {
                return;
            }
            if (null == origin.get(key)) {
                if (allowedNull) {
                    origin.put(key, value);
                } else {
                    if (null != value) {
                        origin.put(key, value);
                    }
                }
            }
        });

        return origin;
    }

    /**
     * merge src mapping key/value that are not exist in origin mapping to origin mapping without some keys
     *
     * @param origin
     * @param src
     * @param allowedNull true allow to copy null value to origin mapping, false is not
     * @param withoutKeys
     * @param <K>
     * @param <V>
     * @return
     */
    static <K, V> Map<K, V> mergeWithout(Map<K, V> origin, Map<K, V> src, boolean allowedNull, K... withoutKeys) {
        return mergeWithout(origin, src, allowedNull, Arrays.asList(withoutKeys));
    }

    /**
     * merge src mapping key/value that are not exist in origin mapping to origin mapping without some keys
     *
     * @param origin
     * @param src
     * @param withoutKeyList
     * @param <K>
     * @param <V>
     * @return
     */
    static <K, V> Map<K, V> mergeWithout(Map<K, V> origin, Map<K, V> src, List<K> withoutKeyList) {
        return mergeWithout(origin, src, true, withoutKeyList);
    }

    /**
     * merge src mapping key/value that are not exist in origin mapping to origin mapping without some keys
     *
     * @param origin
     * @param src
     * @param withoutKeys
     * @param <K>
     * @param <V>
     * @return
     */
    static <K, V> Map<K, V> mergeWithout(Map<K, V> origin, Map<K, V> src, K... withoutKeys) {
        return mergeWithout(origin, src, true, Arrays.asList(withoutKeys));
    }
}
