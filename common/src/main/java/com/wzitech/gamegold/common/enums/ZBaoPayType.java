package com.wzitech.gamegold.common.enums;

/**
 * Created by 340082 on 2017/8/21.
 */
public enum ZBaoPayType {
    etch(1,"5173出售售得",""),
    aliPay(2,"支付宝","pay.alipay.native"),
    weixinPay(3,"微信","pay.weixin.native"),
    oldfull(4,"老资金充值7bao","")
    ;

    private int code;

    private String name;
    //威富通渠道名称
    private String tradeType;

    ZBaoPayType(int code, String name,String tradeType){
        this.code = code;
        this.name = name;
        this.tradeType = tradeType;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 根据code值获取对应的枚举
     * @param code
     * @return
     */
    public static ZBaoPayType getTypeByCode(int code){
        for(ZBaoPayType type : ZBaoPayType.values()){
            if(type.getCode() == code){
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的PayType:" + code);
    }

    public static boolean isValidManualType(int code){
        return (code==weixinPay.getCode() || code == aliPay.getCode());
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
}
