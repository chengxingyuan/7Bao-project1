package com.wzitech.Z7Bao.backend.action.usermgmt;

/**
 * Created by chengXY on 2017/8/10.
 */
import com.wzitech.Z7Bao.backend.action.extjs.AbstractFileUploadAction;
import com.wzitech.Z7Bao.backend.interceptor.ExceptionToJSON;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.UserType;

import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("prototype")
@ExceptionToJSON
public class UserAction extends AbstractFileUploadAction {

    private static final long serialVersionUID = 7158706093540959280L;

    private UserInfoEO user;

    private Long id;

    private List<UserInfoEO> userList;

    @Autowired
    IUserInfoManager userInfoManager;

    /**
     * 查询用户列表
     * @return
     */
    public String queryUser() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (user != null) {
            IUser currentUser = CurrentUserContext.getUser();
            if (StringUtils.isNotBlank(user.getLoginAccount())) {
                queryMap.put("loginAccount", user.getLoginAccount().trim());
            }
            if (StringUtils.isNotBlank(user.getRealName())) {
                queryMap.put("realName", user.getRealName().trim());
            }
        }
        GenericPage<UserInfoEO> genericPage = userInfoManager.queryUser(queryMap, this.limit, this.start, null, true);
        userList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 新增后台管理用户
     * @return
     * @throws IOException
     */
    public String addUser() throws IOException {
        try {
            IUser currentUser = CurrentUserContext.getUser();
            userInfoManager.addUser(user);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 启用用户
     * @return
     */
    public String enableUser() {
        try {
            userInfoManager.enableUser(id);

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
            userInfoManager.disableUser(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }


    /**
     * 启用用户QQ
     * @return
     */
    public String enableQQ() {
        try {
            userInfoManager.enableQQ(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }



    /**
     * 删除用户
     * @return
     */
    public String deleteUser() {
        try {
            userInfoManager.deleteUser(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }
    /**
     * 编辑用户
     * */
    public String modifyUser(){
        if (user != null) {
            user.setId(id);
        }
        userInfoManager.modifyUserInfo(user);
        return  this.returnSuccess();
    }

    public List<UserInfoEO> getUserList() {
        return userList;
    }

    public void setUser(UserInfoEO user) {
        this.user = user;
    }

    public UserInfoEO getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
