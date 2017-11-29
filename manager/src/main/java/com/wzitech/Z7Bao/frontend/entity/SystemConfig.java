package com.wzitech.Z7Bao.frontend.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

/**出货系统配置
 * Created by Administrator on 2016/12/15.
 */
public class SystemConfig extends BaseEntity {
    /**
     * 配置名称
     */
    private String configKey;

    /**
     * 配置的值
     */
    private String configValue;

    /**
     *备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;


    /**
     * 可用资金余额值
     */
    private String AvailableFundValue;


    public String getAvailableFundValue() {
        return AvailableFundValue;
    }

    public void setAvailableFundValue(String availableFundValue) {
        AvailableFundValue = availableFundValue;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
