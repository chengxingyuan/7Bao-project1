package com.wzitech.Z7Bao.frontend.servlet;

import com.wzitech.Z7Bao.frontend.business.IFundManager;
import com.wzitech.Z7Bao.frontend.business.impl.HeePayConfig;
import com.wzitech.Z7Bao.util.HeePayUtils;
import com.wzitech.Z7Bao.util.HttpClientUtil;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.enums.ZBaoPayType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 汇付宝支付成功后的回调
 * Created by guotx on 2017/9/22 19:18.
 */
public class HeePayNotifyServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(HeePayNotifyServlet.class);
    private HeePayConfig heePayConfig;
    private IFundManager fundManager;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        heePayConfig = context.getBean(HeePayConfig.class);
        fundManager = context.getBean(IFundManager.class);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String outTradeNo = null;
        String tradeNo = null;
        try {
            String queryString = req.getQueryString();
            logger.info("queryString:{}",queryString);
            Map<String, String> params = HttpClientUtil.queryStrToMap(queryString, "gb2312");
            logger.info(String.format("汇付回调交易parameterMap为：%s", JsonMapper.nonDefaultMapper().toJson(params)));
            //7Bao交易号
            outTradeNo = params.get("agent_bill_id");
            //汇付交易号
            tradeNo = params.get("jnet_bill_no");
            String remark = params.get("remark");
            String amount = params.get("pay_amt");
            if (HeePayUtils.VerifyNotitySign(heePayConfig.getSignKey(), params)) {
                if (params.get("result").equals("1")) {
                    logger.info(String.format("汇付宝签名验证成功，result:%s", params.get("result")));
                    String tradeType = params.get("pay_type");
                    ZBaoPayType payType = null;
                    if (ZBaoPayType.aliPay.getTradeType().equals(tradeType)) {
                        payType = ZBaoPayType.aliPay;
                    } else if (ZBaoPayType.weixinPay.getTradeType().equals(tradeType)) {
                        payType = ZBaoPayType.weixinPay;
                    }
                    BigDecimal rechargeAmount = new BigDecimal(amount);
                    fundManager.rechargeComplete(outTradeNo, rechargeAmount, tradeNo);
                    out.print("ok");//请不要修改或删除
                } else {
                    logger.info("支付失败");
                    out.print("error");//请不要修改或删除
                }
            } else {
                logger.info("汇付宝支付验签失败：{}", outTradeNo);
            }

        } catch (Exception e) {
            logger.error("支付宝支付通知处理异常，订单号:{}，交易号：{}", outTradeNo, tradeNo);
            logger.error("支付宝支付通知处理异常", e);
            out.print("error");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
