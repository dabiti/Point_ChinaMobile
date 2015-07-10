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
<title>无标题文档</title>
<style>
body{font-family:'Microsoft YaHei'; }
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
	})
	
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
function sendCode(){
	if(!$.trim($('#account').val())){
		showMsg('用户名不能为空！');
		return;
	}
	if(!$.trim($('#phone').val())){
		showMsg('手机号不能为空！');
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
            if(obj.code === '901'){
            	showMsg(obj.msg);
            	$('#pid').val(obj.data);
            }else{
            	showMsg(obj.msg);
            }
        }
    });
}
function submit(){
	if(!$.trim($('#account').val())){
		showMsg('用户名不能为空！');
		return;
	}
	if(!$.trim($('#phone').val())){
		showMsg('手机号不能为空！');
		return;
	}
	if(!$.trim($('#pid').val())){
		showMsg('请点击发送验证码！');
		return;
	}
	if(!$.trim($('#validcode').val())){
		showMsg('验证码不能为空！');
		return;
	}
	showMask();
	var url = "<%=basePath%>pass/passGet";
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
            	window.location.href='<%=basePath%>jumpToLoginView';
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

	
	
	$("#com").removeClass("vali_code_button").addClass("vali_code_click_button");
	
	
	var i = 10;
	var t = setInterval(function(){	
		if (i == -1) {	
			$("#com").removeClass("vali_code_click_button ").addClass("vali_code_button").html("");
			clearInterval(t);
		return;	
		}	
		document.getElementById("com").innerHTML ='等待'+i+'秒';
		i--;
		}
	, 1000)
	
	if(i==-1){
		$("#com").removeClass("vali_code_click_button ").addClass("vali_code_button");
	}
	
	
}
<<<<<<< HEAD:WebContent/WEB-INF/jsp/passGetBack.jsp


=======
//验证账户
function validAccount(){
	if($('#account').val()){
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
	            }
	        }
	    });
	}
}
>>>>>>> origin/master:WebContent/WEB-INF/jsp/pass/passGetback.jsp
</script>
</head>
<body>
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
    <div style="height:768; width:1404; border:0px; background-color:;text-align:center;position:absolute; z-index:2">
      <table width="1404" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <th scope="row" height="92" width="1404" style="background-image:url(<%=basePath%>images/passgetback/bg_top1.png)">&nbsp;</th>
        </tr>
        <tr>
          <th scope="row" height="600" width="1404" style="background-image:url(<%=basePath%>images/passgetback/bg_gray.png)">&nbsp;</th>
        </tr>
        <tr>
          <th scope="row" height="76" width="1404" style="background-image:url(<%=basePath%>images/passgetback/bg_bottom1.png);">&nbsp;</th>
        </tr>
      </table>
    </div>
    <div style="width: 1024; height: 462; background-color: ; text-align: center; position: absolute; z-index: 3; left: 188px; top: 182px">
      <table width="1024" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <th colspan="3" scope="row" height="138" width="1024" style="background-image:url(<%=basePath%>images/passgetback/bg_top.png); background-repeat:no-repeat;background-position: center;">&nbsp;</th>
        </tr>
        <tr>
          <th rowspan="2" scope="row" height="324" width="315" style="background-image:url(<%=basePath%>images/passgetback/bg_left.png);background-repeat:no-repeat">&nbsp;</th>
          <td height="249" width="395" style="background-color:#FFF;">
          
          	<table width="0" border="0" cellspacing="0" cellpadding="0" style="height: 249px;
width: 395px;">
          	  <tr>
          	    <th scope="row" style="color:#909090">用户名：</th>
          	    <td><div style="width:302px;height:42px;text-align: center; vertical-align:middle; background-image: url(<%=basePath%>images/passgetback/yonghuming.png);">
                <input type="text" onblur="validAccount()" name="account" id="account" style="margin: 1px;vertical-align:middle;font-size: 18px; border:0px solid;width:290px;height:36px;line-height:36px;"/>
                <input type="hidden" name="pid" id="pid"/>
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
<<<<<<< HEAD:WebContent/WEB-INF/jsp/passGetBack.jsp
	                    	<input type="text" name="validcode" style="margin: 1px;vertical-align:middle;font-size: 18px; border:0px solid;width:145px;height:36px;line-height:36px;"/>
	                    </div>
	                    <div class="vali_code_button" onclick="validcode_wait();" id="com">                
=======
	                    	<input name="validcode" id="validcode" type="text" style="margin: 1px;vertical-align:middle;font-size: 18px; border:0px solid;width:145px;height:36px;line-height:36px;"/>
	                    </div>
	                    <div class="vali_code_button" onclick="sendCode();">                
>>>>>>> origin/master:WebContent/WEB-INF/jsp/pass/passGetback.jsp
	                    </div>
	                </div>
                </td>
       	      </tr>
          	  <tr>
          	    <th scope="row">&nbsp;</th>
          	    <td><div class="submit_button" id="submit_button_id" onclick="submit();"></div></td>
       	      </tr>
   	      </table></td>
          <td rowspan="2" height="324" width="314" style="background-image:url(<%=basePath%>images/passgetback/bg_right.png);background-repeat:no-repeat" >&nbsp;</td>
        </tr>
        <tr>
          <td height="75" width="395" style="background-image:url(<%=basePath%>images/passgetback/bg_bottom.png);background-repeat:no-repeat; ">&nbsp;</td>
        </tr>
      </table>
    </div>
</div>
</form>
</body>
</html>
