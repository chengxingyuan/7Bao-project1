/************************************************************************************
 * Copyright 2013 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		AuthenticationInterceptor
 * 包	名：		com.wzitech.gamegold.facade.frontend.interceptor
 * 项目名称：	gamegold-facade-frontend
 * 作	者：		SunChengfei
 * 创建时间：	2014-1-13
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014-1-13 下午5:59:36
 ************************************************************************************/
package com.wzitech.Z7Bao.frontend.interceptor;

import com.wzitech.Z7Bao.frontend.servlet.GetUserInfoServlet;
import com.wzitech.chaos.framework.server.common.CommonServiceResponse;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.context.CurrentIpContext;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.usermgmt.IGameUserManager;
import com.wzitech.gamegold.usermgmt.business.ISevenBaoAccountManager;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author SunChengfei
 */
public class AuthenticationInterceptor extends AbstractPhaseInterceptor<Message> {
    /**
     * 日志输出
     */
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Autowired
    private IGameUserManager gameUserManager;

    @Autowired
    private ISevenBaoAccountManager sevenBaoAccountManager;

    /**
     * 对于列表中Url不做authkey检查
     */
    private List<String> ignoreAuthUrlsList;

    public AuthenticationInterceptor() {
        super(Phase.RECEIVE);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)
     */
    @Override
    public void handleMessage(Message message) throws Fault {
        // 获取当前的http请求
        HttpServletRequest request = (HttpServletRequest) message
                .get(AbstractHTTPDestination.HTTP_REQUEST);
        // 获取返回值
        HttpServletResponse response = (HttpServletResponse) message
                .getExchange().getInMessage()
                .get(AbstractHTTPDestination.HTTP_RESPONSE);

        // 获取当前请求的路径
        String path = (String) message
                .get(Message.PATH_INFO);
        logger.debug("当前拦截的URL为 {}", path);

        if (null != ignoreAuthUrlsList && !ignoreAuthUrlsList.isEmpty()) {
            // 如果当前访问路径在ignoreUrlsList中则跳过检查（注意Url为小写）
            for (String ignoreUrl : ignoreAuthUrlsList) {
                if (path.toLowerCase().contains(ignoreUrl.toLowerCase())) {
                    logger.debug("当前请求URL {}在IgnoreUrls列表中,跳过权限检查.", path);
                    CurrentUserContext.clean();
                    return;
                }
            }
        }

        // 获取位于HTTP HEAD中的cookie
//		String cookie = request.getHeader(ServicesContants.SERVICE_REQUEST_COOKIE);
        // 从Http Request中获取Cookie
        String cookie = null;
        Cookie[] cookies = request.getCookies();

        if (null != cookies && cookies.length > 0) {
            for (Cookie acookie : cookies) {
                if (StringUtils.equals(acookie.getName(), GetUserInfoServlet.Z7BAO_AUTH_KEY)) {
                    cookie = acookie.getValue();
                    break;
                }
            }
        }
        logger.debug("当前拦截请求的cookie为 {}", cookie);

        // 查找cookie对应用户
//        GameUserInfo loginUser = null;
        SevenBaoAccountInfoEO accountByAuth = null;
        try {
            //从redis数据库获取数据
            accountByAuth = sevenBaoAccountManager.findAccountByAuth(cookie);
        } catch (Exception e) {
            CurrentUserContext.clean();
            this.response(message, response, ResponseCodes.UnKnownError);
            logger.error("解析用户7aboCookie时发生异常:{}", ExceptionUtils.getStackTrace(e));
            return;
        }

        //判断该redis对象是否存在
        if (accountByAuth == null) {
            logger.debug("请求中包含的cookie {} 没有找到对应的用户信息", cookie);
            CurrentUserContext.clean();
            this.response(message, response, ResponseCodes.InvalidAuthkey);
            return;
        } else {
            // 附加用户信息至当前线程
            logger.debug("附加用户信息 {} 到当前请求User Session.", accountByAuth);
            CurrentUserContext.setUser(accountByAuth);
            // 附加IP信息至当前线程
            // 获取用户真实IP地址（配置在nginx）
            String ip = request.getHeader("X-Real-IP");
            if (StringUtils.isBlank(ip)) {
                ip = request.getRemoteAddr();
            }
            CurrentIpContext.setIp(ip);
            response.setHeader("loginAccount",accountByAuth.getLoginAccount());
        }
    }

    private void response(Message message, HttpServletResponse response, ResponseCodes responseCodes) {
        try {
            // 返回Json类型的错误信息
            IServiceResponse jsonResp = new CommonServiceResponse(
                    responseCodes.getCode(), responseCodes.getMessage());
            response.getOutputStream().write(JsonMapper.nonEmptyMapper().toJson(jsonResp).getBytes("utf-8"));
            response.getOutputStream().flush();

            // 中断此次请求
            message.getInterceptorChain().abort();
        } catch (Exception e) {
            logger.error("AuthenticationInterceptor返回响应时发生异常:{}", e);
        }
    }

    /**
     * @return the ignoreAuthUrlsList
     */
    public List<String> getIgnoreAuthUrlsList() {
        return ignoreAuthUrlsList;
    }

    /**
     * @param ignoreAuthUrlsList the ignoreAuthUrlsList to set
     */
    public void setIgnoreAuthUrlsList(List<String> ignoreAuthUrlsList) {
        this.ignoreAuthUrlsList = ignoreAuthUrlsList;
    }

}
