package com.wzitech.Z7Bao.frontend.dao.redis;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/4/27.
 */
public interface IZbaoPayOrderRedisDao {
    /**
     * 缓存中订单生成序列的KEY
     */
    String ORDER_ID_PAY_SEQ = "CACHE_7BAO_PAY_ORDER_ID_SEQ";

    /**
     * 缓存中订单生成序列的KEY
     */
    String ORDER_ID_TRANSFER_SEQ = "CACHE_7BAO_TRANSFER_ORDER_ID_SEQ";

    /**
     * 生成充值订单号
     * @return
     */
    String getRechargeOrderId();
    /**
     * 生成转账单订单号
     * @return
     */
    String getTransferOrderid();

    String getFreezeOrderId();

    boolean lock(long timeout, TimeUnit timeUnit, String lockKey);

    boolean unlock(String unlockKey);

    Integer getPayChannel();

    void setPayChannel(Integer payChannel);
}
