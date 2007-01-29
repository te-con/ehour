<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/ehour-common.tld" prefix="ehour" %>

<!-- spanTarget: overview -->
<!-- project overview -->
<div class="MonthAggregate">
	<h1><fmt:formatDate value="${timesheetOverviewMonth.time}" pattern="MMMMM yyyy" />: <fmt:message key="user.overview.aggregatedProject" /></h1>
</div>

<div class="MonthAggregateFrame">
	<h3>&nbsp;</h3>
	
	<div class="MonthAggregateBody">
		<table cellpadding="0" cellspacing="10">
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
		
		<c:forEach items="${timesheetOverview.projectHours}" var="projectReport">
		
		<tr>
			<td>${projectReport.projectAssignment.project.name}</td>
<c:if test="${config.showTurnover}">
			<td><fmt:formatNumber type="currency" value="${projectReport.projectAssignment.hourlyRate}" /></td>
</c:if>			
			<td><fmt:formatNumber value="${projectReport.hours}" maxFractionDigits="2" /> </td>
<c:if test="${config.showTurnover}">
			<td><fmt:formatNumber type="currency" value="${projectReport.turnOver}" /></td>
</c:if>
		</tr>
		
		</c:forEach>
	</table>
	</div>
	
	<div class="MonthAggregateFrameFooter">
		<p>
		</p>
	</div>	
</div>

<br><br>

<div class="MonthAggregate">	
	<h1><fmt:formatDate value="${timesheetOverviewMonth.time}" pattern="MMMMM yyyy" />: <fmt:message key="user.overview.monthOverview" /></h1>
</div>

<div class="MonthOverviewFrame">
	<h3>&nbsp;</h3>
	
	<div class="MOBlueFrame">
		<div class="MOBLueLeftTop">
			<div class="MOBLueRightTop">
				&nbsp;
			</div>
		</div>	

		<table class="month" cellpadding="0" cellspacing="0">
			<tr class="weekColumnRow">
				<td class="weekNumber">&nbsp;</td>
				<td>S</td>
				<td>M</td>
				<td>T</td>
				<td>W</td>
				<td>T</td>
				<td>F</td>
				<td class="lastChild">S</td>
			</tr>	
			
		<ehour:overviewCalendar calendar="${timesheetOverviewMonth}"
								timesheetEntries="${timesheetOverview.timesheetEntries}" />
	</table>
	
		<div class="MOBLueLeftBottom">
			<div class="MOBLueRightBottom">
				&nbsp;
			</div>
		</div>			
	</div>

	<br>
	
	<div class="MonthOverviewFrameFooter">
		<p>
		</p>
	</div>				
</div>	