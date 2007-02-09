<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">


<HTML>

<HEAD>
	<TITLE>eHour - access denied</TITLE>
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

		<tr>
			<td colspan=4>
				Access denied<br>
				Your account is not authorized to access the requested functionality.
			</td>
		</tr>
    </table>
   </td>
  </tr>
 </table>
</center>

</BODY>

</HTML>

