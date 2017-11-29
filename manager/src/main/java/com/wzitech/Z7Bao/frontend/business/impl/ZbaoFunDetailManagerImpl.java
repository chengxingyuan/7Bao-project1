package com.wzitech.Z7Bao.frontend.business.impl;

import com.wzitech.Z7Bao.frontend.business.IZbaoFunDetailManager;
import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoFundDetailDBDAO;
import com.wzitech.Z7Bao.frontend.entity.ZbaoFundDetail;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/24
 */
@Component
public class ZbaoFunDetailManagerImpl implements IZbaoFunDetailManager {
    @Autowired
    IZbaoFundDetailDBDAO zbaoFundDetailDBDAO;
    @Override
    public void update(ZbaoFundDetail zbaoFundDetail) {
        zbaoFundDetailDBDAO.update(zbaoFundDetail);
    }

    @Override
    public GenericPage<ZbaoFundDetail> queryPage(Map<String, Object> paramMap, int limit, int start, String orderBy, Boolean isAsc) {
        return zbaoFundDetailDBDAO.selectByMap(paramMap,limit,start,orderBy,isAsc);
    }

    @Override
    public ZbaoFundDetail querylogin(String loginAccount,String orderId,Integer type) {
        return zbaoFundDetailDBDAO.selectLoginAccount(loginAccount,orderId,type);

    }

    @Override
    public List<ZbaoFundDetail> queryloginAccount(String loginAccount) {
        return zbaoFundDetailDBDAO.selectLoginAccount(loginAccount);
    }

    @Override
    public ZbaoFundDetail queryloginCz(String loginAccount, String czOrderId) {
        return zbaoFundDetailDBDAO.selectLoginCz(loginAccount,czOrderId);
    }

    @Override
    public ZbaoFundDetail querylogintx(String loginAccount, String txOrderId) {
        return zbaoFundDetailDBDAO.selectLoginTx(loginAccount,txOrderId);
    }

    @Override
    public ZbaoFundDetail querylogincg(String loginAccount, String cgOrderId) {
        return zbaoFundDetailDBDAO.selectLogincg(loginAccount,cgOrderId);
    }

    @Override
    public ZbaoFundDetail querylogindj(String loginAccount, String freezeOrderId) {
        return zbaoFundDetailDBDAO.selectLogindj(loginAccount,freezeOrderId);
    }




    @Override
    public int queryNumber(String loginAccount) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("loginAccount",loginAccount);
        return zbaoFundDetailDBDAO.countByMap(map);
    }

    @Override
    public List<ZbaoFundDetail> quertExportData(Map<String, Object> query) {
        return zbaoFundDetailDBDAO.selectByMap(query);
    }

    @Override
    public ZbaoFundDetail queryloginOld(String loginAccount, Integer orderId) {
        return zbaoFundDetailDBDAO.selectLoginOld(loginAccount,orderId);
    }


}
