<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
<title>绑定用户-设备</title>
<jsp:include page="css.jsp"></jsp:include>
<!--弹出层效果代码-->
<script src="/static/js/jquery.min.js"></script>
<!-- <script type="text/javascript" src="/static/js/layer/layer.js"></script> -->
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/static/js/Base.js"></script>
<script type="text/javascript" charset="UTF-8">
wx.config({
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: '${appId}', // 必填，公众号的唯一标识
    timestamp:parseInt("${timestamp}",10), // 必填，生成签名的时间戳
    nonceStr: '${nonceStr}', // 必填，生成签名的随机串
    signature: '${signature}',// 必填，签名，见附录1
    jsApiList: ['scanQRCode'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});
function saoyisao(){
	wx.scanQRCode({
	    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
	    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
	    success: function (res) {
		    var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
		   	if(result==null || result==''){
		   		Base.alert("无法获取扫描结果");
		   		return;
		   	}
		    var re = "";
		    if(result.indexOf(",")!=-1){
		    	re = result.split(",")[1];
		    }else{
		    	re = result;
		    }
		    $("#decode").val(re);
		}
	});
}
wx.ready(function(){
	
});
wx.error(function(res){
	//Base.alert("无法扫描");
});
//提交前看是否是该人设备
function tijiaoValid(){
	var code = $("#decode").val();
	if(code == null || code==''){
		Base.alert("请输入设备号");
		return false;
	}
	$.getJSON("/set/isbelong",$('#form').serialize(),function(data){
		if(data.status=='success'){
			if(data.msg=="false"){
				Base.confirm({
					msg:"设备持有人和关注人身份不一致，是否绑定？",
					yes:function(){
						tijiao();
					}
				});
			}else{
				tijiao();
			}
		}else{
			Base.alert(data.msg);
		}
	});
}
function tijiao(){
	var index = Base.open();
	var code = $("#decode").val();
	if(code == null || code==''){
		Base.close(index);
		Base.alert("请输入设备号");
		return false;
	}
	$.ajax({
		url:'/index/addDevice',
		data:$('#form').serialize(),
		type:"POST",
		dataType: "json",
		success:function(data){
			Base.close();
			if(data.status=='success'){
				Base.alert("绑定成功");
				window.location.href="/index/index";
			}else{
				Base.alert(data.msg);
			}
		}
	});
}
</script>
</head>

<body>
	<div class="container containerStyle1">
		<!--button class="menu-button" id="open-button">打开</button-->
		<div class="content-wrap">
			<div class="content">
				<div class="pagePadding">
					<!--  菜单的click样式，是表示，当前点击的按钮，加上click样式 背景变蓝色，字变白色  -->
					<div class="divStyle6">
						<div class="item">
							<div class="div_set_left">
								<div class="div_set_img"><img src="/static/images/man.jpg"></div>
							</div>
							<div class="div_set_left2">
								<div>
									<span class="div_set_name">${ob.nickName==null?ob.realName:ob.nickName }</span>
									<span class="div_set_tel">${ob.mobile }</span>
								</div>
								<div class="div_set_other">${ob.gender } ${ob.age}岁   ${ob.city} ${ob.area }</div>
							</div>
							<div class="div_set_right" onclick="window.location.href='/index/registerUpdate'"><img src="/static/images/ico_modify.png"></div>
							<div class="clearDiv"></div>
						</div>
					</div>
					<form action="/index/addDevice" method="post" name="f" id="form">
						<input type="hidden" value="${ob.id }" name="holderId"/>
						<div class="divStyle6_1">
							<div class="item clear">
								<div class="div_set_left div_set_title" onClick="saoyisao()"><img src="/static/images/ico_addDevice.png"></div>
								<div class="div_set_left3 div_set_add">
									<input type="text" value="${code }" id="decode" name="code" class="myInput" placeholder="请这里输入您的设备号">
								</div>
								<!-- <div class="div_set_right"><img src="/static/images/ico_ico7_hui.png"></div> -->
							</div>
						</div>
					</form>
					<div class="mySubmit1 margin_top2" onclick="tijiaoValid();" style="cursor: pointer;">添加设备</div>
				</div>

			</div>
		</div>
		<!-- /content-wrap -->
	</div>
	<script src="/static/js/classie.js"></script>
	<script src="/static/js/bootstrap.min.js"></script>
</body>
</html>