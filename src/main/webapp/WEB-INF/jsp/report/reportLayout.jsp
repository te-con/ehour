<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<script src="../../js/dojo.js" type="text/javascript"></script>
<script src="<c:url value="/js/base.js" />" type="text/javascript"></script>

<script type="text/javascript">
	dojo.require("dojo.io.*");	
	dojo.require("dojo.event.*");		
	dojo.require("dojo.widget.TabContainer");
	dojo.require("dojo.widget.LinkPane");
	dojo.require("dojo.widget.ContentPane");
	dojo.require("dojo.widget.LayoutContainer");
	dojo.require("dojo.widget.TitlePane");
	dojo.require("dojo.widget.DropdownDatePicker");

	
	var defaultText = "<fmt:message key="report.criteria.filterUsersOn" />";
</script>

<script src="<c:url value="/js/navCalendarOverview.js" />" type="text/javascript"></script>

<div id="criteriaTarget">
	<tiles:insert page="${criteriaTile}" />
</div>

<div id="reportTarget">
	<tiles:insert page="${reportTile}" />
</div>
