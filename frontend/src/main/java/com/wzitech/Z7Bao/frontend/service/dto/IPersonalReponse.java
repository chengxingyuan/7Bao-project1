package com.wzitech.Z7Bao.frontend.service.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 340032 on 2017/8/30.
 */
public class IPersonalReponse extends AbstractServiceResponse {

    /**
     * 银行名
     * 用id表示
     */
    private Integer bankName;

    /**
     * 卡号
     */
    private String accountNO;

    /**
     *卡属性
     */
    private String accountProp;

    /**
     * 开户省
     */
    private String province;

    /**
     * 开户城市
     */
    private String city;

    /**
     * 开户行名
     */
    private String subBankName;
    /**
     * 银行url
     * @return
     */
    private String imgUrl;

    private BigDecimal deposit;

    /**
     * 总金额
     * */
    private BigDecimal totalAmount;
    /**
     * 冻结金额
     * */
    private BigDecimal freezeAmount;
    /**
     * 可用金额
     * */
    private BigDecimal availableAmount;

    /**
     * 新资金
     */
    private Boolean isUserBind;
    /**
     * 7bao账户
     */
    private String zbaoLoginAccount;
    /**
     * 登录账号
     * */
    private String loginAccount;

    //订单状态
    private Integer status;

    /**
     * 账号修改时间
     * */
    private Date updateTime;

    /**
     * 账号申请时间
     * */
    private String applyTime;



    /**
     * 账号创建时间
     * */
    private Date createTime;
/*
    保证金
 */
    private String configValue;

    /**
     * 可用资金余额值
     */
    private String AvailableFundValue;


    /**
     * 用户实名认证
     * @return
     */
    private String name;

    /**
     * 开通绑定
     */
    private Boolean isShBind;

    public Boolean getIsShBind() {
        return isShBind;
    }

    public void setIsShBind(Boolean shBind) {
        isShBind = shBind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getAvailableFundValue() {
        return AvailableFundValue;
    }

    public void setAvailableFundValue(String availableFundValue) {
        AvailableFundValue = availableFundValue;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public Boolean getUserBind() {
        return isUserBind;
    }

    public void setUserBind(Boolean userBind) {
        isUserBind = userBind;
    }

    public String getZbaoLoginAccount() {
        return zbaoLoginAccount;
    }

    public void setZbaoLoginAccount(String zbaoLoginAccount) {
        this.zbaoLoginAccount = zbaoLoginAccount;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public Integer getBankName() {
        return bankName;
    }

    public void setBankName(Integer bankName) {
        this.bankName = bankName;
    }

    public String getAccountNO() {
        return accountNO;
    }

    public void setAccountNO(String accountNO) {
        this.accountNO = accountNO;
    }

    public String getAccountProp() {
        return accountProp;
    }

    public void setAccountProp(String accountProp) {
        this.accountProp = accountProp;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
