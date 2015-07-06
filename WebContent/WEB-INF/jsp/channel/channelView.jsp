<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<%@include file="../resources.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "hkp://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta charset="UTF-8">
<title>主页面</title>
<script>
//导出表
	function exportchannel(){
		$('#man').val($('#name').val());
		$('#ph').val($('#phone').val());
		$('#onc').val($('#cno').val());
		$("#exportform").submit();
		
	}
	
	
	//查询展示
	function searchData() {//把name,phone,cno的值传到后台doChannelview.do对应的方法中同时接受后台传过来的值
		$('#demoTable').datagrid('load', {
			name : $('#name').val(),
			phone : $('#phone').val(),
			cno : $('#cno').val()
		}
		
		
		
		);
	}
	function openWin() {
		$('#win').window('open');
	}
	function closeWin() {
		$('#win').window('close');
	}

	//增加提交的方法
	function submitForm() {
		$('#ff').form('submit');//#ff为表单的id
		if(!$("#ff").form('validate')){//提交失败则弹此窗
			alert("提交失败，请按标准填写数据！");
		}
		 
		//判断表单是否提交成功 如果成功的话
		$("#ff").form({
			success : function(data) {
				alert("提交成功");
				history.go(0);//页面刷新
				closeWin();//关闭窗口
				
			}
		});
	}

	function clearForm() {//表单重置
		$('#ff').form('clear');
	}
	//验证密码和重新输入密码一致
	$(function() {
		$.extend($.fn.validatebox.defaults.rules, {
			equals : {
				validator : function(value, param) {
					return value == $(param[0]).val();
				},
				message : '密码不匹配请重新输入.'
			}
		});
	})
	//自定义验证手机或电话格式
	$.extend($.fn.validatebox.defaults.rules, {
		phoneRex : {
			validator : function(value) {
				var rex = /^1[3-8]+\d{9}$/;
				//var rex=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
				//区号：前面一个0，后面跟2-3位数字 ： 0\d{2,3}
				//电话号码：7-8位数字： \d{7,8
				//分机号：一般都是3位数字： \d{3,}
				//这样连接起来就是验证电话的正则表达式了：/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/		 
				var rex2 = /^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
				if (rex.test(value) || rex2.test(value)) {
					// alert('t'+value);
					return true;
				} else {
					//alert('false '+value);
					return false;
				}

			},
			message : '请输入正确电话或手机格式'
		}
	});
	//以上是手机或电话的验证

	//以上是增加的方法  以下是修改的方法
	function uopenWin() {//先判断是否选中的是一行  然后打开窗口并往窗口里传值
		//easyui 获得选中行数据多行

		var rows = $('#demoTable').datagrid('getSelections');
		if (rows.length > 1) {
			alert("只能选中一个！");
		}
		if (rows.length == 0) {
			alert("请选中其中一条记录！");
		}
		if (rows.length == 1) {
			$('#update').window('open');
			//获取单条行数据

			var row = $('#demoTable').datagrid('getSelected');
			if (row) {//对窗口里的输入框赋值
				$("#id").val(row.id);
				$("#nam").val(row.name);
				$("#password").val(row.password);
				$("#email").val(row.email);
				$("#phon").val(row.phone);
				$("#cn").val(row.cno);
			}
		}
	}

	function ucloseWin() {
		$('#update').window('close'); //关闭窗口 
	}
	//修改提交的方法
	function usubmitForm() {
		$('#uu').form('submit');//提交表单	
		if(!$("#uu").form('validate')){//提交失败则弹此窗
			alert("提交失败，请按标准填写数据！");
		}
		$("#uu").form({
			success : function(data) {//提交成功的话执行下列方法
				
			alert("提交成功！");
				history.go(0);//页面刷新
				ucloseWin();//关闭窗口
			}
		});
	}

	function uclearForm() {
		$('#uu').form('clear');//重置表单
	}
	//以上是修改的方法

	//以下是删除的方法
	function deletechannel() {

		var ids = "";
		var rows = $('#demoTable').datagrid('getSelections');
		if(rows.length==0){
		alert("请至少选择一条记录！");	
		
		}
		
		if(rows.length>0){
		for (var i = 0; i < rows.length; i++) {
			ids += rows[i].id;//把	取到的id拼成字符串以逗号隔开
			ids += ","
		}
		var id = ids.substring(0, ids.length - 1);
		$.ajax({

			url : "deleteChannel.do",

			type : "post",

			cache : false,

			dataType : "text",

			data : {
				id : id
			},

			success : function(msg) {
				
			alert("删除成功！");
				history.go(0);
				
			}
		});
		}
	}

	//以下是查看渠道商的方法

	function lopenWin() {
		//easyui 获得选中行数据多行
		var ids = [];
		var rows = $('#demoTable').datagrid('getSelections');
		if (rows.length > 1) {
			alert("只能选中一个！");

		}
		if (rows.length == 0) {
			alert("请选中其中一条记录！");
		}
		if (rows.length == 1) {
			$('#look').window('open');
			//获取单条行数据

			var row = $('#demoTable').datagrid('getSelected');
			if (row) {//赋值
				$("#lid").html(row.id);
				$("#lname").html(row.name);
				$("#lpwd").html(row.password);
				$("#lemail").html(row.email);
				$("#lphone").html(row.phone);
				$("#lcno").html(row.cno);
				$("#lcreatetime").html(row.createtime);
			}
		}
	}

	function lcloseWin() {
		$('#look').window('close'); //关闭窗口 
	}

	//以上是查看渠道商的方法
</script>
</head>
<body>
 
	<table id='demoTable' class="easyui-datagrid"
		style="width: 100%; height: 100%" url="doChannelview.do"
		title="请输入查询条件" rownumbers="true" toolbar="#searchBar" 
               pagination="true"
		loadMsg="正在查询..."  >
		<thead>
			<tr>
				<th field='id' checkbox="true" ></th>
				<th field='cno' width="10%" >商号</th>
				<th field='name' width="20%">渠道商名称</th>
				<th field='password' width="10%">密码</th>
				<th field='phone' width="20%" align='right'>电话</th>
				<th field='email' width="20%" align='right'>邮箱</th>
				<th field='createtime' width="20%" align='left'>创建时间</th>
			</tr>
		</thead>
	</table>
	
	
	
	
	

 
 
 
 
 
 
 
	<div id="searchBar" style="padding: 5px; height: auto">
		<div style="margin-bottom: 5px">
			<a href="javascript:openWin()" class="easyui-linkbutton"
				iconCls="icon-add" plain="true">增加</a> <a
				href="javascript:uopenWin()" class="easyui-linkbutton"
				iconCls="icon-edit" plain="true">修改</a> <a
				href="javascript:lopenWin()" class="easyui-linkbutton"
				iconCls="icon-save" plain="true">查看</a>
			<!-- <a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true"></a> -->
			<a href="javascript:deletechannel()" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true">删除</a>
		</div>
		<div>
			<span>渠道商名称:</span><input class="easyui-textbox" id="name" 
				style="width: 80px" /> <span>渠道商手机号:</span><input
				class="easyui-textbox" id="phone"  style="width: 80px" /> <span>商号:
			</span><input class="easyui-textbox" id="cno"  style="width: 80px" /> <a
				href="javascript:searchData()" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'">查询</a>
				<a
				href="javascript:exportchannel()" class="easyui-linkbutton"
				>导出</a>
				<form method="post" action="exportChannel.do" id="exportform">
<input type="hidden" name="name" id="man">
<input type="hidden" name="phone" id="ph">
<input type="hidden" name="cno" id="onc">
</form>
		</div>
	</div>


	<!-- 以下增加渠道商 的窗口-->
	<div id="win" class="easyui-window" title="增加渠道商"
		style="width: 600px; height: 400px;" closed="true"
		data-options="iconCls:'icon-save',modal:true">

		<!-- 以下是增加的表单 -->

		<div style="width: 500px; margin: 60px，80px，5px, 180px">
<!-- 			<div class="easyui-panel" title="增加渠道商" style="width: 400px;"> -->
<!-- 				<div style="padding: 10px 0 10px 60px"> -->
					<form id="ff" method="post" action="addChannel.do">
						<table>
							<tr>
								<td>渠道商:</td>
								<td><input class="easyui-validatebox" type="text"
									name="name"
									data-options="required:true,validType:'contentsValidate',validType:'length[0,30]',
                                             missingMessage:'输入用户名'"
									style="width: 140px;"></input></td>
							</tr>
							<tr>
								<td>密码：</td>
								<td><input id="pwd" name="password" type="password"
									class="easyui-validatebox"
									data-options="required:true,validType:'length[6,30]',
                                            missingMessage:'输入密码',invalidMessage:'密码长度不正确'" />
								</td>
							</tr>
							<tr>
								<td>再次输入密码：</td>
								<td><input id="rpwd" name="rpwd" type="password"
									class="easyui-validatebox" required="required"
									validType="equals['#pwd']" missingMessage="再次输入密码" />
								</td>
							</tr>
							<tr>
								<td>邮箱:</td>
								<td><input class="easyui-validatebox" type="text"
									name="email"
									data-options="required:true,validType:'email',
                                             missingMessage:'输入邮箱地址',invalidMessage:'邮箱格式错误'"></input></td>
							</tr>
							<tr>
								<td>电话：</td>
								<td><input class="easyui-validatebox" type="text"
									name="phone" data-options="required:true,validType:'phoneRex'"></input></td>
							</tr>
							<tr>
								<td>商号：</td>
								<td><input class="easyui-validatebox" type="text"
									name="cno"
									data-options="required:true,validType:'length[0,30]'"></input></td>
							</tr>

						</table>
					</form>
				</div>
				<div style="text-align: center; padding: 5px">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-ok',toggle:true"
						onclick="submitForm()"> 提交 </a> <a href="javascript:void(0)"
						class="easyui-linkbutton" data-options="iconCls:'icon-reload'"
						onclick="clearForm()"> 重置 </a>
				</div>
			</div>

<!-- 		</div> -->

<!-- 		<!-- 以上是增加的表单 --> -->

<!-- 	</div> -->
	<!-- 以上是增加的表单的窗口 -->


	<!-- 以下修改渠道商 的窗口-->
	<div id="update" class="easyui-window" title="修改渠道商"
		style="width: 600px; height: 400px;" closed="true"
		data-options="iconCls:'icon-save',modal:true">

		<!-- 以下是修改的表单 -->

		<div style="width: 500px; margin: 60px，80px，5px, 180px">
<!-- 			<div class="easyui-panel" title="修改渠道商" style="width: 400px;"> -->
<!-- 				<div style="padding: 10px 0 10px 60px"> -->
					<form id="uu" method="post" action="updateChannel.do">
						<input type="hidden" id="id" name="id">
						<table>
							<tr>
								<td>渠道商:</td>
								<td><input class="easyui-validatebox" type="text"
									name="name" id="nam"
									data-options="required:true,validType:'contentsValidate',validType:'length[0,30]',
                                             missingMessage:'输入用户名'"
									style="width: 140px;"></input></td>
							</tr>
							<tr>
								<td>密码：</td>
								<td><input id="password" name="password" type="text"
									class="easyui-validatebox"
									data-options="required:true,validType:'length[6,30]',
                                            missingMessage:'输入密码',invalidMessage:'密码长度不正确'" />
								</td>
							</tr>

							<tr>
								<td>邮箱:</td>
								<td><input class="easyui-validatebox" type="text"
									name="email" id="email"
									data-options="required:true,validType:'email',
                                             missingMessage:'输入邮箱地址',invalidMessage:'邮箱格式错误'"></input></td>
							</tr>
							<tr>
								<td>电话：</td>
								<td><input class="easyui-validatebox" type="text" id="phon"
									name="phone" data-options="required:true,validType:'phoneRex'"></input></td>
							</tr>
							<tr>
								<td>商号：</td>
								<td><input class="easyui-validatebox" type="text"
									name="cno" id="cn"
									data-options="required:true,validType:'length[0,30]'"></input></td>
							</tr>

						</table>
					</form>
				</div>
				<div style="text-align: center; padding: 5px">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-ok',toggle:true"
						onclick="usubmitForm()"> 提交 </a> <a href="javascript:void(0)"
						class="easyui-linkbutton" data-options="iconCls:'icon-reload'"
						onclick="uclearForm()"> 重置 </a>
				</div>
			</div>
<!-- 		</div> -->

<!-- 		<!-- 以上是修改的表单 --> -->

<!-- 	</div> -->
	<!-- 以上是修改的表单的窗口 -->


	<!-- 以下查看你渠道商 的窗口-->
	<div id="look" class="easyui-window" title="look window"
		style="width: 600px; height: 400px;" closed="true"
		data-options="iconCls:'icon-save',modal:true">

		<center>
			<table id="looktable" height="295px" width="250px" cellpadding="0"
				cellspacing="0" border="1">
				<tr>
					<td>渠道商ID：</td>
					<td><span id="lid"></span></td>
				</tr>
				<tr>
					<td>渠道商名称：</td>
					<td><span id="lname"></span></td>
				</tr>
				<tr>
					<td>渠道商密码：</td>
					<td><span id="lpwd"></span></td>
				</tr>
				<tr>
					<td>渠道商商号：</td>
					<td><span id="lcno"></span></td>
				</tr>
				<tr>
					<td>渠道商电话：</td>
					<td><span id="lphone"></span></td>
				</tr>
				<tr>
					<td>渠道商邮箱：</td>
					<td><span id="lemail"></span></td>
				</tr>
				<tr>
					<td>创建时间：</td>
					<td><span id="lcreatetime"></span></td>
				</tr>
			</table>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-ok',toggle:true" onclick="lcloseWin()">
				关闭 </a>
		</center>
	</div>
	<!-- 以上是查看的表单的窗口 -->
</body>
</html>