package com.wzitech.Z7Bao.frontend.business;

import com.wzitech.Z7Bao.frontend.entity.ZbaoCityEO;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * 省市信息
 * Created by 汪俊杰 on 2017/8/23.
 */
public interface IZbaoCityManager {
    /**
     * 根据省份ID获取城市列表
     * @param provinceId
     * @return
     */
    List<JSONObject> selectByProvinceId(Long provinceId);

    /**
     * 获取省份列表
     * @return
     */
    List<JSONObject> selectProvince();


    ZbaoCityEO selectProvinceId(Long provinceId);

    ZbaoCityEO selectCityId(Long cityId);
}
