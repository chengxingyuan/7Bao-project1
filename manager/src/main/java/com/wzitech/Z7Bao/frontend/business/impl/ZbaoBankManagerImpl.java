package com.wzitech.Z7Bao.frontend.business.impl;

import com.wzitech.Z7Bao.frontend.business.IZbaoBankManager;
import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoBankDBDAO;
import com.wzitech.Z7Bao.frontend.entity.ZbaoBank;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/23
 * 7bao银行信息管理
 */
@Component
public class ZbaoBankManagerImpl implements IZbaoBankManager{
    @Autowired
    IZbaoBankDBDAO zbaoBankDBDAO;
    @Override
    public void add(ZbaoBank zbaoBank) {
        if (zbaoBank != null){
          zbaoBankDBDAO.insert(zbaoBank);
        }
    }

    @Override
    public void update(ZbaoBank zbaoBank) {
        if (zbaoBank != null){
//            ZbaoBank zbaoBanka = zbaoBankDBDAO.selectById(zbaoBank.getId());
            zbaoBankDBDAO.update(zbaoBank);
        }
    }

    @Override
    public void delete(Long id) {
        zbaoBankDBDAO.deleteById(id);

    }

    @Override
    public GenericPage<ZbaoBank> queryPage(Map<String, Object> paramMap, int limit, int startIndex, String orderBy, Boolean isAsc) {
        return zbaoBankDBDAO.selectByMap(paramMap,limit,startIndex,orderBy,isAsc);
    }

    @Override
    public void disableUser(Long id) {
        if (id != null){
            ZbaoBank zbaoBank = zbaoBankDBDAO.selectById(id);
            if (zbaoBank != null){
                zbaoBank.setEnable(false);
                zbaoBankDBDAO.update(zbaoBank);
            }
        }
    }

    @Override
    public void qyUser(Long id) {
        if (id != null){
            ZbaoBank zbaoBank = zbaoBankDBDAO.selectById(id);
            if (zbaoBank != null){
                zbaoBank.setEnable(true);
                zbaoBankDBDAO.update(zbaoBank);
            }
        }
    }

    @Override
    public ZbaoBank selectByIdBank(Long id) {
        ZbaoBank zbaoBank = new ZbaoBank();
        if (id != null ){
             zbaoBank = zbaoBankDBDAO.selectById(id);
        }
        return zbaoBank;
    }

    @Override
    public ZbaoBank selectByName(int code) {
        return zbaoBankDBDAO.selectByName(code);
    }

    @Override
    public List<Map<String, Object>> selectBankNames() {
        return zbaoBankDBDAO.selectBankName();
    }
}
