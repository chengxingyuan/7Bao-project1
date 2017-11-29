package com.wzitech.Z7Bao.frontend.business.impl;

import com.wzitech.Z7Bao.frontend.business.ISwiftpassConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by guotx on 2017/8/21 13:53.
 */
@Component
public class SwiftpassConfig implements ISwiftpassConfig {

    /**
     * 关闭订单
     */
    @Value("${7bao.SwiftpassConfig.close}")
    private String wapClose;

    /**
     * 查询接口类型
     */
    @Value("${7bao.SwiftpassConfig.wapQuery}")
    private String wapQuery;

    /**
     * 威富通交易密钥
     */
    @Value("${7bao.SwiftpassConfig.key}")
    private String key;

    /**
     * 威富通商户号
     */
    @Value("${7bao.SwiftpassConfig.mch_id}")
    private String mch_id;

    /**
     * 威富通请求url
     */
    @Value("${7bao.SwiftpassConfig.req_url}")
    private String req_url;

    /**
     * 通知url
     */
    @Value("${7bao.SwiftpassConfig.notify_url}")
    private String notify_url;
    /**
     * 终端ip
     */
    @Value("${7bao.SwiftpassConfig.mch_create_ip}")
    private String mch_create_ip;
    /**
     * 应用类型
     */
    @Value("${7bao.SwiftpassConfig.device_info}")
    private String device_info;

    @Override
    public String getWapQuery() {
        return wapQuery;
    }

    public void setWapQuery(String wapQuery) {
        this.wapQuery = wapQuery;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    @Override
    public String getReq_url() {
        return req_url;
    }

    public void setReq_url(String req_url) {
        this.req_url = req_url;
    }

    @Override
    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    @Override
    public String getMch_create_ip() {
        return mch_create_ip;
    }

    public void setMch_create_ip(String mch_create_ip) {
        this.mch_create_ip = mch_create_ip;
    }

    @Override
    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    @Override
    public String getWapClose() {
        return wapClose;
    }

    public void setWapClose(String wapClose) {
        this.wapClose = wapClose;
    }
}
