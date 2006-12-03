<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<form id="UserForm" method="post" action="editUser.do" onSubmit="return validateForm('UserForm')">

<input type="hidden" name="userId" value="${user.userId}">
<input type="hidden" id="filterForm" name="filter" value="">
<input type="hidden" id="inActiveForm" name="hideInactive" value="">


<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td colspan="2"><fmt:message key="admin.user.editUser" /></td>
	</tr>

	<tr>
    	<td colspan="2"><img src="<c:url  value="/img/eh_pixel.gif" />" alt="pix" height="1" width="100%"><br></td>
	</tr>

	
	<tr>
		<td><fmt:message key="admin.user.username" />:</td>
		<td><input class="normtxt"  type="text" name="username" size="30" value="${user.username}"></td>
	</tr>

	<tr>
		<td><fmt:message key="admin.user.password" />:</td>
		<td><input class="normtxt"  type="password" name="password" size="30"></td>
	</tr>

	<tr>
		<td><fmt:message key="admin.user.confirmPassword" />:</td>
		<td><input class="normtxt"  type="password" name="confirmPassword" size="30"></td>
	</tr>	

	<tr>
		<td><fmt:message key="admin.user.firstName" />:</td>
		<td><input class="normtxt"  type="text" name="firstName" size="30" value="${user.firstName}"></td>
	</tr>

	<tr>
		<td><fmt:message key="admin.user.lastName" />:</td>
		<td><input class="normtxt"  type="text" name="lastName" size="30"  value="${user.lastName}"></td>
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
	>${userDepartment.name}
</c:forEach>
			</select>
		</td>
	</tr>

	<tr>
		<td valign="top"><fmt:message key="general.active" />:</td>
		<td><input class="normtxt" type="checkbox" name="active" <c:if test="${user.active}">checked</c:if>></td>
	</tr>		
	

	
	<tr>
		<td colspan="2" align="right">
			<input type="submit" class="redSubmit" value="<fmt:message key="general.edit" />">
		</td>
	</tr>
</table>
</form>