<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
<title>报警记录</title>
<jsp:include page="css.jsp"></jsp:include>
</head>
<body>
<div class="container">
	<c:import url="index/left_slide.jsp"></c:import>
	<!--button class="menu-button" id="open-button">打开</button-->
		<div class="content">
			<div class="pagePadding">
				<ul class="baojingUL">
					<c:forEach items="${list }" var="alert">
						<li>${empty sessionScope.CurrentUser.nickName?sessionScope.CurrentUser.realName:sessionScope.CurrentUser.nickName} 报警<br><fmt:formatDate value="${alert.alertDate}" pattern="yyyy-MM-dd HH:mm"/> <br>
							${alert.position }，发生<span id="alertType">${alert.alertType==1?"自动报警":"手动报警"}</span>，请关注！
						</li>
					</c:forEach>
				</ul>
				<input type="hidden" id="listNum" value="${listNum}">
				
			</div>
			
		</div>
		<span  style="position:absolute; left:0; text-align:center;font-size:16px; width:100%; top:40%; " id="default"></span>
	
	<!-- /content-wrap -->
	 <%---公共部分 --%>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/static/js/main3.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/voice/Math.uuid.js"></script>
<script type="text/javascript" src="/static/js/voice/Code.js"></script>
<script type="text/javascript" src="/static/js/voice/wechatJs.js"></script>		 
<script src="/static/js/Constans.js"></script>
<script src="/static/js/Base.js"></script>
<link rel="stylesheet" type="text/css" href="/static/css/footer.css" />
<script src="/static/js/top.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
		$(function(){
			var num = $('.pagePadding input').val();
			if(num == 0 || num == null){
				//alert("进入判断");
				$('#default').html('暂无记录');
			}
			/* var v = $("#alertType").attr("data-value");
			if(v != null || v!=''){
				alert("1**"+Constans.alertType[1]);
				$("#alertType").text(Constans.alertType[v]);
			}else{
				$("#alertType").text("其他方式报警");
			} */
			
		});
	</script>
</div>
<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
</body>
</html>