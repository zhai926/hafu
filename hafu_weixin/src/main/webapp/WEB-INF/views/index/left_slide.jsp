<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="menu-wrap">
	<div class="openLeftBK">
		<div class="leftMember clear">
			<div class="leftManPic">
				<c:set var="rempic1" value="${initParam.remoteImage}/${sessionScope.CurrentUser.avater }"></c:set>
				<img src="${sessionScope.CurrentUser.avater==null || sessionScope.CurrentUser.avater== ''?'/static/images/man.png':rempic1 }" style="width: 63px;height: 63px;">
			</div>
			<div class="leftManMsg">
				<h2>
					${empty sessionScope.CurrentUser.nickName?sessionScope.CurrentUser.realName:sessionScope.CurrentUser.nickName }<span class="nowColor">当前显示</span>
				</h2>
				手机号：${sessionScope.CurrentUser.mobile }
			</div>
		</div>
		<c:import url="/index/deviceList"></c:import>
	</div>
	<!--button class="close-button" id="close-button">Close Menu</button-->
	<div class="morph-shape" id="morph-shape" data-morph-open="M-1,0h101c0,0,0-1,0,395c0,404,0,405,0,405H-1V0z">
		<svg xmlns="http://www.w3.org/2000/svg" width="100%" height="100%" viewBox="0 0 100 800" preserveAspectRatio="none">
			<path d="M-1,0h101c0,0-97.833,153.603-97.833,396.167C2.167,627.579,100,800,100,800H-1V0z" />
		</svg>
	</div>
	<!-- <script type="text/javascript" src="/static/js/layer/layer.js"></script> -->
	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript">
		wx.config({
		    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
		    appId: '${appId}', // 必填，公众号的唯一标识
		    timestamp:parseInt("${timestamp}",10), // 必填，生成签名的时间戳
		    nonceStr: '${nonceStr}', // 必填，生成签名的随机串
		    signature: '${signature}',// 必填，签名，见附录1
		    jsApiList: ['scanQRCode','chooseImage','uploadImage','downloadImage'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
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
			//alert("无法扫描");
		});
		//提交前看是否是该人设备
		function tijiaoValid(e){
			var code = $("#decode").val();
			if(code == null || code==''){
				Base.alert("请输入设备号");
				$(e).css("background","#32CF6D");
				return false;
			}
			$.getJSON("/set/isbelong",$('#form_add').serialize(),function(data){
				if(data.status=='success'){
					if(data.msg=="false"){
						Base.confirm({
							msg:"该设备已有亲友注册关注，是否添加到被关注人列表？",
							yes:function(){
								tijiao(e);
							}
						});
					}else{
						tijiao(e);
					}
				}else{
					Base.alert(data.msg);
				}
			});
		}
		function tijiao(e){
			$(e).css("background","#8F8F8F");
			var index = Base.open();
			var code = $("#decode").val();
			if(code == null || code==''){
				Base.close(index);
				Base.alert("请输入设备号");
				$(e).css("background","#32CF6D");
				return false;
			}
			//$(e).attr("src","/static/images/ico_ico7.png");
			$.ajax({
				url:'/index/ajaxAddDevice',
				data:$('#form_add').serialize(),
				type:"POST",
				dataType: "json",
				success:function(data){
					$(e).attr("src","/static/images/ico_ico7_hui.png");
					Base.close(index);
					if(data.status=='success'){
						Base.alert({
							msg:"绑定成功",
							end:function(){
								window.location.href="/index/index";
							}
						});
					}else{
						Base.alert(data.msg);
					}
					$(e).css("background","#32CF6D");
				}
			});
		}
		function convert(id){
			window.location.href='/index/convertDevice?userId='+id;
		}
	</script>
</div>