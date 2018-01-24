<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>首页</title>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta http-equiv="" 
		<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
		<link rel="stylesheet" type="text/css" href="/static/css/normalize.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/index.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/footer.css" />
		<!--切换用户-->
		<link rel="stylesheet" type="text/css" href="/static/css/layer-1.1.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/menu_elastic.css" />
		<script src="/static/js/snap.svg-min.js" type="text/javascript" charset="utf-8"></script>
       
		<script src="/static/js/jquery-2.1.4.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="/static/js/layer/layer.js" type="text/javascript" charset="utf-8"></script>
		<%String remoteImage=getServletContext().getInitParameter("remoteImage");
    	request.setAttribute("remoteImage", remoteImage);%>
		<script type="text/javascript">
		
			//页面加载之前先判断是性别；加载性别样式
			$(function() {
				$('.joy').on('click', function(e) {
					e.stopPropagation();
					layer.open({
						type: 2,
						area: ['80%', '65%'],
						closeBtn: 2,
						title: false,
						shadeClose: true,
						content: '/static/tpls/dataChart.html?sex=' + sex + '&num=0'
					});

				});
				
				$('.today-step').on('click', function(e) {
					e.stopPropagation();
					layer.open({
						type: 2,
						area: ['80%', '65%'],
						closeBtn: 2,
						title: false,
						shadeClose: true,
						content: '/static/tpls/dataChart.html?sex=' + sex + '&num=1'
					});

				});
				$('.infor-left #btn').on('click', function(e) {
					e.stopPropagation();
					layer.open({
						type: 2,
						area: ['80%', '65%'],
						closeBtn: 2,
						title: false,
						shadeClose: true,
					    content: '/static/tpls/dataChart.html?sex='+sex + '&num=3'	
					});
			});
			
			
			//这里是控制首页小猫的方法
			/* tip();
			function tip(){
				var catBox = $('<div class="longTime"></div>');
				$('body').append(catBox);
				setTimeout(function(){
					$('.longTime').fadeOut(1000,function(){
						$(this).remove();
					})
				},2000)
			} */
				
			})
		</script>
		<script>
			(function(doc, win) {
				var docEl = doc.documentElement,
					resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
					recalc = function() {
						var clientWidth = docEl.clientWidth;
						if(!clientWidth) return;
						if(clientWidth >= 640) {
							docEl.style.fontSize = '100px';
						} else {
							docEl.style.fontSize = 100 * (clientWidth / 640) + 'px';
						}
					};

				if(!doc.addEventListener) return;
				win.addEventListener(resizeEvt, recalc, false);
				doc.addEventListener('DOMContentLoaded', recalc, false);
			})(document, window);
		</script>
	</head>

	<body style="overflow-y: hidden !important;" >
		<div class="menu-wrap" id="menu-wrap">
		
			<div class="openLeftBK">
				<div class="leftMember clear">
					<c:set var="rempic" value="${initParam.remoteImage}/${sessionScope.CurrentUser.avater}"></c:set>
					<div class="leftManPic"><img src="${sessionScope.CurrentUser.avater==null || sessionScope.CurrentUser.avater==''?'/static/images/man.png':rempic}" /></div>
					
					<div class="leftManMsg">
						<h2>${sessionScope.hfConcernUser.nickName==null || sessionScope.hfConcernUser.nickName==""?sessionScope.CurrentUser.realName:sessionScope.hfConcernUser.nickName}<span class="nowColor">当前显示</span></h2> 设备号：${empty sessionScope.CurrentUser.code?"/":sessionScope.CurrentUser.code}
					</div>
				</div>
				<div class="openLeftBK2">
					 
					<dl class="linkedTitle">
						<!-- <dt>已绑定设备列表</dt>  -->
						<c:import url="/index/deviceList"></c:import>
						
					    <%--遍历设备列表 --%>
						<%-- <c:forEach items="${list}" var="bD">
							<c:set var="rempic2" value="${initParam.remoteImage}/${bD.avater}"></c:set>
							<dd class="select"><img src="${bD.avater==null?'/static/images/man.jpg':rempic2}"><span>${bD.nickName}</span> 设备号：${bD.code}</dd>
					    </c:forEach> --%>
					</dl>
					<div class="margin_top1 mySubmit3" >+添加新设备</div>
					<div style="padding:10px 0">
						<div  onclick="saoyisao()" style="width:4em;float:left;text-align:center;font-size:10px;">
				 		 	<img src="/static/images/ico_addDevice.png" style="width:2em;height:2em;display:inline-block;margin:0 auto;"><br/>
				 		 	<span>(扫一扫)</span>
				  		</div>
						<div class="addLinkpp">
						<div class="addLinkpp_left">
							<div class="addLinkpp_left_sub">
								<form id="form_add" name="form_add" method="post">
									<input type="hidden" value="${sessionScope.CurrentUser.id }" name="holderId" />
									<input name="code"  id="decode" type="text" class="addInput" placeholder="输入设备编号">
								</form>
							</div>
						</div>
							<div class="addLinkpp_right" onclick="tijiaoValid(this);" style="cursor: pointer"><img src="/static/images/ico_ico7.png"></div> 
						</div>
						<div class="clear"></div>
					</div>
					<!-- <div style="width:100%;height:calc(42% - 110px);position:relative;">
					<div class="leftCC" onclick="saoyisao()"><img src="/static/images/ico_addDevice.png" width="60" height="62">扫一扫绑定设备</div>
					</div> -->
				</div>
				
			</div>
			<div class="morph-shape" id="morph-shape" data-morph-open="M-1,0h101c0,0,0-1,0,395c0,404,0,405,0,405H-1V0z">
				<svg xmlns="http://www.w3.org/2000/svg" width="100%" height="100%" viewBox="0 0 100 800" preserveAspectRatio="none">
					<path d="M-1,0C-1,0,100,0,100,0C100,0,100,-1,100,395C100,799,100,800,100,800C100,800,-1,800,-1,800C-1,800,-1,0,-1,0C-1,0,-1,0,-1,0"></path>
					<desc>Created with Snap</desc>
					<defs></defs>
				</svg>
			</div>
		</div>
		<script src="/static/js/top.js" type="text/javascript" charset="utf-8"></script>
		
		<div class="content content-wrap" style="padding-bottom:0;">
			<div class="reset" id="open-button">
				<span>切换</span>
			</div>
			<!--头像-->
			<div class="photo" >
				<c:set var="rempic" value="${initParam.remoteImage}/${sessionScope.CurrentUser.avater}"></c:set>
				<div class="avater"  onclick="temFun1(this);" data-value="1"><img src="${sessionScope.CurrentUser.avater==null || sessionScope.CurrentUser.avater== ''?'/static/images/man.png':rempic}" /></div>
				<span>${sessionScope.hfConcernUser.nickName==null || sessionScope.hfConcernUser.nickName==""?sessionScope.CurrentUser.realName:sessionScope.hfConcernUser.nickName}<i class="sex"></i></span>
			</div>
			<!--今日步数-->
			<div class="steps">
				<div class="today-step fl">
					<div class="today_step">今日步数 <span class="fr num" id="dayStep">0步</span></div>
					<div class="range">
						<span></span>
					</div>
				</div>
				<div class="fr joy">
					<span id="happy">${happy_score}</span> 快乐指数
				</div>
				<div class="clear"></div>
			</div>
			<!--信息-->
			<div class="information">
				<div class="infor-left">
					<ul>
						<li id="btn" class="single"><i class="time"></i>&nbsp;<span id="time"><%= (new java.util.Date()).toLocaleString().substring((new java.util.Date()).toLocaleString().length()-8, (new java.util.Date()).toLocaleString().length()-3) %><span class="fr" id="wear_status">无记录</span></span>
						</li>
						<!-- 用户的位置 -->
						<!-- 默认显示用户的位置 -->
						<li class="single"><i class="addr"></i> &nbsp;<span id="userAddress" onclick="window.location.href='/track/userPosition'">${track.position}</span></li>
						<input type="hidden" id="track_latitude" value="${track.latitude}">
						<input type="hidden" id="track_longitude" value="${track.longitude}">
						<input type="hidden" id="userAddress" value="${address}">
						<%-- <input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}"> --%>
						<!-- 当前天气 -->
						<li><i class="weather"></i> &nbsp;<span id="day_temp" >温度${temperature }<span id="day_weather" class="fr">${weather }</span></span>
						</li>
					</ul>
				</div>
				
				<div class="infor-right">
					<ul>
						<li>
							<a class="hert" href="javascript:;" onclick="care()">关爱一下<span>${care}</span></a>
						</li>
						<li>
							<a class="good" href="javascript:;" onclick="encourage()">鼓励一下<span>${encourage}</span>
							</a>
						</li>
					</ul>
				</div>
				<div class="clear"></div>
			</div>
		</div>

<input type="hidden" id="careNum" value="${care}">
<input type="hidden" id="encourageNum" value="${encourage}">
<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/static/js/voice/Math.uuid.js"></script>
<script type="text/javascript" src="/static/js/voice/Code.js"></script>
<script type="text/javascript" src="/static/js/voice/wechatJs.js"></script>
<script src="/static/js/slide-bar.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/jquery.hiSlider.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/classie.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/main3.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
</script>
<script type="text/javascript">
	var lng = $(".infor-left #track_longitude").val();
	var lat = $(".infor-left #track_latitude").val();
	
	//获取天气
	
		//alert("进入页面异步加载");
	$(function(){
		
		getWeather(lng,lat);
		getWearStatus();
		getDayStep();
		getVoiceNum();
		noread();
	})	
	
	//异步未读报警进来
	function noread(){
		$.getJSON("/alter/noread",{},function(data){
			if(data.status=='success'){
				if(data.ob!=null){ //有未读
					//alert("***有未读警报");
					warn();
				}else{
					//alert("***没有未读警报");
				}	
			}
		});
	}
	//弹出警告窗口
	//warn();
	function warn(){
		layer.open({
			type: 2,
			area: ['80%', '65%'],
			closeBtn: 2,
			shade: [0.7, '#EB5F5E'],
			title: false,
			shadeClose: true,
			content:'/static/tpls/warn.html'
		});
	}
	//这是弹出信息窗口
	function getVoiceNum(){
		$.getJSON("/voice/getVoiceNum",function(data){
			if(data.status == "success"){
				var voiceNum = data.ob;
				//voiceNum = 1;//前端测试
				//alert("***"+voiceNum);
				if(voiceNum != null && voiceNum > 0){
					info(voiceNum);
				}
			}else{
				//alert(data.msg);
			}
			
		});
	}
	function info(voiceNum){
		layer.open({
			type: 2,
			area: ['80%', '65%'],
			closeBtn: 2,
			title: false,
			shadeClose: true,
			content:'/static/tpls/info.html?voiceNum='+voiceNum
		});
	};
	//异步获取今日步数
	function getDayStep(){
		//alert("进入今日步数加载");
		$.ajax({
			url:"/index/dayStep",
			data:{},
			type:"post",
			async:false,
			dataType:"json",
			success:function(data){
				
				if(data.msg == "暂无记录"){

					//alert(data.msg);
					/* alert(data.msg); */
				}else{
					//alert(data.msg);
					$(".today_step #dayStep").html(data.msg+'步');
				}
			}
		})
	}
	//异步获取佩戴状态
	function getWearStatus(){
		$.ajax({
			url:"/index/indexAdorn",
			data:{},
			type:"POST",
			dataType:"json",
			success:function(data){
				if(data.msg == "暂无记录"){
					//alert(data.msg);
					/* alert(data.msg); */
				}else{
					$(".infor-left #wear_status").html(data.msg);
				}
				
			}
		
		});
	}
	//异步获取天气
	
	function getWeather(lng,lat){
		$.ajax({
			url:"/index/getWeather",
			data:{"lng":lng,"lat":lat},
			type:"POST",
			dataType:"json",
			success:function(data){
				
				if(data.status == 'success'){
					var ob = data.ob;
					var dayTemp = ob.temperature;
					var dayWeather = ob.weather;
					var weather;
					if(dayWeather != ""){
						$(".infor-left #day_temp").html('温度'+dayTemp);
						weather = $('<div class="fr">'+dayWeather+'</div>');
						$(".infor-left #day_temp").append(weather);
					}else{
						alert("天气刷新失败!");
						if($(".infor-left #day_weather").html() == ""){
							$(".infor-left #day_temp").html('温度0');
							weather = $('<div class="fr">多云转晴</div>');
							$(".infor-left #day_temp").append(weather);
						}else{
							
						}
					}
					/* if(dayTemp != null && dayTemp != ""){
						$(".infor-left #day_temp").html('温度'+dayTemp);
						if(dayWeather != null && dayWeather != ""){
							weather = $('<div class="fr">'+dayWeather+'</div>');
						}else{
							weather = $('<div class="fr">多云转晴</div>');
						}
						$(".infor-left #day_temp").append(weather);
					}else if(dayWeather != null && dayWeather != ""){
						$(".infor-left #day_weather").html(ob.weather);
					} */
				}else{
					if(data.status == 'error' && data.msg == "天气刷新失败，网络真不给力！"){
					alert(data.msg);
					}
					if(data.status == 'error' && data.msg == "参数错误"){
					alert(data.msg);
					}
					if($(".infor-left #day_weather").html() == ""){
						$(".infor-left #day_temp").html('温度0');
						weather = $('<div class="fr">多云转晴</div>');
						$(".infor-left #day_temp").append(weather);
					}
				}
			}
		
		});
	}
	
	
	//点击关爱
	var type = 0;
	var j = parseInt($('.hert').find('span').html());
	function care() {
		j = j+1;
		var colors = ['#F478A4', '#DF3D58', '#EF69CE', '#EF69CE', '#EF69CE', '#F794A6', '#F2AFC8', '#F794A6', '#FFCFD4', '#DF3D58']
		if(j > 5) {
			j=5;
			layer.msg('每日最多关爱5次哦', {
				time: 800
			});
			return false;
		};
		$('.hert').find('span').remove();
		var span = $("<span>"+j+"</span>");
		$('.hert').append(span);
		var img = $('<img src="/static/images/arrow/hertbg.png" />');
		var num = Math.floor(Math.random() * 10);
		img.css('background-color', colors[num]);
		$('.hert').append(img);
		$.each($('.hert img'), function() {
			if($(this).css('opacity') < 0.2) {
				$(this).remove();
			};
		});
		type = 2;
		saveEncourage(type);
	}
	//点击鼓励
	var i = parseInt($('.good').find('span').html());
	function encourage() {
		i = i + 1;
		var colors = ['#F478A4', '#DF3D58', '#EF69CE', '#EF69CE', '#EF69CE', '#F794A6', '#F2AFC8', '#F794A6', '#FFCFD4', '#DF3D58'];
		if(i > 5) {
			i=5;
			layer.msg('每日最多鼓励5次哦', {
				time: 800
			});
			return false;
		};
		$('.good').find('span').remove();
		var span = $("<span>"+i+"</span>");
		$('.good').append(span);

		var img = $('<img src="/static/images/arrow/goodbg.png" />');
		var num = Math.floor(Math.random() * 10);
		img.css('background-color', colors[num]);
		$('.good').append(img);
		$.each($('.good img'), function() {
			if($(this).css('opacity') < 0.2) {
				$(this).remove();
			};
		});
		type = 1;
		saveEncourage(type);
	}
	//保存鼓励
	function saveEncourage(type){
		$.ajax({
			url:"saveEncourage",
			data:{"type":type},
			type:"POST",
			dataType:"json",
			success:function(data){
				
			}
		})
	}
	//根据头像报警状态判断执行弹出层
	function temFun1(e){
		//var va = $(e).attr("data-value");
		var va = $("#current_alter").attr("class");
		if(va=="ico1"){
			/*read();*/
//			layer.open({
//				closeBtn:1,
//			    content:$("#outLayer1_al").html(),
//			},1);
			layer.open({
				type: 2,
				area: ['80%', '65%'],
				closeBtn: 2,
				title: false,
				shadeClose: true,
				content:'../tpls/warn.html' 
			});
		}else{
			layer.open({
				closeBtn:false,
				title: false,
				btn:false,
				skin:'demo-class',
				shadeClose: true,
				area:['100%','100%'],
			    content: $("#outLayer1").html(),
			}); 
			
		}
	}
</script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/static/js/Base.js" type="text/javascript" charset="utf-8"></script>
<!-- <script src="/static/js/layer.js" type="text/javascript" charset="utf-8"></script> -->
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
				
				layer.msg("请输入设备号");
				//$(e).css("background","#32CF6D");
				//$(e).attr("src","/static/images/ico_ico7.png");
				return false;
			}
			$.getJSON("/set/isbelong",$('#form_add').serialize(),function(data){
				if(data.status=='success'){
					if(data.msg=="false"){
						layer.confirm("该设备不属于当前关注人或该设备已有亲友注册关注，是否添加到被关注人列表？",{
							closeBtn:false,
							title: false,
							btn:['确认','取消'],
							yes:function(){
								tijiao(e);
							}
						});
						
					}else{
						tijiao(e);
					}
				}else{
					layer.msg(data.msg);
				}
			});
		}
		function tijiao(e){
			//$(e).css("background","#8F8F8F");
			var index = Base.open();
			var code = $("#decode").val();
			if(code == null || code==''){
				Base.close(index);
				layer.msg("请输入设备号");
				$(e).css("background","#32CF6D");
				return false;
			}
			//$(e).attr("src","/static/images/ico_ico7.png");
			$.ajax({
				url:'ajaxAddDevice',
				data:$('#form_add').serialize(),
				type:"POST",
				dataType: "json",
				success:function(data){
					//$(e).attr("src","/static/images/ico_ico7_hui.png");
					Base.close(index);
					if(data.status=='success'){
						Base.alert({
							msg:"绑定成功",//不起作用
							end:function(){
								window.location.href="/index/index";
							}
						});  
						
					}else{
						layer.msg(data.msg);
					}
					//$(e).css("background","#32CF6D");
				}
			});
		}
		
		function convert(id){
			window.location.href='/index/convertDevice?userId='+id;
		}
	</script>
	
 <c:import url="/WEB-INF/views/index/index_outLayer.jsp"></c:import>
 
	</body>
	
</html>