package com.wzitech.Z7Bao.frontend.dao.rdb;

import com.wzitech.Z7Bao.frontend.entity.ZbaoBank;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;

import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/23
 */
public interface IZbaoBankDBDAO extends IMyBatisBaseDAO<ZbaoBank, Long> {

    ZbaoBank selectByName(int code);

    List<Map<String,Object>> selectBankName();
}
