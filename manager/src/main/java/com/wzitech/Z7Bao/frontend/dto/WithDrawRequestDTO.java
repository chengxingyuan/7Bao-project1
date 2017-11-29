package com.wzitech.Z7Bao.frontend.dto;

import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;

import java.math.BigDecimal;

/**
 * Created by 339928 on 2017/8/25.
 */
public class WithDrawRequestDTO {

    private String bankName;

    private String accountProp;

    private String accountNo;

    private String accoutName;

    private String desc;

    private String loginAccount;

    private String province;

    private String city;

    private String subBankName;


    private BigDecimal withDrawMoney;



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

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccoutName() {
        return accoutName;
    }

    public void setAccoutName(String accoutName) {
        this.accoutName = accoutName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
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

    public String getSubBankName() {
        return subBankName;
    }

    public void setSubBankName(String subBankName) {
        this.subBankName = subBankName;
    }


    public BigDecimal getWithDrawMoney() {
        return withDrawMoney;
    }

    public void setWithDrawMoney(BigDecimal withDrawMoney) {
        this.withDrawMoney = withDrawMoney;
    }
}
