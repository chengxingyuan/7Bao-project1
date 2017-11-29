/**
 * Created by 340096 on 2017/8/10.
 */
/**
 * 7bao账户信息
 * */
Ext.define('MyApp.model.sevenBaoAccoutModel', {
    extend: 'Ext.data.Model',
    fields: [
        {
            name: 'id',
        },
        {
            name: 'loginAccount'//用户登录名
        },{
            name: 'zbaoLoginAccount'//用户登录名
        },
        {
            name: 'name'//真实姓名
        }, {
            name: 'phoneNumber'//电话号码
        }, {
            name: 'qq'//QQ
        },  {
            name: 'kefuqq'//kefuQQ
        },{
            name: 'totalAmount'//总金额
        },{
            name: 'accountNO',//银行卡号
        },{
            name: 'showName',//银行名称
            convert: function (v, record) {
               console.log(v)
            }
        },
        {
            name: 'freezeAmount'//冻结资金
        },
        {
            name: 'availableAmount'//可用资金
        },
        {
            name: 'createTime',//创建时间
        },
        {
            name: 'updateTime',//更新时间
        },
        {
            name: 'isDeleted',//是否启用
            type: 'Boolean'
        }, {
            name: 'isUserBind',//金币用户绑定
            type: 'Boolean'
        },{
            name: 'isShBind',//收货绑定
            type: 'Boolean'
        }]

});