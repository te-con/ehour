<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/ehour-common.tld" prefix="ehour" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>

	<script>
		function enterTimesheet(day, month, year)
		{
			location.href = '<c:url value="/eh/enterTimesheet.do" />?mutable=1&day=' + day + '&month=' + month + '&year=' + year;
		}
	</script>

<authz:authorize ifAllGranted="ROLE_CONSULTANT">
	<table CLASS="cps_table" CELLSPACING=0>
		<TR>
            <TH style="vertical-align: middle"><a href="<c:out value="${navCalPrevMonth}" />"><img src="<c:url value="/img/left.gif" />" border=0></a></th>
			<TH COLSPAN=5><fmt:formatDate value="${navCalCurCalMonth.time}" pattern="MMMMM yyyy" /></TH>
            <TH style="vertical-align: middle"><a href="<c:out value="${navCalNextMonth}" />"><img src="<c:url value="/img/right.gif" />" border=0></a></th>
		</TR>

		<ehour:navCalendar bookedDays="${navCalMonth}" calendar="${navCalCurCalMonth}" />
	</TABLE>
</authz:authorize>	
