package com.wzitech.Z7Bao.frontend.business.impl;

import com.wzitech.Z7Bao.frontend.business.IZbaoServiceAmountConfigManager;
import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoServiceAmountConfigDao;
import com.wzitech.Z7Bao.frontend.entity.ZbaoServiceAmountConfig;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/30
 */
@Component
public class ZbaoServiceAmountConfigManagerImpl implements IZbaoServiceAmountConfigManager {
    @Autowired
    IZbaoServiceAmountConfigDao zbaoServiceAmountConfigDao;

    @Override
    public GenericPage<ZbaoServiceAmountConfig> queryPage(Map<String, Object> paramMap, int limit, int startIndex, String orderBy, Boolean isAsc) {

        return zbaoServiceAmountConfigDao.selectByMap(paramMap, limit, startIndex, orderBy, isAsc);
    }

    @Override
    public void addServiceAmount(ZbaoServiceAmountConfig zbaoServiceAmountConfig) {
        if (zbaoServiceAmountConfig != null) {
            zbaoServiceAmountConfigDao.insert(zbaoServiceAmountConfig);
        }
    }

    @Override
    public List<ZbaoServiceAmountConfig> queryByMap(Map<String, Object> queryMap, String sortBy, boolean isAsc) {
        return zbaoServiceAmountConfigDao.selectByMap(queryMap, sortBy, isAsc);
    }

    @Override
    public void delete(Long id) {
        if (id != null) {
            ZbaoServiceAmountConfig zbaoServiceAmountConfig = zbaoServiceAmountConfigDao.selectById(id);
            if (zbaoServiceAmountConfig != null) {
                zbaoServiceAmountConfigDao.delete(zbaoServiceAmountConfig);
            }
        }
    }

    @Override
    public void update(ZbaoServiceAmountConfig zbaoServiceAmountConfig) {

        if (zbaoServiceAmountConfig != null) {
            zbaoServiceAmountConfigDao.update(zbaoServiceAmountConfig);
        }
    }

    @Override
    public ZbaoServiceAmountConfig selectByid(Long id) {
        ZbaoServiceAmountConfig zbaoServiceAmountConfig = new ZbaoServiceAmountConfig();
        if (id != null) {
            zbaoServiceAmountConfig = zbaoServiceAmountConfigDao.selectById(id);
        }
        return zbaoServiceAmountConfig;
    }

    @Override
    public List<Map<String, Object>> selectAll() {
       return zbaoServiceAmountConfigDao.selectMaxAndMin();
    }
}
