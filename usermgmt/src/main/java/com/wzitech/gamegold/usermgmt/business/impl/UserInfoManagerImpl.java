/************************************************************************************
 * Copyright 2014 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		UserInfoManagerImpl
 * 包	名：		com.wzitech.gamegold.usermgmt.business.impl
 * 项目名称：	gamegold-usermgmt
 * 作	者：		SunChengfei
 * 创建时间：	2014-2-15
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014-2-15 下午1:38:03
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.security.Base64;
import com.wzitech.chaos.framework.server.common.security.Digests;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentIpContext;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.dao.rdb.IUserInfoDBDAO;
import com.wzitech.gamegold.usermgmt.dao.rdb.impl.QqRedisDAOImpl;
import com.wzitech.gamegold.usermgmt.dao.redis.IUserInfoRedisDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author SunChengfei
 *
 */
@Component("userInfoManagerImpl")
public class UserInfoManagerImpl extends AbstractBusinessObject implements IUserInfoManager {
    @Autowired
    IUserInfoDBDAO userInfoDBDAO;

    @Autowired
    IUserInfoRedisDAO userInfoRedisDAO;
    @Autowired
    UserInfoEO userInfoEO;

    @Autowired
    QqRedisDAOImpl qqRedisDAO;
    @Override
    public UserInfoEO findUserByAccount(String loginAccount) {
        // 先从Redis中查询用户信息
        UserInfoEO userInfo = userInfoRedisDAO
                .findUserByLoginAccount(loginAccount);
        // 如Redis中为找到对应用户信息，则尝试去DB中查找
        if (null == userInfo) {
            logger.debug("在redis中未找到{}相关信息，尝试在数据库中查找。", loginAccount);
            userInfo = userInfoDBDAO.findUserByLoginAccount(loginAccount);

            // 如过在DB中找到对应信息返回，并将信息存入Redis中
            if (null != userInfo) {
                logger.debug("在数据库中查找到{}相关信息，重新讲该信息存储到Redis。", loginAccount);
                userInfoRedisDAO.saveUser(userInfo);
            }
        } else {
            logger.debug("成功的在redis中找到{}相关信息。", loginAccount);
        }
        return userInfo;
    }

    @Override
    public UserInfoEO findUserByUid(String uid) {
        // 先从Redis中查询用户信息
        UserInfoEO userInfo = userInfoRedisDAO.findUserByUid(uid);

        // 如Redis中为找到对应用户信息，则尝试去DB中查找
        if (null == userInfo) {
            logger.debug("在redis中未找到{}相关信息，尝试在数据库中查找。", uid);
            userInfo = userInfoDBDAO.findUserById(Long.valueOf(uid));

            // 如过在DB中找到对应信息返回，并将信息存入Redis中
            if (null != userInfo) {
                logger.debug("在数据库中查找到{}相关信息，重新讲该信息存储到Redis。", uid);
                userInfoRedisDAO.saveUser(userInfo);
            }
        } else {
            logger.debug("成功的在redis中找到{}相关信息。", uid);
        }

        return userInfo;
    }

    @Override
    public UserInfoEO findDBUserById(String uid) {
        UserInfoEO userInfo = userInfoDBDAO.findUserById(Long.valueOf(uid));
        return userInfo;
    }

    @Override
    @Transactional
    public UserInfoEO modifyUserInfo(UserInfoEO user) throws SystemException {
        if (user == null) {
            throw new SystemException(ResponseCodes.UserEmptyWrong.getCode());
        }
        UserInfoEO dbUser = userInfoDBDAO.findUserById((Long) (user.getId()));
        if (StringUtils.isBlank(user.getRealName())) {
            throw new SystemException(ResponseCodes.RealNameEmpty.getCode());
        }
        dbUser.setRealName(user.getRealName().trim());
        if (StringUtils.isNotBlank(user.getPhoneNumber().toString())) {
            dbUser.setPhoneNumber(user.getPhoneNumber());
        }
        if (StringUtils.isNotBlank(user.getQq().toString())) {
            dbUser.setQq(user.getQq());
        }
        if (StringUtils.isNotBlank(user.getWeiXin())) {
            dbUser.setWeiXin(user.getWeiXin());
        }
        dbUser.setSex(user.getSex());
        userInfoDBDAO.update(dbUser);
        // 更新redis
        userInfoRedisDAO.saveUser(dbUser);

        logger.info("修改用户：{},操作员:{},IP:{}", new Object[]{dbUser.getLoginAccount(),
                CurrentUserContext.getUserLoginAccount(), CurrentIpContext.getIp()});
        return dbUser;
    }

    @Override
    @Transactional
    public UserInfoEO modifyPwd(UserInfoEO user) throws SystemException {
        if (user == null) {
            throw new SystemException(ResponseCodes.UserEmptyWrong.getCode());
        }
        UserInfoEO dbUser = userInfoDBDAO.findUserById((Long) (user.getId()));

        String passwordWithCrypt = Base64.encodeBase64Binrary(
                Digests.sha1(user.getPassword().trim().getBytes(), user.getLoginAccount().getBytes()));
        dbUser.setPassword(passwordWithCrypt);
        userInfoDBDAO.update(dbUser);
        // 更新redis
        userInfoRedisDAO.saveUser(dbUser);

        logger.info("修改用户密码：%s,操作员:%s,IP:%s", new Object[]{dbUser.getLoginAccount(),
                CurrentUserContext.getUserLoginAccount(), CurrentIpContext.getIp()});

        return dbUser;
    }

    @Override
    public GenericPage<UserInfoEO> queryUser(Map<String, Object> queryMap,
                                             int limit, int start, String sortBy, boolean isAsc) {
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "ID";
        }
        return userInfoDBDAO.selectByMap(queryMap, limit, start, sortBy, isAsc);
    }

    @Override
    @Transactional
    public void addUser(UserInfoEO user) throws SystemException {
        if (user == null) {
            throw new SystemException(ResponseCodes.UserEmptyWrong.getCode());
        }
        if (StringUtils.isBlank(user.getLoginAccount())) {
            throw new SystemException(ResponseCodes.AccountEmptyWrong.getCode());
        }
        user.setLoginAccount(StringUtils.lowerCase(StringUtils.trim(user.getLoginAccount())));
        if (userInfoDBDAO.findUserByLoginAccount(user.getLoginAccount()) != null) {
            throw new SystemException(ResponseCodes.ExitedAccount.getCode());
        }
        if (StringUtils.isBlank(user.getRealName())) {
            throw new SystemException(ResponseCodes.RealNameEmpty.getCode());
        }
        user.setRealName(user.getRealName().trim());
        if (StringUtils.isBlank(user.getPassword().trim())) {
            throw new SystemException(ResponseCodes.UserPasswordEmpty.getCode());
        }
        String passwordWithCrypt = Base64.encodeBase64Binrary(
                Digests.sha1(user.getPassword().trim().getBytes(), user.getLoginAccount().getBytes()));
        user.setPassword(passwordWithCrypt);
        user.setIsDeleted(false);
        user.setIsQqService(true);
        userInfoDBDAO.insert(user);

    }

    @Override
    public List<UserInfoEO> userInfoList(List<Long> uids) {
        return userInfoDBDAO.selectByIds(uids);
    }

    @Override
    @Transactional
    public UserInfoEO modifyCurrentUserPassword(UserInfoEO currentUser, String oldPassword, String newPassword) throws SystemException {

        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            throw new SystemException(ResponseCodes.PasswordEmptyWrong.getCode());
        }

        UserInfoEO dbUser = userInfoDBDAO.findUserById((Long) (currentUser.getId()));

        oldPassword = oldPassword.trim();
        String oldPasswordWithCrypt = Base64.encodeBase64Binrary(
                Digests.sha1(oldPassword.getBytes(), dbUser.getLoginAccount().getBytes()));
        if (!StringUtils.equals(oldPasswordWithCrypt, dbUser.getPassword())) {
            throw new SystemException(ResponseCodes.OldPasswordWrong.getCode());
        }

        String passwordWithCrypt = Base64.encodeBase64Binrary(
                Digests.sha1(newPassword.getBytes(), dbUser.getLoginAccount().getBytes()));
        dbUser.setPassword(passwordWithCrypt);
        // 更新DB
        userInfoDBDAO.update(dbUser);

        // 更新redis
        userInfoRedisDAO.saveUser(dbUser);
        return dbUser;
    }

    @Override
    @Transactional
    public void enableUser(Long id) throws SystemException {
        if (id == null) {
            throw new SystemException(ResponseCodes.UserIdEmptyWrong.getCode());
        }
        UserInfoEO dbUser = userInfoDBDAO.findUserById(id);
        if (dbUser == null) {
            throw new SystemException(ResponseCodes.UserNotExitedWrong.getCode());
        }
        if (!dbUser.getIsDeleted()) {
            throw new SystemException(ResponseCodes.UserIsEnableWrong.getCode());
        }
        dbUser.setIsDeleted(false);
        userInfoDBDAO.update(dbUser);
        // 更新redis
        userInfoRedisDAO.saveUser(dbUser);
    }

    @Override
    @Transactional
    public void disableUser(Long id) throws SystemException {
        if (id == null) {
            throw new SystemException(ResponseCodes.UserIdEmptyWrong.getCode());
        }
        UserInfoEO dbUser = userInfoDBDAO.findUserById(id);
        if (dbUser == null) {
            throw new SystemException(ResponseCodes.UserNotExitedWrong.getCode());
        }
        if (dbUser.getIsDeleted()) {
            throw new SystemException(ResponseCodes.UserIsDisableWrong.getCode());
        }
        dbUser.setIsDeleted(true);
        userInfoDBDAO.update(dbUser);
        // 更新redis
        userInfoRedisDAO.saveUser(dbUser);

        logger.info("禁用用户：{},操作员:{},IP:{}", new Object[]{dbUser.getLoginAccount(),
                CurrentUserContext.getUserLoginAccount(), CurrentIpContext.getIp()});
    }

    /**
     * 启用QQ
     * @param id
     */
    @Override
    @Transactional
    public void enableQQ(Long id) {
        if (id == null) {
            throw new SystemException(ResponseCodes.UserIdEmptyWrong.getCode());
        }
        UserInfoEO userInfoEO = userInfoDBDAO.selectByQq();
        if (userInfoEO != null) {
            userInfoEO.setIsQqService(true);
            userInfoDBDAO.update(userInfoEO);
        }
        UserInfoEO userInfoEO1 = userInfoDBDAO.findUserById(id);
        if (userInfoEO1 != null) {
            if (userInfoEO1.getQq()==null){
                throw new SystemException(ResponseCodes.EmptyServiceQq.getCode());
            }
            userInfoEO1.setIsQqService(false);
            qqRedisDAO.save(String.valueOf(userInfoEO1.getQq()));
            userInfoDBDAO.update(userInfoEO1);
        }
    }

    @Override
    public UserInfoEO selectByQq() {
        String qq = qqRedisDAO.getQq();
       if (StringUtils.isNotBlank(qq)){
           UserInfoEO userInfoEO = new UserInfoEO();
           userInfoEO.setQq(Integer.parseInt(qq));
           return userInfoEO;
       }else{
           return userInfoDBDAO.selectByQq();
       }
    }


    @Override
    @Transactional
    public void deleteUser(Long id) throws SystemException {
        if (id == null) {
            throw new SystemException(ResponseCodes.UserIdEmptyWrong.getCode());
        }
        UserInfoEO dbUser = userInfoDBDAO.findUserById(id);
        if (dbUser == null) {
            throw new SystemException(ResponseCodes.UserNotExitedWrong.getCode());
        }

        userInfoDBDAO.deleteById(id);
        // 更新redis
        userInfoRedisDAO.deleteUser(String.valueOf(id));

        logger.info("删除用户：{},操作员:{},IP:{}", new Object[]{dbUser.getLoginAccount(),
                CurrentUserContext.getUserLoginAccount(), CurrentIpContext.getIp()});
    }

    public UserInfoEO selectUserById(Long id) {
        return userInfoDBDAO.findUserById(id);
    }

}
