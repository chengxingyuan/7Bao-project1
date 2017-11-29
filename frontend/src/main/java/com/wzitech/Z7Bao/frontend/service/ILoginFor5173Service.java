package com.wzitech.Z7Bao.frontend.service;

import com.wzitech.Z7Bao.frontend.service.dto.UserResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

/**
 * Created by 340082 on 2017/8/18.
 */
public interface ILoginFor5173Service {
    public void loginForMain(@Context HttpServletRequest request, @Context HttpServletResponse response);
    public UserResponse getLoginUser(@Context HttpServletRequest request, @Context HttpServletResponse response);
}
