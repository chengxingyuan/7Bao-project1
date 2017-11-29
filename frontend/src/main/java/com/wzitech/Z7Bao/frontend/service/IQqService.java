package com.wzitech.Z7Bao.frontend.service;

import com.wzitech.Z7Bao.frontend.service.dto.FundRequest;
import com.wzitech.Z7Bao.frontend.service.dto.QqServiceReponse;
import com.wzitech.Z7Bao.frontend.service.dto.QqServiceRequest;
import com.wzitech.chaos.framework.server.common.IServiceResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

/**
 * Created by 340032 on 2017/8/29.
 */
public interface IQqService {
    IServiceResponse queryQq();
}
