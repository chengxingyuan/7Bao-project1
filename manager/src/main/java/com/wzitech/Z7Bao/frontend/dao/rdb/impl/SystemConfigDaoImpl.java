package com.wzitech.Z7Bao.frontend.dao.rdb.impl;

import com.wzitech.Z7Bao.frontend.dao.rdb.ISystemConfigDBDAO;
import com.wzitech.Z7Bao.frontend.entity.SystemConfig;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/12/15.
 */
@Repository
public class SystemConfigDaoImpl extends AbstractMyBatisDAO<SystemConfig, Long> implements ISystemConfigDBDAO {

    @Override
    public  SystemConfig selectByConfigKey(String key) {
        return this.getSqlSession().selectOne(getMapperNamespace() + ".selectByConfigKey", key);
    }
}
