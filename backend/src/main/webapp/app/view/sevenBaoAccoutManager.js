/**
 * Created by 340096 on 2017/8/10.
 */

Ext.define('MyApp.view.userInfoWindow', {
    extend: 'Ext.window.Window',
    width: 800,
    title: '用户详情',
    closeAction: 'hide',
    modal: true,
    form: null,
    getForm: function () {
        var me = this;
        if (me.form == null) {
            me.form = Ext.widget('form', {
                layout: 'column',
                defaults: {
                    margin: '5 5 5 5',
                    columnWidth: .333,
                    labelWidth: 85,
                    xtype: 'textfield'
                },
                items: [{
                    name: 'loginAccount',
                    allowBlank: false,
                    fieldLabel: '5173账号'
                }, {
                    name: 'zbaoLoginAccount',
                    allowBlank: false,
                    readOnly: true,
                    fieldLabel: '7bao账号'
                }, {
                    name: 'name',
                    allowBlank: false,
                    fieldLabel: '姓名',
                    readOnly: true
                }, {
                    name: 'totalAmount',
                    allowBlank: false,
                    fieldLabel: '总金额'
                }, {
                    name: 'freezeAmount',
                    allowBlank: false,
                    fieldLabel: '冻结金额'
                }, {
                    name: 'availableAmount',
                    allowBlank: false,
                    fieldLabel: '可用金额'
                }, {
                    name: 'qq',
                    fieldLabel: 'QQ',
                    readOnly: true
                }, {
                    name: 'kefuqq',
                    fieldLabel: '客服QQ',
                    readOnly: true
                }, {
                    name: 'showName',
                    fieldLabel: '银行名称',
                    readOnly: true,
                }, {
                    name: 'accountNO',
                    fieldLabel: '银行卡号',
                    readOnly: true
                },
                    {
                        name: 'phoneNumber',
                        regex: /(^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$)|(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/,
                        regexText: '请输入正确的手机号码(11位)或电话(区号-电话号)！',
                        fieldLabel: '电话号码',
                        readOnly: true
                    },
                    {
                        name: 'createTime',
                        fieldLabel: '注册时间',
                         // xtype: 'datefield',
                        format: 'Y-m-d H:i:s',
                        readOnly: true
                    }, {
                        fieldLabel: '更新时间',
                        name: 'updateTime',
                         // xtype: 'datefield',
                        format: 'Y-m-d H:i:s',
                        readOnly: true
                    },
                    DataDictionary.getDataDictionaryCombo('zbaoAccount', {
                        fieldLabel: '是否启用',
                        name: 'isDeleted',
                        readOnly: true
                    }), DataDictionary.getDataDictionaryCombo('abc', {
                        fieldLabel: '用户绑定',
                        name: 'isUserBind',
                        readOnly: true,
                        value: '未绑定',
                    }) ,DataDictionary.getDataDictionaryCombo('abc', {
                        fieldLabel: '收货绑定',
                        name: 'isShBind',
                        readOnly: true,
                        value: '未绑定'
                    })

                    ],
                buttons: [
                    /*{
                     text:"保存",
                     formBind: true,
                     disabled: true,
                     handler: function() {
                     var tab = Ext.getCmp('sevenBaoAccoutManager'),
                     userGrid = tab.getUserGrid(),
                     userGridStore = userGrid.getStore(),
                     form = me.getForm(),
                     record = form.getRecord(),
                     url = './user/addUser.action',params,
                     message = '新增';
                     if(!form.isValid()){
                     return;
                     }
                     form.updateRecord(record);
                     var params = {
                     'sevenBaoAccountInfoEO.loginAccount': record.get('loginAccount'),
                     'sevenBaoAccountInfoEO.zbaoLoginAccount': record.get('zbaoLoginAccount'),
                     'sevenBaoAccountInfoEO.name': record.get('name'),
                     'sevenBaoAccountInfoEO.qq': record.get('qq'),
                     'sevenBaoAccountInfoEO.kefuqq': record.get('kefuqq'),
                     'sevenBaoAccountInfoEO.phoneNumber': record.get('phoneNumber'),
                     'sevenBaoAccountInfoEO.totalAmount': record.get('totalAmount'),
                     'sevenBaoAccountInfoEO.freezeAmount': record.get('freezeAmount'),
                     'sevenBaoAccountInfoEO.availableAmount': record.get('availableAmount'),

                     };
                     if(me.isUpdate){
                     url = './accout/modifyAccount.action';
                     message = '修改';
                     Ext.Object.merge(params,{
                     'id': record.get('id')
                     });
                     }

                     form.submit({
                     url : url,
                     params: params,
                     success : function(from, action, json) {
                     var sevenBaoAccoutManager = Ext.getCmp('sevenBaoAccoutManager'),
                     store = sevenBaoAccoutManager.getStore();
                     Ext.ux.Toast.msg("温馨提示", message + "成功");
                     me.close();
                     store.load();
                     },
                     exception : function(from, action, json) {
                     Ext.ux.Toast.msg("温馨提示", json.message);
                     }
                     });
                     }
                     }*/]
            });
        }
        return this.form;
    },
    isUpdate: null,
    bindData: function (record, isUpdate) {
        var me = this,
            form = me.getForm().getForm();
        form.reset();
        form.setValues(record);
    }
    ,
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getForm()]
        });
        me.callParent(arguments);
    }
});


/*
 * 编辑7bao账号窗口
 */
Ext.define('MyApp.view.accountWindow', {
    extend: 'Ext.window.Window',
    width: 700,
    title: '修改7bao用户账号信息',
    closeAction: 'hide',
    modal: true,
    form: null,
    getForm: function () {
        var me = this;
        if (me.form == null) {
            me.form = Ext.widget('form', {
                layout: 'column',
                defaults: {
                    margin: '5 5 5 5',
                    columnWidth: .333,
                    labelWidth: 85,
                    xtype: 'textfield'
                },
                items: [{
                    name: 'loginAccount',
                    allowBlank: false,
                    fieldLabel: '5173账号'
                }, {
                    name: 'zbaoLoginAccount',
                    allowBlank: false,
                    readOnly: true,
                    fieldLabel: '7bao账号'
                }, {
                    name: 'name',
                    allowBlank: false,
                    fieldLabel: '姓名'
                }, {
                    name: 'totalAmount',
                    allowBlank: false,
                    fieldLabel: '总金额'
                }, {
                    name: 'freezeAmount',
                    allowBlank: false,
                    fieldLabel: '冻结金额'
                }, {
                    name: 'availableAmount',
                    allowBlank: false,
                    fieldLabel: '可用金额'
                }, {
                    name: 'qq',
                    fieldLabel: 'QQ'
                }, {
                    name: 'kefuqq',
                    fieldLabel: '客服QQ'
                },
                    {
                        name: 'phoneNumber',
                        regex: /(^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$)|(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/,
                        regexText: '请输入正确的手机号码(11位)或电话(区号-电话号)！',
                        fieldLabel: '电话号码'
                    },],
                buttons: [
                    {
                        text: '保存',
                        formBind: true,
                        disabled: true,
                        handler: function () {
                            var tab = Ext.getCmp('sevenBaoAccoutManager'),
                                userGrid = tab.getUserGrid(),
                                userGridStore = userGrid.getStore(),
                                form = me.getForm(),
                                record = form.getRecord(),
                                url = './user/addUser.action', params,
                                message = '新增';
                            if (!form.isValid()) {
                                return;
                            }
                            form.updateRecord(record);
                            var params = {
                                'sevenBaoAccountInfoEO.loginAccount': record.get('loginAccount'),
                                'sevenBaoAccountInfoEO.zbaoLoginAccount': record.get('zbaoLoginAccount'),
                                'sevenBaoAccountInfoEO.name': record.get('name'),
                                'sevenBaoAccountInfoEO.qq': record.get('qq'),
                                'sevenBaoAccountInfoEO.kefuqq': record.get('kefuqq'),
                                'sevenBaoAccountInfoEO.phoneNumber': record.get('phoneNumber'),
                                'sevenBaoAccountInfoEO.totalAmount': record.get('totalAmount'),
                                'sevenBaoAccountInfoEO.freezeAmount': record.get('freezeAmount'),
                                'sevenBaoAccountInfoEO.availableAmount': record.get('availableAmount'),

                            };
                            if (me.isUpdate) {
                                url = './accout/modifyAccount.action';
                                message = '修改';
                                Ext.Object.merge(params, {
                                    'id': record.get('id')
                                });
                            }

                            form.submit({
                                url: url,
                                params: params,
                                success: function (from, action, json) {
                                    var sevenBaoAccoutManager = Ext.getCmp('sevenBaoAccoutManager'),
                                        store = sevenBaoAccoutManager.getStore();
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
        return this.form;
    },
    isUpdate: null,
    bindData: function (record, isUpdate) {
        var me = this,
            form = me.getForm().getForm(),
            loginAccount = form.findField('loginAccount'),
            totalAmount = form.findField('totalAmount')
        freezeAmount = form.findField('freezeAmount')
        availableAmount = form.findField('availableAmount')
        form.reset();
        form.loadRecord(record);
        me.isUpdate = isUpdate;
        if (isUpdate) {
            loginAccount.setReadOnly(true);
            totalAmount.setReadOnly(true);
            freezeAmount.setReadOnly(true);
            availableAmount.setReadOnly(true);
        } else {
            loginAccount.setReadOnly(false);
            totalAmount.setReadOnly(true);
            freezeAmount.setReadOnly(true);
            availableAmount.setReadOnly(true);
            form.reset();
        }

    }
    ,
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getForm()]
        });
        me.callParent(arguments);
    }
});


/*
 * 7bao账号管理页面
 */
Ext.define('MyApp.view.sevenBaoAccoutManager', {
    extend: 'Ext.panel.Panel',
    id: 'sevenBaoAccoutManager',
    closable: true,
    title: '7bao账号信息管理',
    ////////////////////////////////////////////////////
    autoScroll: false,
    listeners: {
        'resize': function () {
            this.userGrid.setHeight(window.document.body.offsetHeight - 190);
        }
    },
    ////////////////////////////////////////////////////
    toolbar: null,
    getToolbar: function () {
        var me = this;
        if (Ext.isEmpty(me.toolbar)) {
            me.toolbar = Ext.widget('toolbar', {
                dock: 'top',
                items: [
                    {
                        xtype: 'button',
                        itemId: 'editButton',
                        text: '编辑',
                        listeners: {
                            click: {
                                fn: me.modifyAccount,
                                scope: me
                            }
                        }
                    }, '-', {
                        xtype: 'button',
                        itemId: 'enableButton',
                        text: '启用',
                        listeners: {
                            click: {
                                fn: me.enableUser,
                                scope: me
                            }
                        }
                    }, '-', {
                        xtype: 'button',
                        itemId: 'disableButton',
                        text: '禁用',
                        listeners: {
                            click: {
                                fn: me.disableUser,
                                scope: me
                            }
                        }
                    }, {
                        text: '用户详情',
                        listeners: {
                            click: {
                                fn: me.userInfo,
                                scope: me
                            }
                        }
                    }
                ]
            });
        }
        return me.toolbar;
    },

    userWindow: null,
    getUserWindow: function () {
        if (this.userWindow == null) {
            this.userWindow = new MyApp.view.accountWindow();
        }
        return this.userWindow;
    },
    userInfoWindow: null,
    getWindow: function () {
        if (this.userInfoWindow == null) {
            this.userInfoWindow = new MyApp.view.userInfoWindow();
        }
        return this.userInfoWindow;
    },
    pwdWindow: null,
    getPwdWindow: function () {
        if (this.pwdWindow == null) {
            this.pwdWindow = new MyApp.view.pwdWindow();
        }
        return this.pwdWindow;
    },

    // 编辑账号信息
    modifyAccount: function (button, e, eOpts) {
        var grid = this.getUserGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = this.getUserWindow();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要编辑的7bao账号");
            return;
        }
        window.bindData(selRecords[0], true);
        window.show();
    },
  /*  userInfo: function (button, e, eOpts) {
        var grid = this.getUserGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(), order,
            window = this.getWindow();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要查看的详情用户");
            return;
        }
        /!*      order = selRecords[0];
         // window.show();
         window.bindData(order);
         window.show();
         *!/
        window.bindData(selRecords[0], true);
        window.show();
    },*/
    userInfo: function (button, e, eOpts) {
        var grid = this.getUserGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(), record,
            window = this.getWindow();
        record = selRecords[0];
        if (record == null || record.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要查看的用户详情");
            return;
        }
        var detailRecord=null;
                Ext.Ajax.request({
                    url: './accout/queryUserDetail.action',
                    params: {'id': record.get('id')},
                    success:function (response) {
                        detailRecord=Ext.decode(response.responseText).sevenBaoAccountInfoEO;
                        window.bindData(detailRecord, true);
                        window.show();
                    }
                });
    },

    // 禁用7bao账号
    disableUser: function (button, e, eOpts) {
        var grid = this.getUserGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的7bao账号");
            return;
        }
        var record = selRecords[0];
        if (record.get('isDeleted')) {
            Ext.ux.Toast.msg("温馨提示", "该7bao账号已经禁用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定禁用该7bao账号吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './accout/disableAccount.action',
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
    // 启用7bao账号
    enableUser: function (button, e, eOpts) {
        var grid = this.getUserGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要启用的7bao账号");
            return;
        }
        var record = selRecords[0];
        if (!record.get('isDeleted')) {
            Ext.ux.Toast.msg("温馨提示", "该7bao账号已经启用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定启用该7bao账号吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './accout/enableAccount.action',
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
    queryForm: null,
    getQueryForm: function () {
        var me = this;
        if (me.queryForm == null) {
            me.queryForm = Ext.widget('form', {
                layout: 'column',
                defaults: {
                    margin: '10 10 10 10',
                    columnWidth: .2,
                    labelWidth: 80,
                    xtype: 'textfield'
                },
                items: [
                    {
                        name: 'loginAccount',
                        fieldLabel: '5173账号'
                    },

                    {
                        name: 'name',
                        fieldLabel: '姓名'
                    }, DataDictionary.getDataDictionaryCombo('isUserBindSelect', {
                        fieldLabel: '新流程绑定',
                        labelWidth: 90,
                        name: 'isUserBind',
                        editable: false,
                        value: "-1"
                    }),DataDictionary.getDataDictionaryCombo('isUserBindSelect', {
                        fieldLabel: '收货绑定',
                        labelWidth: 90,
                        name: 'isShBind',
                        editable: false,
                        value: "-1"
                    })],
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
        return this.queryForm;
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
    store: null,
    getStore: function () {
        var me = this;
        if (me.store == null) {
            me.store = Ext.create('MyApp.store.sevenBaoAccoutStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues(),
                                /*  Ext.apply(operation, {
                                 params: {
                                 'sevenBaoAccountInfoEO.loginAccount': Ext.String.trim(values.loginAccount),
                                 'sevenBaoAccountInfoEO.name': Ext.String.trim(values.name)
                                 }
                                 });*/

                                params = {};
                            params = {
                                'sevenBaoAccountInfoEO.loginAccount': Ext.String.trim(values.loginAccount),
                                'sevenBaoAccountInfoEO.name': Ext.String.trim(values.name)
                            };
                            if (!Ext.isEmpty(values.isUserBind) && values.isUserBind != -1) {
                                params = Ext.Object.merge(params, {
                                    'sevenBaoAccountInfoEO.isUserBind': values.isUserBind
                                });
                            }
                            if (!Ext.isEmpty(values.isShBind) && values.isShBind != -1) {
                                params = Ext.Object.merge(params, {
                                    'sevenBaoAccountInfoEO.isShBind': values.isShBind
                                });
                            }
                            Ext.apply(operation, {
                                params: params
                            });
                        }
                    },
                }
            });
        }
        return me.store;
    },
    userGrid: null,
    getUserGrid: function () {
        var me = this;
        if (Ext.isEmpty(me.userGrid)) {
            me.userGrid = Ext.widget('gridpanel', {
                header: false,
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                }, {
                    dataIndex: 'loginAccount',
                    text: '5173账号',
                    align: 'center',
                    flex: 1.5
                }, {
                    dataIndex: 'zbaoLoginAccount',
                    text: '7bao账号',
                    align: 'center',
                    flex: 1.5
                },

                    {
                        dataIndex: 'name',
                        flex: 1,
                        align: 'center',
                        text: '姓名'
                    },

                    {
                        dataIndex: 'totalAmount',
                        flex: 1,
                        align: 'center',
                        text: '总金额'
                    },
                    {
                        dataIndex: 'freezeAmount',
                        flex: 1,
                        align: 'center',
                        text: '冻结金额'
                    },
                    {
                        dataIndex: 'availableAmount',
                        flex: 1,
                        align: 'center',
                        text: '可用资金'
                    }, {
                        dataIndex: 'phoneNumber',
                        flex: 1.5,
                        align: 'center',
                        text: '电话号码'
                    },
                    {
                        dataIndex: 'qq',
                        flex: 1,
                        align: 'center',
                        text: 'QQ'
                    },
                    {
                        dataIndex: 'kefuqq',
                        flex: 1,
                        align: 'center',
                        text: '客服QQ'
                    },
                    {
                        dataIndex: 'createTime',
                        flex: 1,
                        align: 'center',
                        text: '注册时间',
                        xtype: 'datecolumn',
                        format: 'Y-m-d H:i:s'
                    }, {
                        dataIndex: 'updateTime',
                        flex: 1,
                        align: 'center',
                        text: '更新时间',
                        xtype: 'datecolumn',
                        format: 'Y-m-d H:i:s'
                    },
                    {
                        dataIndex: 'isDeleted',
                        text: '是否启用',
                        flex: 1,
                        align: 'center',
                        renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                            return CommonFunction.rendererEnable(value);
                        }
                    },

                    {
                        dataIndex: 'isUserBind',
                        sortable: false,
                        width: 120,
                        renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                            return (value == true) ? "已绑定" : "未绑定";
                        },
                        align: 'center',
                        text: '新流程绑定'
                    },
                    {
                        dataIndex: 'isShBind',
                        sortable: false,
                        width: 120,
                        renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                            return (value == true) ? "已绑定" : "未绑定";
                        },
                        align: 'center',
                        text: '收货绑定'
                    }

                ],
                listeners: {
                    itemdblclick: function (view, records, item, index, e, eOpts) {
                         me.userInfo();
                            }
                },

                dockedItems: [me.getToolbar(), me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'SINGLE'
                   /* listeners: {
                        select: function (rowModel, record, index, eOpts) {
                            var toolbar = me.getToolbar(),
                                editButton = toolbar.getComponent('editButton');
                            if (record.get('userType') == 0) {
                                editButton.disable();
                            } else {
                                editButton.enable();
                            }
                        }
                    }*/
                })
            });
        }
        return me.userGrid;
    },
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(), me.getUserGrid()]
        });
        me.callParent(arguments);
    }
});
