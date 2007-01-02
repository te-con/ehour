<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/ehour-common.tld" prefix="ehour" %>

<!-- spanTarget: overview -->
<!-- project overview -->
	<table class="contentTable">
	
		<tr>
			<td colspan="3">
				<h1><fmt:message key="user.overview.projectOverviewFor" /> <fmt:formatDate value="${timesheetOverviewMonth.time}" pattern="MMMMM yyyy" /></h1>
			</td>
		</tr>
		
		<tr>
			<td><fmt:message key="user.overview.project" /></td>
			<td><fmt:message key="user.overview.bookedHours" /></td>
			<td><fmt:message key="user.overview.turnover" /></td>
		</tr>
		
				<tr>
					<td colspan="3"><img src="<c:url  value="/img/eh_pix.gif" />" width="100%" height="1" alt="pixel"><br></td>
				</tr>	
		
		<c:forEach items="${timesheetOverview.projectHours}" var="projectReport">
		
		<tr>
			<td>${projectReport.projectAssignment.project.name}</td>
			<td>${projectReport.hours}</td>
			<td><fmt:formatNumber type="currency" value="${projectReport.turnOver}" /></td>
		</tr>
		</c:forEach>
	</table>

	<br><br><br>
	
	<table CLASS="overview_table" name="overview_daydata" CELLSPACING=0>
	
	    <tr>
	        <td colspan="7">
	            <h1><fmt:message key="user.overview.monthOverview" /> <fmt:formatDate value="${calendar.date}" pattern="MMMMM yyyy" /></h1>
	        </td>
	        
	        <td align=right>
				<a href="" onClick="this.blur()" target="_blank">
					<img src="<c:url value="/img/print_off.gif" />" alt="print maandstaat" border=0
						onMouseover="this.src='<c:url value="/img/print_on.gif" />'"
						onMouseout="this.src='<c:url value="/img/print_off.gif" />'">
				</a>
	        </td>
	        
	    </tr>
	
		<ehour:overviewCalendar calendar="${timesheetOverviewMonth}"
								timesheetEntries="${timesheetOverview.timesheetEntries}" />
	</table>