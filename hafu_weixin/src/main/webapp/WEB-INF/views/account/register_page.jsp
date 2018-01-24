<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta id="viewport" name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
<link rel="stylesheet" href="/static/css/bootstrap.min.css">
<link rel="stylesheet"  style="text/css" href="/static/styles/base.css">
<script src="/static/js/jquery-1.10.2.min.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/static/js/register.js"></script>
<title>用户注册</title>
	
	<style type="text/css">
		body{background: url(/static/images/background_02.png); background-repeat: repeat-x;}
	</style>
</head>
<body>
	
	
	<div class="container" id="content">
		<div class="icon text-center" > <img src="/static/images/image.png" class="img-responsive center-block" alt="LOGO" style="height:auto;"/> </div>
	    <br><br><br><br><br>
	    <div class="text-center">
			<form role="form" class="form-horizontal" action="" method="post" name="f">
				<input type="hidden" value="${type }" name="type">
				<div class="form-group">
					<div class="col-xs-12">
						<input class="form-control" type="text" id="phone" name="mobilePhone" placeholder="手机号" value="">
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-12">
						<input class="form-control" type="password" id="password" name="meetingPassword" placeholder="密码（6-20个大小写英文字符或数字）" value="">
					</div>
				</div>
				<div class="form-group" style="display: none;">
					<div class="col-xs-6">
						<input class="form-control" name="code" placeholder="短信验证码" type="text" id="verify" value="123456">
					</div>
					<div class="col-xs-6">
						<input style="height: 34px;" class="btn  btn-primary btn_send_verify btn-sm btn-block"
							type="button" value="免费获取验证码" onclick="getCheckNum()" id="timeCount">
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-12">
						<input type="checkbox" name="" id="agree">&nbsp;&nbsp;&nbsp;<label for="agree">理财圈相关用户服务协议和隐私政策</label>
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-offset-2 col-xs-9">
						<button type="button" class="btn btn-primary btn-md btn-block" id="tijiao" onclick="doFreeRegister()">立即注册</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>