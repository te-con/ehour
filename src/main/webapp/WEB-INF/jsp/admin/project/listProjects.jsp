<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!-- spanTarget: list -->

<c:forEach items="${projects}" var="project">
	<a href="" onClick="return editProject(${project.projectId})">
				${project.name}
	</a>
		<c:if test="${!project.active}">*</c:if>
	<br>
</c:forEach>
