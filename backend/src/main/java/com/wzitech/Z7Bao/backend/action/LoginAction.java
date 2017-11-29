package com.wzitech.Z7Bao.backend.action;

import com.wzitech.Z7Bao.backend.action.extjs.AbstractAction;
import com.wzitech.Z7Bao.backend.interceptor.ExceptionToJSON;
import com.wzitech.Z7Bao.backend.interceptor.NoCheckRequired;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.usermgmt.business.IAccountManager;
import com.wzitech.gamegold.usermgmt.business.IAuthentication;
import com.wzitech.gamegold.usermgmt.business.ISessionManager;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class LoginAction extends AbstractAction {

	private boolean doLogin;

	private String loginAccount;
	
	private String password;
	
	private UserInfoEO currentUser;
	
	@Autowired
    IAccountManager accountManager;
	
	@Autowired
    IUserInfoManager userInfoManager;
	
	@Autowired
    ISessionManager sessionManager;
	
	@Autowired
    IAuthentication authentication;

	@NoCheckRequired
	public String index() {
		if(doLogin){
			try {
				// 根据账号查找用户信息
				UserInfoEO userInfo = userInfoManager.findUserByAccount(StringUtils.lowerCase(StringUtils.trim(loginAccount)));
				if (null == userInfo) {
					// 如果用户信息不存在设置对应返回用户不存在
					throw new SystemException(ResponseCodes.NotExistedUser.getCode());
				}
				if (userInfo.getIsDeleted()) {
					// 用户已经被禁用
					throw new SystemException(ResponseCodes.UserIdDisabled.getCode());
				}
				if(!authentication.authenticate(userInfo.getLoginAccount(),
						userInfo.getPassword(), password)) {
					// 如果用户登录名密码不匹配设置密码错误
					throw new SystemException(ResponseCodes.WrongPassword.getCode());
				}

				// 获取用户真实IP地址（配置在nginx）
				String ip = ServletActionContext.getRequest().getHeader("X-Real-IP");
				if (StringUtils.isBlank(ip)) {
					ip = ServletActionContext.getRequest().getRemoteAddr();
				}
				logger.info("登录ip:{},用户名：{}", ip, loginAccount);

				// 登录成功
				// 创建会话Session
				String authkey = sessionManager.createSession(userInfo.getId().toString());
				ServletActionContext.getRequest().getSession().setAttribute(ServicesContants.SERVICE_REQUEST_AUTHKEY, authkey);
				return returnSuccess();
			} catch (SystemException ex) {
				return returnError(ex);
			}
		}else{
			return "login";
		}
	}
	
	/**
	 * 退出系统
	 * @return
	 */
	@ExceptionToJSON
	public String logout() {
		ServletActionContext.getRequest().getSession().removeAttribute(ServicesContants.SERVICE_REQUEST_AUTHKEY);
		return this.returnSuccess();
	}

	public UserInfoEO getCurrentUser() {
		return currentUser;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDoLogin(boolean doLogin) {
		this.doLogin = doLogin;
	}
}
