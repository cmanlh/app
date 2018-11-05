package com.lifeonwalden.app.gateway.auth.service;

import com.lifeonwalden.app.gateway.auth.bean.AuditBean;

public interface GlobalTrackService {
    ThreadLocal<AuditBean> get();

    void set(AuditBean auditBean);

    void clear();
}
