var appid;
// JS config 配置
function hafuConfig(url) {
	$.ajax({
		type : 'post',
		url : '../wechatjs/jsConfig',
		data : '&url=' + url,
		dataType : 'json',
		success : function(data) {
			var map = eval(data);
			appid = map.appid;
			wx.config({
				debug : false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
				appId : map.appid, // 必填，公众号的唯一标识
				timestamp : map.timestamp, // 必填，生成签名的时间戳
				nonceStr : map.nonceStr, // 必填，生成签名的随机串
				signature : map.signature,// 必填，签名，见附录1
				jsApiList : [ 'onMenuShareTimeline', 'onMenuShareAppMessage',
						'onMenuShareQQ', 'onMenuShareWeibo', 'startRecord',
						'stopRecord', 'onVoiceRecordEnd', 'playVoice',
						'pauseVoice', 'stopVoice', 'onVoicePlayEnd',
						'uploadVoice', 'downloadVoice', 'chooseImage',
						'previewImage', 'uploadImage', 'downloadImage',
						'translateVoice', 'getNetworkType', 'openLocation',
						'getLocation', 'hideOptionMenu', 'showOptionMenu',
						'hideMenuItems', 'showMenuItems',
						'hideAllNonBaseMenuItem', 'showAllNonBaseMenuItem',
						'closeWindow', 'scanQRCode', 'chooseWXPay',
						'openProductSpecificView', 'addCard', 'chooseCard',
						'openCard' ]
			// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
			});
		}
	});
}
