package com.wzitech.Z7Bao.frontend.service.impl;

import com.wzitech.Z7Bao.frontend.business.IFundManager;
import com.wzitech.Z7Bao.frontend.entity.JBPayOrderTo7BaoEO;
import com.wzitech.Z7Bao.frontend.service.ISevenBaoUserBind;
import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.ZBaoPayType;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.usermgmt.business.ISevenBaoAccountManager;
import com.wzitech.gamegold.usermgmt.dao.rdb.ISevenBaoAccountDBDAO;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;
import com.wzitech.gamegold.usermgmt.request.QuerySevenBaoUserBindRequest;
import com.wzitech.gamegold.usermgmt.respone.SevenBaoUserBindResponse;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 340032 on 2017/8/17.
 */
@Service("SevenBaoUserBindService")
@Path("/BaoBind")
@Produces("application/json; charset=UTF-8")
@Consumes("application/json; charset=UTF-8")
public class SevenBaoUserBindImpl extends AbstractBaseService implements ISevenBaoUserBind {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    ISevenBaoAccountManager sevenBaoAccountManager;
    @Autowired
    ISevenBaoAccountDBDAO sevenBaoAccountDBDAO;

    @Autowired
    IFundManager fundManager;

    @Value("${7bao.ser.key}")
    private String serKey;

    /**
     * 绑定 解绑
     *
     * @param params
     * @param request
     * @param
     * @return
     */
    @Override
    @Path("/querySevenBaoUserBind")
    @POST
    public IServiceResponse querySevenBaoUserBind(QuerySevenBaoUserBindRequest params,
                                                  @Context HttpServletRequest request
    ) {
        SevenBaoUserBindResponse resp = new SevenBaoUserBindResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        resp.setResponseStatus(responseStatus);
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        try {
            logger.info("绑定解绑入参：{}", params);
            String format = String.format("%s_%s_%s_%s_%s_%s_%s_%s", params.getLoginAccount(), params.getUserBind(), params.getIsShBind(),
                    df.format(params.getTotalAmount()), df.format(params.getFreezeAmount()), df.format(params.getAvailableAmount()),params.getDataJson(), serKey);
            logger.info("绑定,解绑接口 querySevenBaoUserBind:{}", format);
            String toEncrypt = EncryptHelper.md5(format);
            if (!StringUtils.equals(toEncrypt, params.getSign())) {
                resp.setMsg(resp.getMsg() + "(签名不一致)");
                return resp;
            }
//            //接受商城传过来,老资金
//            BigDecimal total = params.getTotalAmount();
//            BigDecimal freeze = params.getFreezeAmount();
//            BigDecimal available = params.getAvailableAmount();

//        // 取出商城传过来的数据  "登入账号,QQ,名字,是否绑定,uid,手机号
            SevenBaoAccountInfoEO sevenBaoAccountInfoEO = sevenBaoAccountDBDAO.selectAccount(params.getLoginAccount());
            if (params.getUserBind()==false){
                sevenBaoAccountInfoEO.setUserBind(false);
                sevenBaoAccountInfoEO.setIsShBind(false);
            }else {
                sevenBaoAccountInfoEO.setIsUserBind(params.getUserBind());
                sevenBaoAccountInfoEO.setIsShBind(params.getIsShBind());
            }
            if (params.getTotalAmount().compareTo(BigDecimal.ZERO) > 0) {
                JSONArray jsonArray = JSONArray.fromObject(params.getDataJson());

                List<JBPayOrderTo7BaoEO> list = JSONArray.toList(jsonArray,JBPayOrderTo7BaoEO.class);
                for (JBPayOrderTo7BaoEO l:list){
                    l.getBalance();
                }
                fundManager.addPayOrderAndDetail(params.getTotalAmount(), null, null, ZBaoPayType.oldfull.getCode(), sevenBaoAccountInfoEO,list);
            }else {
                sevenBaoAccountDBDAO.update(sevenBaoAccountInfoEO);
            }

            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("初始化发生异常:{}", e);
        } catch (UnsupportedEncodingException e) {
            responseStatus.setStatus(ResponseCodes.FailToCreateConnect.getCode(), ResponseCodes.FailToCreateConnect.getMessage());
            logger.error("初始化发生异常:{}", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

    /**
     * 老用户初始化生成
     *
     * @param params  ZBUS1709070000088
     * @param request
     * @param
     * @return
     */
    @Override
    @Path("/querySevenBaoUser")
    @POST
    public IServiceResponse querySevenBaoUser(QuerySevenBaoUserBindRequest params, @Context HttpServletRequest request) {
        SevenBaoUserBindResponse resp = new SevenBaoUserBindResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        resp.setResponseStatus(responseStatus);
        try {
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
            String format = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", params.getLoginAccount(),
                    params.getQq(), params.getName(), params.getUserBind(), params.getUid(), params.getPhoneNumber(),
                    df.format(params.getTotalAmountBao()), params.getFreezeAmountBao(), df.format(params.getAvailableAmountBao()),
                    params.getApplyTime(), params.getIsShBind(),params.getDataJson(), serKey);
            logger.info("querySevenBaoUser初始化签名字符串：{}", format);
            String toEncrypt = EncryptHelper.md5(format);
            if (!StringUtils.equals(toEncrypt, params.getSign())) {
                logger.info("老用户初始化生成：{}", "(签名不一致)");
                resp.setMsg(resp.getMsg() + "(签名不一致)");
                return resp;
            }

            // 取出商城传过来的数据  "登入账号,QQ,名字,是否绑定,uid,手机号
            SevenBaoAccountInfoEO sevenBaoAccountInfoEO = new SevenBaoAccountInfoEO();
            sevenBaoAccountInfoEO.setLoginAccount(params.getLoginAccount());
            sevenBaoAccountInfoEO.setQq(params.getQq());
            sevenBaoAccountInfoEO.setName(params.getName());
            sevenBaoAccountInfoEO.setIsUserBind(params.getUserBind());
            sevenBaoAccountInfoEO.setUid(params.getUid());
            sevenBaoAccountInfoEO.setPhoneNumber(params.getPhoneNumber());
            sevenBaoAccountInfoEO.setIsShBind(params.getIsShBind());
            Date date = new Date(params.getApplyTime());
            String applyTime = simpleDateFormat.format(date);
            sevenBaoAccountInfoEO.setApplyTime(applyTime);
            //根据传过来的登入用户去7bao表查是否已经存在
            SevenBaoAccountInfoEO sevenBaoAccountInfoEO2 = sevenBaoAccountManager.queryDateByProp(params.getLoginAccount());
            String uid = null;
            if (sevenBaoAccountInfoEO2 == null) {
                sevenBaoAccountInfoEO.setCreateTime(new Date());
                //生成7bao 账户
                uid = sevenBaoAccountManager.insertSevenBao(sevenBaoAccountInfoEO);
            } else {
                uid = sevenBaoAccountInfoEO2.getZbaoLoginAccount();
                sevenBaoAccountInfoEO.setId(sevenBaoAccountInfoEO2.getId());
                //如果第二次请求,就直接更新这个用户
                sevenBaoAccountManager.updateBind(sevenBaoAccountInfoEO);
            }
            //根据LoginAccount查询,获取返回值zbaoLoginAccount(7报bao账户)返回给商城
            resp.setZbaoLoginAccount(uid);
            //商城传7bao老资金订单号转list
            JSONArray jsonArray = JSONArray.fromObject(params.getDataJson());
            List<JBPayOrderTo7BaoEO> list = JSONArray.toList(jsonArray,JBPayOrderTo7BaoEO.class);
            fundManager.creatRecharge(params.getUid(), params.getTotalAmountBao(), null, null, ZBaoPayType.oldfull.getCode(),list);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
            return resp;
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("初始化发生异常:{}", e);
        } catch (UnsupportedEncodingException e) {
            responseStatus.setStatus(ResponseCodes.FailToCreateConnect.getCode(), ResponseCodes.FailToCreateConnect.getMessage());
            logger.error("初始化发生异常:{}", e);
        } catch (Exception e) {
            logger.info("老用户初始化生成抛出未知异常：{}", e);
            e.printStackTrace();
        }
        return resp;
    }


}
