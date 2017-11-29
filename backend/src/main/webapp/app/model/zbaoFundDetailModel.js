/*
 * 资金明细
 */
Ext.define('MyApp.model.zbaoFundDetailModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'loginAccount'//买家账号
    },{
        name: 'type',//类型
        type: 'int'
    },{
        name: 'czOrderId'//关联的充值单号
    },{
        name: 'txOrderId'//关联的提现订单号
    },{
        name: 'cgOrderId'//关联的采购转账订单号
    },{
        name: 'outOrderId'//外部订单号
    },{
        name: 'amount',//金额
        type: 'float'
    },{
        name: 'log'//日志详情
    },{
        name: 'isSuccess',//是否成功
        type:'boolean'
    },{
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }]
});