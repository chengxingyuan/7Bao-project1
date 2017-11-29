package com.wzitech.Z7Bao.backend.action.ShConfig;

import com.wzitech.Z7Bao.backend.action.extjs.AbstractAction;
import com.wzitech.Z7Bao.backend.interceptor.ExceptionToJSON;
import com.wzitech.Z7Bao.backend.util.WebServerUtil;
import com.wzitech.Z7Bao.frontend.business.IZbaoServiceAmountConfigManager;
import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoServiceAmountConfigDao;
import com.wzitech.Z7Bao.frontend.entity.ZbaoServiceAmountConfig;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/30
 * 服务费配置
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class ZbaoServiceAmountAction extends AbstractAction {

    private ZbaoServiceAmountConfig zbaoServiceAmountConfig;

    private List<ZbaoServiceAmountConfig> zbaoServiceAmountConfigList;

    /**
     * 费率
     */
    private BigDecimal rate;

    /**
     * 最少服务费
     */
    private BigDecimal minServiceAmount;

    /**
     * 最多服务费
     */
    private BigDecimal maxServiceAmount;

    /**
     * 最少提现金额
     */
    private BigDecimal minAmount;

    /**
     * 最大提现金额
     */
    private BigDecimal maxAmount;

    private Long id;

    @Autowired
    IZbaoServiceAmountConfigManager zbaoServiceAmountConfigManager;
    @Autowired
    IZbaoServiceAmountConfigDao zbaoServiceAmountConfigDao;

    public String queryZbaoServiceAmount() {
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            GenericPage<ZbaoServiceAmountConfig> genericPage = zbaoServiceAmountConfigManager.queryPage(queryMap, this.limit, this.start, "id", true);
            zbaoServiceAmountConfigList = genericPage.getData();
            totalCount = genericPage.getTotalCount();
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String addZbaoServiceAmount() {
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            zbaoServiceAmountConfigList = zbaoServiceAmountConfigManager.queryByMap(queryMap, "id", true);
            if (zbaoServiceAmountConfigList.size() > 2) {
                return this.returnError("该配置信息最大只能有3条，请选择一条删除，重新添加");
            }
            ZbaoServiceAmountConfig zbaoServiceAmountConfig = new ZbaoServiceAmountConfig();
            zbaoServiceAmountConfig.setMaxAmount(maxAmount);
            zbaoServiceAmountConfig.setMinAmount(minAmount);
            zbaoServiceAmountConfig.setMaxServiceAmount(maxServiceAmount);
            zbaoServiceAmountConfig.setMinServiceAmount(minServiceAmount);
            zbaoServiceAmountConfig.setRate(rate);
            zbaoServiceAmountConfigManager.addServiceAmount(zbaoServiceAmountConfig);
            zbaoServiceAmountConfig.setRate(rate);
            zbaoServiceAmountConfigDao.updateRate(zbaoServiceAmountConfig);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String deleteZbaoServiceAmount() {
        try {
            zbaoServiceAmountConfigManager.delete(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String updateZbaoServiceAmount() {
        try {
            zbaoServiceAmountConfig = zbaoServiceAmountConfigManager.selectByid(id);
            zbaoServiceAmountConfig.setMaxServiceAmount(maxServiceAmount);
            zbaoServiceAmountConfig.setMinServiceAmount(minServiceAmount);
            zbaoServiceAmountConfig.setMaxAmount(maxAmount);
            zbaoServiceAmountConfig.setMinAmount(minAmount);
            if (zbaoServiceAmountConfig.getRate().compareTo(rate) != 0) {
                zbaoServiceAmountConfig.setRate(rate);
                zbaoServiceAmountConfigDao.updateRate(zbaoServiceAmountConfig);
            }
            zbaoServiceAmountConfigManager.update(zbaoServiceAmountConfig);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }


    public ZbaoServiceAmountConfig getZbaoServiceAmountConfig() {
        return zbaoServiceAmountConfig;
    }

    public void setZbaoServiceAmountConfig(ZbaoServiceAmountConfig zbaoServiceAmountConfig) {
        this.zbaoServiceAmountConfig = zbaoServiceAmountConfig;
    }

    public List<ZbaoServiceAmountConfig> getZbaoServiceAmountConfigList() {
        return zbaoServiceAmountConfigList;
    }

    public void setZbaoServiceAmountConfigList(List<ZbaoServiceAmountConfig> zbaoServiceAmountConfigList) {
        this.zbaoServiceAmountConfigList = zbaoServiceAmountConfigList;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getMinServiceAmount() {
        return minServiceAmount;
    }

    public void setMinServiceAmount(BigDecimal minServiceAmount) {
        this.minServiceAmount = minServiceAmount;
    }

    public BigDecimal getMaxServiceAmount() {
        return maxServiceAmount;
    }

    public void setMaxServiceAmount(BigDecimal maxServiceAmount) {
        this.maxServiceAmount = maxServiceAmount;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
