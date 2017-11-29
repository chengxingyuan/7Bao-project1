package com.wzitech.Z7Bao.frontend.service;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.usermgmt.request.QuerySevenBaoUserBindRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

/**
 * Created by 340032 on 2017/8/22.
 */
public interface ISevenBaoUserBind {
    /**
     * 绑定 解绑
     * @param params
     * @param request
     * @return
     */
    IServiceResponse querySevenBaoUserBind(QuerySevenBaoUserBindRequest params, @Context HttpServletRequest request
                                           );

    /**
     * 老用户初始化生成
     * @param params
     * @param request
     * @param response
     * @return
     */
    IServiceResponse querySevenBaoUser(QuerySevenBaoUserBindRequest params,@Context HttpServletRequest request);
}
