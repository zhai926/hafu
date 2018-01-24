<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>首页33</title>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
		<link rel="stylesheet" type="text/css" href="/static/css/normalize.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/index.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/jquery.hiSlider.min.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/footer.css" />
		<!--切换用户-->
		<link rel="stylesheet" type="text/css" href="/static/css/layer-1.1.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/menu_elastic.css" />
		<script src="/static/js/snap.svg-min.js" type="text/javascript" charset="utf-8"></script>
        <link rel="stylesheet" id="sexcss" sex='woman' type="text/css" href="/static/css/com-woman.css" />
		<script src="/static/js/jquery-2.1.4.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="/static/js/layer/layer.js" type="text/javascript" charset="utf-8"></script>
		 <!-- <script type="text/javascript" src="/static/js/index/index.js"></script> --> 
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
					    content: '/static/tpls/dataChart.html?sex=' + '${sex}' + '&num=1'
						/* content: '../dataChart.jsp?sex=${sex}' + '&num=1'  */
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
					    content: '/static/tpls/dataChart.html?sex=' + '${sex}' + '&num=2'
						 /* content: '../dataChart.jsp?sex='+sex + '&num=2' */
					});

				});
				//这是弹出信息窗口
			function info(){
				layer.open({
					type: 2,
					area: ['80%', '65%'],
					closeBtn: 2,
					title: false,
					shadeClose: true,
					content:'/static/tpls/info.html?sex='+sex 
				});
			};
			//弹出警告窗口
			function warn(){
				layer.open({
					type: 2,
					area: ['80%', '65%'],
					closeBtn: 2,
					title: false,
					shadeClose: true,
					content:'/static/tpls/warn.html' 
				});
			}
			tip();
			function tip(){
				var catBox = $('<div class="longTime"></div>');
				$('body').append(catBox);
				setTimeout(function(){
					$('.longTime').fadeOut(1000,function(){
						$(this).remove();
					})
				},2000)
			}
				
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

	<body style="overflow-y: hidden !important;">
		<div class="menu-wrap" id="menu-wrap">
		
			<div class="openLeftBK">
				<div class="leftMember clear">
				
					<c:set var="rempic" value="${initParam.remoteImage}/${sessionScope.CurrentUser.avater}"></c:set>
					<div class="leftManPic"><img src="${sessionScope.CurrentUser.avater==null?'/static/images/man.png':rempic}"></div>
					
					<div class="leftManMsg">
						<h2>${empty sessionScope.CurrentUser.nickName?sessionScope.CurrentUser.realName:sessionScope.CurrentUser.nickName}<span class="nowColor">当前显示</span></h2> 设备号：${empty sessionScope.CurrentUser.code?"/":sessionScope.CurrentUser.code}
					</div>
				</div>
				<div class="openLeftBK2">
					 
					<dl class="linkedTitle">
						<dt>已绑定设备列表</dt> 
						<%-- <c:import url="/index/deviceList"></c:import>  --%>
						
					    <%--遍历设备列表 --%>
						<c:forEach items="${list}" var="bD">
							<c:set var="rempic2" value="${initParam.remoteImage}/${bD.avater}"></c:set>
							<dd class="select"><img src="${bD.avater==null?'/static/images/man.png':rempic2}"><span>${bD.nickName}</span> 设备号：${bD.code}</dd>
					    </c:forEach>
					</dl>
					<div class="margin_top1 mySubmit3">+添加新设备</div>

					<div class="addLinkpp">
						<div class="addLinkpp_left">
							<div class="addLinkpp_left_sub">
								<form><input name="" type="text" class="addInput" placeholder="输入设备编号"></form>
							</div>
						</div>
						<div class="addLinkpp_right"><img src="/static/images/ico_ico7_hui.png"></div>
					</div>

					<div class="leftCC"><img src="/static/images/ico_cc.png" width="60" height="62">扫一扫绑定设备</div>
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
				<div class="avater"  onclick="temFun1(this);" data-value="1"><img src="${sessionScope.CurrentUser.avater==null?'/static/images/man.jpg':rempic}" /></div>
				<span>${hfConcernUser.relations==null?"亲人":hfConcernUser.relations}<i class="sex"></i></span>
			</div>
			<!--今日步数-->
			<div class="steps">
				<div class="today-step fl">
					<div class="today_step">今日步数 <span class="fr num" id="dayStep">0步</span></div>
					<div class="range">
						<span></span>
					</div>
				</div>
				<div class="fl joy">
					<span id="happy"></span> 快乐指数
				</div>
				<div class="clear"></div>
			</div>
			<!--信息-->
			<div class="information">
				<div class="infor-left">
					<ul>
						<li id="btn" onclick="test()"><i class="time"></i>&nbsp;<span id="time"><%= (new java.util.Date()).toLocaleString().substring((new java.util.Date()).toLocaleString().length()-8, (new java.util.Date()).toLocaleString().length()-3) %><span class="fr" id="wear_status">出门</span></span>
						</li>
						<!-- 用户的位置 -->
						<!-- 默认显示用户的位置 -->
						<li><i class="addr"></i> &nbsp;<span id="userAddress">${track.position==null?"默认地址":track.position}</span></li>
						<input type="hidden" id="track_latitude" value="${track==null?null:track.latitude}">
						<input type="hidden" id="track_longitude" value="${track==null?null:track.longitude}">
						<input type="hidden" id="userAddress" value="${address}">
						<!-- 当前天气 -->
						<li><i class="weather"></i> &nbsp;<span id="day_temp" >温度23<span id="day_weather" class="fr">多云转晴</span></span>
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
			</div>
		</div>


<script src="/static/js/slide-bar.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/jquery.hiSlider.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/classie.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/main3.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
	var lng = $(".infor-left #track_longitude").val();
	var lat = $(".infor-left #track_latitude").val();
	var data_str;
	//获取天气
	$(function(){
		getWeather(lng,lat);
		getWearStatus();
		getDayStep();
	})
	//异步获取今日步数
	function getDayStep(){
		$.ajax({
			url:"index/dayStep",
			data:{},
			type:"POST",
			dataType:"json",
			success:function(data){
				if(data.msg == "暂无记录"){
				/* 	alert(data.msg); */
				}else{
					$(".today_step #dayStep").html(data.msg+'步');
				}
			}
		})
	}
	//异步获取佩戴状态
	function getWearStatus(){
		$.ajax({
			url:"index/indexAdorn",
			data:{},
			type:"POST",
			dataType:"json",
			success:function(data){
				if(data.msg == "未佩戴" ||data.msg == "暂无记录"){
					alert(data.msg);
				}
				$(".infor-left #wear_status").html(data.msg);
			}
		
		});
	}
	//异步获取天气
	
	function getWeather(lng,lat){
		$.ajax({
			url:"index/getWeather",
			data:{"lng":lng,"lat":lat},
			type:"POST",
			dataType:"json",
			success:function(data){
				if(data != null || data != ""){
					var ob = data.ob;
					var dayTemp = ob.temperature;
					var dayWeather = ob.weather;
					var weather;
					
					if(dayTemp != null && dayTemp != ""){
						$(".infor-left #day_temp").html('温度'+dayTemp);
						if(dayWeather != null && dayWeather != ""){
							weather = $('<div class="fr">'+dayWeather+'</div>');
						}else{
							weather = $('<div class="fr">多云转晴</div>');
						}
						$(".infor-left #day_temp").append(weather);
					}else if(dayWeather != null && dayWeather != ""){
						$(".infor-left #day_weather").html(ob.weather);
					}
				}else{
					/* alert(data.status); */
				}
			}
		
		});
	}
	function test() {
		layer.open({
			type: 2,
			area: ['80%', '65%'],
			closeBtn: 2,
			title: false,
			shadeClose: true,
		    content: '/static/tpls/dataChart.html?sex=' + sex + '&num=3'  
		    /* content: '../dataChart.jsp?sex=${sex}' + '&num=3'  */
		});
	}
	
	//点击关爱
	var type = 0;
	//var j = ${care};
	var j = parseInt($('.hert').find('span').html());
	function care() {
		//var j = $('.hert').find('span').html();
		//var j = ${care};
		j = j+1;
		var colors = ['#F478A4', '#DF3D58', '#EF69CE', '#EF69CE', '#EF69CE', '#F794A6', '#F2AFC8', '#F794A6', '#FFCFD4', '#DF3D58']
		if(j > 5) {
			j=5;
			layer.msg('每日最多关爱5次哦', {
				time: 800
			});
			return false;
		};
		$('.hert').find('span').html('+' + (j));
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
	//var i = ${encourage};
	var i = parseInt($('.good').find('span').html());
	function encourage() {
		//var i = $('.good').find('span').html();
		//var i = ${encourage};
		i = i + 1;
		var colors = ['#F478A4', '#DF3D58', '#EF69CE', '#EF69CE', '#EF69CE', '#F794A6', '#F2AFC8', '#F794A6', '#FFCFD4', '#DF3D58'];
		if(i > 5) {
			i=5;
			layer.msg('每日最多鼓励5次哦', {
				time: 800
			});
			
		};
		$('.good').find('span').html('+' + (i));

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
			url:"index/saveEncourage",
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
				shadeClose: true,
				area:['100%','100%'],
			    content: $("#outLayer1").html(),
			}); 
			
		}
	}
</script>
 <c:import url="/WEB-INF/views/index/index_outLayer.jsp"></c:import> 
 <input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
	</body>
	
</html>