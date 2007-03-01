<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<script src="../../js/dojo.js" type="text/javascript"></script>
<script src="<c:url value="/js/base.js" />" type="text/javascript"></script>

<script type="text/javascript">
	dojo.require("dojo.io.*");	
	dojo.require("dojo.event.*");		
</script>

<script src="<c:url value="/js/navCalendarOverview.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/printTimesheet.js" />" type="text/javascript"></script>

<div id="contentSpan">
	<tiles:insert page="${subContent}" />
</div>

