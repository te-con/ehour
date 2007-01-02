<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!-- spanTarget: list -->

<table CLASS="contentTable" CELLSPACING=0>
<c:forEach items="${userDepartments}" var="userDept">
	<tr>
		<td><a href=""
			onClick="return editUserDepartment(${userDept.departmentId})">${userDept.name}</a>
		</td>
		<td>&nbsp;&nbsp;&nbsp;</td>			
		<td align="right">
			${fn:length(userDept.users)} <fmt:message key="admin.dept.users" />
		</td>
	</tr>
</c:forEach>
</table>
	