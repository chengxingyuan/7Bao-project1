package com.wzitech.gamegold.common.usermgmt.entity;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Update History
 * Date         Name    Reason for change
 *
 */

/**
 * Created by huangdq on 2016/12/13.
 */
public class MainStationConstant {

    /** AppId 线上 */
//    public static  String APP_ID = "msite";
    /** 测试服务器 */
    public static String URL = "http://openapi.5173.com";
    /****************************** 2.0之后使用的接口名称 ******************************/
    /** Access接口 */
    public static String URL_ACCESS = URL + "/access.do";
    /** RequestToken接口 */
    public static String URL_REQUESTTOKEN = URL + "/request.do";
    /** 一般请求接口 */
    public static String URL_REST = URL + "/rest.do";
    /** 签名用的字段 */
//    public static  String KAYVALUE = "msite5173";// 修改线下
    /*
    * 账号类型：支付宝后续还可扩展其他
    * */
    public static int ACCOUNT_TYPE=1;

    /** 设置服务器返回数据类型 */
    public static String RESULT_TYPE = "html";
    /** 设置服务器返回加密类型 */
    public static String RESULT_MD5 = "md5";
    /** 设置用户版本 */
    public static String RESULT_VERSION = "1.0";
    /** 设置版本 */
    public static String RESULT_VERS = "1.0";
    /** Fields */
    public static String RESULT_FIELDS = "";

    /**  DEBUG 开关(true:开启 false:关闭)*/
    public static boolean DEBUG = true;


    /** 设置版本 */
    public static String RESULT_AUTHVERS = "1.0";
    public static String RESULT_AUTHVERS2="2.0";


    /** 请求头 */
    public static String HEADER_CONTENT_TYPE = "Content-Type";
    public static String HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static String IS_ANDROID = "1";
    public static String CONTANT_CODE = "text/html;UTF-8";
    public static String CONTANT_CONTENT_TYPE = "multipart/form-data";


    /****************************** 主站提供方法名称 ******************************/
    /**
     * 用户推送接口
     */
    public static String METHOD_MSITEREGISTER="kubao.passport.msiteregister";  /**


     /**
     * 用户推送接口
     */
    public static String METHOD_REQUEST="kubao.passport.request";  /**

     /**
     * 用户推送接口
     */
    public static String METHOD_LOGIN="kubao.passport.login";

    /**
     * 用户推送接口
     */
    public static String METHOD_DISPLAY="kubao.passport.display";
    /**
     kubao.passport.facility.register
     * 用户推送接口登录注册(新)
     */
    public static String METHOD_NEW_MSITEREGISTER="kubao.passport.new.msiteregister";

//    /** 授权 令牌 */
//    public static  String requestStr="?appid="+ URLEncoder.encode(APP_ID)+"&authVers="+URLEncoder.encode(RESULT_AUTHVERS)+
//            "&format="+URLEncoder.encode(RESULT_TYPE)+"&signmethod="+URLEncoder.encode(RESULT_MD5);
//    public static String baseSign = APP_ID + "&" + RESULT_AUTHVERS + "&" +RESULT_TYPE ;

    /**  */
    /** MW_C_ZH_00035_Start **/
    //九、主站根据游戏ID获取游戏平台接口
    public final static String KUBAO_LM_GETPLATFORMINFOBYGAMEID_GET="kubao.lm.getplatforminfobygameid.get";
    //十、根据游戏ID和游戏平台ID查询游戏区
    public final static String KUBAO_LM_GETGAMEAREASBYGAMEANDPLATFORM_GET="kubao.lm.getgameareasbygameandplatform.get";
    //十一、根据游戏区ID查询游戏服
    public final static String KUBAO_CBO_GAME_SERVER_GET="kubao.cbo.game.server.get";
    //十二、获取物品属性
    public final static String KUBAO_MSITE_SHOUYOU_GETBIZCATEGORYPROPERTY="kubao.msite.shouyou.getbizcategoryproperty";
    //根据大类ID获取物品大类信息
    public final static String KUBAO_LM_NEW_BIZCATEGORY_GET = "kubao.lm.new.bizcategory.get";
    //根据大类获取属性属性值
    public final static String KUBAO_LM_GETCATEGORYPROPERTYBYBIZCATEGORY_GET = "kubao.lm.getcategorypropertybybizcategory.get";

    /** MW_C_ZH_00035_END **/
    //获取物品小类信息
    public final static String KUBAO_LM_NEW_BIZCATEGORYTYPE_GET="kubao.lm.new.bizcategorytype.get";
    //获取物品小类信息
    public final static String KUBAO_LM_GETCATEGORYPROPERTYBYCATEGORYTYPE_GET="kubao.lm.getcategorypropertybycategorytype.get";
    public static<T> T JSONToObj(String jsonStr, Class<T> obj) {
        T t = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            t = objectMapper.readValue(jsonStr,
                    obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }


    // 将输入流转换成字符串
    public static String inStream2String(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        return new String(baos.toByteArray());
    }



}
