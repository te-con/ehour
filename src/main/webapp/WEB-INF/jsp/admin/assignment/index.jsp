<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
	var ajaxError = "<fmt:message key="errors.ajax.general" />";
	var formSuccess = "<fmt:message key="admin.general.formSuccess" />";
	var sendingData = "<fmt:message key="general.submitting" />";
	var loadingMsg = "<fmt:message key="general.loading" />";	
	var rateNotValid = "<fmt:message key="admin.assignment.errorRateNotValid" />";	
	var noDeleteMessage = "<fmt:message key="admin.assignment.noDelete" />";		

</script>

<script src="../../../js/dojo.js" type="text/javascript"></script>

<script type="text/javascript">
	dojo.require("dojo.widget.*");
	dojo.require("dojo.widget.DropdownDatePicker");
</script>

<script src="<c:url value="/js/validation.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/base.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/admin/assignmentAdmin.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/admin/baseAdmin.js" />" type="text/javascript"></script>

<div class="ContentFrame" style="max-width: 1200px">
	<table class="contentTable" cellspacing="2">
		<tr>
			<td valign="top" width="270">
				<h1><fmt:message key="admin.assignment.title" /></h1>
				<div class="GreyFrame" style="width: 260px;">
					<h3>&nbsp;</h3>

					<div class="GreyFrameBody">
							<p>
								<input style="margin-bottom: 0;color: #aaaaaa; margin-bottom: 3px"
										class="textInput"
										type="text"
										name="filter"
										size="30"
										id="filterInput"
										value="<fmt:message key="admin.user.filter" />"><br>
								<fmt:message key="admin.user.hideInactive" />: <input style="margin-bottom: 0" type="checkbox" id="hideInactive" name="hideInactive" checked><br>
							</p>
						
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

				
					<br>
					
					<div class="GreyFrameFooter">
						<p>
						</p>
					</div>				
				</div>	
			</td>
			
			<td valign="top">
				<span id="assignmentFormSpan">
					&nbsp;	
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

<c:if test="${param.assignmentId != null}">
<script>
	editAssignment(${param.userId}, ${param.assignmentId});
</script>
</c:if>
