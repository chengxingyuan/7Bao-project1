package com.wzitech.Z7Bao.frontend.service;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/9/1
 */
public class ZbaoBankResponse extends AbstractServiceResponse {

    List<Map<String, Object>> data;

    public List<Map<String, Object>> getData() {

        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

}
