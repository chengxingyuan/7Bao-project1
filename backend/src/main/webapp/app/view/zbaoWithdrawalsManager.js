Ext.define('MyApp.view.zbaoWithdrawalsManager', {
    extend: 'Ext.panel.Panel',
    id:'zbaoWithdrawalsManager',
    closable: true,
    title: '提现明细',
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
                    fieldLabel: '主站登录账号',
                    labelWidth: 120,
                    name: 'loginAccount'
                },DataDictionary.getDataDictionaryCombo('zbaoWithdrawalsType',{
                    fieldLabel: '提现状态',
                    labelWidth: 90,
                    name: 'type',
                    editable: false,
                    value: 0
                }),{
                    fieldLabel: '提现账号',
                    name: 'cardCode'
                },{
                    fieldLabel: '订单号',
                    name: 'orderId'
                },{
                    fieldLabel: '开户姓名',
                    name: 'realName'
                },{
                    fieldLabel: '开户银行',
                    name: 'openbank'
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
            me.store = Ext.create('MyApp.store.zbaoWithdrawalsStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'zbaoWithdrawals.loginAccount': Ext.String.trim(values.loginAccount),
                                    'startTime':values.createStartTime,
                                    'endTime':values.createEndTime,
                                    'zbaoWithdrawals.type':values.type,
                                    'zbaoWithdrawals.cardCode': Ext.String.trim(values.cardCode),
                                    'zbaoWithdrawals.orderId': Ext.String.trim(values.orderId),
                                    'zbaoWithdrawals.realName': values.realName,
                                    'zbaoWithdrawals.openbank': values.openbank

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
                    text: '主站登录账号',
                    flex:  1.5,
                    align: 'center'
                },{
                    dataIndex: 'orderId',
                    text: '订单号',
                    flex: 2.0,
                    align: 'center'
                },{
                    dataIndex: 'cardCode',
                    text: '提现账号',
                    flex: 2.0,
                    align: 'center'
                },{
                    dataIndex: 'type',
                    text: '提现状态',
                    flex: 1.2,
                    align: 'center',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value,'zbaoWithdrawalsType');
                    }
                },{
                    dataIndex: 'serviceAmount',
                    text: '服务费',
                    flex: 0.7,
                    align: 'center',
                },{
                    dataIndex: 'amount',
                    text: '提现金额',
                    flex: 1,
                    align: 'center'
                },{
                    dataIndex: 'fundOnWay',
                    text: '在途资金',
                    flex: 1,
                    align: 'center'
                }/*,{
                    dataIndex: 'bankNameType',
                    text: '提现渠道类型',
                    flex: 1.2,
                    align: 'center'
                }*/,{
                    dataIndex: 'bankName',
                    text: '渠道名称',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'realName',
                    text: '开户姓名',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'provinceName',
                    text: '省份',
                    flex: 1,
                    align: 'center'
                },{
                    dataIndex: 'cityName',
                    text: '城市',
                    flex: 1.2,
                    align: 'center'
                },/*{
                    dataIndex: 'areacode',
                    text: '区号',
                    flex: 1,
                    align: 'center'
                },{
                    dataIndex: 'openbank',
                    text: '开户银行',
                    flex: 1.2,
                    align: 'center'
                },*/{
                    xtype: 'linebreakcolumn',
                    dataIndex: 'log',
                    text: '日志信息 ',
                    flex: 3.5,
                    align: 'center',
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
                }), dockedItems: [me.getToolbar(),me.getPagingToolbar()],
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
        return me.fundDetailGrid;

    },

    exportPurchaseGameAccount: function (button, e, eOpts) {
        var me = this;
        var queryForm = me.getQueryForm();

        if (queryForm != null) {
            var values = queryForm.getValues();
            var params = {
                'zbaoWithdrawals.loginAccount': Ext.String.trim(values.loginAccount),
                'startTime':values.createStartTime,
                'endTime':values.createEndTime,
                'zbaoWithdrawals.type':values.type,
                'zbaoWithdrawals.cardCode': Ext.String.trim(values.cardCode),
                'zbaoWithdrawals.orderId': Ext.String.trim(values.orderId),
                'zbaoWithdrawals.realName': values.realName,
                'zbaoWithdrawals.openbank': values.openbank

            };
            var p = "";
            for (var key in params) {
                var value = "";
                if (!Ext.isEmpty(params[key]))
                    value = params[key];
                p += key + "=" + value + "&";
            }
            //window.open('./fund/exportFundExcel.action');
            window.open('./shpurchase/exportFundExcel.action?' + p);
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