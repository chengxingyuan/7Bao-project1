package com.wzitech.Z7Bao.frontend.service.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 340082 on 2017/8/18.
 */
public class FundRequest extends AbstractServiceRequest {

    private BigDecimal money;

    private Date time;

    private String remark;

    private String loginAccount;

    private String phoneNum;
    //充值渠道，见ZBaoPayType
    private int rechargeType;

    private String sign;

    private String outOrderId;

    private String jbOrderId;

    public String getJbOrderId() {
        return jbOrderId;
    }

    public void setJbOrderId(String jbOrderId) {
        this.jbOrderId = jbOrderId;
    }

    private String orderId;

    private String userId;

    public Integer getTransferFundType() {
        return transferFundType;
    }

    public void setTransferFundType(Integer transferFundType) {
        this.transferFundType = transferFundType;
    }

    /**
     * etch(1,"5173出售售得",""),
      aliPay(2,"支付宝","pay.alipay.native"),
      weixinPay(3,"微信","pay.weixin.native"),
      oldfull(4,"老资金充值7bao","")
     */

    private Integer transferFundType;

    public Date getTime() {
        return time;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOutOrderId() {
        return outOrderId;
    }

    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(int rechargeType) {
        this.rechargeType = rechargeType;
    }
    /**
     * 以下为提现使用
     */

    /**
     * 提现金额
     */
    private BigDecimal fee;

    /**
     * 银行名
     */
    private String bankName;


    /**
     * 账号属性 1 个人 2 公司
     */
    private String accountProp;

    /**
     * 银行卡号
     */
    private String accountNO;

    /**
     * 开户人名
     */
    private String accountName;


    /**
     * 省
     */
    private String area;
    /**
     * 城市
     */
    private String city;
    /**
     * 开户行名
     */
    private String subBankName;

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


    public String getAccountProp() {
        return accountProp;
    }

    public void setAccountProp(String accountProp) {
        this.accountProp = accountProp;
    }

    public String getAccountNO() {
        return accountNO;
    }

    public void setAccountNO(String accountNO) {
        this.accountNO = accountNO;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }



    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubBankName() {
        return subBankName;
    }

    public void setSubBankName(String subBankName) {
        this.subBankName = subBankName;
    }
}
