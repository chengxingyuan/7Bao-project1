// //游戏币访问服务Url
// var baseServiceUrl = "http://yxbmall.5173.com/gamegold-facade-frontend/";
// //游戏币访问html
// var baseHtmlUrl = "http://yxbmall.5173.com/";
// //文件图片等访问Url
// var imageServiceUrl = "http://yxbmall.5173.com";
// //用户登陆url(returnUrl:登录成功回跳地址),需二次encode对应passport会解码二次的问题
// var loginUrl = "https://passport.5173.com/?undecode=1&returnUrl="+escape(window.location.href);
// //线上环信appKey
// var hxAppKey = "5173#5173jbsc";
// // //线下环信appKey
// // var hxAppKey = "5173#mobile5173cs";
// //游戏币访问服务Url
// var baseServiceUrl = "http://m01.7bao.com/api/7Bao-frontend/";
// //游戏币访问html
// var baseHtmlUrl = "http://m01.7bao.com/api/7Bao-frontend/";
// //游戏币访问服务Url
// var baseServiceUrl = "http://localhost:8080/7Bao-frontend/";
//游戏币访问服务Url
var baseServiceUrl = "https://shouhuo.7bao.com/api/7Bao-frontend/";
//游戏币访问html
var baseHtmlUrl = "https://shouhuo.7bao.com/api/7Bao-frontend/";
//文件图片等访问Url
var imageServiceUrl = "http://yxbmall.5173.com";
//用户登陆url(returnUrl:登录成功回跳地址)
var loginUrl = "https://passport.5173.com/?undecode=1&returnUrl=" + escape(window.location.href);

// //游戏币访问服务Url
// var baseServiceUrl = "http://yxbmall.5173.com:8081/gamegold-facade-frontend/";
// //游戏币访问html
// var baseHtmlUrl = "http://yxbmall.5173.com:8081/gamegold-facade-frontend/";
// //文件图片等访问Url
// var imageServiceUrl = "http://yxbmall.5173.com:8081";
// //用户登陆url(returnUrl:登录成功回跳地址)
// var loginUrl = "https://passport.5173.com/?undecode=1&returnUrl="+escape(window.location.href);


// 用户类型
var UserType = {
    "CustomerService": 1, // 接单客服
    "NomalManager": 2,    // 管理员
    "SystemManager": 3,   // 系统管理员
    "EnterService": 7,    // 入驻客服

    1: "接单客服",
    2: "管理员",
    3: "系统管理员",
    7: "入驻客服",

    getText: function (value) {
        for (var p in ComplaintState) {
            if (value == p) {
                return ComplaintState[p];
            }
        }
        return "";
    }
}

// 卖家审核状态
var CheckState = {
    "UnAudited": 0,    // 未审核
    "PassAudited": 1,  // 审核通过
    "UnPassAudited": 2 // 审核不通过
}

//交易类型
var TradeType = {
    "NoDivid": 1, // 当面交易
    "Divided": 2 // 游戏内邮寄
}



//var ChPayStatus=[
//	{code:0,value:'待支付'},
//	{code:1,value:'已支付'},
//	{code:2,value:'已退款'}
//]
var ChPayStatus = {
    "WaitPay": 0,
    "Paid": 1,
    "ApplyRefund": 2,
    "Refund": 3,

    0: "待支付",
    1: "已支付",
    2: "申请退款中",
    3: "已退款",

    getText: function (value) {
        for (var p in ChPayStatus) {
            if (value == p) {
                return ChPayStatus[p];
            }
        }
        return "";
    }
}
// 订单状态
var OrderState = {
    "WaitPayment": 1, // 待付款
    "Paid": 2,        // 已付款
    "WaitDelivery": 3,// 待发货
    "Delivery": 4,    // 已发货
    "Statement": 5,   // 结单
    "Refund": 6,      // 已退款
    "Cancelled": 7,   // 已取消
    "Receive": 8,

    1: "待付款",
    2: "已付款",
    3: "待发货",
    4: "已发货",
    5: "结单",
    6: "已退款",
    7: "已取消",
    8: "已收货",

    getText: function (value) {
        for (var p in OrderState) {
            if (value == p) {
                return OrderState[p];
            }
        }
        return "";
    }
}
// 角色状态
var RoleState = {

    1: "空闲",
    2: "收货中",
    3: "分仓中",

    getText: function (value) {
        for (var p in RoleState) {
            if (value == p) {
                return RoleState[p];
            }
        }
        return "";
    }
}
/**
 * 倒计时显示
 * @param payTime 支付时间(已被序列化)
 * @param delayTime (多少时间内可以到货，单位分钟)
 */
function countdownTime(payTime, delayTime) {
    if (orderInfo_orderState.orderState == OrderState.Paid ||
        orderInfo_orderState.orderState == OrderState.WaitDelivery) {
        // 当前时间
        if (!window.nowTime) {
            window.nowTime = new Date().getTime();
        }

        window.nowTime = window.nowTime + 1 * 1000;

        // 到期时间
        var deadTime = payTime + delayTime * 60 * 1000;

        if (deadTime >= window.nowTime) {
            // 显示倒计时
            var diffTime = deadTime - window.nowTime;
            var minutes = parseInt(diffTime / 60 / 1000);
            var seconds = parseInt((diffTime - minutes * 60 * 1000) / 1000);

            // 动态显示
            $("#countdownTime").html(minutes + "分钟" + seconds + "秒");
        } else {
            if (orderInfo_orderState.isDelay != true) {
                if (window.currentcountdown) {
                    window.clearInterval(parseInt(window.currentcountdown));
                }

                delayOrder(orderInfo_orderState.orderId);
            }
        }
    }
}

/**
 * 已发货，邮寄方式时，显示什么时间取货
 * 当前时间+邮寄时间
 * @param mailTime
 */
function getMailTimeOnDelivery(sendTime, mailTime) {
    if (isNull(sendTime) || isNull(mailTime)) {
        return "0时0分 ";
    }
    var laterTime = sendTime + mailTime * 60 * 1000;

    var date = new Date(laterTime);

    return " " + date.getHours() + "点" + date.getMinutes() + "分 ";
}

// 获取URL对应参数值
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}

// 获取cookie
function getAuthkey() {
//	return "5173auth";
    return $.cookie(".5173auth");
}

// ajax调用结束
$(document).ajaxComplete(function (event, xhr, settings) {
    if (xhr.responseText.indexOf('{') === 0) {
        var response = $.evalJSON(xhr.responseText);
        if (isNull(response.responseStatus.code)) {
            return;
        }
        // 非法的用户auth
        if (response.responseStatus.code == "B1003") {
            // 引导用户登陆
            window.location.href = loginUrl;
        } else {
            if (response.responseStatus.code != "00" && response.responseStatus.code != "11") {
                alert(response.responseStatus.message);
            }
        }

    }
});

var DEBUG = false;
if (typeof window.console === "undefined" || typeof  window.console.log === "undefined") {
    window.console = {};
    if (DEBUG) {
        console.log = function (msg) {
            alert(msg);
        };
    } else {
        console.log = function () {
        };
    }
}

/**
 * 生成图片url
 * @param oriUrl 图片原始路径
 * @param size  生成的图片大小
 */
function buildImageUrl(oriUrl, size) {
    var geneUrl = "";
    if (isNull(oriUrl)) {
        return geneUrl;
    }
    var rootUrl = oriUrl.replace(".jpg", "");
    geneUrl = imageServiceUrl + rootUrl + "_" + size + ".jpg?t=" + (new Date()).getTime();
    return geneUrl;
}

// 判断是否为空
function isNull(value) {
    if (jQuery.type(value) === "undefined" || jQuery.type(value) === ""
        || jQuery.type(value) === "null" || value == "null" || value == null
        || value == "" || value == "undefined") {
        return true;
    } else {
        return false;
    }
}

function isNumber(value) {
    var reg = new RegExp("^[0-9]*$");
    if (!reg.test(value)) {
        return false;
    }
    return true;
}

// 保留两位小数   
// 功能：将浮点数四舍五入，取小数点后2位 
function toDecimal(x) {
    var f = parseFloat(x);
    if (isNaN(f)) {
        return;
    }
    f = Math.round(x * 100) / 100;
    return f;
}

// 强制保留2位小数，如：2，会在2后面补上00.即2.00  
function toDecimal2(x) {
    var f = parseFloat(x);
    if (isNaN(f)) {
        return false;
    }
    var f = Math.round(x * 100) / 100;
    var s = f.toString();
    var rs = s.indexOf('.');
    if (rs < 0) {
        rs = s.length;
        s += '.';
    }
    while (s.length <= rs + 2) {
        s += '0';
    }
    return s;
}
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
function getLoginAccount() {
    $.ajax({
        type: "GET",
        url: baseServiceUrl + "services/loginFor5173/getLoginUser?t=" + new Date().getTime(),
        contentType: "application/json; charset=UTF-8",
        dataType: "json",
        success: function (data, textStatus, resObj) {
            var code = data.responseStatus.code;
            if (code == 'B1003') {
                window.location.href = baseServiceUrl + "services/loginFor5173/login";
            }
            if (resObj.getResponseHeader("loginUser") != null) {
               var account = resObj.getResponseHeader("loginAccount");
                $("#loginAccount").html(account);
            }
        }
    })
}