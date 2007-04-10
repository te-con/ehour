<%@ page contentType="text/html; charset=UTF-8"%>
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
	var loadingMsg = "<fmt:message key="general.loading" />";	
	var defaultText= "<fmt:message key="admin.user.filter" />...";
</script>

<script src="../../../js/dojo.js" type="text/javascript"></script>
<script src="<c:url value="/js/validation.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/base.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/admin/userAdmin.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/admin/baseAdmin.js" />" type="text/javascript"></script>


<div class="ContentFrame" style="max-width: 1200px">
	<table class="contentTable" cellspacing="2">
		<tr>
			<td valign="top" width="270">
				<h1><fmt:message key="admin.user.title" /></h1>
				<div class="GreyFrame" style="width: 260px;">
					<h3>&nbsp;</h3>

					<div class="GreyFrameBody">
						
						<form onSubmit="return false;">
							<p>
								<input class="textInput" 
										type="text" 
										name="filter" 
										value="<fmt:message key="admin.user.filter" />..."
										style="margin-bottom: 0;color: #aaaaaa; margin-bottom: 3px"
										size="30" 
										id="filterInput"><br>
								<fmt:message key="admin.user.hideInactive" />: <input style="margin-bottom: 0" type="checkbox" id="hideInactive" name="hideInactive" checked><br>
							</p>
						</form>
						
						
						<div class="BlueFrame" style="width: 220px; padding-top: 0; margin-top: 10px">
							<div class="BlueLeftTop">
								<div class="BlueRightTop">
									&nbsp;
								</div>
							</div>				
							
							<div class="adminListScroll">
								<span id="listUsersSpan">
									<tiles:insert page="../user/listUsers.jsp" />
								</span>
							</div>
								
							<div class="BlueLeftBottom">
								<div class="BlueRightBottom">
									&nbsp;
								</div>
							</div>			
						</div>									

						<p style="text-align: right;margin: 5px 20px 0 0">
							<a href="" onClick="return showAddForm()"><fmt:message key="admin.user.addUser" /></a>
						</p>
					</div>
					
					<br>
					
					<div class="GreyFrameFooter">
						<p>
						</p>
					</div>				
				</div>	
			</td>
			
			<td valign="top">
				<span id="userFormSpan">
					<tiles:insert page="/eh/admin/user/addUserForm.do" />
				</span>
			</td>
		</tr>
		
		<tr>
			<td colspan="2" align="right" style="text-align: right; margin-right: 20px; color: #233e55" id="statusMessage">
				&nbsp;
			</td>
		</tr>
	</table>
</div>
