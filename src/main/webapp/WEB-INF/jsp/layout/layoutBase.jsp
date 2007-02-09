<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<title><tiles:getAsString name="pageTitle" /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/ehourMain.jsp" />" media="screen">
	
<tiles:importAttribute name="extraCss" />

<c:if test='${extraCss != ""}'>
	<link rel="stylesheet" type="text/css" href="<c:url value="${extraCss}" />" media="screen">
</c:if>

<script>
	var ajaxError = "<fmt:message key="errors.ajax.general" />";
	var formSuccess = "<fmt:message key="admin.general.formSuccess" />";
	var sendingData = "<fmt:message key="general.submitting" />";
	var loadingMsg = "<fmt:message key="general.loading" />";
	var contextRoot = "<c:url value="/" />";	
</script>

	<script src="<c:url value="/js/base.js" />" type="text/javascript"></script>
</head>

<body>

<div class="NavHeader">
	<table>
		<tr>
			<td width="260" align="bottom">
				<img src="<c:url value="/img/ehour.gif" />" alt="eHour v0.1" />
			</td>

			<td align="bottom">
				<div class="MainNav">
					<tiles:insert attribute="header" />
				</div>
			</td>
		</tr>
	</table>
</div>

<div class="LoggedInAs">
	Logged in as Thies Edeling&nbsp;
</div>

<table>
	<tr>
		<td valign="top" width="260" style="padding-left: 12px" height="200">
		    <tiles:insert attribute="navCalendar" />
		    <span id="statusMessage">
				&nbsp;
			</span>
   		    <tiles:insert attribute="contextHelp" />
		</td>
		
		<td valign="top" style="padding-left: 12px">
			<tiles:insert attribute="content" />
		</td>
	</tr>
</table>

</body>

</html>
