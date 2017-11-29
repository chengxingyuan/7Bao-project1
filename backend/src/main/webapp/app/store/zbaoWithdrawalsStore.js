/*
 * 资金明细
 */
Ext.define('MyApp.store.zbaoWithdrawalsStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.zbaoWithdrawalsModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.zbaoWithdrawalsModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './shpurchase/queryZbaoWithdrawals.action',
                reader: {
                    type: 'json',
                    root: 'zbaoWithdrawalsList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});