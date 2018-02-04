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

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface DateUtil {
    String FULL_SHORT_DATE = "yyyyMMdd";

    String FULL_VIEW_DATE = "yyyy-MM-dd";

    String SHORT_DATE = "yyyy-M-d";

    String VIEW_DHM = "yyyy-MM-dd HH:mm";

    String VIEW_DHMS = "yyyy-MM-dd HH:mm:ss";

    static Date parseDate(String dateStr, String format) {
        if (StringUtils.isNotBlank(dateStr)) {
            SimpleDateFormat fmt = new SimpleDateFormat(format);
            try {
                return fmt.parse(dateStr);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    static long parseDate2Long(String dateStr, String format) {
        Date date = parseDate(dateStr, format);

        return null == date ? 0 : date.getTime();
    }

    static String formatDate(long date, String format) {
        if (date > 0) {
            return formatDate(new Date(date), format);
        }

        return StringUtils.EMPTY;
    }

    static String formatDate(Date date, String format) {
        if (date != null) {
            SimpleDateFormat fmt = new SimpleDateFormat(format);
            return fmt.format(date);
        }

        return StringUtils.EMPTY;
    }
}
