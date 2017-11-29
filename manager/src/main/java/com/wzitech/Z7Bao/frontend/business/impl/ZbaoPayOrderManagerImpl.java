package com.wzitech.Z7Bao.frontend.business.impl;

import com.wzitech.Z7Bao.frontend.business.IZbaoPayOrderManager;
import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoPayOrderDAO;
import com.wzitech.Z7Bao.frontend.entity.ZbaoPayOrder;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/25
 */
@Component
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class ZbaoPayOrderManagerImpl implements IZbaoPayOrderManager {
    @Autowired
    IZbaoPayOrderDAO zbaoPayOrderDAO;
    @Override
    public GenericPage<ZbaoPayOrder> queryPage(Map<String, Object> paramMap, int limit, int startIndex, String orderBy, Boolean isAsc) {
        return zbaoPayOrderDAO.selectByMap(paramMap,limit,startIndex,orderBy,isAsc);
    }

    @Override
    public ZbaoPayOrder querylogin(String loginAccount,String orderId) {
        return zbaoPayOrderDAO.selectLoginAccount(loginAccount,orderId);
    }

    @Override
    public ZbaoPayOrder queryOrder(String orderId) {
        return zbaoPayOrderDAO.selectOrder(orderId);
    }

    @Override
    public List<ZbaoPayOrder> queryPayOrderExport(Map<String, Object> query) {
        return zbaoPayOrderDAO.selectByMap(query);
    }

    @Override
    public ZbaoPayOrder queryloginCz(String loginAccount, String cgOrderId) {
        return zbaoPayOrderDAO.selectLoginCz(loginAccount,cgOrderId);
    }

    @Override
    public int update(ZbaoPayOrder order){
        return zbaoPayOrderDAO.update(order);
    }
}
