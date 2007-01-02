<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<form method="post" action="postTimesheet.do" id="timesheetForm" name="timesheetForm">

<input type="hidden" name="userId" value="${timesheetUserId}" />

<table class="cal_table" cellspacing="0" width="500">

<tr>
	<th style="text-align: left; padding-bottom: 2px">
		&nbsp;<fmt:message key="user.overview.week" /> <fmt:formatDate value="${weekDate}" pattern="w, MMMMM yyyy" />
	</th>

	<c:forEach items="${dateSeq}" var="date">
		<th>
			<fmt:formatDate value="${date}" pattern="E" />
			<br>
			<fmt:formatDate value="${date}" pattern="dd" />
		</th>
	</c:forEach>
</tr>

<c:forEach items="${timesheetRows}" var="row">
	<tr>
		<td class="cal_projCell">
			&nbsp;
			<a href="" onClick="return bookToProject(${row.projectAssignment.assignmentId})"
					   title="<fmt:message key="user.timesheet.bookWeekOnProject" />">
				${row.projectAssignment.project.fullname}:
			</a>
		</td>
		
		<c:forEach items="${row.timesheetCells}" var="cell" varStatus="status">
<%-- @todo sunday & saturday as marked as grey, configurable? --%>
			<c:choose>
				<c:when test="${status.count == 1 || status.count == 7}">
					<td class="cal_dataCell" style="background-color: #cccccc">
				</c:when>
				
				<c:otherwise>
					<td class="cal_dataCell">
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${cell.valid}">
					<input type="text" class="dataEntry" size="2"
						   name="ehts_${row.projectAssignment.assignmentId}_<fmt:formatDate value="${cell.cellDate}" pattern="yyyyMMdd" />_${status.count}"
						   value="<fmt:formatNumber value="${cell.timesheetEntry.hours}" maxFractionDigits="2"/>"
						   onChange="validateField(this)"
							>
				</c:when>
				
				<c:otherwise>
						&nbsp;
						<fmt:formatNumber value="${cell.timesheetEntry.hours}" maxFractionDigits="2"/>
						<input type="hidden"
							   name="inactive_${row.projectAssignment.assignmentId}_<fmt:formatDate value="${cell.cellDate}" pattern="yyyyMMdd" />" 
							   value="${cell.timesheetEntry.hours}">
				</c:otherwise>
			</c:choose>
			
			</td>
		</c:forEach>
	</tr>						 
</c:forEach>

	<tr>
		<th colspan="5" style="text-align: left">&nbsp;<fmt:message key="user.timesheet.total" />:</th>
		<th colspan="3" id="totalHours" style="text-align: right;padding-right: 7px">0</th>
	</tr>
	
</table>

<table cellspacing="0" width="500">
	<tr>
		<td><input type="submit" class="redSubmit" value="reset" onClick="return resetTotal();"></td>
		<td align="right"><input type="submit" class="redSubmit" value="<fmt:message key="user.timesheet.storeAndNext" />" onClick="return resetTotal();"></td>
	</tr>
</table>
	
</form>

<script>
	var maxHoursPerDay = 8;
	var errorNotValidNumber = '<fmt:message key="user.timesheet.errorNotValidNumber" />';
	var error24HoursMax = '<fmt:message key="user.timesheet.error24HoursMax" />';

	updateTotal();
</script>
