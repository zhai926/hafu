<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="initial-scale=1.0, user-scalable=yes"/>
<title>运动轨迹</title>

<script charset="utf-8" src="http://map.qq.com/api/js?v=2.exp&amp;key=FS7BZ-6EG3U-FCGV5-4HJYK-RJIFQ-ACBNI&amp;libraries=drawing"></script>
<script src="http://open.map.qq.com/c/=/apifiles/2/4/40/main.js,apifiles/2/4/40/mods/drawing.js" type="text/javascript"></script>
<script src="/static/js/jquery-2.1.4.min.js"></script>
<c:import url="../css.jsp"></c:import>
<script type="text/javascript" src="/static/js/My97DatePicker/WdatePicker.js"></script>
<script src="/static/js/Constans.js"></script>
<script src="/static/js/Base.js"></script>
<script type="text/javascript">
	(function (doc, win) {
		        var docEl = doc.documentElement,
		            resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
		            recalc = function () {
		                var clientWidth = docEl.clientWidth;
		                if (!clientWidth) return;
		                if(clientWidth>=640){
		                    docEl.style.fontSize = '100px';
		                }else{
		                    docEl.style.fontSize = 100 * (clientWidth / 640) + 'px';
		                }
		            };
		
		        if (!doc.addEventListener) return;
		        win.addEventListener(resizeEvt, recalc, false);
		        doc.addEventListener('DOMContentLoaded', recalc, false);
		    })(document, window);
</script>
<script src="/static/js/layer/layer.js"></script>
<link href="http://fb.hafu365.comjs/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<style type="text/css">
html,body{
	width: 100%;
	height: 100%;
	padding: 0;
	overflow: hidden;
	
}

.content{
	width: 100%;
	height: calc(100% - 50px);
	overflow-x: hidden;
	padding-bottom:0 !important;
}
.content-wrap{
	width: 100%;
	height: 100%;
	padding-bottom:0;
}
.pageMenuUL1 span.in {
	background: url(/static/images/map/ico_map1.png) no-repeat left center;
	height: 45px;
	display: inline-block;
	width: 123px;
	margin-bottom: 18px;
	padding-top:0;
	background-size:36px auto;
}
.pageMenuUL1 span.photo{
	border: 2px solid #CCCCCC;
	border-radius: 50%;
	overflow: hidden;	
	padding: 0;
	height: 45px;
	display: inline-block;
	width: 45px;
	float: left;
	margin-left: 15px;
	margin-top: 2px;
}
.pageMenuUL1 span.photo img{
	width: 100%;
	height: 100%;
}
.pageMenuUL1 span.out {
	background: url(/static/images/map/ico_map2.png) no-repeat;
	height: 45px;
	display: inline-block;
	width: 123px;
}

.pageMenuUL1 span.in b {
	color: black;
	font-weight: bold;
}

.pageMenuUL1 span.out b {
	color: red;
	font-weight: bold;
}
.pagePadding{
	height: 50px;
	padding:0;
}
.pageMenuUL1 span.date{
	background: url(/static/images/map/ico_map3.png) no-repeat center center;
    background-size: 70% 70%;
	height: 45px;
	width:45px;
	display: inline-block;
	float: right;
}
.smnoprint{
	display: none;
  }  
#container{
	height:calc(100% - 50px);
	width: 100%;
	position: relative;
	background-color: rgb(229, 227, 223);
	overflow: hidden; 
	transform: translateZ(0px);
}
</style>
<script type="text/javascript">
	var swipe=false;
	$(function() {
		//初始化地图函数  自定义函数名init
		var centerTip;
		 var anchor = new qq.maps.Point(0, 39),
        size = new qq.maps.Size(42, 68),
        origin = new qq.maps.Point(0, 0),
        icon = new qq.maps.MarkerImage(
            /* "/static/images/map/position2.png",  */
            size,
            origin,
            anchor
        );
	    //获取运动轨迹列表
		var trackData = $.parseJSON('${trackList}');
		var temp = {};
		temp.path = new Array();
		temp.pointer = new Array();
		temp.pointer=new Array();
		temp.tip=new Array();
		var infos=new Array();
		if (trackData.length > 0) {
			for ( var i = 0; i < trackData.length; i++) {
				temp.path.push( new qq.maps.LatLng(trackData[i].latitude,
						trackData[i].longitude));
				temp.pointer.push(new qq.maps.LatLng(trackData[i].latitude, trackData[i].longitude));
				var minute=trackData[i].alertDate.minutes;
				minute=minute<10?("0"+minute):minute;
				temp.tip.push(trackData[i].alertDate.hours+":"+minute);
			}
		}
		var center=null;
		if('${last.latitude}'!=''){
			var lat='${last.latitude}'; //经度
			var lng='${last.longitude}'; //维度
			var minute='${last.alertDate.minutes}'
			minute=minute<10?("0"+minute):minute;
			centerTip='${last.alertDate.hours}'+":"+minute;
			if(lat!=''&&lng!=''){
				center=new qq.maps.LatLng(parseFloat(lat),parseFloat(lng));
			}
			if (center == null) {
				center = temp.path[0];
			}
			init();
		}else{
			if (center == null) {
		     	center = temp.path[0];
	          //  center = new qq.maps.LatLng(39.96693, 116.49369)
			}
			init();
		}
		function mark(position,tip,map){
			new qq.maps.Marker({
                map: map,
                position: position
            });
          var la= new qq.maps.Label({
		        position: position,
		        map: map,
		        content: tip,
		        zIndex:100
		    });
		}
		function init() {
			//定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
			var map = new qq.maps.Map(document.getElementById("container"));
			if(center!=null){
				map.setCenter(center);
		        map.setZoom(14);
		       var currentPosition= new qq.maps.Marker({
		            map: map,
		            position: center
		        });
		       currentPosition.setIcon(icon);
		       new qq.maps.Label({
			        position: center,
			        map: map,
			        content: '<span style="color:green">'+centerTip+'</span>',
			        zIndex:150
			    });
			}
			for(var i=0;i<temp.tip.length;i++){
				if(temp.tip[i]!=null&&temp.pointer[i]!=null){
					var t=temp.pointer[i];
					var tip=temp.tip[i];
					mark(t,tip,map);
                }	
			}
		}
		$("span.date").bind("click",function(){
			$("input[name='dateStr']").trigger("click");
		});
	})
	function log(){
		var h=$("span.date").html()
		$("span.date").html("")
		$("span.date").unbind("click");
		window.location.href="/track/track?dateStr="+h;
	}
</script>
<style type="text/css">
	@media screen {
		.smnoscreen {
			display: none
		}
	}
	
	@media print {
		.smnoprint {
			display: none
		}
	}
</style>
<script type="text/javascript" src="http://open.map.qq.com/c/=/apifiles/2/4/40/mods/c3.js,apifiles/2/4/40/mods/c0.js" charset="utf-8"></script>
<script type="text/javascript" src="http://open.map.qq.com/c/=/apifiles/2/4/40/mods/c1.js" charset="utf-8"></script>
<%String remoteImage=getServletContext().getInitParameter("remoteImage");
    	request.setAttribute("remoteImage", remoteImage);%>
</head>
<body>
	<div class="content">
		<div class="content-wrap">
			<div class="pagePadding">
				<ul class="pageMenuUL1" style="text-align: center; border: none;">
					<c:set var="rempic" value="${initParam.remoteImage}/${sessionScope.CurrentUser.avater}"></c:set>
	                <span class="photo"><img src="${sessionScope.CurrentUser.avater==null || sessionScope.CurrentUser.avater== ''?'/static/images/man.png':rempic}" style="padding:1px" /></span>
					<span class="in"><b style="margin-left: 40px; font-size: 14px;color: #a9a9ab;float: left;margin-top: 13px;">${dateStr}</b></span>
					<span class="date"></span>
					<input type="text" name="dateStr" style="display: none;" onclick="WdatePicker({onpicked:log})"/>
				</ul>
			</div>
			<div id="container" style="height:calc(100% - 50px);width: 100%;"></div>
              </div>
		</div>
	</div>
<input type="hidden" value="foot_ico1" id="param_menu">
<script type="text/javascript">
	//菜单
	$(function(){
		var param_menu = $("#param_menu").val();
		if(null!=param_menu && param_menu!=''){
			if(param_menu=='foot_logo'){
				
			}else{
				$("."+param_menu).parent().addClass("over");
			}
		}else{
			$(".foot_ico3").parent().addClass("over");
		}
	});
</script>	
	<!-- /content-wrap -->
		<%---公共部分 --%>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/static/js/main3.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/voice/Math.uuid.js"></script>
<script type="text/javascript" src="/static/js/voice/Code.js"></script>
<script type="text/javascript" src="/static/js/voice/wechatJs.js"></script>	
<script src="/static/js/top.js" type="text/javascript" charset="utf-8"></script>	
<link rel="stylesheet" type="text/css" href="/static/css/footer.css" />
<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">

</body>
</html>