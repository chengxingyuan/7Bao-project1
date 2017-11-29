package com.wzitech.Z7Bao.frontend.service.impl;

import com.wzitech.Z7Bao.frontend.business.IFundManager;
import com.wzitech.Z7Bao.frontend.business.ITenPayManager;
import com.wzitech.Z7Bao.frontend.dto.WithDrawRequestDTO;
import com.wzitech.Z7Bao.frontend.entity.ZbaoPayOrder;
import com.wzitech.Z7Bao.frontend.service.IFundService;
import com.wzitech.Z7Bao.frontend.service.dto.FundRequest;
import com.wzitech.Z7Bao.frontend.service.dto.FundResponse;
import com.wzitech.Z7Bao.frontend.service.dto.TenPayResponse;
import com.wzitech.Z7Bao.util.Alipay;
import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.FundType;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.ZBaoPayType;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/17  wurongfan           ZW_C_JB_00021 商城资金7bao改造
 */
@Service("FundService")
@Path("/fund")
@Produces("application/json;charset=UTF-8")
public class FundServiceImpl extends AbstractBaseService implements IFundService {

    @Autowired
    private IFundManager fundManager;

    @Value("${7Bao.fund.serKey}")
    private String serKey;


    @Value("${7Bao.fund.serKey}")
    private String rechargeKey = "";


    @Value("${zbaoMd5Key}")
    private String zbaoMd5Key = "";

    @Autowired
    private ITenPayManager tenPayManager;

    //单笔最大充值金额3000
    private final BigDecimal maxRechargeAmount = new BigDecimal(3000);

    /**
     * 查询充值状态接口
     * http://7bao.com/frontend/services/fund/transferState
     *
     * @param fundRequest
     * @return
     */
    @Override
    @GET
    @Path("/transferState")
    public FundResponse transferState(@QueryParam("") FundRequest fundRequest, @Context HttpServletRequest request) {
        ResponseStatus responseStatus = new ResponseStatus();
        FundResponse response = new FundResponse();
        response.setSuccess(false);
        response.setException(true);
        responseStatus.setMessage("验证失败");
        response.setResponseStatus(responseStatus);

        String userId = fundRequest.getUserId();
        String orderId = fundRequest.getOrderId();
        String format = String.format("%s_%s_%s", serKey, userId, orderId);
        String toEncrypt = null;
        try {
            toEncrypt = EncryptHelper.md5(format);
            //签名不匹配直接返回
            if (!toEncrypt.equals(fundRequest.getSign())) {
                responseStatus.setStatus(ResponseCodes.SignError.getCode(), ResponseCodes.SignError.getMessage());
                return response;
            }
            //查询当前订单的状态并返回信息
            Integer payOrderInfo = fundManager.getPayOrderStatus(fundRequest.getOrderId(), userId);
            FundType typeByCode = FundType.getTypeByCode(payOrderInfo);
            responseStatus.setStatus("00", typeByCode.getName());
            response.setException(false);
            response.setSuccess(true);
            response.setOrderId(orderId);
            return response;
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
        } catch (Exception e) {
            e.printStackTrace();
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
        }

        return response;
    }

    /**
     * 转账到7bao接口 增加账号余额 自动充值单接口
     *
     * @param fundRequest
     * @param request
     * @return
     */

    @Override
    @POST
    @Path("/transfer")
    public FundResponse etchRecharge(@FormParam("") FundRequest fundRequest, @Context HttpServletRequest request) {
        ResponseStatus responseStatus = new ResponseStatus();
        FundResponse response = new FundResponse();
        response.setSuccess(false);
        response.setException(true);
        responseStatus.setMessage("验证失败");
        response.setResponseStatus(responseStatus);
        try {
            String userId = fundRequest.getUserId();
            String orderId = fundRequest.getOrderId();
            BigDecimal money = fundRequest.getMoney();
            if (money != null && money.compareTo(BigDecimal.ZERO) <= 0) {
                responseStatus.setStatus(ResponseCodes.MoneyZeroErrorType.getCode(), ResponseCodes.MoneyZeroErrorType.getMessage());
                return response;
            }
            String remark = fundRequest.getRemark();
            String jbOrderId = fundRequest.getJbOrderId();

            String format = String.format("%s_%s_%s_%s_%s_%s", serKey, userId, orderId, jbOrderId
                    , money, remark);
            logger.info("增加账号余额接口：{}", format);
            String toEncrypt = EncryptHelper.md5(format);
            //签名不匹配直接返回
            if (!toEncrypt.equals(fundRequest.getSign())) {
                responseStatus.setStatus(ResponseCodes.SignError.getCode(), ResponseCodes.SignError.getMessage());
                return response;
            }
            //调用创建充值单接口
            orderId = fundManager.creatRecharge(userId, money, orderId, jbOrderId, ZBaoPayType.etch.getCode(), null);
            if (orderId != null) {
                responseStatus.setStatus("00", "充值成功");
                response.setException(false);
                response.setSuccess(true);
                response.setOrderId(orderId);
                return response;
            }
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            return response;

        } catch (Exception e) {
            logger.info("扣除采购款接口未知异常：{}", e);
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
        }
        return response;
    }

    /**
     * 扣除采购款接口
     *
     * @param fundRequest
     * @param request
     * @return
     */
    @Override
    @POST
    @Path("/payment")
    public FundResponse payment(@FormParam("") FundRequest fundRequest, @Context HttpServletRequest request) {
        ResponseStatus responseStatus = new ResponseStatus();
        FundResponse response = new FundResponse();
        response.setSuccess(false);
        response.setException(true);
        responseStatus.setMessage("验证失败");
        response.setResponseStatus(responseStatus);
        try {
            String userId = fundRequest.getUserId();
            String orderId = fundRequest.getOrderId();
            BigDecimal money = fundRequest.getMoney().setScale(2, RoundingMode.HALF_UP);
            String remark = fundRequest.getRemark();

            if (money != null && money.compareTo(BigDecimal.ZERO) <= 0) {
                responseStatus.setStatus(ResponseCodes.MoneyZeroErrorType.getCode(), ResponseCodes.MoneyZeroErrorType.getMessage());
                return response;
            }

            String format = String.format("%s_%s_%s_%s_%s", serKey, userId, orderId,
                    money, remark);
            String toEncrypt = EncryptHelper.md5(format);
            //签名不匹配直接返回
            if (!toEncrypt.equals(fundRequest.getSign())) {
                responseStatus.setStatus(ResponseCodes.SignError.getCode(), ResponseCodes.SignError.getMessage());
                return response;
            }
            //调用创建扣款单接口

            orderId = fundManager.creatTransferDeduction(userId, money, orderId);

            if (orderId != null) {
                responseStatus.setStatus("00", "扣款成功");
                response.setException(false);
                response.setSuccess(true);
                response.setOrderId(orderId);
                return response;
            }
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            return response;

        } catch (Exception e) {
            logger.info("扣除采购款接口未知异常：{}", e);
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
        }
        return response;
    }

    /**
     * 申请充值，创建充值单
     *
     * @param fundRequest
     * @param request
     * @return
     */
    @GET
    @Path("/manualRecharge")
    public FundResponse manualRecharge(@QueryParam("") FundRequest fundRequest, @Context HttpServletRequest request) {

        FundResponse response = new FundResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        //默认为失败的状态
        response.setSuccess(false);
        response.setException(true);
        //通过当前登录信息获取用户
        String uid = CurrentUserContext.getUser().getUid();
        if (StringUtils.isBlank(uid)) {
            response.setException(false);
            responseStatus.setStatus(ResponseCodes.NotExistedUser.getCode(), ResponseCodes.NotExistedUser.getMessage());
            return response;
        }
        try {
            BigDecimal money = fundRequest.getMoney();
            //充值金额不为空，大于0，无小数
            if (money == null || money.compareTo(BigDecimal.ZERO) < 0 || money.compareTo(maxRechargeAmount) > 0
//                    || new BigDecimal(money.intValue()).compareTo(money) != 0
                    ) {
                responseStatus.setStatus(ResponseCodes.ErrorRechargeAmount.getCode(), ResponseCodes.ErrorRechargeAmount.getMessage());
                return response;
            }
            money = money.setScale(2, BigDecimal.ROUND_HALF_UP);
            Integer rechargeType = fundRequest.getRechargeType();
            //是否为合法手动充值渠道
            if (rechargeType == null || !ZBaoPayType.isValidManualType(rechargeType)) {
                responseStatus.setStatus(ResponseCodes.ErrorRechargeType.getCode(), ResponseCodes.ErrorRechargeType.getMessage());
                return response;
            }

            String orderId = fundManager.creatRecharge(uid, money, null, null, Integer.valueOf(rechargeType), null);
            response.setOrderId(orderId);
//            response.setRechargeAmount(new Integer(money.intValue()).toString());
            response.setRechargeAmount(money.toString());
            response.setRechargeType(rechargeType);
            response.setSuccess(true);
            response.setException(false);
        } catch (Exception ex) {
            logger.error("手动充值发生异常，异常信息：" + ex.getMessage());
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
        }
        return response;
    }

    /**
     * 执行充值操作，构建支付二维码链接
     *
     * @param fundRequest
     * @param request
     * @return
     */
    @GET
    @Path("/getPayQR")
    public FundResponse getPayQR(@QueryParam("") FundRequest fundRequest, @Context HttpServletRequest request) {
        FundResponse response = new FundResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        //默认为失败的状态
        response.setSuccess(false);
        response.setException(true);
        String orderId = fundRequest.getOrderId();
        if (StringUtils.isBlank(orderId)) {
            responseStatus.setStatus(ResponseCodes.EmptyPayOrderId.getCode(), ResponseCodes.EmptyPayOrderId.getMessage());
            return response;
        }
        try {
            Map<String, Object> result = fundManager.buildPayUrl(orderId);
            String url = (String) result.get("url");
            Integer liveTime = (Integer) (result.get("liveTime"));
            long createTimestamp = (Long) (result.get("createTimestamp"));
            Integer payChannel = (Integer) result.get("payChannel");
            Integer rechargeType = (Integer) result.get("payType");
            response.setCreateTimestamp(createTimestamp);
            response.setPayQrUrl(url);
            response.setLiveTime(liveTime);
            response.setPayChannel(payChannel);
            response.setRechargeType(rechargeType);
            response.setSuccess(true);
            response.setException(false);
        } catch (Exception e) {
            logger.error(e.getMessage() + "orderId:" + orderId);
            response.setException(true);
            responseStatus.setMessage(e.getMessage());
        }
        return response;
    }

    /**
     * 关闭支付订单
     *
     * @param fundRequest 请求参数，订单号
     * @param request
     * @return
     */
   /* @GET
    @Path("/closePayOrder")
    public FundResponse closePayOrder(@QueryParam("") FundRequest fundRequest, @Context HttpServletRequest request) {
        FundResponse response = new FundResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        //默认为失败的状态
        response.setSuccess(false);
        response.setException(true);
        String orderId = fundRequest.getOrderId();
        if (StringUtils.isBlank(orderId)) {
            responseStatus.setStatus(ResponseCodes.EmptyPayOrderId.getCode(), ResponseCodes.EmptyPayOrderId.getMessage());
            return response;
        }
        try {
            fundManager.closePayOrder(orderId);
            response.setSuccess(true);
            response.setException(false);
        } catch (Exception e) {
            responseStatus.setStatus(ResponseCodes.ClosePayOrderError.getCode(), ResponseCodes.ClosePayOrderError.getMessage());
            logger.info("关闭支付单失败:" + e.getMessage());
        }
        return response;
    }*/

    /**
     * 获取订单信息
     *
     * @param fundRequest
     * @return
     */
    @GET
    @Path("/orderInfo")
    public FundResponse payOrderInfo(@QueryParam("") FundRequest fundRequest) {
        FundResponse response = new FundResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        //默认为失败的状态
        response.setSuccess(false);
        response.setException(true);
        String orderId = fundRequest.getOrderId();
        if (StringUtils.isBlank(orderId)) {
            responseStatus.setStatus(ResponseCodes.EmptyPayOrderId.getCode(), ResponseCodes.EmptyPayOrderId.getMessage());
            return response;
        }
        ZbaoPayOrder payOrderInfo = fundManager.getPayOrderInfo(orderId);
        if (payOrderInfo == null) {
            responseStatus.setStatus(ResponseCodes.NotExistPayOrder.getCode(), ResponseCodes.NotExistPayOrder.getMessage());
            return response;
        }
        response.setPayOrder(payOrderInfo);
        return response;
    }

    @Path("/withdraw")
    @POST
    @Override
    public IServiceResponse withDraw(@FormParam("") FundRequest tenPayRequest, @Context HttpServletRequest request) {
        TenPayResponse tenPayResponse = new TenPayResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        tenPayResponse.setResponseStatus(responseStatus);
        try {
            //获取当前登录用户信息
            SevenBaoAccountInfoEO userInfoEO = (SevenBaoAccountInfoEO) CurrentUserContext.getUser();
            if (userInfoEO == null || StringUtils.isBlank(userInfoEO.getLoginAccount())) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
                return tenPayResponse;
            }
            String loginAccount = userInfoEO.getLoginAccount();
            //根据当前登录账户去查询账户信息
            BigDecimal withdrawWant = tenPayRequest.getFee().setScale(2, RoundingMode.DOWN);
            WithDrawRequestDTO withDrawRequestDTO = new WithDrawRequestDTO();
            withDrawRequestDTO.setBankName(tenPayRequest.getBankName());
            withDrawRequestDTO.setAccountProp(tenPayRequest.getAccountProp());
            withDrawRequestDTO.setAccountNo(tenPayRequest.getAccountNO());
            withDrawRequestDTO.setAccoutName(tenPayRequest.getAccountName());
            withDrawRequestDTO.setDesc("货款");
            withDrawRequestDTO.setLoginAccount(loginAccount);
            withDrawRequestDTO.setProvince(tenPayRequest.getArea());
            withDrawRequestDTO.setCity(tenPayRequest.getCity());
            withDrawRequestDTO.setSubBankName(tenPayRequest.getSubBankName());
            withDrawRequestDTO.setWithDrawMoney(withdrawWant);
            Alipay BQR = tenPayManager.withDraw(withDrawRequestDTO);
            if ("T".equals(BQR.getIs_success())) {
                responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
                logger.info("调用财付通提现接口成功:" + BQR.getError());
            } else {
                responseStatus.setStatus(ResponseCodes.FailToConntectCaiFuTong.getCode(), ResponseCodes.FailToConntectCaiFuTong.getMessage());
                logger.info("调用财付通提现接口失败:" + BQR.getError());
                tenPayResponse.setAlipay(BQR);
                return tenPayResponse;
            }
            tenPayResponse.setAlipay(BQR);
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("提现发生异常:{}", e);
        } catch (Exception e) {
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("提现发生未知异常:{}", e);
        }
        return tenPayResponse;
    }


}
