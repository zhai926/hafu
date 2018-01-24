<%@page import="java.util.Date"%>
<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
<title>鼓励一下</title>
<c:import url="../css.jsp"></c:import>
<!--注意这里调用的css不同-->
<link href="/static/css/layer_guli.css" rel="stylesheet" type="text/css">
</head>

<body>
	<div class="container">
		<c:import url="../index/left_slide.jsp"></c:import>
		<!--button class="menu-button" id="open-button">打开</button-->
			<div class="content">
				<div class="fixMarTop"></div>
				<div class="pagePadding fixSubMenu">
					<!--  菜单的click样式，是表示，当前点击的按钮，加上click样式 背景变蓝色，字变白色  -->
					<ul class="pageMenuUL1">
						<li class="alone w33" onclick="window.location.href='/prompt/promptList'" style="width:50%;">关爱提醒</li>
						<!-- <li class="alone w33 click" onclick="window.location.href='/prompt/encourageList'">鼓励一下</li> -->
						<li class="alone w33" onclick="window.location.href='/voice/voice'" style="width:50%;">语音互动</li>
					</ul>
					</div>
					<ul class="baojingUL" style="padding:1px 0">
						<c:forEach items="${list}" var="encourage">
							<li>${encourage.sendUser.nickName}<br><fmt:formatDate value="${encourage.sendDate}" pattern="yyyy年MM月dd日 HH:mm:ss"/> <br>
								${encourage.sendUser.nickName}
								<c:choose>
									<c:when test="${encourage.encouragementType==1}">为你热烈鼓掌</c:when>
									<c:when test="${encourage.encouragementType==2}">为你送上一束献花</c:when>
									<c:when test="${encourage.encouragementType==3}">给你一个拥抱</c:when>
								</c:choose>
							</li>
						</c:forEach>
					</ul>
					<div style="width: 100%;margin-top:10px;">
						<div class="mySubmit1" style="margin-left: 2%;margin-right: 2%;" onclick="temFun1()">鼓励一下</div>
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
	<script src="/static/js/layer.js"></script>

	<script type="text/javascript">
function temFun1(){
    layer.open({
    content: '<div class="guliDiv"><div class="myWidth33"><div class="myGuliDiv"><div class="pic ico_guli1"></div><div class="tt">今天表现真棒，您的孩子为您热情鼓掌。</div></div></div><div class="myWidth33">  	<div class="myGuliDiv"><div class="pic ico_guli2"></div><div class="tt">您的孩子为您送上一束献花。</div></div></div><div class="myWidth33">  	<div class="myGuliDiv"><div class="pic ico_guli3"></div><div class="tt">您的孩子给您一个拥抱。</div></div></div></div>',
});
    $(".myGuliDiv").bind("click",function(){
    	var index=$(this).parent().index(".guliDiv .myWidth33")+1;
    	$.post("/prompt/addEncourage",{"encouragementType":index},function(){
    		
    	},"json");
    	layer.closeAll();
    })
}
</script>
<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
</body>
</html>
