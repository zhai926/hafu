var sex = 2;
		var colorT = [],
	     colorOne = ['blue','#9ED7EA'],
		 colorTwo = ['red','#F2A09D'];
	    //饼状图的颜色colorArr
	    var colorArr = [],
    	    colorArr1 = ['#DEEDF2','#9ED7EA','#A8D9EA','#3F86C4'],
	        colorArr2 = ['#FCE9EB','#FAD7DE','#F08889','#E25563'];
		
			if(sex == 1) {
				//$('#sexcss').attr('href', 'static/css/com-man.css');
				$('.colorT').css({'color':'#9ED7EA'});
				var man = $('<link rel="stylesheet" type="text/css" href="../static/css/com-man.css" />')
				$('body').append(man);
		    	colorT = colorOne;
		    	colorArr = colorArr1;
			} else {
				//$('#sexcss').attr('href', 'static/css/com-woman.css');
				var woman = $('<link rel="stylesheet" type="text/css" href="../static/css/com-woman.css" />')
				$('body').append(woman);
				colorT = colorTwo;
	    		colorArr = colorArr2;
		}

var warp ='<div class="content" id="conWarp" style="z-index: 998;background-color: rgba(0,0,0,0.1);"></div>'

var template = '<div class="left-nav">'+
			'<ul>'+
				'<li><a href="index/device_user_list"><span class="set"></span>设&#12288;&#12288;置</a></li>'+
				'<li><a href="health/sportData"><span class="sport"></span>运动数据</a></li>'+
				'<li><a href="index/behavior_list"><span class="active"></span>行为识别</a></li>'+
				'<li class="opaity5"><a href="#"><span class="scole"></span>福心积分</a></li>'+
				'<li class="opaity5"><a href="#"><span class="song"></span>点&#12288;&#12288;歌</a></li>'+
				'<li class="opaity5"><a href="#"><span class="joke"></span>笑&#12288;&#12288;话</a></li>'+
			'</ul>'+
		'</div>'+
		'<div class="footer">'+
			'<div class="foot-left">'+
				'<a class="slideBtn" href="#">'+
					'<span></span>'+
					'<span></span>'+
					'<span></span>'+
				'</a>'+
			'</div>'+
			'<div class="foot-middle"><a href="voice/voice">按住&nbsp;说话</a><a href="javescript:;" id="btn"></a></div>'+
			'<div class="foot-home"><a href="index/index"></a></div>'+
		'</div>';
		document.write(template);
		
var move = false;
	$('.slideBtn').on('touchstart',slideMove);
	function slideMove(){
		var moveWidh = $('.left-nav').width();
		console.log(moveWidh)
		if(move == false){
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
		

		
		
		
		
		
		
		
		
		