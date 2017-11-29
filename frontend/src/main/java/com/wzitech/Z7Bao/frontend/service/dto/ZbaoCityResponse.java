package com.wzitech.Z7Bao.frontend.service.dto;

import com.wzitech.Z7Bao.frontend.entity.ZbaoCityEO;
import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by wangmin
 * Date:2017/8/30
 */
public class ZbaoCityResponse extends AbstractServiceResponse {
    private ZbaoCityEO zbaoCityEO;

    private List<ZbaoCityEO> zbaoCityEOList;

    private List<JSONObject> list;

    public List<JSONObject> getList() {
        return list;
    }

    public void setList(List<JSONObject> list) {
        this.list = list;
    }

    public ZbaoCityEO getZbaoCityEO() {
        return zbaoCityEO;
    }

    public void setZbaoCityEO(ZbaoCityEO zbaoCityEO) {
        this.zbaoCityEO = zbaoCityEO;
    }

    public List<ZbaoCityEO> getZbaoCityEOList() {
        return zbaoCityEOList;
    }

    public void setZbaoCityEOList(List<ZbaoCityEO> zbaoCityEOList) {
        this.zbaoCityEOList = zbaoCityEOList;
    }
}
