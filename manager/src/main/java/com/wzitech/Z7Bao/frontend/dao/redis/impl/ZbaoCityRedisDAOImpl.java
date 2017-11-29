package com.wzitech.Z7Bao.frontend.dao.redis.impl;

import com.wzitech.Z7Bao.frontend.dao.redis.IZbaoCityRedisDAO;
import com.wzitech.Z7Bao.frontend.entity.ZbaoCityEO;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by 汪俊杰 on 2017/8/23.
 */
@Repository
public class ZbaoCityRedisDAOImpl extends AbstractRedisDAO<ZbaoCityEO> implements IZbaoCityRedisDAO {

    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }


    /**
     * 添加城市信息
     * @param eo
     */
    public void setFinanceCity(ZbaoCityEO eo) {
        JSONObject object = new JSONObject();
        if(null==eo)throw new NullPointerException("ZbaoCityEO is null ");
        if(eo.getProvinceId()!=null&&eo.getProvinceName()!=null) {
            object.put("provinceId", eo.getProvinceId());
            object.put("provinceName", eo.getProvinceName());
            zSetOps.add(RedisKeyHelper.financeProvinceKey(), object.toString(), eo.getProvinceId());
        }
        object.clear();
        if(eo.getCityId()!=null&&eo.getCityName()!=null) {
            object.put("cityId", eo.getCityId());
            object.put("cityName", eo.getCityName());
            zSetOps.add(RedisKeyHelper.financeCityKey(eo.getProvinceId()), object.toString(), eo.getCityId());
        }
    }

    /**
     * 删除城市信息
     * @param eo
     */
    public void deleteFinanceCity(ZbaoCityEO eo){
        JSONObject object = new JSONObject();
        object.put("cityId",eo.getCityId());
        object.put("cityName",eo.getCityName());
        zSetOps.remove(RedisKeyHelper.financeCityKey(eo.getProvinceId()),object.toString());
        if(zSetOps.size(RedisKeyHelper.financeCityKey(eo.getProvinceId())) == 0){
            object.clear();
            object.put("provinceId",eo.getProvinceId());
            object.put("provinceName",eo.getProvinceName());
            zSetOps.remove(RedisKeyHelper.financeProvinceKey(), object.toString());
        }
    }

    /**
     * 获取省份表
     * @return
     */
    public List<JSONObject> selectProvince(){
        Set<String> set = zSetOps.range(RedisKeyHelper.financeProvinceKey(),0,-1);
        List<JSONObject> JSONList = new ArrayList<JSONObject>();
        for(String s:set){
            JSONList.add(JSONObject.fromObject(s));
        }
        return JSONList;
    }


    /**
     * 根据省份Id获取城市表
     * @param provinceId
     * @return
     */
    public List<JSONObject> selectCity(Long provinceId){
        Set<String>  set = zSetOps.range(RedisKeyHelper.financeCityKey(provinceId),0,-1);
        List<JSONObject> JSONList = new ArrayList<JSONObject>();
        for(String s:set){
            JSONList.add(JSONObject.fromObject(s));
        }
        return JSONList;
    }

    /**
     * 清除所有redis所有城市省份信息
     */
    public void clear(){
        List<JSONObject> objectList = selectProvince();
        for(JSONObject jsonObject:objectList){
            Long provinceId = Long.parseLong(jsonObject.get("provinceId").toString());
            template.delete(RedisKeyHelper.financeCityKey(provinceId));
        }
        template.delete(RedisKeyHelper.financeProvinceKey());
    }
}
