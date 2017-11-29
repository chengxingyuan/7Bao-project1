package com.wzitech.Z7Bao.frontend.dao.rdb;

import com.wzitech.Z7Bao.frontend.entity.ZbaoFundDetail;
import com.wzitech.Z7Bao.frontend.entity.ZbaoPayOrder;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/21
 */
public interface IZbaoFundDetailDBDAO extends IMyBatisBaseDAO<ZbaoFundDetail, Long> {

    ZbaoFundDetail selectLoginAccount(String loginAccount,String orderId,Integer type);

    /**
     * 修改充值单详情状态
     * @param orderId 充值单订单号
     * @param log 更新日志
     * @param status 成功或失败
     * @return
     */
    int changeRechargeStatus(String orderId, String log,boolean status);


    List<ZbaoFundDetail> selectLoginAccount(String loginAccount);

    ZbaoFundDetail selectLoginOld(String loginAccount,Integer orderId);

    ZbaoFundDetail selectLoginCz(String loginAccount,String czOrderId);

    ZbaoFundDetail selectLoginTx(String loginAccount,String txOrderId);

    ZbaoFundDetail selectLogincg(String loginAccount,String cgOrderId);

    ZbaoFundDetail selectLogindj(String loginAccount,String freezeOrderId);


}
