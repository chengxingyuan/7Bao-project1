/*
 * 提现明细
 */
Ext.define('MyApp.model.zbaoWithdrawalsModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'loginAccount'//买家账号
    },{
        name: 'type',//订单类型
        type: 'int'
    },{
        name: 'bankNameType',//提现渠道类型
        type: 'int'
    },{
        name: 'orderId'//订单号
    },{
        name: 'outOrderId'//外部订单号
    },{
        name: 'serviceAmount',//服务费
        type: 'float'
    },{
        name: 'amount',//金额
        type: 'float'
    },{
        name: 'fundOnWay',//在途资金
        type: 'float'
    },{
        name: 'log',//日志
    },{
        name: 'bankName',//渠道名称
    },{
        name: 'cardCode',//提现账号
    },{
        name: 'realName',//开户姓名
    },{
        name: 'provinceName',//省份
    },{
        name: 'cityName',//城市
    },{
        name: 'areacode',// 区号
    },{
        name: 'openbank',//开户银行
    },{
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'dealTime',//结束时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }]
});