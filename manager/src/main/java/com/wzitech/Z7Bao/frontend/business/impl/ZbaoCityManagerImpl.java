package com.wzitech.Z7Bao.frontend.business.impl;

import com.wzitech.Z7Bao.frontend.business.IZbaoCityManager;
import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoCityDBDAO;
import com.wzitech.Z7Bao.frontend.dao.redis.IZbaoCityRedisDAO;
import com.wzitech.Z7Bao.frontend.entity.ZbaoCityEO;
import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * s省份信息
 * Created by 汪俊杰 on 2017/8/23.
 */
@Component("ZbaoCityManagerImpl")
public class ZbaoCityManagerImpl extends AbstractBusinessObject implements IZbaoCityManager {
    @Autowired
    IZbaoCityDBDAO zbaoCityDBDAO;
    @Autowired
    IZbaoCityRedisDAO zbaoCityRedisDAO;

    /**
     * 根据省份ID获取城市列表
     *
     * @param provinceId
     * @return
     */
    @Override
    public List<JSONObject> selectByProvinceId(Long provinceId) {
        List<JSONObject> objectList = zbaoCityRedisDAO.selectCity(provinceId);
        if (objectList == null || objectList.size() == 0) {
            Map<String, Object> queryParam = new HashMap<String, Object>();
            queryParam.put("provinceId", provinceId);
            List<ZbaoCityEO> cityEOList = zbaoCityDBDAO.selectByMap(queryParam);
            if (cityEOList != null && cityEOList.size() > 0) {
                for (ZbaoCityEO cityEO : cityEOList) {
                    zbaoCityRedisDAO.setFinanceCity(cityEO);
                }
            }
            return zbaoCityRedisDAO.selectCity(provinceId);

        }
        return objectList;
    }


    /**
     * 获取省份列表
     *
     * @return
     */
    @Override
    public List<JSONObject> selectProvince() {
        List<JSONObject> objectList = zbaoCityRedisDAO.selectProvince();
        if (objectList == null || objectList.size() == 0) {
            List<ZbaoCityEO> cityEOList = zbaoCityDBDAO.selectProvinceList();
            if (cityEOList != null && cityEOList.size() > 0) {
                for (ZbaoCityEO cityEO : cityEOList) {
                    zbaoCityRedisDAO.setFinanceCity(cityEO);
                }
            }
            return zbaoCityRedisDAO.selectProvince();

        }
        return objectList;
    }

    @Override
    public ZbaoCityEO selectProvinceId(Long provinceId) {
        return zbaoCityDBDAO.selectProvinceId(provinceId);

    }

    @Override
    public ZbaoCityEO selectCityId(Long cityId) {
        return zbaoCityDBDAO.selectCityId(cityId);
    }
}
