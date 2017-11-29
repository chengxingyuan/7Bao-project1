package com.wzitech.Z7Bao.frontend.business;

import com.wzitech.Z7Bao.frontend.entity.ZbaoFundDetail;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;

import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/24
 * 资金明细管理
 */
public interface IZbaoFunDetailManager {
    /**
     * 修改资金明细
     *
     * @param zbaoBank
     */
    void update(ZbaoFundDetail zbaoBank);

    /**
     * 查询银行信息
     *
     * @param paramMap
     * @param limit
     * @param startIndex
     * @param orderBy
     * @param desc
     * @return
     */
    GenericPage<ZbaoFundDetail> queryPage(Map<String, Object> paramMap, int limit, int startIndex, String orderBy,
                                          Boolean desc);


    ZbaoFundDetail querylogin(String loginAccount, String orderId, Integer type);


    List<ZbaoFundDetail> queryloginAccount(String loginAccount);

    /**
     * 充值
     *
     * @param loginAccount
     * @param
     * @return
     */
    ZbaoFundDetail queryloginCz(String loginAccount, String czOrderId);

    /**
     * 提现
     */
    ZbaoFundDetail querylogintx(String loginAccount, String txOrderId);

    /**
     * 转账
     */
    ZbaoFundDetail querylogincg(String loginAccount, String cgOrderId);

    /**
     * 冻结,解冻
     */
    ZbaoFundDetail querylogindj(String loginAccount, String freezeOrderId);


    /**
     * 根据用户名字查总条数
     */
    int queryNumber(String loginAccount);

    /**
     * 查询导出会用到的数据
     */
    List<ZbaoFundDetail> quertExportData(Map<String, Object> query);

    ZbaoFundDetail queryloginOld(String loginAccount, Integer orderId);

}
