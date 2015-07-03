<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "hkp://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
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
/* 			background-position:0px 0px; */
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
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script>
		//设置所有菜单初始为关闭
		$(function(){
			$('#MenuGroup').accordion('getSelected').panel('collapse');
		});	
	
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
				var content = '<iframe  scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%"></iframe>';
				$('#tt').tabs('add',{
					title:title,
					content:content,
					closable:true
				});
			}
		}
		
		function zhux(){
			window.location.href="<%=request.getContextPath()%>/logout.do";
		}

	</script>
</head>
<body class="easyui-layout" style="margin:0 1px;" scroll="no">
		<!-- 头部 -->
		<div data-options="region:'north',split:true" style="height:50px;background-color:#B9DCEF;overflow:hidden;" class="Noprint">
			<div class="easyui-layout" style="width:25%;height:0px;position:absolute;right:0px;bottom:-2px" align="right">
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="zhux()">注销</a>
			</div>
		</div>
		
		<div data-options="region:'south',split:true" style="height:5px;"></div>

		<div data-options="region:'west',split:true" id="Content"  title="导航窗格" style="width:12%;" class="Noprint">
			<div id="MenuGroup" class="easyui-accordion" data-options="multiple:false" fit="true" border="false">
					<div title="测试" data-options="iconCls:'icon-ok'" icon="icon-sys" style="overflow:auto;">
						<ul style="padding:0;margin:4px 0 0 0;">
							<li>
								<a  onclick="addTab('上传测试','progressBarView.do')"><span class="nav nav-checkaccount"></span>上传测试</a>
							</li>
						</ul>
					</div>
					<div title="渠道商管理" data-options="iconCls:'icon-ok'" icon="icon-sys" style="overflow:auto;">
						<ul style="padding:0;margin:4px 0 0 0;">
							<li>
								<a  onclick="addTab('渠道商资料','toChannelview.do')"><span class="nav nav-checkaccount"></span>渠道商资料</a>
							</li>
						</ul>
					</div>
			</div>
		</div>
		<div data-options="region:'center',iconCls:'icon-ok'" style="margin:0;padding:0;overflow-y:hidden">
			<div id="tt" class="easyui-tabs" fit="true" style="width:100%;height:100%;margin:0;padding:0;" border="0" id="tabs">
      			<div title="首页"></div>
   			</div>
		</div>
</body>
</html>