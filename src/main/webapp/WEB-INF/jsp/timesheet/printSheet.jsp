<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>

<head>
	<title><fmt:message key="user.timesheet.print.printHeader">
				<fmt:param>${printUser.firstName}  ${printUser.lastName}</fmt:param>
				<fmt:param><fmt:formatDate pattern="MMMMM yyyy" value="${printDate.time}" /></fmt:param>				
			</fmt:message>
	</title>
	
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/ehourPrint.css" />">
</head>

<body>

<div class="Header">
	<img src="<c:url value="/img/print/ehour.png" />">
</div>

<hr>

<br>

<div class="Content">

	<h1><fmt:message key="user.timesheet.print.printHeader">
					<fmt:param>${printUser.firstName}  ${printUser.lastName}</fmt:param>
					<fmt:param><fmt:formatDate pattern="MMMMM yyyy" value="${printDate.time}" /></fmt:param>				
				</fmt:message></h1>
	<br>
	<center>
	<table class="sheetTable" cellpadding="0" cellspacing="0">
	
		<tr>
			<td class="dateRowFirstCell">&nbsp;</td>
		<c:forEach items="${dateSequence}" var="date" varStatus="dateStatus">
			<td class="dateRow"><fmt:formatDate value="${date}" pattern="dd" /></td>
		</c:forEach>
			<td style="padding-left: 10px">Total</td>
		</tr>
		
		<c:set var="grandTotalHour" value="0" />				
		
		<c:forEach items="${printReport.values}" var="row" varStatus="rowStatus">
			<c:set var="totalHour" value="0" />		
		<tr>
			<td class="firstCell">${row.key.project.name} <c:if test='${row.key.description != null && row.key.description != ""}'> - ${row.key.description}</c:if></td>

			<c:forEach items="${dateSequence}" var="date" varStatus="dateStatus">			
				<c:set var="totalHour" value="${totalHour + printReport.values[row.key][date].totalHours}" />	
				<c:set var="grandTotalHour" value="${grandTotalHour + printReport.values[row.key][date].totalHours}" />					
				<td>
					<fmt:formatNumber value="${printReport.values[row.key][date].totalHours}" maxFractionDigits="2"/>&nbsp;
				</td>
			</c:forEach>
			
			<td class="dateRow" style="text-align:right">
				<fmt:formatNumber value="${totalHour}" maxFractionDigits="2"/>
			</td>
		</tr>
		</c:forEach>
		
		<tr>
			<td colspan="${1 + fn:length(dateSequence)}" style="border: 0">
				&nbsp;			
			</td>
			
			<td style="border: 0;text-align:right;font-weight: bold">
				<fmt:formatNumber value="${grandTotalHour}" maxFractionDigits="2"/>
			</td>
		</tr>

		<c:if test="${signatureSpace}">
		<tr>
			<td style="border: 0">
				&nbsp;
			</td>
			
			<td colspan="15" style="border: 0;text-align: left">
				<Br><Br><br>
				<fmt:message key="user.timesheet.print.managerSignature" />
				<br><br><br><br>
				Date:
			</td>
			
			<td colspan="${1 + fn:length(dateSequence) - 15 - 16}" style="border: 0">
				&nbsp;
			</td>
			
			<td colspan="16" style="border: 0;text-align: left">
				<Br><Br><br>		
				${printUser.firstName}  ${printUser.lastName} <fmt:message key="user.timesheet.print.userSignature" />
				<br><br><br><br>
				Date:
			</td>		
		</tr>

		</c:if>

		
	</table>
	</center>
		<Br>
		
		<br><br>
		



</div>

<div class="Footer">
	<hr>
	<div style="float: left; padding-left: 15px;">
		<fmt:message key="user.timesheet.print.printedOn">
			<fmt:param><fmt:formatDate value="${currentDate.time}" pattern="dd MMMMM yyyy" /></fmt:param>
		</fmt:message>	
	</div>

	<div style="float: right; padding-right: 15px">
		eHour - http://www.ehour.nl/
	</div>
</div>

</body>

</html>
