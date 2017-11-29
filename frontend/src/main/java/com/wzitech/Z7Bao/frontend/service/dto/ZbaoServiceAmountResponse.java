package com.wzitech.Z7Bao.frontend.service.dto;

import com.wzitech.Z7Bao.frontend.entity.ZbaoServiceAmountConfig;
import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/9/1
 */
public class ZbaoServiceAmountResponse extends AbstractServiceResponse {

    List<Map<String,Object>> data;

    public List<Map<String, Object>> getData() {

        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    private ZbaoServiceAmountConfig zbaoServiceAmountConfig;

    public ZbaoServiceAmountConfig getZbaoServiceAmountConfig() {
        return zbaoServiceAmountConfig;
    }

    public void setZbaoServiceAmountConfig(ZbaoServiceAmountConfig zbaoServiceAmountConfig) {
        this.zbaoServiceAmountConfig = zbaoServiceAmountConfig;
    }

    private List<ZbaoServiceAmountConfig> zbaoServiceAmountConfigList;

    public List<ZbaoServiceAmountConfig> getZbaoServiceAmountConfigList() {
        return zbaoServiceAmountConfigList;
    }

    public void setZbaoServiceAmountConfigList(List<ZbaoServiceAmountConfig> zbaoServiceAmountConfigList) {
        this.zbaoServiceAmountConfigList = zbaoServiceAmountConfigList;
    }
}
