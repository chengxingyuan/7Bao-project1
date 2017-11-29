package com.wzitech.Z7Bao.frontend.dao.rdb.impl;

import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoBankDBDAO;
import com.wzitech.Z7Bao.frontend.entity.ZbaoBank;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/23
 */
@Repository
public class ZbaoBankDBDAOImpl extends AbstractMyBatisDAO<ZbaoBank, Long> implements IZbaoBankDBDAO{


    @Override
    public ZbaoBank selectByName(int code) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("code",code);
        return this.getSqlSession().selectOne(getMapperNamespace()+".selectByName",map);
    }

    @Override
    public List<Map<String,Object>> selectBankName() {
        return this.getSqlSession().selectList(getMapperNamespace()+".selectBankName");
    }


}
