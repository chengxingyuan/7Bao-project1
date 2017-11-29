/**
 * Created by 340096 on 2017/8/10.
 */
/*
 * 7bao账号数据
 */

Ext.define('MyApp.store.sevenBaoAccoutStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.sevenBaoAccoutModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.sevenBaoAccoutModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                //url: './user/queryUser.action',
                url: './accout/querySevenBaoAccount.action',
                reader: {
                    type: 'json',
                    root: 'userList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});
// Ext.define('MyApp.store.sevenBaoAccoutStore', {
//     extend: 'Ext.data.Store',
//     requires: [
//         'MyApp.model.sevenBaoAccoutModel'
//     ],
//     constructor: function(cfg) {
//         var me = this;
//         cfg = cfg || {};
//         me.callParent([Ext.apply({
//             model: 'MyApp.model.sevenBaoAccoutModel',
//             proxy: {
//                 type: 'ajax',
//                 actionMethods: 'POST',
//                 url: './accout/querySevenBaoAccount111.action',
//                 reader: {
//                     type: 'json',
//                     root: 'userList',
//                     totalProperty : 'totalCount'
//                 }
//             }
//         }, cfg)]);
//     }
// });