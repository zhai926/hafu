<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
<title>设备列表</title>
<jsp:include page="../css.jsp"></jsp:include>
<%String remoteImage=getServletContext().getInitParameter("remoteImage");
    	request.setAttribute("remoteImage", remoteImage);%>
<script src="/static/js/Base.js"></script>
</head>
<body>
<div class="container containerStyle1">
<%-- <c:import url="left_slide.jsp"></c:import> --%>
<!--button class="menu-button" id="open-button">打开</button-->

	<div class="content">
		<div class="pagePadding">
			<!--  菜单的click样式，是表示，当前点击的按钮，加上click样式 背景变蓝色，字变白色  -->
			<ul class="pageMenuUL1">
				<li class="alone w33" onclick="window.location.href='/index/device_user_list'" style="cursor: pointer; width:50%;">被关注人列表</li>
				<li class="alone w33 click" onclick="window.location.href='/index/device_list'" style="cursor: pointer; width:50%;">设备列表</li>
				<!-- <li class="alone w33" onclick="window.location.href='/set/module_setting'">首页显示</li> -->
			</ul>
			<c:forEach items="${list}" var="de">
				<div class="divStyle6_1">
					<div class="item bo3">
						<div class="clear">
							<div class="div_set_title2">行为记录仪</div>
							<div class="div_set_left3">
								<div>${de.code }</div>
							</div>
							<div class="div_set_right" onclick="deleteDevice('${de.deviceId}',this);" style="cursor: pointer;"><img src="/static/images/ico_del.png"></div>
						</div>
					</div>
					<div class="item">
						<div class="clear">
							<div class="div_set_title2">
								<div class="div_set_img2">
									<c:set var="rempic" value="${initParam.remoteImage}/${de.avater }"></c:set>
									<img src="${de.avater==null || de.avater==''?'/static/images/man.png':rempic }" style="width: 40px;height: 40px;">
								</div>
							</div>
							<div class="div_set_left3">
								<div>${empty de.nickName?de.realName:de.nickName } ${de.mobile}</div>
							</div>
							<div class="div_set_right" onclick="window.location.href='/index/toUpdateHolder/${de.id}'" style="cursor: pointer;"><img src="/static/images/ico_modify.png"></div>
							<!-- <a href="toUpdateHolder"><div class="div_set_right" style="cursor: pointer;"><img src="/static/images/ico_modify.png"></div></a> -->
						</div>
					</div>
				</div>
			</c:forEach>
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
<script type="text/javascript">

//删除设备
function deleteDevice(id,e){
	var fl = Base.confirm({
		msg:"确认删除此设备？",
		yes:function(){
			Base.open();
			$.ajax({
				url:'/index/deleteDevice',
				data:{"deviceId":id},
				type:"POST",
				dataType: "text",
				success:function(data){
					Base.close();
					if(data=='success'){
						Base.alert({
							msg:"删除成功",
						});
						$(e).parent().parent().parent(".divStyle6_1").remove();
					}else{
						Base.alert("删除失败");
					}
				}
			});
		}	
	});
}
</script>
<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
</body>
</html>