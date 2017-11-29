package com.wzitech.Z7Bao.util;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Alipay {
    public String getIs_success() {
        return is_success;
    }

    public void setIs_success(String is_success) {
        this.is_success = is_success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * 调用财付通是否成功
     */
    private String is_success;
    /**
     * 财付通返回信息
     */
    private String error;
    /**
     * 银行返回状态 success 或者error
     */
    private String sign;
    /**
     * 银行返回状态 1-8
     */
    private String sign_type;

    private String outTradeNo;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Alipay() {
        super();
    }

}