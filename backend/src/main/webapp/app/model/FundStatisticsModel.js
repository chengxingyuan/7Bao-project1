/**
 * Created by 340096 on 2017/8/10.
 */
/**
 * 7bao账户信息
 * */
Ext.define('MyApp.model.FundStatisticsModel', {
    extend: 'Ext.data.Model',
    fields: [
        {
            name: 'id',
            type: 'int'
        },{
            name: 'qcBalance',//期初余额
            type: 'float'
        },{
            name: 'zfAmount',//支付金额
            type: 'float'
        },{
            name: 'txAmount',//提现金额
            type: 'float'
        },{
            name: 'fkAmount',//付款金额
            type: 'float'
        },{
            name: 'txServiceAmount',//现服务费
            type: 'float'
        },{
            name: 'sdZfAmount',//售得充值
            type: 'float'
        },{
            name: 'qmBalance',//提期末余额
            type: 'float'
        },{
            name: 'oldFund',//老资金
            type: 'float'
        },{
            name: 'processing',//提现处理中金额
            type: 'float'
        },{
            name: 'startTime',//期初时间
            dateReadFormat: 'Y-m-dTH:i:s',
            type: 'date'
        },{
            name: 'endTime',//期末时间
            dateReadFormat: 'Y-m-dTH:i:s',
            type: 'date'
        },]

});