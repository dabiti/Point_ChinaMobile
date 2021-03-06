<%@page import="com.point.web.util.TimeUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<script type="text/javascript" src="<%=basePath %>/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/js/easyui-lang-zh_CN.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>找回密码</title>
<style>
body{font-family:'Microsoft YaHei';text-align: center;margin:auto;}
.vali_code_button{
	float:left;
	width:130px;
	height:42px;
	text-align: center; 
	vertical-align:middle; 
	background-image:url(<%=basePath%>images/passgetback/yanzhengma.png);
	margin-left:15px;
	cursor: pointer;
}
.vali_code_click_button{
	float:left;
	width:130px;
	height:42px;
	text-align: center; 
	vertical-align:middle; 
	background-image:url(<%=basePath%>images/passgetback/yanzhengma_click.png);
	margin-left:15px;
	line-height: 2.5;
}
.submit_button{
	width:302px;
	height:42px;
	background-image:url(<%=basePath%>images/passgetback/submit.png);
	border:0px solid #FF0000; 
	overflow:hidden;
	cursor: pointer;
}
.submit_click_button{
	width:302px;
	height:42px;
	background-image:url(<%=basePath%>images/passgetback/submit_click.png);
	border:0px solid #FF0000; 
	overflow:hidden;
}
.mask {       
    position: absolute; top: 0px; filter: alpha(opacity=60); background-color: #777;     
    z-index: 1002; left: 0px;     
    opacity:0.5; -moz-opacity:0.5;     
}  
</style>
<script type="text/javascript">
if (top.location != self.location){       
    top.location=self.location;       
}

$(function(){
	$('#submit_button_id').mousedown(function(){
		$(this).removeClass("submit_button").addClass("submit_click_button");
	}).mouseup(function(){
		$(this).removeClass("submit_click_button").addClass("submit_button");
    });
	
	/* $('#username').focus(function(){
		closeMsg();
		$("#username_parent_div").removeClass("login_blur_div").addClass("login_focus_div");
	}).blur(function(){
		$("#username_parent_div").removeClass("login_focus_div").addClass("login_blur_div");
    });
	
	$('#password').focus(function(){
		closeMsg();
		$("#password_parent_div").removeClass("login_blur_div").addClass("login_focus_div");
	}).blur(function(){
		$("#password_parent_div").removeClass("login_focus_div").addClass("login_blur_div");
    });
	
	*/
	
	$('#msgBoxButton').click(function(){
		closeMsg();
	});
	
	$('#com').bind("click",sendCode);
	
});

function showMsg(content){
	showMask();
	$('#msgBox').css('display','block');
	$('#msgContent').html(content);
}

function closeMsg(){
	hideMask();
	$('#msgBox').css('display','none');
	$('#msgContent').html('');
}

function showMask(){     
    $("#mask").css("height",$(document).height());     
    $("#mask").css("width",$(document).width());     
    $("#mask").show();     
}  
//隐藏遮罩层  
function hideMask(){     
    $("#mask").hide();     
}  

function validAccountRex(accountVal){
	reg=/^[a-z,A-Z][a-z,A-Z,0-9]{0,20}$/;  
	
    if(!reg.test(accountVal)){   
    	return false;
    } 
    return true;
}

//发送短信验证码
function sendCode(){
	if(!$.trim($('#account').val())){
		showMsg('用户名不能为空！');
		return;
	}
	if(!$.trim($('#phone').val())){
		showMsg('手机号不能为空！');
		return;
	}
    if(!validAccountRex($('#account').val())){   
    	showMsg("用户名格式错误");  
    	return;
    }  
	showMask();
	var url = "<%=basePath%>pass/sendCode";
	$.ajax({
        cache: false,
        type: "post",
        url:url,
        data:$('#passForm_ID').serialize(),
        async: false,
        error: function(request) {
        	hideMask();
        	showMsg(obj.msg);
        },
        success: function(data) {
        	hideMask();
            var obj = eval("("+data+")");
            showMsg(obj.msg);
            if(obj.code === '901'){
            	validcode_wait();
            }
        }
    });
}
//提交找回密码数据
function submitData(){
	if(!$.trim($('#account').val())){
		showMsg('用户名不能为空！');
		return;
	} 
	if(!$.trim($('#phone').val())){
		showMsg('手机号不能为空！');
		return;
	}
	
	if(!$.trim($('#validcode').val())){
		showMsg('验证码不能为空！');
		return;
	}
	
	showMask();
	var url = "<%=basePath%>pass/passGetsubmit";
	$.ajax({
        cache: false,
        type: "post",
        url:url,
        data:$('#passForm_ID').serialize(),
        async: false,
        error: function(request) {
        	hideMask();
        	showMsg(obj.msg);
        },
        success: function(data) {
        	hideMask();
            var obj = eval("("+data+")");
            if(obj.code === '901'){
            	$('#vpid').val(obj.data);
            	var form = document.getElementById('toPassForm_ID');
            	form.action = "<%=basePath%>pass/toPassNew";
            	form.submit();
            }else{
            	showMsg(obj.msg);
            }
        }
    });
}
function keyLogin(){
  /* if (event.keyCode==13)
     document.getElementById("login_button").click(); */
}

function validcode_wait(){
	var inter = parseInt('<%=TimeUtils.differCodeSendTimeMillis%>')/1000;
	var i = inter;
	var t = setInterval(function(){	
		if (i == 0) {	
			$("#com").removeClass("vali_code_click_button ").addClass("vali_code_button").html("");
			$('#com').bind("click",sendCode);
			clearInterval(t);
			return;	
		}	
		document.getElementById("com").innerHTML ='等待'+i+'秒';
		if( i == inter){
			$("#com").removeClass("vali_code_button").addClass("vali_code_click_button");
			$('#com').unbind("click");
		}
		i--;
	}, 1000);
}
//验证账户
function validAccount(){
	if($('#account').val()){
		if(!validAccountRex($('#account').val())){   
	    	showMsg("用户名格式错误");  
	    	return;
	    }
		showMask();
		var url = "<%=basePath%>pass/validAccount";
		$.ajax({
	        cache: false,
	        type: "post",
	        url:url,
	        data:{account:$('#account').val()},
	        async: false,
	        error: function(request) {
	        	hideMask();
	        	showMsg(obj.msg);
	        },
	        success: function(data) {
	        	hideMask();
	            var obj = eval("("+data+")");
	            if(obj.code === '901'){
	            	$('#phone').val(obj.data);
	            }else{
	            	showMsg(obj.msg);
	            	$('#phone').val('');
	            	$('#validcode').val('');
	            }
	        }
	    });
	}
}
function alertMsg(){
	var msg = '${msg}';
	if(msg){
		showMsg(msg);
	}
}
</script>
</head>
<body onload="alertMsg()">
<form id="toPassForm_ID" method="post">
	<input type="hidden" name="vpid" id="vpid"/>
</form>
<div id='mask' class="mask"></div>	
<dl id="msgBox" style="display:none;z-index:10000;width:200px;position:absolute;
background:#FFFFFF;border:1px solid #ccc;line-height:25px; top:50%; left:50%;">
  <dt id="idBoxHead" style="padding-left: 5px;background-color: #999999;color: white;"><b>提示</b> </dt>
  <dd style="text-align:left;">
    <br />
    <span id="msgContent"></span>
    <br /><br />
  </dd>
  <dt style="text-align:center;margin-bottom: 5px">
  	<input style="border:0px;width: 50px" type="button" value="确定 " id="msgBoxButton" />
  </dt>
</dl>
<form id="passForm_ID">
  <div style="height:768; width:1404; border:0px; background-color:;">
	    <table width="1404" border="0" cellpadding="0" cellspacing="0">
	      <tr>
	        <th scope="row" height="92" width="1404" style="background-image:url(<%=basePath%>images/passgetback/bg_top1.png)">&nbsp;</th>
	      </tr>
	      <tr>
        	<th scope="row" height="600" width="1404" style="background-image:url(<%=basePath%>images/passgetback/bg_gray.png)">
        	<div style="width: 1024; height: 462;margin: auto; text-align:center;background-color: ;">
		      <table width="1024" border="0" cellpadding="0" cellspacing="0">
		        <tr>
		          <th colspan="3" scope="row" height="138" width="1024" style="background-image:url(<%=basePath%>images/passgetback/bg_top.png); background-repeat:no-repeat;background-position: center;">&nbsp;</th>
		        </tr>
		        <tr>
		          <th rowspan="2" scope="row" height="324" width="315" style="background-image:url(<%=basePath%>images/passgetback/bg_left.png);background-repeat:no-repeat">&nbsp;</th>
		          <td height="249" width="395" style="background-color:#FFF;">
		          	<table width="0" border="0" cellspacing="0" cellpadding="0" style="height: 249px;width: 395px;">
		          	  <tr>
		          	    <th scope="row" style="color:#909090">用户名：</th>
		          	    <td><div style="width:302px;height:42px;text-align: center; vertical-align:middle; background-image: url(<%=basePath%>images/passgetback/yonghuming.png);">
		                <input type="text" onblur="validAccount()" name="account" id="account" style="margin: 1px;vertical-align:middle;font-size: 18px; border:0px solid;width:290px;height:36px;line-height:36px;"/>
		                </div></td>
		       	      </tr>
		          	  <tr>
		          	    <th scope="row" style="color:#909090">手机号：</th>
		          	    <td><div style="width:302px;height:42px;text-align: center; vertical-align:middle; background-image: url(<%=basePath%>images/passgetback/yonghuming.png);">
		                <input type="text" readonly='readonly' name="phone" id="phone" style="margin: 1px;vertical-align:middle;font-size: 18px; border:0px solid;width:290px;height:36px;line-height:36px;"/>
		                </div></td>
		       	      </tr>
		          	  <tr>
		          	    <th scope="row" style="color:#909090">验证码：</th>
		          	    <td>
			                <div style="background-color:;height:auto; margin:0 auto; background:;overflow:hidden;">
			                    <div style="text-align:center;width:156px;height:42px;background-image:url(<%=basePath%>images/passgetback/shurukuang.png);float:left; border:0px solid #FF0000; overflow:hidden;">
			                    	<input name="validcode" id="validcode" type="text" style="margin: 1px;vertical-align:middle;font-size: 18px; border:0px solid;width:145px;height:36px;line-height:36px;"/>
			                    </div>
			                    <div class="vali_code_button" id="com">                
			                    </div>
			                </div>
		                </td>
		       	      </tr>
		          	  <tr>
		          	    <th scope="row">&nbsp;</th>
		          	    <td><div class="submit_button" id="submit_button_id" onclick="submitData();"></div></td>
		       	      </tr>
			   	      </table>
		   	      </td>
		          <td rowspan="2" height="324" width="314" style="background-image:url(<%=basePath%>images/passgetback/bg_right.png);background-repeat:no-repeat" >&nbsp;</td>
		        </tr>
		        <tr>
		          <td height="75" width="395" style="background-image:url(<%=basePath%>images/passgetback/bg_bottom.png);background-repeat:no-repeat; ">&nbsp;</td>
		        </tr>
		      </table>
	    	</div>
		</th>
      </tr>
      <tr>
        <th scope="row" height="76" width="1404" style="background-image:url(<%=basePath%>images/passgetback/bg_bottom1.png);">&nbsp;</th>
      </tr>
    </table>
  </div>
</form>
</body>
</html>
