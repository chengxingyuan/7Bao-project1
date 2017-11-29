package com.wzitech.Z7Bao.frontend.service.impl;

import com.wzitech.Z7Bao.frontend.service.ILoginFor5173Service;
import com.wzitech.Z7Bao.frontend.service.dto.UserResponse;
import com.wzitech.Z7Bao.frontend.servlet.GetUserInfoServlet;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.usermgmt.IGameUserManager;
import com.wzitech.gamegold.usermgmt.business.ISevenBaoAccountManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * Created by 340082 on 2017/8/17.
 */
@Service("LoginFor5173Service")
@Path("/loginFor5173")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class LoginFor5173ServiceImpl implements ILoginFor5173Service {

    @Autowired
    private IGameUserManager gameUserManager;

    @Autowired
    private ISevenBaoAccountManager sevenBaoAccountManager;

    @Value("${7bao.send.baseServiceUrl}")
    private String baseServiceUrl;

    /**
     * 登录主站跳转接口
     *
     * @param request
     * @param response
     */
    @Path("/login")
    public void loginForMain(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        String url = gameUserManager.getRequestPasswordKey();
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录主站跳转接口
     *
     * @param request
     * @param response
     */
    @Path("/loginForOut")
    public void loginForMainOut(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        String redUrl = gameUserManager.loginOutForMain5173();
        try {
            response.sendRedirect(redUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户
     *
     * @param request
     * @param response
     */
    @Override
    @Path("/getLoginUser")
    @GET
    public UserResponse getLoginUser(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        UserResponse resResponse = new UserResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        resResponse.setResponseStatus(responseStatus);
        String loginAccount = CurrentUserContext.getUser().getLoginAccount();
        response.addHeader("loginUser", loginAccount);
        responseStatus.setMessage("获取用户成功");
        responseStatus.setCode("00");
        return resResponse;
    }

    /**
     * 登出接口
     *
     * @param request
     * @param response
     */
    @Path("/loginOut")
    public void loginOut(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Cookie cookie = new Cookie(GetUserInfoServlet.Z7BAO_AUTH_KEY, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        Cookie[] cookies = request.getCookies();
        String strcookie = null;
        if (null != cookies && cookies.length > 0) {
            for (Cookie acookie : cookies) {
                if (StringUtils.equals(acookie.getName(), GetUserInfoServlet.Z7BAO_AUTH_KEY)) {
                    strcookie = acookie.getValue();
                    break;
                }
            }
        }
        sevenBaoAccountManager.deleteAccountByAuth(strcookie);
        try {
            //路径修改
            response.sendRedirect(baseServiceUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
