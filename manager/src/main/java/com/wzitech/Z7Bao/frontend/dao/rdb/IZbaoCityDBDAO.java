package com.wzitech.Z7Bao.frontend.dao.rdb;

import com.wzitech.Z7Bao.frontend.entity.ZbaoCityEO;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;

import java.util.List;
import java.util.Map;

/**
 * 省市信息
 * Created by 汪俊杰 on 2017/8/23.
 */
public interface IZbaoCityDBDAO extends IMyBatisBaseDAO<ZbaoCityEO, Long> {
    /**
     * 查找对应条件的游戏账号角色信息
     *
     * @return
     */
    List<ZbaoCityEO> selectProvinceList();


    ZbaoCityEO selectProvinceId(Long provinceId);


    ZbaoCityEO selectCityId(Long cityId);



}
