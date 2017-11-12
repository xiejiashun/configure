var TT = TAOTAO = {
	checkLogin : function(){
		//通过jquery获取cookie中的ｔｏｋｅｎ
		var _ticket = $.cookie("TT_TOKEN");
		if(!_ticket){
			alert(1+"cookiemeizhi");
			return ;
		}
		$.ajax({
			url : "http://localhost:8088/user/token/" + _ticket,
			//实际上使用jsnp query自动拼接成：http://localhost:8088/user/token/12939179179?callback=random
			dataType : "jsonp",
			type : "GET",
			success : function(data){//详单于是random
				if(data.status == 200){
					var username = data.data.username;
					var html = username + "，欢迎来到淘淘！<a href=\"http://www.taotao.com/user/logout.html\" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
});