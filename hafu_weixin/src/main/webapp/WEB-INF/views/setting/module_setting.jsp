<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
<title>首页显示</title>
<jsp:include page="../css.jsp"></jsp:include>
</head>
<body>
<div class="container containerStyle1">
	<div class="content-wrap">
		<div class="content">
			<div class="pagePadding">
				<ul class="pageMenuUL1">
					<li class="alone w33" onclick="window.location.href='/index/device_user_list'" style="cursor: pointer; width:50%;"">被关注人列表</li>
					<li class="alone w33" onclick="window.location.href='/index/device_list'" style="cursor: pointer; width:50%;"">设备列表</li>
					<!-- <li class="alone w33 click" onclick="window.location.href='/set/module_setting'">首页显示</li> -->
				</ul>
				<div class="margin_top1 tip_con">
                   	可在下方选择需要在首页上显示的数据，数据来源于不同的智能设备，请注意甑别。
                   </div>
				<!--此处的bg_left是左面选择小图标的设置，通过与click_bg_left切换来表示选中下方js-->
				<div class="divStyle1 margin_top1 clear bg_left" num="1" rel="INDEX_XWJL">
					<div class="index_talbeLeft height_middle">
						<div class="myICODiv"><img src="/static/images/ico_ico1.png"></div>
					</div>
					<div class="index_talbeRight index_talbeRight_border minH">
					 显示通过智能穿戴设备记录的日常行为及变化（行走、乘车、坐、卧等），并对异常行为（如摔倒等）
								    进行报警，可通过这些数据及时掌握被关注人的行为状态。
								    <br /><u><em>数据来源于福心智能穿戴设备</em></u>
					</div>
				</div>
				<div class="divStyle1 margin_top1 clear bg_left" num="1" rel="INDEX_YDGJ">
					<div class="index_talbeLeft height_middle">
						<div class="myICODiv"><img src="/static/images/ico_ico2.png"></div>
					</div>
					<div class="index_talbeRight index_talbeRight_border minH">
					显示通过智能穿戴设备记录的日常行动轨迹（如定位信息），可随时了解被关注人的所在位置。
								    <br /><u><em>数据来源于福心智能穿戴设备</em></u>
					</div>
				</div>
				<div class="divStyle1 margin_top1 clear bg_left" num="1" rel="INDEX_YDSJ">
					<div class="index_talbeLeft height_middle">
						<div class="myICODiv"><img src="/static/images/ico_ico3.png"></div>
					</div>
					<div class="index_talbeRight index_talbeRight_border minH">显示通过智能穿戴设备记录的日常行动轨迹（行走步数、活动次数），可随时了解被关注人的所在位置。
								  	<br /><u><em>数据来源于福心智能穿戴设备</em></u></div>
				</div>
				<div class="divStyle1 margin_top1 clear bg_left" num="1" rel="INDEX_TJSJ">
					<div class="index_talbeLeft height_middle">
						<div class="myICODiv"><img src="/static/images/ico_ico4.png"></div>
					</div>
					<div class="index_talbeRight index_talbeRight_border minH">显示通过健康一体机测量并记录的各项健康数据（血压、血糖、血常规、尿检），可通过数据及时了解被关注人的各项体检数据。
								  	<br /><u><em>数据来源于福心健康一体机</em></u></div>
				</div>
				<div class="divStyle1 margin_top1 clear bg_left" num="1" rel="INDEX_STSJ">
					<div class="index_talbeLeft height_middle">
						<div class="myICODiv"><img src="/static/images/ico_ico5.png"></div>
					</div>
					<div class="index_talbeRight index_talbeRight_border minH">显示通过健康手环和智能床垫实时记录的身体各项数据（心率、睡眠情况、摄入热量、消耗卡路里）。
								   <br /><u><em>数据来源于福心健康手环和福心智能床垫</em></u></div>
				</div>
				<div class="divStyle1 margin_top1 clear bg_left" num="1" rel="INDEX_HJSJ">
					<div class="index_talbeLeft height_middle">
						<div class="myICODiv"><img src="/static/images/ico_ico6.png"></div>
					</div>
					<div class="index_talbeRight index_talbeRight_border minH">显示通过智能穿戴设备所提供的位置的当地天气情况（温度、风力、空气质量）。
								   <br /><u><em>数据来源于福心智能穿戴设备</em></u></div>
				</div>
				<div class="pad1 margin_top2" >
					<div class="myLeft myWidth1"><div class="mySubmit4" onclick="huifu()">恢复默认选择</div></div>
					<div class="myRight myWidth1"><div class="mySubmit1" onclick="tijiao(this)">确定</div></div>
				</div>
			</div>
		</div>
	</div>
	<script src="/static/js/Base.js"></script>
	<%---公共部分 --%>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/static/js/main3.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/voice/Math.uuid.js"></script>
<script type="text/javascript" src="/static/js/voice/Code.js"></script>
<script type="text/javascript" src="/static/js/voice/wechatJs.js"></script>	
   <link rel="stylesheet" type="text/css" href="/static/css/footer.css" />
   <script src="/static/js/top.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
		var fl = false;
		function tijiao(e){
			if(fl) return;
			var sel = $("div[rel^='INDEX_'][num=0]");
			if(sel.length==0){
				Base.alert("请至少选中一个");
				return;
			}
			var arr = [];
			sel.each(function(){
				arr.push($(this).attr("rel"));
			});
			fl=true;
			$(e).css("background","gray");
			$.getJSON("/set/save_module",{"module":arr},function(data){
				if(data.status=='error'){
					Base.alert("请至少选中一个");
				}else{
					Base.alert("设置成功!");
				}
				fl = false;
				$(e).css("background","#FFA647");
			});
		};
		$(function(){
			$(".divStyle1").click(function(){
				var Num=$(this).attr('num');
				if(Num==1){
					$(this).removeClass('bg_left').addClass('click_bg_left');
					$(this).attr('num',0);
				}else{
					$(this).addClass('bg_left').removeClass('click_bg_left');
					$(this).attr('num',1);
				}
			});
			//显示
			var setting = ${setting};
			if(setting && setting!=null && setting!=''){
				for(var i in setting){
					var item = Base.module_setting(setting[i].moduleCode);
					if(item && item!=null){
						$("div[rel="+item+"]").removeClass('bg_left').addClass('click_bg_left');
						$("div[rel="+item+"]").attr('num',0);
					}
				}
			}
		});
		function huifu(){
			Base.confirm({
				msg:"确认恢复默认显示吗？",
				yes:function(){
					$("div[rel^='INDEX_']").addClass('bg_left').removeClass('click_bg_left');;
					$("div[rel^='INDEX_']").attr('num',1);
					$("div[rel^='INDEX_']:not([rel=INDEX_TJSJ],[rel=INDEX_STSJ])").removeClass('bg_left').addClass('click_bg_left');;
					$("div[rel^='INDEX_']:not([rel=INDEX_TJSJ],[rel=INDEX_STSJ])").attr('num',0);
				}
			});
		}
	</script>
</div>
<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
</body>
</html>