package com.wzitech.gamegold.usermgmt.service;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.usermgmt.request.QuerySevenBaoUserBindRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

/**
 * Created by 340032 on 2017/8/17.
 */
public interface ISevenBaoUserBind {

    IServiceResponse querySevenBaoUserBind(QuerySevenBaoUserBindRequest params,@Context HttpServletRequest request,
                                           @Context HttpServletResponse response);

}
