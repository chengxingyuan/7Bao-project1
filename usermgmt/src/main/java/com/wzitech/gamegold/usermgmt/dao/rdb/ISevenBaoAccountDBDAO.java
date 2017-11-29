package com.wzitech.gamegold.usermgmt.dao.rdb;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;

import java.util.Map;

/**
 * Created by chengXY on 2017/8/10.
 */
public interface ISevenBaoAccountDBDAO extends IMyBatisBaseDAO<SevenBaoAccountInfoEO, Long> {

    public GenericPage<SevenBaoAccountInfoEO> findUserList(Map<String, Object> queryMap, int limit, int start);

    public SevenBaoAccountInfoEO findAccountById(Long id);


    SevenBaoAccountInfoEO selectByUId(String uid, Boolean forUpdate);

    public SevenBaoAccountInfoEO selectAccount(String loginAccount);

    /**
     * 更加账号和uid查7bao账号信息
     */
    SevenBaoAccountInfoEO queryByAccountAndUid(String account, String uid);

    /*
    查询银行名称
     */
    String selectBankName(String loginAccount);

}
