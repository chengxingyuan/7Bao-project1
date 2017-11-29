package com.wzitech.gamegold.usermgmt.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;

import java.util.Map;

/**
 * Created by chengXY on 2017/8/10.
 */
public interface ISevenBaoAccountManager {

    /**
     *查询账号信息
     */
    public GenericPage<SevenBaoAccountInfoEO> querySevenBaoAccount(Map<String, Object> queryMap,
        int limit, int start, String sortBy, boolean isAsc);
    public SevenBaoAccountInfoEO modifyAccount(SevenBaoAccountInfoEO sevenBaoAccountInfoEO);
    public void enableAccount(Long id);
    public void disableAccount(Long id);

    String insertSevenBao(SevenBaoAccountInfoEO sevenBaoAccountInfoEO);

    void updateBind(SevenBaoAccountInfoEO sevenBaoAccountInfoEO);

    public String saveLoginAuth(String userName);

    public SevenBaoAccountInfoEO findAccountByAuth(String auth);

    /**
     * 根据登录账号和uid查询实体信息
     * */
    SevenBaoAccountInfoEO queryByAccountAndUid(String account,String uid);

    /**
     * 根据登录账户查询账户信息
     * @param loginAccount
     * @return
     */
    public SevenBaoAccountInfoEO queryDateByProp(String loginAccount);

    SevenBaoAccountInfoEO queryDataByPropWithUid(String uid);

    /**
     * 删除登录用户
     * */
    public void deleteAccountByAuth(String auth);

    String selectBankName(String loginAccount);

    SevenBaoAccountInfoEO selectById(Long id);


}
