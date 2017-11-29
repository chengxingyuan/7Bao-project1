/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 * <p>
 *	模	块：		UserInfoEO
 *	包	名：		com.wzitech.gamegold.usermgmt.entity
 *	项目名称：	gamegold-usermgmt
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午12:29:41
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.gamegold.common.entity.IUser;
import org.springframework.stereotype.Component;

/**
 * 用户信息
 * @author SunChengfei
 *
 */
@Component
public class UserInfoEO extends BaseEntity  implements IUser{
	private static final long serialVersionUID = 1L;

	/**
	 * 登录账户
	 */
	private String loginAccount;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 实际姓名
	 */
	private String realName;

	/**
	 * 性别
	 */
	private Integer sex;

	/**
	 *QQ
	 */
	private Integer qq;

	/**
	 * 	weiXin
	 */
	private String weiXin;

	/**
	 * 手机号
	 */
	private String phoneNumber;

//	/**
//	 * 权限等级
//	 */
	private Integer userType;

	/**
	 * 软删除
	 */
	private Boolean isDeleted;

	/**
	 * uid
	 */
	private String uid;


	private Boolean isQqService;



	@Override
	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getQq() {
		return qq;
	}

	public void setQq(Integer qq) {
		this.qq = qq;
	}

	public String getWeiXin() {
		return weiXin;
	}

	public void setWeiXin(String weiXin) {
		this.weiXin = weiXin;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean deleted) {
		isDeleted = deleted;
	}

	@Override
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Boolean getIsQqService() {
		return isQqService;
	}

	public void setIsQqService(Boolean QqService) {
		isQqService = QqService;
	}
}
