package com.wzitech.Z7Bao.frontend.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wangmin
 * Date:2017/8/21
 * 提现单
 */
@Component
public class ZbaoWithdrawals extends BaseEntity {
    /*
    订单号
     */

    private String orderId;
    /*
    登录账号
     */
    private String loginAccount;
    /*
    提现状态
     */
    private Integer type;// （1：体现中，2：提现成功，3：提现失败）
    /*
    提现金额（实际）
     */
    private BigDecimal amount ;
    /*
    服务费
     */
    private BigDecimal serviceAmount ;
    /*
    日志
     */
   private String log;
    /*
    创建时间
     */
    private Date createTime;
    /*
    处理时间
     */
   private Date  dealTime ;
    /*
    提现渠道类型
     */
    private int bankNameType;
    /*
    渠道名称
     */
   private String bankName ;
    /*
    提现账号
     */
    private String cardCode;
    /*
    开户姓名
     */
   private String  realName ;
    /*
    省份
     */
    private String province ;
    /*
    城市
     */
    private String city  ;
    /*
    区号
     */
   private String  areacode ;
    /*
    开户银行
     */
   private String openbank ;

    /**
     * 卡类型
     */
    private String accountProp;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 省名
     */
    private String provinceName;

    /**
     * 市名
     */
    private String cityName;


    /**
     * 在途资金
     */
    private BigDecimal fundOnWay;

    public BigDecimal getFundOnWay() {
        return fundOnWay;
    }

    public void setFundOnWay(BigDecimal fundOnWay) {
        this.fundOnWay = fundOnWay;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getAccountProp() {
        return accountProp;
    }

    public void setAccountProp(String accountProp) {
        this.accountProp = accountProp;
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

    public BigDecimal getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(BigDecimal serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public int getBankNameType() {
        return bankNameType;
    }

    public void setBankNameType(int bankNameType) {
        this.bankNameType = bankNameType;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getOpenbank() {
        return openbank;
    }

    public void setOpenbank(String openbank) {
        this.openbank = openbank;
    }
}
