package com.wzitech.Z7Bao.frontend.dao.rdb;

import com.wzitech.Z7Bao.frontend.entity.ZbaoTransfer;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;

/**
 * Created by 340082 on 2017/8/21.
 */
public interface IZbaoTransferDAO extends IMyBatisBaseDAO<ZbaoTransfer, Long> {

    public ZbaoTransfer selectLoginAccount(String loginAccount,String orderId);
}
