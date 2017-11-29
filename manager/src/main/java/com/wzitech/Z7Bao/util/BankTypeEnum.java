package com.wzitech.Z7Bao.util;

/**
 * Created by 339928 on 2017/8/21.
 */
public enum BankTypeEnum {
    ZhaoShangYinHang(1001,"招商银行"),
    GongShangYinHang(1002,"工商银行"),
    JianSheYinHang(1003,"建设银行"),
    PuFaYinHang(1004,"上海浦东发展银行"),
    NongYeYinHang(1005,"中国农业银行"),
    XingYeYinHang(1009,"兴业银行"),
    BeiJingYinHang(1032,"北京银行"),
    GuangDaYinHang(1022,"中国光大银行"),
    MinShengYinHang(1006,"中国民生银行"),
    ZhongXinYinHang(1021,"中信实业银行"),
    GuangFaYinHang(1027,"广东发展银行"),
    PingAnYinHang(1010,"平安银行"),
    ZhongGuoYinHang(1026,"中国银行"),
    JiaoTongYinHang(1020,"中国交通银行"),
    YouChuYinHang(1066,"中国邮政储蓄银行"),
    QiTaYinHang(1099,"其他银行");

    private int type;

    private String desc;

    BankTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static String getDescByType(int type) {
        for (BankTypeEnum bank : BankTypeEnum.values()) {
            if(bank.getType()==type){
                return bank.getDesc();
            }
        }
        throw new IllegalArgumentException("未能找到匹配的BankTypeEnum:" + type);
    }

    public static int getTypeByDesc(String desc){
        for (BankTypeEnum bank:BankTypeEnum.values()){
            if(bank.getDesc().equals(desc)){
                return bank.getType();
            }
        }
        throw new IllegalArgumentException("未能找到匹配的BankTypeEnum:" + desc);
    }

    public Integer getType() {
        return type;
    }


    public String getDesc() {
        return desc;
    }

}
