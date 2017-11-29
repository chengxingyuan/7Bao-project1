package com.wzitech.Z7Bao.frontend.service.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

/**
 * Created by 340032 on 2017/8/23.
 */
public class SevenBaoFundResponse extends AbstractServiceResponse {
    /**
     * 保证金key 枚举值
     */
    private String ConfigKey;

    /**
     * 保证金余额
     */
     private String ConfigValue;

    /**
     * 可用资金key 枚举
     */
    private String AvailableFundKey;

    /**
     * 可用资金余额值
     */
    private String AvailableFundValue;

    public String getConfigKey() {
        return ConfigKey;
    }

    public void setConfigKey(String configKey) {
        ConfigKey = configKey;
    }

    public String getConfigValue() {
        return ConfigValue;
    }

    public void setConfigValue(String configValue) {
        ConfigValue = configValue;
    }

    public String getAvailableFundKey() {
        return AvailableFundKey;
    }

    public void setAvailableFundKey(String availableFundKey) {
        AvailableFundKey = availableFundKey;
    }

    public String getAvailableFundValue() {
        return AvailableFundValue;
    }

    public void setAvailableFundValue(String availableFundValue) {
        AvailableFundValue = availableFundValue;
    }
}
