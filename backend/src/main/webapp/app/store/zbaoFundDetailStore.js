/*
 * 资金明细
 */
Ext.define('MyApp.store.zbaoFundDetailStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.zbaoFundDetailModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.zbaoFundDetailModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './shpurchase/queryZbaoFundDetail.action',
                reader: {
                    type: 'json',
                    root: 'detailList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});