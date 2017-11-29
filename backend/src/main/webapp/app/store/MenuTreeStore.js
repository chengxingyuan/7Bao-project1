/**
 * Created by 340096 on 2017/8/9.
 */
/*
 * 主菜单
 */
Ext.define('MyApp.store.MenuTreeStore', {
    extend: 'Ext.data.TreeStore',
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            fields: [{
                name: 'text'
            },{
                name: 'leaf'
            },{
                name: 'id'
            }],
            sorters: [{
                property: 'index',
                direction: 'ASC'
            }]
        }, cfg)]);
    }
});