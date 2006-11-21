<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

	<c:forEach items="${userDepartments}" var="userDept">
		<tr>
			<td><c:out value="${userDept.name}" /></td>
			<td>&nbsp;</td>
			<td><c:out value="${fn:length(userDept.users)}" /></td>
			<td><c:if test="${fn:length(userDept.users) == 0}"><fmt:message key="general.delete" /></c:if></td>
			<td>&nbsp;</td>			
			<td><a href=""
				onClick="return editDepartment(<c:out value="${userDept.departmentId}" />)"><fmt:message key="general.edit" /></a></td>
		</tr>
	</c:forEach>