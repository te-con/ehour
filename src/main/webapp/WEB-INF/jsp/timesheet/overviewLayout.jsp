<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<script src="../js/dojo.js" type="text/javascript"></script>
<script src="../js/base.js" type="text/javascript"></script>

<script type="text/javascript">
	dojo.require("dojo.io.*");	
	dojo.require("dojo.event.*");		

	function enterTimesheet(day, month, year)
	{
		location.href = '<c:url value="/eh/enterTimesheet.do" />?mutable=1&day=' + day + '&month=' + month + '&year=' + year;
	}
	
</script>


<script src="<c:url value="/js/navCalendar.jsp" />" type="text/javascript"></script>


<tiles:insert page="/WEB-INF/jsp/timesheet/overviewProjects.jsp" />

<br><br><br>

<tiles:insert page="/WEB-INF/jsp/timesheet/overviewMonth.jsp" />
