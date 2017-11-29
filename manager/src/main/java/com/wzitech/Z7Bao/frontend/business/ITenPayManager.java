package com.wzitech.Z7Bao.frontend.business;


import com.wzitech.Z7Bao.frontend.dto.WithDrawRequestDTO;
import com.wzitech.Z7Bao.util.Alipay;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/17  wurongfan           ZW_C_JB_00021 商城资金7bao改造
 */
public interface ITenPayManager {

    /**
     * @param withDrawRequestDTO 封装了提现的参数
     * @return 返回一个提示信息
     */
    public Alipay withDraw(WithDrawRequestDTO withDrawRequestDTO);


    /**
     * 从财付通的接口去查询银行返回信息
     * @param outTradeNo
     * @return
     * @throws UnsupportedEncodingException
     */
    public Alipay query(String outTradeNo) throws UnsupportedEncodingException;

    /**
     * 财付通接口查询 包括业务
     * @param outTradeNo
     */
    public void quertTenPayWithBusiness(String outTradeNo) throws Exception;

    /**
     * 同步金币商城账号余额与7bao账户余额
     * @param fee  变动金额
     * @param sevenBaoAccountInfoEO  需要改動的實體類
     */

    public void updateFeeBothInZbaoAndGameGold(BigDecimal fee,SevenBaoAccountInfoEO sevenBaoAccountInfoEO,String orderId);

}
