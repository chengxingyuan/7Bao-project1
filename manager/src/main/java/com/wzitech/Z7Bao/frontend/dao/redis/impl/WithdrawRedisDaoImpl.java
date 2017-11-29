package com.wzitech.Z7Bao.frontend.dao.redis.impl;

import com.wzitech.Z7Bao.frontend.dao.redis.IWithdrawRedisDao;
import com.wzitech.Z7Bao.frontend.entity.ZbaoWithdrawals;
import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/17  wurongfan           ZW_C_JB_00021 商城资金7bao改造
 */
@Component
public class WithdrawRedisDaoImpl extends AbstractRedisDAO<ZbaoWithdrawals> implements IWithdrawRedisDao {


    private static final String WITHDRAW_ORDER_ID_SEQ = "CACHE_7BAO_WITHDRAW_ORDER_ID_SEQ";

    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    @Override
    public void addTenPayToQuery(String outTradeNo,Double serviceFee) {
        zSetOps.add(RedisKeyHelper.keepOutTradeNo(),outTradeNo,serviceFee);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<String>> getTenPay() {
        return zSetOps.rangeWithScores(RedisKeyHelper.keepOutTradeNo(),0,-1);
    }

    @Override
    public String getWithDrawOrderId() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyMMdd");
        Date now=new Date();
        Calendar nowCal=Calendar.getInstance();
        nowCal.setTime(now);
        long id=valueOps.increment(WITHDRAW_ORDER_ID_SEQ,1);
        setIncrement(nowCal,id);
        String orderId=StringUtils.leftPad(String.valueOf(id),7,"0");
        StringBuffer sb=new StringBuffer("ZJ");
        sb.append(sdf.format(nowCal.getTime())).append(orderId);
        return sb.toString();
    }

    private void setIncrement(Calendar nowCal, long id) {
        if (id == 1L) {
            //设置缓存数据最后的失效时间为当天的最后一秒
            Calendar lastCal = Calendar.getInstance();
            lastCal.set(nowCal.get(Calendar.YEAR), nowCal.get(Calendar.MONTH), nowCal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
            lastCal.set(Calendar.MILLISECOND, 999);
            template.expireAt(WITHDRAW_ORDER_ID_SEQ, lastCal.getTime());
        }
    }

    @Override
    public boolean lock(long timeout, TimeUnit timeUnit, String lockKey) {
        if (StringUtils.isNotBlank(lockKey)) {
            Boolean result = valueOps.setIfAbsent(RedisKeyHelper.lockKey(lockKey), lockKey);
            if (result != null && result.booleanValue()) {
                template.expire(RedisKeyHelper.lockKey(lockKey), timeout, timeUnit);
            }
            return result;
        }
        return false;
    }

    /**
     * 解锁
     *
     * @param unlockKey
     * @return
     */
    @Override
    public boolean unlock(String unlockKey) {
        if (StringUtils.isNotBlank(unlockKey)) {
            template.delete(RedisKeyHelper.lockKey(unlockKey));
            return true;
        }
        return false;
    }

    @Override
    public void removeTenPayToQuery(String outTradeNo) {
        zSetOps.remove(RedisKeyHelper.keepOutTradeNo(),outTradeNo);
    }
}
