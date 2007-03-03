<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


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

	<table class="sheetTable">
	
		<tr>
			<td>&nbsp;</td>
		<c:forEach items="${dateSequence}" var="date" varStatus="dateStatus">
			<td><fmt:formatDate value="${date}" pattern="dd" /></td>
		</c:forEach>
		</tr>
		
		<c:forEach items="${printReport.values}" var="row" varStatus="rowStatus">
		<tr>
			<td>${row.key.project.name} <c:if test='${row.key.description != null && row.key.description != ""}'> - ${row.key.description}</c:if></td>

			<c:forEach items="${dateSequence}" var="date" varStatus="dateStatus">			
			<td>
				${printReport.values[row.key][date].totalHours}
			</td>
			</c:forEach>
		</tr>
		</c:forEach>
		
	</table>
	
	<c:if test="${signatureSpace}">
		${printUser.firstName}  ${printUser.lastName} <fmt:message key="user.timesheet.print.userSignature" />
		<Br>
		<fmt:message key="user.timesheet.print.managerSignature" />
		<br><br>
		
		<fmt:message key="user.timesheet.print.printedOn">
			<fmt:param><fmt:formatDate value="${currentDate.time}" pattern="dd MMM yyyy" /></fmt:param>
		</fmt:message>
	</c:if>

</div>

(TODO: fix layout)
</body>

</html>
