<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<%@include file="resources.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "hkp://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>主页面</title>	
	<script>
		var startDate;
		var endDate;
		$(function(){
			$('#orderTable').datagrid({
				onLoadSuccess:function(data){
					$('#mytotal').html(eval(data).mytotal);
				}
			})
			$('#startdate').datebox({
				onSelect: function(date){
					var y = date.getFullYear();
					var m = date.getMonth()+1;
					var d = date.getDate();
					startDate = y+'/'+m+'/'+d;
					if(endDate != null)
						comptime(startDate,endDate);
					console.log('start: '+startDate);
				}
			})
			$('#enddate').datebox({
				onSelect: function(date){
					var y = date.getFullYear();
					var m = date.getMonth()+1;
					var d = date.getDate();
					endDate = y+'/'+m+'/'+d;
					if(startDate != null)
						comptime(startDate,endDate);
					console.log('end: '+endDate);
				}
			});
		})
		function searchData(){
			$('#orderTable').datagrid('load',{
				title: $('#title').val(),
				phone: $('#phone').val(),
				startdate: $('#startdate').datebox("getValue")
			});
		}
		function openWin(){
			$('#win').window('open');  
		}
		function closeWin(){
			$('#win').window('close');  
		}
		
		//导出表
		function exportorder(){
			$('#phonef').val($('#phone').val());
			$('#startdatef').val($('#startdate').val());
			$('#enddatef').val($('#enddate').val());
			$("#exportform").submit();
			
		}
		//时间比较
		function comptime(beginTime,endTime) {
		    var beginTimes = beginTime.substring(0, 10).split('/');
		    var endTimes = endTime.substring(0, 10).split('/');

		    beginTime = beginTimes[1] + '/' + beginTimes[2] + '/' + beginTimes[0] ;
		    endTime = endTimes[1] + '/' + endTimes[2] + '/' + endTimes[0];
		    if(beginTimes[0]-endTimes[0] >= 1||endTimes[0]-beginTimes[0] >=1 || beginTimes[1]-endTimes[1] >= 3||
		    		endTimes[1]-beginTimes[1] >= 3){
		    	alert("搜索时间跨度请小于三个月");
		    }
		    
		}
		
	</script>
</head>
<body> 
<div class="easyui-layout" style="width:100%;height:100%;">
	<div data-options="region:'north',split:true" style="height:92%;">
	<table id='orderTable'  class="easyui-datagrid" style="width:100%;height:100%" url="<%=basePath %>/listHandle" title="请输入查询条件" 
		rownumbers="true" toolbar="#searchBar" loadMsg="正在查询..." pagination="true">
		 
		<thead> 
			<tr> 
				<th field="orderid" width="20%">订单号</th> 
				<th field="phone" width="20%">用户手机号</th> 
				<th field="title" width="20%">商品名称</th> 
				<th field="price" width="10%">商品单价</th> 
				<th field="quantity" width="10%">商品数量</th>
				<th field="orderTime" width="10%">创建时间</th> 
			</tr> 
		</thead>
	</table> 
	
	<div id="searchBar" style="padding:5px;height:auto">
		<div>
		    <span>用户手机号:</span><input class="easyui-textbox" id="phone" style="width:120px" /> 
			创建时间起: <input class="easyui-datebox" id="startdate" style="width:120px" required="required"/>
			止: <input class="easyui-datebox" style="width:120px" id="enddate" required="required"/>
			<!--商品类别暂时不清楚具体有哪些可供选择固暂且注释@0706
			  商品类别: 
			<input class="easyui-combobox" style="width:100px;"
					url="getTypes.do"
					valueField="id" textField="text">  -->
			<a href="javascript:searchData()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> 
			
			
			<a href="javascript:exportorder()" class="easyui-linkbutton">导出</a>
			<form method="post" action="exportOrder.do" id="exportform">
			<input type="hidden" name="phone" id="phonef">
			<input type="hidden" name="startdata" id="startdataf">
			<input type="hidden" name="enddate" id="enddatef">
			</form>
			
		</div>
	</div>
	
	<div id="win" class="easyui-window" title="add window" style="width:600px;height:400px;" closed="true"  
	        data-options="iconCls:'icon-save',modal:true">   </div>
	</div>	        
	<div data-options="region:'south',split:true" style="height:50px;">
	 <label id="mytotal" >合计：${mytotal}</label>
	</div>         
</div>	
</body> 
</html>