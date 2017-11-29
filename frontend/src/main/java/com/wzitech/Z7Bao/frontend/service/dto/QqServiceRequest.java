package com.wzitech.Z7Bao.frontend.service.dto;

/**
 * Created by 340032 on 2017/8/29.
 */
public class QqServiceRequest {
    /**
     * QQ
     */
    private Integer qq;
    /**
     * 是否绑定
     */
    private Boolean isQqService;

    public Integer getQq() {
        return qq;
    }

    public void setQq(Integer qq) {
        this.qq = qq;
    }

    public Boolean getIQqService() {
        return isQqService;
    }

    public void setIQqService(Boolean qqService) {
        isQqService = qqService;
    }

}
