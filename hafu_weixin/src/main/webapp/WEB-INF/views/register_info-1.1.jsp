<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
<title>用户基本信息</title>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<jsp:include page="css2.jsp"></jsp:include>
<script src="../static/js/Base.js"></script>
<script type="text/javascript">
	Base.alert("您还未绑定设备，请立即绑定！");
	function addContact(){
		$(".divStyle5.emergency-contact:first").clone().insertBefore("#form .addLXman");
		$(".divStyle5.emergency-contact:last input.emerge-name").val("");
		$(".divStyle5.emergency-contact:last input.emerge-phone").val("");
		deleteContact();
	}
	function deleteContact(){
		$(".div_set_right").off("click");
		$(".div_set_right").bind("click",function(){
			if($(".divStyle5.emergency-contact").length>1){
				$(this).parents(".emergency-contact").remove();
			}
		})
	}
	deleteContact();
</script>
</head>
<body>
<div class="container containerStyle1">
	<%-- <c:import url="index/left_slide.jsp"></c:import> --%>
	<!--button class="menu-button" id="open-button">打开</button-->
	<div class="content-wrap">
		<!--新添加的绑定的东西   start-->
	  	<div class="step">
				<div class="a1">
					<img src="../static/images/imgStep/step1.png" />
				</div>
				<ul class="txt">
					<li class="taL">父母信息</li>
					<li class="taC">子女联系方式</li>
					<li class="taR">绑定设备</li>
				</ul>
				<div style="clear: both;"></div>
			</div>
		
		<form action="/register/saveInfo" method="post" id="form">
			
			<div class="content mgT1">
			  	
				<div class="divStyle5 fieldset parent">
				<legend>父母信息</legend>
					<div class="clear bo1">
						<div class="left5em">关注对象<span class="redStar">*</span></div>
						<div class="inputLeft5em">
							<input type="text" value="${user.realName}" name="realName" class="myInput2">
						</div>
					</div>
					<div class="clear bo1">
						<div class="left5em">性&#12288;别</div>
						<div class="inputLeft5em">
							<!--<select class="myInput2" style="" name="gender" value="${user.gender}">
								<option value="">请选择</option>
								<option value="男" ${user.gender=='男'?'selected':'' }>男</option>
								<option value="女" ${user.gender=='女'?'selected':'' }>女</option>
							</select>-->
						<span class="mgR10">
							<input type="radio" name="gender" value="男" checked="checked" ${user.gender=='男'?'selected':'' }/> 男 <i class="man"></i>
						</span>
                    	 <span>
                    	 	<input type="radio" name="gender" value="女" ${user.gender=='女'?'selected':'' }/> 女 <i class="woman"></i>
                    	 </span>
						</div>
					</div>
					<!-- <div class="clear bo1">
						<div class="left5em">年&#12288;龄</div>
						<div class="inputLeft5em">
							<input type="text" value="${user.age }" name="age" class="myInput2">
						</div>
					</div> -->
                  
					<div class="clear bo1">
						<div class="left5em">身份证<span class="redStar">*</span></div>
						<div class="inputLeft5em">
							<input type="text" value="${user.card}" name="card" class="myInput2">
						</div>
					</div>
					<div class="clear bo1">
                    <div class="left5em">生&#12288;日<span class="redStar"></span></div>
                    <div class="inputLeft5em">
                    	<input type="text" id="birthday" name="birthday" class="myInput" placeholder="请点击选择您的生日">
                    	</div>
                  </div>
					<div class="clear">
						<div class="left5em">手&#12288;机<span class="redStar">*</span></div>
						<div class="inputLeft5em">
							<input type="text" value="${user.mobile }" name="mobile" class="myInput2">
						</div>
					</div>
					<!--<div class="clear bo1">
						<div class="left" style="width: 20%;">地址</div>
						<div class="right" id="element_id" style="display: inline;width: 80%;">
							<select class="province myInput" name="province" data-value="${user.province }" style="display: inline;width: 33%;" data-first_title="选择省"></select>&nbsp;&nbsp;
							<select class="city myInput" name="city" data-value="${user.city}" data-first_title="选择省" style="display: inline;width: 33%;"></select>&nbsp;&nbsp;
							<select class="area myInput" name="area" data-value="${user.area}" data-first_title="选择省" style="display: inline;width: 20%;"></select>
						</div>
					</div>
					<div class="clear">
						<div class="left5em">详细地址</div>
						<div class="inputLeft5em">
							<input type="text" value="${user.address}" name="address" class="myInput2">
						</div>
					</div>-->
				</div>
				<input name="contactPerson" type="hidden"/>
				<div class="divStyle5 emergency-contact fieldset childs">
					<legend>子女联系方式</legend>
						<div class="clear bo1">
							<div class="left0">
								紧急联系人<span class="redStar">*</span>
							</div>
							<div class="div_set_right"><img src="../static/images/ico_del.png"></div>
							<div class="inputLeft2">
								<input type="text" class="myInput2 emerge-name" style="" placeholder="非被关注人">
							</div>
						</div>
						<div class="clear bo1">
							<div class="left0">
								紧急联系电话<span class="redStar">*</span>
							</div>
							<div class="inputLeft1">
								<input type="text" class="myInput2 emerge-phone">
							</div>
							
						</div>
						<div class="clear">
	                    <div class="left0">关&nbsp;&nbsp;&nbsp;&nbsp;系<span class="redStar">*</span></div>
	                    <div class="inputLeft5em">
	                      <select name="relations" class="relations">
	                      </select>
	                    </div>
	                  </div>
					</div>
	                  
	                </div>
						<div class="addLXman">
							<a href="javascript:addContact()"> <strong style="font-size: 18px;">+</strong> 添加紧急联系人</a>
						</div>
					<!--加一个绑定设备，非必填(改版时再添加，目前不增加)-->
                <div class="botCon">
                	<div class="item clear">
							<div class="div_set_left div_set_title"  onClick="saoyisao()"><img src="/static/images/ico_addDevice.png"><br/><i>（扫一扫）</i></div>
							<div class="div_set_left3 div_set_add" style="line-height:3.5em;">
							  	<input type="text" class="myInput" name="code" id="decode" placeholder="请输入设备号" style="background-color:transparent;">
							</div>
							<%-- <div class="div_set_right" onclick="tijiaoValid('${user.id}',this);">
							<!-- <img src="/static/images/ico_ico7_hui.png" ><div>添加</div> -->
							<button style="background-color: rgb(204, 204, 204);">添加</button>
							<!-- <button style="background-color: rgb(255, 170, 37);">添加</button> -->
							</div> --%>
	           	        </div>
                
	       	      <p class="tip">扫一扫包装盒背后的二维码或输入盒子后面的设备号</p>
       	        </div>
				<div class="pagePadding">
					<div class="mySubmit1" style="cursor: pointer;" id="submit">确定</div>
				</div>
			</div>
		</form>
	</div>
	<div id="datePlugin"></div>
	<!-- /content-wrap -->
	<script src="/static/js/classie.js"></script>
	<script src="/static/js/jquery.min.js"></script>
	<script src="/static/js/bootstrap.min.js"></script>
	<script src="/static/js/Constans.js"></script>
	<script type="text/javascript" src="/static/js/select/jquery.cxselect.js"></script>
    <script type="text/javascript" src="/static/js/date.js" ></script>
    <script type="text/javascript" src="/static/js/iscroll.js" ></script>
	
<script type="text/javascript">
	$('#birthday').date();
	var options="";
	$(document).ready(function(){
		var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/; 
		//添加关系
		var relations = ['爸爸','妈妈','爷爷','奶奶','外公','外婆','其他'];
		
		for(var m=0;m<relations.length;m++){
			var options = "<option value='"+relations[m]+"'>"+relations[m]+"</option>" ;
			$('.relations').append(options);
		}
		//当用户选择其他关系的时候出现一个输入框
		$('.relations').on('change',function(){
			   if($(this).val()=='其他'){
			   	var $input=$('<input type="text" class="otherRelation" />');
			   	$(this).parent().append($input);
			   }else{
			   	$(this).parent().find('input').remove();
			   }
		})
		$('#element_id').cxSelect({
			url: '../static/js/select/cityData.json',               // 如果服务器不支持 .json 类型文件，请将文件改为 .js 文件
		  	selects: ['province', 'city', 'area'],  // 数组，请注意顺序
		  	emptyStyle: 'hidden'
		});	
		var fl = false;
		$("input[name=card]").blur(function(){
			var card = $("input[name=card]").val();
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
		$("#submit").click(function(){
			if(fl) return ;
			Base.open();
			var realName = $("input[name=realName]").val();
			if(realName==null || $.trim(realName)==''){
				Base.close();
				Base.alert("请填写姓名!");
				return false;
			}
			if($.trim(realName).length>10){
				Base.close();
				Base.alert("姓名长度10字以内");
				return false;
			}
			var card = $("input[name=card]").val();
			if(card==null || card==''){
				Base.close();
				Base.alert("请输入身份证号");
				return false;
			}
			 
		    if(reg.test(card) === false){
		    	
		    	Base.close();
		    	Base.alert("身份证号输入不合法");
		        return  false;  
		    }
			var birthday = $("#birthday").val();
			if(birthday==null || birthday==''){
				Base.close();
				Base.alert("请输入生日");
				return false;
			}
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
			var mobile = $("input[name=mobile]").val();
			if(mobile==null || mobile==''){
				Base.close();
				Base.alert("请输入电话");
				return false;
			}else{
				if(/^(\d{11})$/.test(mobile)){
					
				}else{
					Base.close();
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
			fl =true;
			$(this).css("background","gray");
			$.ajax({
				url:'saveInfo',
				data:$('#form').serialize(),
				type:"POST",
				dataType: "json",
				success:function(data){
					Base.close();
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
		//根据用户填写的进度改变头部的图片
			//存储不同状态下头部的背景图片地址
    		var imgs=['../static/images/imgStep/step1.png','../static/images/imgStep/step2.png','../static/images/imgStep/step3.png'];
    		//存储第一步的背景图片
    		var src=imgs[0];
    		var num;
    		//判断父母信息填满
    		$('.parent input[type=text]').on('blur',function(){
    			 num=0;
    				$.each($('.parent input[type=text]'),function(){
    							if($(this).val() !='' ){
    								num += 1;
    							}else{
    								num = num;
    							};
    				   });
	    			if(num == $('.parent input[type=text]').length){ 
	    					$('.step .a1 img').attr('src',imgs[1]);
	    			}else{
	    					$('.step .a1 img').attr('src',imgs[0]);
	    			};
	    			src = $('.step .a1 img').attr('src');
	    	});
    			//判断子女联系方式最少一个填满
    			$('.content-wrap').on('blur','.childs input[type=text]',function(){
    				 $.each($('.childs'),function(){
    				  var num2 = 0;
    				  //得到这个子女的信息填写的是否完整
    					 $.each($(this).find('input[type=text]'),function(){
    						
    							if($(this).val() !='' ){
    								num2 += 1;
    							}else{
    								num2 = num2;
    							};
    				   });
		    			if(num2 == $(this).find('input[type=text]').length && num == $('.parent input[type=text]').length){ 
		    				$('.step .a1 img').attr('src',imgs[2]);
		    				return false;
		    			}else{
		    				$('.step .a1 img').attr('src',src);
		    			};
    				
    			  });
    				 
    		  });
    			//调用扫一扫之前必须先配置参数
    			/*$.getJSON("index/jsTicket",{"url":window.location.href},function(data){
					alert(1);
    				wx.config({
    				    debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    				    appId: data.appId, // 必填，公众号的唯一标识
    				    timestamp:parseInt(data.timestamp,10), // 必填，生成签名的时间戳
    				    nonceStr: data.nonceStr, // 必填，生成签名的随机串
    				    signature: data.signature,// 必填，签名，见附录1
    				    jsApiList: ['scanQRCode'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    				});
    				wx.error(function(res){
    					alert("无法扫描");
    				});
    				wx.ready(function(){
    					
    				});
    			});*/
    	//调用扫一扫之前必须先配置参数
		wx.config({
	    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
	    appId: '${appId}', // 必填，公众号的唯一标识
	    timestamp:parseInt("${timestamp}",10), // 必填，生成签名的时间戳
	    nonceStr: '${nonceStr}', // 必填，生成签名的随机串
	    signature: '${signature}',// 必填，签名，见附录1
	    jsApiList: ['scanQRCode'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	});
		
	});

//扫一扫
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
		/*function tijiaoValid(id,e){
			var code = $("#decode_"+id).val();
			if(code == null || code==''){
				Base.alert("请输入设备号");
				return false;
			}
			$.getJSON("/set/isbelong",$('#form_add_'+id).serialize(),function(data){
				if(data.status=='success'){
					if(data.msg=="section1_true"){
						Base.confirm({
							msg:"持有人已绑定过设备，是否立即同步？",
							yes:function(){
								$("#decode_"+id).val(data.ob.code);
								tijiao(id,e);
							}
						});
					}else if(data.msg=="section1_false"){
						Base.alert("抱歉,当前持有人已经绑定过设备！");
						return;
					}else if(data.msg=="false"){
						Base.confirm({
							msg:"该设备已有亲友注册关注，是否添加到被关注人列表？",
							yes:function(){
								tijiao(id,e);
							}
						});
					}else{
						tijiao(id,e);
					}
				}else{
					Base.alert(data.msg);
				}
			});
		}
		function tijiao(id,e){
			var index = Base.open();
			var code = $("#decode_"+id).val();
			if(code == null || code==''){
				Base.close(index);
				Base.alert("请输入设备号");
				return false;
			}
			$(e).children("img").attr("src","/static/images/ico_ico7.png");
			$.ajax({
				url:'/index/ajaxAddDevice',
				data:$('#form_add_'+id).serialize(),
				type:"POST",
				dataType: "json",
				success:function(data){
					Base.close(index);
					$(e).children("img").attr("src","/static/images/ico_ico7_hui.png");
					if(data.status=='success'){
						Base.alert({
							msg:"绑定成功",
							end:function(){
								window.location.reload();
							}
						});
						var html = '<div class="item bo3 clear">'
							+'<div class="div_set_left div_set_title">设备<i>（行为记录仪）</i></div>'
							+'<div class="div_set_left3"><div>#CODE</div></div>'
							+'<div class="div_set_right" onclick="deleteDevice(#ID,this);" style="cursor: pointer;"><img src="/static/images/ico_del.png" ></div>'
							+'</div>';
						html = html.replace("#ID",data.ob.id).replace("#CODE",data.ob.code);
						$(e).parent().before(html);
						$("#decode_"+id).val(""); 
					}else{
						Base.alert(data.msg);
					}
				},error:function(){Base.close(index);}
			});
		}*/
</script>
</div>
</body>
</html>