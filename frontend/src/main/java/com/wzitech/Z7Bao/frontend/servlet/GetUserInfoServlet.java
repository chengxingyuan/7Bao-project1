package com.wzitech.Z7Bao.frontend.servlet;

import com.kb5173.framework.shared.exception.BusinessException;
import com.wzitech.gamegold.common.usermgmt.IGameUserManager;
import com.wzitech.gamegold.usermgmt.business.ISevenBaoAccountManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by 340082 on 2017/8/17.
 */
public class GetUserInfoServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected ApplicationContext context;
    private IGameUserManager gameUserManager;
    private ISevenBaoAccountManager sevenBaoAccountManager;

    public static final String Z7BAO_AUTH_KEY = "Z7BAO_AUTH_KEY";

    //http://192.168.42.196:8080/7Bao-frontend/getUserInfoServlet 这个添加一下
    //TODO 上线配置
    private static final String SENDURL = "https://shouhuo.7bao.com/html/login_loading.html";

    private static final String ERROEURL = "https://shouhuo.7bao.com/index.html?loginError=true";

    @Override
    public void init() throws ServletException {
        context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        gameUserManager = (IGameUserManager) context.getBean(IGameUserManager.class);
        sevenBaoAccountManager = (ISevenBaoAccountManager) context.getBean(ISevenBaoAccountManager.class);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
//            System.out.println("开始");
            logger.info("获取用户信息开始");
            PrintWriter out = resp.getWriter();
            String openid = req.getParameter("openid");
            if (null == openid) {
                throw new BusinessException("获取用户信息失败,openid为空");
            }
            logger.info("获取用户信息开始:{}", openid);
            //请求openUserInfo.get接口 通过openid获取用户信息
            String ip = req.getHeader("X-Real-IP");
            if (StringUtils.isBlank(ip)) {
                ip = req.getRemoteAddr();
            }
            Map<String, String> user = gameUserManager.getUser(openid,ip);
            if (StringUtils.isBlank(user.get("userID")) || StringUtils.isBlank(user.get("userName"))) {
                resp.sendRedirect(ERROEURL);
            }
            String authStr = sevenBaoAccountManager.saveLoginAuth(user.get("userID"));

            if (StringUtils.isBlank(authStr)) {
                resp.sendRedirect(ERROEURL);
            }
            Cookie cookie = new Cookie(Z7BAO_AUTH_KEY, authStr);
            cookie.setPath("/");
//            cookie.setMaxAge(30 * 60);
            resp.addCookie(cookie);
            resp.sendRedirect(SENDURL);
        } catch (Exception e) {
            e.printStackTrace();
//            logger.error(LogCategory.PAY, "获取用户信息失败", e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
