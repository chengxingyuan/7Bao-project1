package com.wzitech.Z7Bao.frontend.job;

import com.wzitech.Z7Bao.frontend.business.ITenPayManager;
import com.wzitech.Z7Bao.frontend.dao.redis.IWithdrawRedisDao;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/17  wurongfan           ZW_C_JB_00021 商城资金7bao改造
 */
@Component
public class AutoQueryTenPayJob {

    protected static final Logger logger = LoggerFactory.getLogger(AutoQueryTenPayJob.class);

    private static final String JOB_ID = "AUTO_QUERY_TENPAY_JOB";

    @Autowired
    private IWithdrawRedisDao withdrawRedisDao;

    @Autowired
    private ITenPayManager tenPayManager;

    /**
     * 查询提现单银行处理信息的job
     */
    public void tenPayJob() {
        Boolean locked = withdrawRedisDao.lock(30, TimeUnit.MINUTES, JOB_ID);
        if (!locked) {
            logger.info("[财付通自动提现补单JOB]上一个任务还未执行完成。");
            return;
        }
        try {
            Set<ZSetOperations.TypedTuple<String>> setW = withdrawRedisDao.getTenPay();
            if (setW == null || setW.size() == 0) {
                return;
            }
            logger.info("[财付通自动提现补单JOB]任务开始");
            for (ZSetOperations.TypedTuple<String> s : setW) {
                if (StringUtils.isBlank(s.getValue())) {
                    continue;
                }
                logger.info("[财付通自动提现补单JOB]任务开始" + s.getValue().toString());
                try {
                    tenPayManager.quertTenPayWithBusiness(s.getValue());
                } catch (SystemException e) {
                    logger.error("财付通提现查询发生异常:批次号" + s.getValue().toString()+"原因:"+e.getErrorMsg()+e.getErrorCode());
                }catch (Exception e){
                    logger.error("同步账户信息异常：批次号"+s.getValue().toString());
                }
            }
            logger.info("[财付通自动提现补单JOB]任务结束");
        } finally {
            withdrawRedisDao.unlock(JOB_ID);
        }
    }
}
