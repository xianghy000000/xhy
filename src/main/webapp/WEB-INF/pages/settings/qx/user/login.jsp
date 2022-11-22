<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<meta charset="UTF-8">
<link href="${pageContext.request.contextPath}/jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script>
		$(function () {
			$("#loginSubmitBut").click(function(){
				var userCode = $("#loginCode").val();
				var userPwd = $("#loginPwd").val();
				var loginCheck = $("#loginCheck").prop("checked");
				if(userCode =="" && userPwd==""){
					alert("账号，密码不能为空")
					return;
				}
				var data = {"userCode":userCode,"userPwd":userPwd,"loginCheck":loginCheck}
				$.post("${pageContext.request.contextPath}/login/loginUser.action",data,function(message){

					if(message.messageCode==1){
						window.location.href="${pageContext.request.contextPath}/workbench/main/toIndex.action";
					}else{
						alert(message.messageStr)
					}
				},"json")
			});
		})
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="${pageContext.request.contextPath}/image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2019&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<%--<form action="../../../workbench/index.html" class="form-horizontal" role="form">--%>
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginCode" value="${cookie.loginCode.value}">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd" value="${cookie.loginPwd.value}">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<label>
							<input type="checkbox" checked="checked" id="loginCheck"> 十天内免登录
						</label>
						&nbsp;&nbsp;
						<span id="msg"></span>
					</div>
					<button type="submit" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;" id="loginSubmitBut">登录</button>
				</div>
			<%--</form>--%>
		</div>
	</div>
</body>
</html>