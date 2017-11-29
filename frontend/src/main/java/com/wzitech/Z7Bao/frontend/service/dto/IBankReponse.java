package com.wzitech.Z7Bao.frontend.service.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

/**
 * Created by 340032 on 2017/8/29.
 */
public class IBankReponse extends AbstractServiceResponse {

    /**
     * 开户姓名
    */
    private String  realName ;

    /*
    渠道名称
     */
    private String bankName ;


    /*
    城市
     */
    private String city  ;

    /*
    省份
     */
    private String province ;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
