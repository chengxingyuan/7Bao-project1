var purchaseGameList;
var i;//做序号标示以拼接
var GameConfigList;
$(function () {
    me.init();
    $('.goods_type').css('display', 'none'); //这里把页面显示取消掉了 如果增加了商品类目选择就不要注
    $('.ad_g').unbind("click").click(function () {
        $('#poprm').css('display', 'block');
        me.initAddPanel()
        var gameNameSelected; //游戏名称
        var goodsTypeName;  //商品属性id
        var deliveryType; //收货方式id
        $('#gameType').unbind("change").change(function () {
            $("#goodsType").empty();
            $('#tradeType').empty();
            $("#shType").empty();
            $("#shType").append("<option value=''>--请选择收货方式--</option>")
            $("#goodsType").append("<option value=''>-请选择商品-</option>")
            gameNameSelected = $('#gameType option:selected').val()//获取了游戏名称
            if (null == gameNameSelected || '' == gameNameSelected) {
                alert("请选择一款游戏！")
                $('#tradeType').empty();
                $("#shType").empty();
                $("#shType").append("<option value=''>--请选择收货方式--</option>");
                return;
            }
            var remain = [];
            for (var i in GameConfigList) {
                if (gameNameSelected == GameConfigList[i].gameName) {
                    //当下拉选择游戏名称与遍历时名称一致时,设置一个数组,如果遍历时的商品类别不存在于集合中就加入下拉框
                    if (!containsOrNot(remain, GameConfigList[i].goodsTypeName)) {
                        var optionGoodsTypeName = "<option value='" + GameConfigList[i].goodsTypeId + "' selected='selected' >" + GameConfigList[i].goodsTypeName + "</option>"
                        $("#goodsType").append(optionGoodsTypeName);
                        remain.push(GameConfigList[i].goodsTypeName);
                    }
                }
            }
            //因目前业务原因后台仅配金币 而前台需要下拉框获取值 然而前段页面暂时隐去商品类目下拉框 故暂用此处代码 如后续需要开启商品类目选取将以下代码注释掉
            //开始
            $("#shType").empty();
            $("#shType").append("<option value=''>--请选择收货方式--</option>")
            $("#shType").append("<option value='2'>人工收货</option>")
            goodsTypeName = $('#goodsType option:selected').val();//获取的是id
            for (var i in GameConfigList) {
                if (gameNameSelected == GameConfigList[i].gameName && goodsTypeName == GameConfigList[i].goodsTypeId) {
                    if (GameConfigList[i].enableRobot == true) {
                        var optionGoodsTypeName = "<option value='1'>机器收货</option>"
                        $("#shType").append(optionGoodsTypeName);
                        return;
                    }
                }
            }
            //结束
        });
        //此处为选择商品类目逻辑,目前因业务原因暂时隐去，使用时将本页第6行代码注释,并将以下代码启用
        //开始
        // $('#goodsType').change(function () {
        //     $("#shType").empty();
        //     $("#shType").append("<option value=''>--请选择收货方式--</option>")
        //     $("#shType").append("<option value='2'>人工收货</option>")
        //     goodsTypeName = $('#goodsType option:selected').val();
        //     for (var i in GameConfigList) {
        //         if (gameNameSelected == GameConfigList[i].gameName && goodsTypeName == GameConfigList[i].goodsTypeId) {
        //             if (GameConfigList[i].enableRobot == true) {
        //                 var optionGoodsTypeName = "<option value='1'>机器收货</option>"
        //                 $("#shType").append(optionGoodsTypeName);
        //                 return;
        //             }
        //         }
        //     }
        // });
        //结束
        $('#shType').unbind("change").change(function () {
            var arrOfTradeTypeName;
            var arrOfTradeTypeId;
            $('#tradeType').empty();
            var checkBoxForSpell;
            deliveryType = $("#shType option:selected").val()//获取的是收货方式id 这里写定了1是机器 2 是手工
            checkBoxForSpell = '';
            // if (deliveryType=="2") {
            for (var j in GameConfigList) {
                if (gameNameSelected == GameConfigList[j].gameName && goodsTypeName == GameConfigList[j].goodsTypeId) {
                    arrOfTradeTypeName = GameConfigList[j].tradeType.split(",");
                    arrOfTradeTypeId = GameConfigList[j].tradeTypeId.split(",");
                    for (var i in arrOfTradeTypeName) {
                        checkBoxForSpell += '<span class="st_re"><input  class="Unable" value="' + arrOfTradeTypeId[i] + '" type="checkbox" name="mode"  />  ' + arrOfTradeTypeName[i] + '</span>';
                    }
                    $('#tradeType').append(checkBoxForSpell);
                }
            }
            if ("1" == deliveryType || '' == deliveryType) {
                $(".Unable").attr("disabled", true);
            }
            // }
        });
    })

    $('#poprmContClose').unbind("click").click(function () {
        $("#goodsType").empty();
        $('#tradeType').empty();
        $("#shType").empty();
        $("#gameType").empty();
        $('#poprm').css('display', 'none')
    })


    $("#shsub").unbind("click").click(function () {
        var purchaseGameRequest = {};
        purchaseGameRequest.gameName = $('#gameType option:selected').val()
        if (null == purchaseGameRequest.gameName || '' == purchaseGameRequest.gameName) {
            alert("请选择游戏");
            return;
        }
        //开始
        purchaseGameRequest.goodsTypeId = $('#goodsType option:selected').val()
        if (null == purchaseGameRequest.goodsTypeId || '' == purchaseGameRequest.goodsTypeId) {
            alert("请完善游戏配置信息");
            return;
        }
        //结束
        purchaseGameRequest.deliveryTypeId = $('#shType option:selected').val()
        if (null == purchaseGameRequest.deliveryTypeId || '' == purchaseGameRequest.deliveryTypeId) {
            alert("请选择收货方式");
            return;
        }
        //只有是人工收货的时候可以选择交易方式
        if ("2" == purchaseGameRequest.deliveryTypeId) {
            var arr = [];
            $("input:checkbox[name=mode]").each(function () {
                if ($(this).attr("checked") == true) {
                    arr.push($(this).val())
                }
            })
            purchaseGameRequest.tradeTypeId = arr.join(",");//取到多选框内的值拼成字符串
            if (null == purchaseGameRequest.tradeTypeId || '' == purchaseGameRequest.tradeTypeId) {
                alert("请选择交易方式");
                return;
            }

        }
        $.ajax({
            type: 'GET',
            url: baseServiceUrl + "services/purchaseGame/createPurchaseGame",
            contentType: "application/json; charset=UTF-8",
            dataType: 'json',
            data: purchaseGameRequest,
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    $('#poprm').css('display', 'none');
                    me.init();
                }
            }
        })
    });
})


var me = {
    //初始化的时候查询出本登录账户所有的游戏配置
    init: function () {
        $.ajax({
            type: 'GET',
            url: baseServiceUrl + "services/purchaseGame/getPurchaseGameConfig?t=" + new Date().getTime(),
            contentType: "application/json; charset=UTF-8",
            dataType: 'json',
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    purchaseGameList = resp.purchaseGameList;
                    me.initPage();
                } else {
                    if (code == "10007" || code == "10008" || code == "10010" || code == "B1022") {
                        window.location.href = "applyseller.html";
                    }
                    else if (code == "10012") {
                        window.location.href = "rechargeList.html";
                    }
                }
            }
        })
    },
    initPage: function () {
        //初始化页面,防止万一，先删除对应区块
        $("#cont").empty();
        if (null != purchaseGameList && purchaseGameList.length > 0) {
            var divStyle = '<div class="wh_dec">';
            var divEnd = '</div>';
            for (var i in purchaseGameList) {
                var purchaseGame = purchaseGameList[i];
                var userGameConfigId = purchaseGame.id;
                var deliveryTypeId = purchaseGameList[i].deliveryTypeId;
                var mode = "";
                var tradeTypeNameConfig = purchaseGame.shGameConfigs.split(",");
                var tradeTypeName = purchaseGame.tradeTypeName.split(",");
                var tradeTypeNameConfigId = purchaseGame.shGameConfigId.split(",");
                if (typeof (tradeTypeNameConfig) == "undefined" || null == tradeTypeNameConfig || '' == tradeTypeNameConfig) {
                    mode = "";
                } else {
                    for (var j in tradeTypeNameConfig) {
                        mode += '<span class="rc_yo"><input  myAtrr="' + tradeTypeNameConfigId[j] + '" id="tradeTypeName_' + tradeTypeNameConfig[j] + '_' + i + '" value="' + tradeTypeNameConfig[j] + '" type="checkbox" name="mode-01' + i + '"  />  ' + tradeTypeNameConfig[j] + '</span>';
                    }
                }
                htmlStr = "<dd class='re_neir'>";
                htmlStr += "<span class='redd_01'>" + purchaseGame.gameName + "</span>";
                htmlStr += "<span class='redd_02'>";
                htmlStr += '<div class="ar_pc">';
                htmlStr += '<div class="wh_sty"><span class="rc_op">收货模式:</span></div><div class="wh_dec">';
                htmlStr += '<span class="rc_yo"><input value="2" onclick="me.ableBtn(' + i + ')" type="radio" name="layout' + i + '" data-ad="a" class="chage_co_manual radeo_1_' + purchaseGame.id + '">  手工收货</span>';
                if(true==purchaseGame.enableMachine) {
                    htmlStr += '<span class="rc_yo"><input value="1" onclick="me.disableBtn(' + i + ')" type="radio" name="layout' + i + '" class="chage_co_machine radeo_2_' + purchaseGame.id + '" >  机器收货</span>'
                }
                htmlStr += '<span class="rc_yo"><input value="3" onclick="me.disableBtn(' + i + ')" type="radio" name="layout' + i + '" class="chage_co_pause radeo_3_' + purchaseGame.id + '" >  暂停收货</span></div>';
                htmlStr += '</div>';
                htmlStr += '<div class="ar_pc">';
                htmlStr += '<div class="wh_sty"><span class="rc_op ">交易方式:</span></div>';
                htmlStr += divStyle + mode + divEnd;
                htmlStr += '</div>';
                htmlStr += '</span>';
                htmlStr += '<span class="redd_03"><a ><span href=\"javascript:void(0)\" myAttr="' + userGameConfigId + '" onclick="me.saveChanges(this,' + i + ')" class="savede" >保存</span></a>' +
                    '<a href=\"javascript:void(0)\"><span class="xiajia" myAttr="' + userGameConfigId + '" onclick="me.deleteGameConfig(this)">删除</span></a></span></dd>'
                $("#cont").append(htmlStr);
                for (var p = 0; p < tradeTypeName.length; p++) {
                    if (containsOrNot(tradeTypeNameConfig, tradeTypeName[p])) {
                        $("#tradeTypeName_" + tradeTypeName[p] + "_" + i).attr("checked", true);
                    }
                }
                if (deliveryTypeId == "2") {//2是手工收货
                    $(".radeo_1_" + purchaseGame.id).attr("checked", true);
                    $(".radeo_2_" + purchaseGame.id).attr("checked", false);
                    $(".radeo_3_" + purchaseGame.id).attr("checked", false);
                }
                if (deliveryTypeId == "1") {//1是机器收货
                    $("input:checkbox[name='mode-01" + i + "']").each(function () {
                        $(this).attr("disabled", true);
                    })
                    $(".radeo_1_" + purchaseGame.id).attr("checked", false);
                    $(".radeo_2_" + purchaseGame.id).attr("checked", true);
                    $(".radeo_3_" + purchaseGame.id).attr("checked", false);

                }
                if (deliveryTypeId == "3") {
                    $("input:checkbox[name='mode-01" + i + "']").each(function () {
                        $(this).attr("disabled", true);
                    })
                    $(".radeo_1_" + purchaseGame.id).attr("checked", false);
                    $(".radeo_2_" + purchaseGame.id).attr("checked", false);
                    $(".radeo_3_" + purchaseGame.id).attr("checked", true);
                }
                mode = "";
            }
        }
    },
    //将当前条目的id作为参数可以直接回传,row作为当前循环次数可以作为识别参数
    saveChanges: function (btn, row) {
        var gameNameForUpdate = $(btn).parent().parent().parent().children().first().text();
        var deliveryTypeIdForUpdate = $("input:radio[name='layout" + row + "']:checked").val();
        var tradeTypeIdForUpdate;
        var userGameConfigIdForUpdate = $(btn).attr("myAttr")
        var arr = [];

        //获取复选框的值
        $("input:checkbox[name='mode-01" + row + "']").each(function () {
            if ($(this).attr("checked") == true) {
                arr.push($(this).attr("myAtrr"));
            }
        })
        tradeTypeIdForUpdate = arr.join(",")
        console.log("交易方式id" + tradeTypeIdForUpdate, "用户id" + userGameConfigIdForUpdate, "收货方式ID" + deliveryTypeIdForUpdate)
        $.ajax({
            type: 'GET',
            url: baseServiceUrl + "services/purchaseGame/updatePurchaseGame?t=" + new Date().getTime(),
            data: {
                "id": userGameConfigIdForUpdate,
                "deliveryTypeId": deliveryTypeIdForUpdate,
                "tradeTypeId": tradeTypeIdForUpdate
            },
            contentType: "application/json; charset=UTF-8",
            dataType: 'json',
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    me.init();
                    alert("保存成功!");
                }
            }
        })
    },
    disableBtn: function (row) {
        $("input:checkbox[name='mode-01" + row + "']").each(function () {
            $(this).attr("disabled", true);
        })
    },
    ableBtn: function (row) {
        $("input:checkbox[name='mode-01" + row + "']").each(function () {
            $(this).attr("disabled", false);
        })
    },
    //删除当前条目配置,并将当前条目id传入作为参数
    deleteGameConfig: function (btn) {
        var userGameConfigId = $(btn).attr("myAttr")
        var result = confirm('是否删除该项配置?');
        if (!result) {
            return;
        }
        $.ajax({
            type: 'GET',
            url: baseServiceUrl + "services/purchaseGame/deletePurchaseGame?t=" + new Date().getTime(),
            data: {"id": userGameConfigId},
            contentType: "application/json;charset=UTF-8",
            dataType: 'json',
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    me.init()
                    alert("移除收货配置成功!");
                }
            }
        })
        $(btn).parent().parent().parent().remove();
        me.init();
    },
    initAddPanel: function () {
        $.ajax({
            type: 'GET',
            url: baseServiceUrl + "services/shGameConfig/getAllConfigInfo?t=" + new Date().getTime(),
            dateType: 'json',
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    GameConfigList = resp.shGameConfigList;
                    me.addInfo();
                }
            }
        })
    },
    addInfo: function () {
        $("#gameType").empty();
        $("#goodsType").empty();
        $("#shType").empty();
        $("#tradeType").empty();
        var arr = [];
        for (var i in GameConfigList) {
            arr.push(GameConfigList[i].gameName);
        }
        //去除重复的游戏名
        var noRepeatGameName;
        noRepeatGameName = noMore(arr);
        if (null != noRepeatGameName && noRepeatGameName.length > 0) {
            $("#gameType").append("<option value='' >-请选择游戏-</option>");
            for (var i in noRepeatGameName) {
                var optionStr = "<option value='" + noRepeatGameName[i] + "'>" + noRepeatGameName[i] + "</option>>"
                $("#gameType").append(optionStr);
            }
            $("#shType").append("<option value=''>--请选择收货方式--</option>");
        } else {
            alert("当前状态不可用");
            $("#gameType").append("")
        }


    }
}
//自定义函数数组arr里是否包含某个元素val
function containsOrNot(arr, val) {
    for (var i = 0; i < arr.length; i++) {
        if (arr[i] == val)
            return true;
    }
    return false;
}
function noMore(arr) {
    var noRepeatGameName = [];
    for (var i in arr) {
        var flag = false;
        for (var j in noRepeatGameName) {
            if (arr[i] == noRepeatGameName[j]) {
                flag = true;
                break
            }
        }
        if (!flag) {
            noRepeatGameName.push(arr[i])
        }

    }
    return noRepeatGameName;
}

function sucFahuoClose() {
    $("#goodsType").empty();
    $('#tradeType').empty();
    $("#shType").empty();
    $("#gameType").empty();
    $('#poprm').css('display', 'none');
    $('#poprmContNun').val('');
}

var deliveryType = {
    "Manual": "2",
    "Machine": "1"
}