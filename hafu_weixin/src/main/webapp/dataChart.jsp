<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">		
		<link rel="stylesheet" type="text/css" href="/static/css/normalize.css"/>
		<link rel="stylesheet" type="text/css" href="/static/css/jquery.hiSlider.min.css"/>
		<link rel="stylesheet" type="text/css" href="/static/css/alert.css"/>
		<script src="/static/js/jquery-2.1.4.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="/static/js/jquery.hiSlider.js" type="text/javascript" charset="utf-8"></script>
		<script src="/static/js/echarts.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="/static/js/index/index.js" type="text/javascript" charset="utf-8"></script>
		<script src="/static/js/Base.js"></script>
		<script charset="utf-8"
	src="http://map.qq.com/api/js?v=2.exp&key=FS7BZ-6EG3U-FCGV5-4HJYK-RJIFQ-ACBNI&libraries=drawing,geometry,convertor"></script>
		<style type="text/css">
		 	body,html{
			  width:100%;
			  height:100%;
			} 
			.hiSlider-pages a:nth-child(2){
				background-color: #FAD3D4;
			}
			
		</style>
<title>Insert title here</title>
</head>
<body>
		<div class="warp">
		<div class="layer-box">
			 <ul class="hiSlider hiSlider3">
			 	<li class="hiSlider-item">
		        	<h3>快乐指数：</h3>
		        	<div class="top">
		        		<div class="photo happy-arrow">
		        			<img src="/static/images/arrow/happy.png" />
		        		</div>
		        		<span class="scole colorT" id="happy"></span>
		        	</div>
		        	<div class="txt" style="margin-top:10px">
		        		<p>
		        		${empty sessionScope.CurrentUser.nickName?sessionScope.CurrentUser.realName:sessionScope.CurrentUser.nickName}<span id="mood"></span>
		        		</p>
		        	</div>
		        </li>
		        <li class="hiSlider-item">
		        	<h3>快乐指数：</h3>
		        	<div class="top">
		        		<div class="photo">
		        			<img src="/static/images/man.jpg" />
		        		</div>
		        		<span>${empty sessionScope.CurrentUser.nickName?sessionScope.CurrentUser.realName:sessionScope.CurrentUser.nickName}<i class="sex"></i></span>
		        	</div>
		        	<div class="chart" id="main"></div>
		        	<h1>超过全国<span class="colorT">60%</span>的爸妈</h1>
		        </li>
		        <li class="hiSlider-item">
			       	<h3>运动指数：</h3>
		        	<div class="top">
		        		<div class="photo">
		        			<img src="/static/images/man.jpg" />
		        		</div>
		        		<span>${empty sessionScope.CurrentUser.nickName?sessionScope.CurrentUser.realName:sessionScope.CurrentUser.nickName}<i class="sex"></i></span>
		        	</div>
		        	<span class="allStep colorT" id="stepNum"></span>
		        	<div class="chart" id="main2"></div>
		        	<h1>超过全国<span class="colorT">33%</span>的爸妈</h1>
		       </li>
		        <li class="hiSlider-item">
		        	<h3>行为识别：</h3>
		        	<div class="chartY" id="main3"></div>
		        	<div class="txt"><a href="" style="border-bottom:1px solid red;color:#585657">当前状态:<span id="current_behavior">&nbsp;</span></a></div>
		        </li>
		        <li class="hiSlider-item">
		        	<h3>行为识别：</h3>
		        	<div class="behaveBox">
		        		<ul>
		        		    <li><em>●</em><span class="fr" id="startTime"></span></li>
		        			<c:forEach items="${list}" var="bh">
		        			   <li><em>●</em>${bh.behaviorType}<span class="fr"><fmt:formatDate value="${bh.startTime}" pattern="HH:mm"/></span></li>
		        			</c:forEach>
		        		</ul>
		        	</div>
		        	<div class="img"></div>
		        </li>
		   </ul>
		   <div class="hiSlider-pages">
			   <a href="javascript:;" class="">1</a>
			   <a href="javascript:;" class="active">2</a>
			   <a href="javascript:;">3</a>
		   </div>
		</div>
		</div>
</body>
<script type="text/javascript">
       //异步获取行为数据
 	$(function(){
		    $.ajax({
		       url:"index/behaviorList",
		       type:"post",
		       dataType:"json",
		       async:false,
		       success:function(data){
		    	   var behaviorList= data.ob
		           if(data.status=='success'){
		        	  /*  for(var i=0;i<behaviorList.length();i++){
		        		      
		        	   } */
		           }     
		       }
		    });
		}) 
		 //获取快乐指数
	 $(function(){
			 var dayStep=$("#StepCount").val();
			 var careNum=$("#careNum").val();
		     var encourageNum=$("#encourageNum").val();	
		 $.ajax({
			url:"/happiness/findHappinessList",
			data:{"encourageNum":encourageNum,"dayStep":dayStep,"careNum":careNum},
			type:"POST",
			dataType:"json",
			success:function(data){
				   var happy=data.ob
				if(data.status="success"){
	              $("#happy").html(data.ob);
				}else{
				    Base.alert(data.msg);
				}
			}
		});	 
})
     //获取运动数据
     $(function(){
           $.ajax({
              url:"index/dayStep",
              type:"post",
              dataType:"json",
              async:false,
              success:function(data){
                  var stepNum=data.ob;
                  if(data.status=='success'){  
                	  $("#stepNum").html(data.ob+"步");
                  }else{
                      Base.alert(data.msg);
                  }
              }
           });
     });  
	</script>
	
	<script type="text/javascript">
	//这里是根据页面传值判断性别和首个页面
		var r=  window.location.search;
		var data = r.split('?');
		var sex = data[1].split('&')[0];
		var num = data[1].split('&')[1];
		var arr1 = sex.split('=');
		var arr2 = num.split('=');
		//折线图的颜色colorT
		var colorT = [],
	     colorOne = ['#9ED7EA','#9ED7EA'],
		 colorTwo = ['#F2A09D','#F2A09D'];
	    //饼状图的颜色colorArr
	    var colorArr = [],
    	    colorArr1 = ['#DEEDF2','#9ED7EA','#A8D9EA','#3F86C4'],
	        colorArr2 = ['#FCE9EB','#FAD7DE','#F08889','#E25563'];
		//这里判断是性别，为男性（arr[1]==1）才加载一下代码；
		if(arr1[1] == 1){
	    	$('.colorT').css({'color':'#9ED7EA'});
	    	$('.hiSlider-pages').find('a').eq(1).css('background-color','#DEEDF2');
	    	$('.hiSlider-item .img').css({'background':'url(/static/images/arrow/adr2.png) no-repeat center center','background-size':'90% auto'});
	    	$('.hiSlider-item .happy-arrow img').attr('src','/static/images/arrow/happy2.png');
	    	$('.hiSlider-item').find('.sex').css({'background':'url(/static/images/arrow/man.png) no-repeat center center', 'background-size': '90% auto'})
	    	colorT = colorOne;
	    	colorArr = colorArr1;
		}else{
			colorT = colorTwo;
	    	colorArr = colorArr2;
		};
		//弹出框的轮播
		$('.hiSlider3').hiSlider({
			//起始页面arr2[1]
	        startSlide:arr2[1],
	        isFlexible: true,
	        isSupportTouch: true,
	        isShowTitle: false,
	        titleAttr: function(curIdx){
	        }
	    });
	    $(function(){
	    	if(arr1[1] == 1){
		    	//这里判断是否为男性，为男性才加载一下代码；
		    	$('.colorT').css({'color':'#9ED7EA'});
		    	$('.hiSlider-pages').find('a').eq(1).css('background-color','#DEEDF2');
		    	$('.hiSlider-item .img').css({'background':'url(/static/images/arrow/adr2.png) no-repeat center center','background-size':'90% auto'});
		    	$('.hiSlider-item .happy-arrow img').attr('src','/static/images/arrow/happy2.png');
		    	$('.hiSlider-item').find('.sex').css({'background':'url(/static/images/arrow/man.png) no-repeat center center', 'background-size': '90% auto'})
		    
	    	}
	    var charts = $('.item-clone').find('#main');
	    });
	    //这是快乐指数的数据
		var cb = {
		            categories: ["1","2","3","4","5","6"],
		            data: [85, 70, 90, 85, 75,90]
		        };

	    var myChart = echarts.init(document.getElementById("main"));
	    var option = {
        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                axisLabel:{
                	show:false,
                	textStyle:{
                		color:'#fff'
                	}
                },
                axisLine:{show:false},
                splitLine:{show:false},
                axisTick:{show:false},
                data : cb.categories
            }
        ],
        yAxis : [
            {
                type : 'value',
                 axisLabel:{
                	show:false,
                	formatter: '{value}步 '
                },
                axisLine:{show:false},
                splitLine:{show:false},
                axisTick:{show:false},
                
            }
        ],
        series : [
	         
            {
                name:'快乐指数',
                type:'line',
                label:{
                	normal:{
                		show:true,
                		position:'bottom',
                		textStyle:{
                			fontSize:'12',
                			color:'#565656'
                		}
                	}
                },
                itemStyle : {  
                        normal : {  
                        	color:colorT[0],
                            lineStyle:{  
                                color:colorT[1]  
                            } 
                            
                        }  
                    },  
                data:cb.data
//             
            }
        ]
    };
    // 为echarts对象加载数据
    myChart.setOption(option);
    
var myChart2 = echarts.init(document.getElementById("main2"));
    var option = {
        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                axisLabel:{
                	show:false,
                	textStyle:{
                		color:'#fff'
                	}
                },
                axisLine:{show:false},
                splitLine:{show:false},
                axisTick:{show:false},
                data : ['一','二','三','四','五','六']
            }
        ],
        yAxis : [
            {
                type : 'value',
                 axisLabel:{
                	show:false,
                	formatter: '{value}步 ',
                	textStyle:{
                		color:'#fff'
                	},
                	
                },
                axisLine:{show:false},
                splitLine:{show:false},
                axisTick:{show:false},
                
            }
        ],
        series : [
            {
                name:'快乐指数',
                type:'line',
                label:{
                	normal:{
                		show:true,
                		position:'bottom',
                		textStyle:{
                			fontSize:'12',
                			color:'#565656'
                		}
                	}
                },
                itemStyle : {  
                        normal : {  
                        	color:colorT[0],
                            lineStyle:{  
                                color:colorT[1]  
                            } 
                            
                        }  
                    },  
                data:[85, 70, 90, 85, 75,90]
//             
            }
        ]
      };
    myChart2.setOption(option);
    
    var myChart3 = echarts.init(document.getElementById("main3"));
    
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
		    series: [
			       {
		            name:'运动分数',
		            type:'pie',
		            selectedMode: 'single',
		            clickwise:false,
		            radius: '5%',
		            center:['50%','40%'],
		            label: {
		                normal: {
		                    position: 'inner',
			                textStyle:{
			                    fontSize:16,
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
		            data:[
		                {value:1000, name:'98.25', selected:true}
		            ]
		            
		        },
		        {
		            name:'运动分数',
		            type:'pie',
		            selectedMode: 'single',
		            clickwise:false,
		            radius: '5%',
		            center:['50%','50%'],
		            label: {
		                normal: {
		                    position: 'inner',
			                textStyle:{
			                    fontSize:14,
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
		            data:[
		                {value:1000, name:'scole', selected:true}
		            ]
		            
		        },
		        {
		            name:'行为比例',
		            type:'pie',
		            radius: ['45%', '70%'],
		            hoverAnimation: false,
		            data:[
		                {value:635, name:'站着'},
		                {value:1000, name:'坐着'},
		                {value:400, name:'跑步'},
		                {value:800, name:'躺着'}
		            ],
		             labelLine: {
		                normal: {
		                    lineStyle: {
		                        color: 'rgba(0, 0, 0, 0.3)'
		                    },
		                    smooth: 0.2,
		                    length: 10,
		                    length2: 20
		                }
		            },
		            labelText:{
		            	normal:{
		            		textStyle:{
		            			color:'red'
		            		},
		            	}
		            	
		            }
		           
		        }
		    ]
		};
    // 为echarts对象加载数据
    myChart3.setOption(option);
    
	</script>
	
</html>