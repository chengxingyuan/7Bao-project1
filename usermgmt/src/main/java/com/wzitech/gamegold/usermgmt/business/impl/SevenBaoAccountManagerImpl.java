package com.wzitech.gamegold.usermgmt.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.usermgmt.business.ISevenBaoAccountManager;
import com.wzitech.gamegold.usermgmt.dao.rdb.ISevenBaoAccountDBDAO;
import com.wzitech.gamegold.usermgmt.dao.redis.ISevenBaoAccountRedisDAO;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by chengXY on 2017/8/10.
 */
@Component("sevenBaoAccountManagerImpl")
public class SevenBaoAccountManagerImpl extends AbstractBusinessObject implements ISevenBaoAccountManager {

    @Autowired
    ISevenBaoAccountDBDAO sevenBaoAccountDBDAO;

    @Autowired
    ISevenBaoAccountRedisDAO sevenBaoAccountRedisDAO;

    @Override
    @Transactional
    public GenericPage<SevenBaoAccountInfoEO> querySevenBaoAccount(Map<String, Object> queryMap,
                                                                   int limit, int start, String sortBy, boolean isAsc) {
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "id";
        }
        return sevenBaoAccountDBDAO.selectByMap(queryMap, limit, start, sortBy, isAsc);
    }

    @Override
    @Transactional
    public SevenBaoAccountInfoEO modifyAccount(SevenBaoAccountInfoEO sevenBaoAccountInfoEO) {
        SevenBaoAccountInfoEO accountInfoEO = sevenBaoAccountDBDAO.findAccountById(sevenBaoAccountInfoEO.getId());
        accountInfoEO.setPhoneNumber(sevenBaoAccountInfoEO.getPhoneNumber());
        accountInfoEO.setQq(sevenBaoAccountInfoEO.getQq());
        accountInfoEO.setName(sevenBaoAccountInfoEO.getName());
        accountInfoEO.setKefuqq(sevenBaoAccountInfoEO.getKefuqq());
        sevenBaoAccountDBDAO.update(accountInfoEO);
        return accountInfoEO;
    }

    @Override
    public void enableAccount(Long id) {
        SevenBaoAccountInfoEO sevenBaoAccountInfoEO = sevenBaoAccountDBDAO.findAccountById(id);
        sevenBaoAccountInfoEO.setIsDeleted(false);
        sevenBaoAccountDBDAO.update(sevenBaoAccountInfoEO);
    }

    @Override
    public void disableAccount(Long id) {
        SevenBaoAccountInfoEO sevenBaoAccountInfoEO = sevenBaoAccountDBDAO.findAccountById(id);
        sevenBaoAccountInfoEO.setIsDeleted(true);
        sevenBaoAccountDBDAO.update(sevenBaoAccountInfoEO);
    }

    @Override
    public String insertSevenBao(SevenBaoAccountInfoEO sevenBaoAccountInfoEO) {
        //生成用户uid信息
        String sevenBaoAccountId = sevenBaoAccountRedisDAO.getSevenBaoAccountId();
        sevenBaoAccountInfoEO.setZbaoLoginAccount(sevenBaoAccountId);
        logger.info("++++++++++++++++" + sevenBaoAccountInfoEO.getName());
        if (sevenBaoAccountInfoEO != null) {
            sevenBaoAccountDBDAO.insert(sevenBaoAccountInfoEO);
        }
        return sevenBaoAccountId;
    }

    @Override
    public void updateBind(SevenBaoAccountInfoEO sevenBaoAccountInfoEO) {
        sevenBaoAccountDBDAO.update(sevenBaoAccountInfoEO);

    }

    @Override
    public String saveLoginAuth(String userName) {
        List<SevenBaoAccountInfoEO> loginAccount = sevenBaoAccountDBDAO.selectByProp("uid", userName);
        if (loginAccount != null && loginAccount.size() != 0 && loginAccount.get(0) != null) {
            SevenBaoAccountInfoEO sevenBaoAccountInfoEO = loginAccount.get(0);
            if (sevenBaoAccountInfoEO.getIsDeleted() == null || sevenBaoAccountInfoEO.getIsDeleted() == false) {
                return sevenBaoAccountRedisDAO.saveUserForLoginAuth(sevenBaoAccountInfoEO);
            }
        }
        return null;
    }

    @Override
    public SevenBaoAccountInfoEO findAccountByAuth(String auth) {
        return sevenBaoAccountRedisDAO.getLoginId(auth);
    }

    @Override
    public SevenBaoAccountInfoEO queryByAccountAndUid(String account, String uid) {
        return sevenBaoAccountDBDAO.queryByAccountAndUid(account, uid);
    }

    @Override
    public SevenBaoAccountInfoEO queryDateByProp(String loginAccount) {
        return sevenBaoAccountDBDAO.selectAccount(loginAccount);

    }

    @Override
    @Transactional
    public SevenBaoAccountInfoEO queryDataByPropWithUid(String uid) {
        return sevenBaoAccountDBDAO.selectUniqueByProp("uid", uid);
    }

    @Override
    public void deleteAccountByAuth(String auth) {
        sevenBaoAccountRedisDAO.remLoginId(auth);
    }

    @Override
    public String selectBankName(String loginAccount) {
        return sevenBaoAccountDBDAO.selectBankName(loginAccount);
    }

    @Override
    public SevenBaoAccountInfoEO selectById(Long id) {
        return sevenBaoAccountDBDAO.selectById(id);
    }

}
