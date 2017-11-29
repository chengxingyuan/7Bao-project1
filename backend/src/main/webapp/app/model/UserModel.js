/**
 * Created by 340096 on 2017/8/9.
 */
/*
 * 用户信息
 */

Ext.define('MyApp.model.UserModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id'//用户id
    },
    //     {
    //     name: 'mainAccountId'//主账号
    // }, {
    //     name: 'userType'//用户类型（客服、管理员、系统管理员、招商、做单）
    // },
        {
        name: 'loginAccount'//用户登录名
    },
        {
        name: 'fundsAccountId'//客服资金账号ID
    }, {
        name: 'fundsAccount'//客服资金账号
    }, {
        name: 'password'//登录密码
    }, {
        name: 'realName'//真实姓名
    }, {
      name: 'phoneNumber'//电话号码
    }, {
        name: 'qq'//QQ
    },{
        name: 'weiXin'//微信
    }, {
        name: 'sex'//性别
    },   {
        name: 'isDeleted',//是否启用
        type: 'boolean'
    },  {
            name: 'isQqService',//是否启用
            type: 'boolean'
        }, ]
});