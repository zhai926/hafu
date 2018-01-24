<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
<title>数据对比</title>
<c:import url="../css.jsp"></c:import>
<style type="text/css">
.pageMenuUL1 li{
color:#32CF6D;
}
</style>
</head>

<body>
	<div class="container">
		<c:import url="../index/left_slide.jsp"></c:import>
		<!--button class="menu-button" id="open-button">打开</button-->
		<div class="content-wrap">
			<div class="content">

				<div class="pagePadding">
					<!--  菜单的click样式，是表示，当前点击的按钮，加上click样式 背景变蓝色，字变白色  -->
					<ul class="pageMenuUL1">
						<li class="alone w20">综合</li>
						<li class="alone w20">运动</li>
						<li class="alone w20">健康</li>
						<li class="alone w20">身体</li>
						<li class="alone w20">环境</li>
					</ul>
					<div class="margin_top2">
						<div id="chart0"></div>
						<div id="chart1"></div>
						<div id="chart2"></div>
					</div>
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
	<!-- /container -->

	
	<script src="/static/js/chart/higharts.js"></script>
	<script type="text/javascript">
		$(function() {
			$("ul.pageMenuUL1 li:nth-child(${type})").addClass("click");
			$("ul.pageMenuUL1 li").each(function() {
				var index = $(this).index() + 1;
				$(this).bind("click", function() {
					window.location.href = "/health/dataCompare?type=" + index;
				});
			});
			var categories=new Array();
			var series=new Array();
			function merge(array){
				var result=new Array();
				for(var i=0;i<array[0].length;i++){
					var total=0;
					var score=0;
					for(var j=0;j<array.length;j++){
						if(array[j][i]!=null){
							score+=array[j][i];
							total+=1;
						}
					}
					if(total!=0){
						result.push(parseInt(score*1.0/total));
					}else{
						result.push(null);
					}
				}
				return result;
			}
			$.ajax({"url" : "/health/getCompareData",
				async : false,
				success:function(d){
					var day=new Array();
					for(var i=0;i<5;i++){
						var temp=(d["day"][i]["month"]+1)+"/"+d["day"][i]["date"];;
						day.push(temp);
					}
					categories.push(day);
					var week=new Array();
					week.push("五周前");
					week.push("四周前");
					week.push("三周前");
					week.push("二周前");
					week.push("一周前");
					categories.push(week);
					var month=new Array();
					for(var i=0;i<5;i++){
						var temp=d["month"][i]["month"]+1;
						month.push(temp+"月");
					}
					categories.push(month);
					if("${type}"==2){
						var dayData=d["sport_day"];
						series.push([{name:"日平均值",data:dayData}]);
						var weekData=d["sport_week"];
						series.push([{name:"周平均值",data:weekData}]);
						var monthData=d["sport_month"];
						series.push([{name:"月平均值",data:monthData}]);
					}else if("${type}"==3){
						var dayData=d["JKSJ_day"];
						series.push([{name:"日平均值",data:dayData}]);
						var weekData=d["JKSJ_week"];
						series.push([{name:"周平均值",data:weekData}]);
						var monthData=d["JKSJ_month"];
						series.push([{name:"月平均值",data:monthData}]);
					}else if("${type}"==4){
						var dayData=d["STSJ_day"];
						series.push([{name:"日平均值",data:dayData}]);
						var weekData=d["STSJ_week"];
						series.push([{name:"周平均值",data:weekData}]);
						var monthData=d["STSJ_month"];
						series.push([{name:"月平均值",data:monthData}]);
					}else if("${type}"==5){
						var dayData=d["HJSJ_day"];
						series.push([{name:"日平均值",data:dayData}]);
						var weekData=d["JKSJ_week"];
						series.push([{name:"周平均值",data:weekData}]);
						var monthData=d["HJSJ_month"];
						series.push([{name:"月平均值",data:monthData}]);
					}else if("${type}"==1){
						var dayArray=new Array();
						dayArray.push(d["sport_day"]);
						dayArray.push(d["JKSJ_day"]);
						dayArray.push(d["STSJ_day"]);
						dayArray.push(d["HJSJ_day"]);
						var dayData=merge(dayArray);
						series.push([{name:"日平均值",data:dayData}]);
						var weekArray=new Array();
						weekArray.push(d["sport_week"]);
						weekArray.push(d["JKSJ_week"]);
						weekArray.push(d["STSJ_week"]);
						weekArray.push(d["HJSJ_week"]);
						var weekData=merge(weekArray);
						series.push([{name:"周平均值",data:weekData}])
						var monthArray=new Array();
						monthArray.push(d["sport_month"]);
						monthArray.push(d["JKSJ_month"]);
						monthArray.push(d["STSJ_month"]);
						monthArray.push(d["HJSJ_month"]);
						var monthData=merge(monthArray);
						series.push([{name:"月平均值",data:monthData}]);
					}
				},
				"dataType" : "json"});
			//series.push([{"name":"月平均值65分",data:[50,60,70,80,90,60,70]}],[{"name":"季平均值65","data":[30,40,50,50]}],[{"name":"年平均值65","data":[50,40,30]}]);
			Highcharts.setOptions({colors:["#52d1c0"]});
			$('#chart0').highcharts({
				title : {
					text : ''
				},
				xAxis : {
					categories : categories[0],
					labels : {
						align : "left",
					}
				},
				yAxis : {
					title : {
						text : '数值'
					},
					plotLines : [ {
						value : 0,
						width : 1,
						color : '#808080'
					} ]
				},
				legend : {
					layout : 'horizontal',
					align : 'right',
					verticalAlign : 'top',
					borderWidth : 0
				},
				series : series[0],
				tooltip: {
		            formatter: function () {
		                var s = '';
		                $.each(this.points, function () {
		                    s += '<br/>' + this.series.name + ': ' +
		                        this.y ;
		                });
		                return s;
		            },
		            shared: true
		        },
		        credits:{
					enabled:false
				}
			});
			Highcharts.setOptions({colors:["#ffa648"]});
			$('#chart1').highcharts({
				title : {
					text : ''
				},
				xAxis : {
					categories : categories[1],
					labels : {
						align : "left",
					}
				},
				yAxis : {
					title : {
						text : '数值'
					},
					plotLines : [ {
						value : 0,
						width : 1,
						color : '#808080'
					} ]
				},
				legend : {
					layout : 'horizontal',
					align : 'right',
					verticalAlign : 'top',
					borderWidth : 0
				},
				series : series[1],
				tooltip: {
		            formatter: function () {
		                var s = '';
		                $.each(this.points, function () {
		                    s += '<br/>' + this.series.name + ': ' +
		                        this.y;
		                });
		                return s;
		            },
		            shared: true
		        },
		        credits:{
					enabled:false
				}
			});
			Highcharts.setOptions({colors:["#669ef3"]});
			$('#chart2').highcharts({
				title : {
					text : ''
				},
				xAxis : {
					categories : categories[2],
					labels : {
						align : "left",
					}
				},
				yAxis : {
					title : {
						text : '数值'
					},
					plotLines : [ {
						value : 0,
						width : 1,
						color : '#808080'
					} ]
				},
				legend : {
					layout : 'horizontal',
					align : 'right',
					verticalAlign : 'top',
					borderWidth : 0
				},
				series : series[2],
				tooltip: {
		            formatter: function () {
		                var s = '';
		                $.each(this.points, function () {
		                    s += '<br/>' + this.series.name + ': ' +
		                        this.y ;
		                });
		                return s;
		            },
		            shared: true
		        },
		        credits:{
					enabled:false
				}
			});
		})
	</script>
	<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
</body>
</html>
