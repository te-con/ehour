<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script src="<c:url value="/js/base.js" />" type="text/javascript"></script>

<div id="reportContent">
	<tiles:insert page="/eh/userReport/projectReport.do" />
</div>
