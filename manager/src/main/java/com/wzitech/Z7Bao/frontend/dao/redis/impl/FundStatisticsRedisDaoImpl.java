package com.wzitech.Z7Bao.frontend.dao.redis.impl;

import com.wzitech.Z7Bao.frontend.dao.redis.IFundStatisticsRedisDao;
import com.wzitech.Z7Bao.frontend.entity.SystemConfig;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by chengXY on 2017/9/9.
 */
@Component
public class FundStatisticsRedisDaoImpl extends AbstractRedisDAO<SystemConfig> implements IFundStatisticsRedisDao {
    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }
    private static final JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    /**
     * 解锁
     *
     * @param unlockKey
     * @return
     */
    @Override
    public boolean unlock(String unlockKey) {
        if (StringUtils.isNotBlank(unlockKey)) {
            template.delete(RedisKeyHelper.lockKey(unlockKey));
            return true;
        }
        return false;
    }

    @Override
    public boolean lock(long timeout, TimeUnit timeUnit, String lockKey) {
        if (StringUtils.isNotBlank(lockKey)) {
            Boolean result = valueOps.setIfAbsent(RedisKeyHelper.lockKey(lockKey), lockKey);
            if (result != null && result.booleanValue()) {
                template.expire(RedisKeyHelper.lockKey(lockKey), timeout, timeUnit);
            }
            return result;
        }
        return false;
    }

}
