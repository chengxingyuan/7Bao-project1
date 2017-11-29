//7Bao访问服务Url
var baseServiceUrl = "http://yxbmall.5173.com/gamegold-facade-frontend/";
//用户登陆url(returnUrl:登录成功回跳地址),需二次encode对应passport会解码二次的问题
var loginUrl = "https://passport.5173.com/?undecode=1&returnUrl="+escape(window.location.href);

// //7Bao访问服务Url
// var baseServiceUrl = "http://localhost:8081/gamegold-facade-frontend/";
// //用户登陆url(returnUrl:登录成功回跳地址)
// var loginUrl = "https://passport.5173.com/?undecode=1&returnUrl="+escape(window.location.href);

// //游戏币访问服务Url
// var baseServiceUrl = "http://yxbmall.5173.com:8081/gamegold-facade-frontend/";
// //用户登陆url(returnUrl:登录成功回跳地址)
// var loginUrl = "https://passport.5173.com/?undecode=1&returnUrl="+escape(window.location.href);

// 获取cookie
function getAuthkey(){
	return $.cookie(".7BaoAuth");
}

// ajax调用结束
$(document).ajaxComplete(function( event, xhr, settings ) {
	if (xhr.responseText.indexOf('{') === 0) {
		var response = $.evalJSON(xhr.responseText);
		if(isNull(response.responseStatus.code)){
			return;
		}
		// 非法的用户auth
		if(response.responseStatus.code == "B1003"){
			// 引导用户登陆
			window.location.href = loginUrl;
		}else {
			if(response.responseStatus.code != "00" && response.responseStatus.code != "11"){
				alert(response.responseStatus.message);
			}
		}
	}
});

var DEBUG = false;
if (typeof window.console === "undefined" || typeof  window.console.log === "undefined") {
    window.console = {};
    if (DEBUG) {
        console.log = function(msg) {
             alert(msg);
        };
    } else {
        console.log = function() {};
    }
}

// 判断是否为空
function isNull(value){
	if(jQuery.type(value) === "undefined" || jQuery.type(value) === ""
		|| jQuery.type(value) === "null" || value == "null" || value == null
		|| value == "" || value == "undefined"){
		return true;
	}else {
		return false;
	}
}