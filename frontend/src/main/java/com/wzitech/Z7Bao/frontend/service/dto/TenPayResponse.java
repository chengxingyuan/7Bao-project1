package com.wzitech.Z7Bao.frontend.service.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.Z7Bao.util.Alipay;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/17  wurongfan           ZW_C_JB_00021 商城资金7bao改造
 */
public class TenPayResponse extends AbstractServiceResponse{
    private Alipay alipay;

    public Alipay getAlipay() {
        return alipay;
    }

    public void setAlipay(Alipay alipay) {
        this.alipay = alipay;
    }
}
