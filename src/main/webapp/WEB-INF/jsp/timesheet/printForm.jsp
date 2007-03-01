<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- spanTarget: content -->
<fmt:formatDate value="${printDate.time}" pattern="MMMM yyyy" />

<c:forEach items="${projectAssignments}" var="projectAssignment">
	${projectAssignment.project.name}<br>
</c:forEach>