Ext.define('MyApp.view.zbaoPayOrderManager', {
    extend: 'Ext.panel.Panel',
    id: 'zbaoPayOrderManager',
    closable: true,
    title: '充值明细',
    autoScroll: false,
    layout: "border",
    listeners: {
        'resize': function () {
            this.fundDetailGrid.setHeight(window.document.body.offsetHeight - 235);
        }
    },
    fundDetailWindow: null,
    getFundDetailWindow: function () {
        if (this.fundDetailWindow == null) {
            this.fundDetailWindow = new MyApp.view.FundDetailWindow();
        }
        return this.fundDetailWindow;
    },
    queryForm: null,
    getQueryForm: function () {
        var me = this;
        if (me.queryForm == null) {
            me.queryForm = Ext.widget('form', {
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
                    fromValue: new Date(new Date() - 30 * 24 * 60 * 60 * 1000),
                    toValue: new Date()
                }, {
                    fieldLabel: '主站登录账号',
                    labelWidth: 120,
                    name: 'loginAccount'
                }, DataDictionary.getDataDictionaryCombo('zbaoOrderType', {
                    fieldLabel: '充值类型',
                    labelWidth: 90,
                    name: 'orderType',
                    editable: false,
                    value: 0
                }), DataDictionary.getDataDictionaryCombo('zbaoOrderStatus', {
                    fieldLabel: '订单状态',
                    labelWidth: 90,
                    name: 'status',
                    editable: false,
                    value: 0
                }), {
                    fieldLabel: '外部订单号',
                    name: 'outOrderId'
                }, {
                    fieldLabel: '主站订单号',
                    name: 'zzOrderId'
                }, {
                    fieldLabel: '订单号',
                    name: 'orderId'
                }],
                buttons: [{
                    text: '重置',
                    handler: function () {
                        me.getQueryForm().getForm().reset();
                    }
                }, '->', {
                    text: '查询',
                    handler: function () {
                        me.getPagingToolbar().moveFirst();
                    }
                }]
            });
        }
        return me.queryForm;
    },
    toolbar: null,
    getToolbar: function () {
        var me = this;
        if (Ext.isEmpty(me.toolbar)) {
            me.toolbar = Ext.widget('toolbar', {
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
    getStore: function () {
        var me = this;
        if (me.store == null) {
            me.store = Ext.create('MyApp.store.zbaoPayOrderStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'zbaoPayOrder.loginAccount': Ext.String.trim(values.loginAccount),
                                    'startTime': values.createStartTime,
                                    'endTime': values.createEndTime,
                                    'zbaoPayOrder.orderId': Ext.String.trim(values.orderId),
                                    'zbaoPayOrder.outOrderId': Ext.String.trim(values.outOrderId),
                                    'zbaoPayOrder.zzOrderId': Ext.String.trim(values.zzOrderId),
                                    'zbaoPayOrder.orderType': values.orderType,
                                    'zbaoPayOrder.status': values.status
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
    getPagingToolbar: function () {
        var me = this;
        if (me.pagingToolbar == null) {
            me.pagingToolbar = Ext.widget('pagingtoolbar', {
                store: me.getStore(),
                dock: 'bottom',
                displayInfo: true
            });
        }
        return me.pagingToolbar;
    },
    pagingToolbar: null,
    getPagingToolbar: function () {
        var me = this;
        if (me.pagingToolbar == null) {
            me.pagingToolbar = Ext.widget('pagingtoolbar', {
                store: me.getStore(),
                dock: 'bottom',
                displayInfo: true
            });
        }
        return me.pagingToolbar;
    },
    fundDetailGrid: null,
    getFundDetailGrid: function () {
        var me = this;
        if (Ext.isEmpty(me.fundDetailGrid)) {
            me.fundDetailGrid = Ext.widget('gridpanel', {
                region: 'center',
                header: false,
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                }, {
                    dataIndex: 'loginAccount',
                    text: '主站登录账号',
                    flex: 1,
                    align: 'center'
                }, {
                    dataIndex: 'orderId',
                    text: '订单号',
                    flex: 1.2,
                    align: 'center'
                }, {
                    dataIndex: 'outOrderId',
                    text: '外部订单号',
                    flex: 1.2,
                    align: 'center'
                }, {
                    dataIndex: 'zzOrderId',
                    text: '主站订单号',
                    flex: 1.2,
                    align: 'center'
                }, {
                    dataIndex: 'orderType',
                    text: '充值类型',
                    flex: 1.2,
                    align: 'center',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'zbaoOrderType');
                    }
                }, {
                    dataIndex: 'status',
                    text: '订单状态 ',
                    flex: 1.2,
                    align: 'center',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'zbaoOrderStatus');
                    }
                }, {
                    dataIndex: 'amount',
                    text: '金额',
                    flex: 1,
                    align: 'center'
                }/*, {
                    dataIndex: 'remark',
                    text: '备注',
                    flex: 1,
                    align: 'center'
                }, {
                    dataIndex: 'paymentAccount',
                    text: '支付账户',
                    flex: 1,
                    align: 'center'
                }*/, {
                    xtype: 'datecolumn',
                    format: 'Y-m-d H:i:s',
                    dataIndex: 'createTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '创建时间'
                }, {
                    xtype: 'datecolumn',
                    format: 'Y-m-d H:i:s',
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
                dockedItems: [me.getToolbar(), me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'SINGLE',
                    listeners: {
                        select: function (rowModel, record, index, eOpts) {
                            var toolbar = me.getToolbar(),
                                editButton = toolbar.getComponent('editButton');
                            /* if(record.get('userType')==0){
                             editButton.disable();
                             }else{
                             editButton.enable();
                             }*/
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
                'zbaoPayOrder.loginAccount': Ext.String.trim(values.loginAccount),
                'startTime': values.createStartTime,
                'endTime': values.createEndTime,
                'zbaoPayOrder.orderId': Ext.String.trim(values.orderId),
                'zbaoPayOrder.outOrderId': Ext.String.trim(values.outOrderId),
                'zbaoPayOrder.zzOrderId': Ext.String.trim(values.zzOrderId),
                'zbaoPayOrder.orderType': values.orderType,
                'zbaoPayOrder.status': values.status
            };
            var p = "";
            for (var key in params) {
                var value = "";
                if (!Ext.isEmpty(params[key]))
                    value = params[key];
                p += key + "=" + value + "&";
            }
            window.open('./shpurchase/exportPayOrderFundDetail.action?' + p);
        }

    },
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(), me.getFundDetailGrid()]
        });
        me.callParent(arguments);
    }
});