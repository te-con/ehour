<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>
<TABLE CELLSPACING=0>
	<TR>
		<th valign="bottom">
			<a href="<c:url value="/eh/timesheet/enterTimesheet.do" />"><fmt:message key="nav.enterTimesheet" /></a> |		
			<a onClick="alert('under construction');return false" href="<c:url value="/eh/printTimesheetSelection.do" />"><fmt:message key="nav.printTimesheet" /></a> |
			<a href="<c:url value="/eh/timesheet/overview.do" />"><fmt:message key="nav.monthOverview" /></a> |
			<a HREF="<c:url value="/eh/userReport/index.do" />"><fmt:message key="nav.reporting" /></a>
<authz:authorize ifAllGranted="ROLE_ADMIN"> | 
			<a href="<c:url value="/eh/admin/index.do" />"><fmt:message key="nav.admin" /></a>
</authz:authorize>				
<authz:authorize ifAllGranted="ROLE_REPORT"> | 
			<a href="<c:url value="/eh/report/index.do" />"><fmt:message key="nav.report" /></a>
</authz:authorize>				
			| <a href="<c:url value="/eh/logOff.do" />"><fmt:message key="nav.logOff" /></a>
          </th>
	</TR>

</TABLE>
