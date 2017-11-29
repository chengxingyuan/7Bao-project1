package com.wzitech.Z7Bao.frontend.business.impl;

import com.wzitech.Z7Bao.frontend.business.ISystemConfigManager;
import com.wzitech.Z7Bao.frontend.dao.rdb.ISystemConfigDBDAO;
import com.wzitech.Z7Bao.frontend.dao.redis.ISystemConfigRedisDao;
import com.wzitech.Z7Bao.frontend.entity.SystemConfig;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/22
 * 收货系统配置
 */
@Component
public class SystemConfigManagerImpl implements ISystemConfigManager{
    @Autowired
    ISystemConfigDBDAO systemConfigDBDAO;

    @Autowired
    ISystemConfigRedisDao systemConfigRedisDao;

    /**
     * 分页查询系统配置
     * @param paramMap
     * @param limit
     * @param startIndex
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<SystemConfig> queryPage(Map<String, Object> paramMap, int limit, int startIndex, String orderBy, Boolean isAsc) {

        return systemConfigDBDAO.selectByMap(paramMap, limit, startIndex, orderBy, isAsc);
    }

    /**
     * 关键字查询
     * @param key
     * @return
     */
    @Override
    public SystemConfig getSystemConfigByKey(String key) {
        SystemConfig systemConfig = systemConfigRedisDao.getByConfigKey(key);
        if (systemConfig == null) {
            systemConfig = systemConfigDBDAO.selectByConfigKey(key);
            if (systemConfig != null) {
                systemConfigRedisDao.save(systemConfig);
            }
        }
        if(systemConfig != null && systemConfig.getEnabled()==false){
            return null;
        }
        return systemConfig;
    }

    /**
     * 删除系统配置
     * @param id
     */
    @Override
    public void deleteConfigById(Long id) {
        SystemConfig systemConfig = systemConfigDBDAO.selectById(id);
        if (systemConfig != null) {
            systemConfigRedisDao.delete(systemConfig);
        }
        systemConfigDBDAO.deleteById(id);
    }

    @Override
    public void add(SystemConfig systemConfig) {
        if (systemConfig != null) {
            systemConfigDBDAO.insert(systemConfig);
            systemConfigRedisDao.save(systemConfig);
        }
    }

    @Override
    public void update(SystemConfig systemConfig) {
        if (systemConfig != null) {
            SystemConfig systemConfigs = systemConfigDBDAO.selectById(systemConfig.getId());
            if (systemConfig != null) {
                systemConfigRedisDao.delete(systemConfigs);
            }
            systemConfigDBDAO.update(systemConfig);
            systemConfigRedisDao.save(systemConfig);
        }
    }

    @Override
    public void disableUser(Long id) {

        if (id != null){
            SystemConfig systemConfig = systemConfigDBDAO.selectById(id);
            if (systemConfig != null){
                systemConfig.setEnabled(false);
                systemConfigDBDAO.update(systemConfig);
                systemConfigRedisDao.save(systemConfig);
            }
        }
    }

    @Override
    public void qyUser(Long id) {
        if (id != null){
            SystemConfig systemConfig = systemConfigDBDAO.selectById(id);
            if (systemConfig != null){
                systemConfig.setEnabled(true);
                systemConfigDBDAO.update(systemConfig);
                systemConfigRedisDao.save(systemConfig);
            }
        }
    }
}
