package com.wzitech.Z7Bao.frontend.dao.rdb;

import com.wzitech.Z7Bao.frontend.entity.ZbaoPayOrder;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;

import java.util.Map;

/**
 * Created by 340032 on 2017/8/14.
 */
public interface IZbaoPayOrderDAO extends IMyBatisBaseDAO<ZbaoPayOrder, Long> {

    GenericPage<ZbaoPayOrder> findOrderList(Map<String, Object> queryMap, int limit, int start);

    ZbaoPayOrder selectByOrderIdForUpdate(String orderId,boolean isUpdate);

    public ZbaoPayOrder selectLoginAccount(String loginAccount,String orderId);

    ZbaoPayOrder selectOrder(String orderId);


    ZbaoPayOrder selectLoginCz(String loginAccount, String cgOrderId);

}
