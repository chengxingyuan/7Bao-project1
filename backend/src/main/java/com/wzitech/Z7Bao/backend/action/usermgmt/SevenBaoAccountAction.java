package com.wzitech.Z7Bao.backend.action.usermgmt;

import com.wzitech.Z7Bao.backend.action.extjs.AbstractFileUploadAction;
import com.wzitech.Z7Bao.backend.interceptor.ExceptionToJSON;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.usermgmt.business.ISevenBaoAccountManager;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chengXY on 2017/8/10.
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class SevenBaoAccountAction extends AbstractFileUploadAction{

   // private static final long serialVersionUID = 1L;

    private SevenBaoAccountInfoEO sevenBaoAccountInfoEO;

    private List<SevenBaoAccountInfoEO> userList;

    private Long id;

    private Boolean isUserBind;

    private String loginAccount;

    @Autowired
    ISevenBaoAccountManager sevenBaoAccountManager;


    public String queryUserDetail(){
        try {
         sevenBaoAccountInfoEO = sevenBaoAccountManager.selectById(id);
        String bankName = sevenBaoAccountManager.selectBankName(sevenBaoAccountInfoEO.getLoginAccount());
        sevenBaoAccountInfoEO.setShowName(bankName);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }


    /**
     * 查询7bao账号信息
     * */
    public String querySevenBaoAccount(){
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(sevenBaoAccountInfoEO.getLoginAccount())) {
            queryMap.put("loginAccount", sevenBaoAccountInfoEO.getLoginAccount().trim());

        }
//            queryMap.put("isUserBind",isUserBind);
        if (sevenBaoAccountInfoEO.getIsUserBind() != null) {
            queryMap.put("isUserBind", sevenBaoAccountInfoEO.getIsUserBind());
        }
         if (sevenBaoAccountInfoEO.getIsShBind() != null){
            queryMap.put("isShBind",sevenBaoAccountInfoEO.getIsShBind());
        }
        if (StringUtils.isNotBlank(sevenBaoAccountInfoEO.getName())) {
            queryMap.put("name", sevenBaoAccountInfoEO.getName().trim());
        }

        if (StringUtils.isNotBlank(sevenBaoAccountInfoEO.getShowName())){
            queryMap.put("showName",sevenBaoAccountInfoEO.getShowName());
        }
        GenericPage<SevenBaoAccountInfoEO> genericPage = sevenBaoAccountManager.querySevenBaoAccount(queryMap, this.limit, this.start, null, true);
        userList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 编辑7bao账号
     * */
    public String modifyAccount(){
        IUser currentUser = CurrentUserContext.getUser();
        sevenBaoAccountInfoEO.setId(id);
        sevenBaoAccountManager.modifyAccount(sevenBaoAccountInfoEO);
        return this.returnSuccess();
    }

    /**
     *启用该7bao账号
     * */
    public String enableAccount(){
        sevenBaoAccountManager.enableAccount(id);
        return  this.returnSuccess();
    }

    /**
     * 禁用该7bao账号
     * */
    public  String disableAccount(){
        sevenBaoAccountManager.disableAccount(id);
        return  this.returnSuccess();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SevenBaoAccountInfoEO> getUserList() {
        return userList;
    }

    public void setUserList(List<SevenBaoAccountInfoEO> userList) {
        this.userList = userList;
    }

    public SevenBaoAccountInfoEO getSevenBaoAccountInfoEO() {
        return sevenBaoAccountInfoEO;
    }

    public void setSevenBaoAccountInfoEO(SevenBaoAccountInfoEO sevenBaoAccountInfoEO) {
        this.sevenBaoAccountInfoEO = sevenBaoAccountInfoEO;
    }

    public Boolean getUserBind() {
        return isUserBind;
    }

    public void setUserBind(Boolean userBind) {
        isUserBind = userBind;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }
}
