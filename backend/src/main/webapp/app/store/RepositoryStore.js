/**
 * Created by 340096 on 2017/8/9.
 */
Ext.define('MyApp.store.RepositoryStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.GoodsModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            pageSize: 50,
            model: 'MyApp.model.GoodsModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './rs/goods/queryCurrencyRepository',
                reader: {
                    type: 'json',
                    root: 'goods',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});