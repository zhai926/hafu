$(function(){
	//获取未读 
	noread();
	//佩戴，当前行为
	lastAdornAndBehavior();
	setInterval("noread()",5*1000);
	setInterval("lastTrack()",5*1000);
	setInterval("lastAdornAndBehavior()",5*1000);
	//设置运动时间
	$.getJSON("/health/lastHealthPoint",{},function(data){
		if(data.status=='success'){
			if(data.ob!=null){ //
				$("#sportNum").text(data.ob.stepCount);
				$("#sportTime").text(data.ob.liveness);
			}	
		}else{
			$("#sportNum").text("未测量");
			$("#sportNum").attr("class","gray_small");
			$("#sportTime").text("未测量");
			$("#sportTime").attr("class","gray_small");
		}
		var t1 = $("#sportNum").text();
		var t2= $("#sportTime").text();
		if(t1==""){
			$("#sportNum").text("未测量");
			$("#sportNum").attr("class","gray_small");
		}
		if(t2==""){
			$("#sportTime").text("未测量");
			$("#sportTime").attr("class","gray_small");
		}
	});
	
	//显示天气
	var location={};
	if($("#track_latitude").val()==""||$("#track_longitude").val()==null){//
		var geocoder = new qq.maps.Geocoder();
		geocoder.getLocation($("#userAddress").val());
		geocoder.setComplete(function(result) {
			console.log(result.detail.location);
			location.lat=result.detail.location.lat;
			location.lng=result.detail.location.lng;
			Base.ajax({
				url:"/index/getWeather",
				data:location,
				ajax:function(d){
			    	if(d.status=='success'){
			    		$("#temperature").html(d.ob.temperature+"℃");
			    		var pic = d.ob.weatherPic;
			        	$("#weather").children("img").attr("src",pic);
			        	$("#cityName").html(d.ob.cityName);
			        	$("#tem_week span:eq(0)").html(d.ob.week);
			        	$("#tem_week span:eq(1)").html(d.ob.date.month+1+"/"+d.ob.date.date);
			        	if(d.ob && d.ob.humidity && d.ob.humidity!=null ){
			        		var dn = d.ob.humidity.replace("%","");
				        	$("b[data-name=HJSJ_SD]").text(dn); //湿度
			        		if(dn<30 || dn>80) {
			        			$("b[data-name=HJSJ_SD]").addClass("red");
			        			$("#tip_HJSJ").attr("class","tip_warn");
			        		}
			        	}
			        	if(d.ob && d.ob.quality && d.ob.quality!=null ){//空气质量
			        		var dn = d.ob.quality;
			        		if(dn && null!=dn){
								if(dn=="优" || dn=="良"){
									$("b[data-name=HJSJ_KQZL]").text(dn);
								}else{
									$("b[data-name=HJSJ_KQZL]").text(dn);
									$("b[data-name=HJSJ_KQZL]").addClass("red");
									$("#tip_HJSJ").attr("class","tip_warn");
								}
							}
			        	}
			        	if(d.ob && d.ob.windPower && d.ob.windPower!=null ){ //风力
			        		var dn = d.ob.windPower;
							if(isNaN(dn) && dn>3){
								$("b[data-name=HJSJ_ZY]").addClass("red");
								$("#tip_HJSJ").attr("class","tip_warn");
							}
							$("b[data-name=HJSJ_ZY]").text(d.ob.windDirection+" "+dn);
			        	}
			    	}
			    	
			    	var t1 = $("b[data-name=HJSJ_SD]");
			    	var t2 = $("b[data-name=HJSJ_ZY]");
			    	var t3 = $("b[data-name=HJSJ_KQZL]");
			    	no_cel(t1);
			    	no_cel(t2);
			    	no_cel(t3);
			    	
			    },
			    error:function(){
			    }
			});
		});
		geocoder.setError(function() {
			console.log("error");
		});
	}else{
		location.lat=$("#track_latitude").val();
		location.lng=$("#track_longitude").val();
		Base.ajax({
			url:"/index/getWeather",
			data:location,
			ajax:function(d){
				console.log(d);
		    	if(d.status=='success'){
		    		$("#temperature").html(d.ob.temperature+"℃");
		    		var pic = d.ob.weatherPic;
		        	$("#weather").children("img").attr("src",pic);
		        	$("#cityName").html(d.ob.cityName);
		        	$("#tem_week span:eq(0)").html(d.ob.week);
		        	$("#tem_week span:eq(1)").html(d.ob.date.month+1+"/"+d.ob.date.date);
		        	if(d.ob && d.ob.humidity && d.ob.humidity!=null ){
		        		var dn = d.ob.humidity.replace("%","");
			        	$("b[data-name=HJSJ_SD]").text(dn); //湿度
		        		if(dn<30 || dn>80) {
		        			$("b[data-name=HJSJ_SD]").addClass("red");
		        			$("#tip_HJSJ").attr("class","tip_warn");
		        		}
		        	}
		        	if(d.ob && d.ob.quality && d.ob.quality!=null ){//空气质量
		        		var dn = d.ob.quality;
		        		if(dn && null!=dn){
							if(dn=="优" || dn=="良"){
								$("b[data-name=HJSJ_KQZL]").text(dn);
							}else{
								$("b[data-name=HJSJ_KQZL]").text(dn);
								$("b[data-name=HJSJ_KQZL]").addClass("red");
								$("#tip_HJSJ").attr("class","tip_warn");
							}
						}
		        	}
		        	if(d.ob && d.ob.windPower && d.ob.windPower!=null ){ //风力
		        		var dn = d.ob.windPower;
						if(isNaN(dn) && dn>3){
							$("b[data-name=HJSJ_ZY]").addClass("red");
							$("#tip_HJSJ").attr("class","tip_warn");
						}
						$("b[data-name=HJSJ_ZY]").text(d.ob.windDirection+" "+dn);
		        	}
		    	}
		    	var t1 = $("b[data-name=HJSJ_SD]");
		    	var t2 = $("b[data-name=HJSJ_ZY]");
		    	var t3 = $("b[data-name=HJSJ_KQZL]");
		    	no_cel(t1);
		    	no_cel(t2);
		    	no_cel(t3);
		    },
		    error:function(){
		    }
		});
	}
});
//根据头像报警状态判断执行弹出层
function temFun1(e){
	//var va = $(e).attr("data-value");
	var va = $("#current_alter").attr("class");
	if(va=="ico1"){
		/*read();*/
//		layer.open({
//			closeBtn:1,
//		    content:$("#outLayer1_al").html(),
//		},1);
		layer.open({
			type: 2,
			area: ['80%', '65%'],
			closeBtn: 2,
			title: false,
			shadeClose: true,
			content:'../tpls/warn.html' 
		});
	}else{
		layer.open({
			closeBtn:false,
			title: false,
			btn:false,
			offset:'rb',
			shadeClose: true,
			area:['100%','70%'],
		    content: $("#outLayer1").html(),
		});
	}
}
//异步获取 佩戴状态  和 当前行为
function lastAdornAndBehavior(){
	//佩戴状态  和 当前行为
	$.getJSON("/index/indexAdorn",{},function(data){
		if(data.status=='success'){
			if(data.msg=='未佩戴' || data.msg=='暂无记录'){
				$("#current_behavior").text(data.msg); //设置当前行为
				$("#current_behavior").attr("class","gray_small");
				$("#icon-xingzou").attr("class","icon-xingzou");
				$("#text-xe").text("");
				$("#adorn").text("未佩戴");
				$("#adornClass").removeClass("manState1").addClass("manState3");
			}else{
				var Be_St = Base.BEHAVIOR_STATUS[data.msg];
				if(Be_St && Be_St!=null){
					$("#current_behavior").text("正在"+data.msg);
					$("#icon-xingzou").attr("class","icon-xingzou "+Be_St);
					$("#text-xe").text(data.msg);
				}else{
					$("#current_behavior").text(data.msg.substring(0,5)+(data.msg.length>5?"..":"")); //设置当前行为
					$("#icon-xingzou").attr("class","icon-xingzou");
					$("#text-xe").text("");
				}
				$("#adorn").text("佩戴中");
				$("#adornClass").removeClass("manState3").addClass("manState1");
			}
		}else{
			$("#current_behavior").text("未佩戴");
			$("#current_behavior").attr("class","gray_small");
			$("#icon-xingzou").attr("class","icon-xingzou");
			$("#text-xe").text("");
			$("#adorn").text("未佩戴");
			$("#adornClass").removeClass("manState1").addClass("manState3");
		}
	});
}
//异步读取轨迹
function lastTrack(){
	$.getJSON("/index/indexTrack",{},function(data){
		if(data.status=='success'){
			if(data.ob!=null){
				var po = "";
				if(data.ob.position ){
					if(data.ob.position.length>12)
						po = data.ob.position.substring(0,12)+"..";
					else
						po = data.ob.position;
				}
				$("#current_track").text(po);
			}	
		}else{
			$("#current_track").text("暂无记录");
			$("#current_track").attr("class","gray_small");
		}
		var t1 = $("#current_track").text();
		if(t1==""){
			$("#current_track").text("暂无记录");
			$("#current_track").attr("class","gray_small");
		}
	});
}

//异步未读报警进来
function noread(){
	$.getJSON("/alter/noread",{},function(data){
		if(data.status=='success'){
			if(data.ob!=null){ //有未读
				$("#current_alter").removeClass("ico").addClass("ico1");
				$("#alter_id").val(data.ob.id);
				$("#alter_day").text(Base.DateFormat(data.ob.alertDate,"yyyy年MM月dd日"));
				$("#alter_time").text(Base.DateFormat(data.ob.alertDate,"hh:mm:ss"));
				$("#alter_type").text(data.ob.alertType==1?"自动报警":"手动报警");
				$("#alter_position").text(data.ob.position);
				$("#alter_mobile").text(data.ob.mobile);
				temFun1();//有报警直接打开
			}	
		}
	});
}
//读取报警
function read(){
	var id = $("#alter_id").val();
	$.getJSON("/alter/read",{"alterId":id},function(data){
		if(data.status=='success'){
			$("#current_alter").removeClass("ico1").addClass("ico");	
		}
		layer.closeAll();
	});
}

//为测量方法 
function no_cel(e){
	var t = e.text();
	if(t==""){
		e.text("未测量");
		e.attr("class","gray_small");
	}
}