package com.wzitech.Z7Bao.frontend.dao.redis;

import java.util.concurrent.TimeUnit;

/**
 * Created by chengXY on 2017/9/9.
 */
public interface IFundStatisticsRedisDao {

    public boolean unlock(String unlockKey);

    boolean lock(long timeout, TimeUnit timeUnit, String lockKey);

}
