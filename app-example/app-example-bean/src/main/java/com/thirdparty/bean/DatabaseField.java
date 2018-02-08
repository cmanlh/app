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

package com.thirdparty.bean;

import com.lifeonwalden.forestbatis.biz.bean.AbstractDTOMapBean;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;

public class DatabaseField extends AbstractDTOMapBean<DatabaseField> {
    private static final long serialVersionUID = 599491651L;

    public static final String RequiredField = "requiredField";

    public static final String OptionalField = "optionalField";

    public static final String CreateTime = "createTime";

    public static final String CreateUser = "createUser";

    public static final String UpdateTime = "updateTime";

    public static final String UpdateUser = "updateUser";

    public static final String LogicalDel = "logicalDel";

    /**
     * required field */
    public DatabaseField setRequiredField(String requiredField) {
        dataMap.put("requiredField",requiredField);
        return this;
    }

    /**
     * required field */
    public String getRequiredField() {
        Object val = dataMap.get("requiredField");
        if (null == val) {
            return null;
        }
        return (String)val;
    }

    /**
     * optional field */
    public DatabaseField setOptionalField(String optionalField) {
        dataMap.put("optionalField",optionalField);
        return this;
    }

    /**
     * optional field */
    public String getOptionalField() {
        Object val = dataMap.get("optionalField");
        if (null == val) {
            return null;
        }
        return (String)val;
    }

    /**
     * 创建时间 */
    public DatabaseField setCreateTime(Date createTime) {
        dataMap.put("createTime",createTime);
        return this;
    }

    /**
     * 创建时间 */
    public Date getCreateTime() {
        Object val = dataMap.get("createTime");
        if (null == val) {
            return null;
        }
        return (Date)val;
    }

    /**
     * 创建时间 */
    public DatabaseField setCreateTimeStart(Date createTimeStart) {
        dataMap.put("createTimeStart",createTimeStart);
        return this;
    }

    /**
     * 创建时间 */
    public Date getCreateTimeStart() {
        Object val = dataMap.get("createTimeStart");
        if (null == val) {
            return null;
        }
        return (Date)val;
    }

    /**
     * 创建时间 */
    public DatabaseField setCreateTimeEnd(Date createTimeEnd) {
        dataMap.put("createTimeEnd",createTimeEnd);
        return this;
    }

    /**
     * 创建时间 */
    public Date getCreateTimeEnd() {
        Object val = dataMap.get("createTimeEnd");
        if (null == val) {
            return null;
        }
        return (Date)val;
    }

    /**
     * 创建者 */
    public DatabaseField setCreateUser(String createUser) {
        dataMap.put("createUser",createUser);
        return this;
    }

    /**
     * 创建者 */
    public String getCreateUser() {
        Object val = dataMap.get("createUser");
        if (null == val) {
            return null;
        }
        return (String)val;
    }

    /**
     * 更新时间 */
    public DatabaseField setUpdateTime(Date updateTime) {
        dataMap.put("updateTime",updateTime);
        return this;
    }

    /**
     * 更新时间 */
    public Date getUpdateTime() {
        Object val = dataMap.get("updateTime");
        if (null == val) {
            return null;
        }
        return (Date)val;
    }

    /**
     * 更新时间 */
    public DatabaseField setUpdateTimeStart(Date updateTimeStart) {
        dataMap.put("updateTimeStart",updateTimeStart);
        return this;
    }

    /**
     * 更新时间 */
    public Date getUpdateTimeStart() {
        Object val = dataMap.get("updateTimeStart");
        if (null == val) {
            return null;
        }
        return (Date)val;
    }

    /**
     * 更新时间 */
    public DatabaseField setUpdateTimeEnd(Date updateTimeEnd) {
        dataMap.put("updateTimeEnd",updateTimeEnd);
        return this;
    }

    /**
     * 更新时间 */
    public Date getUpdateTimeEnd() {
        Object val = dataMap.get("updateTimeEnd");
        if (null == val) {
            return null;
        }
        return (Date)val;
    }

    /**
     * 更新者 */
    public DatabaseField setUpdateUser(String updateUser) {
        dataMap.put("updateUser",updateUser);
        return this;
    }

    /**
     * 更新者 */
    public String getUpdateUser() {
        Object val = dataMap.get("updateUser");
        if (null == val) {
            return null;
        }
        return (String)val;
    }

    /**
     * 逻辑删除 */
    public DatabaseField setLogicalDel(Integer logicalDel) {
        dataMap.put("logicalDel",logicalDel);
        return this;
    }

    /**
     * 逻辑删除 */
    public Integer getLogicalDel() {
        Object val = dataMap.get("logicalDel");
        if (null == val) {
            return null;
        }
        return (Integer)val;
    }
}
