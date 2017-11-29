package com.wzitech.Z7Bao.frontend.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wangmin
 * Date:2017/8/21
 * 采购转账单(支出)：
 */
@Component
public class ZbaoTransfer extends BaseEntity {
    /*
    单号
     */
    private String orderId ;
    /*
    登录账号
     */
    private String loginAccount;

    private  String shOrderId ;
    /*
    总金额
     */
    private BigDecimal amount ;
    /*
    创建时间
     */
   private Date createTime ;
    /*
    转账状态
     */
    private Integer status;//（1：转账中，2：转账成功，3：转账失败）
    /*
    处理时间
     */
    private Date dealTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getShOrderId() {
        return shOrderId;
    }

    public void setShOrderId(String shOrderId) {
        this.shOrderId = shOrderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }
}
