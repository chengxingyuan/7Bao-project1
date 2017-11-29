package com.wzitech.Z7Bao.frontend.business;

import com.wzitech.Z7Bao.frontend.entity.FundStatistics;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;

import java.util.List;
import java.util.Map;

/**
 * Created by 340032 on 2017/8/25.
 */
public interface IFundStatisticsManager {

    /**
     * 分页查询
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param
     * @return
     */
    GenericPage<FundStatistics> queryListInPage(Map<String,Object> map, int start, int pageSize, String orderBy, boolean
            desc);

    List<FundStatistics> queryAll(Map<String,Object> querypaeam);


    /**
     * 统计每日数据
     */
    void statistics();

    /**
     * 查出所有，导出表格用
     * */
    List<FundStatistics> queryAllForExport(Map<String, Object> queryParam);
}
