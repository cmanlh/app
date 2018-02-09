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

import com.lifeonwalden.forestbatis.biz.bean.AbstractParamMapBean;
import java.lang.Integer;
import java.lang.String;

public class EnableParam extends AbstractParamMapBean {
    private static final long serialVersionUID = 135184888L;

    static {
        typeMap.put("likeFake", Integer.class);
        typeMap.put("notLikeFake", Integer.class);
        typeMap.put("in1", Integer.class);
        typeMap.put("in0", Integer.class);
        typeMap.put("notIn1", Integer.class);
        typeMap.put("notIn0", Integer.class);
    }

    public EnableParam setLike1(String like1) {
        dataMap.put("like1",like1);
        return this;
    }

    public String getLike1() {
        Object val = dataMap.get("like1");
        if (null == val) {
            return null;
        }
        return (String)val;
    }

    public EnableParam setLike0(String like0) {
        dataMap.put("like0",like0);
        return this;
    }

    public String getLike0() {
        Object val = dataMap.get("like0");
        if (null == val) {
            return null;
        }
        return (String)val;
    }

    public EnableParam setNotLike1(String notLike1) {
        dataMap.put("notLike1",notLike1);
        return this;
    }

    public String getNotLike1() {
        Object val = dataMap.get("notLike1");
        if (null == val) {
            return null;
        }
        return (String)val;
    }

    public EnableParam setNotLike0(String notLike0) {
        dataMap.put("notLike0",notLike0);
        return this;
    }

    public String getNotLike0() {
        Object val = dataMap.get("notLike0");
        if (null == val) {
            return null;
        }
        return (String)val;
    }

    public EnableParam setLikeFake(Integer likeFake) {
        dataMap.put("likeFake",likeFake);
        return this;
    }

    public Integer getLikeFake() {
        Object val = dataMap.get("likeFake");
        if (null == val) {
            return null;
        }
        return (Integer)val;
    }

    public EnableParam setNotLikeFake(Integer notLikeFake) {
        dataMap.put("notLikeFake",notLikeFake);
        return this;
    }

    public Integer getNotLikeFake() {
        Object val = dataMap.get("notLikeFake");
        if (null == val) {
            return null;
        }
        return (Integer)val;
    }

    public EnableParam setIn1(Integer in1) {
        dataMap.put("in1",in1);
        return this;
    }

    public Integer getIn1() {
        Object val = dataMap.get("in1");
        if (null == val) {
            return null;
        }
        return (Integer)val;
    }

    public EnableParam setIn0(Integer in0) {
        dataMap.put("in0",in0);
        return this;
    }

    public Integer getIn0() {
        Object val = dataMap.get("in0");
        if (null == val) {
            return null;
        }
        return (Integer)val;
    }

    public EnableParam setNotIn1(Integer notIn1) {
        dataMap.put("notIn1",notIn1);
        return this;
    }

    public Integer getNotIn1() {
        Object val = dataMap.get("notIn1");
        if (null == val) {
            return null;
        }
        return (Integer)val;
    }

    public EnableParam setNotIn0(Integer notIn0) {
        dataMap.put("notIn0",notIn0);
        return this;
    }

    public Integer getNotIn0() {
        Object val = dataMap.get("notIn0");
        if (null == val) {
            return null;
        }
        return (Integer)val;
    }

    public EnableParam setFullEnable(String fullEnable) {
        dataMap.put("fullEnable",fullEnable);
        return this;
    }

    public String getFullEnable() {
        Object val = dataMap.get("fullEnable");
        if (null == val) {
            return null;
        }
        return (String)val;
    }
}
