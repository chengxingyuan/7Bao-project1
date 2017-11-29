package com.wzitech.Z7Bao.frontend.business.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by guotx on 2017/9/22 15:52.
 */
@Component
public class HeePayConfig {

    @Value("${7bao.heepay.agentid}")
    private String agentId;
    @Value("${7bao.heepay.notifyurl}")
    private String notifyUrl;
    @Value("${7bao.heepay.returnurl}")
    private String returnUrl;

    @Value("${7bao.heepay.signkey}")
    private String signKey;

    @Value("${7bao.SwiftpassConfig.mch_create_ip}")
    private String mch_create_ip;

    public String getMch_create_ip() {
        return mch_create_ip;
    }

    public void setMch_create_ip(String mch_create_ip) {
        this.mch_create_ip = mch_create_ip;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
