<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!--meta name="viewport" content="width=device-width, initial-scale=1"-->
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
<title>语音互动</title>
<link href="/static/css/bootstrap.min.css" rel="stylesheet">
<!--左侧滑出样式-->
<link rel="stylesheet" type="text/css" href="/static/css/normalize.css" />
<!--左侧滑出样式over-->
</script>
<%String remoteImage=getServletContext().getInitParameter("remoteImage");
    	request.setAttribute("remoteImage", remoteImage);%>
<link href="/static/css/index.css" rel="stylesheet">
<script src="/static/js/jquery-2.1.4.min.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
	(function (doc, win) {
		        var docEl = doc.documentElement,
		            resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
		            recalc = function () {
		                var clientWidth = docEl.clientWidth;
		                if (!clientWidth) return;
		                if(clientWidth>=640){
		                    docEl.style.fontSize = '100px';
		                }else{
		                    docEl.style.fontSize = 100 * (clientWidth / 640) + 'px';
		                }
		            };
		
		        if (!doc.addEventListener) return;
		        win.addEventListener(resizeEvt, recalc, false);
		        doc.addEventListener('DOMContentLoaded', recalc, false);
		    })(document, window);

</script>
</head>
<body>
<div id="yyICOPic" style="display:none"><img src="/static/images/yl.gif">正在录音中...</div>
		<script src="/static/js/top.js" type="text/javascript" charset="utf-8"></script>
		<link rel="stylesheet" type="text/css" href="/static/css/footer.css" />
		<div class="content containerStyle1" style="overflow:auto;background-color: #F0F0F0;height:100%;padding-bottom:100px;">
			<div class="content-wrap">
				<div>
					<div class="pagePadding fixSubMenu">
						<!--  菜单的click样式，是表示，当前点击的按钮，加上click样式 背景变蓝色，字变白色  -->
						<ul class="pageMenuUL1">
						<li class="alone w50" onclick="window.location.href='/prompt/promptList'">关爱提醒</li>
						<!-- <li class="alone w33" onclick="window.location.href='/prompt/encourageList'">鼓励一下</li> -->
						<li class="alone w50 click" onclick="window.location.href='/voice/voice'">语音互动</li>
						</ul>
					</div>
				</div>
                <c:set var="rempic" value="${initParam.remoteImage}/${sessionScope.CurrentUser.avater}"></c:set>
				<ul class="ltUL">
				<c:forEach items="${voiceList}" var="voice"  varStatus="loop">
	                <c:choose>
	                	<c:when test="${voice.source == 1}">
	                	<c:choose>
	                		<c:when test="${voice.isRead == 1 }">
	                			<li><div class="timeLine">${voice.time}</div></li>
	                			<li>
	                			<span class="photo leftPhoto"><img src="${sessionScope.CurrentUser.avater==null || sessionScope.CurrentUser.avater==''?'/static/images/man.png':rempic}"/></span>
	                			<div class="leftMsg yy" style="width:${voice.length}em; " onclick="playVoice1('${voice.serverId}',${voice.id },${voice.duration })"><span id="s${voice.id }" class="1"></span><i>${voice.duration }''</i></div></li>
	                		</c:when>
	                		<c:otherwise>
	                			<li><div class="timeLine">${voice.time }</div></li>
	                			<li>
	                			<span class="photo leftPhoto"><img src="${sessionScope.CurrentUser.avater==null || sessionScope.CurrentUser.avater==''?'/static/images/man.png':rempic}"/></span>
	                			<div class="leftMsg yy" style="width:${voice.length}em; " onclick="playVoice1('${voice.serverId}',${voice.id },${voice.duration })"><span id="s${voice.id }" class="1"></span><i>${voice.duration }''</i><b id="b${voice.id }" style="display: block;"></b></div></li>
	                		</c:otherwise>
	                	</c:choose>
	                	</c:when>
	                	<c:otherwise>
	                		<c:choose>
	                			<c:when test="${voice.serverId == 'none' }">
	                				<li><div class="timeLine">${voice.time}</div></li>
	                				<li>
	                				<span class="photo rightPhoto"><img src="${empty speakerList[loop.count-1].avater?'/static/images/man.jpg':speakerList[loop.count-1].avater}"/></span>
	                				<div class="rightMsg">${voice.voiceMsg}</div></li>
	                			</c:when>
	                			<c:otherwise>
	                				<li><div class="timeLine">${voice.time}</div></li>
			                		<li>
			                		<span class="photo rightPhoto"><img src="${empty speakerList[loop.count-1].avater?'/static/images/man.jpg':speakerList[loop.count-1].avater}"/></span>
			                		<div class="rightMsg yy" style="width:${voice.length }em; " onclick="playVoice1('${voice.serverId}',${voice.id },${voice.duration })"><span id="s${voice.id}" class="1"></span><i>${voice.duration }''</i></div></li>
	                			</c:otherwise>
	                		</c:choose>
	                	</c:otherwise>
	                </c:choose>
                </c:forEach>	
				</ul>
			</div>
			<!-- /content-wrap -->

			<div class="writeBox">
				<div class="writeBtn" id="write_btn"></div>
				<input type="text" class="message" name="message" id="textfield" />
				<span class="sendBtn" ontouchstart="sendBtn();">发送</span>
				<div class="clear"></div>
			</div>
			<script>
				var open = false,
					options = false,
				 	writeBox = document.getElementById('write_btn'),
					moveBox = writeBox.parentNode,
					width = $(window).width() * 0.50,
					Height = $(window).height();
				
				//进入页面滚动条位置固定底部
				$(function(){
					setTimeout(function(){
						var scrollT = $('.containerStyle1').children().height()-60;
					 	$('.containerStyle1').scrollTop(scrollT);
					 },100)
				})
				
				
				//文字输入框的控制方法
				function toggleBox() {
					if(open == false) {
						$('.writeBtn').parent('.writeBox').stop().animate({
							'width': '100%',
							'left': '0'
						}, 300, function() {
							$('.sendBtn').show();
						});
						
						open = true;
					} else {
						$('.writeBtn').parent('.writeBox').stop().animate({
							'width': '50px'
						}, 300);
						$('.sendBtn').hide();
						open = false;
					}
				};
				//发送按钮的样式控制
				$('.message').on('keyup', function() {
					var val = $(this).val();
					if(val.length > 0 && val.trim().length > 0) {
						$('.sendBtn').css('background-color', 'green');
					} else {
						$('.sendBtn').css('background-color', '#CCCCCC');
					}
				});
				//
				//document.addEventListener("touchend", function(e) {
				//	setTimeout(function(){
					//	$("#textfield").parent().css('bottom','50px');
					//	$('.footer').css('opacity',1);
					//},500);
					
			//})
				//文字输入按钮的跟随移动
				writeBox.addEventListener("touchstart", function(e) {
					e.touches; //多点触控
					startX = e.touches[0].pageX;
					startY = e.touches[0].pageY;
					options = false;					//$('.containerStyle1').css('overflow-y', 'hidden');
					document.addEventListener("touchmove", function(e) {
						e.stopPropagation();
						if(options == false) {
							var moveX = e.touches[0].pageX;
							var moveY = e.touches[0].pageY;
							if(open == false) {
								moveBox.style.left = (moveX - 25) + 'px';
								moveBox.style.bottom = (Height - moveY - 25) + 'px';
							} else {
								return false;
							};
						};

					});
					document.addEventListener("touchend", function(e) {
						e.changedTouches; //结束位置,不能再用touches
						endX = e.changedTouches[0].pageX;
						endY = e.changedTouches[0].pageY;
						var values;
						if(endX >= startX) {
							values = endX - startX;
						} else {
							values = startX - endX;
						}
						//$('.containerStyle1').css('overflow-y', 'scroll');
						if(options == false) {
							if((endX - startX > width)) {
								moveBox.style.left = (width * 2 - 45) + 'px';
								moveBox.style.bottom = '50px';
							} else if(values < 2) {
								toggleBox();
							} else {
								moveBox.style.left = '0px';
								moveBox.style.bottom = '50px';
							};
							options = true;
						}
						return false;
					});
				});
				
				
				//点击发送按钮时   调用voiceSubmit()方法
			    function sendBtn(){
			        var date=new Date();
					var NewTime=date.getTime(); 
					var voiceMsg = $("#textfield").val();
					var imgSrc=$("#imgSrc").val();
					setTimeout(function(){
						$("#textfield").blur();
						$("#textfield").parent().css('bottom','50px');
						$('.footer').show();
					},1000);
					//判断如果发送的内容为空的话，去空格后的长度小于0时，就不能够显示相关内容  
					if(voiceMsg != "" && voiceMsg.trim().length > 0){
					var lis='';
					lis+='<li>'+'<div class="timeLine">'+Date2Format(NewTime)+'</div>'+'</li>'
            		+'<li>'+
            		'<span class="photo rightPhoto">'+'<img src="'+imgSrc+'"/>'+'</span>'
            		+'<div class="rightMsg">'+voiceMsg+'</div>'+'</li>'
            		$('.ltUL').append(lis); 
            		//发送成功后  是发送按钮变灰 不能点击
					$('.sendBtn').css('background-color', '#CCCCCC');
					voiceSubmit();
			        }
				}
			    function Date2Format(date){
					var tempDate = new Date(date);
					M = (tempDate.getMonth()+1 < 10 ? '0'+(tempDate.getMonth()+1) : tempDate.getMonth()+1) + '-'; 
					D = tempDate.getDate() + ' ';  
					h = tempDate.getHours() + ':';
					m = tempDate.getMinutes();
					if(M<10){
		            	M="0"+M;
		            }else{
		            	M=M;
		             }
					if(D<10){
		            	D="0"+D;
		            }else{
		            	D=D;
		            }
					if(h<10){
			            	h="0"+h;
			            }else{
			            	h=h;
			        }
					if(m<10){
		            	m="0"+m;
		            }else{
		            	m=m;
		            }
					var str =M+D+h+m;
					return str ;
				} 
				//将文字 保存到语音表中
				$("#textfield").on('focus',function(e){
					e.stopPropagation();
					setTimeout(function(){
						$('.footer').hide();
						$("#textfield").parent().css('bottom','0px');
						$('.writeBox').css({'bottom':'0px'});
					},350)
					
				})
				$("#textfield").on('blur',function(e){
					e.stopPropagation();
					setTimeout(function(){
						$('.footer').show();
						$("#textfield").parent().css('bottom','50px');
						$('.writeBox').css({'bottom':'50px'});
					},500)
					
				})
				/* $("#textfield").on('blur',function(){
					$("#textfield").parent().css('bottom','50px');
					$('.footer').show();
				}) */
				function voiceSubmit(){
					  var voiceMsg = $("#textfield").val();
					  $('.containerStyle1').scrollTop($('.containerStyle1').get(0).scrollHeight-60);
					  /* alert("---要发送的内容:---"+voiceMsg);  */
					  if(voiceMsg != "" && voiceMsg.trim().length > 0){
						  //然后清空输入框
						  $("#textfield").val("");
						  $.ajax({
			  					url:'saveVoice',
			  					data:{serverId:'none',voiceMsg:voiceMsg,type:1},
			  					type:"POST",
			  					dataType: "json",
			  					success:function(data){
			  						if(data.status=='success'){
				  					}else{
				  						  alert(data.msg);
				  					}
			  					}
			  				});
					  }
				  }
				function from(){
					window.location.href="../voice/voice" 
				}
			</script>
		</div>
		<input type="hidden" id="userGender" value="${sessionScope.CurrentUser.gender}">
		<input type="hidden" id="imgSrc" value="${sessionScope.LoginUser.avater==null || sessionScope.LoginUser.avater==''?'/static/images/man.jpg':sessionScope.LoginUser.avater}" />
</body>
<script src="/static/js/main3.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/static/js/voice/Math.uuid.js"></script>
<script type="text/javascript" src="/static/js/voice/Code.js"></script>
<script type="text/javascript" src="/static/js/voice/wechatJs.js"></script>
<script type="text/javascript">
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
    
    var localId;
    //开始录音接口
   /*  function start(){
    	wx.startRecord();
    }
    //停止录音接口
    function end(){
    	wx.stopRecord({
    	    success: function (res) {
    	       localId = res.localId;
    	    }
    	});
    } */
    
    //播放语音接口
    function playVoice1(serverId,id,time){
    	ids = id;
    	//alert("弹出的 IDS是:"+ids)
    	$("#s"+ids).removeClass().addClass("over");
    	setTimeout(function(){
    		$("#s"+ids).removeClass();
    	},time*1000);
    	//下载语音
        wx.downloadVoice({
            serverId: serverId, // 需要下载的音频的服务器端ID，由uploadVoice接口获得
            isShowProgressTips: 1, // 默认为1，显示进度提示
            success: function (res) {
                var localId2 = res.localId; // 返回音频的本地ID
                wx.playVoice({
            	    localId: localId2
            	});
            }
        });
        var display =$("#b"+id).css('display');
        if(display == 'block'){
        	 $.ajax({
     			url:'updateRead',
     			data:{id:id},
     			type:"POST",
     			dataType: "json",
     			success:function(data){
     				if(data.status=='success'){
     					$("#b"+id).css('display','none');
     				}else{
     					alert(data.msg);
     				}
     			}
     		});
        }
       
    }
    
    //播放录制的语音
    function playVoice(){
    	wx.playVoice({
    	    localId: localId2    //需要播放的音频的本地ID，由stopRecord接口获得.
    		//localId: localId
    	});
    }
    
    var timeOutEvent=0;
  /*  $("#yyBarYY").on({
        touchstart: function(e){
        	alert("进入voice中录音");
            timeOutEvent = setTimeout("longPress()",100);
         	e.preventDefault();
         	$("#yyICOPic").css('display','block');
        },
        touchmove: function(){
            clearTimeout(timeOutEvent); 
            timeOutEvent = 0; 
        },
        touchend: function(){
       		clearTimeout(timeOutEvent);
       		$("#yyICOPic").css('display','none');
            if(timeOutEvent!=0){ 
                alert("请长按说话！"); 
            }else{
            	end();
            	setTimeout("voiceEnd()",500);
            }
            return false; 
        }
    })  */
     
    function voiceEnd(){
    	 localId2 = localId;
         var duration = "";
         //上传语音接口
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
    
    function longPress(){
        timeOutEvent = 0; 
        start(); 
    }  
    $('#content').scrollTop( $('#content')[0].scrollHeight );  
</script>
</html>
