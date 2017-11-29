/**
 * Created by 340096 on 2017/8/9.
 */
Ext.define('MyApp.store.FundStatisticsStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.FundStatisticsModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.FundStatisticsModel',
            sorters: [{
                property: 'startTime',
                direction: 'DESC'
            }],
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './fund/queryFundStatistics.action',
                reader: {
                    type: 'json',
                    root: 'fundStatisticsList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});


