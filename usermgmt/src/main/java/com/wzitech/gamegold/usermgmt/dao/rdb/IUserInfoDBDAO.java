/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IUserInfoDBDAO
 *	包	名：		com.wzitech.gamegold.usermgmt.dao.rdb
 *	项目名称：	gamegold-usermgmt
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午1:10:19
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.dao.rdb;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

import java.util.Map;

/**
 * @author SunChengfei
 *
 */
public interface IUserInfoDBDAO extends IMyBatisBaseDAO<UserInfoEO, Long>{
	/**
	 * 根据用户Id查找用户
	 * @param uid
	 * @return
	 */
	public UserInfoEO findUserById(Long  uid);
	
	/**
	 * 通过帐号名查找用户
	 * @param
	 */
	public UserInfoEO findUserByLoginAccount(String loginAccount);


	/**
	 * 分页查询用户列表信息
	 * @param queryMap (userForm,loginAccount,nickName,realName)
	 * @param limit
	 * @param start
	 * @return
	 */
	public GenericPage<UserInfoEO> findUserList(Map<String, Object> queryMap, int limit, int start);

	int countByUserMap(Map<String, Object> queryParam);

	public GenericPage<UserInfoEO> selectUserByMap(Map<String, Object> var1, int var2, int var3, String var4, boolean var5);

	UserInfoEO selectByQq();


}
