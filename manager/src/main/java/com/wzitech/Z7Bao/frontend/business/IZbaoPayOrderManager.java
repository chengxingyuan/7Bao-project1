package com.wzitech.Z7Bao.frontend.business;

import com.wzitech.Z7Bao.frontend.entity.ZbaoPayOrder;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;

import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/25
 */
public interface IZbaoPayOrderManager {
    GenericPage<ZbaoPayOrder> queryPage(Map<String, Object> paramMap, int limit, int startIndex, String orderBy,
                                        Boolean isAsc);

    ZbaoPayOrder  querylogin(String loginAccount,String orderId);

    ZbaoPayOrder queryOrder(String orderId);

    List<ZbaoPayOrder> queryPayOrderExport(Map<String,Object> query);

    ZbaoPayOrder queryloginCz(String loginAccount, String cgOrderId);

    int update(ZbaoPayOrder order);

}
