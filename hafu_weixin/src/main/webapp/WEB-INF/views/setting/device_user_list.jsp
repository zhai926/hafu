<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
<title>被关注人列表</title>
<jsp:include page="../css.jsp"></jsp:include>
<script src="/static/js/Base.js"></script>
</head>
<body>
<div class="container containerStyle1">
	<button class="menu-button" style="display: none;" id="open-button">打开</button>
	<!-- <div class="content-wrap"> -->
		<div class="content">
			<div class="pagePadding">
				<!--  菜单的click样式，是表示，当前点击的按钮，加上click样式 背景变蓝色，字变白色  -->
				<ul class="pageMenuUL1">
					<li class="alone w33 click" onclick="window.location.href='/index/device_user_list'" style="cursor: pointer; width:50%;">被关注人列表</li>
					<li class="alone w33" onclick="window.location.href='/index/device_list'" style="cursor: pointer; width:50%;">设备列表</li>
					<!-- <li class="alone w33" onclick="window.location.href='/set/module_setting'">首页显示</li> -->
			</ul>
				</ul>
				<c:forEach items="${list }" var="user">
					<div class="divStyle6">
						<div class="item bo3">
							<div class="div_set_left">
								<c:set var="rempic" value="${initParam.remoteImage}/${user.avater }"></c:set>
								<div class="div_set_img"><img src="${user.avater==null || user.avater== ''?'/static/images/man.png':rempic }" style="width: 65px;height: 65px;"></div>
							</div>
							<div class="div_set_left2">
								<div>
									<span class="div_set_name">${empty user.nickName?user.realName:user.nickName}</span>
									<span class="div_set_tel">${user.mobile }</span>
								</div>
								<div class="div_set_other">${user.gender } ${empty  user.age?null:user.age}岁   ${user.province} ${user.city} ${user.area }</div>
							</div>
					        <div class="div_set_right" onclick="window.location.href='/index/toUpdateHolder/${user.id}'" style="cursor: pointer;"><img src="/static/images/ico_modify.png"></div>
							<div class="clearDiv"></div>
							<!-- <div class="div_set_right" style="cursor: pointer;"><a href="toUpdateHolder"><img src="/static/images/ico_modify.png"></a></div> -->
						</div>
						<c:forEach var="de" items="${user.deviceList }">
							<div class="item bo3 clear">
								<div class="div_set_left div_set_title">设备<i>（行为记录仪）</i></div>
								<div class="div_set_left3"><div>${de.code}</div></div>
								<div class="div_set_right" onclick="deleteDevice('${de.id}',this);" style="cursor: pointer;"><img src="/static/images/ico_del.png" ></div>
							</div>
						</c:forEach>
						<div class="item clear">
							<div class="div_set_left div_set_title"  onClick="saoyisao('${user.id}');"><img src="/static/images/ico_addDevice.png"><br/><i>（扫一扫）</i></div>
							<div class="div_set_left3 div_set_add">
							<form id="form_add_${user.id }" name="form_add" method="post">
								<input type="hidden" value="${user.id}" name="holderId" />
							  	<input type="text" class="myInput" name="code" id="decode_${user.id}" placeholder="请输入设备号">
							</form>
							</div>
							<div class="div_set_right" onclick="tijiaoValid('${user.id}',this);">
							<!-- <img src="/static/images/ico_ico7_hui.png" ><div>添加</div> -->
							<button style="background-color: rgb(204, 204, 204);">添加</button>
							<!-- <button style="background-color: rgb(255, 170, 37);">添加</button> -->
							</div>
	           	        </div>
					</div>
				</c:forEach>
				<div class="mySubmit1 margin_top2">
					<a style="color: white;text-decoration: none;" href="/index/toAddUser">添加被关注人</a>
				</div>
			</div>
		<!--</div>  -->
	</div>
	<!-- /content-wrap -->
	<%---公共部分 --%>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/static/js/main3.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/voice/Math.uuid.js"></script>
<script type="text/javascript" src="/static/js/voice/Code.js"></script>
<script type="text/javascript" src="/static/js/voice/wechatJs.js"></script>		
<link rel="stylesheet" type="text/css" href="/static/css/footer.css" />
<script src="/static/js/top.js" type="text/javascript" charset="utf-8"></script>
	<!-- <script type="text/javascript" src="/static/js/layer/layer.js"></script> -->
	<!--
	var val=$('#'+id).val().length;
   		if(val==9){
   			$('#'+id).parent().nextUntil().children().css('background-color','#FFAA25')
   		}else if(val!=9){
   			$('#'+id).parent().nextUntil().children().css('background-color','#ccc')
   		} 
	 -->
	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript">
		$(function(){
			$(".div_set_add form[name='form_add'] input[name='code']").keyup(function(){
				var value=$(this).val();
				var reg=/^\d{9}$/;
				if(reg.test(value)){
					console.log(true);
					$(this).parents(".div_set_add").next().find("button").css('background-color','#FFAA25');
				}else{
					console.log(false);
					$(this).parents(".div_set_add").next().find("button").css('background-color','#ccc');
				}
			})
			
			$.getJSON("/index/jsTicket",{"url":window.location.href},function(data){
				wx.config({
				    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
				    appId: data.appId, // 必填，公众号的唯一标识
				    timestamp:parseInt(data.timestamp,10), // 必填，生成签名的时间戳
				    nonceStr: data.nonceStr, // 必填，生成签名的随机串
				    signature: data.signature,// 必填，签名，见附录1
				    jsApiList: ['scanQRCode'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
				});
				wx.error(function(res){
					//alert("无法扫描");
				});
				wx.ready(function(){
					
				});
			});
		});
		function saoyisao(id){
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
				    $("#decode_"+id).val(re);
				}
			});
		}
		//提交前看是否是该人设备
		function tijiaoValid(id,e){
			var code = $("#decode_"+id).val();
			if(code == null || code==''){
				Base.alert("请输入设备号");
				return false;
			}
			$.getJSON("/set/isbelong",$('#form_add_'+id).serialize(),function(data){
				if(data.status=='success'){
					if(data.msg=="section1_true"){
						Base.confirm({
							msg:"持有人已绑定过设备，是否立即同步？",
							yes:function(){
								$("#decode_"+id).val(data.ob.code);
								tijiao(id,e);
							}
						});
					}else if(data.msg=="section1_false"){
						Base.alert("抱歉,当前持有人已经绑定过设备！");
						return;
					}else if(data.msg=="false"){
						Base.confirm({
							msg:"该设备已有亲友注册关注，是否添加到被关注人列表？",
							yes:function(){
								tijiao(id,e);
							}
						});
					}else{
						tijiao(id,e);
					}
				}else{
					Base.alert(data.msg);
				}
			});
		}
		function tijiao(id,e){
			var index = Base.open();
			var code = $("#decode_"+id).val();
			if(code == null || code==''){
				Base.close(index);
				Base.alert("请输入设备号");
				return false;
			}
			$(e).children("img").attr("src","/static/images/ico_ico7.png");
			$.ajax({
				url:'/index/ajaxAddDevice',
				data:$('#form_add_'+id).serialize(),
				type:"POST",
				dataType: "json",
				success:function(data){
					Base.close(index);
					$(e).children("img").attr("src","/static/images/ico_ico7_hui.png");
					if(data.status=='success'){
						Base.alert({
							msg:"绑定成功",
							end:function(){
								window.location.reload();
							}
						});
						/* var html = '<div class="item bo3 clear">'
							+'<div class="div_set_left div_set_title">设备<i>（行为记录仪）</i></div>'
							+'<div class="div_set_left3"><div>#CODE</div></div>'
							+'<div class="div_set_right" onclick="deleteDevice(#ID,this);" style="cursor: pointer;"><img src="/static/images/ico_del.png" ></div>'
							+'</div>';
						html = html.replace("#ID",data.ob.id).replace("#CODE",data.ob.code);
						$(e).parent().before(html);
						$("#decode_"+id).val(""); */
					}else{
						Base.alert(data.msg);
					}
				},error:function(){Base.close(index);}
			});
		}
		//删除设备
		function deleteDevice(id,e){
			var fl = Base.confirm({
				msg:"确认删除此设备？",
				yes:function(){
					$.ajax({
						url:'/index/deleteDevice',
						data:{"deviceId":id},
						type:"POST",
						dataType: "text",
						success:function(data){
							if(data=='success'){
								$(e).parent(".bo3").remove();
							}else{
								Base.alert("删除失败");
							}
						}
					});
				}
			});
		}
	</script>
</div>
<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
</body>
</html>