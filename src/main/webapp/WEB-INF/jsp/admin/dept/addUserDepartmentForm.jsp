<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<form id="UserDepartmentForm" method="post" action="editUserDepartment.do">

<input type="hidden" id="departmentId" name="departmentId" value="">

<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td colspan="2"><fmt:message key="admin.dept.addDepartment" /></td>
	</tr>

	<tr>
    	<td colspan="2"><img src="<c:url  value="/img/eh_pixel.gif" />" alt="pix" height="1" width="100%"><br></td>
	</tr>

	
	<tr>
		<td><fmt:message key="admin.dept.name" />:</td>
		<td><input class="normtxt"  type="text" name="name"></td>
	</tr>

	<tr>
		<td><fmt:message key="admin.dept.code" />:</td>
		<td><input class="normtxt"  type="text" name="code"></td>
	</tr>
	
	<tr>
		<td colspan="2" align="right">
			<input type="submit" class="redSubmit" value="<fmt:message key="general.add" />">
		</td>
	</tr>
</table>
</form>