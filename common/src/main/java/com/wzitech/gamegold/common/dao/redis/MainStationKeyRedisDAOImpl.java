package com.wzitech.gamegold.common.dao.redis;

import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.dao.IMainStationKeyRedisDAO;
import com.wzitech.gamegold.common.usermgmt.entity.MainStationKeyEO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * Created by chengXY on 2017/8/15.
 */
@Repository
public class MainStationKeyRedisDAOImpl extends AbstractRedisDAO<MainStationKeyEO> implements IMainStationKeyRedisDAO {
    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    /**
     * 保存主站key至redis
     * */
    @Override
    public void saveKey(MainStationKeyEO mainStationKeyEO) {
        if (mainStationKeyEO==null){
            return;
        }
        //存储主站key至redis
        BoundHashOperations<String, String, String> mainKeyOps = template
                .boundHashOps(RedisKeyHelper.mainKey());
        if (StringUtils.isNotEmpty(mainStationKeyEO.getAccessToken())){
            mainKeyOps.put("accessToken",mainStationKeyEO.getAccessToken());
        }
        if (StringUtils.isNotEmpty(mainStationKeyEO.getAccessSecret())){
            mainKeyOps.put("accessSecret",mainStationKeyEO.getAccessSecret());
        }
        template.expire(RedisKeyHelper.mainKey(),600, TimeUnit.SECONDS);
        template.expire(RedisKeyHelper.mainKey(),600, TimeUnit.SECONDS);

    }

    /**
     * 从redis中取出mainKey
     * */
    @Override
    public MainStationKeyEO getKey() {
        // 从redis中获取key
        BoundHashOperations<String, String, String> keyOps = template.boundHashOps(RedisKeyHelper.mainKey());
//        if (keyOps==null ||keyOps.entries().isEmpty()){
//            return null;
//        }else {
//            return mapper.fromHash(keyOps.entries());
//        }
        return keyOps.entries().isEmpty() != true ? mapper.fromHash(keyOps.entries()) : null;
    }

    /**
     * 清除redis中的主站密钥
     * */
    @Override
    public void clearKey(){
        template.delete(RedisKeyHelper.mainKey());
    }
}
