<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
<title>修改基本信息</title>
<jsp:include page="css.jsp"></jsp:include>
<link href = "/static/css/common.css" rel="stylesheet">
<script type="text/javascript" src="/static/js/ajaxfileupload.js"></script>
<script type="text/javascript" src="/static/js/avater.js"></script>
<script type="text/javascript" src="/static/js/layer.js"></script>
<script src="/static/js/classie.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/Constans.js"></script>
<script src="/static/js/Base.js"></script>
<script src="/static/js/main3.js"></script>
<script type="text/javascript">
function addContact(){
	$(".divStyle5.emergency-contact:first").clone().insertBefore("#form .addLXman");
	$(".divStyle5.emergency-contact:last input.emerge-name").val("");
	$(".divStyle5.emergency-contact:last input.emerge-phone").val("");
	deleteContact();
}
function deleteContact(){
	$(".div_set_right").off("click");
	$(".div_set_right").bind("click",function(){
		if($(".divStyle5.emergency-contact").length>1){
			$(this).parents(".emergency-contact").remove();
		}
	})
}
deleteContact();
</script>
<style type="text/css">
label.aa{color:red;font-size: 12px;}
</style>
</head>
<body>
<div class="container">
	<!--button class="menu-button" id="open-button">打开</button-->
	<!-- <div class="content" style="left:0"> -->
		<div class="content">
			<div class="manDiv margin_top2">
				<div class="manPic">
					<%-- <input type="hidden" value="${initParam.remoteImage}" id="remoteurl">
					<input type="file" accept="image/*" name="avater" id="avater" onchange="uploadPic()"  style="display: none;"> --%>
					<c:set var="rempic" value="${initParam.remoteImage}/${ob.avater }"></c:set>
					<img id="preview" src="${ob.avater==null||ob.avater==''?'/static/images/man.png':rempic }" style="height: 100px;width: 100px;">
				</div>
			</div>
			<form action="/index/updateInfo" method="post" id="form">
			<input type="hidden" value="${ob.id }" name="id" />
			<div class="divStyle5">
				<div class="clear bo1">
					<div class="left7em">被关注人昵称</div>
					<div class="inputLeft7em">
						<input type="text" value="${ob.nickName }" name="nickName" class="myInput2">
					</div>
				</div>
				<div class="clear bo1">
					<div class="left7em">被关注人姓名<span class="redStar">*</span></div>
					<div class="inputLeft7em">
						<input type="text" value="${ob.realName}" name="realName" class="myInput2">
					</div>
				</div>
				<div class="clear bo1">
					<div class="left3em">性别<!-- <span class="redStar">*</span> --></div>
					<div class="inputLeft3em">
						<select class="myInput2" style="" name="gender" value="${ob.gender}">
							<option value="">请选择</option>
							<option value="男" ${ob.gender=='男'?'selected':'' }>男</option>
							<option value="女" ${ob.gender=='女'?'selected':'' }>女</option>
						</select>
					</div>
				</div>
				<%-- <div class="clear bo1">
					<div class="left3em">年龄<!-- <span class="redStar">*</span> --></div>
					<div class="inputLeft3em">
						<input type="text" value="${ob.age }" name="age" class="myInput2">
					</div>
				</div> --%>
				<div class="clear bo1">
					<div class="left5em">身份证号<span class="redStar">*</span></div>
					<div class="inputLeft5em">
						<input type="text" value="${ob.card}" name="card" class="myInput2">
					</div>
				</div>
				<div class="clear bo1">
                    <div class="left5em">生&#12288;日<span class="redStar"></span></div>
                    <div class="inputLeft5em">
                    	<input type="text" id="birthday" name="birthday" value="${ob.birthday}" class="myInput" placeholder="请点击选择您的生日">
                    	</div>
                  </div>
				<div class="clear bo1">
					<div class="left5em">手机号码<span class="redStar">*</span></div>
					<div class="inputLeft5em">
						<input type="text" value="${ob.mobile }" name="mobile" class="myInput2">
					</div>
				</div>
				<div class="clear bo1">
					<div class="left" style="width: 20%;">地址</div>
					<div class="right" id="element_id" style="display: flex;flex-direction:row;align-items:center;width: 80%;">
						<select class="province myInput2" name="province" data-value="${ob.province }" style="display: inline;padding-right:10px;flex-grow:1;" data-first_title="选择省"></select>&nbsp;&nbsp;
						<select class="city myInput2" name="city" data-value="${ob.city}" data-first_title="选择市" style="display: inline;padding-right:10px;flex-grow:1"></select>&nbsp;&nbsp;
						<select class="area myInput2" name="area" data-value="${ob.area}" data-first_title="选择区" style="display: inline;padding-right:10px;flex-grow:1"></select>
					</div>
				</div>
				<div class="clear">
					<div class="left5em">详细地址</div>
					<div class="inputLeft5em">
						<input type="text" value="${ob.address}" name="address" class="myInput2">
					</div>
				</div>
			</div>
			<div class="divStyle5 emergency-contact">
						<div class="clear bo1">
							<div class="left0">
								紧急联系人<span class="redStar">*</span>
							</div>
							<div class="div_set_right"><img src="/static/images/ico_del.png"></div>
							<div class="inputLeft2">
								<input type="text" class="myInput2 emerge-name" style="" placeholder="非被关注人">
							</div>
						</div>
						<div class="clear">
							<div class="left0">
								紧急联系电话<span class="redStar">*</span>
							</div>
							<div class="inputLeft1">
								<input type="text" class="myInput2 emerge-phone">
							</div>
						</div>
					</div>
					<div class="addLXman">
						<a href="javascript:addContact()"> <strong style="font-size: 18px;">+</strong> 添加紧急联系人</a>
					</div>
			<input type="hidden" name="contactPerson"/>
			<input type="text" name="sleepTime" style="display: none;"/>
			<input name="stepCount" type="text" style="display: none">
			<input type="text" name="staticCount" style="display: none"/>
			<input type="text" name="dataType" style="display: none"/>
			<input type="hidden" name="day_healthTarget"/>
			<input type="hidden" name="week_healthTarget"/>
			<input type="hidden" name="month_healthTarget"/>
			<input type="hidden" name="ob"/>
			</form>
			<div class="divStyle6_2">
				<c:forEach items="${ob.deviceList }" var="de">
					<div class="item bo3 clear">
						<div class="div_set_left div_set_title">设备<i>（行为记录仪）</i></div>
						<div class="div_set_left3"><div>${de.code}</div></div>
						<div class="div_set_right" onclick="deleteDevice('${de.id}',this);" style="cursor: pointer;"><img src="/static/images/ico_del.png" ></div>
					</div>
				</c:forEach>
				<div class="item clear">
					<div class="div_set_left div_set_title" onclick="saoyisao();"><img src="/static/images/ico_addDevice.png"><br/><i>（扫一扫）</i></div>
					<div class="div_set_left3 div_set_add">
						<form id="form_add" name="form_add" method="post">
							<input type="hidden" value="${ob.id }" name="holderId" />
						  	<input type="text" class="myInput" name="code" id="decode" placeholder="请输入设备号">
						</form>
					</div>
					<div class="div_set_right" onclick="tijiao(this);" data-skip="1">
						<!-- <img src="/static/images/ico_ico7_hui.png" > -->
						<button style="background-color: rgb(204, 204, 204);">添加</button>
					</div>
				</div>
			</div>
			<div class="divStyle5" id="prompt">
				<div class="clear bo1">
					<ul class="pageMenuUL1 pageMenuUL1_2">
						<li class="alone w33 click">日健康目标</li>
						<li class="alone w33">周健康目标</li>
						<li class="alone w33">月健康目标</li>
					</ul>
				</div>
				<div class="clear bo1">
					<div class="left1">睡眠</div>
					<div class="right1"><input type="text" id="sT" onchange="changes(0)" value=0 style="border: none"/>小时</div>
				</div>
				<div class="clear bo1">
					<div class="left1">步数</div>
					<div class="right1"><input id="sC1" type="text"  onchange="changes(1)" value=0 style="border: none">&nbsp;&nbsp;&nbsp;&nbsp;步</div>
				</div>
				<div class="clear">
					<div class="left1">久坐</div>
					<div class="right1"><input type="text" id="sC2"  onchange="changes(2)" value=0 style="border: none"/>小时</div>
				</div>
			</div>
			<div class=" pad1">
				<div class="myLeft myWidth1"><div class="mySubmit4" style="cursor: pointer;" onclick="deleteUser('${ob.id }');">删除用户</div></div>
				<div class="myRight myWidth1"><div class="mySubmit1" style="cursor: pointer;" id="submit">保 存</div></div>
			</div>
		</div>
		<div id="datePlugin"></div>
	<!-- </div> -->
	<!-- /content-wrap -->
	 <%---公共部分 --%>

<script type="text/javascript" src="/static/js/voice/wechatJs.js"></script>	
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/static/js/voice/Math.uuid.js"></script>
<script type="text/javascript" src="/static/js/voice/Code.js"></script>
<script type="text/javascript" src="/static/js/main3.js"  charset="utf-8"></script>

<script type="text/javascript" src="/static/js/top.js" charset="utf-8"></script>
<link   type="text/css" href="/static/css/footer.css"  rel="stylesheet"/>
<script type="text/javascript" src="/static/js/select/jquery.cxselect.js"></script>
<script type="text/javascript" src="/static/js/index/update_user.js"></script> 
<script type="text/javascript" src="/static/js/date.js" ></script>
<script type="text/javascript" src="/static/js/iscroll.js" ></script>
<script type="text/javascript">
	$('#birthday').date();
	$(function(){
		$(".div_set_add form[name='form_add'] input[name='code']").keyup(function(){
			var value=$(this).val();
			var reg=/^\d{9}$/; 
			if(reg.test(value)){
				console.log(true);
				$(this).parents(".div_set_add").next().find("button").css('background-color','#FFAA25');
			}else{
				console.log(false);
				$(this).parents(".div_set_add").next().find("button").css('background-color','#ccc');
			}
		});
		var temp=${ob.contactPerson};
		console.log(temp);
		if(temp!=null){
			for(var i=1;i<temp.length;i++){
				$(".divStyle5.emergency-contact:first").clone().insertBefore("#form .addLXman");
			}
			var html=$("#form .emergency-contact");
			for(var i=0;i<temp.length;i++){
				$(html[i]).find(".emerge-name").val(temp[i].name)
				$(html[i]).find(".emerge-phone").val(temp[i].phone)
			}
			deleteContact();
		}
	});
	
	
	</script>
</div>
<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
</body>
</html>