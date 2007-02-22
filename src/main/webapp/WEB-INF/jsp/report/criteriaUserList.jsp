<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- spanTarget: userList -->

<select multiple="multiple" name="userIds" id="userId" size="4" class="textInputSmall" stfyle="width: 100%">
	<option value="-1"
		<c:if test="${criteria.userCriteria.emptyUsers}">
			SELECTED
		</c:if>>{<fmt:message key="report.criteria.all" />}
	
	<c:forEach items="${criteria.availableCriteria.users}" var="user">
		<option value="${user.userId}"
		<c:set var="uId" value="${user.userId}-" />
		<c:if test="${fn:contains(criteria.userCriteria.usersAsString,  uId)}">
				SELECTED
		</c:if>
		>${user.lastName}, ${user.firstName}
	</c:forEach>
</select>