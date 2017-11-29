package com.wzitech.gamegold.common.servicer.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.wzitech.gamegold.common.constants.FinanceContants;
import com.wzitech.gamegold.common.servicer.IAliPayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * 支付宝支付实现
 * Created by guotx on 2017/8/17 13:35.
 */
@Service
public class AliPayServiceImpl implements IAliPayService {
    @Value("${alipay.notify.url}")
    private String aliNotifyUrl;
    @Value("${alipay.return.url}")
    private String aliReturnUrl;

    @Override
    public void buildPayUrl() {
        AlipayClient alipayClient = new DefaultAlipayClient(FinanceContants.ALIPAY_GATEWAY,
                FinanceContants.APP_ID, FinanceContants.APP_PRIVATE_KEY, FinanceContants.FORMAT,
                FinanceContants.CHARSET, FinanceContants.ALIPAY_PUBLIC_KEY, FinanceContants.SIGN_TYPE); //获得初始化的AlipayClient

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl(aliReturnUrl);
        alipayRequest.setNotifyUrl(aliNotifyUrl);//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"20150320010101001\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":88.88," +
                "    \"subject\":\"Iphone6 16G\"," +
                "    \"body\":\"Iphone6 16G\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }
}
