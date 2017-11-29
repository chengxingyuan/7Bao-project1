/**
 * Created by 340096 on 2017/8/9.
 */
Ext.define('MyApp.store.DictionaryTypeTreeStore',{
    extend: 'Ext.data.TreeStore',
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './rs/dictionary/queryDictionaryList',
                reader: {
                    type: 'json',
                    root: 'nodes'
                }
            },
            root: {
                id: '10000000',
                text: "7bao后台管理",
                entity: {
                    typeAlias: "mobile"
                },
                expanded: true
            },
            nodeParam: 'code',
            sorters: [{
                property: 'text',
                direction: 'ASC'
            }]
        }, cfg)]);
    }
});