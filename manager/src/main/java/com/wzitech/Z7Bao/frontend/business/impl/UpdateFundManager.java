package com.wzitech.Z7Bao.frontend.business.impl;

import com.wzitech.Z7Bao.frontend.business.IUpdateFundManager;
import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoFundDetailDBDAO;
import com.wzitech.Z7Bao.frontend.dao.redis.IZbaoPayOrderRedisDao;
import com.wzitech.Z7Bao.frontend.entity.ZbaoFundDetail;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.ZbaoFundDetailType;
import com.wzitech.gamegold.usermgmt.business.ISevenBaoAccountManager;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chengXY on 2017/9/1.
 */
@Component
public class UpdateFundManager implements IUpdateFundManager {
    @Autowired
    private ISevenBaoAccountManager sevenBaoAccountManager;

    @Autowired
    IZbaoFundDetailDBDAO zbaoFundDetailDBDAO;

    @Autowired
    private IZbaoPayOrderRedisDao zbaoPayOrderRedisDao;

    /**
     * 冻结/解冻金额结算
     * 参数：被操作用户的 loginAccount,uid
     *      yesOrNo  1 冻结  2解冻 ； amount 要冻结或者解冻的金额
     * */
    @Override
    public void updateAmount(String loginAccount,String uid, int yesOrNo,BigDecimal amount,String orderId) {
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //查出7bao账号信息
        SevenBaoAccountInfoEO sevenBaoAccount = sevenBaoAccountManager.queryByAccountAndUid(loginAccount, uid);
        //总资金
        BigDecimal totalAmount = sevenBaoAccount.getTotalAmount();

        ZbaoFundDetail detail = new ZbaoFundDetail();
        detail.setLoginAccount(loginAccount);
        Date date = new Date();
        String formatdate = dateFormat.format(date);
        String log = null;
        //如果yesOrNo=1 是冻结该7bao账户资金 冻结金额是amount
        if (yesOrNo == 1) {
            if (amount.compareTo(sevenBaoAccount.getAvailableAmount())==1||amount.compareTo(BigDecimal.ZERO)==-1){
                throw new SystemException(ResponseCodes.FreezeException.getMessage());
            }
            //新的冻结金额 = 冻结金额 + 上amount
            BigDecimal freezeAmount = sevenBaoAccount.getFreezeAmount();
            freezeAmount = freezeAmount.add(amount.setScale(2, RoundingMode.DOWN));
            if (freezeAmount.compareTo(totalAmount)==1||amount.compareTo(BigDecimal.ZERO)==-1){
                throw new SystemException(ResponseCodes.FreezeException.getMessage());
            }
            sevenBaoAccount.setFreezeAmount(freezeAmount);
            //新的可用金额= 总金额-新的冻结金额
            BigDecimal availableAmout = totalAmount.subtract(freezeAmount.setScale(2, RoundingMode.DOWN));
            if (availableAmout.compareTo(totalAmount)==1||amount.compareTo(BigDecimal.ZERO)==-1){
                throw new SystemException(ResponseCodes.FreezeException.getMessage());
            }
            sevenBaoAccount.setAvailableAmount(availableAmout);
            //资金明细记录
            detail.setType(ZbaoFundDetailType.FREEZE.getCode());
            log = "【冻结资金】"+loginAccount+"在"+formatdate+"冻结了"+amount+"元,当前总金额"+totalAmount+",当前冻结金额"+freezeAmount+",当前可用金额"+availableAmout;
        }
        //yesOrNo=2 表示解冻
        if (yesOrNo == 2){
            if (amount.compareTo(sevenBaoAccount.getFreezeAmount())==1||amount.compareTo(BigDecimal.ZERO)==-1){
                throw new SystemException(ResponseCodes.FreezeException.getMessage());
            }
            //新的冻结金额 = 冻结金额 -amount
            BigDecimal freezeAmount = sevenBaoAccount.getFreezeAmount();
            freezeAmount = freezeAmount.subtract(amount.setScale(2, RoundingMode.DOWN));
            if (freezeAmount.compareTo(totalAmount)==1 || freezeAmount.compareTo(BigDecimal.ZERO)==-1){
                throw new SystemException(ResponseCodes.FreezeException.getMessage());
            }
            sevenBaoAccount.setFreezeAmount(freezeAmount);
            //新的可用金额= 总金额-新的冻结金额
            BigDecimal availableAmout = totalAmount.subtract(freezeAmount.setScale(2, RoundingMode.DOWN));
            if (availableAmout.compareTo(totalAmount)==1 || availableAmout.compareTo(BigDecimal.ZERO)==-1){
                throw new SystemException(ResponseCodes.FreezeException.getMessage());
            }
            sevenBaoAccount.setAvailableAmount(availableAmout);
            //资金明细记录
            detail.setType(ZbaoFundDetailType.UNFREEZE.getCode());
            log = "【解冻资金】"+loginAccount+"在"+formatdate+"解冻了"+amount+"元,当前总金额"+totalAmount+",当前冻结金额"+freezeAmount+",当前总金额"+totalAmount;
        }
        try {
            sevenBaoAccountManager.updateBind(sevenBaoAccount);
            String freezeOrderId = zbaoPayOrderRedisDao.getFreezeOrderId();
            detail.setFreezeOrderId(freezeOrderId);
            detail.setAmount(amount);
            detail.setIsSuccess(true);
            detail.setLog(log);
            detail.setCreateTime(date);
            detail.setOutOrderId(orderId);
            zbaoFundDetailDBDAO.insert(detail);
        }catch (Exception e){
            throw new SystemException("冻结/解冻该7bao账号资金出错{}：",loginAccount);
        }

    }
}
