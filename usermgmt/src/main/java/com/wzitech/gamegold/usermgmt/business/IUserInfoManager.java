/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IUserInfoManager
 *	包	名：		com.wzitech.gamegold.usermgmt.business
 *	项目名称：	gamegold-usermgmt
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午1:36:32
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.business;

import com.wzitech.chaos.framework.server.common.exception.BusinessException;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

import java.util.List;
import java.util.Map;

/**
 * 用户信息管理
 * @author SunChengfei
 *
 */
public interface IUserInfoManager {
	/**
	 * 根据帐号查找用户信息
	 * @param loginAccount
	 * @return
	 */
	public UserInfoEO findUserByAccount(String loginAccount);
	
	/**
	 * 根据id查找用户信息
	 * @param uid
	 * @return
	 */
	public UserInfoEO findUserByUid(String uid);
	
	/**
	 * 根据uid集合返回用户信息
	 * @return
	 */
	public List<UserInfoEO> userInfoList(List<Long> uids);
	
	/**
	 * 查询用户信息
	 * @param queryMap
	 * @param limit
	 * @param start
	 * @param sortBy
	 * @param isAsc
	 * @return
	 */
	public GenericPage<UserInfoEO> queryUser(Map<String, Object> queryMap, int limit, int start,
			String sortBy, boolean isAsc);
	
	/**
	 * 修改用户信息
	 * @param userInfoEO
	 * @return
	 */
	public UserInfoEO modifyUserInfo(UserInfoEO userInfoEO) throws SystemException;
	
	/**
	 * 添加用户
	 * @throws BusinessException 
	 */
	public void addUser(UserInfoEO user) throws SystemException;
	
	/**
	 * 修改后台用户密码
	 * @param currentUser
	 * @param newPassword
	 * @return
	 */
	public UserInfoEO modifyCurrentUserPassword(UserInfoEO currentUser, String oldPassword, String newPassword) throws SystemException;

	/**
	 * 通过ID启用用户
	 * @param id
	 */
	public void enableUser(Long id) throws SystemException;

	/**
	 * 通过ID禁用用户
	 * @param id
	 */
	public void disableUser(Long id) throws SystemException;

	/**
	 * 通过ID启用QQ
	 * @param id
     */
	public void enableQQ(Long id) throws SystemException;

	/**
	 * 查启用的QQ号
	 * @return
     */
	public UserInfoEO selectByQq();

	/**
	 * 通过ID删除用户
	 * @param id
	 */
	public void deleteUser(Long id) throws SystemException;
	
	public UserInfoEO findDBUserById(String uid)throws SystemException;

	/**
	 * 修改密码
	 * @param user
	 * @return
	 */
	public UserInfoEO modifyPwd(UserInfoEO user) throws SystemException;


	public UserInfoEO selectUserById(Long id);





}
