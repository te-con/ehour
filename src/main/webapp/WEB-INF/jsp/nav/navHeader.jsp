<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>
<TABLE CELLSPACING=0>
	<TR>
		<th valign="bottom">
			<a href="<c:url value="/eh/timesheet/overview.do" />"><fmt:message key="nav.monthOverview" /></a>&nbsp;&nbsp;|&nbsp;&nbsp;
			<a href="" onClick="alert('under construction.\nfor now go to the overview and click on a week in the calendar');return false"><fmt:message key="nav.enterTimesheet" /></a>&nbsp;&nbsp;|&nbsp;&nbsp;		
			<a HREF="<c:url value="/eh/userReport/index.do" />"><fmt:message key="nav.reporting" /></a>
<authz:authorize ifAllGranted="ROLE_ADMIN">&nbsp;&nbsp;|&nbsp;&nbsp;
			<a href="<c:url value="/eh/admin/index.do" />"><fmt:message key="nav.admin" /></a>
</authz:authorize>				
<authz:authorize ifAllGranted="ROLE_REPORT">&nbsp;&nbsp;|&nbsp;&nbsp;
			<a href="<c:url value="/eh/report/index.do" />"><fmt:message key="nav.report" /></a>
</authz:authorize>				
			&nbsp;&nbsp;|&nbsp;&nbsp;<a href="<c:url value="/eh/logOff.do" />"><fmt:message key="nav.logOff" /></a>
          </th>
	</TR>

</TABLE>
