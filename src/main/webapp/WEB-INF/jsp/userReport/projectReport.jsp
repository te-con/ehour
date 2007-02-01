<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:forEach items="${report.customers}" var="customer">
	${customer.name}<br>
	
	<c:forEach items="${report.reportValues[customer]}" var="pag">
		&nbsp;${pag.projectAssignment.project.name}-${pag.hours}--${pag.turnOver}<Br>
	</c:forEach>
	
</c:forEach>