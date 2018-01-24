<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html; charset=utf-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
<title>身体数据</title>
<c:import url="../css.jsp"></c:import>
<script src="/static/js/layer.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/static/js/main3.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/voice/Math.uuid.js"></script>
<script type="text/javascript" src="/static/js/voice/Code.js"></script>
<script type="text/javascript" src="/static/js/voice/wechatJs.js"></script>	
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
						<li class="alone w33">心率</li>
						<li class="alone w33">睡眠</li>
						<li class="alone w33">体能</li>
					</ul>
					<div class="margin_top2">
						<div id="chart"></div>
					</div>
					<div class="divStyle5">
						<div class="left">检测地</div>
						<div class="right">
							<input type="text" value="${checkData.checkAddress }"
								class="myInput" id="address" disabled="disabled" style="background: white;">
						</div>
					</div>
					<div class="clear margin_top2">
						<c:choose>
							<c:when test="${type==1}">
								<div class="myLeft width_p48">
									<div class="mySubmit1" onclick="temFun1()">鼓励一下</div>
								</div>
								<div class="myRight width_p48">
									<div class="mySubmit2" onclick="window.location.href='/health/toAddCheckData?type=XL'">手动添加</div>
								</div>
							</c:when>
							<c:otherwise>
								<div class="mySubmit1" onclick="temFun1()">鼓励一下</div>
							</c:otherwise>
						</c:choose>
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
			var series = new Array();
			var categories = new Array();
			var color=new Array();
			var address;
			var unit={"心率":"次/钟","睡眠":"小时","摄入热量":"大卡","消耗卡路里":"大卡"}
			var colors={"步数":"#52d0c2","时长":"#52d0c2","舒张压":"#669ef3","收缩压":"#b980eb","总胆固醇":"#669ef3","低密度酯蛋白":"#fea645","甘油三酯":"#b87fea",
					"高密度酯蛋白":"#52d0c2","血压":"#52d0c2","血糖":"#52d0c2","心率":"#52d0c2","睡眠":"#52d0c2","消耗卡路里":"#b980eb","摄入热量":"#669ef3"};
			$.ajax({
				"url" : "/health/getHelthPoint",
				async : false,
				success : function(d) {
					for ( var p in d) {
						if (p == 'date') {
							var temp = d[p];
							for ( var i = 0; i < temp.length; i++) {
								var myDate = new Date();
								myDate.setTime(temp[i].time);
								categories.push(myDate.getDate());
							}
						} else if (p == "心率" || p == "睡眠" || p == "摄入热量"
								|| p == "消耗卡路里") {
							var temp = {};
							temp.name = p;
							temp.data = d[p];
							if (p == "心率" && '${type}' == 1) {
								series.push(temp);
								color.push(colors[p]);
							} else if ('${type}' == 2 && p == "睡眠") {
								series.push(temp);
								color.push(colors[p]);
							} else if (('${type}' == 3 && p == "摄入热量")
									|| ('${type}' == 3 && p == "消耗卡路里")) {
								series.push(temp);
								color.push(colors[p]);
							}
						}else if(p=='STSJ_XLaddress'||p=='STSJ_SMaddress'||p=='STSJ_TNaddress'){
							if('${type}'==1&&p=='STSJ_XLaddress'){
								address=d[p];
							}else if('${type}'==2&&p=='STSJ_SMaddress'){
								address=d[p];
							}else if('${type}'==3&&p=='STSJ_TNaddress'){
								address=d[p];
							}
						}
					}
				},
				"dataType" : "json"
			});
			$("ul.pageMenuUL1 li:nth-child(${type})").addClass("click");
			$("ul.pageMenuUL1 li").each(function() {
				var index = $(this).index() + 1;
				$(this).bind("click", function() {
					window.location.href = "/health/bodyData?type=" + index;
				});
			});
			Highcharts.setOptions({colors:color});
			$('#chart').highcharts({
				title : {
					text : ''
				},
				xAxis : {
					categories : categories,
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
		                var index=this.points[0].point.index;
		                $("#address").val(address[index]);
		                $.each(this.points, function () {
		                    s += '<br/>' + this.series.name + ': ' +
		                        this.y + unit[this.series.name];;
		                });
		                $("#address").val(address[index]);

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
