<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运动轨迹</title>
<script charset="utf-8"
	src="http://map.qq.com/api/js?v=2.exp&key=FS7BZ-6EG3U-FCGV5-4HJYK-RJIFQ-ACBNI&libraries=drawing,geometry,convertor"></script>
	<script src="http://open.map.qq.com/c/=/apifiles/2/4/40/main.js,apifiles/2/4/40/mods/drawing.js" type="text/javascript"></script>
	<script src="http://open.map.qq.com/c/=/apifiles/2/4/40/main.js,apifiles/2/4/40/mods/drawing.js" type="text/javascript"></script> 
<script type="text/javascript" src="/static/js/jquery.min.js"></script>
<meta name="viewport" content="initial-scale=1.0, user-scalable=yes"/>
<c:import url="../css.jsp"></c:import>
<style type="text/css">

span.in {
	background: url(/static/images/map/ico_map1.png) no-repeat;
	height: 45px;
	display: inline-block;
	width: 50px;
}
span.out {
	background: url(/static/images/map/ico_map2.png) no-repeat;
	height: 45px;
	display: inline-block;
	width: 50px;
}
span.in b{
	color: black;
	font-weight: bold;
}
span.out b{
	color: red;
	font-weight: bold;

}
.positionTip{
 	position:fixed;
 	bottom:50px;
 	width:160px;
 	border:1px solid red;
 	padding:8px;
	left:50%;
	margin-left:-75px;
	background-color:white;
	color:#888;
	border-radius:6px;
}
.positionTip h1{
	font-size:24px;
	text-align:center;
	color:red;
	padding-bottom:8px;
}
.positionTip p{
	font-size:14px;
}
</style>

<script type="text/javascript">
	function getPosition(){
		var r=  window.location.search;
		var data = r.split('?');
		var id = data[1].split("=")[1];
		//alert("解析到id"+id);
		$.getJSON("/alter/getAlter",{"id":id},function(data){
			if(data.status == "success"){
				var alter = data.ob;
				position_lng = alter.longitude;
				position_lat = alter.latitude;
				if(position_lng == null || position_lng == '' || position_lat == null || position_lat == ''){
					position_lng = "${smsconfig.defaultLng}";
					position_lat = "${smsconfig.defaultLat}";
				}
				//alert(alter.position+"：报警id");
				$('.positionTip #detailPosition').text(alter.position);
				//$('.positionTip #detailPosition').text(alter.alertDate);//显示报警时间
				$('.pagePadding #time').text(alter.createDate.substring(0,alter.createDate.length-8));
				
				var temp = {};
				temp.path = new qq.maps.LatLng(position_lat,
						position_lng);
				temp.pointer = new qq.maps.LatLng(position_lat,
						position_lng);
				temp.tip = new qq.maps.LatLng(position_lat,
						position_lng);
				//需要获取address
				var address = "${address}";
				var geocoder = new qq.maps.Geocoder();
				geocoder.getLocation(address);
				var center = {};
				var map = new qq.maps.Map(document.getElementById("container"));
				geocoder.setComplete(function(result) {
					map.setCenter(result.detail.location);
					var marker = new qq.maps.Marker({
						map : map,
						position : result.detail.location
					});
					center = result.detail.location;
					console.log("center:"+center);
					init();
				});
				geocoder.setError(function() {
					console.log("error");
					if (temp.path != null) {
						center = temp.path;
						console.log("error");
						init();
					}
				});
				console.log("center:" + center);
				if (center == null) {
					center = temp.path;
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
					map = new qq.maps.Map(document.getElementById("container"), {
						center : new qq.maps.LatLng(position_lat, position_lng), // 地图的中心地理坐标。
						zoom : 12,
						zoomControlOptions: {
						position: qq.maps.ControlPosition.RIGHT_TOP
						},
						 //设置控件的地图类型和位置
		                mapTypeControlOptions: {
		                    //设置控件的地图类型ID，ROADMAP显示普通街道地图，SATELLITE显示卫星图像，HYBRID显示卫星图像上的主要街道透明层
		                    mapTypeIds: [
		                        //qq.maps.MapTypeId.ROADMAP,
		                        //qq.maps.MapTypeId.SATELLITE,
		                        //qq.maps.MapTypeId.HYBRID
		                    ],
		                    //设置控件位置相对上方中间位置对齐
		                  // position: qq.maps.ControlPosition.TOP_CENTER
		                }
					// 地图的中心地理坐标。
					});
					/*  var circle = new qq.maps.Circle({
						map : map,
						center : center,
						radius : 3000,
						strokeWeight : 5,
						strokeDashStyle : "dash"
					});  */
					var iconImg = "/static/images/map/icon.png";
					var anchor = new qq.maps.Point(10, 30);
					var size = new qq.maps.Size(32, 30);
					var origin = new qq.maps.Point(0, 0);
					var icon = new qq.maps.MarkerImage(iconImg, size, origin, anchor);
					new qq.maps.Marker({
						icon:icon,
				        //设置Marker的位置坐标
				        position: center,
				        //设置显示Marker的地图
				        map: map,
				       
				       
				    }); 
					for(var i=0;i<temp.tip.length;i++){
						var tip=temp.tip[i];
						var position=temp.pointer[i];
						 //mark(position,tip,map); 
					}
					 var polyline = new qq.maps.Polyline({
						path : temp.path,
						strokeWeight : 5,
						editable : false,
						map : map
					});
					var anchor = new qq.maps.Point(10, 30);
					var size = new qq.maps.Size(32, 30);
					var origin = new qq.maps.Point(0, 0);
					size = new qq.maps.Size(52, 30);
					var originShadow = new qq.maps.Point(32, 0);
				}
				
			}
		});
	}
	$(function() {
		var position_lng = "";
		var position_lat = "";
		getPosition()
		/* //初始化地图函数  自定义函数名init
		var trackData = $.parseJSON('${trackList}');
		var temp = {};
		var iconImg = "/static/images/map/icon.png";
		var iconImg2 = "/static/images/map/icon2.png";
		temp.path = new Array();
		temp.pointer = new Array();
		temp.pointer=new Array();
		temp.tip=new Array();
		var infos=new Array();
		var address = "${address}";
		var geocoder = new qq.maps.Geocoder();
		geocoder.getLocation(address);
		var center = {};
		var map = new qq.maps.Map(document.getElementById("container"));
		if (trackData.length > 0) {
			for ( var i = 0; i < trackData.length; i++) {
				temp.path.push( new qq.maps.LatLng(trackData[i].latitude,
						trackData[i].longitude));
				temp.pointer.push(new qq.maps.LatLng(trackData[i].latitude, trackData[i].longitude));
				temp.tip.push(trackData[i].alertDate.hours+":"+trackData[i].alertDate.minutes);
			}
		}
		geocoder.setComplete(function(result) {
			map.setCenter(result.detail.location);
			var marker = new qq.maps.Marker({
				map : map,
				position : result.detail.location
			});
			center = result.detail.location;
			console.log("center:"+center);
			init();
			//点击Marker会弹出反查结果
			 qq.maps.event.addListener(marker, 'click', function() {
			    Base.alert("坐标地址为： " + result.detail.location);
			}); 
		 });
		geocoder.setError(function() {
			console.log("error");
			if (temp.path.length > 0) {
				center = temp.path[0];
				console.log("error");
				init();
			}
		});
		console.log("center:" + center);
		if (center == null) {
			center = temp.path[0];
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
			map = new qq.maps.Map(document.getElementById("container"), {
				center : center, // 地图的中心地理坐标。
				zoom : 12
			// 地图的中心地理坐标。
			});
			 var circle = new qq.maps.Circle({
				map : map,
				center : center,
				radius : 3000,
				strokeWeight : 5,
				strokeDashStyle : "dash"
			}); 
			 new qq.maps.Marker({
		        //设置Marker的位置坐标
		        position: center,
		        //设置显示Marker的地图
		        map: map,
		       
		    }); 
			 for(var i=0;i<temp.tip.length;i++){
				var tip=temp.tip[i];
				var position=temp.pointer[i];
				 //mark(position,tip,map); 
			}
			 var polyline = new qq.maps.Polyline({
				path : temp.path,
				strokeWeight : 5,
				editable : false,
				map : map
			});
			var anchor = new qq.maps.Point(10, 30);
			var size = new qq.maps.Size(32, 30);
			var origin = new qq.maps.Point(0, 0);
			var icon = new qq.maps.MarkerImage(iconImg, size, origin, anchor);
			var icon2 = new qq.maps.MarkerImage(iconImg2, size, origin, anchor);
			size = new qq.maps.Size(52, 30);
			var originShadow = new qq.maps.Point(32, 0);
			var shadow = new qq.maps.MarkerImage(iconImg, size, originShadow,
					anchor);

			for ( var i = 0; i < temp.path.length; i++) {
				if (i != temp.path.length - 1) {
					new qq.maps.Marker({
						icon : icon,
						shadow : shadow,
						map : map,
						position : temp.path[i]
					});
				} else {
					new qq.maps.Marker({
						icon : icon2,
						shadow : shadow,
						map : map,
						position : temp.path[i]
					});
				}
				distance(temp.path[i], center);
			} 
		}
		
		
		 function distance(c ,d){
			var distance=qq.maps.geometry.spherical.computeDistanceBetween(c, d);
			if(distance>3000){
				out();
			}
		}
		
		function out(){
			$("span.in b").html("超出正常范围");
			$("span.in").attr("class","out");
		} 	 */	
	})
</script>
<%String remoteImage=getServletContext().getInitParameter("remoteImage");
    	request.setAttribute("remoteImage", remoteImage);%>
<script src="/static/js/layer/layer.js"></script>
<link href="http://fb.hafu365.comjs/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<style type="text/css">

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
/* .smnoprint{
	display: none;
  }   //缩放悬浮*/
#container{
	height:calc(100% - 50px);
	width: 100%;
	position: relative;
	background-color: rgb(229, 227, 223);
	overflow: hidden; 
	transform: translateZ(0px);
}
</style>
</head>
<body>
	<div class="container">
		<!--button class="menu-button" id="open-button">打开</button-->
		<div class="content-wrap" style="height:100%;padding-bottom:50px;">
			 <div class="pagePadding">
				<ul class="pageMenuUL1 row" style="text-align: center;height:100%;border: none;">
					<!-- <li class="alone" style="width: 100%;text-align: center;display: inline;margin-left: 45px;"><p><i class="in"></i>心率</p></li> -->
					<!-- <span class="in"><b style="margin-left: 45px;">三公里内</b></span> -->
					<c:set var="rempic" value="${initParam.remoteImage}/${sessionScope.CurrentUser.avater}"></c:set>
					<span class="photo"><img src="${sessionScope.CurrentUser.avater==null || sessionScope.CurrentUser.avater==''?'/static/images/man.png':rempic}"/></span>
					<span class="in"><b id="time" style="margin-left: 45px; font-size: 14px;color: #a9a9ab;float: left;margin-top: 13px;">默认时间</b></span>
					<!-- <span class="date"></span> -->
					<!-- <input type="text" name="dateStr" style="display: none;" onclick="WdatePicker({onpicked:log})"/> -->
				</ul>
			</div> 
			
			<div id="container" style="height:calc(100% - 50px);padding-top:0px"></div>
			<div class="positionTip">
			<h1>报警位置</h1>
			<p id="detailPosition">上海市长阳路长阳谷142645号阳谷142645号</p>
			</div>
		</div>
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
	</div>
	<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
</body>
</html>