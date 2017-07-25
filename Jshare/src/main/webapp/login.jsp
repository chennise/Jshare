<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>登录页面</title>
</head>
<body>
	<form action="/u/login" method="post">
		
		账号：<input type="text" value="" name="username" id="username" title="请输入账号"/></br>
		密码：<input type="password" value="" name="passwd" id="passwd"/></br>
		<input type="submit" name="登录" /><input type="reset" name="重置"/>
		
	</form>

</body>
</html>