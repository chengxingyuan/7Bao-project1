package com.wzitech.Z7Bao.frontend.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigDecimal;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/17  wurongfan           ZW_C_JB_00021 商城资金7bao改造
 */
public class ZbaoServiceAmountConfig extends BaseEntity {

    /**
     * 费率
     */
    private BigDecimal rate;

    /**
     * 最少服务费
     */
    private BigDecimal minServiceAmount;

    /**
     * 最多服务费
     */
    private BigDecimal maxServiceAmount;

    /**
     * 最少提现金额
     */
    private BigDecimal minAmount;

    /**
     * 最大提现金额
     */
    private BigDecimal maxAmount;


    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getMinServiceAmount() {
        return minServiceAmount;
    }

    public void setMinServiceAmount(BigDecimal minServiceAmount) {
        this.minServiceAmount = minServiceAmount;
    }

    public BigDecimal getMaxServiceAmount() {
        return maxServiceAmount;
    }

    public void setMaxServiceAmount(BigDecimal maxServiceAmount) {
        this.maxServiceAmount = maxServiceAmount;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }
}
