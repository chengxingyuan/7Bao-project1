package com.wzitech.Z7Bao.frontend.dao.rdb.impl;

import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoFundDetailDBDAO;
import com.wzitech.Z7Bao.frontend.entity.ZbaoFundDetail;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/21
 */
@Repository("zbaoFundDetailDBDAO")
public class ZbaoFundDetailDBDAOImpl extends AbstractMyBatisDAO<ZbaoFundDetail,Long> implements IZbaoFundDetailDBDAO{


    @Override
    public ZbaoFundDetail selectLoginAccount(String loginAccount,String orderId,Integer type) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("loginAccount",loginAccount);
        map.put("orderId",orderId);
        map.put("type",type);
        return this.getSqlSession().selectOne(getMapperNamespace()+".selectByOrderId",map);
    }

    @Override
    public int changeRechargeStatus(String orderId,String log,boolean status) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("orderId",orderId);
        map.put("status",status);
        map.put("log",log);
        return this.getSqlSession().update(getMapperNamespace()+".changeRechargeStatus",map);
    }

    @Override
    public List<ZbaoFundDetail> selectLoginAccount(String loginAccount) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("loginAccount",loginAccount);
        return this.getSqlSession().selectList(getMapperNamespace()+".selectLoginAccount",loginAccount);
    }

    @Override
    public ZbaoFundDetail selectLoginOld(String loginAccount, Integer orderId) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("loginAccount",loginAccount);
        map.put("id",orderId);
        return this.getSqlSession().selectOne(getMapperNamespace()+".selectLoginOld",map);
    }

    @Override
    public ZbaoFundDetail selectLoginCz(String loginAccount, String czOrderId) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("loginAccount",loginAccount);
        map.put("czOrderId",czOrderId);
        return this.getSqlSession().selectOne(getMapperNamespace()+".selectcz",map);
    }

    @Override
    public ZbaoFundDetail selectLoginTx(String loginAccount, String txOrderId) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("loginAccount",loginAccount);
        map.put("txOrderId",txOrderId);
        return this.getSqlSession().selectOne(getMapperNamespace()+".selecttx",map);
    }

    @Override
    public ZbaoFundDetail selectLogincg(String loginAccount, String cgOrderId) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("loginAccount",loginAccount);
        map.put("cgOrderId",cgOrderId);
        return this.getSqlSession().selectOne(getMapperNamespace()+".selectcg",map);
    }

    @Override
    public ZbaoFundDetail selectLogindj(String loginAccount, String freezeOrderId) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("loginAccount",loginAccount);
        map.put("freezeOrderId",freezeOrderId);
        return this.getSqlSession().selectOne(getMapperNamespace()+".selectdj",map);
    }


}
