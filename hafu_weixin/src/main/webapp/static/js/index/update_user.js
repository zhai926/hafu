//健康目标数据存储
	var data_store = {};
$(function(){
	$.getJSON("/index/jsTicket",{"url":window.location.href},function(data){
		wx.config({
		    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
		    appId: data.appId, // 必填，公众号的唯一标识
		    timestamp:parseInt(data.timestamp,10), // 必填，生成签名的时间戳
		    nonceStr: data.nonceStr, // 必填，生成签名的随机串
		    signature: data.signature,// 必填，签名，见附录1
		    jsApiList: ['scanQRCode','chooseImage'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		});
		wx.error(function(res){
			//alert("无法扫描");
		});
		wx.ready(function(){
			
		});
	});
	
	//初始化健康目标
	$.post("/index/getHealthTarget",function(d){
		
		var data={};
		//if(d!=null&&d.length>0){
			for(var i=0;i<d.length;i++){	
				data[d[i].dataType]=d[i];
			}
			for(var i=0;i<3;i++){
				if(data[i]==null){
					data[i]={"sleepTime":"","stepCount":"","staticCount":""};
				}
			}
			data_store = data;
			var text=data[0].sleepTime;
			$("#sT").val(text);
			$("form input[name='sleepTime']").val(text);
			text=data[0].stepCount;
			$("#sC1").val(text);
			$("form input[name='stepCount']").val(text);
			text=data[0].staticCount;
			$("#sC2").val(text);
			$("form input[name='staticCount']").val(text);
			$("form input[name='dataType']").val(0);//dataType
			$("#prompt .clear.bo1 .pageMenuUL1.pageMenuUL1_2 li").bind("click",function(){
					var len=$("#prompt .clear.bo1 .pageMenuUL1.pageMenuUL1_2 li.click").length;
					if(len>0){
						$("#prompt .clear.bo1 .pageMenuUL1.pageMenuUL1_2 li.click").removeClass("click");
					}
					$(this).addClass("click");
					var index=$("#prompt .clear.bo1 .pageMenuUL1.pageMenuUL1_2 li").index($(this)[0]);
					var text=data[index].sleepTime;
					$("#sT").val(text);
					$("form input[name='sleepTime']").val(text);
					text=data[index].stepCount;
					$("#sC1").val(text);
					$("form input[name='stepCount']").val(text);
					text=data[index].staticCount;
					$("#sC2").val(text);
					$("form input[name='staticCount']").val(text);
					$("form input[name='dataType']").val(index);
				})
		//}
		
	},"json");
	//初始化地址
	$('#element_id').cxSelect({
		url: '/static/js/select/cityData.json',               // 如果服务器不支持 .json 类型文件，请将文件改为 .js 文件
	  	selects: ['province', 'city', 'area'],  // 数组，请注意顺序
	  	emptyStyle: 'hidden'
	});	
	//
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
	
	//表单异步提交
	var fl = false;
	$("#submit").click(function(){
		if(fl) return ;
		var realName = $("input[name=realName]").val();
		if(realName==null || $.trim(realName)==''){
			Base.alert("请填写姓名!");
			return false;
		}
		if($.trim(realName).length>10){
			Base.alert("姓名长度10字以内");
			return false;
		}
		/*var gender = $("select[name=gender]").val();
		if(gender==null || gender==""){
			alert("请选择性别");
			return false;
		}*/
		var age = $("input[name=age]").val();
		if(age && $.trim(age)!=null){
			if(/^(\+|-)?\d+($|\.\d+$)/.test(age)){
				var value=parseInt(age);
	            if(value<1&&value>150){
	            	Base.close();
	            	Base.alert("请输入正确的年龄");
	                return false;
	            }
			}else{
				 Base.close();
				 Base.alert("请输入正确的年龄");
				 return false;
			}
		}
		var card = $("input[name=card]").val();
		if(card==null || card==''){
			Base.alert("请输入身份证号");
			return false;
		}
		var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  
	    if(reg.test(card) === false){
	    	Base.close();
	    	Base.alert("身份证号输入不合法");
	        return  false;  
	    }
		var mobile = $("input[name=mobile]").val();
		if(mobile==null || mobile==''){
			Base.alert("请输入电话");
			return false;
		}else{
			if(/^(\d{11})$/.test(mobile)){
				
			}else{
				Base.alert("请输入正确的电话");
				return false;
			}
		}
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
				if (/^(\d{11})$/.test(phone)) {
					
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
		//添加三个对象开始
		var array_health = new Array();
		array_health.push({"sleepTime":data_store[0].sleepTime,"stepCount":data_store[0].stepCount,"staticCount":data_store[0].staticCount,"dataType":0});
		array_health.push({"sleepTime":data_store[1].sleepTime,"stepCount":data_store[1].stepCount,"staticCount":data_store[1].staticCount,"dataType":1});
		array_health.push({"sleepTime":data_store[2].sleepTime,"stepCount":data_store[2].stepCount,"staticCount":data_store[2].staticCount,"dataType":2});
		var temp_health=JSON.stringify(array_health);
		$("input[name='ob']").val(temp_health);
		fl =true;
		$(this).css("background","gray");
		$.ajax({
			url:'/index/updateInfo',
			data:$('#form').serialize(),
			type:"POST",
			dataType: "json",
			success:function(data){
				if(data.status=='success'){
					Base.alert("保存成功");
					window.location.href="/index/device_user_list";
				}else{
					Base.alert(data.msg);
				}
				fl = false;
				$("#submit").css("background","#FFA647");
			}
		});
	});
});
//数值改变监听
function changes(index){
	var out_index=$("#prompt .clear.bo1 .pageMenuUL1.pageMenuUL1_2 li").index($("#prompt .clear.bo1 .pageMenuUL1.pageMenuUL1_2 li.click"));
	if(index == 0){
		data_store[out_index].sleepTime = $("#sT").val();
	}else if(index == 1){
		data_store[out_index].stepCount = $("#sC1").val();
	}else{
		data_store[out_index].staticCount = $("#sC2").val();
	}
	$("input[name='dataType']").val(index);
}
//打开扫一扫
function saoyisao(){
	wx.scanQRCode({
	    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
	    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
	    success: function (res) {
		    var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
		   	if(result==null || result==''){
		   		Base.alert("无法获取扫描结果");
		   		return;
		   	}
		    var re = "";
		    if(result.indexOf(",")!=-1){
		    	re = result.split(",")[1];
		    }else{
		    	re = result;
		    }
		    $("#decode").val(re);
		}
	});
}
//提交前看是否是该人设备
function tijiaoValid(e){
	var code = $("#decode").val();
	if(code == null || code==''){
		Base.alert("请输入设备号");
		$(e).css("background","#32CF6D");
		return false;
	}
	$.getJSON("/set/isbelong",$('#form_add').serialize(),function(data){
		if(data.status=='success'){
			if(data.msg=="false"){
				Base.confirm({
					msg:"该设备已有亲友注册关注，是否添加到被关注人列表？",yes:function(){
						tijiao(e,1);
					}
				});
			}else{
				tijiao(e);
			}
		}else{
			Base.alert(data.msg);
		}
	});
}
//提交设备
function tijiao(e,i){
	var index = Base.open();
	var code = $("#decode").val();
	if(code == null || code==''){
		Base.close(index);
		Base.alert("请输入设备号");
		return false;
	}
	$(e).children("img").attr("src","/static/images/ico_ico7.png");
	$.ajax({
		url:'/index/ajaxAddDevice',
		data:$('#form_add').serialize(),
		type:"POST",
		dataType: "json",
		success:function(data){
			$(e).children("img").attr("src","/static/images/ico_ico7_hui.png");
			Base.close(index);
			if(data.status=='success'){
				Base.alert("绑定成功");
				if(i && i==1){
					window.location.href="/index/device_user_list";
				}else{
					var html = '<div class="item bo3 clear">'
						+'<div class="div_set_left div_set_title">设备<i>（行为记录仪）</i></div>'
						+'<div class="div_set_left3"><div>#CODE</div></div>'
						+'<div class="div_set_right" onclick="deleteDevice(#ID,this);" style="cursor: pointer;"><img src="/static/images/ico_del.png" ></div>'
						+'</div>';
					html = html.replace("#ID",data.ob.id).replace("#CODE",data.ob.code);
					$(e).parent().before(html);
					$("#decode").val("");
				}
			}else{
				Base.alert(data.msg);
			}
		}
	});
}
//删除用户
function deleteUser(id){
    var dl = layer.open({
		content: '是否删除该用户？',
		btn: ['取消','确认'],
		shadeClose: false,
		no: function(){
			window.location.href="/set/deleteUser?id="+id;
		}, yes: function(){
			layer.close(dl);
		}
	});
}
//删除设备
function deleteDevice(id,e){
	var fl = Base.confirm({
		msg:"确认删除此设备？",yes:function(){
			$.ajax({
				url:'/index/deleteDevice',
				data:{"deviceId":id},
				type:"POST",
				dataType: "text",
				success:function(data){
					if(data=='success'){
						$(e).parent(".bo3").remove();
					}else{
						Base.alert("删除失败");
					}
				}
			});
		}
	});
}
