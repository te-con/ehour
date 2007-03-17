<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/ehour-common.tld" prefix="ehour" %>

<!-- spanTarget: overview -->

<!-- project overview -->
<div class="ContentFrame">
	<h1><fmt:message key="user.overview.aggregatedProject" /></h1>
	<div class="GreyFrame">
		<h3>&nbsp;</h3>
		
		<div class="GreyFrameBody">
			<table class="MonthAggregateTable" cellpadding="0" cellspacing="0">
			<tr>
				<th class="firstCell"><fmt:message key="user.overview.project" /></th>
	<c:if test="${config.showTurnover}">			
				<th><fmt:message key="user.overview.rate" /></th>
	</c:if>
				<th><fmt:message key="user.overview.bookedHours" /></th>
	<c:if test="${config.showTurnover}">			
				<th><fmt:message key="user.overview.turnover" /></th>
	</c:if>
			</tr>
			
<!-- TODO: fix IE boxing
			<tr>
				<td colspan="4" height="1" style="height: 1px;padding: 0; margin: 0; background: #c1c1c1">

				</td>
			</tr>
 -->						
			<c:forEach items="${timesheetOverview.projectHours}" var="projectReport">
			
			<tr>
				<td>${projectReport.projectAssignment.project.name}</td>
	<c:if test="${config.showTurnover}">
				<td><fmt:formatNumber type="currency" value="${projectReport.projectAssignment.hourlyRate}" /></td>
	</c:if>			
				<td><fmt:formatNumber value="${projectReport.hours}" maxFractionDigits="2" /> </td>
	<c:if test="${config.showTurnover}">
				<td><fmt:formatNumber type="currency" value="${projectReport.turnOver}" currencyCode="EUR" /></td>
	</c:if>
			</tr>
			
			</c:forEach>
		</table>
		
		<br>
		</div>
		
		<div class="GreyFrameFooter">
			<p />
		</div>	
	</div>
</div>
<br><br>

<div class="ContentFrame">
	<div style="float: left; margin: 0 0 -5px 0; padding: 0">
		<h1><fmt:message key="user.overview.monthOverview" /></h1>
	</div>
	
	<div style="text-align: right;max-width: 719px;margin: 0 0 -5px 0; padding: 0">
		<a href="printTimesheet.do?zeroBased=0&month=<fmt:formatDate value="${timesheetOverviewMonth.time}" pattern="M" />&year=<fmt:formatDate value="${timesheetOverviewMonth.time}" pattern="yyyy" />">
			<img src="<c:url value="/img/print_off.gif" />"
				onMouseover="this.src='<c:url value="/img/print_on.gif" />'"
				onMouseout="this.src='<c:url value="/img/print_off.gif" />'"
				alt="<fmt:message key="user.overview.print" />"
			 border="0">
		</a>
	</div>

	<div class="GreyFrame">
		<h3>&nbsp;</h3>
		
		<div class="BlueFrame">
			<div class="BlueLeftTop">
				<div class="BlueRightTop">
					&nbsp;
				</div>
			</div>	
	
			<table cellpadding="0" cellspacing="0" class="MonthOverviewIEWorkAround">
				<tr>
					<td>
					<table class="MonthOverviewMonthTable" cellpadding="0" cellspacing="0">
						<tr class="weekColumnRow">
							<td class="weekNumber">&nbsp;</td>
							<td><fmt:message key="user.overview.sun" /></td>
							<td><fmt:message key="user.overview.mon" /></td>
							<td><fmt:message key="user.overview.tue" /></td>
							<td><fmt:message key="user.overview.wed" /></td>
							<td><fmt:message key="user.overview.thu" /></td>
							<td><fmt:message key="user.overview.fri" /></td>
							<td class="lastChild"><fmt:message key="user.overview.sat" /></td>
						</tr>	
						
					<ehour:overviewCalendar calendar="${timesheetOverviewMonth}"
											timesheetEntries="${timesheetOverview.timesheetEntries}" />
					</table>
					<td width="5">&nbsp</td>
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