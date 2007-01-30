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

<!-- 
	<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehour.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehour_header.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehour_calendar_small.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehour_overview.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehour_calendar.css" />">
-->		
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

<table width="100%">
	<tr>
		<td style="padding-left: 35px; padding-top: 20px; padding-bottom: 1.0em;">
			<img SRC="<c:url value="/img/ehour.gif" />" alt="eHour v0.1" />
		</td>
		
		<td style="padding-left: 25px; padding-bottom: 0.65em" valign="bottom">
			<div class="Header">
				<tiles:insert attribute="header" />
			</div>
		</td>
	</tr>

	<tr>
		<td valign="top" width="220" style="padding-left: 20px" height="200">
		    <tiles:insert attribute="navCalendar" />
		</td>
		
		<td rowspan="2" valign="top" style="padding-left: 20px">
			<tiles:insert attribute="content" />
		</td>
	</tr>
	
	<tr>
		<td style="padding-left: 20px" valign="top">
		    <span id="statusMessage">
				&nbsp;
			</span>
		    
    		<br>

		    <tiles:insert attribute="contextHelp" />
		</td>
	</tr>
</table>

</body>

</html>
