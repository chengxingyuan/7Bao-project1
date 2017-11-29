package com.wzitech.Z7Bao.frontend.service;

import com.wzitech.Z7Bao.frontend.service.dto.FundRequest;
import com.wzitech.Z7Bao.frontend.service.dto.FundResponse;
import com.wzitech.chaos.framework.server.common.IServiceResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

/**
 * Created by 340082 on 2017/8/18.
 */
public interface IFundService {
    public FundResponse etchRecharge(@QueryParam("") FundRequest fundRequest, @Context HttpServletRequest request);

    public FundResponse payment(@QueryParam("") FundRequest fundRequest, @Context HttpServletRequest request);


    public FundResponse transferState(@QueryParam("") FundRequest fundRequest, @Context HttpServletRequest request);

    /**
     * 提现接口
     * @param tenPayRequest  提现所需参数
     * @return
     */
    public IServiceResponse withDraw(@QueryParam("") FundRequest tenPayRequest,@Context HttpServletRequest request);

}
