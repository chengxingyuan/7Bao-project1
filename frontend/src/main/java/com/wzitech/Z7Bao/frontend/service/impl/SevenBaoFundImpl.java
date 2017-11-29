package com.wzitech.Z7Bao.frontend.service.impl;

import com.wzitech.Z7Bao.frontend.business.ISystemConfigManager;
import com.wzitech.Z7Bao.frontend.entity.SystemConfig;
import com.wzitech.Z7Bao.frontend.service.ISevenBaoFund;
import com.wzitech.Z7Bao.frontend.service.dto.ISevenBaoFundReponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;

/**
 * Created by 340032 on 2017/9/4.
 */
@Service("SevenBaoService")
@Path("/FundBao")
@Produces("application/json; charset=UTF-8")
@Consumes("application/json; charset=UTF-8")
public class SevenBaoFundImpl implements ISevenBaoFund {

    @Autowired
    ISystemConfigManager systemConfigManager;

    @POST
    @Path("/querySevenBaoFund")
    @Override
    public ISevenBaoFundReponse queryFund() {
        ISevenBaoFundReponse reponse=new ISevenBaoFundReponse();
        ResponseStatus responseStatus = new ResponseStatus();
        reponse.setResponseStatus(responseStatus);
        try {
            SystemConfig systemConfig=systemConfigManager.getSystemConfigByKey(SystemConfigEnum.BALANCE_STOP_LINE.getKey());
            SystemConfig systemConfig1=systemConfigManager.getSystemConfigByKey(SystemConfigEnum.BALANCE_REMIND_LINE.getKey());
            //保证金
            reponse.setConfigKey(systemConfig.getConfigKey());
            reponse.setConfigValue(systemConfig.getConfigValue());
            //可用余额
            reponse.setAvailableFundKey(systemConfig1.getConfigKey());
            reponse.setAvailableFundValue(systemConfig1.getConfigValue());
        }catch (Exception e){
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
        }
        return reponse;
    }
}
