package com.wzitech.Z7Bao.frontend.dao.rdb;

import com.wzitech.Z7Bao.frontend.entity.ZbaoWithdrawals;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;

/**
 * Created by wangmin
 * Date:2017/8/21
 */
public interface IZbaoWithdrawalsDBDAO extends IMyBatisBaseDAO<ZbaoWithdrawals, Long> {

    public ZbaoWithdrawals selectByOrderId(String key);

    public ZbaoWithdrawals selectLoginAccount(String loginAccount,String orderId);

    public ZbaoWithdrawals selectLoginAccount(String loginAccount);

    public ZbaoWithdrawals selectTx(String orderId);

}
