<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.hafu.entity.HfCheckData"%>
<%@page import="java.util.List"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8" />
		<title>运动数据</title>
		<c:import url="../css.jsp"></c:import>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
		<link rel="stylesheet" type="text/css" href="/static/css/normalize.css"/>
		<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="/static/css/index.css"/>
		
		<link rel="stylesheet" type="text/css" href="/static/css/footer.css"/>
		<script src="/static/js/jquery-2.1.4.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="/static/js/echarts.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="/static/js/Constans.js"></script>
        <script src="/static/js/Base.js"></script>
        <link href="/static/css/layer_guli.css" rel="stylesheet" type="text/css">
        <script src="/static/js/layer.js"></script>
		<script>   
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
	</head>
	<body >
	  
		<script src="/static/js/top.js" type="text/javascript" charset="utf-8"></script>
		<div class="content hide" id="conWarp" style="z-index: 9999;background-color: rgba(0,0,0,0.3);"></div>
		<div class="content" style="padding-bottom:0">
		 	<div class="banner" >
		 		<div class="chart" id="main" style="width: 100%;height: 200px;"></div>
		 	</div>
		 	<div class="step-title">
		 	    <div class="nav ">
		 		    <ul class=" nav-list">
		 		        <%Date date=new Date();
							SimpleDateFormat sdf=new SimpleDateFormat("MM-dd");
							SimpleDateFormat fullsdf=new SimpleDateFormat("yyyy-MM-dd");
							Calendar calendar=Calendar.getInstance();
							for(int i=0;i<7;i++){
								sdf.format(calendar.getTime());
								/* <i>"+(calendar.get(Calendar.MONTH)+1)+"</i> */
								String str=i==0?"今天":"<span>"+calendar.get(Calendar.DATE)+"</span>";
								out.println("<li title='"+ i +"' class='"+(i==0?"select":"")+"' data-full-data="+fullsdf.format(calendar.getTime())+">"+str+"</li>");
								calendar.add(Calendar.DATE, -1);
							}
						%>   
		 		    </ul>
		 		</div>
		 	</div> 
		 	<div class="step-con">
		 		<h3>超过全国<span id="sportScore">60%</span>的用户</h3>
		 		<ul class="stepList">
		 		    <!-- 异步获取今日数据 -->
		 			<li>今日 <span class="fr" id="stepNum">${SposrtstepNum}步</span></li>
		 			<li class="week">本周<span class="fr" id="weekStepNum">平均（2000步/天）</span>
		 			<!-- <div class="range"><span></span></div> -->
		 			
		 			</li>
		 			<li class="histroy">历史<span class="fr" id="historyStepNum">平均（1600步/天）</span>
		 				<!-- <div class="range"><span></span></div> -->
		 			</li>
		 			<li></li>
		 		</ul>
		 		<!-- <a href="" class="fr moreData">日运动数据</a> -->
		 	</div>
		</div>
		<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
	</body>
</html>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/static/js/main3.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/voice/Math.uuid.js"></script>
<script type="text/javascript" src="/static/js/voice/Code.js"></script>
<script type="text/javascript" src="/static/js/voice/wechatJs.js"></script>	
<script src="/static/js/slide-bar.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript"> 


/*$(function(){
		$("ul.nav-list li[data-full-data='${date}']").addClass("click");
		$("ul.nav-list li").each(function(){
			var attr=$(this).attr("data-full-data");
			$(this).bind("click",function(){
				window.location.href="/health/getStep?date="+attr;
			});
		})
})*/

var sport_date_arr = new Array();
var sport_data_arr = new Array();
var index = 0 ;
$(function(){
	getWeekSport(index);
	getSportData();
});

	$('.nav ul li').on('touchstart',function(e){
		e.preventDefault();//js阻止时间冒泡和浏览器默认事件
		$('.nav').find('li').removeClass('select');
		$(this).addClass('select');
		index = $(this).attr("title");
		getWeekSport(index);
	})
	
	var bgColor="rgba(156, 213, 221,0.5)";

	var  colorT = ['white','#4086C4'];
	if($('#userGender').val()=="女"){
		bgColor="#FAD3D4";
		var  colorT = ['white','#EB5F5E'];
	}

//异步获取一周数据
function getWeekSport(index){
	$.getJSON("/index/weekSport",{"index":index},function(data){
		sport_date_arr = [];
		sport_data_arr = [];
		$.each(data.ob, function(i, item) {
			sport_date_arr.push(item.sportDate);
			sport_data_arr.push(item.sportData);
		});
		weekDataCharts();
	});
}
function getSportData(){
	$.getJSON("/index/sportData",function(data){
		var score = data.ob.score;
		$('.step-con #sportScore').html(score);
		var todaySteps = data.ob.todaySteps;
		$('.step-con #stepNum').html(todaySteps+"步");
		var weekAve = data.ob.weekAve;
		/* var weekAVE='平均('+weekAve+'步/天)'; */
		$('.step-con #weekStepNum').html('平均('+weekAve+'步/天)');
		var monthAve = data.ob.monthAve;
		$('.step-con #historyStepNum').html('平均('+monthAve+'步/天)');
		
	});
}

function weekDataCharts(){
	
	var cb = {
	            categories: sport_date_arr,
	            data: sport_data_arr
	        };

		    var myChart = echarts.init(document.getElementById("main"));
		    var option = {
		    backgroundColor: bgColor,
	        calculable : true,
//	       legend: {
//		        data:['环境指数','运动指数'],
//		        x:"right",
//		        y:'bottom'
//		    },
			tooltip: {
	    		trigger: 'item',
	    		textStyle:{
	    			fontSize:'12px'
	    		}
			},
	         grid: {
	            left: '-15%',
	            right: '1%',
	            bottom: '-14%',
	            top:"1%",
	            containLabel: true
	        },
	        xAxis : [
	            {
	                type : 'category',
	                boundaryGap : false,
	                axisLabel:{
	                	show:true,
	                	textStyle:{
	                		color:'white'
	                	}
	                },
	                axisLine:{show:false},
	                splitLine:{show:false},
	                axisTick:{show:false},
	                splitLine:{
	                	show:true,
	                	lineStyle:{
	                		color:"white"
	                	}
	                },
//	              axisTick:{show:true},
	                data : cb.categories
	            }
	        ],
	        yAxis : [
	            {
	                type : 'value',
	                axisLabel:{
	                	show:true,
	                	formatter: '{value}步 '
	                },
	                axisLine:{show:false},
	                axisTick:{show:false},
	                splitLine:{
	                	show:true,
	                	lineStyle:{
	                		color:"white"
	                	}
	                },
//	              axisTick:{show:true},
	                
	            }
	        ],
	        series : [
		         
	            {
	                name:'运动数据',
	                type:'line',
	                label:{
	                	normal:{
	                		show:false,
	                		position:'bottom',
	                	}
	                },
	                hoverAnimation: false,
	                symbolSize: 6,
	                itemStyle : {  
	                        normal : {  
	                        	color:colorT[0],
	                            lineStyle:{  
	                                color:colorT[1]  
	                            } 
	                            
	                        }  
	                    },
	                    areaStyle: {
			                normal: {
			                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
			                        offset: 0,
			                        color: 'rgba(156, 213, 221,0.9)'
			                    }, {
			                        offset: 1,
			                        color: 'rgba(156, 213, 221,0.9)'
			                    }])
			                }
			            },
	                data:cb.data
	            }
	            
	        ]
	    };
	    // 为echarts对象加载数据
	    myChart.setOption(option);
}

</script>
