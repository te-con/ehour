<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- spanTarget: report -->

<script>
	// TODO fix this
	var form = dojo.byId('criteriaForm');
	form.reportName.value = 'userReport';
</script>

<div class="ContentFrame">
	<div class="GreyFrame">
		<h3>&nbsp;</h3>
				
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td valign="top">
	
				<div class="BlueFrame">
					<div class="BlueLeftTop">
						<div class="BlueRightTop">
							&nbsp;
						</div>
					</div>			

					<table cellpadding="0" cellspacing="0" style="margin-top: -10px">
						<tr>
							<td width="5">&nbsp;</td>
							<td>
					<table class="reportTable" cellpadding="0" cellspacing="0">
						<tr>
							<th><fmt:message key="report.report.user" /></th>
							<th><fmt:message key="report.report.customer" /></th>
							<th><fmt:message key="report.report.project" /></th>
							<th><fmt:message key="report.report.hours" /></th>										
							<th><fmt:message key="report.report.turnOver" /></th>
						</tr>
					
					<c:set var="totalHour" value="0" />
					<c:set var="totalTurnOver" value="0" />					
			
					<c:forEach items="${userReport.reportValues}" var="userItem" varStatus="status">
						<tr class="customerRow" <c:if test="${status.count % 2 == 0}">style="background-color: #fefeff"</c:if>>
							<td colspan="3">${userItem.key.lastName}, ${userItem.key.firstName}</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>							
						</tr>

						<c:forEach items="${userReport.reportValues[userItem.key]}" var="customerItem">
							<tr class="customerRow" <c:if test="${status.count % 2 == 0}">style="background-color: #fefeff"</c:if>>
								<td>&nbsp;</td>
								<td>${customerItem.key.name}</td>					
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
							</tr>

							<c:forEach items="${userReport.reportValues[userItem.key][customerItem.key]}" var="projectItem">
								<tr class="customerRow" <c:if test="${status.count % 2 == 0}">style="background-color: #fefeff"</c:if>>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>${projectItem.projectAssignment.project.name}</td>					
									<td><fmt:formatNumber value="${projectItem.hours}" maxFractionDigits="2" /></td>
									<td><fmt:formatNumber maxFractionDigits="2" value="${projectItem.turnOver}" type="currency" /></td>
								</tr>
							</c:forEach>
						</c:forEach>
					</c:forEach>
					</table>
							</td>
							<td width="5">&nbsp;</td>
						</tr>
					</table>
							

					<div class="BlueLeftBottom">
						<div class="BlueRightBottom">
							&nbsp;
						</div>
					</div>			
				</div>
			</td>
		</tr>
	</table>
	<br>
	
	<div class="GreyFrameFooter">
		<p>
		</p>
	</div>				
</div>