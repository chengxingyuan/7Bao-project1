package com.wzitech.Z7Bao.frontend.service.impl;

import com.wzitech.Z7Bao.frontend.business.IUpdateFundManager;
import com.wzitech.Z7Bao.frontend.service.IUpdateFundService;
import com.wzitech.Z7Bao.frontend.service.dto.UpdateFunResponse;
import com.wzitech.Z7Bao.frontend.service.dto.UpdateFundRequest;
import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.usermgmt.business.ISevenBaoAccountManager;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by chengXY on 2017/8/23.
 * 冻结/解冻资金 金币商城通过http调用此处，来更新7bao冻结金额
 */

@Service("UpdateFundService")
@Path("/updatefund")
@Produces("application/json;charset=UTF-8")
public class UpdateFundSeviceImpl extends AbstractBaseService implements IUpdateFundService {

    @Autowired
    private IUpdateFundManager updateManager;

    @Value("${7Bao.fund.serKey}")
    private String freezeserKey;

    @Override
    @POST
    @Path("/changefund")
    public UpdateFunResponse changeFund(UpdateFundRequest updateFundRequest,@Context HttpServletRequest request) {
        ResponseStatus responseStatus = new ResponseStatus();
        UpdateFunResponse resp = new UpdateFunResponse();
        responseStatus.setMessage("验证失败");
        resp.setResponseStatus(responseStatus);

        String loginAccount = updateFundRequest.getLoginAccount();
        String uid = updateFundRequest.getUid();
        BigDecimal amount = updateFundRequest.getFreezeFund();
        Integer yesOrNo = updateFundRequest.getYesOrNo();
        String orderId = updateFundRequest.getOrderId();
        String toEncrypt = "";
        BigDecimal getAmount = updateFundRequest.getFreezeFund().setScale(2, RoundingMode.HALF_UP);
        try {
            String format = String.format("%s_%s_%s_%s_%s_%s",updateFundRequest.getLoginAccount(),updateFundRequest.getUid(),
                    getAmount,updateFundRequest.getYesOrNo(),orderId,freezeserKey);
            logger.info("参数签名{}：",format);
             toEncrypt = EncryptHelper.md5(format);
        }catch (Exception e){
            e.printStackTrace();
        }
        //签名不匹配直接返回
        if (!toEncrypt.equals(updateFundRequest.getSign())) {
            responseStatus.setStatus(ResponseCodes.SignError.getCode(), ResponseCodes.SignError.getMessage());
            return resp;
        }

        try {
            //冻结/解冻 金额结算并更新账号信息
            updateManager.updateAmount(loginAccount,uid, yesOrNo, amount,orderId);
            responseStatus.setStatus(ResponseCodes.Success.getCode(),ResponseCodes.Success.getMessage());
            resp.setException(false);
            resp.setSuccess(true);
        }catch (Exception e){
            responseStatus.setStatus(ResponseCodes.FailToCreateConnect.getCode(), ResponseCodes.FailToCreateConnect.getMessage());
            resp.setException(true);
            resp.setSuccess(false);
        }

        return  resp;
    }
}
