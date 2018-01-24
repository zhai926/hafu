<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
<title>用户基本信息</title>
<jsp:include page="css.jsp"></jsp:include>
</head>
<body>
<%String remoteImage=getServletContext().getInitParameter("remoteImage");
    	request.setAttribute("remoteImage", remoteImage);%>
<div class="container">
	<c:import url="index/left_slide.jsp"></c:import>
	<!--button class="menu-button" id="open-button">打开</button-->
	<div class="content-wrap">
		<form action="/index/saveInfo" method="post" id="form">
			<input type="hidden" value="${deviceCode }" name="code" />
			<div class="content">
				<!-- <div class="divStyle5">
					<div class="clear divIco1">
						<div class="left">日期</div>
						<div class="right">2016-05-05</div>
					</div>
				</div> -->
				<div class="divStyle5">
					<div class="clear bo1">
						<div class="left">姓名</div>
						<div class="right">
							<input type="text" value="${user.realName}" name="realName" class="myInput">
						</div>
					</div>
					<div class="clear bo1">
						<div class="left">性别</div>
						<div class="right">
							<%-- <input type="text" value="${user.gender }" name="gender" class="myInput"> --%>
							<select class="myInput" style="width:150px;" name="gender" value="${user.gender}">
								<option value="">请选择</option>
								<option value="男" ${user.gender=='男'?'selected':'' }>男</option>
								<option value="女" ${user.gender=='女'?'selected':'' }>女</option>
							</select>
						</div>
					</div>
					<div class="clear">
						<div class="left">年龄</div>
						<div class="right">
							<input type="text" value="${user.age }" name="age" class="myInput">
						</div>
					</div>
				</div>
				<div class="divStyle5">
					<div class="left">电话</div>
					<div class="right">
						<input type="text" value="${user.mobile }" name="mobile" class="myInput">
					</div>
				</div>
		
				<div class="divStyle5">
					<div class="clear divIco1">
						<div class="left">地址</div>
						<div class="right" id="element_id">
							<%-- <input type="text" value="${user.province }-${user.city}-${user.area}" name="user.province" class="myInput"> --%>
							<select class="province myInput" name="province" data-value="${user.province }" style="display: inline;width: auto;" data-first_title="选择省"></select>&nbsp;&nbsp;
							<select class="city myInput" name="city" data-value="${user.city}" data-first_title="选择省" style="display: inline;width: auto;"></select>&nbsp;&nbsp;
							<select class="area myInput" name="area" data-value="${user.area}" data-first_title="选择省" style="display: inline;width: auto;"></select>&nbsp;&nbsp;
						</div>
					</div>
				</div>
				<div class="pagePadding">
					<div class="mySubmit1" style="cursor: pointer;" id="submit">确定</div>
				</div>
			</div>
		</form>
	</div>
	<!-- /content-wrap -->
	<script src="/static/js/classie.js"></script>
	<script src="/static/js/jquery.min.js"></script>
	<script src="/static/js/bootstrap.min.js"></script>
	<script src="/static/js/Constans.js"></script>
	<script src="/static/js/Base.js"></script>
	<script type="text/javascript" src="/static/js/select/jquery.cxselect.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$('#element_id').cxSelect({
			url: '/static/js/select/cityData.json',               // 如果服务器不支持 .json 类型文件，请将文件改为 .js 文件
		  	selects: ['province', 'city', 'area'],  // 数组，请注意顺序
		  	emptyStyle: 'hidden'
		});	
		$("#submit").click(function(){
			/* var realName = $("input[name=realName]").val();
			if(realName || $.trim(realName)==''){
				Base.alert("请填写姓名!");
				return false;
			}
			if($.trim(realName).length>10){
				Base.alert("姓名长度10字以内");
				return false;
			}
			var age = $("input[name=age]").val();
			if(age && $.trim(age)!=null){
				if(/^(\+|-)?\d+($|\.\d+$)/.test(age)){
					var value=parseInt(age);
		            if(value<1&&value>150){
		            	Base.alert("请输入正确的年龄");
		                return false;
		            }
				}else{
					Base.alert("请输入正确的年龄");
					 return false;
				}
			}
			var mobile = $("input[name=mobile]").val();
			if(mobile || mobile==''){
				Base.alert("请输入电话");
				return false;
			}else{
				if(/^(\d{11})$/.test(value)){
					
				}else{
					Base.alert("请输入正确的电话");
					return false;
				}
			} */
			$.ajax({
				url:'/index/saveInfo',
				data:$('#form').serialize(),
				type:"POST",
				dataType: "json",
				success:function(data){
					if(data.status=='success'){
						Base.alert("保存成功");
						window.location.href="/index/index";
					}else{
						Base.alert(data.msg);
					}
				}
			});
		});
	});
</script>
</div>
</body>
</html>