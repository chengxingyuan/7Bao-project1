/*
 * 收货模式
 */
Ext.define('MyApp.model.zbaoAmountConfigModel', {
    extend: 'Ext.data.Model',
    idProperty: 'extId',
    idgen: 'uuid',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'rate',
        type:'float'
    },{
        name: 'minServiceAmount',
        type:'float'
    },{
        name: 'maxServiceAmount',
        type:'float'
    },{
        name: 'minAmount',
        type:'float'
    },{
        name: 'maxAmount',
        type:'float'
    }]
});