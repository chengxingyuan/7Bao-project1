package com.wzitech.Z7Bao.frontend.service.impl;

import com.wzitech.Z7Bao.frontend.business.IZbaoFunDetailManager;
import com.wzitech.Z7Bao.frontend.business.IZbaoPayOrderManager;
import com.wzitech.Z7Bao.frontend.business.IZbaoTransferManager;
import com.wzitech.Z7Bao.frontend.business.IZbaoWithdrawalsManager;
import com.wzitech.Z7Bao.frontend.entity.*;
import com.wzitech.Z7Bao.frontend.service.IFundDetailsService;
import com.wzitech.Z7Bao.frontend.service.dto.IFundDetailSRequest;
import com.wzitech.Z7Bao.frontend.service.dto.IFundDetailsReponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金明细列表
 * Created by 340032 on 2017/8/30.
 */
@Service("DetailService")
@Path("/Detail")
@Produces("application/json; charset=UTF-8")
@Consumes("application/json; charset=UTF-8")
public class FundDetailServiceImpl implements IFundDetailsService {

    @Autowired
    IZbaoFunDetailManager zbaoFunDetailManager;
    @Autowired
    ZbaoFundDetail zbaoFundDetail;
    @Autowired
    IZbaoWithdrawalsManager zbaoWithdrawalsManager;
    @Autowired
    IZbaoPayOrderManager zbaoPayOrderManager;
    @Autowired
    IZbaoTransferManager zbaoTransferManager;


    @GET
    @Path("/FundDetail")
    @Override
    public IFundDetailsReponse ZbaoFundDetail(@QueryParam("") IFundDetailSRequest params,
                                              @Context HttpServletRequest request,
                                              @Context HttpServletResponse response) {
        IFundDetailsReponse reponse = new IFundDetailsReponse();
        ResponseStatus responseStatus = new ResponseStatus();
        reponse.setResponseStatus(responseStatus);
        String userLoginAccount = CurrentUserContext.getUserLoginAccount();
        try {
            //判断用户是否为空
            if (userLoginAccount == null) {
                responseStatus.setStatus(ResponseCodes.UserEmptyWrong.getCode(), ResponseCodes.UserEmptyWrong.getMessage());
                return reponse;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("loginAccount", userLoginAccount);
            if (params.getType() != null) {
                map.put("type", params.getType());
            }
            if (params.getPayType() != null) {
                if (params.getPayType().equals("iSnull")) {
                    map.put("isSuccessNull", "null");
                } else if (params.getPayType().equals("true")) {
                    map.put("isSuccess", true);
                } else if (params.getPayType().equals("false")) {
                    map.put("isSuccess", false);
                }
            }
            if (params.getCreatTime() != null) {
                //接受过来的String类型转为date
                String creatTime = params.getCreatTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date parse = simpleDateFormat.parse(creatTime);
                map.put("startTime", parse);
            }
            if (params.getEndTime() != null) {
                //接受过来的String类型转为date
                String endTime = params.getEndTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date parse = simpleDateFormat.parse(endTime);
                if (parse != null) {
                    map.put("endTime", parse);
                }
            }
            //分页查询
            int page = params.getPage();
            int i = (page - 1) * ZbaoFundDetail.pageSize;
            GenericPage<ZbaoFundDetail> zbaoFundDetails = zbaoFunDetailManager.queryPage(map, ZbaoFundDetail.pageSize, i, "create_time", false);
            reponse.setZbaoFundDetails(zbaoFundDetails);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
        } catch (Exception e) {
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
        }
        return reponse;
    }

    /**
     * 资金详情
     *
     * @return
     */
    @Path("/DetailsPage")
    @GET
    @Override
    public IFundDetailsReponse queryDetails(@QueryParam("") IFundDetailSRequest params,
                                            @Context HttpServletRequest request,
                                            @Context HttpServletResponse response) {
        IFundDetailsReponse reponse = new IFundDetailsReponse();
        ResponseStatus responseStatus = new ResponseStatus();
        reponse.setResponseStatus(responseStatus);
        String userLoginAccount = CurrentUserContext.getUserLoginAccount();
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int type = params.getType();
        String czOrderId = params.getOrderId();
        String txOrderId = params.getOrderId();
        String cgOrderId = params.getOrderId();
        String freezeOrderId=params.getOrderId();
        try {
            if (params.getOrderId() == null) {
                responseStatus.setStatus(ResponseCodes.NotFoundOrder.getCode(), ResponseCodes.NotFoundOrder.getMessage());
            }
            if (params.getType() == null) {
                responseStatus.setStatus(ResponseCodes.OrderType.getCode(), ResponseCodes.OrderType.getMessage());
            }
            //查询订单详情
            if (type == 1) {
//                String转时间戳
                ZbaoFundDetail zbaoFundDetail = zbaoFunDetailManager.queryloginCz(userLoginAccount, czOrderId);
                ZbaoPayOrder zbaopayOrder = zbaoPayOrderManager.queryOrder(cgOrderId);
                Date date = zbaopayOrder.getCreateTime();
                Long time=date.getTime();
                //充值金额
                reponse.setAmount(zbaoFundDetail.getAmount());
                //创建时间
                reponse.setCreateTime(time);
                //充值状态
                reponse.setIsSuccess(zbaoFundDetail.getIsSuccess());
                //充值单号
                reponse.setCzOrderId(zbaoFundDetail.getCzOrderId());
                //5173对应账号
                reponse.setOutOrderId(zbaoFundDetail.getOutOrderId());
                //订单类型
                reponse.setOrderType(zbaopayOrder.getOrderType());
                //处理时间
                reponse.setDealTime(zbaopayOrder.getDealTime());
            } else if (type == 2) {
//                Date date=format.parse((zbaoFundDetail.getCreateTime()).toString());
                ZbaoFundDetail zbaoFundDetail = zbaoFunDetailManager.querylogintx(userLoginAccount, txOrderId);
                ZbaoWithdrawals zbaoWithdrawals = zbaoWithdrawalsManager.queryTx(zbaoFundDetail.getTxOrderId());
                reponse.setTxOrderId(zbaoFundDetail.getTxOrderId());
                Date date = zbaoFundDetail.getCreateTime();
                Long time=date.getTime();
                //金额
                reponse.setAmount(zbaoFundDetail.getAmount());
                //创建时间
                reponse.setCreateTime(time);
                //状态
                reponse.setIsSuccess(zbaoFundDetail.getIsSuccess());
                //提现服务费
                if (zbaoWithdrawals != null) {
                    reponse.setServiceAmount(zbaoWithdrawals.getServiceAmount());
                }
                //5173对应账号
                reponse.setOutOrderId(zbaoFundDetail.getOutOrderId());
                //银行
                reponse.setBankName(zbaoWithdrawals.getBankName());
                //银行卡号
                reponse.setCardCode(zbaoWithdrawals.getCardCode());
                //处理日志
                reponse.setFailReason(zbaoWithdrawals.getFailReason());
                //处理时间
                reponse.setDealTime(zbaoWithdrawals.getDealTime());
            } else if (type == 3) {
                ZbaoFundDetail zbaoFundDetail = zbaoFunDetailManager.querylogincg(userLoginAccount, cgOrderId);
                Date date = zbaoFundDetail.getCreateTime();
                Long time=date.getTime();
                //充值金额
                reponse.setAmount(zbaoFundDetail.getAmount());
                //创建时间
                reponse.setCreateTime(time);
                //状态
                reponse.setIsSuccess(zbaoFundDetail.getIsSuccess());
                //订单
                reponse.setCgOrderId(zbaoFundDetail.getCgOrderId());
                //5173订单
                reponse.setOutOrderId(zbaoFundDetail.getOutOrderId());
            } else if (type == 5) {
                //售得款
                ZbaoFundDetail zbaoFundDetail = zbaoFunDetailManager.queryloginCz(userLoginAccount, czOrderId);
                Date date = zbaoFundDetail.getCreateTime();
                Long time=date.getTime();
                //金额
                reponse.setAmount(zbaoFundDetail.getAmount());
                //创建时间
                reponse.setCreateTime(time);
                //状态
                reponse.setIsSuccess(zbaoFundDetail.getIsSuccess());
                //单号
                reponse.setCzOrderId(zbaoFundDetail.getCzOrderId());
                //5173单号
                reponse.setOutOrderId(zbaoFundDetail.getOutOrderId());
            } else if (type == 6) {
                Integer orderId=Integer.parseInt(cgOrderId);
                //老资金
                ZbaoFundDetail zbaoFundDetail = zbaoFunDetailManager.queryloginOld(userLoginAccount, orderId);
                Date date = zbaoFundDetail.getCreateTime();
                Long time=date.getTime();
                //充值金额
                reponse.setAmount(zbaoFundDetail.getAmount());
                //创建时间
                reponse.setCreateTime(time);
                //充值状态
                reponse.setIsSuccess(zbaoFundDetail.getIsSuccess());
                //充值单号
                reponse.setCzOrderId(zbaoFundDetail.getCzOrderId());
                //5173订单号
                reponse.setOutOrderId(zbaoFundDetail.getOutOrderId());
            }else if (type==7){
                //冻结资金
                ZbaoFundDetail zbaoFundDetail=zbaoFunDetailManager.querylogindj(userLoginAccount,freezeOrderId);
                Date date = zbaoFundDetail.getCreateTime();
                Long time=date.getTime();
                //订单号
                reponse.setFreezeOrderId(zbaoFundDetail.getFreezeOrderId());
                //金额
                reponse.setAmount(zbaoFundDetail.getAmount());
                //创建时间
                reponse.setCreateTime(time);
                //状态
                reponse.setIsSuccess(zbaoFundDetail.getIsSuccess());
            }else if (type==8){
                //解冻
                ZbaoFundDetail zbaoFundDetail=zbaoFunDetailManager.querylogindj(userLoginAccount,freezeOrderId);
                Date date = zbaoFundDetail.getCreateTime();
                Long time=date.getTime();
                //订单号
                reponse.setFreezeOrderId(zbaoFundDetail.getFreezeOrderId());
                //金额
                reponse.setAmount(zbaoFundDetail.getAmount());
                //创建时间
                reponse.setCreateTime(time);
                //状态
                reponse.setIsSuccess(zbaoFundDetail.getIsSuccess());
            }


        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
        } catch (Exception e) {
            e.printStackTrace();
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
        }
        return reponse;
    }


}
