<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title></title>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
		<link rel="stylesheet" type="text/css" href="../static/css/normalize.css"/>
		<link rel="stylesheet" type="text/css" href="../static/css/bootstrap.min.css"/>
		<!-- <link rel="stylesheet" type="text/css" href="../static/css/swiper.min.css"/>
		<link rel="stylesheet" type="text/css" href="../static/css/animate.min.css"/> -->
		<style type="text/css">
			body,html{
		width: 100%;
		height: 100%;
	}
		.slide-1{
			background: url(../static/images/imgH5/1.jpg) no-repeat center center;
			background-size: 100% auto;
		}
		
		.item-image{
			width: 100%;
			padding: 10% 10% 0 10%;
			background-color: ;
		}
		.item-image img{
			width: 100%;
			margin: 0 auto;
			height: auto;
		}
		.swiper-container{
			width: 100%;
			height: 100%;
		}
		.slide-2 .item-image{
			padding: 10% 5% 0 5%;
		}
		.item-image2{
			width: 76%;
			height: auto;
			margin: 0 auto;
		}
		.item-image2 img{
			width: 100%;
			height: auto;
			margin: 0 auto;
		}
		/*.swiper-button-next{
			background: none !important;
			position: fixed;
			width: 30px;
			height: auto;
			top: 95%;
			left: 50%;
			transform: translate(-50%);
		}*/
		/*箭头动画*/
		.arrow-up {
		    background: none;
		    height: auto;
		    width: 25px;
		    position: absolute;
		    left: 50%;
		    top: 95%;
		    margin-left: -12px;
		    z-index: 99;
		}
		
		.pt-page-moveIconUp {
		    -webkit-animation: moveIconUp ease-in-out 1s both infinite;
		    animation: moveToBottom ease-in-out 1s both infinite;
		}
		
		@-webkit-keyframes moveIconUp {
		    0% {
		        -webkit-transform: translateY(50%);
		        opacity: 0;
		    }
		    50% {
		        -webkit-transform: translateY(0%);
		        opacity: 1;
		    }
		    100% {
		        -webkit-transform: translateY(-50%);
		        opacity: 0;
		    }
		}
		
		@keyframes moveIconUp {
		    0% {
		        -webkit-transform: translateY(50%);
		        transform: translateY(50%);
		        opacity: 0;
		    }
		    50% {
		        -webkit-transform: translateY(0%);
		        transform: translateY(0%);
		        opacity: 1;
		    }
		    100% {
		        -webkit-transform: translateY(-50%);
		        transform: translateY(-50%);
		        opacity: 0;
		    }
		}
		
		@-webkit-keyframes moveToBottom {
		    from {
		    }
		    to {
		        -webkit-transform: translateY(50%);
		    }
		}
		
		@keyframes moveToBottom {
		    from {
		    }
		    to {
		        -webkit-transform: translateY(50%);
		        transform: translateY(50%);
		    }
		}
		.containerbg{
					width: 100%;
					display: block;
					height: 100%;
					background:url(../static/images/imgH5/welcom-bg.jpg) no-repeat center center;
					background-size:98% auto;
				}
		</style>
	</head>
	<body>
	<a href="register" class="containerbg">   </a>
		<!-- <div class="swiper-container">
			    <div class="swiper-wrapper">
			        <div class="swiper-slide slide-1">
			            <div class="item-image ani" swiper-animate-effect="zoomIn" swiper-animate-duration="0.2s" swiper-animate-delay="0s">
			            </div>
			            <div class="item-image ani" swiper-animate-effect="slideInLeft" swiper-animate-duration="0.2s" swiper-animate-delay="0s">
			            	
			            </div>
			        </div>
			        <div class="swiper-slide slide-2">
			            <div class="item-image ani" swiper-animate-effect="fadeInUp" swiper-animate-duration="0.2s" swiper-animate-delay="0s">
			            	<img src="../static/images/imgH5/2.jpg" />
			            </div>
			            <div class="item-image2 ani" swiper-animate-effect="fadeInDown" swiper-animate-duration="0.3s" swiper-animate-delay="0s">
			            	<img src="../static/images/imgH5/txt1.jpg" />
			            </div>
			        </div>
			        <div class="swiper-slide slide-3">
			            <div class="item-image ani"swiper-animate-effect="fadeInLeft" swiper-animate-duration="0.2s" swiper-animate-delay="0s">
			            	<img src="../static/images/imgH5/3.jpg" />
			            </div>
			            <div class="item-image2 ani" swiper-animate-effect="fadeInRight" swiper-animate-duration="0.2s" swiper-animate-delay="0s">
			            	<img src="../static/images/imgH5/txt2.jpg" />
			            </div>
			        </div>
					<div class="swiper-slide slide-4">
			            <div class="item-image ani"swiper-animate-effect="slideInLeft" swiper-animate-duration="0.2s" swiper-animate-delay="0s">
			            	<img src="../static/images/imgH5/4.jpg" />
			            </div>
			            <div class="item-image2 ani" swiper-animate-effect="slideInRight" swiper-animate-duration="0.2s" swiper-animate-delay="0s">
			            	<img src="../static/images/imgH5/txt3.jpg" />
			            </div>
			        </div>
			        <div class="swiper-slide slide-5">
			        	<a href="register">
			            <div class="item-image ani"swiper-animate-effect="fadeInDown" swiper-animate-duration="0.2s" swiper-animate-delay="0s">
			            	<img src="../static/images/imgH5/5.jpg" />
			            </div>
			            <div class="item-image2 ani" swiper-animate-effect="slideInRight" swiper-animate-duration="0.2s" swiper-animate-delay="0s">
			            	<img src="../static/images/imgH5/txt4.jpg" />
			            </div>
			            
			            </a>
			        </div> -->
			        <!-- swiper禁止了在拖动时页面整体移动（preventDefault），箭头放swiper里手指点到箭头拖动页面就不会整体移动，页面整体移动时animated设置的动画会失效 -->
			       <!--  <button class="up-arrow">
			            <i class="icon-angle-double-up"></i>
			        </button>
			    </div>
			</div>
			<img src="../static/images/imgH5/arrowUp.png" alt="" class="swiper-button-next arrow-up pt-page-moveIconUp"> -->
	</body>
	<!-- <script src="../static/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="../static/js/swiper.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="../static/js/swiper.animate1.0.2.min.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript">
			
		  var mySwiper = new Swiper ('.swiper-container', {
		  	direction: 'vertical',
		  	speed:200,
		  	momentumRatio:2,
		  	touchRatio:0.5,
		  	moveStartThreshold:'20px',
			loop: false,
			nextButton:'.swiper-button-next',
			lazyLoading:true,
			lazyLoadingInPrevNext:true,
			  onInit: function(swiper){ //Swiper2.x的初始化是onFirstInit
			    swiperAnimateCache(swiper); //隐藏动画元素 
			    swiperAnimate(swiper); //初始化完成开始动画
			  }, 
			  onSlideChangeEnd: function(swiper){ 
			    swiperAnimate(swiper); //每个slide切换结束时也运行当前slide动画
			  },
			  onTouchStart:function(){
			  	
//			  	if($(this).index()==1){
//			  		alert($(this).attr('class'))
//			  	}
			  }
			  
			  })
		  
		  
		</script> -->
</html>
