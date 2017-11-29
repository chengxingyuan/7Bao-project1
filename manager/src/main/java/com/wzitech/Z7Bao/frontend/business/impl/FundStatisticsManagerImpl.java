package com.wzitech.Z7Bao.frontend.business.impl;

import com.wzitech.Z7Bao.frontend.business.IFundStatisticsManager;
import com.wzitech.Z7Bao.frontend.dao.rdb.IFundStatisticsDao;
import com.wzitech.Z7Bao.frontend.entity.FundStatistics;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 340032 on 2017/8/25.
 */
@Component
public class FundStatisticsManagerImpl implements IFundStatisticsManager {
    private static final Logger logger = LoggerFactory.getLogger(FundStatisticsManagerImpl.class);
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    IFundStatisticsDao fundStatisticsDao;


    /**
     * 分页查询
     *
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param
     * @return
     */
    @Override
    public GenericPage<FundStatistics> queryListInPage(Map<String, Object> map, int start, int pageSize, String orderBy, boolean desc) {
        if (null == map) {
            map = new HashMap<String, Object>();
        }

        //检查分页参数
        if (pageSize < 1) {
            throw new IllegalArgumentException("分页pageSize参数必须大于1");
        }

        if (start < 0) {
            throw new IllegalArgumentException("分页startIndex参数必须大于0");
        }

        return fundStatisticsDao.selectByMap(map, pageSize, start, orderBy, desc);
    }

    @Override
    public List<FundStatistics> queryAll(Map<String, Object> querypaeam) {
        return fundStatisticsDao.selectExportExcel(querypaeam);
    }


    /**
     * 统计每日数据
     */
    @Override
    @Transactional
    public void statistics() {
        // 获取最后一次的统计数据
        FundStatistics lastDayData = fundStatisticsDao.queryLastDayData();
        if (lastDayData == null) {
            // 第一次
            Calendar now = Calendar.getInstance();
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            start.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH) - 1, 0, 0, 0);
            start.set(Calendar.MILLISECOND, 000);
            end.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH) - 1, 23, 59, 59);
            end.set(Calendar.MILLISECOND, 999);
            statisticsByDateTime(null, start.getTime(), end.getTime());
            return;
        }


//        Calendar lastDay = Calendar.getInstance();
//        lastDay.setTime(lastDayData.getStartTime());
        Calendar now = Calendar.getInstance();
//        int diff = now.get(Calendar.DAY_OF_MONTH) - lastDay.get(Calendar.DAY_OF_MONTH);
        Long time = (now.getTime().getTime() - lastDayData.getStartTime().getTime()) / (24 * 60 * 60 * 1000);
        int diff = (int) Math.abs(time);
        //int diff=new Integer(time.longValue());
        logger.info("开始每日统计，当前统计日期跟上次相差{}天", diff);
        for (int i = diff - 1; i > 0; i--) {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            start.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH) - i, 0, 0, 0);
            start.set(Calendar.MILLISECOND, 000);
            end.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH) - i, 23, 59, 59);
            end.set(Calendar.MILLISECOND, 999);
            Date startTime = start.getTime();
            Date endTime = end.getTime();

            statisticsByDateTime(lastDayData.getQmBalance(), startTime, endTime);
            if (i > 0) {
                // 获取最后一次的统计数据
                lastDayData = fundStatisticsDao.queryLastDayData();
            }
        }
    }

    /**
     * 按日期统计
     *
     * @param lastQmBalance 上次期末余额
     * @param startTime     统计开始时间
     * @param endTime       统计结束时间
     */
    private void statisticsByDateTime(BigDecimal lastQmBalance, Date startTime, Date endTime) {
        logger.info("统计资金{}-{}的数据。", dateFormat.format(startTime), dateFormat.format(endTime));
        // 期初余额
        BigDecimal qcBalance = (lastQmBalance == null) ? BigDecimal.ZERO : lastQmBalance;
        // 统计当日支付金额
        BigDecimal recharge = fundStatisticsDao.queryRechargeAmount(startTime, endTime);
        recharge = (recharge == null) ? BigDecimal.ZERO : recharge;
        // 统计当日提现金额
        BigDecimal refund = fundStatisticsDao.queryRefundAmount(startTime, endTime);
        refund = (refund == null) ? BigDecimal.ZERO : refund;
        // 统计当日采购款金额
        BigDecimal pay = fundStatisticsDao.queryPayAmount(startTime, endTime);
        pay = (pay == null) ? BigDecimal.ZERO : pay;
        // 统计当日售得充值
        BigDecimal sold = fundStatisticsDao.querySoldAmount(startTime, endTime);
        sold = (sold == null) ? BigDecimal.ZERO : sold;
        // 统计当日提现服务费
        BigDecimal service = fundStatisticsDao.queryWithdrawalsAmount(startTime, endTime);
        service = (service == null) ? BigDecimal.ZERO : service;
        // 统计当日老资金
        BigDecimal oldFund = fundStatisticsDao.queryOldFund(startTime, endTime);
        oldFund = (oldFund == null) ? BigDecimal.ZERO : oldFund;
        //提现处理中金额
        BigDecimal processing = fundStatisticsDao.queryProcessingAmount(startTime, endTime);
        processing = (processing == null) ? BigDecimal.ZERO : processing;
        // 计算期末余额
        BigDecimal qmBalance = qcBalance.add(recharge).add(sold).add(oldFund).subtract(pay).subtract(refund).subtract(service);
        // 写入统计记录
        FundStatistics statistics = new FundStatistics();
        statistics.setStartTime(startTime);
        statistics.setEndTime(endTime);
        statistics.setQcBalance(qcBalance);
        statistics.setZfAmount(recharge);
        statistics.setTxAmount(refund);
        statistics.setFkAmount(pay);
        statistics.setSdZfAmount(sold);
        statistics.setTxServiceAmount(service);
        statistics.setQmBalance(qmBalance);
        statistics.setOldFund(oldFund);
        statistics.setProcessing(processing);
        fundStatisticsDao.insert(statistics);
    }
    public  /**
     * 查出所有，导出表格用
     * */
    List<FundStatistics> queryAllForExport(Map<String, Object> queryParam){
      return  fundStatisticsDao.selectByMap(queryParam);
    }

}
