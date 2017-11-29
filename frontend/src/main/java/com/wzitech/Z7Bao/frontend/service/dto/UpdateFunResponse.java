package com.wzitech.Z7Bao.frontend.service.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

/**
 * Created by chengXY on 2017/8/23.
 */
public class UpdateFunResponse extends AbstractServiceResponse{
    private Boolean success;

    private Boolean exception;

    public Boolean getException() {
        return exception;
    }

    public void setException(Boolean exception) {
        this.exception = exception;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
