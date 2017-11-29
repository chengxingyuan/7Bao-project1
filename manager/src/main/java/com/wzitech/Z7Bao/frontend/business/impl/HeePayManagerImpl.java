package com.wzitech.Z7Bao.frontend.business.impl;

import com.wzitech.Z7Bao.frontend.business.IHeePayManager;
import com.wzitech.Z7Bao.frontend.business.IZbaoPayOrderManager;
import com.wzitech.Z7Bao.frontend.entity.ZbaoPayOrder;
import com.wzitech.Z7Bao.util.HeePayUtils;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.enums.ZBaoPayType;
import com.wzitech.gamegold.common.utils.HttpConnectionUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by guotx on 2017/9/22 14:49.
 */
@Component
public class HeePayManagerImpl implements IHeePayManager {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    @Autowired
    private HeePayConfig heePayConfig;
    @Autowired
    private IZbaoPayOrderManager payOrderManager;
    private Logger logger = LoggerFactory.getLogger(HeePayManagerImpl.class);

    @Override
    public String getPayUrl(String outTradeNo, String body, Integer rechargeAmount, ZBaoPayType payType) {
        ZbaoPayOrder payOrder = payOrderManager.queryOrder(outTradeNo);
        if (payOrder.getPayQr() != null && payOrder.getPayQr().length() > 0) {
            return payOrder.getPayQr();
        }
        try {
            HeePayModel model = new HeePayModel();
            model.setVersion("1");
//            微信30，支付宝22
            if (payType.getCode() == ZBaoPayType.weixinPay.getCode()) {
                model.setPay_type("30");
            } else if (payType.getCode() == ZBaoPayType.aliPay.getCode()) {
                model.setPay_type("22");
            }

            model.setAgent_id(heePayConfig.getAgentId());
            model.setAgent_bill_id(outTradeNo.trim());
            model.setAgent_bill_time(sdf.format(new Date()));

            model.setPay_amt(new BigDecimal(rechargeAmount).divide(new BigDecimal(100)).toString());
            model.setNotify_url(heePayConfig.getNotifyUrl());
            model.setReturn_url(heePayConfig.getReturnUrl());
            model.setUser_ip(heePayConfig.getMch_create_ip());

            model.setGoods_name(body);
            model.setGoods_num("1");
            model.setGoods_note(body);

            model.setRemark(String.format(body + model.getPay_amt()));

            //获取提交地址
            String sign = HeePayUtils.SignMD5(model, heePayConfig.getSignKey());
            String heePayUrl = HeePayUtils.GateWaySubmitUrl(sign, model);
            logger.info("汇付支付构建请求url：{}",heePayUrl);
            CloseableHttpClient httpClient = HttpConnectionUtils.getHttpClient();
            HttpGet get = new HttpGet(heePayUrl);
            CloseableHttpResponse httpResponse = httpClient.execute(get);
            String resStr = EntityUtils.toString(httpResponse.getEntity());
            logger.info("汇付支付接口，单号：{},返回结果:【{}】", outTradeNo, resStr);
            JSONObject jsonObject = JSONObject.fromObject(resStr);
            String code = jsonObject.getString("code");
            if ("0000".equals(code)) {
                String qr_url = jsonObject.getString("qr_code_url");
                ZbaoPayOrder newOrder = new ZbaoPayOrder();
                newOrder.setId(payOrder.getId());
                newOrder.setPayQr(qr_url);
                payOrderManager.update(newOrder);
                return qr_url;
            }
            return null;
        } catch (Exception ex) {
            logger.error("调用汇付宝支付发生异常，订单:{},错误信息:{}", outTradeNo + "|" + payType.getName(), ExceptionUtils.getRootCauseStackTrace(ex));
        }
        return null;
    }


    @Override
    public boolean heePayQuery(String tradeNo) {
        HeePayModel model = new HeePayModel();
        model.setVersion("1");
        model.setAgent_id(heePayConfig.getAgentId());
        model.setAgent_bill_id(tradeNo);
        model.setAgent_bill_time(sdf.format(new Date()));
        model.setRemark(tradeNo);
        model.setReturn_mode("1");
        String queryUrl = HeePayUtils.buildQueryUrl(heePayConfig.getSignKey(), model);

        CloseableHttpClient httpClient = HttpConnectionUtils.getHttpClient();
        HttpGet get = new HttpGet(queryUrl);
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(get);
            String resStr = EntityUtils.toString(httpResponse.getEntity());
            logger.info("汇付支付查询接口，查询订单{},返回结果:【{}】", tradeNo, resStr);
            Map<String, String> resultMap = new HashedMap();
            if (StringUtils.isNotBlank(resStr)) {
                String[] params = resStr.split("\\|");
                for (int i = 0; i < params.length; i++) {
                    String[] param = params[i].split("=");
                    if (param.length == 2) {
                        resultMap.put(param[0], param[1]);
                    }
                }
                logger.info("汇付支付查询接口，查询订单{},结果map集:【{}】", tradeNo, JsonMapper.nonDefaultMapper().toJson(resultMap));
                //7Bao交易号
                String outTradeNo = resultMap.get("agent_bill_id");
                String payType = resultMap.get("pay_type");
                String result = resultMap.get("result");
                String amount = resultMap.get("pay_amt");
//                支付成功
                if (result.equals("1")) {
                    resultMap.get("deal_time");
                    return true;
                } else {
                    resultMap.get("pay_message");
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


}
