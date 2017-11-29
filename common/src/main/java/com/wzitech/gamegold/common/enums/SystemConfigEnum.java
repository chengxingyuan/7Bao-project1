package com.wzitech.gamegold.common.enums;

/**
 * 出货单状态
 */
public enum SystemConfigEnum {
    /**
     * 采购商余额小于该值，停止收货
     */
    BALANCE_STOP_LINE("BALANCE_STOP_LINE", "采购商余额小于该值，停止收货"),
    BALANCE_REMIND_LINE("BALANCE_REMIND_LINE","可用余额小于200元,停止收货"),
    PAY_CHANNEL("PAY_CHANNEL","支付渠道"),
    ;


    private String key;

    private String remark;

    SystemConfigEnum(String key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    /**
     * 根据key值获取对应的枚举
     * @param key
     * @return
     */
    public static SystemConfigEnum getTypeByCode(String key){
        for(SystemConfigEnum status : SystemConfigEnum.values()){
            if(status.getKey().equals(key)){
                return status;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的SystemConfigEnum:" + key);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
