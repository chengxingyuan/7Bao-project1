package com.wzitech.gamegold.common.utils;

import com.wzitech.gamegold.common.usermgmt.entity.MainStationConstant;

import java.net.URLEncoder;

/**
 * Created by 340082 on 2017/8/9.
 */
public class SpitConstant {

    /** 请求头 */
    public static  String HEADER_CONTENT_TYPE = "Content-Type";
    public static  String HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static  String IS_ANDROID = "1";
    public static  String CONTANT_CODE = "text/html;UTF-8";
    public static  String CONTANT_CONTENT_TYPE = "multipart/form-data";

    public static String getRequestStr(String appId){
        String requestStr="?appid="+ URLEncoder.encode(appId)+"&authVers="+URLEncoder.encode(MainStationConstant.RESULT_AUTHVERS)+
                "&format="+URLEncoder.encode(MainStationConstant.RESULT_TYPE)+"&signmethod="+URLEncoder.encode(MainStationConstant.RESULT_MD5);
        return requestStr;
    }

    public static String getBaseSign(String appId){
        String baseSign = appId + "&" + MainStationConstant.RESULT_AUTHVERS + "&" +MainStationConstant.RESULT_TYPE ;
        return baseSign;
    }
}
