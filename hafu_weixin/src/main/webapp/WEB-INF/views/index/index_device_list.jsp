<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<meta name="format-detection" content="telphone=no" />
<div class="openLeftBK2">
	<dl class="linkedTitle">
		<dt>已关注用户列表</dt>
		<!-- <div style="overflow: scroll;height: 12em;"> -->
		<div >
		<c:forEach items="${list}" var="bd">
			<c:choose>
				<c:when test="${sessionScope.CurrentUser.id==bd.id }">
					<dd onclick="convert('${bd.id}');" style="cursor: pointer;" class="select">
						<c:set var="rempic2" value="${initParam.remoteImage}/${bd.avater }"></c:set>
						<img src="${bd.avater==null || bd.avater==''?'/static/images/man.png':rempic2 }" style="width: 30px;height: 30px;" />
						<div style="line-height:0.3rem;">
							<span>${empty bd.nickName?bd.realName:bd.nickName }</span><br/>
							<span>手机号：${bd.mobile}</span>
						</div>
					</dd>
				</c:when>
				<c:otherwise>
					<dd onclick="convert('${bd.id}');" style="cursor: pointer;">
						<c:set var="rempic2" value="${initParam.remoteImage}/${bd.avater }"></c:set>
						<img src="${bd.avater==null || bd.avater==''?'/static/images/man.png':rempic2 }" style="width: 30px;height: 30px;" />
						<div style="line-height:0.3rem;">
							<span>${empty bd.nickName?bd.realName:bd.nickName }</span><br/>
							<span>手机号：${bd.mobile}</span>
						</div>
					</dd>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		</div>
	</dl>
	<!-- <div class="addLinkpp"> -->
		<!-- <div class="addLinkpp_left">
			<div class="addLinkpp_left_sub"> -->
				<%-- <form id="form_add" name="form_add" method="post">
					<input type="hidden" value="${sessionScope.CurrentUser.id }" name="holderId" />
					<input name="code" id="decode" type="text" class="addInput" placeholder="输入设备编号">
				</form> --%>
			<!-- </div>
		</div> -->
		<!-- <div class="addLinkpp_right">
			<img src="/static/images/ico_ico7_hui.png">
		</div> -->
	<!-- </div> -->
<!-- 	<div class="margin_top1 mySubmit3" onclick="tijiaoValid(this);" style="cursor: pointer">添加新设备</div> -->

	<!-- <div class="leftCC" onclick="saoyisao();"><img src="/static/images/ico_cc.png" width="60" height="62">扫一扫绑定设备</div> -->
	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script src="/static/js/Base.js" type="text/javascript" charset="utf-8"></script>
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
		
		function convert(id){
			window.location.href='/index/convertDevice?userId='+id;
		}
	</script>
</div>

