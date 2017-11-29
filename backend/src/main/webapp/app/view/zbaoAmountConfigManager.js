//7bao相关银行管理
Ext.define('MyApp.view.zbaoAmountConfigManager', {
    extend: 'Ext.panel.Panel',
    id: 'zbaoAmountConfigManager',
    layout: "border",
    closable: true,
    title: '服务费配置管理',
    toolbar: null,
    getToolbar: function () {
        var me = this;
        if (Ext.isEmpty(me.toolbar)) {
            me.toolbar = Ext.widget('toolbar', {
                dock: 'top',
                items: [{
                    text: '添加服务费配置',
                    listeners: {
                        click: {
                            fn: me.addBank,
                            scope: me
                        }
                    }
                }, '-', {
                    text: '修改服务费配置',
                    listeners: {
                        click: {
                            fn: me.modifyBank,
                            scope: me
                        }
                    }
                }, '-',{
                    text: '删除服务费配置',
                    listeners: {
                        click: {
                            fn: me.deleteBank,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toolbar;
    },

    addFirmWindow: null,
    getAddFirmAccountWindow: function () {
        if (this.addFirmWindow == null) {
            this.addFirmWindow = new MyApp.view.updateZbaoBank();
        }
        return this.addFirmWindow;
    },

    addBank: function (button, e, eOpts) {
        var window = this.getAddFirmAccountWindow();
        window.bindData(Ext.create('MyApp.model.zbaoAmountConfigModel'), false);
        window.show();
    },
    // 修改系统配置地信息
    modifyBank: function (button, e, eOpts) {
        var grid = this.getFirmGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = this.getUpdateFirm();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要修改的配置信息");
            return;
        }
        window.bindData(selRecords[0], true);
        window.show();
    },
    //删除
    deleteBank: function () {
        var me = this,
            grid = me.getFirmGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要删除的配置信息");
            return;
        }
        var record = selRecords[0];
        Ext.MessageBox.confirm('温馨提示', '确定删除吗？', function (btn) {
            if (btn == 'yes') {
                me.getStore().remove(record);
                Ext.Ajax.request({
                    url: './shpurchase/deleteZbaoServiceAmount.action',
                    params: {'id': record.get('id')},
                    method: 'POST',

                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "删除成功！");
                        me.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception: function (response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.responseStatus.message);
                    }
                });
            } else {
                return;
            }
        });
    },

    queryForm: null,
    getQueryForm: function () {
        var me = this;
        if (me.queryForm == null) {
            me.queryForm = Ext.widget('form', {
                region: 'north',
                layout: 'column',
                defaults: {
                    margin: '10 10 10 10'
                },
            });
        }
        return this.queryForm;
    },
    store: null,
    getStore: function () {
        var me = this;
        if (me.store == null) {
            me.store = Ext.create('MyApp.store.zbaoAmountConfigStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        //var values = queryForm.getValues();
                        //var name = queryForm.getForm().findField('minServiceAmount');
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                           // var params = {
                               // 'minServiceAmount': Ext.String.trim(name.getRawValue()),
                           // }
                            Ext.apply(operation, {
                            //    params: params
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
    updateZbaoBank: null,
    getUpdateFirm: function () {
        if (this.updateWindow == null) {
            this.updateWindow = new MyApp.view.updateZbaoBank();
        }
        return this.updateWindow;
    },
    //主页面显示页面
    FirmGrid: null,
    getFirmGrid: function () {
        var me = this;
        if (Ext.isEmpty(me.FirmGrid)) {
            me.FirmGrid = Ext.widget('gridpanel', {
                region: 'center',
                header: false,
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                }, {
                    dataIndex: 'minServiceAmount',
                    text: '最少服务费',
                    flex: 1.7,
                    align: 'center'
                },{
                    dataIndex: 'maxServiceAmount',
                    sortable: false,
                    flex: 1,
                    text: '最多服务费',
                    align:'center',
                },{
                    dataIndex: 'minAmount',
                    sortable: false,
                    flex: 1,
                    text: '最少提现金额',
                    align:'center',
                },{
                    dataIndex: 'maxAmount',
                    sortable: false,
                    flex: 1,
                    text: '最大提现金额',
                    align:'center',
                },{
                    dataIndex: 'rate',
                    sortable: false,
                    flex: 1,
                    text: '费率',
                    align:'center',
                }],
                dockedItems: [me.getToolbar(), me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'SINGLE'
                })
            });
        }
        return me.FirmGrid;
    },
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(), me.getFirmGrid()]
        });
        me.callParent(arguments);
    }
});
Ext.define('MyApp.view.updateZbaoBank', {
    extend: 'Ext.window.Window',
    title: '新增/修改服务费配置信息',
    width: 600,
    closeAction: 'hide',
    modal: true,
    form: null,
    getForm: function () {
        var me = this;
        if (me.form == null) {
            me.form = Ext.widget('form', {
                layout: 'column',
                defaults: {
                    margin: '10 10 5 5',
                    columnWidth: .5,
                    labelWidth: 130,
                    xtype: 'textfield'
                },
                items: [{
                    fieldLabel: '最少服务费',
                    allowBlank: false,
                    name: 'minServiceAmount'
                }, {
                    fieldLabel: '最多服务费',
                    allowBlank: false,
                    name: 'maxServiceAmount',
                }, {
                    fieldLabel: '最少提现金额',
                    allowBlank: false,
                    name: 'minAmount',
                }, {
                    fieldLabel: '最大提现金额',
                    allowBlank: false,
                    name: 'maxAmount',
                }, {
                    fieldLabel: '费率',
                    allowBlank: false,
                    name: 'rate',
                }],
                buttons: [{
                    text: '保存',
                    handler: function () {
                        var formView = me.getForm(),
                            form = formView.getForm(), params,
                            record = form.getRecord(),
                            url = './shpurchase/addZbaoServiceAmount.action',
                            message = '新增';
                        if (!form.isValid()) {
                            return;
                        }
                        form.updateRecord(record);
                        params = {
                            'maxAmount': record.get('maxAmount'),
                            'minAmount': record.get('minAmount'),
                            'maxServiceAmount': record.get('maxServiceAmount'),
                            'minServiceAmount': record.get('minServiceAmount'),
                            'rate': record.get('rate'),

                        };
                        if (me.isUpdate) {
                            url = './shpurchase/updateZbaoServiceAmount.action';
                            message = '修改';
                            Ext.Object.merge(params, {
                                'id': record.get('id'),
                            });
                        }
                        form.submit({
                            url: url,
                            method: 'POST',
                            params: params,
                            success: function (from, action, json) {
                                var gameConfigManager = Ext.getCmp('zbaoAmountConfigManager'),
                                    store = gameConfigManager.getStore();
                                Ext.ux.Toast.msg("温馨提示", message + "成功");
                                me.close();
                                store.load();
                            },
                            exception: function (from, action, json) {
                                Ext.ux.Toast.msg("温馨提示", json.message);
                            }
                        });
                    }
                }]
            });
        }
        return me.form;
    },
    isUpdate: null,
    bindData: function (record, isUpdate) {
        var me = this,
            form = me.getForm().getForm();
        form.reset();
        form.loadRecord(record);
        me.isUpdate = isUpdate;
        if (!isUpdate) {
            form.reset();
        }
    },
    constructor: function (config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [me.getForm()]
        me.callParent([cfg]);
    }
});





