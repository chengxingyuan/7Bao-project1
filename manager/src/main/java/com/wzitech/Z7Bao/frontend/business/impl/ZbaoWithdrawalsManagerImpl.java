package com.wzitech.Z7Bao.frontend.business.impl;

import com.wzitech.Z7Bao.frontend.business.IZbaoWithdrawalsManager;
import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoWithdrawalsDBDAO;
import com.wzitech.Z7Bao.frontend.entity.ZbaoWithdrawals;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/28
 */
@Component
public class ZbaoWithdrawalsManagerImpl implements IZbaoWithdrawalsManager {
    @Autowired
    IZbaoWithdrawalsDBDAO zbaoWithdrawalsDBDAO;
    @Override
    public GenericPage<ZbaoWithdrawals> queryPage(Map<String, Object> paramMap, int limit, int startIndex, String orderBy, Boolean isAsc) {
        return zbaoWithdrawalsDBDAO.selectByMap(paramMap,limit,startIndex,orderBy,isAsc);
    }

    @Override
    public ZbaoWithdrawals querylogin(String loginAccount,String orderId) {
        return zbaoWithdrawalsDBDAO.selectLoginAccount(loginAccount,orderId);
    }

    @Override
    public ZbaoWithdrawals queryloginAccount(String loginAccount) {
        return zbaoWithdrawalsDBDAO.selectLoginAccount(loginAccount);
    }

    @Override
    public ZbaoWithdrawals queryTx(String orderId) {
        return zbaoWithdrawalsDBDAO.selectTx(orderId);
    }

    @Override
    public List<ZbaoWithdrawals> queryAllForExport(Map<String, Object> queryParam) {
        return zbaoWithdrawalsDBDAO.selectByMap(queryParam);
    }
}
