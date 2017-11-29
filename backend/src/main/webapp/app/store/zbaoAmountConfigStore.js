/*
 * 收货模式
 */
Ext.define('MyApp.store.zbaoAmountConfigStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.zbaoAmountConfigModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.zbaoAmountConfigModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './shpurchase/queryZbaoServiceAmount.action',
                reader: {
                    type: 'json',
                    root: 'zbaoServiceAmountConfigList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});