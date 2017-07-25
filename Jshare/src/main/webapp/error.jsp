<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统异常</title>
</head>
<body>
	HTTP代码:${error.httpCode }
	<br /> 友好提示信息: ${error.msg }
	<br /> 实际系统异常: ${error.exception }
	<br /> 响应状态码: ${error.statusCode }
	<br />
</body>
</html>