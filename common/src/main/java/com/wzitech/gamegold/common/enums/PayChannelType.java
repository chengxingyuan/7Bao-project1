package com.wzitech.gamegold.common.enums;

/**
 * 支付渠道，威富通或汇付宝等
 * Created by guotx on 2017/9/23 12:54.
 */
public enum PayChannelType {

    swiftpass(1,"威富通"),
    heepay(2,"汇付宝");
    private String name;

    private Integer type;


    PayChannelType(Integer type,String name){
        this.name=name;
        this.type=type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public static PayChannelType getChannelByType(int type){
        for(PayChannelType channel : PayChannelType.values()){
            if(channel.getType() == type){
                return channel;
            }
        }
        throw new IllegalArgumentException("未能找到匹配的PayType:" + type);
    }
}
