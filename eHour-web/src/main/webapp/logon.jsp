<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page import="org.acegisecurity.AuthenticationException" %>
<%@ page import="org.acegisecurity.ui.AbstractProcessingFilter" %>
<%@ page import="org.acegisecurity.ui.webapp.AuthenticationProcessingFilter" %>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>eHour - <fmt:message key="login.login.header" /></title>
		<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehourLogin.jsp" />" />
		
		<script>
			
			function toggleCheckbox(imgId, id)
			{
				var checkboxId = document.getElementById(id);
				var img = document.getElementById(imgId);
				
				if (checkboxId.checked)
				{
					checkboxId.checked = false;
					img.src='<c:url value="/img/checkbox_off.gif" />';
				}
				else
				{
					checkboxId.checked = true;
					img.src='<c:url value="/img/checkbox_on.gif" />';
				}
				
				img.blur();
				
				return false;
			}
		</script>
		
	</head>
<body>

<!-- spanTarget: loginForm -->

<br /><br />

<div class="BackFrame">
	<h3><img src="img/bg_grey/ehour.gif" style="padding-top: 5px" /></h3>
	<div class="BlueFrame">
		<h3>&nbsp;</h3>

		<table>
			<tr>
				<td colspan="3">
					<form name="loginForm" method="post" action="<c:url value="/security_check.jsp"/>">
				
				    <c:if test="${not empty param.login_error}">
						<fmt:message key="login.login.failed" > 
							<fmt:param><%= ((AuthenticationException) session.getAttribute(AbstractProcessingFilter.ACEGI_SECURITY_LAST_EXCEPTION_KEY)).getMessage() %></fmt:param>
						</fmt:message>
					</c:if>&nbsp;
				</td>
			</tr>
		
			<tr>
				<td><fmt:message key="login.login.username" /> :</td>
            	<td><input name="j_username" type="text" <c:if test="${not empty param.login_error}">value='<%= session.getAttribute(AuthenticationProcessingFilter.ACEGI_SECURITY_LAST_USERNAME_KEY) %>'</c:if> /></td>
				<td>&nbsp;</td>
			</tr>
			
			<tr>
				<td><fmt:message key="login.login.password" /> :</td>
	            <td><input name="j_password" type="password" /></td>
				<td><input type="submit" value="<fmt:message key="login.login.submit" /> >>" id="submitButton" onfocus="blur()" />
				</td>
			</tr>
			
		<c:if test="${config.rememberMeAvailable}">			
			<tr>
				<td colspan="2"><fmt:message key="login.login.rememberMe" />&nbsp;
            	<a href="" onclick="return toggleCheckbox('checkboxImg', 'rememberId')"><img id="checkboxImg" src="<c:url value="/img/checkbox_off.gif" />" border="off" /></a>
            		<input type="hidden" name="j_acegi_security_remember_me" id="rememberId"/></td>
				<td>&nbsp;</td>
			</tr>
		</c:if>
		
			<tr>
				<td colspan="3"><br /><br /></form></td>
			</td>	
		</table>
		

		<div class="BlueFrameFooter">
			<p>
			</p>
		</div>
	</div>
	
	<div class="BackFrameFooter">
		<p style="padding-right: 60px;margin-top: -15px">
			v0.6
		</p> 
	</div>
</div>
</body>
</html>