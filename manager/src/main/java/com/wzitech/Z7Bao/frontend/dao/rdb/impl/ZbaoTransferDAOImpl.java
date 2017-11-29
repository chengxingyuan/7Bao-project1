package com.wzitech.Z7Bao.frontend.dao.rdb.impl;

import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoTransferDAO;
import com.wzitech.Z7Bao.frontend.entity.ZbaoTransfer;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 340082 on 2017/8/21.
 */
@Repository
public class ZbaoTransferDAOImpl extends AbstractMyBatisDAO<ZbaoTransfer,Long> implements IZbaoTransferDAO {
    @Override
    public ZbaoTransfer selectLoginAccount(String loginAccount,String orderId) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("loginAccount",loginAccount);
        map.put("orderId",orderId);
        return this.getSqlSession().selectOne(getMapperNamespace()+".selectLoginAccount",map);
    }
}
