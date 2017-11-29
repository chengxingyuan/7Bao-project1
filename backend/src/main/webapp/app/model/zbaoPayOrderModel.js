/*
 * 资金明细
 */
Ext.define('MyApp.model.zbaoPayOrderModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'loginAccount'//买家账号
    },{
        name: 'orderType',//订单类型
        type: 'int'
    },{
        name: 'orderId'//订单号
    },{
        name: 'outOrderId'//外部订单号
    },{
        name: 'zzOrderId'//主站订单号
    },{
        name: 'status',//充值状态
        type: 'int'
    },{
        name: 'amount',//金额
        type: 'float'
    },{
        name: 'remark',//备注
    },{
        name: 'paymentAccount',//支付账户
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