/*
 * 资金明细
 */
Ext.define('MyApp.store.zbaoTransferStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.zbaoTransferModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.zbaoTransferModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './shpurchase/queryZbaoTransfer.action',
                reader: {
                    type: 'json',
                    root: 'zbaoTransferList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});