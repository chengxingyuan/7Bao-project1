/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		UserInfoDBDAOImpl
 *	包	名：		com.wzitech.gamegold.usermgmt.dao.rdb.impl
 *	项目名称：	gamegold-usermgmt
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午1:24:32
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.dao.rdb.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.usermgmt.dao.rdb.IUserInfoDBDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户信息DAO
 * @author SunChengfei
 *
 */
@Repository("userInfoDBDAOImpl")
public class UserInfoDBDAOImpl extends AbstractMyBatisDAO<UserInfoEO, Long> implements IUserInfoDBDAO{
	@Override
	public UserInfoEO findUserById(Long  uid) {
		if(uid==null){
			return null;
		}
		return selectUniqueByProp("id",uid);
	}
	
	@Override
	public UserInfoEO findUserByLoginAccount(String loginAccount) {
		if(StringUtils.isEmpty(loginAccount)){
			return null;
		}
		return selectUniqueByProp("loginAccount", loginAccount);
	}



	@Override
	public GenericPage<UserInfoEO> findUserList(Map<String, Object> queryMap, int limit, int start) {
		GenericPage<UserInfoEO> genericPage = this.selectByMap(queryMap, limit, start);
		return genericPage;
	}

	@Override
	public int countByUserMap(Map<String, Object> queryParam) {
		return ((Integer)this.getSqlSession().selectOne(this.mapperNamespace + ".countUserByMap", queryParam)).intValue();
	}

	public GenericPage<UserInfoEO> selectUserByMap(Map<String, Object> queryParam, int pageSize, int startIndex, String orderBy, boolean isAsc) {
		Validate.notBlank(orderBy);
		if(pageSize < 1) {
			throw new IllegalArgumentException("分页pageSize参数必须大于1");
		} else if(startIndex < 0) {
			throw new IllegalArgumentException("分页startIndex参数必须大于0");
		} else {
			if(null == queryParam) {
				queryParam = new HashMap();
			}

			((Map)queryParam).put("ORDERBY", orderBy);
			if(isAsc) {
				((Map)queryParam).put("ORDER", "ASC");
			} else {
				((Map)queryParam).put("ORDER", "DESC");
			}

			int totalSize = this.countByMap((Map)queryParam);
			if(totalSize == 0) {
				return new GenericPage((long)startIndex, (long)totalSize, (long)pageSize, (List)null);
			} else {
				List pagedData = this.getSqlSession().selectList(this.mapperNamespace + ".selectUserByMap", queryParam, new RowBounds(startIndex, pageSize));
				return new GenericPage((long)startIndex, (long)totalSize, (long)pageSize, pagedData);
			}
		}
	}

	@Override
	public UserInfoEO selectByQq() {
		return getSqlSession().selectOne(getMapperNamespace() + ".selectByQq");
	}


}
