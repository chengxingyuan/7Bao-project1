Ext.define('MyApp.view.zbaoTransferManager', {
    extend: 'Ext.panel.Panel',
    id:'zbaoTransferManager',
    closable: true,
    title: '采购转账明细',
    autoScroll: false,
    layout: "border",
    listeners:{
        'resize':function(){
            this.fundDetailGrid.setHeight(window.document.body.offsetHeight-235);
        }
    },
    fundDetailWindow: null,
    getFundDetailWindow: function(){
        if(this.fundDetailWindow == null){
            this.fundDetailWindow = new MyApp.view.FundDetailWindow();
        }
        return this.fundDetailWindow;
    },
    queryForm: null,
    getQueryForm: function(){
        var me = this;
        if(me.queryForm==null){
            me.queryForm = Ext.widget('form',{
                region: 'north',
                layout: 'column',

                defaults: {
                    margin: '10 10 10 10',
                    xtype: 'textfield'
                },
                items: [{
                    fieldLabel: '创建日期',
                    xtype: 'rangedatefield',
                    fromName: 'createStartTime',
                    toName: 'createEndTime',
                    fromValue: new Date(new Date()-30*24*60*60*1000),
                    toValue: new Date()
                },{
                    fieldLabel: '登录账号',
                    labelWidth: 120,
                    name: 'loginAccount'
                },DataDictionary.getDataDictionaryCombo('zbaoWithdrawalsStatus',{
                    fieldLabel: '转账状态',
                    labelWidth: 90,
                    name: 'status',
                    editable: false,
                    value: 0
                }),{
                    fieldLabel: '5173订单号',
                    name: 'shOrderId'
                },{
                    fieldLabel: '订单号',
                    name: 'orderId'
                }],
                buttons: [{
                    text:'重置',
                    handler: function() {
                        me.getQueryForm().getForm().reset();
                    }
                },'->',{
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
            me.store = Ext.create('MyApp.store.zbaoTransferStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'zbaoTransfer.loginAccount': Ext.String.trim(values.loginAccount),
                                    'startTime':values.createStartTime,
                                    'endTime':values.createEndTime,
                                    'zbaoTransfer.orderId': Ext.String.trim(values.orderId),
                                    'zbaoTransfer.shOrderId': Ext.String.trim(values.shOrderId),
                                    'zbaoTransfer.status': values.status
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
    fundDetailGrid: null,
    getFundDetailGrid: function(){
        var me = this;
        if(Ext.isEmpty(me.fundDetailGrid)){
            me.fundDetailGrid = Ext.widget('gridpanel',{
                region: 'center',
                header: false,
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'loginAccount',
                    text: '登录账号',
                    flex:  1,
                    align: 'center'
                },{
                    dataIndex: 'orderId',
                    text: '订单号',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'shOrderId',
                    text: '5173订单号',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'status',
                    text: '转账状态',
                    flex: 1.2,
                    align: 'center',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value,'zbaoWithdrawalsStatus');
                    }
                },{
                    dataIndex: 'amount',
                    text: '金额',
                    flex: 1,
                    align: 'center'
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'createTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '创建时间'
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'dealTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '结束时间'
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
                        select: function (rowModel, record, index, eOpts) {
                            var toolbar = me.getToolbar(),
                                editButton = toolbar.getComponent('editButton');
                            if (record.get('userType') == 0) {
                                editButton.disable();
                            } else {
                                editButton.enable();
                            }
                        }
                    }
                })

            });
        }
        return me.fundDetailGrid;
    },
    exportPurchaseGameAccount: function (button, e, eOpts) {
        var me = this;
        var queryForm = me.getQueryForm();

        if (queryForm != null) {
            var values = queryForm.getValues();
            var params = {
                'zbaoTransfer.loginAccount': Ext.String.trim(values.loginAccount),
                'startTime':values.createStartTime,
                'endTime':values.createEndTime,
                'zbaoTransfer.orderId': Ext.String.trim(values.orderId),
                'zbaoTransfer.shOrderId': Ext.String.trim(values.shOrderId),
                'zbaoTransfer.status': values.status
            };
            var p = "";
            for (var key in params) {
                var value = "";
                if (!Ext.isEmpty(params[key]))
                    value = params[key];
                p += key + "=" + value + "&";
            }
            //window.open('./fund/exportFundExcel.action');
            window.open('./shpurchase/exportTransferExcel.action?' + p);
        }

    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getFundDetailGrid()]
        });
        me.callParent(arguments);
    }
});