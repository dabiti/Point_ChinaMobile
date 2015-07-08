<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
body{text-align:center;font-family:'Microsoft YaHei'; }
</style>
<script type="text/javascript">
function validcode_wait(com){
	$(com).css('background-image','');
}
</script>
</head>
<body>
<div style="height:768; width:1404; border:0px; background-color:;text-align:center">
    <div style="height:768; width:1404; border:0px; background-color:;text-align:center;position:absolute; z-index:1">
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
    <div style="width: 1024; height: 462; background-color: ; text-align: center; position: absolute; z-index: 2; left: 188px; top: 182px">
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
                <input type="text" style="margin: 1px;vertical-align:middle;font-size: 18px; border:0px solid;width:290px;height:36px;line-height:36px;"/>
                </div></td>
       	      </tr>
          	  <tr>
          	    <th scope="row" style="color:#909090">手机号：</th>
          	    <td><div style="width:302px;height:42px;text-align: center; vertical-align:middle; background-image: url(<%=basePath%>images/passgetback/yonghuming.png);">
                <input type="text" style="margin: 1px;vertical-align:middle;font-size: 18px; border:0px solid;width:290px;height:36px;line-height:36px;"/>
                </div></td>
       	      </tr>
          	  <tr>
          	    <th scope="row" style="color:#909090">验证码：</th>
          	    <td>
	                <div style="background-color:;height:auto; margin:0 auto; background:;overflow:hidden;">
	                    <div style="text-align:center;width:156px;height:42px;background-image:url(<%=basePath%>images/passgetback/shurukuang.png);float:left; border:0px solid #FF0000; overflow:hidden;">
	                    	<input type="text" style="margin: 1px;vertical-align:middle;font-size: 18px; border:0px solid;width:145px;height:36px;line-height:36px;"/>
	                    </div>
	                    <div style="float:left;;width:130px;height:42px;text-align: center; vertical-align:middle; background-image:url(<%=basePath%>images/passgetback/yanzhengma.png);margin-left:15px;cursor: pointer;" onclick="validcode_wait(this);">                
	                    </div>
	                </div>
                </td>
       	      </tr>
          	  <tr>
          	    <th scope="row">&nbsp;</th>
          	    <td><div style="width:302px;height:42px;background-image:url(<%=basePath%>images/passgetback/submit.png);border:0px solid #FF0000; overflow:hidden;"></div></td>
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
</body>
</html>
