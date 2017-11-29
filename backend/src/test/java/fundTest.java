import com.wzitech.Z7Bao.backend.job.FundStatisticsJob;
import com.wzitech.Z7Bao.frontend.business.IFundStatisticsManager;
import com.wzitech.Z7Bao.frontend.dao.rdb.IFundStatisticsDao;
import com.wzitech.Z7Bao.frontend.entity.FundStatistics;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.utils.MD5;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 340032 on 2017/8/25.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-backend-context.xml"})
@ActiveProfiles("development")
public class fundTest {
    @Autowired
    IFundStatisticsManager fundStatisticsManager;

    @Autowired
    IFundStatisticsDao fundStatisticsDao;
    @Test
    public void test(){

//        fundStatisticsManager.statistics();
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
//        logger.info("开始每日统计，当前统计日期跟上次相差{}天", diff);
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

    @Autowired
    private FundStatisticsJob fundStatisticsJob;

    /**
     * 按日期统计
     *
     * @param lastQmBalance 上次期末余额
     * @param startTime     统计开始时间
     * @param endTime       统计结束时间
     */
    private void statisticsByDateTime(BigDecimal lastQmBalance, Date startTime, Date endTime) {
//        logger.info("统计资金{}-{}的数据。", dateFormat.format(startTime), dateFormat.format(endTime));
//        // 期初余额
//        BigDecimal qcBalance = (lastQmBalance == null) ? BigDecimal.ZERO : lastQmBalance;
//        // 统计当日支付金额
//        BigDecimal recharge = fundStatisticsDao.queryRechargeAmount(startTime, endTime);
//        recharge = (recharge == null) ? BigDecimal.ZERO : recharge;
//        // 统计当日提现金额
//        BigDecimal refund = fundStatisticsDao.queryRefundAmount(startTime, endTime);
//        refund = (refund == null) ? BigDecimal.ZERO : refund;
//        // 统计当日付款金额
//        BigDecimal pay = fundStatisticsDao.queryPayAmount(startTime, endTime);
//        pay = (pay == null) ? BigDecimal.ZERO : pay;
//        // 统计当日售得充值
//        BigDecimal sold = fundStatisticsDao.querySoldAmount(startTime, endTime);
////        sold = (sold == null) ? BigDecimal.ZERO : sold;
//        System.out.println(sold+"*********************************************");
//        // 统计当日提现服务费
//        BigDecimal service = fundStatisticsDao.queryWithdrawalsAmount(startTime, endTime);
//        service = (service == null) ? BigDecimal.ZERO : service;
//        // 计算期末余额
//        BigDecimal qmBalance = qcBalance.add(recharge).add(pay).add(sold).subtract(refund).subtract(service);
//        // 写入统计记录
//        FundStatistics statistics = new FundStatistics();
//        statistics.setStartTime(startTime);
//        statistics.setEndTime(endTime);
//        statistics.setQcBalance(qcBalance);
//        statistics.setSdZfAmount(recharge);
//        statistics.setTxAmount(refund);
//        statistics.setFkAmount(pay);
//        statistics.setSdZfAmount(sold);
//        statistics.setTxServiceAmount(service);
//        statistics.setQmBalance(qmBalance);
//        System.out.println(statistics+"////////***********///////////////");
//        fundStatisticsDao.insert(statistics);



    }
    @Test
    public void test02(){
        fundStatisticsJob.autoStatistics();
    }





}



