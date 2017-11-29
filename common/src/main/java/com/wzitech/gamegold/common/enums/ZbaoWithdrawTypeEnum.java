package com.wzitech.gamegold.common.enums;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/17  wurongfan           ZW_C_JB_00021 商城资金7bao改造
 */
public enum ZbaoWithdrawTypeEnum {


    /**
     * 以下为提现具体状态信息
     */
    LOADING(1,"提现中"),
    SUCCESS(2,"提现成功"),
    FAILURE(3,"提现失败");

    private Integer statu;

    private String describe;

    ZbaoWithdrawTypeEnum(Integer statu, String describe) {
        this.statu = statu;
        this.describe = describe;
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public static ZbaoWithdrawTypeEnum getTypeByCode(int code){
        for(ZbaoWithdrawTypeEnum type : ZbaoWithdrawTypeEnum.values()){
            if(type.getStatu() == code){
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的PayType:" + code);
    }
}
