package com.wzitech.gamegold.usermgmt.dao.rdb.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.usermgmt.dao.rdb.ISevenBaoAccountDBDAO;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chengXY on 2017/8/10.
 */
@Repository("sevenBaoAccountDBDAOImpl")
public class SevenBaoAccountDBDAOImpl extends AbstractMyBatisDAO<SevenBaoAccountInfoEO, Long> implements ISevenBaoAccountDBDAO {

    public GenericPage<SevenBaoAccountInfoEO> findUserList(Map<String, Object> queryMap, int limit, int start) {
        GenericPage<SevenBaoAccountInfoEO> genericPage = this.selectByMap(queryMap, limit, start);
        return genericPage;
    }

    public SevenBaoAccountInfoEO findAccountById(Long id) {
        return selectUniqueByProp("id", id);
    }
    @Override
    public SevenBaoAccountInfoEO selectByUId(String uid, Boolean forUpdate) {
        Map<String, Object> selectMap = new HashMap<String, Object>();
        selectMap.put("uid", uid);
        selectMap.put("forUpdate", forUpdate);
        return this.getSqlSession().selectOne(this.mapperNamespace + ".selectByUidForUpdate", selectMap);
    }

	//根据loginAccount查询
	public  SevenBaoAccountInfoEO selectAccount(String loginAccount){
        Map<String,Object>map=new HashMap<String, Object>();
        map.put("loginAccount",loginAccount);
        return this.getSqlSession().selectOne(this.getMapperNamespace()+".selectAccount",map);
    }

    /**
     * 查询出对应的账号进行资金的冻结，解冻 锁表
     * */
   public SevenBaoAccountInfoEO queryByAccountAndUid(String account ,String uid){
       Map<String, Object> selectMap = new HashMap<String, Object>();
       selectMap.put("account", account);
       selectMap.put("uid", uid);
       selectMap.put("locked", true);
       return this.getSqlSession().selectOne(this.mapperNamespace + "." + "queryByAccountAndUid", selectMap);
    }

    @Override
    public String selectBankName(String loginAccount) {
        return this.getSqlSession().selectOne(this.mapperNamespace + ".selectBankName",loginAccount);
    }
}
