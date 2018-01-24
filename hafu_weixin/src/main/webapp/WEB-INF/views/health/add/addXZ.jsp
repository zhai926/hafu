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
<title>添加血常规数据</title>
<!--左侧滑出样式-->
<jsp:include page="../../css.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/My97DatePicker/WdatePicker.js"></script>
<!--左侧滑出样式over-->
</head>

<body>
	<%Date date=new Date();
	request.setAttribute("today", date);%>
	<div class="container containerStyle1">
		<c:import url="../../index/left_slide.jsp"></c:import>
		<!--button class="menu-button" id="open-button">打开</button-->
		<div class="content-wrap">
			<div class="content">
				<div class="divStyle5">
					<div class="clear divIco1">
						<div class="left">日期</div>
						<div class="right">
							<input type="text" name="checkDate" onclick="WdatePicker({maxDate:'<fmt:formatDate value='${today }' type='date'/>'})" class="Wdate" style="height: 2.5em;line-height: 2.5em;border: none;"/>
						</div>
					</div>
				</div>
				<div class="divStyle5">
					<div class="clear bo1">
						<div class="left">总胆固醇</div>
						<div class="right">
							<input type="text" value="" class="myInput" name="zong">
						</div>
					</div>
					<div class="clear bo1">
						<div class="left">血氧</div>
						<div class="right">
							<input type="text" value="" class="myInput" name="ganyou">
						</div>
					</div>
					<div class="clear bo1">
						<div class="left">白细胞</div>
						<div class="right">
							<input type="text" value="" class="myInput" name="gaomidu">
						</div>
					</div>
					<div class="clear">
						<div class="left">血红蛋白</div>
						<div class="right">
							<input type="text" value="" class="myInput" name="dimidu">
						</div>
					</div>
				</div>
				<div class="divStyle5">
					<div class="left">检测地</div>
					<div class="right">
						<select name="checkAddress">
							<option value="养老馆">养老馆</option>
							<option value="医院">医院</option>
							<option value="家中">家中</option>
							<option value="其它">其它</option>
						</select>
					</div>
				</div>
				<div class="pagePadding">
					<div class="mySubmit1">确定</div>
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
	<script type="text/javascript">
		$(function(){
			$(".mySubmit1").bind("click",function(){
				Base.open();
				var date=$("input[name='checkDate']").val();
				var zong=$("input[name='zong']").val();
				var ganyou=$("input[name='ganyou']").val();
				var gaomidu=$("input[name='gaomidu']").val();
				var dimidu=$("input[name='dimidu']").val();
				var address=$("select[name='checkAddress']").val();
				if(date==null||date.length==0){
					Base.close();
					Base.alert("日期不能为空");
					return ;
				}else if(zong==null||zong.length==0){
					Base.close();
					Base.alert("总胆固醇不能为空");
					return ;
				}else if(isNaN(zong)){
					Base.close();
					Base.alert("总胆固醇格式不正确");
					return ;
				}else if(ganyou==null||ganyou.length==0){
					Base.close();
					Base.alert("血氧不能为空");
					return ;
				}else if(isNaN(ganyou)){
					Base.close();
					Base.alert("血氧格式不正确");
					return ;
				}else if(gaomidu==null||gaomidu.length==0){
					Base.close();
					Base.alert("白细胞不能为空");
					return ;
				}else if(isNaN(gaomidu)){
					Base.close();
					Base.alert("白细胞格式不正确");
					return ;
				}else if(dimidu==null||dimidu.length==0){
					Base.close();
					Base.alert("血红蛋白不能为空");
					return ;
				}else if(isNaN(dimidu)){
					Base.close();
					Base.alert("血红蛋白格式不正确");
					return ;
				}else if(address==null||address==0){
					Base.close();
					Base.alert("检测地不能为空");
					return ;
				}
				var data=zong+","+ganyou+","+gaomidu+","+dimidu;
				var d={"checkDate":date,"checkData":data,"checkAddress":address,"checkType":"JKSJ_XZ"};
				$.post("/health/addCheckData",d,function(result){
					if(result.success){
						Base.alert("保存成功");
						window.location="/health/healthData?type=2";
					}else{
						Base.alert("添加失败");
					}
				},"json");
			})
		})
	</script>
	<!-- /container -->
	<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
</body>
</html>
