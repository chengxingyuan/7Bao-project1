package com.wzitech.gamegold.usermgmt.request;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by 340032 on 2017/8/17.
 */
public class QuerySevenBaoUserBindRequest extends AbstractServiceRequest {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 登录账号
     * */
    private String loginAccount;

    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 判断shif
     */

    private Integer openShState;


    /**
     * qq
     * */
    private Long qq;

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
    /**
     * 签名
     */

    private String sign;

    /**
     * 7bao总金额
     */
    private BigDecimal totalAmountBao;

    private BigDecimal freezeAmountBao;
    /**
     * 可用金额
     */
    private BigDecimal availableAmountBao;

    /**
     * 申请时间
     * */
    private Long applyTime;

    /**
     * 金币绑定
     * @return
     */
    private  Boolean isShBind;

    /**
     * 总金额
     **/
    private BigDecimal totalAmount;

    /**
     * 冻结金额
     * */
    private BigDecimal freezeAmount;

    /**
     * 可用金额
     * */
    private BigDecimal availableAmount;

    private int yesOrNo;

    /**
     * 订单列表
     * */
    private JSONArray dataJson;

    public JSONArray getDataJson() {
        return dataJson;
    }

    public void setDataJson(JSONArray dataJson) {
        this.dataJson = dataJson;
    }

    public int getYesOrNo() {
        return yesOrNo;
    }

    public void setYesOrNo(int yesOrNo) {
        this.yesOrNo = yesOrNo;
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

    public Boolean getIsShBind() {
        return isShBind;
    }

    public void setIsShBind(Boolean shBind) {
        isShBind = shBind;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public BigDecimal getTotalAmountBao() {
        return totalAmountBao;
    }

    public void setTotalAmountBao(BigDecimal totalAmountBao) {
        this.totalAmountBao = totalAmountBao;
    }

    public BigDecimal getFreezeAmountBao() {
        return freezeAmountBao;
    }

    public void setFreezeAmountBao(BigDecimal freezeAmountBao) {
        this.freezeAmountBao = freezeAmountBao;
    }

    public BigDecimal getAvailableAmountBao() {
        return availableAmountBao;
    }

    public void setAvailableAmountBao(BigDecimal availableAmountBao) {
        this.availableAmountBao = availableAmountBao;
    }

    /**
     * 真实姓名
     * */
    private String realName;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getUserBind() {
        return isUserBind;
    }

    public void setUserBind(Boolean userBind) {
        isUserBind = userBind;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
