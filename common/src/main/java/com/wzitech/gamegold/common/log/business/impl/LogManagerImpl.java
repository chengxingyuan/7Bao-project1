/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		SellerManagerImpl
 *	包	名：		com.wzitech.gamegold.repository.business.impl
 *	项目名称：	gamegold-repository
 *	作	者：		HeJian
 *	创建时间：	2014-2-22
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-22 下午9:58:33
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.log.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.log.business.ILogManager;
import com.wzitech.gamegold.common.log.dao.ILogDBDAO;
import com.wzitech.gamegold.common.log.entity.LogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 日志管理实现
 * @author ztjie
 *
 */
@Component
public class LogManagerImpl extends AbstractBusinessObject implements ILogManager{
	
	@Autowired
	ILogDBDAO logDBDAO;

	@Override
	public GenericPage<LogInfo> queryLog(Map<String, Object> queryMap,
			int limit, int start, String sortBy, boolean isAsc)
			throws SystemException {
		return logDBDAO.selectByMap(queryMap, limit, start, sortBy, isAsc);
	}
}
