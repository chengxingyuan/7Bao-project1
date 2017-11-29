package com.wzitech.gamegold.usermgmt.dao.redis.impl;

import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.usermgmt.dao.redis.ISevenBaoAccountRedisDAO;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by 340082 on 2017/8/17.
 */
@Repository("sevenBaoAccountRedisDAOImpl")
public class SevenBaoAccountRedisDAOImpl extends AbstractRedisDAO<SevenBaoAccountInfoEO> implements ISevenBaoAccountRedisDAO {

    private static final String USER_ID_PAY_SEQ = "CACHE_7BAO_PAY_USER_ID_SEQ";

    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    public String saveUserForLoginAuth(SevenBaoAccountInfoEO userInfoEO) {

        String authkey = UUID.randomUUID().toString().replace("-", "");

        // 将userInfo保存至Redis
        BoundHashOperations<String, String, String> userOps = template
                .boundHashOps(RedisKeyHelper.accountAuthKey(authkey));

        userOps.put("id", userInfoEO.getId().toString());
        userOps.put("uid", userInfoEO.getUid().toString());
        userOps.put("loginAccount", userInfoEO.getLoginAccount());
        template.expire(RedisKeyHelper.accountAuthKey(authkey), 60, TimeUnit.MINUTES);
        return authkey;
    }

    public SevenBaoAccountInfoEO getLoginId(String authkey) {
// 将userInfo保存至Redis
        BoundHashOperations<String, String, String> userOps = template
                .boundHashOps(RedisKeyHelper.accountAuthKey(authkey));
        template.expire(RedisKeyHelper.accountAuthKey(authkey), 30, TimeUnit.MINUTES);
        return userOps.entries().isEmpty() != true ? mapper.fromHash(userOps.entries()) : null;
    }

    //删除登录用户信息
    public void remLoginId(String authkey) {
        template.delete(RedisKeyHelper.accountAuthKey(authkey));
    }

    /**
     * 生成用户id号
     *
     * @return
     */
    @Override
    public String getSevenBaoAccountId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        Date now = new Date();
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(now);
        long id = valueOps.increment(USER_ID_PAY_SEQ, 1);
        setIncrement(nowCal, id);
        String orderId = StringUtils.leftPad(String.valueOf(id), 7, '0');
        StringBuffer sb = new StringBuffer("ZBUS");
        sb.append(sdf.format(nowCal.getTime())).append(orderId);
        return sb.toString();
    }

    private void setIncrement(Calendar nowCal, long id) {
        if (id == 1L) {
            //设置缓存数据最后的失效时间为当天的最后一秒
            Calendar lastCal = Calendar.getInstance();
            lastCal.set(nowCal.get(Calendar.YEAR), nowCal.get(Calendar.MONTH), nowCal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
            lastCal.set(Calendar.MILLISECOND, 999);
            template.expireAt(USER_ID_PAY_SEQ, lastCal.getTime());
        }
    }
}
