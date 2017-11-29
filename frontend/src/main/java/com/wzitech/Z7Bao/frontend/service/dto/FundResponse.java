package com.wzitech.Z7Bao.frontend.service.dto;


import com.wzitech.Z7Bao.frontend.entity.ZbaoPayOrder;
import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

/**
 * Created by 340082 on 2017/8/18.
 */
public class FundResponse  extends AbstractServiceResponse {
    private String rechargeAmount;

    private Integer rechargeType;

    private Boolean success;

    private String payQrUrl;

    private Integer liveTime;

    private Long createTimestamp;

    //支付渠道，见com.wzitech.gamegold.common.enums.PayChannelType

    public Integer getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel) {
        this.payChannel = payChannel;
    }

    private Integer payChannel;

    public Integer getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(Integer liveTime) {
        this.liveTime = liveTime;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    private Boolean isException;

    public Boolean getException() {
        return isException;
    }

    public void setException(Boolean exception) {
        isException = exception;
    }

    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Integer getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(Integer rechargeType) {
        this.rechargeType = rechargeType;
    }

    public String getPayQrUrl() {
        return payQrUrl;
    }

    public void setPayQrUrl(String payQrUrl) {
        this.payQrUrl = payQrUrl;
    }

    public Long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    private ZbaoPayOrder payOrder;

    public ZbaoPayOrder getPayOrder() {
        return payOrder;
    }

    public void setPayOrder(ZbaoPayOrder payOrder) {
        this.payOrder = payOrder;
    }
}
