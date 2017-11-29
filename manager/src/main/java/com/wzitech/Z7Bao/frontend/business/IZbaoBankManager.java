package com.wzitech.Z7Bao.frontend.business;

import com.wzitech.Z7Bao.frontend.entity.ZbaoBank;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;

import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/23
 * 7bao银行管理信息
 */
public interface IZbaoBankManager {

    /**
     * 添加银行信息
     * @param zbaoBank
     */
    void add(ZbaoBank zbaoBank);

    /**
     * 修改银行信息
     * @param zbaoBank
     */
    void update(ZbaoBank zbaoBank);

    /**
     * 删除银行信息
     * @param id
     */
    void delete(Long id);

    /**
     * 查询银行信息
     * @param paramMap
     * @param limit
     * @param startIndex
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<ZbaoBank> queryPage(Map<String, Object> paramMap, int limit, int startIndex, String orderBy,
    Boolean isAsc);
    /**
     * 禁用配置
     * @param id
     */
    void disableUser(Long id);


    /**
     * 启用配置
     * @param id
     */
    void qyUser(Long id);

     ZbaoBank  selectByIdBank(Long id);

    /**
     * 根据银行名,查图面路径
     */
    ZbaoBank selectByName(int code);

    List<Map<String,Object>> selectBankNames();

}
