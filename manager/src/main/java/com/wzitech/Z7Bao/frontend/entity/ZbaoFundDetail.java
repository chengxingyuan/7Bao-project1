package com.wzitech.Z7Bao.frontend.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wangmin
 * Date:2017/8/21
 * 资金明细表
 */
@Component
public class ZbaoFundDetail extends BaseEntity {
    public static final int pageSize = 10;

    /*
    买家账号
     */
    private String loginAccount;

    private Integer type;//（1.充值。2.提现，3，采购转账,4.提现服务费，5.售得充值）
    /**
     * 充值
     */
    private String czOrderId;
    /**
     * 提现
     */
    private String txOrderId;
    /**
     * 采购转账
     */
    private String cgOrderId;
    /**
     * 外部订单号
     */
    private String outOrderId;
    /**
     * 资金
     */
    private BigDecimal amount;
    /**
     * 日志
     */
    private String log;

    /**
     * 创建时间
     *
     * @return
     */
    private Date createTime;
    private Boolean isSuccess;

    /**
     * 冻结/解冻订单号
     * */
    private String freezeOrderId;

    public String getFreezeOrderId() {
        return freezeOrderId;
    }

    public void setFreezeOrderId(String freezeOrderId) {
        this.freezeOrderId = freezeOrderId;
    }

    public String getOutOrderId() {
        return outOrderId;
    }

    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCzOrderId() {
        return czOrderId;
    }

    public void setCzOrderId(String czOrderId) {
        this.czOrderId = czOrderId;
    }

    public String getTxOrderId() {
        return txOrderId;
    }

    public void setTxOrderId(String txOrderId) {
        this.txOrderId = txOrderId;
    }

    public String getCgOrderId() {
        return cgOrderId;
    }

    public void setCgOrderId(String cgOrderId) {
        this.cgOrderId = cgOrderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
    }
}
