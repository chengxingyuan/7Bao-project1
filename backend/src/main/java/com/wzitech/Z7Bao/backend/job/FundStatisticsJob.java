package com.wzitech.Z7Bao.backend.job;

import com.wzitech.Z7Bao.frontend.business.IFundStatisticsManager;
import com.wzitech.Z7Bao.frontend.dao.redis.IFundStatisticsRedisDao;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by chengXY on 2017/9/9.
 */
@Component
public class FundStatisticsJob {
    private static final String JOB_ID = "ZB_FUND_STATISTICS_JOB";

    protected static final Logger logger = LoggerFactory.getLogger(FundStatisticsJob.class);
    @Autowired
    private IFundStatisticsRedisDao fundStatisticsRedisDao;

    @Autowired
    private IFundStatisticsManager fundStatisticsManager;

    /**
     * 资金平衡表每日统计job
     */
    public void autoStatistics() {
//        //测试用
//        fundStatisticsRedisDao.unlock(JOB_ID);
//        //测试用
        logger.info("平衡资金表统计job,开始!");
        Boolean locked = fundStatisticsRedisDao.lock(30, TimeUnit.MINUTES, JOB_ID);
        if (!locked) {
            logger.info("[资金平衡表每日统计job]上一个任务还未执行完成。");
            return;
        }
        try {
            fundStatisticsManager.statistics();
            logger.info("[资金平衡表每日统计job]任务结束");
        } finally {
            fundStatisticsRedisDao.unlock(JOB_ID);
        }
    }

}
