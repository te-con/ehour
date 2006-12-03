<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<form id="UserForm" method="post" action="editUser.do">

<input type="hidden" name="userId"  value="${user.userId}">
<input type="hidden" id="filterForm" name="filter" value="">
<input type="hidden" id="inActiveForm" name="hideInactive" value="">

<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td colspan="2">
			<c:choose>
				<c:when test="${user == null || user == ''}">
					<fmt:message key="admin.user.addUser" />
				</c:when>
				
				<c:otherwise>
					<fmt:message key="admin.user.editUser" />
				</c:otherwise>
			</c:choose>
	</tr>

	<tr>
    	<td colspan="2"><img src="<c:url  value="/img/eh_pixel.gif" />" alt="pix" height="1" width="100%"><br></td>
	</tr>

	
	<tr>
		<td><fmt:message key="admin.user.username" />:</td>
		<td><input class="normtxt" type="text" name="username" size="30" value="${user.username}" onChange="return checkUserExists(this.value, '${user.username}')"></td>
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
		<td><input class="normtxt"  type="text" name="firstName" size="30" value="${user.firstName}"></td>
		<td></td>		
	</tr>

	<tr>
		<td><fmt:message key="admin.user.lastName" />:</td>
		<td><input class="normtxt"  type="text" name="lastName" size="30" value="${user.lastName}"></td>
		<td id="lastNameError" style="color: red"></td>
	</tr>
	
	<tr>
		<td><fmt:message key="admin.user.email" />:</td>
		<td><input class="normtxt"  type="text" name="email" size="30" value="${user.email}"></td>
		<td id="emailError" style="color: red"></td>
	</tr>

	<tr>
		<td><fmt:message key="admin.user.department" />:</td>
		<td>
			<select class="normtxt" name="departmentId">
<c:forEach items="${userDepartments}" var="userDepartment">
	<option value="${userDepartment.departmentId}" 
		<c:if test="${userDepartment.departmentId == user.userDepartment.departmentId}">
			SELECTED
		</c:if>
	> ${userDepartment.name}
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
	<option value="${role.role}"
		<c:if test="${fn:contains(userRolesString, role.role)}">
			SELECTED
		</c:if>
	
	>${role.roleName}
</c:forEach>		
			</select>
		</td>
		<td valign="top" id="userRoleError" style="color: red"></td>	
	</tr>
	
	<tr>
		<td valign="top"><fmt:message key="general.active" />:</td>
		<td><input class="normtxt" type="checkbox" name="active" <c:if test="${user == null || user.active}">checked</c:if>></td>
	</tr>	
	
	<tr>
		<td colspan="2" align="right">
			<c:choose>
				<c:when test="${user == null || user == ''}">
					<input type="submit" class="redSubmit" value="<fmt:message key="general.add" />">
				</c:when>
				
				<c:otherwise>
					<input type="submit" class="redSubmit" value="<fmt:message key="general.edit" />">
				</c:otherwise>
			</c:choose>		
		</td>
		
		<td>&nbsp;</td>
	</tr>
</table>

</form>
