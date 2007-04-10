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
</script>

<script src="../../../js/dojo.js" type="text/javascript"></script>

<script src="<c:url value="/js/base.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/admin/configurationAdmin.js" />" type="text/javascript"></script>

<div class="ContentFrame" style="max-width: 1200px">
	<table class="contentTable" cellspacing="2">
		<tr>
			<td valign="top">
				<h1><fmt:message key="admin.config.title" /></h1>
				<div class="GreyFrame">
					<h3>&nbsp;</h3>

					<div class="GreyFrameBody">
						
						<div class="BlueFrame">
							<div class="BlueLeftTop">
								<div class="BlueRightTop">
									&nbsp;
								</div>
							</div>											

							<span id="localeSpan">
								<tiles:insert page="localeConfiguration.jsp" />
							</span>
							
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
		
	</table>
</div>

<c:if test="${param.assignmentId != null}">
<script>
	editAssignment(${param.userId}, ${param.assignmentId});
</script>
</c:if>
