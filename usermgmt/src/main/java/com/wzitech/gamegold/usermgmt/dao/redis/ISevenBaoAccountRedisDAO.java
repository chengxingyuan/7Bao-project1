package com.wzitech.gamegold.usermgmt.dao.redis;

import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;

/**
 * Created by 340082 on 2017/8/17.
 * 用户redis数据库类
 */
public interface ISevenBaoAccountRedisDAO {

    public String saveUserForLoginAuth(SevenBaoAccountInfoEO userInfoEO);

    public SevenBaoAccountInfoEO getLoginId(String authkey);

    public void remLoginId(String authkey);

    public String getSevenBaoAccountId();
}
