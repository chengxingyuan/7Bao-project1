package com.wzitech.Z7Bao.frontend.business;

/**
 * Created by guotx on 2017/8/21 13:54.
 */
public interface ISwiftpassConfig {

    /**
     * 关闭订单接口名称
     * @return
     */
    String getWapClose();
    /**
     * 支付查询接口类型
     */
    String getWapQuery();

    /**
     * 威富通交易密匙
     */
    String getKey();

    /**
     * 威富通商户号
     */
    String getMch_id();

    /**
     * 威富通请求url
     */
    String getReq_url();

    /**
     * 通知url
     */
    String getNotify_url();

    /**
     * 终端ip
     */
    String getMch_create_ip();

    /**
     * 应用类型
     */
    String getDevice_info();

}
