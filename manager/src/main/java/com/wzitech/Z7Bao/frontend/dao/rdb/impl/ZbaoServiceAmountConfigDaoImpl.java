package com.wzitech.Z7Bao.frontend.dao.rdb.impl;


import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoServiceAmountConfigDao;
import com.wzitech.Z7Bao.frontend.entity.ZbaoServiceAmountConfig;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/17  wurongfan           ZW_C_JB_00021 商城资金7bao改造
 */
@Repository
public class ZbaoServiceAmountConfigDaoImpl extends AbstractMyBatisDAO<ZbaoServiceAmountConfig,Long> implements IZbaoServiceAmountConfigDao{
    @Override
    public void updateRate(ZbaoServiceAmountConfig zbaoServiceAmountConfig) {
        this.getSqlSession().update(this.mapperNamespace + ".updateRate",zbaoServiceAmountConfig);
    }

    @Override
    public List<Map<String,Object>> selectMaxAndMin() {
        return this.getSqlSession().selectList(this.mapperNamespace+".selectMaxAndMin");
    }
}
