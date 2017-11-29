package com.wzitech.Z7Bao.frontend.business;

import com.wzitech.Z7Bao.frontend.entity.ZbaoServiceAmountConfig;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;

import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/30
 */
public interface IZbaoServiceAmountConfigManager {
    /*
    查询所有数据
     */
    GenericPage<ZbaoServiceAmountConfig> queryPage(Map<String, Object> paramMap, int limit, int startIndex, String orderBy,
                                        Boolean isAsc);

    /*
    添加数据
     */
    void addServiceAmount(ZbaoServiceAmountConfig zbaoServiceAmountConfig);

    public List<ZbaoServiceAmountConfig> queryByMap(Map<String,Object> queryMap, String sortBy, boolean isAsc);

    void delete(Long id);

    void update(ZbaoServiceAmountConfig zbaoServiceAmountConfig);

    ZbaoServiceAmountConfig selectByid(Long id);

    List<Map<String, Object>> selectAll();
}
