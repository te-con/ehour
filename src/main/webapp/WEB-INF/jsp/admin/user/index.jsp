<%@ page contentType="text/html; charset=ASCII"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
	var ajaxError = "<fmt:message key="errors.ajax.general" />";
	var userExistError = "<fmt:message key="admin.user.errorUsernameExists" />";
	var usernameRequired = "<fmt:message key="admin.user.errorUsernameNotNull" />";	
	var passwordRequired = "<fmt:message key="admin.user.errorPasswordNotNull" />";		
	var lastNameRequired = "<fmt:message key="admin.user.errorLastNameNotNull" />";			
	var userRoleRequired = "<fmt:message key="admin.user.errorUserRoleNotNull" />";				
	var emailNotValid = "<fmt:message key="admin.user.errorMailNotValid" />";				
	var noPasswordMatch=  "<fmt:message key="admin.user.errorConfirmPassNeeded" />";
	var formSuccess = "<fmt:message key="admin.general.formSuccess" />";
	var sendingData = "<fmt:message key="general.submitting" />";

</script>

<script src="<c:url value="/js/validation.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/admin/user/userAdmin.js" />" type="text/javascript"></script>


<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td>
			<table CLASS="contentTable" CELLSPACING=2 width="100%">
				<tr>
					<td valign="top"><fmt:message key="admin.user.filter" />:</td>
					<td><form><input class="normtxt" type="text" name="filter"
								size="30" id="filterInput"></form>
					</td>
				</tr>

				<tr>
					<td valign="top"><fmt:message key="admin.user.hideInactive" />:
					<input class="normtxt" type="checkbox" id="hideInactive" name="hideInactive" checked></td>
				</tr>
			</table>
		</td>
		
		<td valign="top" align="right" style="color: #913023" id="statusMessage">
			&nbsp;
		</td>
	</tr>
	
	<tr>
		<td>
			<div class="userScroll">
				<span id="listUsersSpan">
					<tiles:insert page="listUsers.jsp" />
				</span>
			</div>
		</td>
		
		<td valign="top" rowspan="2">
			<span id="userFormSpan">
				<tiles:insert page="/eh/admin/user/addUserForm.do" />
			</span>
		</td>
	</tr>
	<tr>
		<td align=right>
			<a href="" onClick="return showAddForm()"><fmt:message key="admin.user.addUser" /></a>
		</td>
	</tr>
</table>
