<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="myNavbar">
	<ul class="foot_menuUL">
	    <!--  2016/8/29 数据中心临时指向到昨日健康得分页面。
		<li><a href="/health/dataCompare" class="foot_ico1">数据中心</a></li>
		-->
		<li><a href="/health/healthPoint" class="foot_ico1">数据中心</a></li>
		<li><a href="/prompt/promptList" class="foot_ico2">关爱互动</a></li>
		<li class="foot_centerLogo"><a href="/index/index" class="foot_logo">福</a></li>
		<li><a href="http://www1.hafu365.com/index" class="foot_ico3">哈福服务</a></li>
		<!--  下面的按钮，弹出左侧菜单的，只要让id为下所示就可以了。 -->
		<li><a href="/index/device_user_list" class="foot_ico4" >我的设置</a></li>
	</ul>
</div>
<div class="yyDiv"><a href="/voice/voice"><img src="/static/images/ico_yuyin.png"></a><b id="voiceNum"></b></div>
<!-- <script src="/static/js/jquery.min.js"></script>
<link href="/static/css/css.css" rel="stylesheet"> -->
<script>
	$(function(){
		$.ajax({
			url:'/voice/getVoiceNum',
			type:"POST",
			dataType: "json",
			success:function(data){
				if(data.status=='success'){
					var num = data.ob;
					if(Number(num)>0){
						$("#voiceNum").html(data.ob);
					}else{
						$("#voiceNum").css("display","none");
					}
				}else{
					//alert(data.msg);
				}
			}
		});
	});


</script>
<input type="hidden" value="${param.param_menu }" id="param_menu">
<script type="text/javascript">
	//菜单
	$(function(){
		var param_menu = $("#param_menu").val();
		if(null!=param_menu && param_menu!=''){
			if(param_menu=='foot_logo'){
				
			}else{
				$("."+param_menu).parent().addClass("over");
			}
		}else{
			$(".foot_ico3").parent().addClass("over");
		}
	});
</script>