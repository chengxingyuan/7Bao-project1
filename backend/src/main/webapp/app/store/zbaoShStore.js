//出货地址配置
Ext.define('MyApp.store.zbaoShStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.zbaoShModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.zbaoShModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './shpurchase/queryConfig.action',
                reader: {
                    type: 'json',
                    root: 'configList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});