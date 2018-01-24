<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<title>行为识别</title>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
		<link rel="stylesheet" type="text/css" href="/static/css/normalize.css"/>
		<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="/static/css/index.css"/>
		<link rel="stylesheet" type="text/css" href="/static/css/footer.css"/>
		<link rel="stylesheet" type="text/css" href="/static/css/common.css"/>
		<script src="/static/js/jquery-2.1.4.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="/static/js/echarts.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="/static/js/classie.js"></script>
        <script src="/static/js/main3.js"></script>
        <script src="/static/js/jquery.min.js"></script>               
		<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
		<script type="text/javascript" src="/static/js/voice/Math.uuid.js"></script>
		<script type="text/javascript" src="/static/js/voice/Code.js"></script>
		<script type="text/javascript" src="/static/js/voice/wechatJs.js"></script>

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
		<div class="content" >
		 	<div class="banner">
		 		<img src="/static/images/activeBg.jpg" />
		 	</div>
		 	<div class="dates">
		 	    <form action="/index/behaviorList?dateStr">
		 		<input type="text" name="dateStr" class="kbtn" readonly="readonly" value=""/>
		 		<input type="text" id="beginTime" name="time"/>
		 		</form>
		 	</div>
		 	<ul class="active-con">
		 	 <li class="active-pie">
		        	<div class="chartY" id="main"></div>
		        	<span style="display:inline-block;">${empty sessionScope.CurrentUser.nickName?sessionScope.CurrentUser.realName:sessionScope.CurrentUser.nickName }<i class="sex"></i></span>
		        </li>
		 		<li class="">
		        	<div class="behaveBox">
		        		<ul style="text-align:center;">
		        		 <%--  <c:forEach items="${list}" var="bh">
		        			<li><em>●</em>${bh.behaviorType}<span class="fr"><fmt:formatDate value="${bh.startTime}" pattern="HH:mm"/></span></li>
		        			</c:forEach>   --%>
		        		</ul>
		        	</div>
		        </li>
		       </ul>
		</div>
		<div id="datePlugin"></div>
		<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
	</body>
</html>
<script src="/static/js/date/date.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/date/iscroll.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/slide-bar.js" type="text/javascript" charset="utf-8"></script>

<script type="text/javascript">
//这里是根据页面传值判断性别和首个页面

document.addEventListener("touchstart", function(e) {
			e.touches; //多点触控
			startX = e.touches[0].pageX;
			startY = e.touches[0].pageY;
	});
document.addEventListener("touchmove", function(e) {
			if (move == true) {
				e.preventDefault();
			} 
}, false);
	var width = $(window).width()*0.10;
document.addEventListener("touchend", function(e) {
		e.changedTouches; //结束位置,不能再用touches
		endX = e.changedTouches[0].pageX;
		endY = e.changedTouches[0].pageY;
		if ((endX+width < startX) && move == true &&   Math.abs(endY-startY)<30 ) {
			slideMove();
		}else if(endX<startX){
			return false;
		}else if((endY-startY)< -10){
			if($('#datePage').css('display') == 'none' || $('#datePlugin').html() == ''){
				if($('.banner').find('img').length>0){
					$('.banner').slideUp();
				}
			}
			
		}
});	


//一周步数
var step_arr = new Array();
var date_arr = new Array();
//昨日行为类型及时间数组
var yesterday_behav_arr = new Array();
var yesterday_time = new Array();
//今日行为列表及开始时间数组
var today_behav_arr = new Array();

function behaviorList(){
	var dateStr=$('.kbtn').val();
	/* alert("弹出的时间是:"+dateStr); */
    $.ajax({
       url:"/index/be_list",
       type:"post",
       dataType:"json",
       data:{"dateStr":dateStr},
       async:false,
       success:function(data){
        var behaviorList= data.ob
           if(data.status=='success'){
        	   var lis='';
        	    for(var i = 0; i < data.ob.length; i++) {
					lis += '<li><em>●</em>'+data.ob[i].behaviorType+'<span class="fr">'+ Date2Format(data.ob[i].startTime.time)+'</span></li>';
        	    }
        	        $('.behaveBox').find('ul').html(lis); 
           }else{
        	        $('.behaveBox').find('ul').html(data.msg);  
           }    
       }
    });
}
$(function(){
	//console.log(colorArr)
	$('#beginTime').date(this,log);
	var Dates = new Date();
	var years = Dates.getFullYear();
	var months = Dates.getMonth()+1;
	var days = Dates.getDate();
	$('.kbtn').val(years +'-'+months +'-'+days);
	function log(time){
		$('.kbtn').val(time);
		behaviorList();
		getdayBehavior();
		yesterdayBehavCharts();
	}
});

$(function(){
	behaviorList();
     /* yesterdayBehavior(); */
	 getdayBehavior();
	 yesterdayBehavCharts();
	
})
  //时间的转换
		  function Date2Format(date){
						var tempDate = new Date(date);
						h = tempDate.getHours();
						if(h<10){
				            	h='0'+h+':';
				            }else{
				            	h=h+':';
				        }
						m = tempDate.getMinutes();
						if(m<10){
			            	m='0'+m;
			            }else{
			            	m=m;
			            }
						var str =h+m
						return str ;
					}    




function getdayBehavior(){
	var dateStr=$('.kbtn').val();
		$.ajax({
	       url:"/health/getdayBehavior",
	       type:"post",
	       dataType:"json",
	       data:{"dateStr":dateStr},
	       async:false,
	       success:function(data){
	    	   yesterday_behav_arr = data.list;
	   		   yesterday_time = data.ob;
	   		   yesterdayBehavCharts();
	    	   }
	       });
} 






//异步获取行为比例
/*  function yesterdayBehavior(){
	$.getJSON("/index/getYesterdayBehavior",function(data){
		yesterday_behav_arr = data.list;
		yesterday_time = data.ob;
		yesterdayBehavCharts();
	});
} 
yesterdayBehavior() ; */
function yesterdayBehavCharts(){
		var myChart = echarts.init(document.getElementById("main"));
	    option = {
//	    	visualMap: {
//			        show: false,
//			        min: 80,
//			        max: 600,
//			        inRange: {
//			            colorLightness: [0, 0.8]
//			        }
//			    },
			color:colorArr,
			 legend: {
		        orient: 'vertical',
//		        x: 'right',
//		        y:'bottom',
		        right:10,
		        bottom:0,
		        textStyle:{
		        	color:'#666',
		        	fontSize:10,
		        },
		        itemGap:-2,
		        itemWidth:10,
		        itemHeight:10,
		       /*  data:day_behav_arr */
		        data:yesterday_behav_arr
		    },
		    series: [
			       {
		            name:'运动分数',
		            type:'pie',
		            selectedMode: 'single',
		            clickwise:false,
		            radius: '5%',
		            center:['50%','35%'],
		            label: {
		                normal: {
		                    position: 'inner',
			                textStyle:{
			                    fontSize:18,
			                	color:'#999',
			                },
			                
		                },
		                 
		            },
		            itemStyle:{
		            	normal:{
		            		color:'white',
		            		opacity:0.0
		            	},
		            },
		            labelLine: {
		                normal: {
		                    show: false
		                }
		            },
		           /*  data:new Array(day_time[0]) */
		            data:new Array(yesterday_time[0])
		        },
		        {
		            name:'运动分数',
		            type:'pie',
		            selectedMode: 'single',
		            clickwise:false,
		            radius: '5%',
		            center:['50%','43%'],
		            label: {
		                normal: {
		                	
		                    position: 'inner',
			                textStyle:{
			                    fontSize:12,
			                	color:'#ccc',
			                },
			                
		                },
		                 
		            },
		            itemStyle:{
		            	normal:{
		            		color:'white',
		            		opacity:0.0
		            	},
		            },
		            labelLine: {
		                normal: {
		                    show: false
		                }
		            },
		           /*  data:new Array(day_time[1]) */
		            data:new Array(yesterday_time[1])
		            
		        },
		        {
		            name:'行为比例',
		            type:'pie',
		            radius: ['50%', '80%'],
		            center:['50%','45%'],
		            hoverAnimation: false,
		            clickWise:false,
		            data:yesterday_behav_arr,
		             labelLine: {
		                normal: {
		                    lineStyle: {
		                        color: 'rgba(0, 0, 0, 0.3)'
		                    },
		                    smooth: 0.2,
		                    length: 5,
		                    length2: 10
		                }
		            },
		            label:{
		            	normal:{
		            		show:false,
		            		 textStyle:{
			                    fontSize:12,
			                	color:'#aaa',
			                },
		            	}
		            }
		        }
		    ]
		};
    // 为echarts对象加载数据
    myChart.setOption(option);	
}
</script>