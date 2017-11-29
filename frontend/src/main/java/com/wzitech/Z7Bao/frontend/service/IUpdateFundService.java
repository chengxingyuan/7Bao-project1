package com.wzitech.Z7Bao.frontend.service;

/**
 * Created by chengXY on 2017/8/23.
 */

import com.wzitech.Z7Bao.frontend.service.dto.UpdateFunResponse;
import com.wzitech.Z7Bao.frontend.service.dto.UpdateFundRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * 冻结、解冻接口
 * */
public interface IUpdateFundService {
    public UpdateFunResponse changeFund(UpdateFundRequest updateFundRequest, @Context HttpServletRequest request);
}
