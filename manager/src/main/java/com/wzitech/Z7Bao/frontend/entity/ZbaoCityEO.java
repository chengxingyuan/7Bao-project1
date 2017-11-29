package com.wzitech.Z7Bao.frontend.entity;
import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import org.springframework.stereotype.Component;

/**
 * Created by 汪俊杰 on 2017/8/23.
 */
@Component
public class ZbaoCityEO  extends BaseEntity {

    /**
     * 省份编号
     */
    private Long provinceId;
    /**
     * 省份名称
     */
    private String provinceName;
    /**
     * 城市编号
     */
    private Long cityId;
    /**
     * 城市名称
     */
    private String cityName;


    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}
