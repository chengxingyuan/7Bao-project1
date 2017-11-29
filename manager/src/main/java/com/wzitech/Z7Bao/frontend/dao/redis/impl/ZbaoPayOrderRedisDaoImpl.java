package com.wzitech.Z7Bao.frontend.dao.redis.impl;

import com.wzitech.Z7Bao.frontend.dao.redis.IZbaoPayOrderRedisDao;
import com.wzitech.Z7Bao.frontend.entity.ZbaoPayOrder;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.enums.PayChannelType;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 生成收货方付款订单号
 */
@Repository
public class ZbaoPayOrderRedisDaoImpl extends AbstractRedisDAO<ZbaoPayOrder> implements IZbaoPayOrderRedisDao {
    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    private final String PAY_CHANNEL = "z7Bao:pay_channel";

    /**
     * 生成订单号
     *
     * @return
     */
    @Override
    public String getRechargeOrderId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        Date now = new Date();
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(now);
        long id = valueOps.increment(ORDER_ID_PAY_SEQ, 1);
        setIncrement(nowCal, id);
        String orderId = StringUtils.leftPad(String.valueOf(id), 7, '0');
        StringBuffer sb = new StringBuffer("Z7BCZ");
        sb.append(sdf.format(nowCal.getTime())).append(orderId);
        return sb.toString();
    }

    /**
     * 生成充值单号
     *
     * @return
     */
    @Override
    public String getTransferOrderid() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        Date now = new Date();
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(now);
        long id = valueOps.increment(ORDER_ID_TRANSFER_SEQ, 1);
        setIncrement(nowCal, id);
        String orderId = StringUtils.leftPad(String.valueOf(id), 7, '0');
        StringBuffer sb = new StringBuffer("Z7BKK");
        sb.append(sdf.format(nowCal.getTime())).append(orderId);
        return sb.toString();
    }

    /**
     * 生成冻结解冻订单号
     */
    @Override
    public String getFreezeOrderId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        Date now = new Date();
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(now);
        long id = valueOps.increment(ORDER_ID_TRANSFER_SEQ, 1);
        setIncrement(nowCal, id);
        String orderId = StringUtils.leftPad(String.valueOf(id), 7, '0');
        StringBuffer sb = new StringBuffer("Z7BDJ");
        sb.append(sdf.format(nowCal.getTime())).append(orderId);
        return sb.toString();
    }

    private void setIncrement(Calendar nowCal, long id) {
        if (id == 1L) {
            //设置缓存数据最后的失效时间为当天的最后一秒
            Calendar lastCal = Calendar.getInstance();
            lastCal.set(nowCal.get(Calendar.YEAR), nowCal.get(Calendar.MONTH), nowCal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
            lastCal.set(Calendar.MILLISECOND, 999);
            template.expireAt(ORDER_ID_TRANSFER_SEQ, lastCal.getTime());
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
    public Integer getPayChannel() {
        String s = valueOps.get(PAY_CHANNEL);
        if (s != null && !"".equals(s)) {
            return Integer.valueOf(s);
        }else{
            setPayChannel(PayChannelType.swiftpass.getType());
            return PayChannelType.swiftpass.getType();
        }
    }

    @Override
    public void setPayChannel(Integer payChannel) {
        valueOps.set(PAY_CHANNEL, payChannel.toString());
    }
}
