<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<%@include file="resources.jsp" %>
	<script type="text/javascript">
		
		function synchronizeProgress(){
			$.post("${pageContext.request.contextPath}/getStatus.do",
					function (data){
						var progress = eval('(' + data + ')');
						var width = progress.pBytesRead/progress.pContentLength*100;
						$('#p').progressbar('setValue', Math.floor(width));
						$("#ps").text(progress.pBytesRead+"/"+progress.pContentLength);
						if(width!=100){
// 							setTimeout("synchronizeProgress()",1000);
							synchronizeProgress();
						}
					}
				);
		}
		
		function run(){
			document.getElementById("form1").submit();
			$('#p').progressbar('setValue', 0);
			synchronizeProgress();
		}
	</script>
</head>
<body>
	
		<form id="form1" target="iframe1" action="${pageContext.request.contextPath}/upload.do" method="post" enctype="multipart/form-data">
			<table>
				<tr>
					<td>
						<input type="file" name="doc" style="display:yes;"/>
						<div id="p" class="easyui-progressbar" style="width:400px;"></div>
					</td>
					<td>
						<span id="ps"></span>
					</td>
				</tr>
				
			</table>
		</form>
		<iframe name="iframe1" style="display:none"></iframe>
		<input type="button" id="btn" value="上传" onclick="run()"/>
</body>
</html>