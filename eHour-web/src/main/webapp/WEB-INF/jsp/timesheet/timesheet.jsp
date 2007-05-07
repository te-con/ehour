<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- spanTarget: overview -->

<form method="post" action="postTimesheet.do" id="timesheetFormId" name="timesheetForm">

<input type="hidden" id="sheetDay" name="sheetDay" value="<fmt:formatDate value="${timesheet.weekStart}" pattern="dd" />">
<input type="hidden" id="sheetMonth" name="sheetMonth" value="<fmt:formatDate value="${timesheet.weekStart}" pattern="MM" />">
<input type="hidden" id="sheetYear" name="sheetYear" value="<fmt:formatDate value="${timesheet.weekStart}" pattern="yyyy" />">
<input type="hidden" id="userId" value="${timesheet.userId}">


<div class="ContentFrame">

	<div class="TimesheetHeaderDiv">
		<a href="" onclick="return moveRelativeWeek(-1)" title="<fmt:message key="user.timesheet.previousWeek" />">&lt;&lt;</a>
		<span class="timesheetHeader"><fmt:message key="user.overview.week" /> <fmt:formatDate value="${timesheet.weekStart}" pattern="w, MMMMM yyyy" /></span>
		<a href="" onclick="return moveRelativeWeek(1)" title="<fmt:message key="user.timesheet.nextWeek" />">&gt;&gt;</a>
	</div>
	
	<div class="GreyFrame">
		<h3>&nbsp;</h3>
		
		<div class="BlueFrame">
			<div class="BlueLeftTop">
				<div class="BlueRightTop">
					&nbsp;
				</div>
			</div>	

		<table class="timesheetTableIEPaddingHack" cellpadding="0" cellspacing="0" style="margin-top: -15px">
			<tr>
				<td width="5">&nbsp;</td>
				<td valign="top">
		<table class="timesheetTable" cellpadding="0" cellspacing="0">

		<tr class="weekColumnRow">
			<td class="project" style="left">&nbsp;&nbsp;<fmt:message key="user.timesheet.project" /></td>
			<td class="projectCode"><fmt:message key="user.timesheet.projectCode" /></td>
			<td class="customer" valign="bottom"><fmt:message key="user.timesheet.customer" /></td>
			<td class="spacer">&nbsp;</td>
			
			<c:forEach items="${timesheet.dateSequence}" var="date" varStatus="status">
				<c:choose>
					<c:when test="${status.count == 7}">
						<td class="lastChild">
					</c:when>
					
					<c:otherwise>
						<td>
					</c:otherwise>
				</c:choose>		
					<fmt:formatDate value="${date}" pattern="E" />
					<br>
					<fmt:formatDate value="${date}" pattern="dd" />
				</td>
			</c:forEach>
		</tr>

		<c:forEach items="${timesheet.timesheetRows}" var="row" varStatus="status">
		<tr class="projectRow" <c:if test="${status.count % 2 == 0}">style="background-color: #fefeff"</c:if>>
				<td class="project">
					&nbsp;
					<a href="" onClick="return bookToProject(${row.projectAssignment.assignmentId})"
							   title="<fmt:message key="user.timesheet.bookWeekOnProject" />">
						${row.projectAssignment.project.name}
					</a>
				</td>
				
				<td class="projectCode">${row.projectAssignment.project.projectCode}</td>
				<td class="customer">${row.projectAssignment.project.customer.name}</td>
				<td class="spacer">&nbsp;</td>

				<c:forEach items="${row.timesheetCells}" var="cell" varStatus="status">
					<c:choose>
						<c:when test="${status.count == 1}">
							<td class="sunday">
						</c:when>
		
						<c:when test="${status.count == 7}">
							<td class="saturday">
						</c:when>
						
						<c:otherwise>
							<td class="weekday">
						</c:otherwise>
					</c:choose>
		
					<c:choose>
						<c:when test="${cell.valid}">
							<input type="text"
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

		<tr class="totalRow">
			<td colspan="9">&nbsp;<fmt:message key="user.timesheet.total" />:</th>
			<td colspan="3" id="totalHours" style="text-align: right;padding-right: 7px">0</th>
		</tr>
		
		</table>
		
		</td><td width="5">&nbsp;</td></tr></table>

			<div class="BlueLeftBottom">
				<div class="BlueRightBottom">
					&nbsp;
				</div>
			</div>			
		</div>
	
		<br>
		
		<div class="BlueFrame">
			<div class="BlueLeftTop">
				<div class="BlueRightTop">
					&nbsp;
				</div>
			</div>

			<table class="timesheetCommentsTable" cellpadding="0" cellspacing="0">

			<tr>
				<td style="width: 12px" rowspan="2">&nbsp;</td>

				<td valign="top">
					<fmt:message key="user.timesheet.weekComments" />:
				</td>
				
				<td align="right">
					<textarea name="comment" id="comment"
								rows="3" cols="40"
								wrap="virtual">${timesheet.comment.comment}</textarea>
				</td>

				<td style="width: 12px" rowspan="2">&nbsp;</td>
			</tr>

			<tr>
				<td><input type="submit" id="submitButton" value="reset" onClick="return resetTotal();"></td>
				<td align="right"><input type="submit" id="submitButton" value="<fmt:message key="user.timesheet.storeAndNext" />" onClick="return makeFormSubmittable();"></td>
			</tr>

		</table>

			<div class="BlueLeftBottom">
				<div class="BlueRightBottom">
					&nbsp;
				</div>
			</div>			
		</div>
	
		<br>
		
		<div class="GreyFrameFooter">
			<p>
			</p>
		</div>				
	</div>	
</div>
</form>

<script>
	updateTotal();
	
	bindTimesheetForm();
	
	<c:if test="${errorCommentTooLong}">
		alert(errorCommentTooLong);
	</c:if>	
</script>
