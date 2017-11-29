package com.wzitech.Z7Bao.frontend.business;

import com.wzitech.Z7Bao.frontend.entity.JBPayOrderTo7BaoEO;
import com.wzitech.Z7Bao.frontend.entity.ZbaoPayOrder;
import com.wzitech.chaos.framework.server.common.exception.BusinessException;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by 340082 on 2017/8/18.
 */
public interface IFundManager {
    Integer getPayOrderStatus(String orderId, String userId);

    String creatRecharge(String uid, BigDecimal amount, String outOrderId, String jbOrderId , Integer payType, List<JBPayOrderTo7BaoEO> jbTo7Bao);

    String creatTransferDeduction(String uid, BigDecimal amount,  String outOrderId);

    boolean rechargeComplete(String orderId, BigDecimal rechargeAmount, String outTradeNo) throws BusinessException;

    Map<String,Object> buildPayUrl(String orderId) throws BusinessException;

    /**
     * 校验订单是否已支付，已支付就修改订单，未支付订单状态改为失败
     * @param payOrder
     */
    void checkOrder(ZbaoPayOrder payOrder);

//    boolean updateGoldBeanAccount(String loginAccount,BigDecimal ammount);

    /**
     * 关闭订单
     * @param orderId 支付订单号
     */
    void closePayOrder(String orderId) ;

    /**
     * 获取订单信息
     * @param outOrderId 订单号
     * @return
     */
    ZbaoPayOrder getPayOrderInfo(String outOrderId);

    /**
     * 创建充值单以及资金明细
     * @param amount
     * @param zzOrderId
     * @param jbOrderId
     * @param payType
     * @param updateEo
     * @return
     */
    public String addPayOrderAndDetail(BigDecimal amount, String zzOrderId, String jbOrderId, Integer payType, SevenBaoAccountInfoEO updateEo, List<JBPayOrderTo7BaoEO> jbTo7Bao);
}
