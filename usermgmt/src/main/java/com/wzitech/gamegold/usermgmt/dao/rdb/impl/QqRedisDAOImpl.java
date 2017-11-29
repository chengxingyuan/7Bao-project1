package com.wzitech.gamegold.usermgmt.dao.rdb.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.usermgmt.dao.rdb.IQqRedisDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by wangmin
 * Date:2017/9/5
 */
@Component
public class QqRedisDAOImpl extends AbstractRedisDAO<UserInfoEO> implements IQqRedisDAO {
    private static String USER_QQ = "7bao:userQq:";
    UserInfoEO userInfoEO;
    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }
    private static final JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
    @Override
    public String getQq() {
        String key = RedisKeyHelper.getQqInUser();
        String json = valueOps.get(key);
        return String.valueOf(jsonMapper.fromJson(json, String.class));
    }

    @Override
    public void save(String qq) {
        if (qq != null) {
            String json = jsonMapper.toJson(qq);
            String key = RedisKeyHelper.getQqInUser();
            valueOps.set(key, json);
        }
    }
}
