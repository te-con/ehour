<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

	<c:forEach items="${users}" var="user">
		<a href="" onClick="return editUser(${user.userId})">
					${user.lastName}, ${user.firstName}
		</a>
			<c:if test="${!user.active}">*</c:if>
		<br>
	</c:forEach>
