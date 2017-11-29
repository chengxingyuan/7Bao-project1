package com.wzitech.Z7Bao.frontend.dao.rdb;

import com.wzitech.Z7Bao.frontend.entity.FundStatistics;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 340032 on 2017/8/25.
 */
public interface IFundStatisticsDao extends IMyBatisBaseDAO<FundStatistics,Long> {
    /**
     * 获取最后一天的统计数据
     *
     * @return
     */
    FundStatistics queryLastDayData();

    /**
     * 按时间统计支付金额
     *
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal queryRechargeAmount(Date startTime, Date endTime);

    /**
     * 按日期统计提现金额
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal queryRefundAmount(Date startTime, Date endTime);

    /**
     * 按日期统计付款金额
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal queryPayAmount(Date startTime, Date endTime);


    /**
     * 按日期统计提现服务费
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal queryWithdrawalsAmount(Date startTime,Date endTime);

    /**
     * 按日期统计提现处理中金额
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal queryProcessingAmount(Date startTime,Date endTime);

    /**
     * 按日期统计售得充值
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal querySoldAmount(Date startTime,Date endTime);

    /***
     * 按日期统计老资金
     * @param
     * @return
     */
    BigDecimal queryOldFund(Date startTime,Date endTime);

    List<FundStatistics> selectExportExcel(Map<String, Object> map);

}
