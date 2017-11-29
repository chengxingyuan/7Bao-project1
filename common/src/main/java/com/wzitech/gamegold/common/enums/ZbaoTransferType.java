package com.wzitech.gamegold.common.enums;

/**
 * Created by 340082 on 2017/8/21.
 */
public enum ZbaoTransferType {

    TransferWait(1,"转账中"),
    TransferSuccess(2,"转账成功"),
    TransferFailed(3,"转账失败");

    private int code;

    private String name;

    ZbaoTransferType(int code, String name){
        this.code = code;
        this.name = name;
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
    public static ZbaoTransferType getTypeByCode(int code){
        for(ZbaoTransferType type : ZbaoTransferType.values()){
            if(type.getCode() == code){
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的PayType:" + code);
    }

}
