<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz" %>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<title><tiles:getAsString name="pageTitle" /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/ehourMain.jsp" />" media="screen">
	
<tiles:importAttribute name="extraCss" scope="page" />
<tiles:importAttribute scope="request" /> 

<c:if test='${extraCss != ""}'>
	<link rel="stylesheet" type="text/css" href="<c:url value="${extraCss}" />" media="screen">
</c:if>

<script>
	var ajaxError = "<fmt:message key="errors.ajax.general" />";
	var formSuccess = "<fmt:message key="admin.general.formSuccess" />";
	var sendingData = "<fmt:message key="general.submitting" />";
	var loadingMsg = "<fmt:message key="general.loading" />";
	var contextRoot = "<c:url value="/" />";
	var loginAs = "<fmt:message key="nav.loggedInAs"><fmt:param><authz:authentication operation="username"/></fmt:param></fmt:message>";
</script>

</head>

<body>

<div class="NavHeader">
	<table>
		<tr>
			<td width="260" align="bottom">
				<img src="<c:url value="/img/ehour.gif" />" alt="eHour v0.1" />
			</td>

			<td align="bottom" style="padding-left: 11px">
				<div class="MainNav">
					<tiles:insert attribute="header" />
				</div>
			</td>
		</tr>
	</table>
</div>

<div id="LoggedInAs" class="LoggedInAs">
	<fmt:message key="nav.loggedInAs"><fmt:param><authz:authentication operation="username"/></fmt:param></fmt:message>&nbsp;
</div>

<table>
	<tr>
		<td valign="top" width="248" style="padding-left: 12px">
			<div id="NavCalTarget">
			    <tiles:insert attribute="navCalendar" />
			</div>
		    <br><br>
			<div id="HelpTarget">
   		    	<tiles:insert attribute="contextHelp" />
   		    </div>
		</td>
		
		<td valign="top" style="">
			<tiles:insert attribute="content" />
		</td>
	</tr>
</table>

</body>

</html>
