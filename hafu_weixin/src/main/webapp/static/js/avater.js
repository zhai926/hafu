function uploadPic(){
	$.ajaxFileUpload({
		url:"/set/avaterUpload",
		fileElementId:"avater",
		secureuri: false,
		dataType:"text",
		success:function(data){
			if(data=="noLogin"){
				var url = window.location.href;
				login_then_return(url);
			}else if(data =="noImg"){
				Base.alert("图片不能为空");
				return;
			}else if(data=="typeError"){
				Base.alert("类型错误");
				return;
			}else if(data=="tooLarge"){
				Base.alert("图片太大(不能超过200k)");
				return;
			}else if(data=="error"){
				Base.alert("上传失败");
				return;
			}else{
				$("#preview").attr("src",$("#remoteurl").val()+"/"+data+"?"+Math.random());
				return;
			}
		},error:function(){
			Base.alert("上传失败");
		}
	});
}
function avater_paizhao(type){
	var sourceType = null;
	if(type==1){
		sourceType=['camera'];
	}else if(type==2){
		sourceType=['album'];
	}else{
		sourceType=['camera','album']
	}
	wx.chooseImage({
	    count: 1, // 默认9
	    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
	    sourceType: sourceType, // 可以指定来源是相册还是相机，默认二者都有
	    success: function (res) {
	        var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
	        $("#avater").val(localIds);
	        uploadPic();
	        /*wx.uploadImage({
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
	    						$("#preview").attr("src",$("#remoteurl").val()+"/"+data.msg+"?"+Math.random());
	    					}else{
	    						alert(data.msg);
	    					}
	    				}
	    			});
	    	    }
	    	});*/
	    }
	});
}