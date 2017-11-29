package com.wzitech.Z7Bao.frontend.business.impl;

import com.wzitech.Z7Bao.frontend.business.IZbaoTransferManager;
import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoTransferDAO;
import com.wzitech.Z7Bao.frontend.entity.ZbaoTransfer;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/29
 */
@Component
public class ZbaoTransferManagerImpl implements IZbaoTransferManager {
    @Autowired
    IZbaoTransferDAO zbaoTransferDAO;
    @Override
    public GenericPage<ZbaoTransfer> queryPage(Map<String, Object> paramMap, int limit, int startIndex, String orderBy, Boolean isAsc) {
        return zbaoTransferDAO.selectByMap(paramMap,limit,startIndex,orderBy,isAsc);
    }

    @Override
    public ZbaoTransfer querylogin(String loginAccount,String orderId) {
        return zbaoTransferDAO.selectLoginAccount(loginAccount,orderId);
    }

    @Override
    public List<ZbaoTransfer> queryAllForExport(Map<String, Object> queryParam) {
        return zbaoTransferDAO.selectByMap(queryParam);
    }
}
