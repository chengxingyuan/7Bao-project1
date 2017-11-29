package com.wzitech.gamegold.common.enums;

/**
 * Created by wangmin
 * Date:2017/8/22
 * 资金明细类型
 * （1.充值。2.提现，3，采购转账,4.提现服务费，5.售得充值）
 */
public enum ZbaoFundDetailType {
    FULLMONEY(1,"充值"),
    WITHDRAWALS(2,"提现"),
    TRANSFER(3,"采购转账"),
    SERVICEDRAWALS(4,"提现服务费"),
    BUYANDFULL(5,"5173售得充值"),
    OLDFULL(6,"老资金充值7bao"),
    FREEZE(7,"冻结"),
    UNFREEZE(8,"解冻"),
    ;

    private int code;

    private String name;

    ZbaoFundDetailType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ZbaoFundDetailType getTypeByCode(int code){
        for (ZbaoFundDetailType type : ZbaoFundDetailType.values()){
            if(type.getCode() == code){
                return type;
            }
        }
        throw new IllegalArgumentException("未能找到匹配的DetailType:" + code);
    }
}
