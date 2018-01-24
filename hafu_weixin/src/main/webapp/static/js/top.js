var sex = 2;
		var colorT = [],
	     colorOne = ['#9ED7EA','#9ED7EA'],
		 colorTwo = ['#F2A09D','#F2A09D'];
	    //饼状图的颜色colorArr
	    var colorArr = [],
    	    colorArr1 = ['#DEEDF2','#9ED7EA','#5eb8f0','#3F86C4'],
	        colorArr2 = ['#FCE9EB','#FAD7DE','#F08889','#E25563'];
		
			if(sex == 1) {
				
				$('.colorT').css({'color':'#9ED7EA'});
				
		    	colorT = colorOne;
		    	colorArr = colorArr1;
			} else {
				
				
				colorT = colorTwo;
	    		colorArr = colorArr2;
		}
$(function(){
	var sex_str = $("#userGender").val();
	if(sex_str == "男"){
		sex = 1;
	}
	if(sex == 1) {
				$('.colorT').css({'color':'#9ED7EA'});
				$('.commonCss').remove();
				var man = $('<link rel="stylesheet" type="text/css" class="commonCss" href="/static/css/com-man.css" />')
				$('.commonCss').remove();
				$('body').prepend(man);
		    	colorT = colorOne;
		    	colorArr = colorArr1;
			} else {
				$('.commonCss').remove();
				var woman = $('<link rel="stylesheet" type="text/css" class="commonCss" href="/static/css/com-woman.css" />')
				$('body').prepend(woman);
				colorT = colorTwo;
	    		colorArr = colorArr2;
		}
})



var mydate = new Date();
var date = mydate.getMonth()+"-"+mydate.getDate()+" "+mydate.getHours()+":"+mydate.getMinutes();
var localId2 = "";
var ids;
$(function() {
	var url = window.location.href;
		var b = new Base64();
		url = b.encode(url);
		hafuConfig(url);
}); 

//开始
var localId;
function start(){
	
	wx.startRecord();
}

function longPress(){
    timeOutEvent = 0; 
    start(); 
} 

//结束
function end(){
	wx.stopRecord({
	    success: function (res) {
	       localId = res.localId;
	    }
	});
	//alert("end()方法结束");
}

//录音

$(function(){
	var timeOutEvent=1,startTime=null,nowTime=null,lengthTime=0;
    $("#yyBarYY").on({
        touchstart: function(e){
        	if(move == true){
        		var moveWidh = $('.left-nav').width();
        		$('#conWarp').remove();
    			$("body").css("overflow-y", "auto");
    			$('.footer').animate({'left':'0px'},300);
    			$('.content').animate({'left':'0px'},300);
    			$('.left-nav').animate({'left':-moveWidh},300,function(){
    				$('#conWarp').addClass('hide');
    			});
    			move = false;
        	}
        	
        	startTime = new Date().getTime();
            timeOutEvent = setTimeout("longPress()",100);
         	e.preventDefault();
         	e.stopPropagation();
         	$("#yyICOPic").css('display','block');
        },
        touchmove: function(){
            clearTimeout(timeOutEvent); 
             
        },
        touchend: function(){
        	nowTime = new Date().getTime();
       		clearTimeout(timeOutEvent);
       		$("#yyICOPic").css('display','none');
       		lengthTime = nowTime - startTime;
            if(lengthTime<500){ 
            	
            	$('.footer-list').toggleClass('move-list');
    			$('.foot-left').toggleClass('color-change');
    			$('.foot-middle').toggleClass('color-change');
            	
            }else{
            	window.location.href="../voice/voice";
            	end();
            	//setTimeout("voiceEnd2()",500);
            	voiceEnd2()
            	//alert("超时执行");
            }
            return false; 
        }
    })
});
//调用微信接口 实现语音上转
function voiceEnd2(){
	//alert("进入voiceEnd()");
	 localId2 = localId;
    var duration = "";
    wx.uploadVoice({
        localId: localId,  // 需要上传的音频的本地ID，由stopRecord接口获得
        isShowProgressTips: 0, // 默认为1，显示进度提示
        success: function (res) {
         var serverId1 = res.serverId; // 返回音频的服务器端ID
          wx.translateVoice({
          		    localId: localId, // 需要识别的音频的本地Id，由录音相关接口获得
          		    isShowProgressTips: 0, // 默认为1，显示进度提示
          		    success: function (res) {
          		    	$.ajax({
          					url:'saveVoice',
          					data:{serverId:serverId1,voiceMsg:res.translateResult,type:0},
          					type:"POST",
          					dataType: "json",
          					success:function(data){
          						if(data.status=='success'){
          							duration = data.msg;
          							var s = (Number(duration)/2)+3;
          							var imgSrc=$("#imgSrc").val();
          							$(".ltUL").append("<li><div class='timeLine'>"+date+"</div></li><li><span class='photo rightPhoto'><img src="+imgSrc+" /></span><div class='rightMsg yy' style='width:"+s+"em' onclick='playVoice()'><span id='s"+data.ob+"'></span><i>"+duration+"''</i></div></li>");
          						}else{
          							alert(data.msg);
          						}
          					}
          				});
          		 }
          	});
        }
     });	
}


var voice='<div id="yyICOPic" style="display:none"><img src="/static/images/yl.gif" >正在录音中...</div>'     
document.write(voice);
var warp ='<div class="content" id="conWarp" style="z-index: 998;background-color: rgba(0,0,0,0.1);"></div>'

var template = '<div class="left-nav">'+
			'<ul>'+
				'<li><a href="../../index/device_user_list"><span class="set"></span>设&#12288;&#12288;置</a></li>'+
				'<li><a href="../../health/sportData"><span class="sport"></span>运动数据</a></li>'+
				'<li><a href="../../track/track"><span class="map"></span>运动轨迹</a></li>'+
				'<li><a href="../../index/behavior_list"><span class="active"></span>行为识别</a></li>'+
				'<li class="opaity5"><a href="#"><span class="scole"></span>福心积分</a></li>'+
				'<li class="opaity5"><a href="#"><span class="song"></span>点&#12288;&#12288;歌</a></li>'+
				'<li class="opaity5"><a href="#"><span class="joke"></span>笑&#12288;&#12288;话</a></li>'+
			'</ul>'+
		'</div>'+
		'<div class="footer">'+
			'<div class="foot-left">'+
				'<a class="slideBtn" href="javascript:;">'+
					'<span></span>'+
					'<span></span>'+
					'<span></span>'+
				'</a>'+
			'</div>'+
			'<div  class="foot-middle"><a href id="yyBarYY" style="display:block;width:100%;height:100%">按住&nbsp;说话</a><a id="btn"></a></div>'+
			'<div class="foot-home"><a href="../../index/index"></a></div>'+
			'<div class="footer-list">'+
			  '<ul>'+
			    '<li><a href="../../../voice/voice" id="goVoice">聊     天</a></li>'+
			    '<li><a href="#"  id="content0" ontouchstart="send(1)">关爱一下</a></li>'+
			    '<li><a href="#"  id="content1" ontouchstart="send(2)">照护爸妈</a></li>'+
			    '<li><a href="#"  id="content2" ontouchstart="send(3)">关爱提醒</a></li>'+
			    '<li><a href="#"  id="content3" ontouchstart="send(4)">传递思念</a></li>'+
			  '</ul>'+
			'</div>'+
		'</div>';
		document.write(template);
		$('#btn').on('touchstart',function(e){
			e.stopPropagation();
			
		});
		$('.footer-list').on("touchstart", function(e) {
			e.stopPropagation();
		});
		$('#goVoice').on("touchstart", function(e) {
			e.stopPropagation();
		});
//发送关爱类型
function send(index){		
  $.ajax({
		url:'../../voice/sendWord',
		data:{"LoveType":index},
		type:"POST",
		dataType: "json",
		success:function(data){
			if(data.status=='success'){
			    var Msg= $("#sendcontent").val(data.ob);
			    var voiceMsg =$("#sendcontent").val();
			   // alert("获取的voiceMsg："+voiceMsg);
			    if(voiceMsg != ""){
			    	voiceSubmit2(voiceMsg);//调用方法
				 }
			}else{
				  alert(data.msg);
			}
		}
	});
}

//将从数据库获取的文字 保存到语音表中
var voiceMsg =$("#sendcontent").val();
function voiceSubmit2(voiceMsg){
				  $.ajax({
	  					url:'../../voice/sentVoice',
	  					data:{serverId:'none',"voiceMsg":voiceMsg,type:1},
	  					type:"POST",
	  					dataType: "json",
	  					success:function(data){
	  						if(data.status=='success'){
	  					       setTimeout("from()",200);	//定时调转
	  					}else{
	  						  alert(data.msg);
	  					}
	  					}
	  				});
 }
function from(){
	window.location.href="../../voice/voice"
}	
var sendtemFun1 ='<input type="hidden" id="sendcontent" value="">'
document.write(sendtemFun1);
		
var warp ='<div class="content" id="conWarp" style="z-index: 998;background-color: rgba(0,0,0,0.1);"></div>'
	
var move = false;
	$('.slideBtn').on('touchstart',slideMove);
	function slideMove(e){
		e.preventDefault();
		var moveWidh = $('.left-nav').width();
		console.log(moveWidh)
		if(move == false){
			$('body').append(warp);
			$("body").css("overflow-y", "hidden");
			$('.left-nav').animate({'left':'0px'},250,function(){
				$('#conWarp').removeClass('hide');
			});
			$('.footer').animate({'left':moveWidh},300);
			$('.content').animate({'left':moveWidh},300);
			
//			if($('#sexcss').attr('sex') == 'man'){
//				$('.foot-home').css('background-color','#DEEDF2');
//			}else{
//				$('.foot-home').css('background-color','#FAD3D4');
//			}
			move = true;
		}else{
			$('#conWarp').remove();
			$("body").css("overflow-y", "auto");
			$('.footer').animate({'left':'0px'},300);
			$('.content').animate({'left':'0px'},300);
			$('.left-nav').animate({'left':-moveWidh},300,function(){
				$('#conWarp').addClass('hide');
			});
//			if($('#sexcss').attr('sex') == 'man'){
//				$('.foot-home').css('background-color','#4086C4');
//			}else{
//				$('.foot-home').css('background-color','#EB5F5E');
//			}
			move = false;
		};
	};
	
	var startX,startY;
	document.addEventListener("touchstart", function(e) {
			e.touches; //多点触控
			startX = e.touches[0].pageX;
			startY = e.touches[0].pageY;
			if($('.footer-list').hasClass('move-list')){
				setTimeout(function(){
					$('.footer-list').toggleClass('move-list');
					$('.foot-left').toggleClass('color-change');
					$('.foot-middle').toggleClass('color-change');
				},300);
				
			};
	});
	document.addEventListener("touchmove", function(e) {
			if (move == true) {
				e.preventDefault();
			}
	}, false);
	var width = $(window).width()*0.10;
	document.addEventListener("touchend", function(e) {
		e.changedTouches; //结束位置,不能再用touches
		endX = e.changedTouches[0].pageX;
		endY = e.changedTouches[0].pageY;
		if ((endX+width < startX) && move == true &&   Math.abs(endY-startY)<30 ) {
			slideMove();
		}
	});		
		

		
		
		
		
		
		
		
		
		