package com.wzitech.Z7Bao.frontend.dao.redis.impl;

import com.wzitech.Z7Bao.frontend.dao.redis.ISystemConfigRedisDao;
import com.wzitech.Z7Bao.frontend.entity.SystemConfig;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by wangmin
 * Date:2017/9/4
 */
@Component
public class SystemConfigRedisDaoImpl extends AbstractRedisDAO<SystemConfig> implements ISystemConfigRedisDao {

    private static String SYSTEM_CONFIG_KEY = "7bao:systemconfig:";
    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }
    private static final JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    @Override
    public SystemConfig getByConfigKey(String configKey) {
     BoundHashOperations<String, String, String> userOps = template.boundHashOps(SYSTEM_CONFIG_KEY);
        String json = userOps.get(configKey);
        return jsonMapper.fromJson(json, SystemConfig.class);
    }

    @Override
    public void save(SystemConfig configEO) {
        if(configEO==null || StringUtils.isBlank(configEO.getConfigKey())){
            return;
        }
        BoundHashOperations<String, String, String> userOps = template.boundHashOps(SYSTEM_CONFIG_KEY);
        String json = jsonMapper.toJson(configEO);
        userOps.put(configEO.getConfigKey(), json);
    }

    @Override
    public int delete(SystemConfig configEO) {
        if (configEO != null && !StringUtils.isBlank(configEO.getConfigKey())) {
            // 删除对应的keys
            BoundHashOperations<String, String, String> userOps = template.boundHashOps(SYSTEM_CONFIG_KEY);
            userOps.delete(configEO.getConfigKey());
        }
        return 0;
    }
}
