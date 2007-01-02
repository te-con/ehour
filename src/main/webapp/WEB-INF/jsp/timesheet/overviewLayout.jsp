<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="../../js/dojo.js" type="text/javascript"></script>
<script src="../../js/base.js" type="text/javascript"></script>

<script type="text/javascript">
	dojo.require("dojo.io.*");	
	dojo.require("dojo.event.*");		

	function enterTimesheet(day, month, year)
	{
		location.href = '<c:url value="/eh/enterTimesheet.do" />?mutable=1&day=' + day + '&month=' + month + '&year=' + year;
	}
</script>

<script src="<c:url value="/js/navCalendarOverview.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/timesheet.js" />" type="text/javascript"></script>

<div id="overviewSpan">
	<tiles:insert page="overview.jsp" />
</div>

