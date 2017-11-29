/*
 * 收货模式
 */
Ext.define('MyApp.model.zbaoBankModel', {
    extend: 'Ext.data.Model',
    idProperty: 'extId',
    idgen: 'uuid',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'name'
    },{
        name: 'bankImg'
    },{
        name: 'createTime',
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'enable',
        type: 'boolean'
    },{
        name: 'imgUrl'
    },{
        name: 'code',
        type: 'int'
    }]
});