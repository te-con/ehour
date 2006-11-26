<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<form id="UserDepartmentForm" method="post" action="editUserDepartment.do">

<input type="hidden" id="departmentId" name="departmentId" value="<c:out value="${department.departmentId}" />">

<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td colspan="3"><fmt:message key="admin.dept.editDepartment" /></td>
	</tr>

	<tr>
    	<td colspan="3"><img src="<c:url  value="/img/eh_pixel.gif" />" alt="pix" height="1" width="100%"><br></td>
	</tr>

	
	<tr>
		<td><fmt:message key="admin.dept.name" />:</td>
		<td><input class="normtxt"  type="text" name="name" size="30" value="<c:out value="${department.name}" />"></td>

		<td rowspan=2>
			&nbsp;&nbsp;
		</td>
		
		<td rowspan="2" valign="top">
			<b><fmt:message key="admin.dept.users" /></b>
			<br>
			
			<c:forEach var="user" items="${department.users}">
				<c:out value="${user.firstName}" />&nbsp;<c:out value="${user.lastName}" /><br>
			</c:forEach>
		</td>
	</tr>

	<tr>
		<td><fmt:message key="admin.dept.code" />:</td>
		<td><input class="normtxt"  type="text" name="code" value="<c:out value="${department.code}" />" size="30"></td>
	</tr>
	
	<tr>
		<td colspan="2" align="right">
			<input type="submit" class="redSubmit" value="<fmt:message key="general.edit" />">
		</td>
	</tr>
</table>
</form>
