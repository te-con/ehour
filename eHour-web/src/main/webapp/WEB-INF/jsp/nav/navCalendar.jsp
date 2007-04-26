<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/ehour-common.tld" prefix="ehour" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- spanTarget:navCalendar -->
<div class="NavCalFrame">
	<div class="NavCalFrameLeftTop">
		<div class="NavCalFrameRightTop">
			&nbsp;
		</div>
	</div>

	<table cellpadding="0" cellspacing="1">
		<tr>
			<th style="vertical-align: middle"><a href="" onClick="return changeCalMonth(${navCalPrevMonth})"><img src="<c:url value="/img/left.gif" />" border=0></a></th>
			<th colspan="5"><fmt:formatDate value="${navCalCurCalMonth.time}" pattern="MMMMM yyyy" /></th>
			<th style="vertical-align: middle"><a href="" onClick="return changeCalMonth(${navCalNextMonth})"><img src="<c:url value="/img/right.gif" />" border=0></a></th>
		</tr>

		<ehour:navCalendar bookedDays="${navCalData}" calendar="${navCalCurCalMonth}" userId="${navCalUserId}" />
	</table>
	

	<div class="NavCalFrameLeftBottom">
		<div class="NavCalFrameRightBottom">
			&nbsp;
		</div>
	</div>		
</div>
