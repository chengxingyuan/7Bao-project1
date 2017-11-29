package com.wzitech.Z7Bao.frontend.dao.redis;

import com.wzitech.Z7Bao.frontend.entity.SystemConfig;

/**
 * Created by wangmin
 * Date:2017/9/4
 */
public interface ISystemConfigRedisDao {

    public SystemConfig getByConfigKey(String configKey) ;

    public void  save(SystemConfig configEO);

    public int delete(SystemConfig configEO);
}
