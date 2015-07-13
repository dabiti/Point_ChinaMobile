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
<title>信息</title>
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

function returnIndex(){
	window.location.href='<%=basePath%>';
}
</script>
</head>
<body >
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
<form id="passForm_ID" method="post">
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
		          <th colspan="3" scope="row" height="138" width="1024" style="background-color:white;">&nbsp;</th>
		        </tr>
		        <tr>
		          <th rowspan="2" scope="row" height="324" width="315" style="background-color:white;">&nbsp;</th>
		          <td height="249" width="395" style="background-color:#FFF;">
		          	<table width="0" border="0" cellspacing="0" cellpadding="0" style="height: 249px;width: 395px;">
		          	  <tr>
		          	  	<td style="text-align:center;">
		          	  		<div>${msg}</div> 
		          	  		<div onclick="returnIndex();" style="font-size: 12px;color:#F90;margin-top:15px;cursor: pointer;">返回首页</div>
		          	  	</td>
		       	      </tr>
		       	      <tr>
		          	  	<td style="text-align:center;">
		          	  	&nbsp;
		          	  	</td>
		       	      </tr>
			   	      </table>
		   	      </td>
		          <td rowspan="2" height="324" width="314" style="background-color:white;" >&nbsp;</td>
		        </tr>
		        <tr>
		          <td height="75" width="395" style="background-color:white;">&nbsp;</td>
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
