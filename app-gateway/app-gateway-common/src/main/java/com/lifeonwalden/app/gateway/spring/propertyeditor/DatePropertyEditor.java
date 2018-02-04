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

package com.lifeonwalden.app.gateway.spring.propertyeditor;

import com.lifeonwalden.app.util.date.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyEditorSupport;
import java.util.Date;


public class DatePropertyEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        Object value = getValue();

        if (null != value) {
            return String.valueOf(((Date) value).getTime());
        }

        return null;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isNumeric(text)) {
            setValue(new Date(Long.parseLong(text)));
        } else {
            setValue(DateUtil.parseDate(text, DateUtil.FULL_VIEW_DATE));
        }
    }
}

