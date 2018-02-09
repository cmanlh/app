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
import java.util.List;

public class Enable extends AbstractDTOMapBean<Enable> {
    private static final long serialVersionUID = 90205195L;

    public static final String Like1 = "like1";

    public static final String Like0 = "like0";

    public static final String NotLike1 = "notLike1";

    public static final String NotLike0 = "notLike0";

    public static final String LikeFake = "likeFake";

    public static final String NotLikeFake = "notLikeFake";

    public static final String In1 = "in1";

    public static final String In0 = "in0";

    public static final String NotIn1 = "notIn1";

    public static final String NotIn0 = "notIn0";

    public static final String FullEnable = "fullEnable";

    public Enable setLike1(String like1) {
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

    public Enable setLike1Like(String like1Like) {
        dataMap.put("like1Like",like1Like);
        return this;
    }

    public String getLike1Like() {
        Object val = dataMap.get("like1Like");
        if (null == val) {
            return null;
        }
        return (String)val;
    }

    public Enable setLike0(String like0) {
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

    public Enable setNotLike1(String notLike1) {
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

    public Enable setNotLike1NotLike(String notLike1NotLike) {
        dataMap.put("notLike1NotLike",notLike1NotLike);
        return this;
    }

    public String getNotLike1NotLike() {
        Object val = dataMap.get("notLike1NotLike");
        if (null == val) {
            return null;
        }
        return (String)val;
    }

    public Enable setNotLike0(String notLike0) {
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

    public Enable setLikeFake(Integer likeFake) {
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

    public Enable setNotLikeFake(Integer notLikeFake) {
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

    public Enable setIn1(Integer in1) {
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

    public Enable setIn1In(List<Integer> in1In) {
        dataMap.put("in1In",in1In);
        return this;
    }

    public List<Integer> getIn1In() {
        Object val = dataMap.get("in1In");
        if (null == val) {
            return null;
        }
        return (List<Integer>)val;
    }

    public Enable setIn0(Integer in0) {
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

    public Enable setNotIn1(Integer notIn1) {
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

    public Enable setNotIn1NotIn(List<Integer> notIn1NotIn) {
        dataMap.put("notIn1NotIn",notIn1NotIn);
        return this;
    }

    public List<Integer> getNotIn1NotIn() {
        Object val = dataMap.get("notIn1NotIn");
        if (null == val) {
            return null;
        }
        return (List<Integer>)val;
    }

    public Enable setNotIn0(Integer notIn0) {
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

    public Enable setFullEnable(String fullEnable) {
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

    public Enable setFullEnableIn(List<String> fullEnableIn) {
        dataMap.put("fullEnableIn",fullEnableIn);
        return this;
    }

    public List<String> getFullEnableIn() {
        Object val = dataMap.get("fullEnableIn");
        if (null == val) {
            return null;
        }
        return (List<String>)val;
    }

    public Enable setFullEnableNotIn(List<String> fullEnableNotIn) {
        dataMap.put("fullEnableNotIn",fullEnableNotIn);
        return this;
    }

    public List<String> getFullEnableNotIn() {
        Object val = dataMap.get("fullEnableNotIn");
        if (null == val) {
            return null;
        }
        return (List<String>)val;
    }

    public Enable setFullEnableLike(String fullEnableLike) {
        dataMap.put("fullEnableLike",fullEnableLike);
        return this;
    }

    public String getFullEnableLike() {
        Object val = dataMap.get("fullEnableLike");
        if (null == val) {
            return null;
        }
        return (String)val;
    }

    public Enable setFullEnableNotLike(String fullEnableNotLike) {
        dataMap.put("fullEnableNotLike",fullEnableNotLike);
        return this;
    }

    public String getFullEnableNotLike() {
        Object val = dataMap.get("fullEnableNotLike");
        if (null == val) {
            return null;
        }
        return (String)val;
    }
}
