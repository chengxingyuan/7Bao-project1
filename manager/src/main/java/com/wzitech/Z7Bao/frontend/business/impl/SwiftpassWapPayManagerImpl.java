package com.wzitech.Z7Bao.frontend.business.impl;

import com.kb5173.framework.shared.exception.BusinessException;
import com.wzitech.Z7Bao.frontend.business.ISwiftpassConfig;
import com.wzitech.Z7Bao.frontend.business.ISwiftpassPayManager;
import com.wzitech.gamegold.common.enums.SwiftpassPayStatus;
import com.wzitech.gamegold.common.enums.ZBaoPayType;
import com.wzitech.gamegold.common.utils.MD5;
import com.wzitech.gamegold.common.utils.PayHelper;
import com.wzitech.gamegold.common.utils.SignHelper;
import com.wzitech.gamegold.common.utils.XmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 威富通充值接口封装
 * Created by guotx on 2017/8/21 13:49.
 */
@Component
public class SwiftpassWapPayManagerImpl implements ISwiftpassPayManager {

    @Autowired
    ISwiftpassConfig swiftpassConfig;
    /**
     * 日志记录器
     */
    protected final Logger logger = LoggerFactory.getLogger(SwiftpassWapPayManagerImpl.class);

    /**
     * 构建支付url
     *
     * @param outTradeNo     订单号
     * @param body           订单描述
     * @param rechargeAmount 充值金额,单位：分
     * @param payType        充值渠道
     * @return
     */
    @Override
    public String getUrl(String outTradeNo, String body, Integer rechargeAmount, ZBaoPayType payType) {
        Map<String, String> map = new HashMap<String, String>();
        //传入需要的参数

        map.put("service", payType.getTradeType());
        //商户号
        map.put("mch_id", swiftpassConfig.getMch_id());
        //订单号
        map.put("out_trade_no", outTradeNo);
        //商品描述
        map.put("body", body);
        //总金额
        map.put("total_fee", rechargeAmount.toString());
        //终端ip
        map.put("mch_create_ip", swiftpassConfig.getMch_create_ip());
        //通知地址
        map.put("notify_url", swiftpassConfig.getNotify_url());
        //设备号
        map.put("device_info", swiftpassConfig.getDevice_info());

        //随机字符串
        map.put("nonce_str", String.valueOf(new Date().getTime()));
        Map<String, String> params = SignHelper.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        //将参数按顺序拼接
        SignHelper.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        //MD5加密
        String sign = MD5.sign(preStr, "&key=" + swiftpassConfig.getKey(), "utf-8");
        map.put("sign", sign);
        String reqUrl = swiftpassConfig.getReq_url();
        logger.info("reqParams:" + XmlUtils.parseXML(map));
        String res = null;
        //发送虚拟请求
        HttpResponse response = PayHelper.send(reqUrl, map);
        if (response != null && response.getEntity() != null) {
            Map<String, String> resultMap = null;
            try {
                resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException("操作失败");
            }
            res = XmlUtils.toXml(resultMap);
            logger.info("响应结果：" + res);

            if (resultMap.containsKey("sign")) {
                if (!SignHelper.checkParam(resultMap, swiftpassConfig.getKey())) {
                    res = "验证签名不通过";
                } else {
                    if ("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))) {
                        //得到支付地址
                        res = resultMap.get("code_img_url");
                        logger.info("qr_url:" + res);
                    } else {
                        res = resultMap.get("err_msg");
                        if(StringUtils.isBlank(res)){
                            res=resultMap.get("message");
                        }
                        logger.info("构建支付链接失败，单号：" + outTradeNo + ",失败原因：" + res);
                        throw new BusinessException("构建支付链接失败，" + res);
                    }
                }
            } else {
                String message = resultMap.get("message");
                logger.info(message + ",订单号为：" + outTradeNo);
                throw new BusinessException(message);
            }
        } else {
            throw new BusinessException("操作失败");
        }

        return res;
    }

    @Override
    public SwiftpassPayStatus checkPayStatus(String outTradeNo) {
        logger.info("校验订单["+outTradeNo+"]支付状态");
        Map<String, String> map = new HashMap<String, String>();
        //传入需要的参数

        map.put("service", swiftpassConfig.getWapQuery());
        //商户号
        map.put("mch_id", swiftpassConfig.getMch_id());
        //订单号
        map.put("out_trade_no", outTradeNo);
        //随机字符串
        map.put("nonce_str", String.valueOf(new Date().getTime()));
        Map<String, String> params = SignHelper.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        //将参数按顺序拼接
        SignHelper.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        //MD5加密
        String sign = MD5.sign(preStr, "&key=" + swiftpassConfig.getKey(), "utf-8");
        map.put("sign", sign);
        String reqUrl = swiftpassConfig.getReq_url();
        logger.info("reqParams:" + XmlUtils.parseXML(map));
        String res = null;
        //发送虚拟请求
        HttpResponse response = PayHelper.send(reqUrl, map);
        if (response != null && response.getEntity() != null) {
            Map<String, String> resultMap = null;
            String status = null;
            String resultCode = null;
            try {
                resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException("操作失败");
            }
            res = XmlUtils.toXml(resultMap);
            logger.info("支付查询响应结果：" + res);

            if (resultMap.containsKey("sign")) {
                if (!SignHelper.checkParam(resultMap, swiftpassConfig.getKey())) {
                    res = "验证签名不通过";
                    logger.info("支付状态查询失败，订单号为：" + outTradeNo+res);
                    throw new BusinessException(res);
                } else {
                    status = resultMap.get("status");
                    resultCode = resultMap.get("result_code");
                    if ("0".equals(status) && "0".equals(resultCode)) {
                        String tradeState = resultMap.get("trade_state");
                        return SwiftpassPayStatus.getStatusByCode(tradeState);
                    } else {
                        String errorMsg = resultMap.get("err_msg");
                        String errorCode = resultMap.get("err_code");
                        logger.info("支付状态查询失败，订单号为：" + outTradeNo);
                        throw new BusinessException(errorMsg == null ? "支付状态查询失败" : errorMsg);
                    }
                }
            } else {
                logger.info("缺少签名参数，订单号为：" + outTradeNo);
                throw new BusinessException("缺少签名参数");
            }
        } else {
            throw new BusinessException("操作失败");
        }
    }

    @Override
    public boolean closeOrder(String outTradeNo) {
        Map<String, String> map = new HashMap<String, String>();
        //传入需要的参数

        map.put("service", swiftpassConfig.getWapClose());
        //商户号
        map.put("mch_id", swiftpassConfig.getMch_id());
        //订单号
        map.put("out_trade_no", outTradeNo);
        //随机字符串
        map.put("nonce_str", String.valueOf(new Date().getTime()));
        Map<String, String> params = SignHelper.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        //将参数按顺序拼接
        SignHelper.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        //MD5加密
        String sign = MD5.sign(preStr, "&key=" + swiftpassConfig.getKey(), "utf-8");
        map.put("sign", sign);
        String reqUrl = swiftpassConfig.getReq_url();
        logger.info("reqParams:" + XmlUtils.parseXML(map));
        String res = null;
        //发送虚拟请求
        HttpResponse response = PayHelper.send(reqUrl, map);
        if (response != null && response.getEntity() != null) {
            Map<String, String> resultMap = null;
            String status = null;
            String resultCode = null;
            try {
                resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException("操作失败");
            }
            res = XmlUtils.toXml(resultMap);
            logger.info("支付查询响应结果：" + res);

            if (resultMap.containsKey("sign")) {
                if (!SignHelper.checkParam(resultMap, swiftpassConfig.getKey())) {
                    throw new BusinessException("签名校验错误");
                } else {
                    status = resultMap.get("status");
                    resultCode = resultMap.get("result_code");
                    if ("0".equals(status)) {
                        if ("0".equals(resultCode)) {
                            logger.info("订单" + outTradeNo + "关闭成功");
                            return true;
                        } else {
                            String errMsg = resultMap.get("err_msg");
                            String errCode = resultMap.get("err_code");
                            logger.info("订单" + outTradeNo + "关闭失败,错误信息：" + errMsg);
                            return errCode.equals("ACQ.TRADE_NOT_EXIST");
//                            return false;
                        }
                    } else {
                        throw new BusinessException("操作失败");
                    }
                }
            } else {
                logger.info("缺少签名参数，订单号为：" + outTradeNo);
                throw new BusinessException("缺少签名参数");
            }
        } else {
            throw new BusinessException("操作失败");
        }
    }
}
