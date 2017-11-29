/*
 * 资金明细
 */
Ext.define('MyApp.model.zbaoTransferModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'loginAccount'//登录账号
    },{
        name: 'orderId'//订单号
    },{
        name: 'shOrderId'//收货订单号
    },{
        name: 'status',//转账状态
        type: 'int'
    },{
        name: 'amount',//金额
        type: 'float'
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