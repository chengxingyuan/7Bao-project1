package com.wzitech.Z7Bao.frontend.business;

import com.wzitech.Z7Bao.frontend.entity.ZbaoWithdrawals;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;

import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/28
 */
public interface IZbaoWithdrawalsManager {
    GenericPage<ZbaoWithdrawals> queryPage(Map<String, Object> paramMap, int limit, int startIndex, String orderBy,
                                           Boolean isAsc);

    ZbaoWithdrawals querylogin(String loginAccount,String orderId);



    ZbaoWithdrawals queryloginAccount(String loginAccount);


    ZbaoWithdrawals queryTx(String orderId);

    List<ZbaoWithdrawals> queryAllForExport(Map<String, Object> queryParam);


}
