<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登陆页面</title>
<style>
body{text-align:center}
.login_input{  
background-position: 5px 6px;  
background-repeat:no-repeat;  
font-size: 18px;
border: 0px solid #95B8E7;
border-color:blue;
padding-left:35px; 
padding-top:10px; 
padding-bottom:10px;
background-image:url(<%=basePath%>images/login/icon_enter_03.png);   
width:280px;
height:39px;
-moz-border-radius: 5px 5px 5px 5px;
-webkit-border-radius: 5px 5px 5px 5px;
border-radius: 5px 5px 5px 5px;
}
.login_pass_input{  
background-position: 5px 6px;  
background-repeat:no-repeat;  
font-size: 18px;
border: 0px;
border-color:blue;
padding-left:35px; 
padding-top:10px; 
padding-bottom:10px;
background-image:url(<%=basePath%>images/login/icon_password_03.png);   
width:280px;
height:39px;
-moz-border-radius: 5px 5px 5px 5px;
-webkit-border-radius: 5px 5px 5px 5px;
border-radius: 5px 5px 5px 5px;
}
.login_focus_div{
height:42px;
padding:1px;
background-repeat:no-repeat; 
background-image:url(<%=basePath%>images/login/input_box_click.png);
}
.login_blur_div{
height:42px;
padding:1px;
background-repeat:no-repeat; 
background-image:url(<%=basePath%>images/login/input_box.png);
}

.login_button_click_div{
height:42px;
padding:1px;
background-repeat:no-repeat; 
background-image:url(<%=basePath%>images/login/button_enter_click_1.png);
cursor: pointer;
}

.login_button_div{
height:42px;
padding:1px;
background-repeat:no-repeat; 
background-image:url(<%=basePath%>images/login/button_enter_1.png);
cursor: pointer;
}

.login_space_div{
height:32px;
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
		$('#login_button').mousedown(function(){
			$(this).removeClass("login_button_div").addClass("login_button_click_div");
		}).mouseup(function(){
			$(this).removeClass("login_button_click_div").addClass("login_button_div");
        });
		
		$('#username').focus(function(){
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
	
	function login(){
		if(!$.trim($('#username').val())){
			showMsg('用户名不能为空！');
			return;
		}
		if(!$.trim($('#password').val())){
			showMsg('密码不能为空！');
			return;
		}
		showMask();
		var url = "<%=basePath%>login/login";
		$.ajax({
            cache: false,
            type: "post",
            url:url,
            data:$('#loginForm').serialize(),
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
	  if (event.keyCode==13)
	     document.getElementById("login_button").click();
	}
</script>
</head>

<body onkeydown="keyLogin();">
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
<form id="loginForm">
<table width="1404" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <th colspan="4" scope="row" height="92" width="1404" style="background-image:url(<%=basePath%>images/login/bg_top.png);">&nbsp;</th>
  </tr>
  <tr>
    <th rowspan="3" scope="row" height="600" width="878" style="background-image:url(<%=basePath%>images/login/bg_left.png);"><img src="<%=basePath%>images/login/img.png" /></th>
    <td rowspan="2" width="28" height="426" style="background-image:url(<%=basePath%>images/login/bg_01.png);">&nbsp;</td>
    <td width="283" height="205" style="background-image:url(<%=basePath%>images/login/bg_top_01.png);">&nbsp;</td>
    <td rowspan="2" width="215" height="426" style="background-image:url(<%=basePath%>images/login/bg_right.png);">&nbsp;</td>
  </tr>
  <tr>
    <td width="283" height="220" >
    <div style="height:200px;text-align:center">
    	<div class="login_blur_div" id="username_parent_div">
    		<input class="login_input" id="username" name="username"/>
    	</div>
    	
    	<div class="login_space_div"></div>
    	
    	<div class="login_blur_div" id="password_parent_div">
    		<input class="login_pass_input" type="password" id="password" name="password"/>
    	</div>
    	
    	<div class="login_space_div"></div>
    	
    	<div class="login_button_div" id="login_button" onclick="login()">
    		
    	</div>
    </div>
    	
    </td>
  </tr>
  <tr>
    <td colspan="3" width="526" height="174" id="anc" style="background-image:url(<%=basePath%>images/login/bg_bottom_01.png);">&nbsp;</td>
  </tr>
  <tr>
    <th colspan="4" scope="row" height="76" width="1404" style="background-image:url(<%=basePath%>images/login/bg_bottom.png);">&nbsp;</th>
  </tr>
</table>
</form>
</body>
</html>
