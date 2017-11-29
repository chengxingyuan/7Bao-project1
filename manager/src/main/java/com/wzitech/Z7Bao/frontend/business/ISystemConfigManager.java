package com.wzitech.Z7Bao.frontend.business;

import com.wzitech.Z7Bao.frontend.entity.SystemConfig;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;

import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/22
 * 收货系统配置
 */
public interface ISystemConfigManager {
    /**
     * 查询系统配置
     * @param paramMap
     * @param limit
     * @param startIndex
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<SystemConfig> queryPage(Map<String, Object> paramMap, int limit, int startIndex, String orderBy,
                                        Boolean isAsc);

    /**
     * 根据KEY查询配置
     * @param key
     * @return
     */
    SystemConfig getSystemConfigByKey(String key);

    /**
     * 删除系统配置
     *
     */
    void deleteConfigById(Long id);

    /**
     * 增加系统配置
     */
    void add(SystemConfig systemConfig);

    /**
     * 修改系统配置
     * @param systemConfig
     */
    void update(SystemConfig systemConfig);

    /**
     * 禁用配置
     * @param id
     */
    void disableUser(Long id);


    /**
     * 启用配置
     * @param id
     */
    void qyUser(Long id);

}
