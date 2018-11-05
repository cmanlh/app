package com.lifeonwalden.app.gateway.auth.service.impl;

import com.lifeonwalden.app.gateway.auth.bean.AuditBean;
import com.lifeonwalden.app.gateway.auth.service.GlobalTrackService;
import org.springframework.stereotype.Service;

@Service
public class GlobalTrackServiceImpl implements GlobalTrackService {
    private ThreadLocal<AuditBean> trackInfo = new ThreadLocal<>();

    @Override
    public ThreadLocal<AuditBean> get() {
        return this.trackInfo;
    }

    @Override
    public void set(AuditBean auditBean) {
        this.trackInfo.set(auditBean);
    }

    @Override
    public void clear() {
        this.trackInfo.remove();
    }
}
