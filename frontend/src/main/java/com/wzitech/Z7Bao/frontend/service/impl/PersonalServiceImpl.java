package com.wzitech.Z7Bao.frontend.service.impl;

import com.wzitech.Z7Bao.frontend.business.IFundStatisticsManager;
import com.wzitech.Z7Bao.frontend.business.ISystemConfigManager;
import com.wzitech.Z7Bao.frontend.business.IZbaoBankManager;
import com.wzitech.Z7Bao.frontend.business.IZbaoFunDetailManager;
import com.wzitech.Z7Bao.frontend.entity.SystemConfig;
import com.wzitech.Z7Bao.frontend.entity.ZbaoBank;
import com.wzitech.Z7Bao.frontend.entity.ZbaoFundDetail;
import com.wzitech.Z7Bao.frontend.service.IPersonalService;
import com.wzitech.Z7Bao.frontend.service.dto.IPersonalReponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import com.wzitech.gamegold.usermgmt.business.ISevenBaoAccountManager;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import java.math.BigDecimal;

/**
 * 个人中心信息
 * Created by 340032 on 2017/8/30.
 */
@Service("PersonalService")
@Path("/Personal")
@Produces("application/json; charset=UTF-8")
@Consumes("application/json; charset=UTF-8")
public class PersonalServiceImpl implements IPersonalService {

    @Autowired
    ISevenBaoAccountManager sevenBaoAccountManager;
    @Autowired
    IZbaoBankManager zbaoBankManager;
    @Autowired
    SevenBaoAccountInfoEO sevenBaoAccountInfoEO;
    @Autowired
    ZbaoBank zbaoBank;
    @Autowired
    IZbaoFunDetailManager zbaoFunDetailManager;

    @Autowired
    ISystemConfigManager systemConfigManager;

    @GET
    @Path("/PersonalCenter")
    @Override
    public IPersonalReponse qureyPersonal() {
        IPersonalReponse reponse=new IPersonalReponse();
        ResponseStatus responseStatus = new ResponseStatus();
        reponse.setResponseStatus(responseStatus);
        try {
            //获取当前用户信息
            String userLoginAccount = CurrentUserContext.getUserLoginAccount();
            SevenBaoAccountInfoEO sevenBaoAccountInfoEO=sevenBaoAccountManager.queryDateByProp(userLoginAccount);
            if (sevenBaoAccountInfoEO==null){
                responseStatus.setStatus(ResponseCodes.NotExistedUser.getCode(), ResponseCodes.NotExistedUser.getMessage());
                return reponse;
            }
            if (sevenBaoAccountInfoEO!=null) {
                if (sevenBaoAccountInfoEO.getBankName()!=null) {
                    //获取银行图片
                    ZbaoBank zbaoBank = zbaoBankManager.selectByName(sevenBaoAccountInfoEO.getBankName());
                    reponse.setImgUrl(zbaoBank.getImgUrl());
                }
                reponse.setBankName(sevenBaoAccountInfoEO.getBankName());
                reponse.setSubBankName(sevenBaoAccountInfoEO.getSubBankName());
                reponse.setAccountNO(sevenBaoAccountInfoEO.getAccountNO());
                reponse.setProvince(sevenBaoAccountInfoEO.getProvince());
                reponse.setCity(sevenBaoAccountInfoEO.getCity());
                reponse.setName(sevenBaoAccountInfoEO.getName());
            }
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        }catch (SystemException e){
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
        }catch (Exception e){
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
        }
        return reponse;
    }
    /**
     * 账号管理页面
     * @return
     */
    @Path("/UserInfo")
    @GET
    @Override
    public IPersonalReponse queryNumber() {
        IPersonalReponse reponse=new IPersonalReponse();
        ResponseStatus responseStatus = new ResponseStatus();
        reponse.setResponseStatus(responseStatus);
        try {
            //获取当前用户信息
            String userLoginAccount = CurrentUserContext.getUserLoginAccount();
            SevenBaoAccountInfoEO sevenBaoAccountInfoEO=sevenBaoAccountManager.queryDateByProp(userLoginAccount);
            //获取保证金
            SystemConfig systemConfig=systemConfigManager.getSystemConfigByKey(SystemConfigEnum.BALANCE_STOP_LINE.getKey());
            SystemConfig systemConfig1=systemConfigManager.getSystemConfigByKey(SystemConfigEnum.BALANCE_REMIND_LINE.getKey());
            // 保证金
            reponse.setConfigValue(systemConfig.getConfigValue());
             //可用金额
            reponse.setAvailableFundValue(systemConfig1.getConfigValue());
            if (sevenBaoAccountInfoEO!=null){
                //冻结资金
                reponse.setFreezeAmount(sevenBaoAccountInfoEO.getFreezeAmount());
//                BigDecimal big = new BigDecimal(systemConfig.getConfigValue());
//                //如果可用余额小于保证金
//                if (big.compareTo(sevenBaoAccountInfoEO.getAvailableAmount())==1){
//                    //如果可以约小于保证金直接返回 零
//                    reponse.setDeposit(BigDecimal.ZERO);
//                }else {
//                    BigDecimal balance=sevenBaoAccountInfoEO.getAvailableAmount().subtract(big);
//                    //可以余额
//                    reponse.setDeposit(balance);
//                }
                //可用金额
                reponse.setAvailableAmount(sevenBaoAccountInfoEO.getAvailableAmount());
                //5173账户
                reponse.setLoginAccount(sevenBaoAccountInfoEO.getLoginAccount());
                //7bao账户
                reponse.setZbaoLoginAccount(sevenBaoAccountInfoEO.getZbaoLoginAccount());
                //是否开通新流出
                reponse.setUserBind(sevenBaoAccountInfoEO.getUserBind());
                //申请时间
                reponse.setApplyTime(sevenBaoAccountInfoEO.getApplyTime());
                //审核时间
                reponse.setCreateTime(sevenBaoAccountInfoEO.getCreateTime());
                //是否开通收货
                reponse.setIsShBind(sevenBaoAccountInfoEO.getIsShBind());
                //关闭时间
                if (sevenBaoAccountInfoEO.getUserBind()==false){
                    reponse.setUpdateTime(sevenBaoAccountInfoEO.getUpdateTime());
                }
            }
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        }catch (SystemException e){
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
        }catch (Exception e){
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
        }
        return reponse;
    }


}
