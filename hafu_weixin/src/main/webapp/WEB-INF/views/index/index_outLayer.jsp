<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 头像点击content -->
<!-- 报警头像点击 -->
<div id="outLayer1_al" style="display: none;" >
	<div class="outLayer1" >
		<div class="divStyle6">
			<div class="item bo3">
				<input type="hidden" value="" id="alter_id">
				<div class="TextCenter">${empty sessionScope.CurrentUser.nickName?sessionScope.CurrentUser.realName:sessionScope.CurrentUser.nickName }报警</div>
			</div>
			<div class="item bo3 clear">
				<div class="div_set_title2">日期</div>
				<div class="div_set_left3">
					<div id="alter_day">2016年5月4日</div>
				</div>
			</div>
			<div class="item bo3 clear">
				<div class="div_set_title2">时间</div>
				<div class="div_set_left3">
					<div id="alter_time">12:30:02</div>
				</div>
			</div>
			<div class="item bo3 clear">
				<div class="div_set_title2">动作</div>
				<div class="div_set_left3">
					<div id="alter_type">手动报警</div>
				</div>
			</div>
			<div class="item bo3 clear">
				<div class="div_set_title2">位置</div>
				<div class="div_set_left3">
					<div id="alter_position">无</div>
				</div>
			</div>
			<div class="item bo3 clear">
				<div class="div_set_title2">电话</div>
				<div class="div_set_left3">
					<div id="alter_mobile"></div>
				</div>
			</div>
			<div class="item clear">
				<!-- <div class="div_set_title2">已知晓！</div>
				<div class="div_set_left3">
					<div id="alter_tianqi"></div>
				</div> -->
				<div class="divStyle6_1">
					<div class="itemBlue" onclick="read()" style="cursor: pointer;">确认知晓</div>
				</div>
			</div>
		</div>
		<div class="divStyle6_1">
			<div class="itemBlue" onclick="window.location.href='/alter/user_alter_list'" style="cursor: pointer;">查看过往报警记录</div>
		</div>
	</div>
</div>
<!-- 正常点击头像 -->
<div id="outLayer1" style="display: none;" >
	<div class="outLayer1">
		<div class="divStyle6">
			<div class="item bo3" style="height:4em">
				<!-- <div class="ico_modify" onclick="window.location.href='toUpdateHolder/${sessionScope.CurrentUser.id}'" style="cursor: pointer;"> -->
				<div class="ico_modify" style="cursor: pointer;">
					<div class="div_set_left">
						<div class="div_set_img">
							<!-- <img src="/static/images/man.jpg"> -->
							<c:set var="rempic" value="${initParam.remoteImage}/${sessionScope.CurrentUser.avater }"></c:set>
							<img src="${sessionScope.CurrentUser.avater==null || sessionScope.CurrentUser.avater== ''?'/static/images/man.png':rempic }" style="width: 56px;height: 56px;">
						</div>
					</div>
					<div class="div_set_left2">
						<div>
							<span class="div_set_name">${sessionScope.hfConcernUser.nickName==null || sessionScope.hfConcernUser.nickName==""?sessionScope.CurrentUser.realName:sessionScope.hfConcernUser.nickName }</span><span class="div_set_tel" hidden>13789647425</span>
						</div>
						<div class="div_set_other">${sessionScope.CurrentUser.gender} ${sessionScope.CurrentUser.age==null?"/":sessionScope.CurrentUser.age}岁   ${sessionScope.CurrentUser.province} ${sessionScope.CurrentUser.city} ${sessionScope.CurrentUser.area }</div>
					</div>
					<a href="/index/toUpdateHolder/${sessionScope.CurrentUser.id }"><div class="clearDiv"></div></a>
					<!-- <div class="clearDiv"></div> -->
				</div>
			</div>
			<div class="item bo3 clear">
				<div class="div_set_title2">电话</div>
				<div class="div_set_left3">
					<div>${sessionScope.CurrentUser.mobile }</div>
				</div>
			</div>
			<div class="item clear">
				<div class="div_set_title2">设备号</div>
				<div class="div_set_left3">
					<div>${sessionScope.CurrentUser.code }</div>
				</div>
			</div>
		</div>
		<div class="divStyle6_1">
			<div class="itemBlue" onclick="window.location.href='/alter/user_alter_list'" style="cursor: pointer;">查看过往报警记录</div>
		</div>
		<div class="divStyle6_1">
			<div class="itemBlue bo3" onclick="avater_paizhao(1);" style="cursor:pointer; ">拍照</div>
			<div class="itemBlue" onclick="avater_paizhao(2);" style="cursor:pointer; ">从相册选择</div>
		</div>
		<div class="divStyle6_1">
			<div class="itemBlue" onClick="layer.closeAll()">取消</div>
		</div>
	</div>
</div>
<input type="hidden" value="${initParam.remoteImage}" id="remoteurl">
<!-- 头像点击content end -->
<script src="/static/js/Base.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
//拍照
function avater_paizhao(type){
	layer.closeAll();
	wx.chooseImage({
	    count: 1, // 默认9
	    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
	    sourceType: type==1?['camera']:['album'], // 可以指定来源是相册还是相机，默认二者都有
	    success: function (res) {
	        var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
	        wx.uploadImage({
	    	    localId: localIds.toString(), // 需要上传的图片的本地ID，由chooseImage接口获得
	    	    isShowProgressTips: 1, // 默认为1，显示进度提示
	    	    success: function (res) {
	    	        var serverId = res.serverId; // 返回图片的服务器端ID
	    	        Base.open();
	    	        $.ajax({//异步下载图片
	    				url:'/set/ajaxUpload',
	    				data:{"serverId":serverId},
	    				type:"POST",
	    				dataType: "json",
	    				success:function(data){
	    					Base.close();
	    					
	    					if(data.status=='success'){
	    						$(".div_set_img img").attr("src",$("#remoteurl").val()+"/"+data.msg+"?"+Math.random());
	    						//$("#preview").attr("src",$("#remoteurl").val()+"/"+data.msg+"?"+Math.random());
	    						window.location.reload();
	    					}else{
	    						Base.alert(data.msg);
	    					}
	    				}
	    			});
	    	    }
	    	});
	    }
	});
}
wx.ready(function(){
	
});
wx.error(function(res){
	//alert("无法扫描");
});
</script>