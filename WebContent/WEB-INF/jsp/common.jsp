<%@ page isELIgnored="false" %>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<link rel="stylesheet" type="text/css" href="<%=basePath %>/style/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>/style/icon.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>/style/demo.css">
<script type="text/javascript" src="<%=basePath %>/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=basePath %>/js/common.js"></script>