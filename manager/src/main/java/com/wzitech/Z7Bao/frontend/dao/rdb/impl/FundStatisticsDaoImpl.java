package com.wzitech.Z7Bao.frontend.dao.rdb.impl;

import com.wzitech.Z7Bao.frontend.dao.rdb.IFundStatisticsDao;
import com.wzitech.Z7Bao.frontend.entity.FundStatistics;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 340032 on 2017/8/25.
 */
@Repository
public class FundStatisticsDaoImpl extends AbstractMyBatisDAO<FundStatistics,Long> implements IFundStatisticsDao {
    /**
     * 获取最后一天的统计数据
     *
     * @return
     */
    @Override
    public FundStatistics queryLastDayData() {
        return getSqlSession().selectOne(getMapperNamespace() + ".queryLastDayData");
    }

    /**
     * 按时间统计支付金额
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public BigDecimal queryRechargeAmount(Date startTime, Date endTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return getSqlSession().selectOne(getMapperNamespace() + ".queryRechargeAmount", params);
    }

    @Override
    public List<FundStatistics> selectExportExcel(Map<String, Object> map) {
        return this.getSqlSession().selectList(getMapperNamespace() + ".selectExportExcel",map);
    }

    /**
     * 按日期统计提现金额
     * @param startTime
     * @param endTime
     * @return
     */
    public BigDecimal queryRefundAmount(Date startTime, Date endTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return getSqlSession().selectOne(getMapperNamespace() + ".queryRefundAmount", params);
    }

    /**
     * 按日期统计付款金额
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public BigDecimal queryPayAmount(Date startTime, Date endTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return getSqlSession().selectOne(getMapperNamespace() + ".queryPayAmount", params);
    }

    /**
     * 按日期统计提现服务费
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public BigDecimal queryWithdrawalsAmount(Date startTime, Date endTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return getSqlSession().selectOne(getMapperNamespace() + ".queryWithdrawalsAmount", params);
    }

    @Override
    public BigDecimal queryProcessingAmount(Date startTime, Date endTime) {
        Map<String,Object> params =new HashMap<String, Object>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return getSqlSession().selectOne(getMapperNamespace()+".queryProcessingAmount",params);
    }

    /**
     * 按日期统计售得充值
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public BigDecimal querySoldAmount(Date startTime, Date endTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return getSqlSession().selectOne(getMapperNamespace() + ".querySoldAmount", params);
    }

    @Override
    public BigDecimal queryOldFund(Date startTime, Date endTime) {
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        return getSqlSession().selectOne(getMapperNamespace()+".queryOldFund",params);
    }
}
