package com.wzitech.Z7Bao.backend.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.context.CurrentIpContext;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.expection.UserNotLoginException;
import com.wzitech.gamegold.usermgmt.business.ISessionManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Component
public class AuthenticationInterceptor extends AbstractInterceptor {

	public static final String SERVICE_REQUEST_AUTHKEY = "gamegold-authkey";

	private static final long serialVersionUID = -5665511978967345874L;

	@Autowired
    ISessionManager userSessionMger;
	
	/**
	 * 性能监控与权限控件拦截器初始化方法
	 * @see
	 *
	 * @since:
	 */
	@Override
	public void init() {
	}

	/**
	 * 性能监控与权限控制拦截器主方法，实现性能监控与权限控制
	 * @see AbstractInterceptor#intercept(ActionInvocation)
	 * intercept
	 * @param invocation
	 * @return
	 * @throws Exception
	 * @since:
	 */
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		Object target = invocation.getAction();
		String methodName = invocation.getProxy().getMethod();
		Method method = ReflectionUtils.findMethod(target.getClass(), methodName);
		String authkey = null;
		if (!method.isAnnotationPresent(NoCheckRequired.class)) {
			authkey =(String)request.getSession().getAttribute(ServicesContants.SERVICE_REQUEST_AUTHKEY);
			if (StringUtils.isEmpty(authkey)) {
				throw new UserNotLoginException();
			}else{
				// 查找authkey对应用户
				UserInfoEO loginUser = userSessionMger.getUserByAuthkey(authkey);
				if (null == loginUser) {
					CurrentUserContext.clean();
					throw new UserNotLoginException();
				} else {
					// 附加用户信息至当前线程
					CurrentUserContext.setUser(loginUser);

                    // 设置IP信息
                    // 获取用户真实IP地址（配置在nginx）
                    String ip = request.getHeader("X-Real-IP");
                    if (StringUtils.isBlank(ip)) {
                        ip = request.getRemoteAddr();
                    }
                    CurrentIpContext.setIp(ip);
                }
			}
		}
		return invocation.invoke();
	}

}
