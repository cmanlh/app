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

package com.lifeonwalden.app.gateway.auth.audit;

import com.lifeonwalden.app.gateway.auth.bean.AuditBean;
import com.lifeonwalden.app.gateway.auth.service.GlobalTrackService;
import com.lifeonwalden.app.gateway.auth.util.RemoteAddressUtil;
import com.lifeonwalden.app.util.character.IdGenerator;
import com.lifeonwalden.app.util.character.JSON;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public abstract class BaseAudit implements HandlerInterceptor {

    @Autowired
    private GlobalTrackService globalTrackService;

    public abstract void writeLog(AuditBean auditBean);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Object principle = SecurityUtils.getSubject().getPrincipal();
        AuditBean auditBean = new AuditBean();
        if (null != principle) {
            auditBean.setPrinciple((String) principle);
        }
        auditBean.setDate(new Date()).setHost(RemoteAddressUtil.getIpAddr(httpServletRequest)).setOperation(httpServletRequest.getRequestURI()).setContent(JSON.writeValueAsString(httpServletRequest.getParameterMap())).setTrackId(IdGenerator.getId());
        globalTrackService.set(auditBean);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        AuditBean auditBean = globalTrackService.get().get();
        long costTime = System.currentTimeMillis() - auditBean.getDate().getTime();
        if (costTime > Integer.MAX_VALUE) {
            auditBean.setCostTime(Integer.MAX_VALUE);
        } else {
            auditBean.setCostTime((int) costTime);
        }
        writeLog(auditBean);
        globalTrackService.clear();
    }
}
