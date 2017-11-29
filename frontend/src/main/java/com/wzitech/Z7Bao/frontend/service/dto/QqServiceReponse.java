package com.wzitech.Z7Bao.frontend.service.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

/**
 * Created by 340032 on 2017/8/29.
 */
public class QqServiceReponse extends AbstractServiceResponse {
    /**
     * QQ
     */
    private Integer qq;
    /**
     * 是否绑定
     */
    private Boolean isQqService;

    private Boolean success;

    private Boolean isException;

    public Boolean getException() {
        return isException;
    }

    public void setException(Boolean exception) {
        isException = exception;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getQqService() {
        return isQqService;
    }

    public void setQqService(Boolean qqService) {
        isQqService = qqService;
    }

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
