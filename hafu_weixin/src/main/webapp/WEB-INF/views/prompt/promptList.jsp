<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;"
	name="viewport" />
<title>关爱提醒</title>
<c:import url="../css.jsp"></c:import>
<style type="text/css">
.delete {
	/* background: url(/static/images/ico_f1.png) no-repeat;
	height: 2.4em;
	width: 2em;
	margin-left: 20px;
	background-size: 1.5em; */
	background: url(/static/images/ico_f1.png) no-repeat 1em center;
    background-size: 1.5em;
    width: 3em;
    height: 100%;
    
}
.tixingUL li>div:nth-child(1){
    width: 3em;
    height: 100%;
    float: left;
}
.tixingUL li>div:nth-child(2) {
    width: calc(100% - 3em);
    float: right;
}
.tixingUL li b input[type=text] {
	background: none;
}
</style>
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
						<li class="alone w33 click"
							onclick="window.location.href='/prompt/promptList'" style="width:50%;" >关爱提醒</li>
						<!-- <li class="alone w33"
							onclick="window.location.href='/prompt/encourageList'">鼓励一下</li>  -->
						<li class="alone w33"
							onclick="window.location.href='/voice/voice'" style="width:50%;">语音互动</li>
					</ul>
				</div>
				<ul class="tixingUL" style="border-top:none;">
					<c:forEach items="${list}" var="prompt">
						<c:set value="${prompt.cronExpression}" var="cron" scope="request"></c:set>
						<li>
							<div style="height: 60px;">
								<i class="delete" data-id="${prompt.id}">
								</i>
							</div>
							<div>
								<b onclick="javascript:window.location.href='/prompt/toAddPrompt?id=${prompt.id}'">
									${prompt.hour>=10?'':'0'}${prompt.hour}:${prompt.minute>=10?'':'0'}${prompt.minute}
								</b>
									<i style="width: 50px;float: none;"> <c:choose>
											<c:when test="${prompt.promptType==1}">睡觉</c:when>
											<c:when test="${prompt.promptType==2}">起床</c:when>
											<c:when test="${prompt.promptType==3}">活动</c:when>
											<c:when test="${prompt.promptType==4}">休息</c:when>
											<c:when test="${prompt.promptType==5}">吃药</c:when>
										</c:choose>
								</i>
								<c:choose>
									<c:when test="${prompt.isPrompt==1}">
										<span class="ico_k2" data-id='${prompt.id }'></span>
									</c:when>
									<c:otherwise>
										<span class="ico_k1" data-id='${prompt.id}'></span>
									</c:otherwise>
								</c:choose>
								<div class="tixing-con" onclick="javascript:window.location.href='/prompt/toAddPrompt?id=${prompt.id}'">${prompt.content}</div>
							</div>
						</li>
					</c:forEach>
					<li class="ico_f2"
						onclick="javascript:window.location.href='/prompt/toAddPrompt'"
						style="padding-left: 3em !important;">每日关爱提醒最多可发送10条</li>
				</ul>
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
	<script type="text/javascript">
		$(function() {
			$(".delete").bind("click", function() {
				var id = $(this).attr("data-id");
				$.post("/prompt/delePrompt?id=" + id, function(d) {
					if (d.success == 1 || d.success == "1") {
						window.location.href = "/prompt/promptList";
					}
				}, "json");
			})

			$(".ico_k1,.ico_k2").bind("click", function() {
				var repeat = 0;
				if ($(this).hasClass("ico_k1")) {
					$(this).removeClass("ico_k1");
					$(this).addClass("ico_k2");
					repeat = 1;
				} else {
					$(this).removeClass("ico_k2");
					$(this).addClass("ico_k1");
				}
				var obj = {};
				obj.id = $(this).attr("data-id");
				obj.repeat = repeat;
				$.post("/prompt/editPrompt", obj, function(d) {
					if (d.success == 1 || d.success == "1") {
						//confirm("修改成功");
					}
				}, "json");
			})
		})
	</script>
	<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
</body>
</html>
