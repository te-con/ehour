<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<table class="contentTable">

	<tr>
		<td colspan="3">
			<h1>Project overview for <fmt:formatDate value="${timesheetOverviewMonth.time}" pattern="MMMMM yyyy" /></h1>
			<br><br>
		</td>
	</tr>
	
	<tr>
		<td>Project</td>
		<td>Booked hours</td>
		<td>Turnover</td>
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