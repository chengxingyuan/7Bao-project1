package com.wzitech.Z7Bao.frontend.business;

import com.wzitech.gamegold.common.enums.SwiftpassPayStatus;
import com.wzitech.gamegold.common.enums.ZBaoPayType;

import java.math.BigDecimal;

/**
 * Created by guotx on 2017/8/21 13:48.
 */
public interface ISwiftpassPayManager {
    /**
     * 获取充值url
     * @param outTradeNo 订单号
     * @param body 订单描述
     * @param rechargeAmount 充值金额,单位：分
     * @param payType 充值渠道
     * @return
     */
    String getUrl(String outTradeNo, String body, Integer rechargeAmount, ZBaoPayType payType);

    /**
     * 校验支付状态
     * @param outTradeNo 7宝订单号
     * @return 支付成功返回true，否则返回false
     */
    SwiftpassPayStatus checkPayStatus(String outTradeNo);

    /**
     * 关闭待支付订单
     * @param outTradeNo 7宝订单号
     * @return 支付成功返回true，否则返回false
     */
    boolean closeOrder(String outTradeNo);
}
