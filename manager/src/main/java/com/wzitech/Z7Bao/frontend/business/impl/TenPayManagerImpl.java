package com.wzitech.Z7Bao.frontend.business.impl;

import com.wzitech.Z7Bao.frontend.business.ITenPayManager;
import com.wzitech.Z7Bao.frontend.dao.rdb.*;
import com.wzitech.Z7Bao.frontend.dao.redis.IWithdrawRedisDao;
import com.wzitech.Z7Bao.frontend.dto.WithDrawRequestDTO;
import com.wzitech.Z7Bao.frontend.entity.*;
import com.wzitech.Z7Bao.util.*;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import com.wzitech.gamegold.common.enums.ZbaoFundDetailType;
import com.wzitech.gamegold.common.enums.ZbaoWithdrawTypeEnum;
import com.wzitech.gamegold.common.utils.StreamIOHelper;
import com.wzitech.gamegold.usermgmt.dao.rdb.ISevenBaoAccountDBDAO;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/17  wurongfan           ZW_C_JB_00021 商城资金7bao改造
 */
@Component
public class TenPayManagerImpl implements ITenPayManager {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IZbaoWithdrawalsDBDAO zbaoWithdrawalsDBDAO;  //7宝支付单处理


    @Autowired
    private ISevenBaoAccountDBDAO sevenBaoAccountDBDAO;

    @Autowired
    private IWithdrawRedisDao withdrawRedisDao;

    @Value("${zbaoMd5Key}")
    private String zbaoMd5Key = "";

    @Value("${purchasedataurlUpdate}")
    private String updateUrl = "";


    @Value("${purchasedataurl}")
    private String addOrderUrl = "";

    @Value("${deletePayOrderURL}")
    private String deletePayOrderUrl = "";

    @Value("${caPath}")
    private String caPath = "";

    @Value("${certPath}")
    private String certPath = "";

    @Autowired
    IZbaoServiceAmountConfigDao zbaoServiceAmountConfigDao;

    @Autowired
    IZbaoFundDetailDBDAO zbaoFundDetailDBDAO;

    @Autowired
    IZbaoCityDBDAO zbaoCityDBDAO;

    @Autowired
    private ISystemConfigDBDAO systemConfigDBDAO;

    /**
     * @param withDrawRequestDTO 封装了提现的参数
     * @return 返回一个封装了状态信息的caifutong提供的jar包中的类
     */
    @Override
    @Transactional
    public Alipay withDraw(WithDrawRequestDTO withDrawRequestDTO) {
        SevenBaoAccountInfoEO sevenBaoAccountInfoEO = sevenBaoAccountDBDAO.selectUniqueByProp("loginAccount", withDrawRequestDTO.getLoginAccount());
        if (sevenBaoAccountInfoEO == null) {
            throw new SystemException(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
        }
        //提现开户行人名与登录账号登记人名不一致的 抛异常
        if (!sevenBaoAccountInfoEO.getName().equals(withDrawRequestDTO.getAccoutName())) {
            throw new SystemException(ResponseCodes.NullAccountName.getCode(), ResponseCodes.NullAccountName.getMessage());
        }
        //从金币商城去查询可用余额与zbao表中的余额是否一致
        JSONObject repJson = null;
        try {
            repJson = getJsonObject(withDrawRequestDTO.getLoginAccount());
        } catch (IOException e) {
            e.printStackTrace();
            throw new SystemException(ResponseCodes.FailToCreateConnect.getCode(), ResponseCodes.FailToCreateConnect.getMessage());
        }
        String responseStatusStr = repJson.getString("responseStatus");
        if (StringUtils.isBlank(responseStatusStr)) {
            throw new SystemException(ResponseCodes.NullResponseFromGameGoldCrossingSelect.getCode(), ResponseCodes.NullResponseFromGameGoldCrossingSelect.getMessage());
        }
        JSONObject jo = JSONObject.fromObject(responseStatusStr);
        if (jo.getString("code").equals("00")) {
            String responseOrderInfo = repJson.getString("purchaserData");
            JSONObject orderInofoJo = JSONObject.fromObject(responseOrderInfo);
            String avalieable = orderInofoJo.getString("availableAmountZBao");
            BigDecimal avaliebleFeeFromGameGold = new BigDecimal(avalieable);
            String totalAmount = orderInofoJo.getString("totalAmountZBao");
            BigDecimal totalAmountFromPurchaseData = new BigDecimal(totalAmount);
            //不一致报错
            if (avaliebleFeeFromGameGold.compareTo(sevenBaoAccountInfoEO.getAvailableAmount()) != 0) {
                throw new SystemException(ResponseCodes.NotEqual.getCode(), ResponseCodes.NotEqual.getMessage());
            }
            if (totalAmountFromPurchaseData.compareTo(sevenBaoAccountInfoEO.getTotalAmount()) != 0) {
                throw new SystemException(ResponseCodes.NotEqual.getCode(), ResponseCodes.NotEqual.getMessage());
            }
        } else {
            logger.info("查询金币商城采购商数据失败,失败编码:" + jo.getString("code") + "失败原因:" + jo.getString("message"));
            throw new SystemException(ResponseCodes.NoPurchaseData.getCode(), ResponseCodes.NoPurchaseData.getMessage());
        }
        if (withDrawRequestDTO.getWithDrawMoney() == null || withDrawRequestDTO.getWithDrawMoney().compareTo(BigDecimal.ZERO) < 0) {
            throw new SystemException(ResponseCodes.FeeError.getCode(), ResponseCodes.FeeError.getMessage());
        }
        //可提现额减去配置的押金金额,为开通收货可提现金额
        SystemConfig systemConfig = systemConfigDBDAO.selectByConfigKey(SystemConfigEnum.BALANCE_STOP_LINE.getKey());
        BigDecimal deliverwithDrawCantGetFiveHundredFirst = sevenBaoAccountInfoEO.getAvailableAmount().subtract(new BigDecimal(systemConfig.getConfigValue()));
        //开通收货可提现金额比提现金额少的报错
        if (sevenBaoAccountInfoEO.getIsShBind() == true && deliverwithDrawCantGetFiveHundredFirst.compareTo(withDrawRequestDTO.getWithDrawMoney()) < 0) {
            throw new SystemException(ResponseCodes.NotEnoughFee.getCode(), ResponseCodes.NotEnoughFee.getMessage());
        }
        //未开通收货的  可提现金额小于提现金额的报错
        if (sevenBaoAccountInfoEO.getIsShBind() == false && sevenBaoAccountInfoEO.getAvailableAmount().compareTo(withDrawRequestDTO.getWithDrawMoney()) < 0) {
            throw new SystemException(ResponseCodes.NotEnoughFee.getCode(), ResponseCodes.NotEnoughFee.getMessage());
        }
        BigDecimal serviceFee = null;
        //查询手续费标准
        List<ZbaoServiceAmountConfig> list = zbaoServiceAmountConfigDao.selectAll();
        for (ZbaoServiceAmountConfig zbaoServiceAmountConfig : list) {
            //配置的是 0-2500 2500.01-10000 10000.01-9999999 不会出现重复的区间 所以可以取=号
            if (zbaoServiceAmountConfig.getMaxAmount().compareTo(withDrawRequestDTO.getWithDrawMoney()) >= 0 && zbaoServiceAmountConfig.getMinAmount().compareTo(withDrawRequestDTO.getWithDrawMoney()) <= 0) {
                serviceFee = withDrawRequestDTO.getWithDrawMoney().multiply(zbaoServiceAmountConfig.getRate()).setScale(2, RoundingMode.DOWN);
                if (serviceFee.compareTo(zbaoServiceAmountConfig.getMinServiceAmount()) <= 0) {
                    serviceFee = zbaoServiceAmountConfig.getMinServiceAmount().setScale(2, RoundingMode.DOWN);
                }
                //最低手续费必然不会高于最高手续费 所以只有两种情况 比小的小 比大的大
                // 如果出现最小手续费等于最大手续费时 说明服务费该取固定值了 这个值就是最大/最小值 取其中一个就可以 因为和一个数字比较 要么小于等于 要么大于等于 总是会运行其中的一个的  这里取最大值放在后面 所以是取最大值
                if (serviceFee.compareTo(zbaoServiceAmountConfig.getMaxServiceAmount()) >= 0) {
                    serviceFee = zbaoServiceAmountConfig.getMaxServiceAmount().setScale(2, RoundingMode.DOWN);
                }
            }
        }
        //提现金额为空或减去手续费小于0报错,最少提现3元
        if (null == withDrawRequestDTO.getWithDrawMoney() || withDrawRequestDTO.getWithDrawMoney().compareTo(serviceFee) <= 0 || withDrawRequestDTO.getWithDrawMoney().compareTo(new BigDecimal(3)) < 0) {
            throw new SystemException(ResponseCodes.FeeError.getCode(), ResponseCodes.FeeError.getMessage());
        }
        //银行名
        if (StringUtils.isBlank(withDrawRequestDTO.getBankName())) {
            throw new SystemException(ResponseCodes.NullBankNameError.getCode(), ResponseCodes.NullBankNameError.getMessage());
        }
        //现在默认都是私人的卡,下面的判断暂时注销 若后期开启对公账户服务 再启用
        withDrawRequestDTO.setAccountProp("1");
        //账号属性 1 个人 2公司 当公司时 省 市 开户行必须有
//        if (StringUtils.isBlank(withDrawRequestDTO.getAccountProp())) {
//            throw new SystemException(ResponseCodes.NullAccountPropError.getCode(), ResponseCodes.NullAccountPropError.getMessage());
//        }
//        if (("2".equals(withDrawRequestDTO.getAccountProp())) && (StringUtils.isBlank(withDrawRequestDTO.getProvince()) || StringUtils.isBlank(withDrawRequestDTO.getCity()) || StringUtils.isBlank(withDrawRequestDTO.getSubBankName()))) {
//            throw new SystemException(ResponseCodes.CompanyAccountNeedsMoreInfo.getCode(), ResponseCodes.CompanyAccountNeedsMoreInfo.getMessage());
//        }

        String province = null;
        String city = null;
        if (StringUtils.isNotBlank(withDrawRequestDTO.getProvince()) && StringUtils.isNotBlank(withDrawRequestDTO.getCity()) && !"0".equals(withDrawRequestDTO.getProvince()) && !"0".equals(withDrawRequestDTO.getCity())) {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("provinceId", Integer.parseInt(withDrawRequestDTO.getProvince()));
            queryMap.put("cityId", Integer.parseInt(withDrawRequestDTO.getCity()));
            List<ZbaoCityEO> cityEOs = zbaoCityDBDAO.selectByMap(queryMap);
            if (cityEOs.size() == 1 && StringUtils.isNotBlank(cityEOs.get(0).getProvinceName()) && StringUtils.isNotBlank(cityEOs.get(0).getCityName())) {
                province = cityEOs.get(0).getProvinceName();
                city = cityEOs.get(0).getCityName();
            }
        }

        //如果开户地区信息为空 就提供0给财付通接口(目前都是私人卡 且不需要传给财付通，所以这里给0,如果以后开通对公业务 再启用)
//        if (StringUtils.isBlank(withDrawRequestDTO.getProvince())) {
        String provinceCode = withDrawRequestDTO.getProvince();
        withDrawRequestDTO.setProvince("0");
//        }
        //如果开户城市信息为空 也提供0给财付通接口
//        if (StringUtils.isBlank(withDrawRequestDTO.getCity())) {
        String cityCode = withDrawRequestDTO.getCity();
        withDrawRequestDTO.setCity("0");
//        }
        //如果开户行信息为空 就提供全角空串
//        if (StringUtils.isBlank(withDrawRequestDTO.getSubBankName())) {
        withDrawRequestDTO.setSubBankName("　");
//        }

        //银行账
        if (StringUtils.isBlank(withDrawRequestDTO.getAccountNo())) {
            throw new SystemException(ResponseCodes.NullAccountNOError.getCode(), ResponseCodes.NullAccountNOError.getMessage());
        }
        //开户人姓名
        if (StringUtils.isBlank(withDrawRequestDTO.getAccoutName())) {
            throw new SystemException(ResponseCodes.NullAccountName.getCode(), ResponseCodes.NullAccountName.getMessage());
        }
        //登录用户
        if (StringUtils.isBlank(withDrawRequestDTO.getLoginAccount())) {
            throw new SystemException(ResponseCodes.NotExistedUser.getCode(), ResponseCodes.NotExistedUser.getMessage());
        }

        Date date = new Date();
        String outTradeNo = withdrawRedisDao.getWithDrawOrderId();
        Alipay BQR = new Alipay();
        DirectTransClientRequestHandler reqHandler =
                new DirectTransClientRequestHandler(null, null);
        reqHandler.init();
        reqHandler.setKey(TenpayUtil.SIGN);

        String client_ip = "61.130.29.5";
        //减掉手续费 之后才是真正提出的金额
        Integer fee = (withDrawRequestDTO.getWithDrawMoney().subtract(serviceFee).multiply(new BigDecimal(100))).intValue();
        String DetailData = "<record>" +
                "<serial>" + outTradeNo + "last" + "</serial>" +
                "<rec_bankacc>" + withDrawRequestDTO.getAccountNo() + "</rec_bankacc>" +
                "<bank_type>" + withDrawRequestDTO.getBankName() + "</bank_type>" +
                "<rec_name>" + withDrawRequestDTO.getAccoutName() + "</rec_name>" +
                "<pay_amt>" + fee + "</pay_amt>" +
                "<acc_type>" + withDrawRequestDTO.getAccountProp() + "</acc_type>" +
                "<area>" + withDrawRequestDTO.getProvince() + "</area>" +
                "<city>" + withDrawRequestDTO.getCity() + "</city>" +
                "<subbank_name>" + withDrawRequestDTO.getSubBankName() + "</subbank_name>" +
                "<desc>" + withDrawRequestDTO.getDesc() + "</desc>" +
                "</record>";
        reqHandler.setParameter("encoding", "GBK");
        reqHandler.setParameter("op_code", "1013");
        reqHandler.setParameter("op_name", "batch_draw");
        reqHandler.setParameter("op_user", TenpayUtil.OP_USER);
        reqHandler.setParameter("op_passwd", MD5Util.MD5Encode(TenpayUtil.PASSWD, "GBK"));
        String commitTime = TenpayUtil.getCurrTime();
        reqHandler.setParameter("op_time", commitTime);
        reqHandler.setParameter("sp_id", TenpayUtil.PARTNER);
        reqHandler.setParameter("package_id", outTradeNo);
        reqHandler.setParameter("total_num", "1");
        reqHandler.setParameter("total_amt", fee.toString());
        reqHandler.setParameter("client_ip", client_ip);
        reqHandler.setParameter("record_set", DetailData);


        String reqUrl = null;
        try {
            reqUrl = reqHandler.getRequestURL();
        } catch (UnsupportedEncodingException e) {
            throw new SystemException(ResponseCodes.FailToConntectCaiFuTong.getCode(), ResponseCodes.FailToConntectCaiFuTong.getMessage());
        }

        TenpayHttpClient httpClient = new TenpayHttpClient();
        httpClient.setReqContent(reqUrl);

        httpClient.setCaInfo(new File(caPath));
        httpClient.setCertInfo(new File(certPath), TenpayUtil.CERPASS);

        httpClient.setMethod("POST");

        //初始金额扣除提现金额 先把钱扣掉 把包括手续费的钱一起扣掉 如果失败再加回来
        BigDecimal orignalAmount = sevenBaoAccountInfoEO.getAvailableAmount().setScale(2, BigDecimal.ROUND_DOWN);
        BigDecimal orignalTotalAmount = sevenBaoAccountInfoEO.getTotalAmount();
        BigDecimal newBigDecimal = orignalAmount.subtract(withDrawRequestDTO.getWithDrawMoney().setScale(2, BigDecimal.ROUND_DOWN));
        //先扣七宝 需要同时扣除可用资金
        sevenBaoAccountInfoEO.setAvailableAmount(newBigDecimal);
        //再扣除掉总资金
        sevenBaoAccountInfoEO.setTotalAmount(sevenBaoAccountInfoEO.getTotalAmount().subtract(withDrawRequestDTO.getWithDrawMoney()).setScale(2, BigDecimal.ROUND_DOWN));
        sevenBaoAccountDBDAO.update(sevenBaoAccountInfoEO);
        //再扣商城,因为是扣款所以是个负数
        BigDecimal changeForGameGold = BigDecimal.ZERO.subtract(withDrawRequestDTO.getWithDrawMoney());
        updateFeeBothInZbaoAndGameGold(changeForGameGold, sevenBaoAccountInfoEO, outTradeNo);
        try {
            //设置通用日志记录信息
            ZbaoWithdrawals zbaoWithdrawals = new ZbaoWithdrawals();
            zbaoWithdrawals.setOrderId(outTradeNo);
            zbaoWithdrawals.setLoginAccount(withDrawRequestDTO.getLoginAccount());
            //记录扣除手续费的金额
            zbaoWithdrawals.setAmount(withDrawRequestDTO.getWithDrawMoney().subtract(serviceFee));
            zbaoWithdrawals.setServiceAmount(serviceFee);
            zbaoWithdrawals.setFundOnWay(withDrawRequestDTO.getWithDrawMoney());
            zbaoWithdrawals.setCreateTime(date);
            zbaoWithdrawals.setBankName(BankTypeEnum.getDescByType(Integer.parseInt(withDrawRequestDTO.getBankName())));
            zbaoWithdrawals.setBankNameType(Integer.parseInt(withDrawRequestDTO.getBankName()));
            zbaoWithdrawals.setCardCode(withDrawRequestDTO.getAccountNo());
            zbaoWithdrawals.setRealName(withDrawRequestDTO.getAccoutName());
            zbaoWithdrawals.setProvince(provinceCode);
            zbaoWithdrawals.setCity(cityCode);
            zbaoWithdrawals.setProvinceName(province);
            zbaoWithdrawals.setCityName(city);
            zbaoWithdrawals.setOpenbank(withDrawRequestDTO.getSubBankName());
            zbaoWithdrawals.setAccountProp(withDrawRequestDTO.getAccountProp());
            zbaoWithdrawals.setType(ZbaoWithdrawTypeEnum.LOADING.getStatu());
            StringBuffer stringBuffer = new StringBuffer("用户" + withDrawRequestDTO.getLoginAccount() + "申请提现" + withDrawRequestDTO.getWithDrawMoney() + "元,到银行卡" + withDrawRequestDTO.getAccountNo() + "含手续费" + serviceFee + "元" + "财付通已成功受理,正等待银行返回结果");
            zbaoWithdrawals.setLog(stringBuffer.toString());
            zbaoWithdrawalsDBDAO.insert(zbaoWithdrawals);

            //资金明细记录
            ZbaoFundDetail zbaoFundDetail = new ZbaoFundDetail();
            zbaoFundDetail.setLoginAccount(withDrawRequestDTO.getLoginAccount());
            zbaoFundDetail.setType(ZbaoFundDetailType.WITHDRAWALS.getCode());
            zbaoFundDetail.setTxOrderId(outTradeNo);
            zbaoFundDetail.setAmount(withDrawRequestDTO.getWithDrawMoney());
            zbaoFundDetail.setCreateTime(new Date());
            String log = "【提现单】提现单号" + outTradeNo + ",提现金额" + withDrawRequestDTO.getWithDrawMoney() +
                    "元,含手续费" + serviceFee + "元," + "当前总金额" + sevenBaoAccountInfoEO.getTotalAmount() +
                    ",冻结金额" + sevenBaoAccountInfoEO.getFreezeAmount() + ",可用金额" + newBigDecimal;
            zbaoFundDetail.setLog(log);
            zbaoFundDetailDBDAO.insert(zbaoFundDetail);
        } catch (Exception e) {
            logger.error("处理日志信息失败,还原商城资金,金额:" + withDrawRequestDTO.getWithDrawMoney() + ",对应账号:" + sevenBaoAccountInfoEO.getLoginAccount());
            //出现任何异常把商城的钱加回去
            updateFeeBothInZbaoAndGameGold(withDrawRequestDTO.getWithDrawMoney(), sevenBaoAccountInfoEO, outTradeNo);
            throw new SystemException(ResponseCodes.FailToGetContentFromCaiFuTong.getCode(), ResponseCodes.FailToGetContentFromCaiFuTong.getMessage());
        }
        if (httpClient.call()) {
            logger.info("调用财付通提现接口成功,准备处理返回信息,对应单号:" + outTradeNo + ",变动账户:" + sevenBaoAccountInfoEO.getLoginAccount() + ",变动金额:" + withDrawRequestDTO.getWithDrawMoney());
            String resConte = httpClient.getResContent();
            DirectTransClientResponseHandler resHandler =
                    new DirectTransClientResponseHandler();
            try {
                resHandler.setContent(resConte);
            } catch (Exception e) {
                logger.error("解析财付通返回结果失败,将单号:" + outTradeNo + "加入待 查询列表,对应登录账号:" + sevenBaoAccountInfoEO.getLoginAccount() + ".变动金额:" + withDrawRequestDTO.getWithDrawMoney());
                withdrawRedisDao.addTenPayToQuery(outTradeNo, orignalAmount.doubleValue());
            }
            String retCode = resHandler.getParameter("retcode");
            if (retCode.equals("0") || retCode.equals("00")) {
                BQR.setError("财付通提现提交成功");
                BQR.setIs_success("T");
                BQR.setOutTradeNo(outTradeNo);
                //加入redis里
                withdrawRedisDao.addTenPayToQuery(outTradeNo, orignalAmount.doubleValue());
            } else {
                logger.error("因:" + resHandler.getParameter("retmsg") + "财付通提现失败,返回商城变动资金开始,变动金额:" + withDrawRequestDTO.getWithDrawMoney() + ",变动账户:" + sevenBaoAccountInfoEO.getLoginAccount());
                //处理失败钱要加回去商城
                updateFeeBothInZbaoAndGameGold(withDrawRequestDTO.getWithDrawMoney(), sevenBaoAccountInfoEO, outTradeNo);
                throw new SystemException(ResponseCodes.FailToGetContentFromCaiFuTong.getCode(), ResponseCodes.FailToGetContentFromCaiFuTong.getMessage());
            }
        } else {
            logger.error("调用财付通提现接口失败(call()=false),返回商城变动资金开始 变动金额:" + withDrawRequestDTO.getWithDrawMoney() + ",变动账户:" + sevenBaoAccountInfoEO.getLoginAccount());
            //失败把钱加回去 商城
            updateFeeBothInZbaoAndGameGold(withDrawRequestDTO.getWithDrawMoney(), sevenBaoAccountInfoEO, outTradeNo);
            throw new SystemException(ResponseCodes.FailToGetContentFromCaiFuTong.getCode(), ResponseCodes.FailToGetContentFromCaiFuTong.getMessage());
        }
        return BQR;
    }

    /**
     * 更新金币商城的账户金额
     *
     * @param fee
     * @param sevenBaoAccountInfoEO
     */
    @Override
    @Transactional
    public void updateFeeBothInZbaoAndGameGold(BigDecimal fee, SevenBaoAccountInfoEO sevenBaoAccountInfoEO, String orderId) {
        InputStream content = null;
        try {
            logger.info("7bao同步商城资金开始,账号:" + sevenBaoAccountInfoEO.getLoginAccount() + "变动金额为:" + fee);
            String sign = DigestUtils.md5Hex(fee.toString() + sevenBaoAccountInfoEO.getLoginAccount() + orderId + zbaoMd5Key);
            CloseableHttpResponse response = null;
            CloseableHttpClient httpclient = HttpClients.createDefault();
            StringBuffer stringBuffer = new StringBuffer(updateUrl);
            stringBuffer.append("?loginAccount=" + sevenBaoAccountInfoEO.getLoginAccount() + "&sign=" + sign + "&changeAmount=" + fee + "&orderId=" + orderId);
            HttpGet httpPost = new HttpGet(stringBuffer.toString());
            httpPost.addHeader("Content-Type", "application/json");
            response = httpclient.execute(httpPost);
            content = response.getEntity().getContent();
            String responseStr = StreamIOHelper.inputStreamToStr(content, "utf-8");
            JSONObject repJson = JSONObject.fromObject(responseStr);
            String responseStatusStr = repJson.getString("responseStatus");
            if (StringUtils.isBlank(responseStr)) {
                logger.error("同步商城未返回数据，商户账号:" + sevenBaoAccountInfoEO.getLoginAccount() + ",变动金额:" + fee + ",对应订单号:" + orderId);
                throw new SystemException(ResponseCodes.NullResponseFromGameGold.getCode(), ResponseCodes.NullResponseFromGameGold.getMessage());
            }
            JSONObject jo = JSONObject.fromObject(responseStatusStr);
            if (!jo.getString("code").equals("00") || StringUtils.isBlank(jo.getString("code")) || StringUtils.isBlank(jo.getString("message"))) {
                logger.error("同步商城资金失败(返回码为" + jo.getString("code") + "返回提示为:" + jo.getString("message") + "),商户账号:" + sevenBaoAccountInfoEO.getLoginAccount() + "变动金额:" + fee + ",对应单号:" + orderId);
                throw new SystemException(jo.getString("code"), jo.getString("message"));
            }
        } catch (SystemException e) {
            throw new SystemException(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("同步商城资金失败,商户账号:" + sevenBaoAccountInfoEO.getLoginAccount() + "变动金额:" + fee);
            throw new SystemException(ResponseCodes.FailToUpdatePurchaseData.getCode(), ResponseCodes.FailToUpdatePurchaseData.getMessage());
        } finally {
            if (content != null) {
                try {
                    content.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 纯粹的调财付通接口查询，无任何业务逻辑，供业务使用方调用
     *
     * @param outTradeNo
     * @return
     * @throws UnsupportedEncodingException
     */
    @Override
    @Transactional
    public Alipay query(String outTradeNo) throws UnsupportedEncodingException {
        Alipay BQR = new Alipay();
        String key = TenpayUtil.SIGN;
        DirectTransClientRequestHandler reqHandler = new DirectTransClientRequestHandler(null, null);
        reqHandler.init();
        reqHandler.setKey(key);
        reqHandler.setParameter("op_code", "1014");
        reqHandler.setParameter("op_name", "batch_draw_query");
        reqHandler.setParameter("service_version", "1.2");
        reqHandler.setParameter("op_user", TenpayUtil.PARTNER);
        reqHandler.setParameter("op_passwd", MD5Util.MD5Encode("172040", "GBK"));
        reqHandler.setParameter("op_time", TenpayUtil.getCurrTime());
        reqHandler.setParameter("sp_id", "1482147241");
        reqHandler.setParameter("package_id", outTradeNo);
        reqHandler.setParameter("client_ip", "61.130.151.19");
        String reqUrl = reqHandler.getRequestURL();
        String debugInfo = reqHandler.getDebugInfo();

        //创建TenpayHttpClient
        TenpayHttpClient httpClient = new TenpayHttpClient();

        //设置请求内容
        httpClient.setReqContent(reqUrl);

        //设置ca证书
        httpClient.setCaInfo(new File(caPath));
        //设置商户证书
        httpClient.setCertInfo(new File(certPath), TenpayUtil.CERPASS);

        //设置发送类型 POST
        httpClient.setMethod("POST");
        if (httpClient.call()) {
            String resContent = httpClient.getResContent();
            DirectTransClientResponseHandler resHandler =
                    new DirectTransClientResponseHandler();

            DirectTransClientResponseHandler resHandler1 =
                    new DirectTransClientResponseHandler();

            try {
                Document document = DocumentHelper.parseText(resContent);
                Element element = document.getRootElement();
                String retcode = element.elementText("retcode");
                String retMesg = element.elementText("retmsg");
                if ("0".equals(retcode) || "00".equals(retcode)) {
                    //处理成功
                    BQR.setError("财付通提现查询成功,目前暂无银行返回信息");
                    //解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
                    Element resultNode = element.element("result");
                    Element stateNode = resultNode.element("trade_state");
                    String tradeState = stateNode.getText();
                    Element successSet = resultNode.element("success_set");
                    String succCount = successSet.elementText("suc_total");
                    Element failSet = resultNode.element("fail_set");
                    String failCount = failSet.elementText("fail_total");
                    BQR.setSign_type(tradeState);//会返回 1 2 3 4 5 6 7   6代表受理成功 但是还要确定有没有成功打款,目前就一笔 所以只要成功笔数是1那就算提现成功了
                    if ("1".equals(succCount)) {    //测试如果不行就换成 注释掉的代码中的sucTotal

                        BQR.setSign("success");
                        BQR.setError("提现成功");
                    } else if ("1".equals(failCount)) {
                        Element failRec = failSet.element("fail_rec");
                        String failReason = failRec.elementText("err_msg");
                        BQR.setSign("failed");
                        BQR.setError(failReason);
                    }
                    BQR.setIs_success("T");
                } else if ("03020165".equals(retcode)) {
                    //查询接口调用失败
                    BQR.setError("未接收到该批次数据,请联系相关工作人员");
                    BQR.setIs_success("T");

                    logger.error("单号:" + outTradeNo + ",未从银行返回对应数据,该单号不存在");
                    ZbaoWithdrawals zbaoWithdrawals = zbaoWithdrawalsDBDAO.selectUniqueByProp("orderId", outTradeNo);
                    if (zbaoWithdrawals != null && zbaoWithdrawals.getType() == 1) {
                        BigDecimal changeAmount = zbaoWithdrawals.getAmount().add(zbaoWithdrawals.getServiceAmount());
                        //更新七宝余额与商城余额 提现单更新状态为提现失败  资金明细也要改为失败
                        SevenBaoAccountInfoEO sevenBaoAccountInfoEO = sevenBaoAccountDBDAO.selectUniqueByProp("loginAccount", zbaoWithdrawals.getLoginAccount());
                        sevenBaoAccountInfoEO.setTotalAmount(sevenBaoAccountInfoEO.getTotalAmount().add(changeAmount));
                        sevenBaoAccountInfoEO.setAvailableAmount(sevenBaoAccountInfoEO.getAvailableAmount().add(changeAmount));
                        sevenBaoAccountDBDAO.update(sevenBaoAccountInfoEO);
                        logger.info("调用财付通查询信息,银行返回失败信息,变动金额" + changeAmount + "变动账户:" + sevenBaoAccountInfoEO.getLoginAccount());
                        updateFeeBothInZbaoAndGameGold(changeAmount, sevenBaoAccountInfoEO, outTradeNo);

                        zbaoWithdrawals.setType(3);
                        zbaoWithdrawals.setLog(new StringBuffer(zbaoWithdrawals.getLog()).append("......失败,此单号未提交至银行").toString());
                        zbaoWithdrawalsDBDAO.update(zbaoWithdrawals);

                        ZbaoFundDetail zbaoFundDetail = zbaoFundDetailDBDAO.selectUniqueByProp("txOrderId", outTradeNo);
                        zbaoFundDetail.setIsSuccess(false);
                        zbaoFundDetail.setLog(new StringBuffer(zbaoFundDetail.getLog()).append("......失败,该单号并未提交至银行").toString());
                        zbaoFundDetailDBDAO.update(zbaoFundDetail);
                    }
                    //因为没有信息 所以不再查询
                    withdrawRedisDao.removeTenPayToQuery(outTradeNo);
                } else {
                    //查询接口调用失败
                    logger.error("因返回结果异常,判断调用查询接口失败,单号为" + outTradeNo);
                    BQR.setError(resHandler.getParameter("retmsg"));
                    BQR.setIs_success("F");
                }
            } catch (Exception e) {
                //处理返回信息出现任何异常 封装 一个对象 给外部使用
                logger.error("处理提现查询返回结果异常, 单号为:" + outTradeNo);
                BQR.setError(e.getMessage());
                BQR.setIs_success("F");
                e.printStackTrace();
            }
        } else {
            //没有调用成功的情况
            logger.error("调用财付通提现查询接口失败(call=false),单号为:" + outTradeNo);
            BQR.setError("getResponseCode:" + httpClient.getResponseCode() + "|getErrInfo:" + httpClient.getErrInfo());
            BQR.setIs_success("F");
            throw new SystemException(ResponseCodes.FailToGetContentFromCaiFuTong.getCode(), ResponseCodes.FailToGetContentFromCaiFuTong.getMessage());
        }
        return BQR;
    }

    /**
     * 财付通查询业务处理
     *
     * @param outTradeNo
     */
    @Override
    @Transactional
    public void quertTenPayWithBusiness(String outTradeNo) {
        ZbaoFundDetail zbaoFundDetail = zbaoFundDetailDBDAO.selectUniqueByProp("txOrderId", outTradeNo);
        //所有未从银行获取的数据都应该已经记录在详情表中了
        if (zbaoFundDetail == null || zbaoFundDetail.getIsSuccess() != null) {
            throw new SystemException(ResponseCodes.FailToGetFundDetailMsg.getCode(), ResponseCodes.FailToGetFundDetailMsg.getMessage());
        }
        Alipay BQR = new Alipay();
        try {
            BQR = query(outTradeNo);
        } catch (UnsupportedEncodingException e) {
            BQR.setError("UnsupportedEncodingException:错误");
            BQR.setIs_success("F");
            e.printStackTrace();
        } catch (SystemException e) {
            throw new SystemException(e.getErrorCode(), e.getErrorMsg());
        }
        if ("T".equals(BQR.getIs_success())) {
            ZbaoWithdrawals zbaoWithdrawals = zbaoWithdrawalsDBDAO.selectUniqueByProp("orderId", outTradeNo);
            //设置成功返回消息时间
            zbaoWithdrawals.setDealTime(new Date());
            StringBuffer stringBuffer = new StringBuffer(zbaoWithdrawals.getLog());
            String loginAccount = zbaoWithdrawals.getLoginAccount();
            SevenBaoAccountInfoEO sevenBaoAccountInfoEO = sevenBaoAccountDBDAO.selectUniqueByProp("loginAccount", loginAccount);
            //应当加回去的余额  包括了实际提现金额  和 服务费
            BigDecimal orignalAmount = zbaoWithdrawals.getAmount().add(zbaoWithdrawals.getServiceAmount());
            BigDecimal payOrderNeedToSubstarct = BigDecimal.ZERO.subtract(orignalAmount).setScale(2, BigDecimal.ROUND_DOWN);
            //提现到卡失败了 ,其他状态不是最终状态 不处理
            if ("4".equals(BQR.getSign_type()) || "7".equals(BQR.getSign_type()) || ("6".equals(BQR.getSign_type()) && !"success".equals(BQR.getSign()))) {
                logger.info("银行返回提现失败,单号:" + outTradeNo);
                //改状态
                zbaoWithdrawals.setType(ZbaoWithdrawTypeEnum.FAILURE.getStatu());
                //改日志
                stringBuffer.append(".......银行返回处理结果为" + BQR.getError());
                zbaoWithdrawals.setLog(stringBuffer.toString());
                //加上失败原因
                zbaoWithdrawals.setFailReason(BQR.getError());
                //在途资金归零
                zbaoWithdrawals.setFundOnWay(BigDecimal.ZERO);
                //更新提现单记录
                zbaoWithdrawalsDBDAO.update(zbaoWithdrawals);
                // 通过登录账号取得账户信息  在原有余额的基础上加上
                BigDecimal oldAmount = sevenBaoAccountInfoEO.getAvailableAmount();
                //老金额加上了扣除的应当加回去的余额（可用资金）
                BigDecimal rightAmount = oldAmount.add(orignalAmount);
                sevenBaoAccountInfoEO.setAvailableAmount(rightAmount);
                //更新总资金
                sevenBaoAccountInfoEO.setTotalAmount(sevenBaoAccountInfoEO.getTotalAmount().add(orignalAmount));
                sevenBaoAccountDBDAO.update(sevenBaoAccountInfoEO);
                //再去更新金币商城的 传递应该改变的金额
                logger.info("因银行提现失败,准备调用商城资金同步接口,金额:" + orignalAmount + ".对应账号:" + sevenBaoAccountInfoEO.getLoginAccount());
                updateFeeBothInZbaoAndGameGold(orignalAmount, sevenBaoAccountInfoEO, outTradeNo);
                //再去更新资金明细表 状态为失败
                zbaoFundDetail.setIsSuccess(false);
                zbaoFundDetailDBDAO.update(zbaoFundDetail);
                //知道最终结果失败了 就不用再查询了
                withdrawRedisDao.removeTenPayToQuery(outTradeNo);
            } else if ("6".equals(BQR.getSign_type()) && "success".equals(BQR.getSign())) {
                logger.info("银行提现成功,单号:" + outTradeNo);
                //同步银行卡信息给账号
                sevenBaoAccountInfoEO.setBankName(zbaoWithdrawals.getBankNameType());
                sevenBaoAccountInfoEO.setAccountNO(zbaoWithdrawals.getCardCode());
                sevenBaoAccountInfoEO.setAccountProp(zbaoWithdrawals.getAccountProp());
                sevenBaoAccountInfoEO.setProvince(zbaoWithdrawals.getProvince());
                sevenBaoAccountInfoEO.setCity(zbaoWithdrawals.getCity());
                sevenBaoAccountInfoEO.setProvinceName(zbaoWithdrawals.getProvinceName());
                sevenBaoAccountInfoEO.setCityName(zbaoWithdrawals.getCityName());
                sevenBaoAccountInfoEO.setSubBankName(zbaoWithdrawals.getOpenbank());
                sevenBaoAccountDBDAO.update(sevenBaoAccountInfoEO);
                //提现成功了 日志也要改一下 金额就不用改动了
                zbaoWithdrawals.setType(ZbaoWithdrawTypeEnum.SUCCESS.getStatu());
                stringBuffer.append(".......银行返回处理结果为:" + BQR.getError());
                zbaoWithdrawals.setLog(stringBuffer.toString());
                //在途资金归零
                zbaoWithdrawals.setFundOnWay(BigDecimal.ZERO);
                zbaoWithdrawalsDBDAO.update(zbaoWithdrawals);
                //更新资金明细表状态为成功
                zbaoFundDetail.setIsSuccess(true);
                zbaoFundDetailDBDAO.update(zbaoFundDetail);
                JSONObject jsonObject = null;
                //提现成功删除采购单
                try {
                    jsonObject = deletePayOrderCrossing(loginAccount, payOrderNeedToSubstarct);
                } catch (IOException e) {
                    logger.error("解析因提现成功删除采购单,返回信息失败,账号:" + loginAccount + ",变动金额:" + payOrderNeedToSubstarct);
                    throw new SystemException(ResponseCodes.NullResponseFromGameGoldDeletePayOrder.getCode(), ResponseCodes.NullResponseFromGameGoldDeletePayOrder.getMessage());
                }
                String responseStatusStr = jsonObject.getString("responseStatus");
                if (StringUtils.isBlank(responseStatusStr)) {
                    logger.error("解析删除采购单接口返回数据为空,对应账号:" + loginAccount + ",变动资金:" + payOrderNeedToSubstarct);
                    throw new SystemException(ResponseCodes.NullResponseFromGameGoldDeletePayOrder.getCode(), ResponseCodes.NullResponseFromGameGoldDeletePayOrder.getMessage());
                }
                JSONObject jo = JSONObject.fromObject(responseStatusStr);
                if (!jo.getString("code").equals("00") || StringUtils.isBlank(jo.getString("code")) || StringUtils.isBlank(jo.getString("message"))) {
                    logger.error("因提现成功删除采购单失败(返回码为" + jo.getString("code") + "返回提示为:" + jo.getString("message") + "),商户账号:" + loginAccount + "变动金额:" + payOrderNeedToSubstarct);
                    throw new SystemException(jo.getString("code"), jo.getString("message"));
                }
                if (jo.getString("code").equals("00")) {
                    logger.info("提现成功删除采购单成功！,商户账号:" + loginAccount + ",变动资金:" + payOrderNeedToSubstarct);
                }
                //成功了就不用再查询了
                withdrawRedisDao.removeTenPayToQuery(outTradeNo);
            }
        } else {
            throw new SystemException(BQR.getIs_success(), BQR.getError());
        }

    }

    /**
     * 删除金币商城采购单接口，本方法中不包括返回信息处理 由方法调用者自行处理
     *
     * @param loginAccount 登录账号
     * @param changeAmount 变动资金
     * @return 返回json串 包括responseStatus 状态值
     * @throws IOException
     */
    private JSONObject deletePayOrderCrossing(String loginAccount, BigDecimal changeAmount) throws IOException {
        InputStream content = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            StringBuffer stringBuffer = new StringBuffer(deletePayOrderUrl);
            String sign = DigestUtils.md5Hex(loginAccount + changeAmount.toString() + zbaoMd5Key);
            stringBuffer.append("?loginAccount=" + loginAccount + "&changeAmount=" + changeAmount + "&sign=" + sign);
            HttpGet httpPost = new HttpGet(stringBuffer.toString());
            httpPost.addHeader("Content-Type", "application/json");
            CloseableHttpResponse response = null;
            logger.info("提现成功删除采购单开始,对应账号:" + loginAccount + "应删除金额为:" + changeAmount);
            response = httpclient.execute(httpPost);
            content = response.getEntity().getContent();
            String responseStr = StreamIOHelper.inputStreamToStr(content, "utf-8");
            return JSONObject.fromObject(responseStr);
        } finally {
            if (content != null) {
                content.close();
            }
        }
    }

    /**
     * 调用商城查询purchaseData表中数据方法 ，返回信息本方法中不做处理，提供json数据 由调用者自行处理
     *
     * @param loginAccount 登录账号
     * @return json串 包括 responseStatus 状态信息  purchaserData 对应账号信息
     * @throws IOException
     */
    private JSONObject getJsonObject(String loginAccount) throws IOException {
        InputStream content = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            StringBuffer stringBuffer = new StringBuffer(addOrderUrl);
            String sign = DigestUtils.md5Hex(loginAccount + zbaoMd5Key);
            stringBuffer.append("?loginAccount=" + loginAccount + "&sign=" + sign);
            logger.info("路径地址为:" + stringBuffer.toString());
            HttpGet httpPost = new HttpGet(stringBuffer.toString());
            httpPost.addHeader("Content-Type", "application/json");
            CloseableHttpResponse response = null;
            logger.info("商城获取资金数据开始,对应账号:" + loginAccount);
            response = httpclient.execute(httpPost);
            logger.info("商城请求完成,开始处理返回结果");
            content = response.getEntity().getContent();
            String responseStr = StreamIOHelper.inputStreamToStr(content, "utf-8");
            logger.info("返回成功,信息为:" + JSONObject.fromObject(responseStr));
            return JSONObject.fromObject(responseStr);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("商城获取资金数据开始出现错误：{}", e);
            return null;
        } finally {
            if (content != null) {
                content.close();
            }
        }
    }
}
