<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<HTML>

<HEAD>
	<TITLE><tiles:getAsString name="pageTitle" /></TITLE>
	<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehour.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehour_header.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehour_calendar_small.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehour_overview.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url  value="/css/ehour_calendar.css" />">
		
<script>
	var ajaxError = "<fmt:message key="errors.ajax.general" />";
	var formSuccess = "<fmt:message key="admin.general.formSuccess" />";
	var sendingData = "<fmt:message key="general.submitting" />";
	var loadingMsg = "<fmt:message key="general.loading" />";
	var contextRoot = "<c:url value="/" />";	
</script>

<script src="<c:url value="/js/base.js" />" type="text/javascript"></script>

	<tiles:insert attribute="headJs" />
</HEAD>

<BODY>

<!-- Header incl nav -->
<DIV ID="header">
    <tiles:insert attribute="header" />
</DIV>

<!-- Calendar pane left -->
<DIV ID="calenderPaneSmall">
    <tiles:insert attribute="calendar_small" />
    <br><br>
    <tiles:insert attribute="admin_calendar_small" />
</DIV>

<!-- Content -->
<DIV ID="content">
    <tiles:insert attribute="content" />
</DIV>

</BODY>

</HTML>
