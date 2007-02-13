<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<!-- spanTarget: form -->

<form id="UserDepartmentForm" method="post" action="editUserDepartment.do">

<input type="hidden" id="departmentId" name="departmentId" value="${department.departmentId}">

<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td colspan="3">
			<c:choose>
				<c:when test="${department == null || department.departmentId == null}">
					<fmt:message key="admin.dept.addDepartment" />
				</c:when>
				
				<c:otherwise>
					<fmt:message key="admin.dept.editDepartment" />
				</c:otherwise>
			</c:choose>
		</td>	
	</tr>

	<tr>
    	<td colspan="3"><img src="<c:url  value="/img/eh_pixel.gif" />" alt="pix" height="1" width="100%"><br></td>
	</tr>

	
	<tr>
		<td height="20"><fmt:message key="admin.dept.name" />:</td>
		<td><input class="normtxt"  type="text" name="name" maxlength="128" size="30" value="${department.name}"></td>


		<td id="departmentNameError" style="color: red"><html:errors property="name" /></td>
		
		<td rowspan="4" valign="top">
			<c:if test="${department != null && department.departmentId != null}">		
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
			</c:if>
		</td>
	</tr>

	<tr>
		<td height="20"><fmt:message key="admin.dept.code" />:</td>
		<td><input class="normtxt"  type="text" maxlength="32" name="code" value="${department.code}" size="30"></td>
		<td id="departmentCodeError" style="color: red"><html:errors property="code" /></td>
	</tr>
	
	<tr>
		<td>
			<c:if test="${department != null && department.departmentId != null}">
				<a href="" onClick="return deleteDepartment(${department.departmentId})"><fmt:message key="general.delete" /></a>
			</c:if>
		</td>

		<td align="right">
			<c:choose>
				<c:when test="${department == null || department.departmentId == null}">
					<input type="submit" class="redSubmit" value="<fmt:message key="general.add" />">
				</c:when>
				
				<c:otherwise>
					<input type="submit" class="redSubmit" value="<fmt:message key="general.edit" />">
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	
	
	<tr>
		<td colspan="2" style="color: red">
			<html:errors property="delete" />
		</td>
	</tr>
</table>
</form>
