package com.wzitech.Z7Bao.frontend.business;

import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;

import java.math.BigDecimal;

/**
 * Created by chengXY on 2017/9/1.
 */
public interface IUpdateFundManager {
    /**
     * 结算该用户的7bao冻结或解冻金额
     * */
    void updateAmount(String loginAccount,String uid, int yesOrNo, BigDecimal amount,String orderId);
}
