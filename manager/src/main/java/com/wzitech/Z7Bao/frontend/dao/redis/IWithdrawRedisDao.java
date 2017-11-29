package com.wzitech.Z7Bao.frontend.dao.redis;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/17  wurongfan           ZW_C_JB_00021 商城资金7bao改造
 */
public interface IWithdrawRedisDao {

    public void addTenPayToQuery(String outTradeNo,Double serviceFee);

    Set<ZSetOperations.TypedTuple<String>> getTenPay();

    boolean lock(long timeout, TimeUnit timeUnit, String lockKey);
    boolean unlock(String unlockKey );

    void removeTenPayToQuery(String outTradeNo);

    String getWithDrawOrderId();
}
