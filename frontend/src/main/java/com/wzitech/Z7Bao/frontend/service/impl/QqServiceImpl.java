package com.wzitech.Z7Bao.frontend.service.impl;

import com.wzitech.Z7Bao.frontend.service.IQqService;
import com.wzitech.Z7Bao.frontend.service.dto.QqServiceReponse;
import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.dao.rdb.IUserInfoDBDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by 340032 on 2017/8/29.
 *
 */
@Service("QQService")
@Path("/QQ")
@Produces("application/json;charset=UTF-8")
public class QqServiceImpl extends AbstractBaseService implements IQqService {

    @Autowired
    IUserInfoManager userInfoManager;

    @Autowired
    IUserInfoDBDAO userInfoDBDAO;

    @Autowired
    UserInfoEO userInfoEO;

    @Override
    @GET
    @Path("/queryQQ")
    public IServiceResponse queryQq() {
        ResponseStatus responseStatus = new ResponseStatus();
        QqServiceReponse reponse=new QqServiceReponse();
        reponse.setResponseStatus(responseStatus);
        try {
            UserInfoEO userInfoEO=userInfoManager.selectByQq();
            if (userInfoEO!=null){
                    reponse.setQq(userInfoEO.getQq());
                    return reponse;
            }
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
        }
        return null;
    }
}
