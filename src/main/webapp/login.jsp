<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="org.acegisecurity.AuthenticationException" %>
<%@ page import="org.acegisecurity.ui.AbstractProcessingFilter" %>
<%@ page import="org.acegisecurity.ui.webapp.AuthenticationProcessingFilter" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">


<HTML>

<HEAD>
	<TITLE>eHour - login</TITLE>
	<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehour.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehour_header.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehour_calendar_small.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehour_overview.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehour_calendar.css" />">
</HEAD>

<BODY>

<!-- Content -->

<br><br>
<br><br><br><br>

<center>
<table border="0" cellpadding="0" cellspacing="0">

	<tr>
		<td>

		<img src="<c:url value="/img/ehour.gif" />" alt="eHour">
		<table class="login_table" cellspacing="0">
			<form name="loginForm" method="post" action="<c:url value='j_acegi_security_check'/>">

    <c:if test="${not empty param.login_error}">
		<tr>
			<td colspan=4>
				Failed to login (<%= ((AuthenticationException) session.getAttribute(AbstractProcessingFilter.ACEGI_SECURITY_LAST_EXCEPTION_KEY)).getMessage() %>)
			</td>
		</tr>
	</c:if>

		<tr>
            <td>Username:</td>
            <td><input name="j_username" size="20" class="normtxt" type="text" <c:if test="${not empty param.login_error}">value='<%= session.getAttribute(AuthenticationProcessingFilter.ACEGI_SECURITY_LAST_USERNAME_KEY) %>'</c:if>></td>
						<td colspan=2>&nbsp;</td>
        </tr>
<input type="hidden" name="_acegi_security_remember_me" value="checked">
        <tr>
            <td>Password:</td>
            <td><input name="j_password" size="20" class="normtxt" type="password"></td>
            <td>&nbsp;&nbsp;&nbsp;<input type="submit" value="login >>" class="loginSubmit"></td>
            <td>&nbsp;</td>
        </tr>

			</form>
    </table>
   </td>
  </tr>
 </table>
</center>

</BODY>

</HTML>

