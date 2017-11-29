package com.wzitech.gamegold.common.enums;

/**
 * Created by guotx on 2017/9/8 12:55.
 */
public enum SwiftpassPayStatus {

    /*
        SUCCESS—支付成功REFUND—转入退款
        NOTPAY—未支付CLOSED—已关闭
        PAYERROR—支付失败(其他原因，如银行返回失败)
          */
    SUCCESS("SUCCESS"),
    NOTPAY("NOTPAY"),
    REFUND("REFUND"),
    CLOSED("CLOSED"),
    PAYERROR("PAYERROR"),;

    SwiftpassPayStatus(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static SwiftpassPayStatus getStatusByCode(String code){
        for(SwiftpassPayStatus status : SwiftpassPayStatus.values()){
            if(status.getCode().equals(code)){
                return status;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的PayStatus:" + code);
    }
}
