<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>
<TABLE CELLSPACING=0>
	<TR>
		<th valign="bottom">
<authz:authorize ifAllGranted="ROLE_CONSULTANT">
			<a href="<c:url value="/eh/timesheet/overview.do" />"><fmt:message key="nav.monthOverview" /></a>
			&nbsp;&nbsp;|&nbsp;&nbsp;
			<a href="<c:url value="/eh/timesheet/viewTimesheet.do" />"><fmt:message key="nav.enterTimesheet" /></a>
			&nbsp;&nbsp;|&nbsp;&nbsp;
			<a href="<c:url value="/eh/timesheet/printTimesheet.do" />"><fmt:message key="nav.printTimesheet" /></a>
			&nbsp;&nbsp;|&nbsp;&nbsp;
</authz:authorize>			

<authz:authorize ifAllGranted="ROLE_REPORT">
			<a href="<c:url value="/eh/report/index.do" />"><fmt:message key="nav.report" /></a>
			&nbsp;&nbsp;|&nbsp;&nbsp;
			
			<c:set var="reportSet" value="true" />
</authz:authorize>				

<authz:authorize ifAllGranted="ROLE_CONSULTANT">
			<c:if test="${reportSet == null}">
				<a HREF="<c:url value="/eh/userReport/index.do" />"><fmt:message key="nav.reporting" /></a>
				&nbsp;&nbsp;|&nbsp;&nbsp;
			</c:if>
</authz:authorize>				

			
<authz:authorize ifAllGranted="ROLE_ADMIN">
			<a href="<c:url value="/eh/admin/index.do" />"><fmt:message key="nav.admin" /></a>
			&nbsp;&nbsp;|&nbsp;&nbsp;
</authz:authorize>			
	
			<a href="<c:url value="/eh/logOff.do" />"><fmt:message key="nav.logOff" /></a>
          </th>
	</TR>

</TABLE>
