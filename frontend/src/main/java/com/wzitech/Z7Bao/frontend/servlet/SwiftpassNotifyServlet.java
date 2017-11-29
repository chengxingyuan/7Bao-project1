package com.wzitech.Z7Bao.frontend.servlet;

import com.wzitech.Z7Bao.frontend.business.IFundManager;
import com.wzitech.Z7Bao.frontend.business.impl.SwiftpassConfig;
import com.wzitech.gamegold.common.enums.ZBaoPayType;
import com.wzitech.gamegold.common.utils.SignHelper;
import com.wzitech.gamegold.common.utils.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 威富通支付成功回调处理
 * Created by guotx on 2017/8/21 14:22.
 */
public class SwiftpassNotifyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    Logger logger = LoggerFactory.getLogger(SwiftpassNotifyServlet.class);
    private SwiftpassConfig swiftpassConfig;
    private IFundManager fundManager;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.swiftpassConfig = (SwiftpassConfig) context.getBean("swiftpassConfig");
        fundManager = context.getBean(IFundManager.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String respString = "fail";
        try {
            req.setCharacterEncoding("utf-8");
            resp.setCharacterEncoding("utf-8");
            resp.setHeader("Content-type", "text/html;charset=UTF-8");
            String resString = XmlUtils.parseRequst(req);
            logger.info("通知内容：" + resString);

            if (resString != null && !"".equals(resString)) {
                Map<String, String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");
                String res = XmlUtils.toXml(map);
                logger.info("解析后：" + res);
                if (map.containsKey("sign")) {
                    if (!SignHelper.checkParam(map, swiftpassConfig.getKey())) {
                        respString = "fail";
                    } else {
                        String status = map.get("status");
                        if (status != null && "0".equals(status)) {
                            String result_code = map.get("result_code");
                            if (result_code != null && "0".equals(result_code)) {
                                //7宝交易号
                                String outTradeNo = map.get("out_trade_no");
                                //威富通交易号
                                String tradeNo = map.get("transaction_id");
//支付宝或微信交易号
                                String thirdTradeNo = map.get("out_transaction_id");
                                //充值金额，单位分
                                String totalFee = map.get("total_fee");
//交易类型，分辨渠道
                                String tradeType = map.get("trade_type");
                                ZBaoPayType payType = null;
                                if (ZBaoPayType.aliPay.getTradeType().equals(tradeType)) {
                                    payType = ZBaoPayType.aliPay;
                                } else if (ZBaoPayType.weixinPay.getTradeType().equals(tradeType)) {
                                    payType = ZBaoPayType.weixinPay;
                                }
                                BigDecimal rechargeAmount = new BigDecimal(totalFee).divide(new BigDecimal(100));
                                fundManager.rechargeComplete(outTradeNo, rechargeAmount, tradeNo);
                            }
                        }
                        respString = "success";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            respString="fail";
        }
        resp.getWriter().write(respString);
    }
}
