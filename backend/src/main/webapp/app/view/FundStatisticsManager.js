/**
 * Created by 340032 on 2017/8/28.
 */
Ext.define('MyApp.view.FundStatisticsManager', {
    extend: 'Ext.panel.Panel',
    id:'FundStatisticsManager',
    closable: true,
    title: '资金统计',
    autoScroll: false,
    layout: "border",
    shFundStatisticsWindow: null,
    getShFundStatisticsWindow: function(){
        if(this.shFundStatisticsWindow == null){
            this.shFundStatisticsWindow = new MyApp.view.ShFundStatisticsWindow();
        }
        return this.shFundStatisticsWindow;
    },
    queryForm: null,
    getQueryForm: function(){
        var me = this;
        if(me.queryForm==null){
            me.queryForm = Ext.widget('form',{
                layout: 'column',
                region: 'north',
                defaults: {
                    margin: '10 10 10 10',
                    xtype: 'textfield'
                },
                items: [{
                    fieldLabel: ' 期初日期',
                    xtype: 'rangedatefield',
                    fromName: 'createStartTime',
                    toName: 'createEndTime',
                    fromValue: new Date(new Date()-30*24*60*60*1000),
                    toValue: new Date()
                }],
                buttons: [{
                    text:'重置',
                    handler: function() {
                        me.getQueryForm().getForm().reset();
                    }
                },,'->',{
                    text:'查询',
                    handler: function() {
                        me.getPagingToolbar().moveFirst();
                    }
                }]
            });
        }
        return me.queryForm;
    },
    toolbar: null,
    getToolbar: function(){
        var me = this;
        if(Ext.isEmpty(me.toolbar)){
            me.toolbar = Ext.widget('toolbar',{
                dock: 'top',
                items: [{
                    text: '导出',
                    handler: function () {
                        me.exportPurchaseGameAccount();
                    }
                }]
            });
        }
        return me.toolbar;
    },
    store: null,
    getStore: function(){
        var me = this;
        if(me.store==null){
            me.store = Ext.create('MyApp.store.FundStatisticsStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'startTime':values.createStartTime,
                                    'endTime':values.createEndTime
                                }
                            });
                        }
                    }
                }
            });
        }
        return me.store;
    },
    pagingToolbar: null,
    getPagingToolbar: function(){
        var me = this;
        if(me.pagingToolbar==null){
            me.pagingToolbar = Ext.widget('pagingtoolbar',{
                store: me.getStore(),
                dock: 'bottom',
                displayInfo: true
            });
        }
        return me.pagingToolbar;
    },
    shFundStatisticsGrid: null,
    getShFundStatisticsGrid: function(){
        var me = this;
        if(Ext.isEmpty(me.shFundStatisticsGrid)){
            me.shFundStatisticsGrid = Ext.widget('gridpanel',{
                header: false,
                region: 'center',
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'qcBalance',
                    text: '期初余额',
                    flex:  1,
                    align: 'center'
                },{
                    dataIndex: 'zfAmount',
                    text: '支付金额',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'txAmount',
                    text: '提现金额',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'fkAmount',
                    text: '采购款金额',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'txServiceAmount',
                    text: '提现服务费',
                    flex: 1,
                    align: 'center'
                },{
                    dataIndex: 'sdZfAmount',
                    text: '售得充值',
                    flex: 1,
                    align: 'center'
                },{
                    dataIndex: 'oldFund',
                    text: '老流程剩余资金',
                    flex: 1,
                    align: 'center'
                },{
                    dataIndex: 'qmBalance',
                    text: '期末余额',
                    flex: 1,
                    align: 'center'
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'startTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '期初时间'
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'endTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '期末时间'
                },{
                    dataIndex: 'processing',
                    text: '提现处理中金额',
                    flex: 1,
                    align: 'center'
                }],
                dockedItems: [me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI'
                }),
                dockedItems: [me.getToolbar(),me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'SINGLE',
                    listeners: {
                        select: function(rowModel, record, index, eOpts){
                            var toolbar = me.getToolbar(),
                                editButton = toolbar.getComponent('editButton');
                            if(record.get('userType')==0){
                                editButton.disable();
                            }else{
                                editButton.enable();
                            }
                        }
                    }
                })
            });
        }
        return me.shFundStatisticsGrid;

    },

    exportPurchaseGameAccount: function (button, e, eOpts) {
    var me = this;
    var queryForm = me.getQueryForm();

    if (queryForm != null) {
        var values = queryForm.getValues();
        var params = {

            'startTime':values.createStartTime,
            'endTime':values.createEndTime,

        };
        var p = "";
        for (var key in params) {
            var value = "";
            if (!Ext.isEmpty(params[key]))
                value = params[key];
            p += key + "=" + value + "&";
        }
        //window.open('./fund/exportFundExcel.action');
        window.open('./fund/exportFundExcel.action?' + p);
    }

},
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getShFundStatisticsGrid()]
        });
        me.callParent(arguments);
    }
});