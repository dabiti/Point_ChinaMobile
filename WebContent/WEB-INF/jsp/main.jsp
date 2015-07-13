<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "hkp://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>主页面</title>
	<link rel="stylesheet" type="text/css" href="style/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="style/icon.css">
	<link rel="stylesheet" type="text/css" href="style/demo.css">
	<style   media=print>  
    .Noprint{display:none;}  
    .PageNext{page-break-after:   always;}  
	</style>  
	<style type="text/css">
		ul li{
 			list-style:none;
 			height: 30px;
		}
		
		ul li a{
			display: block;
		    text-decoration:none;
			border-bottom: 1px solid #e6e6e6;
		    border-top: 1px solid #fff;
		    color: #666;
		    padding:5px 7px 5px 25px;
		} 
		.nav{
			margin-bottom:-7px;
  			padding:2px 9px;
			width:20px;
			background-image:url(style/images/tabicons.png);
		}
		
		.nav-user{
 			background-position:-100px -480px;
		}
		.nav-role{
			background-position:-360px -200px;
		}
		.nav-org{
			background-position:-280px 0;
		}
		.nav-holi{
 			background-position:-100px -80px;
		}
		.nav-set{
 			background-position:-20px -100px;
		}
		.nav-exch{
 			background-position:-260px 0;
		}
		.nav-seq{
 			background-position:-120px -100px;
		}
		.nav-checkaccount{
 			background-position:-240px -80px;
		}
		
	</style>
	
	
	<style media="print" type="text/css">
	  .Noprint{display:none;}   
	  .PageNext{page-break-after: always;}  
	</style> 
	<script>
		//设置所有菜单初始为关闭
		$(function(){
			$('#MenuGroup').accordion('getSelected').panel('collapse');
			hoverMenuItem();
		});
		
		function hoverMenuItem() {
		    $(".easyui-accordion").find('a').hover(function() {
		        $(this).parent().css("cursor","pointer");
		    }, function() {
		        $(this).parent().removeClass("hover");
		    });
		}
	
		function onSearch(){
			$('#dg').datagrid(
			'load',{
			userid:	$('#userid').val()
			});
		}
		//添加标签页
		function addTab(title, url){
			if ($('#tt').tabs('exists', title)){
				$('#tt').tabs('select', title);
			} else {
				var content = '<iframe  scrolling="auto" frameborder="0"  src="<%=basePath%>'+url+'" style="width:100%;height:100%"></iframe>';
				$('#tt').tabs('add',{
					title:title,
					content:content,
					closable:true
				});
			}
		}
		
		function logout(){
			window.location.href="<%=basePath%>login/logout";
		}
	</script>
</head>
<body class="easyui-layout" style="margin:0 1px;" scroll="no">
		<!-- 头部 -->
		<div data-options="region:'north',split:true" style="height:90px;background-color:#B9DCEF;overflow:hidden;" class="Noprint">

			<img alt="" src="<%=basePath%>images/main/bg_nav.png" width='100%'>



			<div style="z-index:100;height:50px;width:100px;background-image:<%=basePath%>images/main/bg_nav.png"></div>
			<div class="easyui-layout" style="width:25%;height:0px;position:absolute;right:0px;bottom:-2px" align="right">
				<a href="#" class="easyui-linkbutton" iconCls="icon-logout" plain="true" onclick="logout()">注销</a>
			</div>
		</div>
		
		<div data-options="region:'south',split:true" style="height:5px;"></div>

		<div data-options="region:'west',split:true" id="Content"  title="导航窗格" style="width:12%;" class="Noprint">
			${menu}
		</div>
		
		<div data-options="region:'center',iconCls:'icon-ok'" style="margin:0;padding:0;overflow-y:hidden">
			<div id="tt" class="easyui-tabs" fit="true" style="width:100%;height:100%;margin:0;padding:0;background-image:" border="0" id="tabs">
      			<div title="首页">
      				<div style="width: 100%;height: 100%;text-align: center;">
      					<img alt="" src="<%=basePath%>images/main/bg_img.png" style="margin-top: 10%">
      				</div>
      			</div>
   			</div>
		</div>
</body>
</html>
