/************************************************************************************
 * Copyright 2013 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		UserInfoRedisDAOImpl
 * 包	名：		com.wzitech.gamegold.usermgmt.dao.redis.impl
 * 项目名称：	gamegold-usermgmt
 * 作	者：		SunChengfei
 * 创建时间：	2014-2-15
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014-2-15 下午1:42:07
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.dao.redis.impl;

import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.usermgmt.dao.redis.IUserInfoRedisDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author SunChengfei
 */
@Repository
public class UserInfoRedisDAOImpl extends AbstractRedisDAO<UserInfoEO> implements IUserInfoRedisDAO {
    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    ;

    @Override
    public UserInfoEO findUserByUid(String uid) {
        if (StringUtils.isEmpty(uid)) {
            return null;
        }
        // 从redis中获取对应uid的UserInfo
        BoundHashOperations<String, String, String> userOps = template
                .boundHashOps(RedisKeyHelper.uid(uid));

        return userOps.entries().isEmpty() != true ? mapper.fromHash(userOps.entries()) : null;
    }

    @Override
    public UserInfoEO findUserByLoginAccount(String loginAccount) {
        // 先获取loginAccount对应的Uid
        String uid = findUidByLoginAccount(loginAccount);
        if (StringUtils.isEmpty(uid)) {
            return null;
        }

        // 从Redis中获取对应uid的UserInfo
        return this.findUserByUid(uid);
    }

    @Override
    public void saveUser(UserInfoEO userInfo) {
        if (userInfo == null) {
            return;
        }
        // 将userInfo保存至Redis
        BoundHashOperations<String, String, String> userOps = template
                .boundHashOps(RedisKeyHelper.uid(userInfo.getId().toString()));

        if (StringUtils.isNotBlank(userInfo.getId().toString())) {
            userOps.put("id", userInfo.getId().toString());
            // 设置帐号名对应的uid
            valueOps.set(RedisKeyHelper.account(StringUtils.lowerCase(userInfo.getLoginAccount())),
                    userInfo.getId().toString());
        }

//        if (userInfo.getUserType() != null) {
//            userOps.put("userType", userInfo.getUserType().toString());
//        }
        if (StringUtils.isNotEmpty(userInfo.getLoginAccount())) {
            userOps.put("loginAccount", userInfo.getLoginAccount());
        }
        if (StringUtils.isNotEmpty(userInfo.getPassword())) {
            userOps.put("password", userInfo.getPassword());
        }
        if (StringUtils.isNotEmpty(userInfo.getRealName())) {
            userOps.put("realName", userInfo.getRealName());
        }
        if (userInfo.getSex() != null) {
            userOps.put("sex", userInfo.getSex().toString());
        }
        if (userInfo.getQq() != null) {
            userOps.put("qq", userInfo.getQq().toString());
        }
        if (StringUtils.isNotEmpty(userInfo.getPhoneNumber())) {
            userOps.put("phoneNumber", userInfo.getPhoneNumber());
        }
        if (userInfo.getIsDeleted() != null) {
            userOps.put("isDeleted", userInfo.getIsDeleted().toString());
        }
    }

    @Override
    public void setUserAuthkey(String uid, String authkey) {
        // 删除原先的authkey
        String origAuthkey = valueOps.get(RedisKeyHelper.auth(uid));
        if (StringUtils.isNotEmpty(origAuthkey)) {
            template.delete(RedisKeyHelper.authkey(origAuthkey));
        }

        // 创建authkey
        valueOps.set(RedisKeyHelper.auth(uid), authkey);
        valueOps.set(RedisKeyHelper.authkey(authkey), uid.toString());
    }

    @Override
    public void setUserAuthkey(String uid, String authkey, int timeout) {
        // 删除原先的authkey
        String origAuthkey = valueOps.get(RedisKeyHelper.auth(uid));
        if (StringUtils.isNotEmpty(origAuthkey)) {
            template.delete(RedisKeyHelper.authkey(origAuthkey));
        }

        // 创建authkey
        valueOps.set(RedisKeyHelper.auth(uid), authkey);
        valueOps.set(RedisKeyHelper.authkey(authkey), uid.toString());

        template.expire(RedisKeyHelper.auth(uid), timeout, TimeUnit.SECONDS);
        template.expire(RedisKeyHelper.authkey(authkey), timeout, TimeUnit.SECONDS);
    }

    /*
     * (non-Javadoc)
     * @see com.wzitech.chinabeauty.usermgmt.dao.redis.IUserInfoRedisDAO#removeUserAuthkey(java.lang.String)
     */
    @Override
    public void removeUserAuthkey(String uid) {
        String authkey = valueOps.get(RedisKeyHelper.auth(uid));

        template.delete(RedisKeyHelper.auth(uid));
        template.delete(RedisKeyHelper.authkey(authkey));
    }

    /*
     * (non-Javadoc)
     * @see com.wzitech.chinabeauty.usermgmt.dao.redis.IUserInfoRedisDAO#getUidByAuthkey(java.lang.String)
     */
    @Override
    public String getUidByAuthkey(String authkey) {
        return valueOps.get(RedisKeyHelper.authkey(authkey));
    }

    /**
     * 根据帐号名查找uid
     *
     * @param loginAccount
     * @return
     */
    private String findUidByLoginAccount(String loginAccount) {
        return valueOps.get(RedisKeyHelper.account(StringUtils.lowerCase(loginAccount)));
    }

    /*
     * (non-Javadoc)
     * @see com.wzitech.chinabeauty.usermgmt.dao.redis.IUserInfoRedisDAO#deleteUser(java.lang.String)
     */
    @Override
    public void deleteUser(String uid) {
        // 删除用户信息
        this.template.delete(RedisKeyHelper.uid(uid));

        // 删除用户session
        this.removeUserAuthkey(uid);
    }


}
