<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td><fmt:message key="general.department" /></td>
		<td>&nbsp;</td>
		<td><fmt:message key="admin.dept.users" /></td>
		<td>&nbsp;</td>
		<td colspan=2></td>
	</tr>

	<tr>
		<td colspan="6"><img src="<c:url  value="/img/eh_pixel.gif" />"
			alt="pix" height="1" width="100%"><br>
		</td>
	</tr>

	
	<c:forEach items="${userDepartments}" var="userDept">
		<tr>
			<td>${userDept.name}</td>
			<td>&nbsp;</td>
			<td>${fn:length(userDept.users)}</td>
			<td><c:if test="${fn:length(userDept.users) == 0}">
				<a href="" onClick="return deleteDepartment(${userDept.departmentId})"><fmt:message key="general.delete" /></a></c:if></td>
			<td>&nbsp;</td>			
			<td><a href=""
				onClick="return editDepartment(${userDept.departmentId})"><fmt:message key="general.edit" /></a></td>
		</tr>
	</c:forEach>


	<tr>
		<td colspan="6"><img src="<c:url  value="/img/eh_pixel.gif" />"
			alt="pix" height="1" width="100%"><br>
		</td>
	</tr>
		
		<tr>
			<td colspan=6 align=right>
				<a href="" onClick="return showAddForm()"><fmt:message key="admin.dept.addDepartment" /></a>
			</td>
		</tr>

</table>
	