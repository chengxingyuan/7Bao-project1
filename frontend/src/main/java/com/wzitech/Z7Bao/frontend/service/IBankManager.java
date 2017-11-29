package com.wzitech.Z7Bao.frontend.service;

import com.wzitech.Z7Bao.frontend.entity.ZbaoWithdrawals;
import com.wzitech.Z7Bao.frontend.service.dto.IBankReponse;
import com.wzitech.chaos.framework.server.common.IServiceResponse;

import javax.ws.rs.QueryParam;

/**
 * 7bao 银行账号管理
 * Created by 340032 on 2017/8/29.
 */
public interface IBankManager {

    IBankReponse bankData();

    /**
     * 根据省份ID获取城市列表
     *
     * @param provinceId
     * @return
     */
    IServiceResponse getCityListByProvinceId(@QueryParam("provinceId") Long provinceId);

    IServiceResponse getProvinceList();

    //14.获取提现手续费配置
    IServiceResponse getServiceAmountConfig();

//    查询开通提现的银行列表
    IServiceResponse getBankNames();
}
