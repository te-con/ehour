<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- spanTarget: projectList -->

<select multiple="multiple" name="projectId" id="projectId" size="4"  class="criteriaInput">
	<option value="-1"
		<c:if test="${criteria.userCriteria.emptyProjects}">
			SELECTED
		</c:if>>{<fmt:message key="report.criteria.all" />}
	<c:forEach items="${criteria.availableCriteria.projects}" var="project">
		<option value="${project.projectId}"
		<c:set var="pId" value="${project.projectId}-" />
		<c:if test="${fn:contains(criteria.userCriteria.projectsAsString,  pId)}">
				SELECTED
		</c:if>
		>${project.name}
	</c:forEach>
</select>