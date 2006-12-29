<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="/WEB-INF/ehour-common.tld" prefix="ehour" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<span id="navCalendarSpan">

<table CLASS="cps_table" CELLSPACING=0>
	<TR>
		<TH style="vertical-align: middle"><a href="" onClick="return changeCalMonth(${navCalPrevMonth})"><img src="<c:url value="/img/left.gif" />" border=0></a></th>
		<TH COLSPAN=5><fmt:formatDate value="${navCalCurCalMonth.time}" pattern="MMMMM yyyy" /></TH>
		<TH style="vertical-align: middle"><a href="" onClick="return changeCalMonth(${navCalNextMonth})"><img src="<c:url value="/img/right.gif" />" border=0></a></th>
	</TR>

	<ehour:navCalendar bookedDays="${navCalData}" calendar="${navCalCurCalMonth}" userId="${navCalUserId}" />
</TABLE>

</span>