Ext.define('MyApp.view.zbaoFundDetailManager', {
    extend: 'Ext.panel.Panel',
    id: 'zbaoFundDetailManager',
    closable: true,
    title: '资金明细',
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
                    fieldLabel: '买家账号',
                    labelWidth: 120,
                    name: 'loginAccount'
                }, DataDictionary.getDataDictionaryCombo('zbaoFundDetailType', {
                    fieldLabel: '类型',
                    labelWidth: 90,
                    name: 'type',
                    editable: false,
                    value: 0
                }), {
                    fieldLabel: '充值单号',
                    name: 'czOrderId'
                }, {
                    fieldLabel: '提现订单号',
                    name: 'txOrderId'
                }, {
                    fieldLabel: '采购转账单号',
                    name: 'cgOrderId'
                }, {
                    fieldLabel: '外部单号',
                    name: 'outOrderId'
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
            me.store = Ext.create('MyApp.store.zbaoFundDetailStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'zbaoFundDetail.loginAccount': Ext.String.trim(values.loginAccount),
                                    'startTime': values.createStartTime,
                                    'endTime': values.createEndTime,
                                    'zbaoFundDetail.czOrderId': Ext.String.trim(values.czOrderId),
                                    'zbaoFundDetail.txOrderId': Ext.String.trim(values.txOrderId),
                                    'zbaoFundDetail.cgOrderId': Ext.String.trim(values.cgOrderId),
                                    'zbaoFundDetail.outOrderId': Ext.String.trim(values.outOrderId),
                                    'zbaoFundDetail.type': values.type
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
                    text: '买家账号',
                    flex: 1,
                    align: 'center'
                }, {
                    dataIndex: 'czOrderId',
                    text: '充值单号',
                    flex: 1.2,
                    align: 'center'
                }, {
                    dataIndex: 'txOrderId',
                    text: '提现订单号',
                    flex: 1.2,
                    align: 'center'
                }, {
                    dataIndex: 'cgOrderId',
                    text: '采购转账订单号',
                    flex: 1.2,
                    align: 'center'
                }, {
                    dataIndex: 'outOrderId',
                    text: '外部订单号',
                    flex: 1.2,
                    align: 'center'
                }, {
                    dataIndex: 'type',
                    text: '资金类型 ',
                    flex: 1.3,
                    sortable: false,
                    align: 'center',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'zbaoFundDetailType');
                    }
                }, {
                    dataIndex: 'amount',
                    text: '金额',
                    flex: 1,
                    align: 'center'
                }, {
                    xtype: 'linebreakcolumn',
                    dataIndex: 'log',
                    text: '日志详情',
                    flex: 2.2,
                    align: 'center'
                }, {
                    dataIndex: 'isSuccess',
                    sortable: false,
                    text: '操作是否成功',
                    flex: 1,
                    align: 'center',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'check');
                    }
                }, {
                    xtype: 'datecolumn',
                    format: 'Y-m-d H:i:s',
                    dataIndex: 'createTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '操作时间'
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
                'zbaoFundDetail.loginAccount': Ext.String.trim(values.loginAccount),
                'startTime': values.createStartTime,
                'endTime': values.createEndTime,
                'zbaoFundDetail.czOrderId': Ext.String.trim(values.czOrderId),
                'zbaoFundDetail.txOrderId': Ext.String.trim(values.txOrderId),
                'zbaoFundDetail.cgOrderId': Ext.String.trim(values.cgOrderId),
                'zbaoFundDetail.outOrderId': Ext.String.trim(values.outOrderId),
                'zbaoFundDetail.type': values.type

            };
            var p = "";
            for (var key in params) {
                var value = "";
                if (!Ext.isEmpty(params[key]))
                    value = params[key];
                p += key + "=" + value + "&";
            }
            //window.open('./fund/exportFundExcel.action');
            window.open('./shpurchase/exportFundDetail.action?' + p);
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