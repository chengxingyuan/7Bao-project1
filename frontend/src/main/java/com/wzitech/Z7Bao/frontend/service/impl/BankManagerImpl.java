package com.wzitech.Z7Bao.frontend.service.impl;

import com.wzitech.Z7Bao.frontend.business.IZbaoBankManager;
import com.wzitech.Z7Bao.frontend.business.IZbaoCityManager;
import com.wzitech.Z7Bao.frontend.business.IZbaoServiceAmountConfigManager;
import com.wzitech.Z7Bao.frontend.business.IZbaoWithdrawalsManager;
import com.wzitech.Z7Bao.frontend.entity.ZbaoCityEO;
import com.wzitech.Z7Bao.frontend.entity.ZbaoServiceAmountConfig;
import com.wzitech.Z7Bao.frontend.entity.ZbaoWithdrawals;
import com.wzitech.Z7Bao.frontend.service.IBankManager;
import com.wzitech.Z7Bao.frontend.service.ZbaoBankResponse;
import com.wzitech.Z7Bao.frontend.service.dto.IBankReponse;
import com.wzitech.Z7Bao.frontend.service.dto.ZbaoCityResponse;
import com.wzitech.Z7Bao.frontend.service.dto.ZbaoServiceAmountResponse;
import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import java.util.List;
import java.util.Map;

/**
 * Created by 340032 on 2017/8/29.
 */
@Service("BankService")
@Path("/Bank")
@Produces("application/json; charset=UTF-8")
@Consumes("application/json; charset=UTF-8")
public class BankManagerImpl extends AbstractBaseService implements IBankManager {

    @Autowired
    IZbaoWithdrawalsManager zbaoWithdrawalsManager;

    @Autowired
    IZbaoCityManager zbaoCityManager;

    @Autowired
    ZbaoCityEO zbaoCityEO;

    @Autowired
    ZbaoWithdrawals zbaoWithdrawals;

    @Autowired
    IZbaoServiceAmountConfigManager zbaoServiceAmountConfigManager;

    @Autowired
    IZbaoBankManager zbaoBankManager;

    @GET
    @Path("/BankData")
    @Override
    public IBankReponse bankData() {
        IBankReponse reponse = new IBankReponse();
        ResponseStatus responseStatus = new ResponseStatus();
        reponse.setResponseStatus(responseStatus);
        try {
            //查询用户信息
            String userLoginAccount = CurrentUserContext.getUserLoginAccount();
            ZbaoWithdrawals zbaoWithdrawals = zbaoWithdrawalsManager.queryloginAccount(userLoginAccount);
            ZbaoCityEO zbaoCityEO = zbaoCityManager.selectProvinceId(Long.valueOf(zbaoWithdrawals.getProvince()));
            ZbaoCityEO zbaoCityEO1 = zbaoCityManager.selectCityId(Long.valueOf(zbaoWithdrawals.getCity()));
            if (zbaoCityEO==null){
                return null;
            }
            if (zbaoCityEO1==null){
                return null;
            }
            if (zbaoWithdrawals != null) {
                reponse.setBankName(zbaoWithdrawals.getBankName());
                reponse.setRealName(zbaoWithdrawals.getRealName());
                reponse.setProvince(zbaoCityEO.getProvinceName());
                reponse.setCity(zbaoCityEO1.getCityName());
                return reponse;
            }
        }catch (SystemException e){
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
        }catch (Exception e){
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
        }
        return null;
    }


    @GET
    @Path("/getCityListByProvinceId")
    @Override
    public IServiceResponse getCityListByProvinceId(@QueryParam("provinceId")  Long provinceId) {
        ZbaoCityResponse cityResponse = new ZbaoCityResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        cityResponse.setResponseStatus(responseStatus);
        try {
            List<JSONObject>  zbaoCityEOList = zbaoCityManager.selectByProvinceId(provinceId);
            cityResponse.setList(zbaoCityEOList);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取城市列表发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取城市列表发生未知异常:{}", e);
        }
        return cityResponse;
    }

    @GET
    @Path("/getProvinceList")
    @Override
    public IServiceResponse getProvinceList() {
        ZbaoCityResponse cityResponse = new ZbaoCityResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        cityResponse.setResponseStatus(responseStatus);
        try {
            List<JSONObject>  zbaoCityEOList = zbaoCityManager.selectProvince();
            cityResponse.setList(zbaoCityEOList);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取省份发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取省份发生未知异常:{}", e);
        }
        return cityResponse;
    }

    @GET
    @Path("/getServiceAmountConfig")
    @Override
    public IServiceResponse getServiceAmountConfig() {
        ZbaoServiceAmountResponse response = new ZbaoServiceAmountResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            List<Map<String, Object>> list = zbaoServiceAmountConfigManager.selectAll();
            response.setData(list);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取提现手续费发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取提现手续费发生未知异常:{}", e);
        }
        return response;
    }

    @GET
    @Path("/getBankNames")
    @Override
    public IServiceResponse getBankNames() {
        ZbaoBankResponse response = new ZbaoBankResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            List<Map<String, Object>> list = zbaoBankManager.selectBankNames();
            response.setData(list);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取银行列表发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取银行列表发生未知异常:{}", e);
        }
        return response;
    }
}
