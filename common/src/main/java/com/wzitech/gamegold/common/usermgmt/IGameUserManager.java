/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IGameUserManager
 *	包	名：		com.wzitech.gamegold.common.usermgmt
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-20 下午4:51:20
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.usermgmt;

import com.wzitech.gamegold.common.usermgmt.entity.SRRequsestTokenResponse;

import java.util.Map;

/**
 * 5173注册用户管理
 * @author SunChengfei
 *
 */
public interface IGameUserManager {

	/**
	 *
	 */
	public SRRequsestTokenResponse getRequestAuthorization();

	public String getMainResponse(String method, Map<String, Object> param, String vers, String token) throws Exception;

	public String getRequestPasswordKey();

	public Map<String, String> getUser(String openId,String ip);

	String loginOutForMain5173();

}
