<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- spanTarget: overview -->

<form method="post" action="postTimesheet.do" id="timesheetFormId" name="timesheetForm">

<input type="hidden" id="sheetDay" name="sheetDay" value="<fmt:formatDate value="${timesheet.weekStart}" pattern="dd" />">
<input type="hidden" id="sheetMonth" name="sheetMonth" value="<fmt:formatDate value="${timesheet.weekStart}" pattern="MM" />">
<input type="hidden" id="sheetYear" name="sheetYear" value="<fmt:formatDate value="${timesheet.weekStart}" pattern="yyyy" />">
<input type="hidden" id="userId" value="${timesheet.userId}">

<table class="cal_table" cellspacing="0" width="500">

<tr>
	<th style="text-align: left; padding-bottom: 2px">
		&nbsp;<fmt:message key="user.overview.week" /> <fmt:formatDate value="${timesheet.weekStart}" pattern="w, MMMMM yyyy" />
	</th>

	<c:forEach items="${timesheet.dateSequence}" var="date">
		<th>
			<fmt:formatDate value="${date}" pattern="E" />
			<br>
			<fmt:formatDate value="${date}" pattern="dd" />
		</th>
	</c:forEach>
</tr>

<c:forEach items="${timesheet.timesheetRows}" var="row">
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
						   <c:if test="${cell.timesheetEntry.hours != null && cell.timesheetEntry.hours  > 0}">
							   value="<fmt:formatNumber value="${cell.timesheetEntry.hours}" maxFractionDigits="2"/>"
							</c:if>							  
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
<br>
<table class="cal_table" cellspacing="0" width="500">
	<tr>
		<th style="text-align: left; padding-bottom: 2px">&nbsp;<fmt:message key="user.timesheet.weekComments" /></th>
	</tr>
	
	<tr>
		<td style="border-bottom: #66000 1px solid;">
			<textarea name="comment" id="comment"
						rows="3" cols="40" class="dataEntry" style="width: 495px" wrap="virtual">${timesheet.comment.comment}</textarea>
		</td>
	</tr>
</table>


<table cellspacing="0" width="500">
	<tr>
		<td><input type="submit" class="redSubmit" value="reset" onClick="return resetTotal();"></td>
		<td align="right"><input type="submit" class="redSubmit" value="<fmt:message key="user.timesheet.storeAndNext" />" onClick="return makeFormSubmittable();"></td>
	</tr>
</table>
	
</form>

<script>
	updateTotal();
	
	<c:if test="${errorCommentTooLong}">
		alert(errorCommentTooLong);
	</c:if>	
</script>
