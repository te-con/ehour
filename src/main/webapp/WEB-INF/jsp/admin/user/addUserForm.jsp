<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<form id="UserForm" method="post" action="editUser.do" onSubmit="return validateForm('UserForm')">

<input type="hidden" name="userId" value="">
<input type="hidden" id="filterForm" name="filter" value="">
<input type="hidden" id="inActiveForm" name="hideInactive" value="">

<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td colspan="2"><fmt:message key="admin.user.addUser" /></td>
	</tr>

	<tr>
    	<td colspan="2"><img src="<c:url  value="/img/eh_pixel.gif" />" alt="pix" height="1" width="100%"><br></td>
	</tr>

	
	<tr>
		<td><fmt:message key="admin.user.username" />:</td>
		<td><input class="normtxt"  type="text" name="username" size="30" value="${user.username}" onChange="return checkUserExists(this.value)"></td>
		<td id="userNameError" style="color: red"></td>
	</tr>

	<tr>
		<td><fmt:message key="admin.user.password" />:</td>
		<td><input class="normtxt"  type="password" name="password" size="30"></td>
		<td id="passwordError" style="color: red"></td>
	</tr>

	<tr>
		<td><fmt:message key="admin.user.confirmPassword" />:</td>
		<td><input class="normtxt"  type="password" name="confirmPassword" size="30"></td>
		<td id="confirmPasswordError" style="color: red"></td>
	</tr>	

	<tr>
		<td><fmt:message key="admin.user.firstName" />:</td>
		<td><input class="normtxt"  type="text" name="firstName" size="30"></td>
		<td></td>		
	</tr>

	<tr>
		<td><fmt:message key="admin.user.lastName" />:</td>
		<td><input class="normtxt"  type="text" name="lastName" size="30"></td>
		<td id="lastNameError" style="color: red"></td>
	</tr>
	
	<tr>
		<td><fmt:message key="admin.user.email" />:</td>
		<td><input class="normtxt"  type="text" name="email" size="30"></td>
		<td id="emailError" style="color: red"></td>
	</tr>

	<tr>
		<td><fmt:message key="admin.user.department" />:</td>
		<td>
			<select class="normtxt" name="departmentId">
<c:forEach items="${userDepartments}" var="userDepartment">
	<option value="${userDepartment.departmentId}">${userDepartment.name}
</c:forEach>
			</select>
		</td>
		<td></td>		
	</tr>
	
	<tr>
		<td valign="top"><fmt:message key="admin.user.roles" />:</td>
		<td>
			<select class="normtxt" name="roles" multiple="true">
<c:forEach items="${userRoles}" var="role">
	<option value="${role.role}">${role.roleName}
</c:forEach>		
			</select>	
		</td>
		<td valign="top" id="userRoleError" style="color: red"></td>	
	</tr>
	<tr>
		<td colspan="2" align="right">
			<input type="submit" class="redSubmit" value="<fmt:message key="general.add" />">
		</td>
		
		<td>&nbsp;</td>
	</tr>
</table>

</form>
