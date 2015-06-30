<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登陆页面</title>
<link href="style/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript">
	if (top.location != self.location){       
	    top.location=self.location;       
	} 
	function save1(){
		$('#loginForm').submit();
	}
	function clean(){
		if($('#message_box'))
		$('#message_box').css('display','none');
	}
</script>
</head>

<body onload="if(!$('#message_box')){$('#username').focus();}">
<div id="register_space">&nbsp;</div>
<div id="llogin">
	<form id="loginForm"  action="/Sinopec_Oil/login.do"  method="post">
  <div class="login_pic">LOGO
<!--   <img src="xx.jpg" />  -->
  </div>
  <div class="login_con">
    <div class="login_input">
      <ul>
     
        <li><span>账&nbsp;&nbsp;&nbsp;&nbsp;号：</span> <INPUT id="username"  name="username" class="login_name1 inputwidth2" type="text" onfocus="clean()"></li>
        <li><span>密&nbsp;&nbsp;&nbsp;&nbsp;码：</span> <INPUT id="password"  name="password"class="login_password1 inputwidth2" type="password" ></li>
        <c:if test="${message!=null && message!=''}">
	        <li id="message_box">
	        	<span>&nbsp;&nbsp;</span>
	        	<span style="color: red; width: 180px;">${message }</span>
	        </li>
        </c:if>
        <li>
        	<span>&nbsp;&nbsp;</span>
        	<INPUT class="login_btn" id="btn" style="cursor: default;color:#fff;width:190px;box-shadow: inset 0px 0px 1px rgba(0, 0, 0, 0.05), 0px 0px 8px rgba(217, 250, 217, 0.6);" type="button"  onclick="save1()"  value="登录" />
        	<div class="radio_div1">
          	</div>
        </li>
          <li></li>
      </ul>
    </div>
  </div>
  </form>
</div>
<div id="bottom_div">
  <div id="rightbq">
    <div class="rightcon">
      <ul>
        <li><A href="#">关于我们</A>&nbsp;&nbsp;|&nbsp;&nbsp;
  <A href="#">联系我们</A></li>
        <li>Copyright © 2015 sinopec</li>
      </ul>
    </div>
    <div class="rightcon1">中石化集团</div>
  </div>
</div>
</body>
</html>
