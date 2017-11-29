package com.wzitech.Z7Bao.frontend.service.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 资金详情前段需要传的数据
 *
 * Created by 340032 on 2017/8/31.
 */
public class IFundDetailSRequest extends AbstractServiceRequest {
    private Integer id;

    /**
     * 订单类型
     */
    private  Integer type ;//（1.充值。2.提现，3，采购转账,4.提现服务费，5.售得充值）

    /*
    买家账号
     */
    private String loginAccount ;

    private String orderId;

    private int page;

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
    private String cgOrderId ;
    /**
     * 状态
     */
    private Boolean isSuccess;
    /**
     * 支付状态
     * 只是查询用
     */
    private String payType;

    /**
     * 创建时间
     * @return
     */
    private String creatTime;

    /**
     *  结束时间
     * @return
     */
    private String endTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 冻结  解冻
     */

    private String freezeOrderId;

    public String getFreezeOrderId() {
        return freezeOrderId;
    }

    public void setFreezeOrderId(String freezeOrderId) {
        this.freezeOrderId = freezeOrderId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
