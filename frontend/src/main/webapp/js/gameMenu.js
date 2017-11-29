var zoneId="";
$(document).ready(function(){
    //选择区服时选择全部时的效果
    $("#all").click(function(){
        if($("#all").attr("flag")=="game"){
            $("#game").html("全部游戏")
            $("#region").html("全部区");
            $("#server").html("全部服");
            gameName="全部游戏"
            zoneName="全部区";
            serverName="全部服";
        }
        else if($("#all").attr("flag")=="region"){
            $("#region").html("全部区");
            $("#server").html("全部服");
            zoneName="全部区";
            serverName="全部服";
        }
        else{
            $("#server").html("全部服");
            serverName="全部服";
        }
        $("#gsBox").hide();
        $('#searchbar_arrow').hide();
    });

    $(".close_btn").click(function(){
        $("#gsBox").hide();
        $("#searchbar_arrow").hide();
    });
    getGameData();
});

//加载游戏
var gameData = new Array();

function getGame(){
    var htmlStr = "";
    for(var p in gameData){
        var pCode = gameData[p].code;
        var pValue = gameData[p].value;
        htmlStr += "<li id=\""+pCode+"\" lang=\"netgame\"><a class=\"hot\" title=\""+pValue+"\" onclick=\"GetGameZone('"+pCode+"','"+pValue+"',false)\">"+pValue+"</a></li>"
    }
    $("#gsList").html(htmlStr);

    $(".gs_name dt h1").html("请选择游戏：");

    htmlStr = "<option value='0'>请选择</option>"
    for(var p in gameData){
        var pCode = gameData[p].code;
        var pValue = gameData[p].value;
        htmlStr += "<option value='"+pCode+"'>"+pValue+"</option>"
    }
    $("#selGame").html(htmlStr);

    //新增页面中
    //游戏
    $("#selAddGame").html(htmlStr);

    //加载阵营
    $("#selAddRace").html("<option value='0'>请选择</option><option value='1'>联盟</option><option value='2'>部落</option>");

    $("#all").show();
    $("#all").attr("flag","game");
    $("#all").html("全部游戏");
}
//根据游戏的id获取大区
function GetGameZone(gameIdTemp, gameNameTemp,flagInit) {
    $("#region").html("请选择区");
    $("#server").html("请选择服");
    // $("#race").val("请选择");
    if(gameNameTemp=="魔兽世界(国服)"){
        $("#race").removeAttr("disabled");
    }
    else{
        $("#race").attr("disabled","disabled");
    }
    if(!flagInit){
        $("#gs_area").html("游戏区");
        $("#gs_server").html("游戏服");
    }
    gameId = gameIdTemp;
    gameName = gameNameTemp;
    $("#searchbar_arrow").css("left", "265px");
    $("#game").html(gameName);
    $(".gs_name dt h1").html("请选择游戏区：");
    $("#all").show();
    $("#all").attr("flag","region");
    $("#all").html("全部区");
    $.ajax({
        type: "GET",
        url: "http://fcd.5173.com/ajax.axd?methodName=gamesV31&cache=600&gameType=GameAreas&tradingType=other&id=" + gameId + "&jsoncallback=callarea",
        dataType: "jsonp",
        jsonp: "jsoncallback",
        scriptCharset: "GBK",
        jsonpCallback: "callarea",
        cache: true,
        success: function (jsonList) {
            var htmlstr = "";
            if (jsonList != null && jsonList != undefined) {
                for (var i = 0; i < jsonList.length; i++) {
                    var json = jsonList[i];
                    htmlstr += "<li id=\"" + json.id + "\" lang=\"undefined\"><a onclick=\"GetGameServer('" + json.id + "','" + json.name + "')\">" + json.name + "</a></li>";
                }
            }
            $("#gsList").html(htmlstr);
        }
    });
};

//根据选择的大区ID获取服务器
function GetGameServer(zoneIdTemp, zoneNameTemp) {
    zoneId = zoneIdTemp;
    zoneName = zoneNameTemp;
    $("#server").html("请选择");
    $("#searchbar_arrow").css("left", "373px");
    $("#region").html(zoneName);
    $(".gs_name dt h1").html("请选择游戏服：");
    $("#all").show();
    $("#all").attr("flag","server");
    $("#all").html("全部服");
    $.ajax({
        type: "GET",
        url: "http://fcd.5173.com/ajax.axd?methodName=gamesV31&cache=600&gameType=GameServers&tradingType=other&id=" + zoneId + "&jsoncallback=callserver",
        dataType: "jsonp",
        jsonp: "jsoncallback",
        scriptCharset: "GBK",
        jsonpCallback: "callserver",
        cache: true,
        success: function (jsonList) {
            var htmlstr = "";
            if (jsonList != null && jsonList != undefined) {
                for (var i = 0; i < jsonList.length; i++) {
                    var json = jsonList[i];
                    htmlstr += "<li id=\"" + json.id + "\" lang=\"undefined\"><a onclick=\"selectServer($(this))\">" + json.name + "</a></li>";
                }
            }
            $("#gsList").html(htmlstr);
        }
    });
};

//选择服务器
function selectServer(obj){
    $("#server").html(obj.html());
    $('#gsBox').hide();
    $('#searchbar_arrow').hide();
}

function select(obj){
    if(obj.attr("id")=="game"){
        //$("#all").hide();
        $('#gsBox').show();
        $('#searchbar_arrow').show();
        $("#searchbar_arrow").css("left","156px");
        getGame();
    }else if(obj.attr("id")=="region"){
        $('#gsBox').show();
        $('#searchbar_arrow').show();
        $("#searchbar_arrow").css("left","264px");
        if (gameId != null && gameId !="") {
            GetGameZone(gameId, gameName,false);
        }
    }else{
        $('#gsBox').show();
        $('#searchbar_arrow').show();
        $("#searchbar_arrow").css("left","373px");
        if($("#region").text()=="全部区"){
            $('#gsBox').hide();
            $('#searchbar_arrow').hide();
            return;
        }
        if(zoneId!=null&&zoneId!="") {
            GetGameServer(zoneId, zoneName);
        }
    }
    $("#gsBox").show();
    $("#searchbar_arrow").show();
}

//新增页面中根据游戏查找游戏大区列表
function selectGame(value){
    $("#selAddServer").html("<option value='0'>请选择</option>");
    if($("#selAddGame option:selected").text()=="魔兽世界(国服)"){
        $("#selAddRace").removeAttr("disabled");
    }
    else{
        $("#selAddRace").attr("disabled","disabled");
        $("#selAddRace").val(0);
    }

    $.ajax({
        type: "GET",
        url: "http://fcd.5173.com/ajax.axd?methodName=gamesV31&cache=600&gameType=GameAreas&tradingType=other&id=" + value + "&jsoncallback=callarea",
        dataType: "jsonp",
        jsonp: "jsoncallback",
        scriptCharset: "GBK",
        jsonpCallback: "callarea",
        cache: true,
        success: function (jsonList) {
            var htmlstr = "<option value='0'>请选择</option>";
            if (jsonList != null && jsonList != undefined) {
                for (var i = 0; i < jsonList.length; i++) {
                    var json = jsonList[i];
                    htmlstr+="<option value='"+json.id+"'>"+json.name+"</option>";
                }
            }
            $("#selAddRegion").html(htmlstr);
        }
    });
}

//新增页面中根据游戏大区查找服务器列表
function selectRegion(value){
    $.ajax({
        type: "GET",
        url: "http://fcd.5173.com/ajax.axd?methodName=gamesV31&cache=600&gameType=GameServers&tradingType=other&id=" + value + "&jsoncallback=callserver",
        dataType: "jsonp",
        jsonp: "jsoncallback",
        scriptCharset: "GBK",
        jsonpCallback: "callserver",
        cache: true,
        success: function (jsonList) {
            var htmlstr = "<option value='0'>请选择</option>";
            if (jsonList != null && jsonList != undefined) {
                for (var i = 0; i < jsonList.length; i++) {
                    var json = jsonList[i];
                    htmlstr+="<option value='"+json.id+"'>"+json.name+"</option>";
                }
            }
            $("#selAddServer").html(htmlstr);
        }
    });
}

//游戏集合
function getGameData() {
    // {code: '44343b06076d4a7a95a0ef22aac481ae',value:'地下城与勇士'}
    $.ajax({
        type: "GET",
        url: baseServiceUrl + "services/maingameconfig/getgamelist",
        contentType: "application/json; charset=UTF-8",
        dataType: "jsonp",
        success: function (resq) {
            var code = resq.responseStatus.code;
            var mainGameConfigList = resq.mainGameConfigList;
            if (code = "00") {
                if(mainGameConfigList != null && mainGameConfigList.length >0){
                    var code,value;
                    //默认设置请选择
                    $("#game").text("请选择游戏");
                    $("#region").text("请选择区");
                    $("#server").text("请选择服");
                    for(var i in mainGameConfigList){
                        code = mainGameConfigList[i].gameId;
                        value= mainGameConfigList[i].gameName;
                        gameData.push({"code":code, "value":value});
                    }
                    getGame();
                }
            }
        }
    });
}
