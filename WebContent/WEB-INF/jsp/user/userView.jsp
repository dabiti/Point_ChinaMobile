<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="../common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "hkp://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>主页面</title>
	<style type="text/css"> 
        .fitem 
        { 
            margin-bottom: 5px; 
        } 
        .fitem label 
        { 
            display: inline-block; 
            width: 80px; 
        } 
    </style>
	<script>
	//查询展示
	function searchData() {
		$('#demoTable').datagrid('load', {
			username : $('#username').val(),
			phone : $('#phone').val(),
			jobnumber : $('#jobnumber').val(),
		});
	}
	//增加提交的方法
	function submitForm() {
		$('#add').form('submit');//#ff为表单的id
		if(!$("#add").form('validate')){//提交失败则弹此窗
			alert("提交失败，请按标准填写数据！");
		}
		 
		//判断表单是否提交成功 如果成功的话
		$("#add").form({
			success : function(data) {
				alert("提交成功");
				history.go(0);//页面刷新
				closeDlg();//关闭窗口
			}
		});
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
				closeDlg();//关闭窗口
			}
		});
	}
	
	//以下是修改的方法
	function editUser() {//先判断是否选中的是一行  然后打开窗口并往窗口里传值
		//easyui 获得选中行数据多行
		var rows = $('#demoTable').datagrid('getSelections');
		if (rows.length > 1) {
			alert("只能选中一个！");
		}
		if (rows.length == 0) {
			alert("请选中其中一条记录！");
		}
		if (rows.length == 1) {
			$('#updateuser').window('open');
			//获取单条行数据

			var row = $('#demoTable').datagrid('getSelected');
			if (row) {//对窗口里的输入框赋值
				$("#id").val(row.id);
				$("#eusername").val(row.username);
				$("#epassword").val(row.password);
				$("#erealname").val(row.realname);
				$("#ejnumber").val(row.jobnumber);
				$("#eemail").val(row.email);
				$("#ephone").val(row.phone);
				$("#eenableId").val(row.enable);
				$("#edesc").textbox('setValue',row.description);
			}
		}
	}
	//以下是删除的方法
	function deleteUser() {
		var ids = "";
		var rows = $('#demoTable').datagrid('getSelections');
		if(rows.length==0){
		alert("请至少选择一条记录！");	
		}
		
		if(rows.length>0){
		for (var i = 0; i < rows.length; i++) {
			ids += rows[i].id;//把取到的id拼成字符串以逗号隔开
			ids += ","
		}
		var id = ids.substring(0, ids.length - 1);
		$.ajax({
			url : "deleteUser",
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
	//验证密码和重新输入密码一致
	$(function() {
		$.extend($.fn.validatebox.defaults.rules, {
			equals : {
				validator : function(value, param) {
					return value == $(param[0]).val();
				},
				message : '密码不匹配请重新输入'
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
					return true;
				} else {
					return false;
				}
			},
			message : '请输入正确电话或手机格式'
		}
	});
	//以上是手机或电话的验证
	function addUser() {
		$('#adduser').dialog('open');
	}
	function closeDlg() {
		$('#adduser').dialog('close');
	}
	
	//以下是查看用户的方法
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
			$('#lookUser').dialog('open');
			//获取单条行数据
			var row = $('#demoTable').datagrid('getSelected');
			if (row) {//赋值
				$("#lid").html(row.id);
				$("#lname").html(row.username);
				$("#lrealname").html(row.realname);
				$("#ljnumber").html(row.jobnumber);
				$("#lemail").html(row.email);
				$("#lphone").html(row.phone);
				$("#lenableId").html(row.enable);
				$("#lcreatetime").html(row.createtime);
				$("#ldesc").html(row.description);
			}
		}
	}

	function lclosedlg() {
		$('#lookUser').dialog('close'); //关闭窗口 
	}
	function eclosedlg() {
		$('#updateuser').dialog('close');
	}
	</script>
</head>
<body>
	<table id='demoTable' class="easyui-datagrid"
		style="width: 100%; height: 100%" url="<%=basePath%>/user/list"
		title="请输入查询条件" rownumbers="true" toolbar="#searchBar"
		loadMsg="正在查询..." pagination="true">
		<thead>
			<tr>
				<th field='id' checkbox="true" ></th>
				<th field="username" width="10%">登录名</th>
				<th field="realname" width="10%">真实姓名</th>
				<th field="jobnumber" width="10%">工号</th>
				<th field="phone" width="10%">手机号码</th>
				<th field="email" width="20%">邮箱地址</th>
				<th field="createtime" width="15%">创建时间</th>
				<th field="description" width="25%">描述</th>
			</tr>
		</thead>
	</table>
	
	<!-- 以下是增、删、改、查操作栏 -->
	<div id="searchBar" style="padding: 5px; height: auto">
		<div style="margin-bottom: 5px">
			<a href="javascript:addUser()" class="easyui-linkbutton" iconCls="icon-add" plain="true">增加</a> 
			<a href="javascript:editUser()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
			<a href="javascript:lopenWin()" class="easyui-linkbutton" iconCls="icon-save" plain="true">查看</a>
			<a href="javascript:deleteUser()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
		</div>
		<div>
			<span>登录名: </span>
				<input class="easyui-textbox" id="username" style="width: 80px" /> 
			<span>工号: </span>
				<input class="easyui-textbox" id="jobnumber" style="width: 80px" /> 
			<span>手机号码:</span>
				<input class="easyui-textbox" id="phone" style="width: 80px" />
			<a href="javascript:searchData()"
				class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
		</div>
	</div>
	
	<!-- 以下是增加的表单 -->
	<div id="adduser" class="easyui-dialog" title="新增运营人员" data-options="iconCls:'icon-save',modal:true"
		style="width: 400px; height: 450px;" closed="true" buttons="#add-buttons">
		<form id="add" method="post" action="addUser">
			<table border="0" cellpadding="0" cellspacing="0" style="border-collapse:separate; border-spacing:10px;">
				<tr class="fitem">
					<th>登录名:</th>
					<td>
						<input class="easyui-validatebox" type="text" name="username"
										data-options="required:true,validType:'contentsValidate',validType:'length[0,30]',
	                                             missingMessage:'输入用户名'"
										style="width: 140px;" />
					</td>
				</tr>
				<tr class="fitem">
					<th>密　码:</th>
					<td>
						<input id="pword" name="password" type="password" class="easyui-validatebox"
										data-options="required:true,validType:'length[6,30]',
                                            missingMessage:'输入密码',invalidMessage:'密码长度不正确'" />
					</td>
				</tr>
				<tr class="fitem">
					<th>确认密码:</th>
					<td>
						<input id="rpwd" name="rpwd" type="password"
									class="easyui-validatebox" required="required"
									validType="equals['#pword']" missingMessage="再次输入密码" />
					</td>
				</tr>
				<tr class="fitem">
					<th>真实姓名:</th>
					<td>
						<input class="easyui-validatebox" name="realname" type="text"
										data-options="required:true,validType:'contentsValidate',validType:'length[0,30]',
	                                             missingMessage:'请输入真实姓名'"
										style="width: 140px;" />
					</td>
				</tr>
				<tr class="fitem">
					<th>工　号:</th>
					<td>
						<input class="easyui-validatebox" name="jobnumber" type="text"
										data-options="required:true,validType:'contentsValidate',validType:'length[0,30]',
	                                             missingMessage:'请输入真实姓名'"
										style="width: 140px;" />
					</td>
				</tr>
				<tr class="fitem">
					<th>电子邮箱:</th>
					<td>
						<input class="easyui-validatebox" name="email" type="text"
									data-options="required:true,validType:'email',
                                             missingMessage:'输入邮箱地址',invalidMessage:'邮箱格式错误'"/>
					</td>
				</tr>
				<tr class="fitem">
					<th>手机号码:</th>
					<td>
						<input class="easyui-validatebox" type="text"
									name="phone" data-options="required:true,validType:'phoneRex'"/>
					</td>
				</tr>
				<tr class="fitem">
						<th>描　述:</th>
						<td>
						<input class="easyui-textbox" type="text" style ="width:240px;height:60px;" 
						name="description" data-options="required:true,validType:'length[0,30]',multiline:true">
						</input>
						</td>
				</tr>
			</table>
		<div id="add-buttons"> 
        	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()" iconcls="icon-save">添加</a> 
        	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="closeDlg()" iconcls="icon-cancel">取消</a> 
    	</div> 
	</form>
	</div>
	<!-- 以上是增加的表单的窗口 -->
	
	<!-- 以下修改渠道商 的窗口-->
	<div id="updateuser" class="easyui-dialog" title="修改运营人员信息" style="width: 400px; height: 450px;" closed="true"
		data-options="iconCls:'icon-edit',modal:true" buttons="#update-buttons">
		<form id="uu" method="post" action="updateUser">
		<input type="hidden" id="id" name="id">
		<table>
							<tr>
								<td>登录名:</td>
								<td><input class="easyui-validatebox" type="text"
									name="username" id="eusername"
									data-options="required:true,validType:'contentsValidate',validType:'length[0,30]',
                                             missingMessage:'输入登录名'"
									style="width: 140px;">
									</input>
								</td>
							</tr>
							
							<tr>
								<td>密码：</td>
								<td><input id="epassword" name="password" type="text"
									class="easyui-validatebox"
									data-options="required:true,validType:'length[6,30]',
                                            missingMessage:'输入密码',invalidMessage:'密码长度不正确'" />
								</td>
							</tr>
							
							<tr>
								<th>真实姓名:</th>
								<td>
								<input class="easyui-validatebox" type="text" name="realname" id="erealname"
										data-options="required:true,validType:'contentsValidate',validType:'length[0,30]',
	                                             missingMessage:'请输入真实姓名'"
										style="width: 140px;" />
								</td>
							</tr>
							
							<tr>
								<th>工    号:</th>
								<td>
								<input class="easyui-validatebox" type="text" name="jobnumber" id="ejnumber"
										data-options="required:true,validType:'contentsValidate',validType:'length[0,30]',
	                                             missingMessage:'请输入真实姓名'"
										style="width: 140px;" />
								</td>
							</tr>
							
							<tr>
								<td>邮箱:</td>
								<td><input class="easyui-validatebox" type="text"
									name="email" id="eemail"
									data-options="required:true,validType:'email',
                                             missingMessage:'输入邮箱地址',invalidMessage:'邮箱格式错误'"></input></td>
							</tr>
							<tr>
								<td>电话：</td>
								<td><input class="easyui-validatebox" type="text" id="ephone"
									name="phone" data-options="required:true,validType:'phoneRex'">
								</input>
								</td>
							</tr>
							<tr>
								<th>描　述:</th>
								<td>
								<input class="easyui-textbox" type="text" style ="width:240px;height:60px;" id="edesc"
								name="description" data-options="required:true,validType:'length[0,30]',multiline:true">
								</input>
								</td>
							</tr>
						</table>
						<div id="update-buttons"> 
        					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="usubmitForm()" iconcls="icon-edit">提交</a> 
        					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="eclosedlg()" iconcls="icon-cancel">取消</a> 
    					</div>
					</form>
				</div>
	<!-- 以上是修改的表单的窗口 -->
	
	<!-- 以下查看运营商信息的窗口-->
	<div id="lookUser" class="easyui-dialog" title="查看"
		style="width: 400px; height: 450px;" closed="true"
		data-options="iconCls:'icon-save',modal:true">
		<br/>
		<center >
			<table id="looktable" height="300px" width="250px" cellpadding="0"
				cellspacing="0" border="1" >
				<tr class="fitem">
					<td>登录名ID:</td>
					<td><span id="lid"></span></td>
				</tr>
				<tr class="fitem">
					<td>登录名:</td>
					<td><span id="lname"></span></td>
				</tr>
				<tr class="fitem">
					<td>真实姓名:</td>
					<td><span id="lrealname"></span></td>
				</tr>
				<tr class="fitem">
					<td>工　号:</td>
					<td><span id="ljnumber"></span></td>
				</tr>
				<tr class="fitem">
					<td>电子邮箱:</td>
					<td><span id="lemail"></span></td>
				</tr>
				<tr class="fitem">
					<td>手机号码:</td>
					<td><span id="lphone"></span></td>
				</tr>
				<tr class="fitem">
					<td>创建时间：</td>
					<td><span id="lcreatetime"></span></td>
				</tr>
				<tr class="fitem">
					<td>描述：</td>
					<td><span id="ldesc"></span></td>
				</tr>
			</table>
			<br/>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-ok',toggle:true" onclick="lclosedlg()">关闭 </a>
		</center>
	</div>
	<!-- 以上是查看的表单的窗口 -->
</body>
</html>