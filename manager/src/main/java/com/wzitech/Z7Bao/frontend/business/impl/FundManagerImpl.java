package com.wzitech.Z7Bao.frontend.business.impl;

import com.kb5173.framework.shared.exception.BusinessException;
import com.wzitech.Z7Bao.frontend.business.*;
import com.wzitech.Z7Bao.frontend.dao.rdb.ISystemConfigDBDAO;
import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoFundDetailDBDAO;
import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoPayOrderDAO;
import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoTransferDAO;
import com.wzitech.Z7Bao.frontend.dao.redis.IZbaoPayOrderRedisDao;
import com.wzitech.Z7Bao.frontend.entity.*;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.*;
import com.wzitech.gamegold.common.utils.DateUtil;
import com.wzitech.gamegold.usermgmt.dao.rdb.ISevenBaoAccountDBDAO;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by 340082 on 2017/8/18.
 */
@Component("FundManagerImpl")
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class FundManagerImpl implements IFundManager {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISevenBaoAccountDBDAO sevenBaoAccountDBDAO;

    @Autowired
    private IZbaoPayOrderDAO zbaoPayOrderDAO;

    @Autowired
    private IZbaoFundDetailDBDAO zbaoFundDetailDBDAO;

    @Autowired
    private IZbaoPayOrderRedisDao zbaoPayOrderRedisDao;

    @Autowired
    private ISystemConfigDBDAO systemConfigDBDAO;

    @Autowired
    private IZbaoTransferDAO zbaoTransferDAO;

    @Autowired
    private ISwiftpassPayManager swiftpassPayManager;

    @Autowired
    private IHeePayManager heePayManager;

    @Autowired
    private IZbaoPayOrderRedisDao payOrderRedisDao;
    @Autowired
    private ISystemConfigManager systemConfigManager;

    @Value("${7bao.recharge.title}")
    private String rechargeTitle = "7宝账户充值";

    @Autowired
    private ITenPayManager tenPayManager;

    //充值单最大有效支付时间
    private final int MAX_PAY_ORDER_LIVE_MINUTE = 10;


    /**
     * 查询充值状态接口
     *
     * @param outOrderId
     * @return
     */
    public Integer getPayOrderStatus(String outOrderId, String userId) {
        ZbaoPayOrder zbaoPayOrder = zbaoPayOrderDAO.selectUniqueByProp("zzOrderId", outOrderId);
        if (zbaoPayOrder == null) {
            throw new SystemException(ResponseCodes.EmptyTransferState.getCode(), ResponseCodes.EmptyTransferState.getMessage());
        }
        return zbaoPayOrder.getStatus();
    }

    public ZbaoPayOrder getPayOrderInfo(String orderId) {
        ZbaoPayOrder zbaoPayOrder = null;
        try {
            zbaoPayOrder = zbaoPayOrderDAO.selectOrder(orderId);
        } catch (Exception e) {
            logger.info("获取充值订单信息失败，" + e.getMessage());
        }
        return zbaoPayOrder;
    }

    /**
     * 充值接口
     *
     * @param uid
     * @param amount
     * @param outOrderId
     * @param payType
     * @return
     */
    @Override
    @Transactional
    public String creatRecharge(String uid, BigDecimal amount, String outOrderId, String jbOrderId, Integer payType, List<JBPayOrderTo7BaoEO> jbTo7Bao) {
        SevenBaoAccountInfoEO updateEo = sevenBaoAccountDBDAO.selectByUId(uid, true);
        logger.info("当前用户信息为：{}", updateEo);
        //判断当前用户是匹配
        if (updateEo == null) {
            throw new SystemException(ResponseCodes.EmptyUserOrNotAccount.getCode(), ResponseCodes.EmptyUserOrNotAccount.getMessage());
        }
        if (StringUtils.isNotBlank(outOrderId)) {
            //更新资金操作
            ZbaoPayOrder zbaoPayOrder = zbaoPayOrderDAO.selectUniqueByProp("zzOrderId", outOrderId);
            if (zbaoPayOrder != null && zbaoPayOrder.getCreateTime() != null) {
                throw new SystemException(ResponseCodes.NotUniqueZZorderId.getCode(), ResponseCodes.NotUniqueZZorderId.getMessage());
            }
        }
        String orderId = addPayOrderAndDetail(amount, outOrderId, jbOrderId, payType, updateEo, jbTo7Bao);
        if (ZBaoPayType.etch.getCode() == payType) {
            tenPayManager.updateFeeBothInZbaoAndGameGold(amount, updateEo, outOrderId);
        }
        return orderId;
    }

    /**
     * 插入支付单以及资金明细表
     *
     * @param amount
     * @param zzOrderId
     * @param payType
     * @param updateEo
     * @return
     */
    public String addPayOrderAndDetail(BigDecimal amount, String zzOrderId, String jbOrderId, Integer payType, SevenBaoAccountInfoEO updateEo, List<JBPayOrderTo7BaoEO> jbTo7Bao) {
        //将当前信息写入日志表中
        ZbaoPayOrder payOrder = new ZbaoPayOrder();
        String orderId = zbaoPayOrderRedisDao.getRechargeOrderId();
        payOrder.setOrderId(orderId);
        payOrder.setCreateTime(new Date());
        payOrder.setAmount(amount);
        payOrder.setLoginAccount(updateEo.getLoginAccount());
        payOrder.setZzOrderId(zzOrderId);

        if (payType.equals(ZBaoPayType.etch.getCode())) {
            BigDecimal totalAmount = updateEo.getTotalAmount();
            BigDecimal freezeAmount = updateEo.getFreezeAmount();
//            BigDecimal availableAmount = updateEo.getAvailableAmount();
            totalAmount = totalAmount.add(amount);
            updateEo.setTotalAmount(totalAmount);
            updateEo.setAvailableAmount(totalAmount.subtract(freezeAmount));
            logger.info("开始更新支付单表：{}", updateEo);
            sevenBaoAccountDBDAO.update(updateEo);

            ZbaoFundDetail zbaoFundDetail = new ZbaoFundDetail();
            //添加资金明细表写入
            zbaoFundDetail.setCreateTime(new Date());
            zbaoFundDetail.setAmount(amount);
            zbaoFundDetail.setLoginAccount(updateEo.getLoginAccount());
            zbaoFundDetail.setCzOrderId(orderId);
            zbaoFundDetail.setIsSuccess(true);
            String log = String.format("【售得充值自动转充值单】充值订单号:%s,充值金额:%s,更新后总金额:%s,冻结金额:%s,可用金额:%s",
                    orderId, amount, updateEo.getTotalAmount(), updateEo.getFreezeAmount(),
                    updateEo.getAvailableAmount());
            zbaoFundDetail.setLog(log);
            zbaoFundDetail.setType(ZbaoFundDetailType.BUYANDFULL.getCode());
            zbaoFundDetail.setOutOrderId(jbOrderId);
            logger.info("开始添加金明细表：{}", zbaoFundDetail);
            zbaoFundDetailDBDAO.insert(zbaoFundDetail);
            payOrder.setStatus(FundType.RechargeSuccessfully.getCode());
        } else if (payType.equals(ZBaoPayType.oldfull.getCode()) && amount.compareTo(BigDecimal.ZERO)==1) {
            BigDecimal freezeAmount = updateEo.getFreezeAmount();
            BigDecimal availableAmount = updateEo.getAvailableAmount();
            BigDecimal totalAmount = updateEo.getTotalAmount();
            if (freezeAmount == null) {
                updateEo.setFreezeAmount(BigDecimal.ZERO);
            }
            if (totalAmount == null || totalAmount.equals(BigDecimal.ZERO)) {
                totalAmount = BigDecimal.ZERO;
            }
            if (availableAmount == null) {
                availableAmount = BigDecimal.ZERO;
            }

            for (JBPayOrderTo7BaoEO j : jbTo7Bao) {
                totalAmount = totalAmount.add(j.getBalance());
                availableAmount = availableAmount.add(j.getBalance());
                updateEo.setTotalAmount(totalAmount);
                updateEo.setAvailableAmount(availableAmount);
                ZbaoFundDetail zbaoFundDetail = new ZbaoFundDetail();
                //添加资金明细表写入
                zbaoFundDetail.setOutOrderId(j.getOldOrderId() + "==>" + j.getNewOrderId());
                zbaoFundDetail.setCreateTime(new Date());
                zbaoFundDetail.setAmount(j.getBalance());
                zbaoFundDetail.setLoginAccount(updateEo.getLoginAccount());
                zbaoFundDetail.setCzOrderId(orderId);
                zbaoFundDetail.setIsSuccess(true);
                String log = String.format("【老用户资金转新流程】充值订单号:%s,充值金额:%s,更新后总金额:%s,冻结金额:%s,可用金额:%s",
                        orderId, j.getBalance(), updateEo.getTotalAmount(), updateEo.getFreezeAmount(),
                        updateEo.getAvailableAmount());
                zbaoFundDetail.setLog(log);
                zbaoFundDetail.setType(ZbaoFundDetailType.OLDFULL.getCode());
                logger.info("开始添加金明细表：{}", zbaoFundDetail);
                zbaoFundDetailDBDAO.insert(zbaoFundDetail);

            }
            payOrder.setStatus(FundType.RechargeSuccessfully.getCode());
            sevenBaoAccountDBDAO.update(updateEo);

        } else if (payType.equals(ZBaoPayType.aliPay.getCode()) || payType.equals(ZBaoPayType.weixinPay.getCode())) {
            //添加资金明细表写入
            ZbaoFundDetail zbaoFundDetail = new ZbaoFundDetail();
            zbaoFundDetail.setCreateTime(new Date());
            zbaoFundDetail.setAmount(amount);
            zbaoFundDetail.setLoginAccount(updateEo.getLoginAccount());
            zbaoFundDetail.setCzOrderId(orderId);
            zbaoFundDetail.setIsSuccess(null);
            String log = String.format("【手动充值单】充值订单号:%s,充值金额:%s,更新后总金额:%s,冻结金额:%s,可用金额:%s",
                    orderId, amount, updateEo.getTotalAmount(), updateEo.getFreezeAmount(),
                    updateEo.getAvailableAmount());
            zbaoFundDetail.setLog(log);
            zbaoFundDetail.setType(ZbaoFundDetailType.FULLMONEY.getCode());
            logger.info("开始添加金明细表：{}", zbaoFundDetail);
            zbaoFundDetailDBDAO.insert(zbaoFundDetail);
            payOrder.setStatus(FundType.Recharge.getCode());
            SystemConfig systemConfig = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.PAY_CHANNEL.getKey());
            PayChannelType payChannelType = null;
            try {
                payChannelType = PayChannelType.getChannelByType(Integer.valueOf(systemConfig.getConfigValue()));
            } catch (NullPointerException nuEx) {
                logger.info("获取支付渠道配置失败，使用默认渠道【威富通】");
                payChannelType = null;
            }
//            PayChannelType payChannelType = PayChannelType.getChannelByType(payOrderRedisDao.getPayChannel());
            if (payChannelType == null) {
                //默认威富通
                payOrder.setPayChannel(PayChannelType.swiftpass.getType());
            } else {
                payOrder.setPayChannel(payChannelType.getType());
            }
        } else {
            throw new SystemException(ResponseCodes.ErrorPayType.getCode(), ResponseCodes.ErrorPayType.getMessage());
        }
        payOrder.setOrderType(payType);
        if (StringUtils.isNotBlank(jbOrderId)) {
            payOrder.setOutOrderId(jbOrderId);
        }
        zbaoPayOrderDAO.insert(payOrder);
        return orderId;
    }

    /**
     * 以冻结资金扣款接口
     *
     * @param uid
     * @param amount
     * @param outOrderId
     * @return
     */
    @Override
    @Transactional
    public String creatTransferDeduction(String uid, BigDecimal amount, String outOrderId) {

        SevenBaoAccountInfoEO updateEo = sevenBaoAccountDBDAO.selectByUId(uid, true);
        logger.info("当前用户信息为：{}", updateEo);
        //更新资金操作
        BigDecimal totalAmount = updateEo.getTotalAmount();
        BigDecimal availableAmount = updateEo.getAvailableAmount();
        BigDecimal freezeAmount = updateEo.getFreezeAmount();
        //进行资金减少操作 判断当前资金是否足够扣除
        SystemConfig systemConfig = systemConfigDBDAO.selectByConfigKey(SystemConfigEnum.BALANCE_STOP_LINE.getKey());
        if (totalAmount.subtract(new BigDecimal(systemConfig.getConfigValue())).compareTo(amount) < 0) {
            throw new SystemException(ResponseCodes.FundShortage.getCode(), ResponseCodes.FundShortage.getMessage());
            //扣除冻结资金
        } else if (freezeAmount.compareTo(amount) < 0) {
            throw new SystemException(ResponseCodes.FundShortage.getCode(), ResponseCodes.FundShortage.getMessage());
        }
        //进行冻结资金减少操作
        totalAmount = totalAmount.subtract(amount);
        freezeAmount = freezeAmount.subtract(amount);
        availableAmount = totalAmount.subtract(freezeAmount);
        //设置资金
        updateEo.setTotalAmount(totalAmount);
        updateEo.setFreezeAmount(freezeAmount);
        updateEo.setAvailableAmount(availableAmount);
        //扣除当前的7bao账号的余额日志写入
        ZbaoTransfer zt = new ZbaoTransfer();
        String orderId = zbaoPayOrderRedisDao.getTransferOrderid();
        zt.setCreateTime(new Date());
        zt.setLoginAccount(updateEo.getLoginAccount());
        zt.setAmount(amount);
        zt.setOrderId(orderId);
        zt.setShOrderId(outOrderId);
        zt.setDealTime(new Date());
        zt.setStatus(ZbaoTransferType.TransferSuccess.getCode());
        //创建资金明细并插入
        ZbaoFundDetail zf = new ZbaoFundDetail();
        zf.setType(ZbaoFundDetailType.TRANSFER.getCode());
        zf.setCreateTime(new Date());
        zf.setCgOrderId(orderId);
        zf.setIsSuccess(true);
        zf.setOutOrderId(outOrderId);
        zf.setLoginAccount(updateEo.getLoginAccount());
        zf.setAmount(amount);
        String log = String.format("【转账扣款单】转账订单号:%s,扣款金额:%s,当前总金额:%s,冻结金额:%s,可用金额:%s",
                orderId, amount, updateEo.getTotalAmount(), updateEo.getFreezeAmount(),
                updateEo.getAvailableAmount());
        zf.setLog(log);
        logger.info("开始更新采购转账单表：{}", zt);
        logger.info("开始更新转账扣款单表：{}", zf);
        zbaoTransferDAO.insert(zt);
        zbaoFundDetailDBDAO.insert(zf);
        sevenBaoAccountDBDAO.update(updateEo);

        return orderId;
    }

    /**
     * 充值成功后业务处理
     *
     * @param orderId        7bao订单号
     * @param rechargeAmount 充值金额
     * @param outTradeNo     威富通交易号
     * @return
     */
    @Override
    @Transactional
    public boolean rechargeComplete(String orderId, BigDecimal rechargeAmount, String outTradeNo) throws BusinessException {
        ZbaoPayOrder zbaoPayOrder = zbaoPayOrderDAO.selectByOrderIdForUpdate(orderId, true);
        if (zbaoPayOrder == null) {
            logger.info("充值完成，订单{}不存在", orderId);
            return false;
        }
        SevenBaoAccountInfoEO sevenBaoAccountInfoEO = sevenBaoAccountDBDAO.selectAccount(zbaoPayOrder.getLoginAccount());
        //加锁更新
        SevenBaoAccountInfoEO updateEo = sevenBaoAccountDBDAO.selectByUId(sevenBaoAccountInfoEO.getUid(), true);
        if (zbaoPayOrder.getStatus() == FundType.RechargeSuccessfully.getCode()) {
            logger.info("充值处理已处理完成");
            return false;
        }
        if (zbaoPayOrder == null) {
            throw new BusinessException("充值单不存在");
        }
        if (zbaoPayOrder.getAmount().compareTo(rechargeAmount) != 0) {
            throw new BusinessException("充值金额不一致");
        }
        zbaoPayOrder.setDealTime(new Date());
        if (outTradeNo != null) {
            zbaoPayOrder.setOutOrderId(outTradeNo);
        }
        zbaoPayOrder.setStatus(FundType.RechargeSuccessfully.getCode());
        int update = zbaoPayOrderDAO.update(zbaoPayOrder);

        //更新用户资金
        BigDecimal totalAmount = updateEo.getTotalAmount();
        BigDecimal availableAmount = updateEo.getAvailableAmount();
        updateEo.setAvailableAmount(availableAmount.add(rechargeAmount));
        updateEo.setTotalAmount(totalAmount.add(rechargeAmount));
        int updateAccRow = sevenBaoAccountDBDAO.update(updateEo);
//        boolean updateRes = updateGoldBeanAccount(updateEo.getLoginAccount(), rechargeAmount);
//        if (!updateRes) {
//            logger.info("更新金币账户资金失败，数据回滚中，订单号：" + orderId);
//            throw new BusinessException("更新金币账户资金失败");
//        }
        tenPayManager.updateFeeBothInZbaoAndGameGold(rechargeAmount, updateEo, orderId);
        ZbaoFundDetail oldDetail = zbaoFundDetailDBDAO.selectLoginCz(zbaoPayOrder.getLoginAccount(), zbaoPayOrder.getOrderId());
        String detailLog = oldDetail.getLog();
        String newLog = null;
        if (StringUtils.isNotBlank(detailLog)) {
            String log = "【手动充值单】充值订单号:%s,充值金额:%s,更新后总金额:%s,冻结金额:%s,可用金额:%s";
            Pattern r = Pattern.compile("(:[1-9]\\d*\\.?\\d*)|(:0\\.\\d*[1-9])|(:0)");
            Matcher m = r.matcher(detailLog);
            int index = 0;
            BigDecimal afterUpdate = null;
            BigDecimal availAmount = null;
            String freezenAmount = null;
            while (m.find()) {
                index = index + 1;
                String t = m.group(0).trim().replaceAll(":", "");
                if (index == 2) {
                    afterUpdate = new BigDecimal(t).add(rechargeAmount);
                } else if (index == 3) {
                    freezenAmount = t;
                } else if (index == 4) {
                    availAmount = new BigDecimal(t).add(rechargeAmount);
                }
            }
            newLog = String.format(log, oldDetail.getCzOrderId(), rechargeAmount, afterUpdate, freezenAmount, availAmount);
        }
        zbaoFundDetailDBDAO.changeRechargeStatus(zbaoPayOrder.getOrderId(), newLog, true);
        return true;
//        if (update == 1 && insertRow == 1L && updateAccRow == 1) {
//            return true;
//        } else {
//            throw new BusinessException("更新充值单状态失败");
//        }
    }

    /**
     * 根据订单构建充值二维码链接
     *
     * @param orderId
     * @return
     */
    @Override
    public Map<String, Object> buildPayUrl(String orderId) {
        ZbaoPayOrder zbaoPayOrder = zbaoPayOrderDAO.selectByOrderIdForUpdate(orderId, false);
        if (zbaoPayOrder == null) {
            throw new BusinessException("充值单不存在");
        }
        Integer status = zbaoPayOrder.getStatus();
        if (status != FundType.Recharge.getCode()) {
            throw new BusinessException("该订单已支付成功或已取消");
        }
        if (DateUtil.distanceTime(zbaoPayOrder.getCreateTime(), new Date(), TimeUnit.MINUTES) > MAX_PAY_ORDER_LIVE_MINUTE) {
            //订单已过期
            invalidPayOrder(zbaoPayOrder.getId(), orderId);
            throw new BusinessException("该订单已取消");
        }
        Integer amount = zbaoPayOrder.getAmount().multiply(new BigDecimal(100)).intValue();
        ZBaoPayType payType = ZBaoPayType.getTypeByCode(zbaoPayOrder.getOrderType());
        PayChannelType channelType = PayChannelType.getChannelByType(zbaoPayOrder.getPayChannel());
        if (channelType == null) {
            channelType = PayChannelType.swiftpass;
        }
        String url = null;
        if (channelType.getType() == PayChannelType.heepay.getType()) {
            //汇付宝
            url = heePayManager.getPayUrl(orderId, rechargeTitle, amount, payType);
        } else if (channelType.getType() == PayChannelType.swiftpass.getType()) {
            //威富通
            url = swiftpassPayManager.getUrl(orderId, rechargeTitle, amount, payType);
        }
        logger.info("构建支付URL，支付渠道{},url:{}", channelType.getName(), url);
        Date createTime = zbaoPayOrder.getCreateTime();
        Date now = new Date();
        int liveTime = (int) (now.getTime() - createTime.getTime()) / 1000;
        Map<String, Object> result = new HashedMap();
        result.put("url", url);
        result.put("liveTime", liveTime);
        result.put("createTimestamp", createTime.getTime());
        result.put("payChannel", zbaoPayOrder.getPayChannel());
        result.put("payType",zbaoPayOrder.getOrderType());
        return result;
    }

    /**
     * 失效订单
     *
     * @param id      支付单id
     * @param orderId 支付单订单号
     */
    private void invalidPayOrder(long id, String orderId) {
        ZbaoPayOrder upOrder = new ZbaoPayOrder();
        upOrder.setId(id);
        upOrder.setStatus(FundType.RechargeFailed.getCode());
        zbaoPayOrderDAO.update(upOrder);
        zbaoFundDetailDBDAO.changeRechargeStatus(orderId, null, false);
    }

    @Override
    public void closePayOrder(String orderId) {
        ZbaoPayOrder payOrder = zbaoPayOrderDAO.selectByOrderIdForUpdate(orderId, true);
        boolean closeResult = swiftpassPayManager.closeOrder(orderId);
        if (closeResult) {
            invalidPayOrder(payOrder.getId(), payOrder.getOrderId());
        } else {
            logger.info("关闭支付订单失败，单号{}", payOrder.getOrderId());
            throw new BusinessException("关闭订单失败");
        }
    }

    @Override
    public void checkOrder(ZbaoPayOrder payOrder) {
        if (payOrder == null) {
            logger.info("校验支付状态，订单为空");
            throw new BusinessException("订充值单不能为空");
        }
        if (payOrder.getPayChannel() == PayChannelType.swiftpass.getType()) {
            //威富通，先校验订单状态，再关闭
            SwiftpassPayStatus payStatus = null;
            try {
                payStatus = swiftpassPayManager.checkPayStatus(payOrder.getOrderId());
            } catch (BusinessException bs) {
                logger.info("检查订单支付状态，单号" + payOrder.getOrderId() + bs.getMessage());
                if (bs.getMessage().equals("订单不存在")) {
                    invalidPayOrder(payOrder.getId(), payOrder.getOrderId());
                }
            }
            if (SwiftpassPayStatus.SUCCESS.equals(payStatus)) {
                try {
                    rechargeComplete(payOrder.getOrderId(), payOrder.getAmount(), null);
                } catch (BusinessException e) {
                    logger.info("校验充值单是否成功，发生异常，异常信息：" + e.getMessage() + "订单号：" + payOrder.getOrderId());
                }
            } else if (SwiftpassPayStatus.CLOSED.equals(payStatus)) {
                invalidPayOrder(payOrder.getId(), payOrder.getOrderId());
            } else {
                boolean closeResult = swiftpassPayManager.closeOrder(payOrder.getOrderId());
                if (closeResult) {
                    invalidPayOrder(payOrder.getId(), payOrder.getOrderId());
                } else {
                    logger.info("关闭支付订单失败，单号{}", payOrder.getOrderId());
                }
            }
        } else if (payOrder.getPayChannel() == PayChannelType.heepay.getType()) {
            //汇付宝，查询支付状态，没支付成功取消订单
            boolean payStatus = heePayManager.heePayQuery(payOrder.getOrderId());
            if (!payStatus) {
                invalidPayOrder(payOrder.getId(), payOrder.getOrderId());
            }
        }
    }

    /**
     * 更新金币账户金额
     *
     * @param loginAccount
     * @param ammount      需要修改的金额，
     */
    /*@Override
    public boolean updateGoldBeanAccount(String loginAccount, BigDecimal ammount) {
        String money = ammount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        Map<String, String> param = new HashedMap();
        String sign = MD5.sign(money + loginAccount, ServicesContants.GOLD_BEAN_KEY, "utf-8");
        param.put("loginAccount", loginAccount);
        param.put("changeAmount", money);
        param.put("sign", sign);
        try {
            String result = HttpClientUtil.sendGetReq(ServicesContants.GOLD_BEAN_HOST + "/payOrder/updatePurchaseDataWithZbao", param);
            logger.info("增加金币账户资金返回：" + result);
            JSONObject jsonObject = JSONObject.fromObject(result);
            JSONObject responseStatus = jsonObject.getJSONObject("responseStatus");
            String code = responseStatus.getString("code");
            if (StringUtils.isNotBlank(code) && code.equals("00")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            logger.info("更新金币账户金额失败，" + ex.getMessage());
        }
        return false;
    }*/
}
