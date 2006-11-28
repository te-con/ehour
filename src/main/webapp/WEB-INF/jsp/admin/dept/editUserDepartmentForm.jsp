<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<form id="UserDepartmentForm" method="post" action="editUserDepartment.do">

<input type="hidden" id="departmentId" name="departmentId" value="${department.departmentId}">

<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td colspan="3"><fmt:message key="admin.dept.editDepartment" /></td>
	</tr>

	<tr>
    	<td colspan="3"><img src="<c:url  value="/img/eh_pixel.gif" />" alt="pix" height="1" width="100%"><br></td>
	</tr>

	
	<tr>
		<td height="20"><fmt:message key="admin.dept.name" />:</td>
		<td><input class="normtxt"  type="text" name="name" size="30" value="${department.name}"></td>

		<td rowspan=2>
			&nbsp;&nbsp;
		</td>
		
		<td rowspan="4" valign="top">
			<b><fmt:message key="admin.dept.users" /></b>
			<br>
			
			<c:set var="overflow" value="0" />
			
			<c:forEach var="user" items="${department.users}" varStatus="i">
				<c:choose>
					<c:when test="${i.count < 10}">
						${user.firstName}&nbsp;${user.lastName}<br>
					</c:when>
					<c:otherwise>
						<c:set var="overflow" value="1" />					
					</c:otherwise>
				</c:choose>
			</c:forEach>
			
			<c:if test="${overflow == 1}">
				...
			</c:if>
		</td>
	</tr>

	<tr>
		<td height="20"><fmt:message key="admin.dept.code" />:</td>
		<td><input class="normtxt"  type="text" name="code" value="${department.code}" size="30"></td>
	</tr>
	
	<tr>
		<td colspan="2" align="right">
			<input type="submit" class="redSubmit" value="<fmt:message key="general.edit" />">
		</td>
	</tr>
	
	<tr>
		<td colspan="2" height="100%">
			&nbsp;
		</td>
	</tr>
</table>
</form>
