<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
<script src="/static/js/jquery.min.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<title>扫一扫-添加设备</title>
<%-- <jsp:include page="css.jsp"></jsp:include> --%>
<script type="text/javascript">
Base.alert("您还未绑定设备，请立即绑定！");
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
	Base.alert("无法扫描");
});
function tijiao(){
	var code= $("#decode").val();
	if(code || code==''){
		Base.alert("请输入设备号或使用扫一扫");
		return false;
	}
	document.querySelector("form").submit();
}
</script>
</head>
<body >
	<%String remoteImage=getServletContext().getInitParameter("remoteImage");
      	request.setAttribute("remoteImage", remoteImage);%>
   <form action="/index/user_info" method="post">
		<div class="leftTitle" style="font-size: 4em;">+添加新设备</div>
		<div class="addLinkpp" style="font-size: 4em;">
            <div class="myLeft" style="float: left;"><input style="width: 120px;height: 20px;" value="" name="deviceCode" id="decode" type="text" class="addInput"></div>
            <div class="myRight ico" style="float: left;" onclick="tijiao();"><img src="/static/images/ico_ico7.png"></div>
        </div>
        <div class="leftCC" style="font-size: 4em;" onclick="saoyisao()" style="background: url(/static/images/ico_cc.png) no-repeat;">扫一扫绑定设备</div>
   </form>
   <span></span>
</body>
</html>