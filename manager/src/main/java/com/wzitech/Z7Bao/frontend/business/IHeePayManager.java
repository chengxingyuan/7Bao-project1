package com.wzitech.Z7Bao.frontend.business;

import com.wzitech.chaos.framework.server.common.exception.BusinessException;
import com.wzitech.gamegold.common.enums.ZBaoPayType;

/**
 * 汇付宝支付接口
 * Created by guotx on 2017/9/22 14:48.
 */
public interface IHeePayManager {
    /**
     * 汇付支付构建url
     * @param outTradeNo 7宝交易号
     * @param body 交易名称
     * @param rechargeAmount 支付金额，单位分
     * @param payType 支付类型，支付宝和微信
     * @return
     */
    String getPayUrl(String outTradeNo, String body, Integer rechargeAmount, ZBaoPayType payType);

    /**
     * 查询汇付支付状态
     * @param tradeNo 7宝交易号
     * @return
     */
    boolean heePayQuery(String tradeNo);
}
