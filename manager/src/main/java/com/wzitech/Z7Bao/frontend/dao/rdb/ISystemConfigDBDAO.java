package com.wzitech.Z7Bao.frontend.dao.rdb;


import com.wzitech.Z7Bao.frontend.entity.SystemConfig;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;

/**
 * Created by Administrator on 2016/12/15.
 */
public interface ISystemConfigDBDAO extends IMyBatisBaseDAO<SystemConfig, Long> {

    /**
     * 根据KEY查询VALUE
     * @param key
     * @return
     */
    SystemConfig selectByConfigKey(String key);
}
