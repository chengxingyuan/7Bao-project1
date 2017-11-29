package com.wzitech.Z7Bao.frontend.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class ZbaoPayOrder extends BaseEntity {
    //id
    private Long id;

    //订单id
    private String orderId;

    //主站登录账号
    private String loginAccount;

    //金额
    private BigDecimal amount;

    //创建时间
    private Date createTime;

    //订单状态
    private Integer status;

    //结束时间
    private Date dealTime;

    //订单类型
    private Integer orderType;

    //支付渠道，威富通或汇付宝
    private Integer payChannel;

    //外部订单id
    private String outOrderId;

    //主站订单id
    private String zzOrderId;

    //备注
    private String remark;

    //支付账户 支付宝 微信使用
    private String paymentAccount;

    //支付二维码

    public String getPayQr() {
        return payQr;
    }

    public void setPayQr(String payQr) {
        this.payQr = payQr;
    }

    private String payQr;

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZzOrderId() {

        return zzOrderId;
    }

    public void setZzOrderId(String zzOrderId) {
        this.zzOrderId = zzOrderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount == null ? null : loginAccount.trim();
    }

    public BigDecimal getAmount() {
        return amount == null ? null : amount.setScale(2, BigDecimal.ROUND_UP);
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

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOutOrderId() {
        return outOrderId;
    }

    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId == null ? null : outOrderId.trim();
    }

    public Integer getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel) {
        this.payChannel = payChannel;
    }
}