package com.wzitech.Z7Bao.frontend.dao.rdb.impl;

import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoCityDBDAO;
import com.wzitech.Z7Bao.frontend.entity.ZbaoCityEO;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省市信息
 * Created by 汪俊杰 on 2017/8/23.
 */
@Repository
public class ZbaoCityDBDAOImpl extends AbstractMyBatisDAO<ZbaoCityEO, Long> implements IZbaoCityDBDAO {
    /**
     * 查找对应条件的游戏账号角色信息
     *
     * @return
     */
    @Override
    public List<ZbaoCityEO> selectProvinceList() {
        return this.getSqlSession().selectList(getMapperNamespace() + ".selectProvinceList");
    }

    @Override
    public ZbaoCityEO selectProvinceId(Long provinceId) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("provinceId",provinceId);
        return getSqlSession().selectOne(getMapperNamespace()+".selectProvinceId",map);
    }

    @Override
    public ZbaoCityEO selectCityId(Long cityId) {
        Map<String,Object> map =new HashMap<String, Object>();
        map.put("cityId",cityId);
        return getSqlSession().selectOne(getMapperNamespace()+".selectcityId",map);
    }
}
