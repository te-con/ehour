<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>eHour - <fmt:message key="login.accessDenied.header" /></title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehourLogin.jsp" />">
	</head>
<body>

<br><br>

<div class="BackFrame">
	<h3><img src="<c:url value="/img/bg_grey/ehour.gif" />" style="padding-top: 5px"></h3>
	<div class="BlueFrame">
		<h3><fmt:message key="login.accessDenied.header" /></h3>

		<p style="margin:0 0 0 20px">
			<fmt:message key="login.accessDenied.body" />
		</p>
		
		<br><br>
		<div class="BlueFrameFooter">
			<p>
			</p>
		</div>
	</div>
	
	<div class="BackFrameFooter">
		<p style="padding-right: 60px;margin-top: -15px">
			v0.1
		</p> 
	</div>
</div>
</body>
</html>

