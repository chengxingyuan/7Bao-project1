package com.wzitech.Z7Bao.frontend.business;

import com.wzitech.Z7Bao.frontend.entity.ZbaoTransfer;
import com.wzitech.Z7Bao.frontend.entity.ZbaoWithdrawals;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;

import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/29
 * 支出明细
 */
public interface IZbaoTransferManager {
    GenericPage<ZbaoTransfer> queryPage(Map<String, Object> paramMap, int limit, int startIndex, String orderBy,
                                        Boolean isAsc);

    ZbaoTransfer  querylogin(String loginAccount,String orderId);

    List<ZbaoTransfer> queryAllForExport(Map<String, Object> queryParam);
}
