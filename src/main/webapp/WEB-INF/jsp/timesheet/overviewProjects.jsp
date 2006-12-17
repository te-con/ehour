<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<table class="contentTable">

	<tr>
		<td colspan="3">
			Bla bla
		</td>
	</tr>
	
	<tr>
		<th>Project</th>
		<th>Booked hours</th>
		<th>Turnover</th>
	</tr>
	
	<c:forEach items="${timesheetOverview.projectHours}" var="projectReport">
	
	<tr>
		<td>${projectReport.projectAssignment.project.name}</td>
		<td>${projectReport.hours}</td>
		<td><fmt:formatNumber type="currency" value="${projectReport.turnOver}" /></td>
	</tr>
	</c:forEach>
</table>