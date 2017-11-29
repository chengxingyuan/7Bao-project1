package com.wzitech.Z7Bao.frontend.job;

import com.wzitech.Z7Bao.frontend.business.IFundManager;
import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoPayOrderDAO;
import com.wzitech.Z7Bao.frontend.dao.redis.IZbaoPayOrderRedisDao;
import com.wzitech.Z7Bao.frontend.entity.ZbaoPayOrder;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.FundType;
import com.wzitech.gamegold.common.enums.PayChannelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 自动校验待支付订单状态，超时关闭
 * Created by guotx on 2017/8/25 14:10.
 */
@Component
public class AutoCheckOrderStatusJob {
    @Autowired
    private IZbaoPayOrderRedisDao redisDao;
    @Autowired
    private IZbaoPayOrderDAO payOrderDAO;
    @Autowired
    private IFundManager fundManager;

    private Logger logger = LoggerFactory.getLogger(AutoCheckOrderStatusJob.class);

    private final String JOB_ID = "AUTO_CHECK_PAY_ORDER_JOB";
    //订单超时取消时间
    private final int OVER_TIME = 5 ;

    public void checkOrder() {
        Boolean locked = redisDao.lock(OVER_TIME - 1, TimeUnit.MINUTES, JOB_ID);
        if (!locked) {
            logger.info("[手动充值支付状态检查JOB]上一个任务还未执行完成。");
            return;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, - OVER_TIME * 2);
            Date startDate = calendar.getTime();
            calendar.add(Calendar.MINUTE, OVER_TIME);
            Date endDate = calendar.getTime();
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("status", FundType.Recharge.getCode());
//            queryMap.put("startTime",startDate);
            queryMap.put("endTime",endDate);
//            queryMap.put("payChannel", PayChannelType.swiftpass.getType());
            GenericPage<ZbaoPayOrder> orderList = payOrderDAO.findOrderList(queryMap, 1000, 0);
            List<ZbaoPayOrder> data = orderList == null ? null : orderList.getData();
            if (data != null && data.size() > 0) {
                logger.info("手动支付状态job检查，共计订单" + data.size());
                for (ZbaoPayOrder payOrder : data) {
                    try {
                        fundManager.checkOrder(payOrder);
                    } catch (Exception e) {
                        logger.info("校验支付状态发生异常，" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            boolean unlock = redisDao.unlock(JOB_ID);
            logger.info("充值支付JOB解锁结果：" + unlock);
        }
    }
}
