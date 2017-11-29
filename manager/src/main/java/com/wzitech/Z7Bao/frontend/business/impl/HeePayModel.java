package com.wzitech.Z7Bao.frontend.business.impl;

/**
 * Created by guotx on 2017/9/22 14:56.
 */
public class HeePayModel {
    private String version;  //当前接口版本号
    private String pay_type;  //20
    private String agent_id;  //商户编号
    private String agent_bill_id; //商户系统内部订单号
    private String pay_amt;  //订单总金额
    private String notify_url;  //支付宝返回的商户处理页面，可为空 ，不能为null
    private String return_url;  //支付宝返回的商户显示页面，
    private String user_ip; //用户所在客户端的真是ip;
    private String agent_bill_time;  //提交数据时间；
    private String goods_name;  //商品名称；
    private String goods_num;
    private String remark;  //商户自定义，原样返回
    private String is_test; //1=测试。非测试不用传本参数，上传参数，参见MD5加密
    private String goods_note;  //支付说明
    private String return_mode;
    private String sign;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getAgent_bill_id() {
        return agent_bill_id;
    }

    public void setAgent_bill_id(String agent_bill_id) {
        this.agent_bill_id = agent_bill_id;
    }

    public String getPay_amt() {
        return pay_amt;
    }

    public void setPay_amt(String pay_amt) {
        this.pay_amt = pay_amt;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getUser_ip() {
        return user_ip == null ? user_ip : user_ip.replaceAll("\\.", "_");
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    public String getAgent_bill_time() {
        return agent_bill_time;
    }

    public void setAgent_bill_time(String agent_bill_time) {
        this.agent_bill_time = agent_bill_time;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIs_test() {
        return is_test;
    }

    public void setIs_test(String is_test) {
        this.is_test = is_test;
    }

    public String getGoods_note() {
        return goods_note;
    }

    public void setGoods_note(String goods_note) {
        this.goods_note = goods_note;
    }

    public String getReturn_mode() {
        return return_mode;
    }

    public void setReturn_mode(String return_mode) {
        this.return_mode = return_mode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
