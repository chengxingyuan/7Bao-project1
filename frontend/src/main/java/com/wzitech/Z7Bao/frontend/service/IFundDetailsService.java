package com.wzitech.Z7Bao.frontend.service;

import com.wzitech.Z7Bao.frontend.service.dto.IFundDetailSRequest;
import com.wzitech.Z7Bao.frontend.service.dto.IFundDetailsReponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

/**
 * Created by 340032 on 2017/8/30.
 */
public interface IFundDetailsService {

    IFundDetailsReponse ZbaoFundDetail(@QueryParam("")IFundDetailSRequest params,
                                       @Context HttpServletRequest request,
                                       @Context HttpServletResponse response);


    IFundDetailsReponse queryDetails(@QueryParam("")IFundDetailSRequest params,
                                     @Context HttpServletRequest request,
                                     @Context HttpServletResponse response);



}
