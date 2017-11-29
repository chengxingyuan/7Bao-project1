/*
 * 收货模式
 */
Ext.define('MyApp.store.zbaoBankStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.zbaoBankModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.zbaoBankModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './bank/queryBank.action',
                reader: {
                    type: 'json',
                    root: 'zbaoBankList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});