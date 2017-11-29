package com.wzitech.Z7Bao.frontend.dao.rdb;

import com.wzitech.Z7Bao.frontend.entity.ZbaoServiceAmountConfig;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;

import java.util.List;
import java.util.Map;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/17  wurongfan           ZW_C_JB_00021 商城资金7bao改造
 */
public interface IZbaoServiceAmountConfigDao extends IMyBatisBaseDAO<ZbaoServiceAmountConfig,Long> {

    void updateRate(ZbaoServiceAmountConfig zbaoServiceAmountConfig);
    List<Map<String,Object>> selectMaxAndMin();

}
