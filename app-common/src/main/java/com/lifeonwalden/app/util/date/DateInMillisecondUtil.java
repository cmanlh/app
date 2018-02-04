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

package com.lifeonwalden.app.util.date;

public interface DateInMillisecondUtil {
    long MILL_SEC_OF_DAY = 1000 * 3600 * 24;

    /**
     * 获取两个日期之间日天数
     * daysBetween(2015-10-01, 2015-10-02) = 1
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return the new time
     */
    static long daysBetween(long start, long end) {

        long startInMs = trim(start);
        long endInMs = trim(end);

        return (endInMs - startInMs) / MILL_SEC_OF_DAY;
    }

    /**
     * 获得某日期零点时的毫秒数
     *
     * @param millsec target time
     * @return the new time
     */
    static long trim(final long millsec) {
        return millsec - (millsec - 57600000) % 86400000;
    }

    /**
     * @param millsec target time
     * @param days    variable
     * @return the new time
     */
    static long plus(final long millsec, final int days) {
        return millsec + days * 86400000L;
    }

    /**
     * @param millsec target time
     * @param days    variable
     * @return the new time
     */
    static long minus(final long millsec, final int days) {
        return millsec - days * 86400000L;
    }

    static long daysTo(final int days) {
        return days * 86400000L;
    }

    static long daysTo(final long days) {
        return days * 86400000L;
    }

    static int diffOnDay(final long end, final long start) {
        return (int) ((trim(end) - trim(start)) / 86400000L);
    }
}
