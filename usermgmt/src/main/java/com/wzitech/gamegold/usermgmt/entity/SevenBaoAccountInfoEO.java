package com.wzitech.gamegold.usermgmt.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.gamegold.common.entity.IUser;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by chengXY on 2017/8/10.
 * 7bao前台用户的登录账号E0
 */
@Component
public class SevenBaoAccountInfoEO extends BaseEntity implements IUser{
    private static final long serialVersionUID = 1L;

    /**
     * 登录账号
     * */
    private String loginAccount;

    /**
     * 账号创建时间
     * */
    private Date createTime;

    /**
     * 账号修改时间
     * */
    private Date updateTime;

    /**
     * 是否禁用
     * */
    private Boolean isDeleted;

    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * qq
     * */
    private Long qq;
    /**
     * 客服qq
     * */
    private Integer kefuqq;

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
     * 姓名
     * */
    private  String name;

    /**
     * 金币用户绑定
     */
    private Boolean isUserBind;

    /**
     * 7bao账户Id
     * @return
     */
    private String uid;

    private String zbaoLoginAccount;

    private Boolean isQqService;



    public Boolean getIsQqService() {
        return isQqService;
    }

    public void setIsQqService(Boolean QqService) {
        isQqService = QqService;
    }


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
     * 开户省名
     */
    private String provinceName;

    /**
     * 开户市名
     */
    private String cityName;

    /**
     * 开户城市
     */
    private String city;

    /**
     * 开户行名
     */
    private String subBankName;

    private Boolean isShBind;

    /**
     * 银行名称
     */
    private String showName;

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    /**
     * 账号申请时间
     * */
    private String applyTime;

    public Boolean getIsShBind() {
        return isShBind;
    }

    public void setIsShBind(Boolean shBind) {
        isShBind = shBind;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public Integer getKefuqq() {
        return kefuqq;
    }

    public void setKefuqq(Integer kefuqq) {
        this.kefuqq = kefuqq;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getIsUserBind() {
        return isUserBind;
    }

    public void setIsUserBind(Boolean userBind) {
        isUserBind = userBind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getQq() {
        return qq;
    }

    public void setQq(Long qq) {
        this.qq = qq;
    }

    public String getZbaoLoginAccount() {
        return zbaoLoginAccount;
    }

    public void setZbaoLoginAccount(String zbaoLoginAccount) {
        this.zbaoLoginAccount = zbaoLoginAccount;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getUserBind() {
        return isUserBind;
    }

    public void setUserBind(Boolean userBind) {
        isUserBind = userBind;
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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
