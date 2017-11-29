package com.wzitech.Z7Bao.backend.action.ShConfig;

import com.wzitech.Z7Bao.backend.action.extjs.AbstractAction;
import com.wzitech.Z7Bao.backend.interceptor.ExceptionToJSON;
import com.wzitech.Z7Bao.frontend.business.ISystemConfigManager;
import com.wzitech.Z7Bao.frontend.entity.SystemConfig;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by wangmin
 * Date:2017/8/22
 * 收货系统配置
 */

@Controller
@Scope("prototype")
@ExceptionToJSON
public class shSystemConfigAction extends AbstractAction {

    private SystemConfig systemConfig;

    @Autowired
    private ISystemConfigManager systemConfigManager;

    private List<SystemConfig> configList;

    private Long id;
    //------------------------------------------------------------------

    /**
     * 查询出货系统配置列表
     *
     * @return
     */
    public String queryConfig() {
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(systemConfig.getConfigKey())) {
                queryMap.put("configKey", systemConfig.getConfigKey());
            }
            GenericPage<SystemConfig> genericPage = systemConfigManager.
                    queryPage(queryMap, this.limit, this.start, "id", true);
            configList = genericPage.getData();
            totalCount = genericPage.getTotalCount();
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 删除出货系统配置
     *
     * @return
     */
    public String deleteConfig() {
        try {
            if (id != null) {
                systemConfigManager.deleteConfigById(id);
            }
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 增加出货系统配置
     *
     * @return
     */
    public String addConfig() {
        try {
            systemConfigManager.add(systemConfig);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 修改系统配置
     *
     * @return
     */
    public String updateConfig() {
        try {
            systemConfigManager.update(systemConfig);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }


    /**
     * 禁用用户
     * @return
     */
    public String disableUser() {
        try {
            systemConfigManager.disableUser(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 启用用户
     * @return
     */
    public String qyUser() {
        try {
            systemConfigManager.qyUser(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e.getMessage());
        }
    }
    //-------------------------------------------------------------


    public SystemConfig getSystemConfig() {
        return systemConfig;
    }

    public void setSystemConfig(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }

    public ISystemConfigManager getSystemConfigManager() {
        return systemConfigManager;
    }

    public void setSystemConfigManager(ISystemConfigManager systemConfigManager) {
        this.systemConfigManager = systemConfigManager;
    }

    public List<SystemConfig> getConfigList() {
        return configList;
    }

    public void setConfigList(List<SystemConfig> configList) {
        this.configList = configList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
