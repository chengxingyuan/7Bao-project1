package com.wzitech.gamegold.usermgmt.respone;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

/**
 * Created by 340032 on 2017/8/17.
 */
public class SevenBaoUserBindResponse extends AbstractServiceResponse {

    /**
     * 7bao账户ID
     */
     private String ZbaoLoginAccount;
    /**
     * 签名不一致
     */
    private String Msg;

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getZbaoLoginAccount() {
        return ZbaoLoginAccount;
    }

    public void setZbaoLoginAccount(String zbaoLoginAccount) {
        ZbaoLoginAccount = zbaoLoginAccount;
    }
}
