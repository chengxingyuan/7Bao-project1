/*
 * 资金明细
 */
Ext.define('MyApp.store.zbaoPayOrderStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.zbaoPayOrderModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.zbaoPayOrderModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './shpurchase/queryPayOrder.action',
                reader: {
                    type: 'json',
                    root: 'zbaoPayOrderList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});