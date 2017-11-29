package com.wzitech.gamegold.common.dao;

import com.wzitech.gamegold.common.usermgmt.entity.MainStationKeyEO;

/**
 * Created by chengXY on 2017/8/15.
 */
public interface IMainStationKeyRedisDAO {
    /**
     * 保存主站key至redis
     * */
    public void saveKey(MainStationKeyEO mainStationKeyEO);

    /**
     * 从redis中取出mainkey
     * */
    public MainStationKeyEO getKey();

    /**
     * 清除redis中的主站密钥
     * */
    public void clearKey();
}
