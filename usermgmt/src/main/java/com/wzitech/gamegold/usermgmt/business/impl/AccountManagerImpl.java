/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		AccountManagerImpl
 *	包	名：		com.wzitech.gamegold.usermgmt.business.impl
 *	项目名称：	gamegold-usermgmt
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午4:29:38
 *
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.security.Base64;
import com.wzitech.chaos.framework.server.common.security.Digests;
import com.wzitech.gamegold.usermgmt.business.IAccountManager;
import com.wzitech.gamegold.usermgmt.dao.rdb.IUserInfoDBDAO;
import com.wzitech.gamegold.usermgmt.dao.redis.IUserInfoRedisDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author SunChengfei
 *
 */
@Component("accountManagerImpl")
public class AccountManagerImpl extends AbstractBusinessObject implements IAccountManager{
	@Autowired
	IUserInfoDBDAO userInfoDBDAO;

	@Autowired
	IUserInfoRedisDAO userInfoRedisDAO;

	@Override
	@Transactional
	public UserInfoEO register(UserInfoEO userInfoEO) {
		// 先对要保存的用户密码明文进行加密
		String passwordWithCrypt = encyptPassword(userInfoEO.getPassword(),
				userInfoEO.getLoginAccount());
		userInfoEO.setPassword(passwordWithCrypt);

		// 保存用户
		userInfoEO.setIsDeleted(false);
		userInfoDBDAO.insert(userInfoEO);
		userInfoRedisDAO.saveUser(userInfoEO);

		return userInfoEO;
	}

	@Override
	@Transactional
	public void modifyPassword(UserInfoEO userInfoEO, String newPassword) {
		// 先对要保存的用户密码明文进行加密
		String passwordWithCrypt = encyptPassword(newPassword, userInfoEO.getLoginAccount());
		userInfoEO.setPassword(passwordWithCrypt);
		
		// 更新DB
		UserInfoEO modifyPdUserEO = new UserInfoEO();
		modifyPdUserEO.setId((Long)userInfoEO.getId());
		modifyPdUserEO.setPassword(passwordWithCrypt);
		userInfoDBDAO.update(modifyPdUserEO);
		// 更新redis
		userInfoRedisDAO.saveUser(userInfoEO);
	}
	
	/**
	 * 加密密码
	 * @param clearTextPwd
	 * @param userLoginName
	 * @return
	 */
	public static String encyptPassword(String clearTextPwd,
										String userLoginName) {
		return Base64.encodeBase64Binrary(Digests.sha1(clearTextPwd.getBytes(),
				userLoginName.getBytes()));
	}

}
