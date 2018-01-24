<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<script type="text/javascript" src="../static/js/jquery.min.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="../static/js/voice/Math.uuid.js"></script>
<script type="text/javascript" src="../static/js/voice/Code.js"></script>
<script type="text/javascript" src="../static/js/voice/wechatJs.js"></script>
<script type="text/javascript">
	var serverId = "${serverId}";
    $(function() {
    	var url = window.location.href;
 		var b = new Base64();
 		url = b.encode(url);
 		alert(url);
 		hafuConfig(url);
	}); 
    
    var localId;
    function start(){
    	wx.startRecord();
    }
    
    function end(){
    	wx.stopRecord({
    	    success: function (res) {
    	       localId = res.localId;
    	    }
    	});
    }
    
    function playVoice1(){
    	//下载语音
        wx.downloadVoice({
            serverId: serverId, // 需要下载的音频的服务器端ID，由uploadVoice接口获得
            isShowProgressTips: 1, // 默认为1，显示进度提示
            success: function (res) {
                var localId1 = res.localId; // 返回音频的本地ID
                wx.playVoice({
            	    localId: localId1
            	});
            }
        });
    }
    
    //播放录制的语音
    function playVoice(){
    	var serverId1 = "";
    	wx.uploadVoice({
            localId: localId,  // 需要上传的音频的本地ID，由stopRecord接口获得
            isShowProgressTips: 1, // 默认为1，显示进度提示
                success: function (res) {
                serverId1 = res.serverId; // 返回音频的服务器端ID
            }
        });
    	
    	alert("上传语音ServerId = "+serverId1);
    	
    	wx.playVoice({
    	    localId: localId
    	});
    	
    	wx.translateVoice({
 		   localId: localId, // 需要识别的音频的本地Id，由录音相关接口获得
 		    isShowProgressTips: 1, // 默认为1，显示进度提示
 		    success: function (res) {
 		        alert("语音识别结果 ： "+res.translateResult); // 语音识别的结果
 		 }
 	});
    	
    }
    
    function pauseVoice(){
    	wx.pauseVoice({
            localId: localId
        });
    }
    
    
    
</script>
<body>
		<input type="button" onclick="start()" value="开始">
		<input type="button" onclick="end()" value="结束">
		<input type="button" onclick="playVoice()" value="播放">
		<input type="button" onclick="pauseVoice()" value="暂停">
		<input type="button" onclick="playVoice1()" value="播放已有数据">
</body>
</html>