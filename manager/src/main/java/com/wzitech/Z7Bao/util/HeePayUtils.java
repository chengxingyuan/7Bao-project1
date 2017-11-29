package com.wzitech.Z7Bao.util;

import com.wzitech.Z7Bao.frontend.business.impl.HeePayModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;

/**
 * 汇付宝支付工具类
 * Created by guotx on 2017/9/22 14:58.
 */
public class HeePayUtils {
    public static String SignMD5(HeePayModel model, String key) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("version=").append(model.getVersion())
                .append("&agent_id=").append(model.getAgent_id())
                .append("&agent_bill_id=").append(model.getAgent_bill_id())
                .append("&agent_bill_time=").append(model.getAgent_bill_time())
                .append("&pay_type=").append(model.getPay_type())
                .append("&pay_amt=").append(model.getPay_amt())
                .append("&notify_url=").append(model.getNotify_url())
                .append("&return_url=").append(model.getReturn_url())
                .append("&user_ip=").append(model.getUser_ip())
                .append("&key=" + key);
        return Md5(stringBuilder.toString(), "UTF-8").toLowerCase();
    }

    public static String GateWaySubmitUrl(String sign, HeePayModel model) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append("https://pay.heepay.com/Payment/Index.aspx?");
        sbUrl.append("version=" + model.getVersion());
        sbUrl.append("&agent_id=" + model.getAgent_id());
        sbUrl.append("&agent_bill_id=" + model.getAgent_bill_id());
        sbUrl.append("&agent_bill_time=" + model.getAgent_bill_time());
        sbUrl.append("&pay_type=" + model.getPay_type());
        sbUrl.append("&pay_amt=" + model.getPay_amt());
        sbUrl.append("&notify_url=" + model.getNotify_url());
        sbUrl.append("&return_url=" + model.getReturn_url());
        sbUrl.append("&user_ip=" + model.getUser_ip());

        sbUrl.append("&goods_name=" + URLEncoder.encode(model.getGoods_name(), "GB2312"));
        sbUrl.append("&goods_num=" + model.getGoods_num());
        sbUrl.append("&goods_note=" + URLEncoder.encode(model.getGoods_note(), "GB2312"));
        sbUrl.append("&remark=" + URLEncoder.encode(model.getRemark(), "GB2312"));
        sbUrl.append("&sign=" + sign);
        return sbUrl.toString();
    }

    public static boolean VerifyNotitySign(String key, Map<String, String> params) {
        StringBuilder sbSign = new StringBuilder();
        sbSign.append("result=" + params.get("result"));
        sbSign.append("&agent_id=" + params.get("agent_id"));
        sbSign.append("&jnet_bill_no=" + params.get("jnet_bill_no"));
        sbSign.append("&agent_bill_id=" + params.get("agent_bill_id"));
        sbSign.append("&pay_type=" + params.get("pay_type"));
        sbSign.append("&pay_amt=" + params.get("pay_amt"));
        sbSign.append("&remark=" + params.get("remark"));
        sbSign.append("&key=" + key);
        return Md5(sbSign.toString(), null).toLowerCase().equals(params.get("sign"));
    }

    private static String Md5(String s, String encode) {
        if (encode == null || "".equals(encode)) {
            encode = "UTF-8";
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] byInput = s.getBytes(encode);
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(byInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(Md5("123456", "UTF-8"));
    }

    public static String buildQueryUrl(String signKey, HeePayModel model) {
        StringBuilder build = new StringBuilder();
        build.append("version=").append(model.getVersion())
                .append("&agent_id=").append(model.getAgent_id())
                .append("&agent_bill_id=").append(model.getAgent_bill_id())
                .append("&agent_bill_time=").append(model.getAgent_bill_time())
                .append("&return_mode=").append(model.getReturn_mode())
                .append("&key=").append(signKey);
        String sign = Md5(build.toString(), "UTF-8").toLowerCase();
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append("https://query.heepay.com/Payment/Query.aspx?");
        sbUrl.append("version=").append(model.getVersion());
        sbUrl.append("&agent_id=").append(model.getAgent_id());
        sbUrl.append("&agent_bill_id=").append(model.getAgent_bill_id());
        sbUrl.append("&agent_bill_time=").append(model.getAgent_bill_time());
        sbUrl.append("&remark=").append(model.getRemark())
                .append("&return_mode=").append(model.getReturn_mode())
                .append("&sign=").append(sign);
        return sbUrl.toString();
    }
}
