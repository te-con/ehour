<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page import="org.acegisecurity.AuthenticationException" %>
<%@ page import="org.acegisecurity.ui.AbstractProcessingFilter" %>
<%@ page import="org.acegisecurity.ui.webapp.AuthenticationProcessingFilter" %>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>eHour - <fmt:message key="login.login.header" /></title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehourLogin.jsp" />">
	</head>
<body>

<br><br>

<div class="BackFrame">
	<h3><img src="img/bg_grey/ehour.gif" style="padding-top: 5px"></h3>
	<div class="BlueFrame">
		<h3>&nbsp;</h3>
		<table>
			<tr>
				<td colspan="3">
				    <c:if test="${not empty param.login_error}">
						<fmt:message key="login.login.failed" > 
							<fmt:param><%= ((AuthenticationException) session.getAttribute(AbstractProcessingFilter.ACEGI_SECURITY_LAST_EXCEPTION_KEY)).getMessage() %></fmt:param>
						</fmt:message>
					</c:if>&nbsp;
				</td>
			</tr>
		
			<form name="loginForm" method="post" action="<c:url value="/security_check.jsp"/>">
			<tr>
				<td><fmt:message key="login.login.username" /> :</td>
            	<td><input name="j_username" type="text" <c:if test="${not empty param.login_error}">value='<%= session.getAttribute(AuthenticationProcessingFilter.ACEGI_SECURITY_LAST_USERNAME_KEY) %>'</c:if>></td>
				<td>&nbsp;</td>
			</tr>
			
			<tr>
				<td><fmt:message key="login.login.password" /> :</td>
	            <td><input name="j_password" type="password"></td>
				<td><input type="submit" value="<fmt:message key="login.login.submit" /> >>" id="submitButton" onFocus="blur()">
				</td>
			</tr>
		</table>
		</form>
		
		<br><br>
		<div class="BlueFrameFooter">
			<p>
			</p>
		</div>
	</div>
	
	<div class="BackFrameFooter">
		<p style="padding-right: 60px;margin-top: -15px">
			v0.3
		</p> 
	</div>
</div>
</body>
</html>

