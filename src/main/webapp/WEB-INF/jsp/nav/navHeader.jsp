<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>
	<TABLE CLASS="headerTable" CELLSPACING=0>
		<TR>
			<TD STYLE="padding-left: 10px; padding-top: 10px" width="145"><img SRC="<c:url value="/img/ehour.gif" />" width="64" alt="eHour v0.1"><br></TD>

			<TD valign="bottom">
				<a href="<c:url value="/eh/timesheet/overview.do" />"><fmt:message key="nav.timesheetOverview" /></a> |
				<a href="<c:url value="/eh/printTimesheetSelection.do" />"><fmt:message key="nav.printTimesheet" /></a> |
				<a HREF="<c:url value="/eh/projectOverview.do" />"><fmt:message key="nav.projectOverview" /></a>
<authz:authorize ifAllGranted="ROLE_ADMIN"> | 
				<a href="<c:url value="/eh/admin/index.do" />"><fmt:message key="nav.admin" /></a>
</authz:authorize>				
<authz:authorize ifAllGranted="ROLE_REPORT"> | 
				<a href="<c:url value="/eh/report/index.do" />"><fmt:message key="nav.report" /></a>
</authz:authorize>				
            </TD>

            <TD align="right" valign="bottom">
            	<fmt:message key="nav.welcome" />&nbsp;<authz:authentication operation="firstName"/>&nbsp;<authz:authentication operation="lastName"/>
            	&nbsp;&nbsp;
            </TD>
		</TR>

		<TR>
			<TD COLSPAN="3" style="padding-top: -1px"><img src="<c:url value="/img/eh_pixel.gif" />" width="100%" height="1" alt="pix"><br></TD>
		</TR>

		<TR>
			<TD COLSPAN="3" align="right">
				<a href="<c:url value="/eh/logOff.do" />"><fmt:message key="nav.logOff" /></a>
				&nbsp;&nbsp;
			</TD>
		</TR>



	</TABLE>
