package com.wzitech.Z7Bao.frontend.dao.redis;

import com.wzitech.Z7Bao.frontend.entity.ZbaoCityEO;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by 汪俊杰 on 2017/8/23.
 */
public interface IZbaoCityRedisDAO {

    /**
     * 添加城市信息
     * @param eo
     */
    void setFinanceCity(ZbaoCityEO eo);

    /**
     * 删除城市信息
     * @param eo
     */
    void deleteFinanceCity(ZbaoCityEO eo);

    /**
     * 获取省份表
     * @return
     */
    List<JSONObject> selectProvince();

    /**
     * 根据省份Id获取城市表
     * @param provinceId
     * @return
     */
    List<JSONObject> selectCity(Long provinceId);

    /**
     * 清除所有redis所有城市省份信息
     */
    void clear();
}
