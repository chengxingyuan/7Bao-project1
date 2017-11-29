package com.wzitech.Z7Bao.frontend.service;

import com.wzitech.Z7Bao.frontend.service.dto.IPersonalReponse;

/**
 * Created by 340032 on 2017/8/30.
 */
public interface IPersonalService {

    /**
     * 个人中心信息
      */
    IPersonalReponse qureyPersonal();


    /**
     * 账号
     */
    IPersonalReponse queryNumber();



}
