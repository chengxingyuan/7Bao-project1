//7bao相关银行管理
Ext.define('MyApp.view.zbaoBankManager', {
    extend: 'Ext.panel.Panel',
    id: 'zbaoBankManager',
    layout: "border",
    closable: true,
    title: '银行管理',
    toolbar: null,
    getToolbar: function () {
        var me = this;
        if (Ext.isEmpty(me.toolbar)) {
            me.toolbar = Ext.widget('toolbar', {
                dock: 'top',
                items: [/*{
                    text: '添加银行信息',
                    listeners: {
                        click: {
                            fn: me.addBank,
                            scope: me
                        }
                    }
                }, '-',*/ {
                    text: '修改银行信息',
                    listeners: {
                        click: {
                            fn: me.modifyBank,
                            scope: me
                        }
                    }
                }, /*'-',{
                    text: '删除银行信息',
                    listeners: {
                        click: {
                            fn: me.deleteBank,
                            scope: me
                        }
                    }
                },*/ '-', {
                    xtype: 'button',
                    itemId: 'enableButton',
                    text: '启用',
                    listeners: {
                        click: {
                            fn: me.enableFirm,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    itemId: 'disableButton',
                    text: '禁用',
                    listeners: {
                        click: {
                            fn: me.disableFirm,
                            scope: me
                        }
                    }
                }, {
                        xtype: 'label',
                        style: "color: red;font-weight: bold;",
                        text: '*图片上传大小为 103*35 注意格式,避免前台报错'
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
        window.bindData(Ext.create('MyApp.model.zbaoBankModel'), false);
        window.show();
    },
    // 修改系统配置地信息
    modifyBank: function (button, e, eOpts) {
        var grid = this.getFirmGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = this.getUpdateFirm();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要修改银行信息");
            return;
        }
        window.bindData(selRecords[0], true);
        window.show();
    },
    // 启用
    enableFirm: function (button, e, eOpts) {
        var grid = this.getFirmGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要启用的银行");
            return;
        }
        var record = selRecords[0];
        if (record.get('enable')) {
            Ext.ux.Toast.msg("温馨提示", "该银行已经启用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定启用该银行吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './bank/enabled.action',
                    params: {'id': record.get('id')},
                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "启用成功！");
                        grid.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception: function (response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                });
            } else {
                return;
            }
        });
    },
    // 禁用
    disableFirm: function (button, e, eOpts) {
        var grid = this.getFirmGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的银行");
            return;
        }
        var record = selRecords[0];
        if (!record.get('enable')) {
            Ext.ux.Toast.msg("温馨提示", "该银行已经禁用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定禁用该银行吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './bank/disenable.action',
                    params: {'id': record.get('id')},
                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "禁用成功！");
                        grid.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception: function (response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                });
            } else {
                return;
            }
        });
    },
    //删除
    deleteBank: function () {
        var me = this,
            grid = me.getFirmGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要删除的银行信息");
            return;
        }
        var record = selRecords[0];
        Ext.MessageBox.confirm('温馨提示', '确定删除吗？', function (btn) {
            if (btn == 'yes') {
                me.getStore().remove(record);
                Ext.Ajax.request({
                    url: './bank/deleteBank.action',
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
                items: [Ext.create('Ext.form.ComboBox', {
                    fieldLabel: '银行名称',
                    labelWidth: 100,
                    name: 'name',
                    store: Ext.create('MyApp.store.zbaoBankStore', {
                        listeners: {
                            load: function (store, records, successful, eOpts) {
                                //添加
                                store.insert(0, {id: 0, name: '全部'});
                            }
                        }
                    }),
                    displayField: 'name',
                    valueField: 'name',
                    value: '全部',
                    editable: true
                })],
                buttons: [{
                    text: '查询',
                    handler: function () {
                        me.getPagingToolbar().moveFirst();
                    }
                }, '-', {
                    text: '重置',
                    handler: function () {
                        me.getQueryForm().getForm().reset();
                    }
                }, '->']
            });
        }
        return this.queryForm;
    },
    store: null,
    getStore: function () {
        var me = this;
        if (me.store == null) {
            me.store = Ext.create('MyApp.store.zbaoBankStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        var values = queryForm.getValues();
                        var name = queryForm.getForm().findField('name');
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            var params = {
                                'name': Ext.String.trim(name.getRawValue()),
                            }
                            Ext.apply(operation, {
                                params: params
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
                    dataIndex: 'name',
                    text: '银行名称',
                    flex: 1.7,
                    align: 'center'
                }, {
                    dataIndex: 'code',
                    text: '银行编码',
                    flex: 1.7,
                    align: 'center'
                }, {
                    dataIndex: 'imgUrl',
                    sortable: false,
                    flex: 2.0,
                    text: '银行图标',
                    align: 'center',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        if (value != null && value != undefined && value != '') {
                            return "<img src='" + "https://shouhuo.7bao.com" + value + "' height='100' />";
                        }
                        else {
                            return "";
                        }
                    }
                }, {
                    dataIndex: 'enable',
                    sortable: false,
                    flex: 1,
                    align: 'center',
                    text: '是否启用',
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
                    text: '创建时间 '
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
    title: '新增/修改银行信息',
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
                items: [/*{
                    fieldLabel: '银行名称',
                    allowBlank: false,
                    name: 'name'
                }, */{
                    fieldLabel: '银行图标',
                    allowBlank: true,
                    name: 'bankImg',
                    xtype: 'filefield',
                    vtype: 'image',
                    columnWidth: 1,
                    labelWidth: 130,
                }/*, DataDictionary.getDataDictionaryCombo('check', {
                    fieldLabel: '是否启用',
                    editable: false,
                    name: 'enable',
                    value: true
                })*/],
                buttons: [{
                    text: '保存',
                    handler: function () {
                        var formView = me.getForm(),
                            form = formView.getForm(), params,
                            record = form.getRecord(),
                            url = './bank/addBank.action',
                            message = '新增';
                        if (!form.isValid()) {
                            return;
                        }
                        form.updateRecord(record);
                        params = {
                          /*  'name': record.get('name'),*/
                            'bankImg': record.get('bankImg'),

                        };
                        if (me.isUpdate) {
                            url = './bank/modifyBank.action';
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
                                var gameConfigManager = Ext.getCmp('zbaoBankManager'),
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





