/**
 * Created by 340096 on 2017/8/9.
 */
//定义区域树结构
Ext.define('MyApp.view.AreaTree', {
    extend:'Ext.tree.Panel',
    title: '区域选择树',
    animate: false,
    useArrows: true,
    rootVisible: true,
    height : 385,
    dsSelectIds: [],
    selectIds: null,
    getCheckedIds: function() {
        var me = this,
            dsSelectIds = me.dsSelectIds,
            selectIds = me.selectIds,
            checkedNodes = this.getChecked(),
            ids = [];
        for (var t = 0; t < checkedNodes.length; t++) {
            ids[t] = checkedNodes[t].data.id;
        }
        if(!Ext.isEmpty(selectIds)){
            ids = Ext.Array.merge(selectIds, ids);
        }
        if(!Ext.isEmpty(dsSelectIds)&&dsSelectIds.length>0){
            Ext.Array.forEach(dsSelectIds, function(item, index, array){
                if(Ext.Array.contains(ids, item)){
                    Ext.Array.remove(ids, item);
                }
            })
        }
        return ids;
    },
    checkable: null,
    record: null,
    bindData: function(record, checkable){
        var me = this;
        me.record = record;
        me.checkable = checkable;
        me.getStore().load();
        me.getRootNode().expand();
    },
    constructor: function(config){
        var me = this,
            cfg = Ext.apply({}, config);
        me.store = Ext.create('MyApp.store.AreaTreeStore',{
            root: {
                id: '1',
                text: "金华利诚"
            },
            listeners: {
                beforeload: function(store, operation, eOpts) {
                    Ext.apply(operation, {
                        params: {
                            'deptType': 1,
                            'checkable': me.checkable,
                            'uid': me.record.get('id')
                        }
                    });
                },
                load: function(store, node, records, successful, eOpts){
                    var rawData = store.getProxy().getReader().rawData;
                    me.selectIds = rawData.areaIds;
                }
            }
        });
        me.listeners = {
            checkchange: function(node, checked, eOpts ){
                var me = this,
                    dsSelectIds = me.dsSelectIds;
                if(!checked){
                    Ext.Array.push(dsSelectIds, node.data.id);
                }else{
                    Ext.Array.remove(dsSelectIds, node.data.id);
                }
            }
        };
        me.callParent([cfg]);
    }
});

//定义部门结构树
Ext.define('MyApp.view.DeptTree', {
    extend:'Ext.tree.Panel',
    title: '部门选择树',
    animate: false,
    useArrows: true,
    rootVisible: true,
    height : 385,
    selectIds: null,
    getCheckedIds: function() {
        var me = this,
            selectIds = me.selectIds,
            checkedNodes = this.getChecked(),
            ids = [];
        for (var t = 0; t < checkedNodes.length; t++) {
            ids[t] = checkedNodes[t].data.id;
        }
        if(!Ext.isEmpty(ids)&&ids.length>0){
            return ids;
        }else{
            return selectIds;
        }
    },
    checkable: null,
    record: null,
    bindData: function(record, checkable){
        var me = this;
        me.record = record;
        me.checkable = checkable;
        me.getStore().load();
        me.getRootNode().expand();
    },
    constructor: function(config){
        var me = this,
            cfg = Ext.apply({}, config);
        me.store = Ext.create('MyApp.store.DepartmentTreeStore',{
            root: {
                id: '1',
                text: "金华利诚"
            },
            listeners: {
                beforeload: function(store, operation, eOpts) {
                    Ext.apply(operation, {
                        params: {
                            'deptType': 2,
                            'checkable': me.checkable,
                            'uid': me.record.get('id')
                        }
                    });
                },
                load: function(store, node, records, successful, eOpts){
                    var rawData = store.getProxy().getReader().rawData;
                    me.selectIds = rawData.deptIds;
                }
            }
        });
        me.listeners = {
            checkchange: function(node, checked, eOpts ){
                if(checked){
                    var checkedNodes = this.getChecked();
                    for (var t = 0; t < checkedNodes.length; t++) {
                        checkedNodes[t].set('checked',false);
                    }
                    node.set('checked', true);
                }
            }
        };
        me.callParent([cfg]);
    }
});

/**
 * 用户区域部门编辑窗口
 */
Ext.define('MyApp.view.UserAreaDeptWindow',{
    extend : 'Ext.window.Window',
    title: '用户区域部门编辑',
    width:700,
    closable : true,
    modal : true,
    closeAction : 'hide',
    layout:'column',
    resizable:false,
    bindData: function(record){
        var me = this,
            form = me.getForm().getForm(),
            areaTree = me.getAreaTree(),
            deptTree = me.getDeptTree();
        form.reset();
        form.loadRecord(record);
        areaTree.bindData(record, true);
        deptTree.bindData(record, true);
    },
    areaTree: null,
    getAreaTree: function(){
        var me = this;
        if(Ext.isEmpty(me.areaTree)){
            me.areaTree = Ext.create('MyApp.view.AreaTree',{
                columnWidth: .5,
                height:313
            });
        }
        return me.areaTree;
    },
    deptTree: null,
    getDeptTree: function(){
        var me = this;
        if(Ext.isEmpty(me.deptTree)){
            me.deptTree = Ext.create('MyApp.view.DeptTree',{
                columnWidth: .5,
                height:313
            });
        }
        return me.deptTree;
    },
    form: null,
    getForm: function(){
        var me = this;
        if(me.form==null){
            me.form = Ext.widget('form',{
                columnWidth: 1,
                layout: 'column',
                defaults: {
                    margin: '15 5 15 5',
                    columnWidth: .333,
                    labelWidth: 85,
                    xtype: 'textfield'
                },
                items: [{
                    name: 'loginAccount',
                    fieldLabel: '登录名(邮箱)'
                },{
                    name: 'realName',
                    fieldLabel: '真实姓名'
                },{
                    name: 'phoneNumber',
                    fieldLabel: '电话号码'
                }]
            });
        }
        return this.form;
    },
    buttons: [{
        text:'保存',
        handler: function() {
            var me = this.up('window'),
                form = me.getForm().getForm(),
                areaTree = me.getAreaTree(),
                deptTree = me.getDeptTree(),
                record = form.getRecord(),
                url = './rs/user/authUserAreaDept';
            if(!form.isValid()){
                return;
            }
            form.updateRecord(record);
            Ext.Ajax.request({
                url : url,
                jsonData: {
                    'id': record.get('id'),
                    'areaIds': areaTree.getCheckedIds(),
                    'deptIds': deptTree.getCheckedIds()
                },
                success : function(response, opts) {
                    Ext.ux.Toast.msg("温馨提示", "分配成功！");
                    me.close();
                },
                exception : function(response, opts) {
                    var json = Ext.decode(response.responseText);
                    Ext.ux.Toast.msg("温馨提示", json.responseStatus.message);
                }
            });
        }
    }],
    constructor : function(config) {
        var me = this,
            cfg = Ext.apply({}, config);
        me.items = [me.getForm(), me.getAreaTree(), me.getDeptTree()];
        me.callParent([cfg]);
    }
});

Ext.define('MyApp.view.commonComponent', {
    extend: 'Ext.panel.Panel',
    id: 'commonComponent',
    initComponent: function() {
        var me = this;
        me.callParent(arguments);
    }
});