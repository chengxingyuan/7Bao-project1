package com.wzitech.gamegold.common.utils;


/**
 * Redis Key帮助类
 *
 * @author jiyangxin
 *         Update History
 *         Date         Name            Reason For Update
 *         ----------------------------------------------
 *         jiyangxin
 */
public class RedisKeyHelper {

    /**
     * 获取uid对应的auth
     *
     * @param uid
     * @return
     */
    public static String auth(String uid) {
        return "z7Bao:uid:" + uid + ":auth";
    }

    /**
     * 获取authkey对应的用户id
     *
     * @param authkey
     * @return
     */
    public static String authkey(String authkey) {
        return "z7Bao:auth:" + authkey + ":uid";
    }
    /**
     * 获取对应uid的用户信息key
     *
     * @param uid
     * @return
     */
    public static String uid(String uid) {
        return "z7Bao:uid:" + uid + ":userInfo";
    }

    /**
     * 获取对应帐号的uid
     *
     * @param loginAccount
     * @return
     */
    public static String account(String loginAccount) {
        return "z7Bao:loginAccount:" + loginAccount + ":uid";
    }

    /**
     * 主站key存储reids
     */
    public static String mainKey() {
        return "z7Bao:accessToken:mainKey";
    }

    /**
     * 保存auth登录权限的redis
     */
    public static String accountAuthKey(String auth) {
        return "z7Bao:accountAuth:" + auth;
    }

    /**
     * 保存提交提现财付通成功的redis
     */
    public static String keepOutTradeNo() {
        return "z7Bao:queryTenpayWithdrawList";
    }

    /**
     * 锁表key
     *
     * @param lockKey
     * @return
     */
    public static String lockKey(String lockKey) {
        return "z7Bao:lockKey:" + lockKey.trim();
    }

    /**
     * 城市key
     * @param provinceId
     * @return
     */
    public static String financeCityKey(Long provinceId){
        return  "z7Bao:city:provinceId:" + provinceId;
    }

    /**
     * 省份key
     * @return
     */
    public static String financeProvinceKey(){
        return  "z7Bao:city:province";
    }

    /**
     * qqKey
     */
    public static String getQqInUser(){
        return  "z7Bao:qq:";
    }
}
