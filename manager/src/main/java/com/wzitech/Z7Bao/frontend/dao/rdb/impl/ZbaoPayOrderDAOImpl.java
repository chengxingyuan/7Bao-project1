package com.wzitech.Z7Bao.frontend.dao.rdb.impl;

import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoPayOrderDAO;
import com.wzitech.Z7Bao.frontend.entity.ZbaoPayOrder;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 340032 on 2017/8/14.
 */
@Repository("ZbaoPayOrderDAOImpl")
public class ZbaoPayOrderDAOImpl extends AbstractMyBatisDAO<ZbaoPayOrder,Long> implements IZbaoPayOrderDAO {

    public GenericPage<ZbaoPayOrder> findOrderList(Map<String, Object> queryMap, int limit, int start) {
        GenericPage<ZbaoPayOrder> genericPage = this.selectByMap(queryMap, limit, start);
        return genericPage;
    }

    /**
     * 根据order查询唯一结果
     * @param orderId
     * @param isUpdate 是否加锁
     * @return
     */
    @Override
    public ZbaoPayOrder selectByOrderIdForUpdate(String orderId,boolean isUpdate) {
        Map<String,Object> param=new HashedMap();
        param.put("orderId",orderId);
        param.put("isUpdate",isUpdate);
        return this.getSqlSession().selectOne(this.getMapperNamespace() + ".selectByOrderIdForUpdate",param);
    }

    @Override
    public ZbaoPayOrder selectLoginAccount(String loginAccount,String orderId) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("loginAccount",loginAccount);
        map.put("orderId",orderId);
        return this.getSqlSession().selectOne(getMapperNamespace()+".selectLoginAccount",map);
    }

    @Override
    public ZbaoPayOrder selectOrder(String orderId) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("orderId",orderId);
        return this.getSqlSession().selectOne(getMapperNamespace()+".selectorder",map);
    }

    @Override
    public ZbaoPayOrder selectLoginCz(String loginAccount,String cgOrderId) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("loginAccount",loginAccount);
        map.put("id",cgOrderId);
        return this.getSqlSession().selectOne(getMapperNamespace()+".selectLoginCz",map);
    }
}
