package com.wzitech.Z7Bao.frontend.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 340032 on 2017/8/25.
 */
public class FundStatistics extends BaseEntity {
    /**
     * 期初余额
     */
    private BigDecimal qcBalance;
    /**
     * 支付金额
     */
    private BigDecimal zfAmount;
    /**
     * 提现金额
     */
    private BigDecimal txAmount;
    /**
     * 付款金额
     */
    private BigDecimal fkAmount;
    /**
     * 期末余额
     */
    private BigDecimal  qmBalance;
    /**
     * 期初时间
     */
    private Date startTime;
    /**
     * 期末时间
     */
    private Date endTime;

    /**
     *售得充值
     */
    private BigDecimal sdZfAmount;


    /**
     * 提现服务费
     */
    private BigDecimal txServiceAmount;

    /**
     * 老资金
     * @return
     */
    private BigDecimal oldFund;

    /**
     * 提现处理中
     * @return
     */
    private BigDecimal processing;

    public BigDecimal getProcessing() {
        return processing;
    }

    public void setProcessing(BigDecimal processing) {
        this.processing = processing;
    }

    public BigDecimal getOldFund() {
        return oldFund;
    }

    public void setOldFund(BigDecimal oldFund) {
        this.oldFund = oldFund;
    }

    public BigDecimal getQcBalance() {
        return qcBalance;
    }

    public void setQcBalance(BigDecimal qcBalance) {
        this.qcBalance = qcBalance;
    }

    public BigDecimal getZfAmount() {
        return zfAmount;
    }

    public void setZfAmount(BigDecimal zfAmount) {
        this.zfAmount = zfAmount;
    }

    public BigDecimal getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(BigDecimal txAmount) {
        this.txAmount = txAmount;
    }

    public BigDecimal getFkAmount() {
        return fkAmount;
    }

    public void setFkAmount(BigDecimal fkAmount) {
        this.fkAmount = fkAmount;
    }

    public BigDecimal getQmBalance() {
        return qmBalance;
    }

    public void setQmBalance(BigDecimal qmBalance) {
        this.qmBalance = qmBalance;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getSdZfAmount() {
        return sdZfAmount;
    }

    public void setSdZfAmount(BigDecimal sdZfAmount) {
        this.sdZfAmount = sdZfAmount;
    }

    public BigDecimal getTxServiceAmount() {
        return txServiceAmount;
    }

    public void setTxServiceAmount(BigDecimal txServiceAmount) {
        this.txServiceAmount = txServiceAmount;
    }
}
