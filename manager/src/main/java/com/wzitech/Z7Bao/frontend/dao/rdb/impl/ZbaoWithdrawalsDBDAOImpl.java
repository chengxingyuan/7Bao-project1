package com.wzitech.Z7Bao.frontend.dao.rdb.impl;

import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoWithdrawalsDBDAO;
import com.wzitech.Z7Bao.frontend.entity.ZbaoWithdrawals;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/21
 */
@Repository("zbaoWithdrawalsDBDAO")
public class ZbaoWithdrawalsDBDAOImpl  extends AbstractMyBatisDAO<ZbaoWithdrawals,Long> implements IZbaoWithdrawalsDBDAO{

    @Override
    public ZbaoWithdrawals selectByOrderId(String key) {
        return this.getSqlSession().selectOne(getMapperNamespace() + ".selectByOrderId", key);
    }

    @Override
    public ZbaoWithdrawals selectLoginAccount(String loginAccount,String orderId) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("loginAccount",loginAccount);
        map.put("orderId",orderId);
        return this.getSqlSession().selectOne(getMapperNamespace()+".selectLoginAccount",map);
    }

    @Override
    public ZbaoWithdrawals selectLoginAccount(String loginAccount) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("loginAccount",loginAccount);
        return this.getSqlSession().selectOne(getMapperNamespace()+".selectLogin",map);
    }

    @Override
    public ZbaoWithdrawals selectTx(String orderId) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("orderId",orderId);
        return this.getSqlSession().selectOne(getMapperNamespace()+".selectTx",map);
    }


}
