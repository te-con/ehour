<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!-- spanTarget:form -->

<form id="ProjectForm" method="post" action="editProject.do">

<input type="hidden" name="projectId"  value="${project.projectId}">
<input type="hidden" id="inActiveForm" name="hideInactive" value="true">

<h1><c:choose>
		<c:when test="${project == null || project == ''}">
			<fmt:message key="admin.project.addProject" />
		</c:when>
		
		<c:otherwise>
			<fmt:message key="admin.project.editProject" />
		</c:otherwise>
	</c:choose></h1>

<div class="GreyFrame">
	<h3>&nbsp;</h3>

	<div class="GreyFrameBody">			

<table class="contentTable" cellspacing="2">
	<tr>
		<td width="20%"><fmt:message key="admin.project.name" />:</td>
		<td><input class="textInputSmall" type="text" name="name" size="30" value="${project.name}"></td>
		<td id="projectNameError" style="color: red"></td>
	</tr>

	<tr>
		<td><fmt:message key="admin.project.code" />:</td>
		<td><input class="textInputSmall" type="text" name="projectCode" size="30" value="${project.projectCode}"></td>
		<td id="projectCodeError" style="color: red"></td>
	</tr>

	<tr>
		<td><fmt:message key="admin.project.customer" />:</td>
		<td>
			<select class="textInputSmall" name="customerId">
<c:forEach items="${customers}" var="customer">
	<option value="${customer.customerId}" 
		<c:if test="${customer.customerId== project.customer.customerId}">
			SELECTED
		</c:if>
	> ${customer.name}
</c:forEach>
			</select>
		</td>
		<td></td>		
	</tr>		

	<tr>
		<td valign="top"><fmt:message key="admin.project.description" />:</td>
		<td><textarea name="description" class="textInputSmall" wrap="virtual" cols="40" rows="2">${project.description}</textarea>
		</td>
		<td></td>
	</tr>
	
	<tr>
		<td><fmt:message key="admin.project.contact" />:</td>
		<td><input class="textInputSmall" type="text" name="contact" size="30" value="${project.contact}"></td>
		<td></td>
	</tr>	

	<tr>
		<td valign="top"><fmt:message key="admin.project.defaultProject" />:</td>
		<td><input class="textInputSmall" type="checkbox" name="defaultProject" <c:if test="${project != null && project.defaultProject}">checked</c:if>></td>
	</tr>	


	<tr>
		<td valign="top"><fmt:message key="general.active" />:</td>
		<td><input class="textInputSmall" type="checkbox" name="active" <c:if test="${project == null || project.active}">checked</c:if>></td>
	</tr>	
	
	<tr>
		<td colspan="2" style="text-align: right">
			<c:choose>
				<c:when test="${project == null || project == ''}">
					<input type="submit" class="submitButtonBlue" value="<fmt:message key="general.add" />">
				</c:when>
				
				<c:otherwise>
					<input type="submit" class="submitButtonBlue" value="<fmt:message key="general.edit" />">
				</c:otherwise>
			</c:choose>		
		</td>
		
		<td>&nbsp;</td>
	</tr>
</table>

</form>

	</div>
						
	<div class="GreyFrameFooter">
		<p>
		</p>
	</div>				
</div>						
