<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<script src="../../js/dojo.js" type="text/javascript"></script>
<script src="<c:url value="/js/base.js" />" type="text/javascript"></script>

<script type="text/javascript">
	dojo.require("dojo.io.*");	
	dojo.require("dojo.event.*");		
	
	var maxHoursPerDay = ${config.completeDayHours};
	var errorNotValidNumber = '<fmt:message key="user.timesheet.errorNotValidNumber" />';
	var error24HoursMax = '<fmt:message key="user.timesheet.error24HoursMax" />';
	var errorCommentTooLong= '<fmt:message key="user.timesheet.errorCommentTooLong" />';
</script>

<script src="<c:url value="/js/navCalendarOverview.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/timesheet.js" />" type="text/javascript"></script>

<div id="overviewSpan">
	<tiles:insert page="${subContent}" />
</div>

