package com.wzitech.Z7Bao.frontend.service.dto;

import com.wzitech.Z7Bao.frontend.entity.ZbaoFundDetail;
import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import org.apache.poi.hssf.record.formula.functions.T;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by 340032 on 2017/8/30.
 */
public class IFundDetailsReponse extends AbstractServiceResponse {
    /*
    买家账号
     */
    private String loginAccount ;

    private  Integer type ;//（1.充值。2.提现，3，采购转账,4.提现服务费，5.售得充值）
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
     * 资金
     */
    private BigDecimal amount ;


    /**
     * 日志
     */
    private String log ;

    /**
     * 创建时间
     * @return
     */
    private Long createTime;

    private String endTime;



    private Boolean isSuccess;

    /*
    处理时间
     */
    private Date  dealTime ;


    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }


    //主站订单id
    private String zzOrderId;

    /*
    服务费
     */
    private BigDecimal serviceAmount ;


    private String outOrderId;

    private List<ZbaoFundDetail> rusult;




    private int pageAll;


    private GenericPage<ZbaoFundDetail> zbaoFundDetails;


    /*
   渠道名称
    */
    private String bankName ;
    /*
    提现账号
     */
    private String cardCode;

    //订单类型
    private Integer orderType;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 冻结,解冻
     */
    private String freezeOrderId;

    /**
     * 老资金 充值单号
     * @return
     *
     */
    private String orderId;

    /**
     * 老资金 状态
     * @return
     */
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getFreezeOrderId() {
        return freezeOrderId;
    }

    public void setFreezeOrderId(String freezeOrderId) {
        this.freezeOrderId = freezeOrderId;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public GenericPage<ZbaoFundDetail> getZbaoFundDetails() {
        return zbaoFundDetails;
    }

    public void setZbaoFundDetails(GenericPage<ZbaoFundDetail> zbaoFundDetails) {
        this.zbaoFundDetails = zbaoFundDetails;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public int getPageAll() {
        return pageAll;
    }

    public void setPageAll(int pageAll) {
        this.pageAll = pageAll;
    }

    public String getZzOrderId() {
        return zzOrderId;
    }

    public void setZzOrderId(String zzOrderId) {
        this.zzOrderId = zzOrderId;
    }

    public List<ZbaoFundDetail> getRusult() {
        return rusult;
    }

    public void setRusult(List<ZbaoFundDetail> rusult) {
        this.rusult = rusult;
    }

    public String getOutOrderId() {
        return outOrderId;
    }

    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
    }

    public BigDecimal getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(BigDecimal serviceAmount) {
        this.serviceAmount = serviceAmount;
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

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
    }
}
