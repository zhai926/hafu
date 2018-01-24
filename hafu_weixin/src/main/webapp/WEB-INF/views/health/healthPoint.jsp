<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.hafu.entity.HfCheckData"%>
<%@page import="java.util.List"%>
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
<title>健康评分</title>
<c:import url="../css.jsp"></c:import>
<script src="/static/js/layer.js"></script>
<!--注意这里调用的css不同-->
<link href="/static/css/layer_guli.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
function temFun1(){
    layer.open({
    content: '<div class="guliDiv"><div class="myWidth33"><div class="myGuliDiv"><div class="pic ico_guli1"></div><div class="tt">今天表现真棒，您的孩子为您热情鼓掌。</div></div></div><div class="myWidth33">  	<div class="myGuliDiv"><div class="pic ico_guli2"></div><div class="tt">您的孩子为您送上一束献花。</div></div></div><div class="myWidth33">  	<div class="myGuliDiv"><div class="pic ico_guli3"></div><div class="tt">您的孩子给您一个拥抱。</div></div></div></div>',
});
    $(".myGuliDiv").bind("click",function(){
    	Base.open();
    	var index=$(this).parent().index(".guliDiv .myWidth33")+1;
    	$.post("/prompt/addEncourage",{"encouragementType":index},function(){
    		Base.close();
    	},"json");
    	layer.closeAll();
    })
}
</script>
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
						<%Date date=new Date();
							SimpleDateFormat sdf=new SimpleDateFormat("MM-dd");
							SimpleDateFormat fullsdf=new SimpleDateFormat("yyyy-MM-dd");
							Calendar calendar=Calendar.getInstance();
							for(int i=0;i<7;i++){
								sdf.format(calendar.getTime());
								String str=i==0?"今天":"<span>"+calendar.get(Calendar.DATE)+"<i>"+(calendar.get(Calendar.MONTH)+1)+"</i></span>";
								out.println("<li class='"+(i==0?"now":"")+"' data-full-data="+fullsdf.format(calendar.getTime())+">"+str+"</li>");
								calendar.add(Calendar.DATE, -1);
							}
						%>
					</ul>

					<dl class="dataDL1 margin_top2">
						<dt>
							健康评分：
							<c:choose>
								<c:when test="${yesterday==null}">暂无数据</c:when>
								<c:otherwise><c:out value="${yesterday.point}" /></c:otherwise>
							</c:choose>
						</dt>
						<dd>
							<div class="left">
								活跃度：
								<c:out value="${yesterday.liveness}" />
							</div>
							<div class="right">
								步数：
								<c:out value="${yesterday.stepCount}" />
							</div>
						</dd>
						<dd>
							<div class="left">步频：${yesterday.stepSpeed} 步/分钟 </div>
							<div class="right">佩戴：${yesterday.activity} 分</div>
						</dd>
						<dd>
							<div class="left">久坐次数：${yesterday.staticCount}次</div>
							<div class="right">
								睡眠：
								<c:out value="${yesterday.rest}" />
								分
							</div>
						</dd>

					</dl>
					<div class="margin_top2" id="chart"></div>
					<div class="mySubmit1 margin_top2" onclick="temFun1()">鼓励一下</div>
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
		var series=new Array();
		var categories=new Array();
		var color=["#b980eb","#52d1c0","#ffa648","#669ef3"];
		function between(array,min,max){
			var a=new Array();
			for(var i=0;i<array.length;i++){
				if(array[i]!=null){
					if(array[i]>=min&&array[i]<=max){
						a.push(100);
					}else{
						a.push(0);
					}
				}else{
					a.push(null);
				}
			}
			return a;
		}
		
		function less(array,limit){
			var a=new Array();
			for(var i=0;i<array.length;i++){
				if(array[i]==null){
					a.push(null);
				}else if(array[i]<=limit){
					a.push(100);
				}else{
					a.push(0);
				}
			}
			return a;
		}
		
		
		function greater(array,limit){
			var a=new Array();
			for(var i=0;i<array.length;i++){
				if(array[i]==null){
					a.push(null);
				}else if(array[i]>=limit){
					a.push(100);
				}else{
					a.push(0);
				}
			}
			return a;
		}
		
		function merge(array){
			var number=new Array();
			var sum=new Array();
			for(var i=0;i<array.length;i++){
				var temp=array[i];
				for(var j=0;j<temp.length;j++){
					if(i==0){
						if(temp[j]!=null) {
							sum.push(temp[j]);
							number.push(1);
						}else{
							sum.push(0);
							number.push(0);
						}
						
					}else{
						if(temp[j]!=null){
							sum[j]+=temp[j];
							number[j]+=1;
						}
					}
				}
			}
			var a=new Array();
			for(var i=0;i<number.length;i++){
				if(number[i]==0){
					a.push(null);
				}else{
					a.push(Math.round(sum[i]*1.0/number[i]));
				}
			}
			return a;
		}
		$.ajax({"url":"/health/getHelthPoint?date=${date}",async:false,success:function(d){
			var envi=new Array();
			var body=new Array();
			var sport=new Array();
			var health=new Array();
			for ( var p in d) {
				if(p=="收缩压"){
					health.push(between(d[p],130,148));
				}else if(p=="甘油三酯"){
					health.push(between(d[p],0.56,1.7));
				}else if(p=="风力"){
					//envi.push(less(d[p],50));
				}else if(p=="低密度酯蛋白"){
					health.push(between(d[p],2.07,3.12));
				}else if(p=="舒张压"){
					health.push(between(d[p],85,89));
				}else if(p=="空气质量"){
					envi.push(between(d[p],0,50));
				}else if(p=="摄入热量"){
					body.push(greater(d[p],2000));
				}else if(p=="心率"){
					body.push(between(d[p],60,100));
				}else if(p=="睡眠"){
					body.push(between(d[p],7,10));
				}else if(p=="消耗卡路里"){
					body.push(greater(d[p],1500));
				}else if(p=="运动量"){
					sport.push(greater(d[p],4000));
				}else if(p=="总胆固醇"){
					health.push(between(d[p],2.8,5.17));
				}else if(p=="湿度"){
					envi.push(between(d[p],30,80));
				}else if(p=="血糖"){
					health.push(between(d[p],3.89,6.1));
				}else if(p=="高密度酯蛋白"){
					health.push(between(d[p],0.94,2.0));
				}
			}
			var temp=merge(body);
			series.push({"name":"身体数据","data":temp});
			temp=merge(envi);
			series.push({"name":"环境数据","data":temp});
			temp=merge(sport);
			series.push({"name":"运动数据","data":temp});
			temp=merge(health);
			series.push({"name":"健康数据","data":temp});
			var temp=d["date"];
			for(var i=0;i<temp.length;i++){
				categories.push(temp[i].date);
			}
			},"dataType":"json"
		});
		$(function() {
			$("ul.pageMenuUL1 li[data-full-data='${date}']").addClass("click");
			$("ul.pageMenuUL1 li").each(function(){
				var attr=$(this).attr("data-full-data");
				$(this).bind("click",function(){
					window.location.href="/health/healthPoint?date="+attr;
				})
			})
			Highcharts.setOptions({colors:color});
		$('#chart').highcharts({
						credits:{
							enabled:false
						},
						title : {
							text : ''
						},
						xAxis : {
							categories: categories,
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
						series : series,
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
				        }
					});
					});
	</script>
	<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
</body>
</html>
