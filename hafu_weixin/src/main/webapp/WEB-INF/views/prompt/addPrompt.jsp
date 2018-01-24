<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="com.hafu.entity.HfPrompt"%>
<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
<title>添加关爱提醒</title>
<c:import url="../css.jsp"></c:import>
<script src="/static/js/layer.js"></script>
<!--注意这里调用的css不同-->
<link href="/static/css/layer_guli.css" rel="stylesheet" type="text/css">
<!--时间控件mobiscroll--> 
  <script src="/static/mobiscroll/2.6.2/mobiscroll.core-2.6.2.js" type="text/javascript"></script>
	<script src="/static/mobiscroll/2.6.2/mobiscroll.core-2.6.2-zh.js" type="text/javascript"></script>
	<link href="/static/mobiscroll/2.6.2/mobiscroll.core-2.6.2.css" rel="stylesheet" type="text/css" />
	<link href="/static/mobiscroll/2.6.2/mobiscroll.animation-2.6.2.css" rel="stylesheet" type="text/css" />
	<script src="/static/mobiscroll/2.6.2/mobiscroll.datetime-2.6.2.js" type="text/javascript"></script>
	<script src="/static/mobiscroll/2.6.2/mobiscroll.list-2.6.2.js" type="text/javascript"></script>
	<script src="/static/mobiscroll/2.6.2/mobiscroll.list-2.6.2.js" type="text/javascript"></script>
	<script src="/static/mobiscroll/2.6.2/mobiscroll.select-2.6.2.js" type="text/javascript"></script>
	<script src="/static/mobiscroll/2.6.2/mobiscroll.android-2.6.2.js" type="text/javascript"></script>
	<link href="/static/mobiscroll/2.6.2/mobiscroll.android-2.6.2.css" rel="stylesheet" type="text/css" />
	<script src="/static/mobiscroll/2.6.2/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script>
	<link href="/static/mobiscroll/2.6.2/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" />
	<script src="/static/mobiscroll/2.6.2/mobiscroll.ios-2.6.2.js" type="text/javascript"></script>
	<link href="/static/mobiscroll/2.6.2/mobiscroll.ios-2.6.2.css" rel="stylesheet" type="text/css" />
	<script src="/static/mobiscroll/2.6.2/mobiscroll.jqm-2.6.2.js" type="text/javascript"></script>
	<link href="/static/mobiscroll/2.6.2/mobiscroll.jqm-2.6.2.css" rel="stylesheet" type="text/css" />
	<link href="/static/mobiscroll/2.6.2/mobiscroll.sense-ui-2.6.2.css" rel="stylesheet" type="text/css" />
	<script src="/static/mobiscroll/2.6.2/mobiscroll.wp-2.6.2.js" type="text/javascript"></script>
	<link href="/static/mobiscroll/2.6.2/mobiscroll.wp-2.6.2.css" rel="stylesheet" type="text/css" />
	<script src="/static/js/Base.js"></script>
<script>
$(function () {
	var opt = {
	        preset: 'time', //时间
	        theme: 'android-ics light', //皮肤样式
	        display: 'modal', //显示方式 
	        mode: 'scroller', //日期选择模式
	        dateFormat: 'HH:mm', // 日期格式
	        setText: '确定', //确认按钮名称
	        cancelText: '取消',//取消按钮名籍我
	        lang: 'zh'
	    };
	opt.time = { preset: 'time' }; 
	$("input.tiptime").mobiscroll(opt);
});
</script>
<script>
	//这个方法为最下面的按钮加的，根据情况自行修改，
	//用的是layer用法帮助文档http://layer.layui.com/mobile/
	var array=new Array();
	<%
		HfPrompt prompt=(HfPrompt)request.getAttribute("prompt");
		if(prompt!=null){
			String cron=prompt.getCronExpression();
			if(StringUtils.isNotBlank(cron)){
				String[] strs=cron.split("\\s{1,}",7);
				if(StringUtils.isNotBlank(strs[5])&&!"*".equals(strs[5])){
					String[] days=strs[5].split(",");
					for(int i=0;i<days.length;i++){
						out.println("array.push("+(Integer.valueOf(days[i])-1)+");");
					}
				}else if("*".equals(strs[5])){
					out.println("var oneTime=true;");
				}
			}
			out.print("var promptType="+prompt.getPromptType()+";");
		}
	%>
	function temFun1() {
		layer
				.open({
					content : '<div class="weekdayDiv"><div class="title">选择循环日期</div><ul class="txICO txICO_7s" style="height:4em;"><li><div><span>周一</span></div></li><li><div><span>周二</span></div></li><li><div><span>周三</span></div></li><li><div><span>周四</span></div></li><li><div><span>周五</span></div></li><li><div><span>周六</span></div></li><li><div><span>周日</span></div></li></ul><div class="openLayerSub"><div class="itemBlue">确定</div></div></div>',
				});
		$("ul.txICO_7s li div").each(function(){
			var index=$(this).index("ul.txICO_7s li div");
			var $t=this;
			$(this).bind("click",function(){
				$($t).toggleClass("over");
			})
		});
		for(var i=0;i<array.length;i++){
			$("ul.txICO_7s li:nth-child("+(array[i]+1)+") div").addClass("over");
		}
		$(".itemBlue").bind("click",function(){
			array=new Array();
			var html=new Array();
			$("ul.txICO_7s li div.over").each(function(){
				var index=$(this).index("ul.txICO_7s li div");
				array.push(index);
				switch(index){
				case 0:html.push("一");break;
				case 1:html.push("二");break;
				case 2:html.push("三");break;
				case 3:html.push("四");break;
				case 4:html.push("五");break;
				case 5:html.push("六");break;
				case 6:html.push("日");break;
				}
			})
			if(html.length>0){
				var $html="周"+html.join(",");
				$("#pickDate").html($html);
			}else{
				$("#pickDate").html();
			}
			layer.closeAll();
		});
	}
	$(function(){
		var _html=new Array();
		for(var i=0;i<array.length;i++){
			switch(array[i]){
			case 0:_html.push("一");break;
			case 1:_html.push("二");break;
			case 2:_html.push("三");break;
			case 3:_html.push("四");break;
			case 4:_html.push("五");break;
			case 5:_html.push("六");break;
			case 6:_html.push("日");break;
			}
		}
		if(_html.length>0){
			var $_html="周"+_html.join(",");
			console.log($_html);
			$("#pickDate").html($_html);
		}else{
			$("#pickDate").html();
		}
		if(typeof(promptType)!='undefined'){
			$("ul.tiptype li:nth-child("+promptType+") div").addClass("over");
		}
		if(typeof(oneTime)!='undefined'){
			$(".myRight.ico_k2").removeClass("ico_k2").addClass("ico_k1");
		}
		var timetest=/\d{1,2}\:\d{1,2}/;
		$(".tixingDivHeight .myRight").bind("click",function(){
			if($(this).hasClass("ico_k1")){
				$(this).removeClass("ico_k1");
				$(this).addClass("ico_k2");
			}else if($(this).hasClass("ico_k2")){
				$(this).removeClass("ico_k2");
				$(this).addClass("ico_k1");
			}
		})
		$(".tiptype li div").bind("click",function(){
			$(".tiptype li div").removeClass("over");
			$(this).addClass("over");
		});
		$(".mySubmit1.margin_top2").bind("click",function(){
			Base.open();
			var obj={};
			var time=$("input.tiptime").val();
			if(time.length>0){
				time=time.replace("：",":");
			}
			if(time.length==0){
				Base.close();
				Base.alert("请输入提醒时间");
				return false;
			}else if(!timetest.test(time)){
				Base.close();
				Base.alert("时间格式不正确");
				return false;
			}
			obj.time=time;
			if($(".tiptype li div.over").length==0){
				obj.type=0;
			}else{
				var type=$(".tiptype li div.over").parents("li").index(".tiptype li")+1;
				obj.type=type;
			}
			var content=$("#textarea").val();
			if(content.length==0){
				Base.close();
				Base.alert("请输入提醒内容");
				return false;
			}
			obj.content=content;
			if($(".tixingDivHeight .myRight.ico_k1").length>0){
				obj.repeat=0;
			}else{
				obj.repeat=2;
				obj.week=array;
			}
			var id='${prompt.id}';
			if(id!=null&&id!=''){
				obj.id=id;
			}
			$.post("/prompt/addPrompt",obj,function(d){
				Base.close();
				if(d.success==1){
					window.location.href="/prompt/promptList";
				}else{
					Base.alert("添加失败");
				}
			},"json");
		})
	})
</script>
<!--弹出层效果代码over-->
</head>
<body>
	<div class="container">
		<c:import url="../index/left_slide.jsp"></c:import>
		<!--button class="menu-button" id="open-button">打开</button-->
			<div class="content">
				<div class="divStyle5">
					<div class="clear">
						<div class="left1">提醒时间</div>
						<div class="right">
							<c:choose>
								<c:when test="${prompt.hour==null||prompt.minute==null}">
									<input type="text" placeholder="请输入时间" class="myInput2 tiptime">
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="请输入时间" class="myInput2 tiptime" value="${prompt.hour}:${prompt.minute}">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
				<div class="helpDiv">关爱内容:</div>
				<div class="tixingDiv">
					<ul class="txICO tiptype">
						<li><div>
								<span>睡觉</span>
							</div>
						</li>
						<li><div>
								<span>起床</span>
							</div>
						</li>
						<li><div>
								<span>活动</span>
							</div>
						</li>
						<li><div>
								<span>休息</span>
							</div>
						</li>
						<li><div>
								<span>吃药</span>
							</div>
						</li>
					</ul>
					<textarea name="textarea" rows="5" class="textareaStyle2"
						id="textarea">${prompt.content}</textarea>
				</div>
				<div class="tixingDiv">
					<div class="tixingDivHeight divIco1" onClick="temFun1()">
						<div class="myLeft">循环提醒:<span id="pickDate"></span></div>
					</div>
					<div class="tixingDivHeight">
						<div class="myLeft">一次提醒:</div>
						<div class="myRight ico_k2"></div>
					</div>
				</div>
				<div class="pagePadding">
					<div class="mySubmit1 margin_top2">保 存</div>
				</div>
				<br> <br> <br> <br> <br> <br> <br>
				<br>
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
		<!-- <script src="/static/js/main3.js"></script> -->
	</div>
	<!-- /container -->
<script type="text/javascript">
</script>
<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
</body>
</html>
