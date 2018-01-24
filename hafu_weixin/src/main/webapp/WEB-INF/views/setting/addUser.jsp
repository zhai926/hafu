<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
<title>添加被关注人</title>
<script type="text/javascript"
	src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<jsp:include page="../css.jsp"></jsp:include>
<link href = "/static/css/common.css" rel="stylesheet">
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
</head>
<body>
	<div class="container containerStyle1">
		<!--button class="menu-button" id="open-button">打开</button-->
		<div class="content-wrap" style="padding-bottom:0">
			<form action="/index/saveInfo" method="post" id="form">
				<div class="content">
					<div class="divStyle5">
						<div class="clear bo1">
							<div class="left7em">
								被关注人昵称<span class="redStar"></span>
							</div>
							<div class="inputLeft7em">
								<input type="text" name="nickName" class="myInput2" value="${user.nickName}">
							</div>
						</div>
						<div class="clear bo1">
							<div class="left7em">
								被关注人姓名<span class="redStar">*</span>
							</div>
							<div class="inputLeft7em">
								<input type="text" name="realName" value="${user.realName}" class="myInput2">
							</div>
						</div>
						<div class="clear bo1">
							<div class="left3em">
								性别<span class="redStar"></span>
							</div>
							<div class="inputLeft3em">
								<select class="myInput2" style="" name="gender" value="${user.gender}">
								<option value="">请选择</option>
								<option value="男" ${user.gender=='男'?'selected':'' }>男</option>
								<option value="女" ${user.gender=='女'?'selected':'' }>女</option>
							</select>
							</div>
						</div>
						<%-- <div class="clear bo1">
							<div class="left3em">
								年龄<span class="redStar"></span>
							</div>
							<div class="inputLeft3em">
								<input type="text" name="age" class="myInput2" value="${user.age}">
							</div>
						</div> --%>
						<div class="clear bo1">
							<div class="left5em">
								身份证号<span class="redStar">*</span>
							</div>
							<div class="inputLeft5em">
								<input type="text" name="card" class="myInput2" value="${user.card}">
							</div>
						</div>
						
						<div class="clear bo1">
                   			 <div class="left5em">生&#12288;日<span class="redStar"></span></div>
                   			 <div class="inputLeft5em">
                    				<input type="text" id="birthday" name="birthday" value="${ob.birthday}" class="myInput" placeholder="请点击选择您的生日">
                    		 </div>
                 		 </div>
						
						<div class="clear bo1">
							<div class="left5em">
								手机号码<span class="redStar">*</span>
							</div>
							<div class="inputLeft5em">
								<input type="text" name="mobile" class="myInput2" value="${user.mobile}">
							</div>
						</div>
						<div class="clear bo1">
							<div class="left" style="width: 20%;">地址</div>
							<div class="right" id="element_id" style="display: inline;width: 80%;">
								<select class="province myInput" name="province" data-value="${user.province }" style="display: inline;width: 33%;" data-first_title="选择省"></select>&nbsp;&nbsp;
								<select class="city myInput" name="city" data-value="${user.city}" data-first_title="选择省" style="display: inline;width: 33%;"></select>&nbsp;&nbsp;
								<select class="area myInput" name="area" data-value="${user.area}" data-first_title="选择省" style="display: inline;width: 20%;"></select>
							</div>
						</div>
						<div class="clear">
							<div class="left5em">详细地址</div>
							<div class="inputLeft5em">
								<input type="text" name="address" class="myInput2" value="${user.address}">
							</div>
						</div>
					</div>
					<input type="hidden" name="contactPerson"/>
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
						<div class="clear bo1">
							<div class="left0">
								紧急联系电话<span class="redStar">*</span>
							</div>
							<div class="inputLeft1">
								<input type="text" class="myInput2 emerge-phone">
							</div>
						</div>
						 <div class="clear">
						   <div class="left0">关&nbsp;&nbsp;&nbsp;&nbsp;系<span class="redStar">*</span></div>
	                         <div class="inputLeft5em">
	                            <select name="relations" class="relations">
	                          </select>
	                         </div>
						 </div>
					</div>
					<div class="addLXman">
						<a href="javascript:addContact()"> <strong style="font-size: 18px;">+</strong> 添加紧急联系人</a>
					</div>
					<div class="pagePadding">
						<div class="mySubmit1" style="cursor: pointer;" id="submit">确定</div>
					</div>
				</div>
			</form>
			<div id="datePlugin"></div>
		</div>
		
		<!-- /content-wrap -->
		<script src="/static/js/classie.js"></script>
		<script src="/static/js/jquery.min.js"></script>
		<script src="/static/js/bootstrap.min.js"></script>
		<script src="/static/js/Constans.js"></script>
		<script src="/static/js/Base.js"></script>
		<script type="text/javascript" src="/static/js/select/jquery.cxselect.js"></script>
		<script type="text/javascript" src="/static/js/date.js" ></script>
		<script type="text/javascript" src="/static/js/iscroll.js" ></script>
		<script type="text/javascript">
		$('#birthday').date();
		$("input[name=card]").blur(function(){
			var card = $("input[name=card]").val();
			var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/; 
			if(card!=null && card.length != 0){
				if(reg.test(card)){
					var year;
					var month;
					var day;
					if(card.length == 18){
						year = card.substr(6,4);
						month = card.substr(10,2);
						day = card.substr(12,2);
					}
					if(card.length == 15){
						year = 19 + card.substr(6,2);
						month = card.substr(8,2);
						day = card.substr(10,2);
					}
					$("#birthday").val(year + '-' + month + '-' + day);
				}
			}
		});
		    var options="";
			$(document).ready(function() {
				$('#element_id').cxSelect({
					url : '/static/js/select/cityData.json', // 如果服务器不支持 .json 类型文件，请将文件改为 .js 文件
					selects : [ 'province', 'city', 'area' ], // 数组，请注意顺序
					emptyStyle : 'hidden'
				});
				
				//添加关系
				var relations = ['爸爸','妈妈','爷爷','奶奶','外公','外婆','其他'];
				
				for(var m=0;m<relations.length;m++){
					var options = "<option value='"+relations[m]+"'>"+relations[m]+"</option>" ;
					$('.relations').append(options);
				}
				//当用户选择其他关系的时候出现一个输入框
				$('.relations').on('change',function(){
					   if($(this).val()=='其他'){
					   	var $input=$('<input type="text" class="otherRelation" />');
					   	$(this).parent().append($input);
					   }else{
					   	$(this).parent().find('input').remove();
					   }
				});
				var fl = false;
				$("#submit").click(function() {
					if(fl) return ;
					Base.open();
					var realName = $("input[name=realName]").val();
					if (realName == null || $.trim(realName) == '') {
						Base.close();
						Base.alert("请填写姓名!");
						return false;
					}
					if ($.trim(realName).length > 10) {
						Base.close();
						Base.alert("姓名长度10字以内");
						return false;
					}
					var age = $("input[name=age]").val();
					if (age && $.trim(age) != null) {
						if (/^(\+|-)?\d+($|\.\d+$)/.test(age)) {
							var value = parseInt(age);
							if (value<1&&value>150) {
								Base.close();
								Base.alert("请输入正确的年龄");
								return false;
							}
						} else {
							Base.close();
							Base.alert("请输入正确的年龄");
							return false;
						}
					}
					var card = $("input[name=card]").val();
					if (card == null || card == '') {
						Base.close();
						Base.alert("请输入身份证号");
						return false;
					}
					var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
					if (reg.test(card) === false) {
						Base.close();
						Base.alert("身份证号输入不合法");
						return false;
					}
					var mobile = $("input[name=mobile]").val();
					if (mobile == null || mobile == '') {
						Base.close();
						Base.alert("请输入电话");
						return false;
					} else {
						if (/^(\d{11})$/.test(mobile)) {

						} else {
							Base.close();
							Base.alert("请输入正确的电话");
							return false;
						}
					}
					console.log($(".divStyle5.emergency-contact").length)
					var array=new Array();
					$(".divStyle5.emergency-contact").each(function(){
						var name=$(this).find(".emerge-name").val();
						var phone=$(this).find(".emerge-phone").val();
						if(name!=null&&name.trim().length>0){
							if(name.trim().length>10){
								Base.close();
								Base.alert("紧急联系人长度不能超过10个字！")
								return false;
							}
						}else{
							Base.close();
							Base.alert("紧急联系人不能为空！")
							return false;
						}
						if(phone!=null&&phone.trim().length>0){
							if (/^(1(3|4|5|7|8|9)\d{9})$/.test(phone)) {
								
							} else {
								Base.close();
								Base.alert("请输入正确的电话");
								return false;
							}
						}else{
							Base.close();
							Base.alert("紧急联系电话不能为空！")
							return false;
						}
						array.push({"name":name,"phone":phone});
					})
					if(array.length==0){
						return false;
					}else{
						var temp=JSON.stringify(array);
						$("input[name='contactPerson']").val(temp);
					}
					fl =true;
					$(this).css("background","gray");
					$.ajax({
						url : '/register/saveInfo',
						data : $('#form').serialize(),
						type : "POST",
						dataType : "json",
						success : function(data) {
							Base.close();
							if (data.status == 'success') {
								Base.alert("保存成功");
								window.location.href = "/index/device_user_list";
							} else {
								Base.alert(data.msg);
							}
							fl = false;
							$("#submit").css("background","#FFA647");
						}
					});
				});
			});
			
			
		</script>
	</div>
</body>
</html>