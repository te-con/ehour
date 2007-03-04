<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>

<!-- spanTarget: report -->

<script>
	setReportName('${userReport.reportName}');
</script>

<div class="ContentFrame">
	<div class="GreyFrame">
		<h3><fmt:message key="report.report.userReport" />: <fmt:formatDate pattern="dd MMM yyyy" value="${userReport.reportCriteria.reportRange.dateStart}" /> - <fmt:formatDate pattern="dd MMM yyyy" value="${userReport.reportCriteria.reportRange.dateEnd}" /></h3>
				
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
					
					<c:set var="grandTotalHour" value="0" />
					<c:set var="grandTotalTurnOver" value="0" />					
			
					<c:forEach items="${userReport.reportValues}" var="userItem" varStatus="status">
						<c:set var="totalHour" value="0" />
						<c:set var="totalTurnOver" value="0" />					
					
					
						<tr class="dataRow" <c:if test="${status.count % 2 == 1}">style="background-color: #fefeff"</c:if>>
							<td>${userItem.key.lastName}, ${userItem.key.firstName}</td>

						<c:forEach items="${userReport.reportValues[userItem.key]}" var="customerItem" varStatus="customerStatus">
							<c:if test="${!customerStatus.first}">
								<tr class="dataRow" <c:if test="${status.count % 2 == 1}">style="background-color: #fefeff"</c:if>>
								<td>&nbsp;</td>
							</c:if>					
								<td><a href=""
										onClick="return updateReport('customerReport', '${reportSessionKey}', '${customerItem.key.customerId}')"
										>${customerItem.key.name}</a></td>

								<c:forEach items="${userReport.reportValues[userItem.key][customerItem.key]}" var="projectItem" varStatus="projectStatus">
									<c:if test="${!projectStatus.first}">
										<tr class="dataRow" <c:if test="${status.count % 2 == 1}">style="background-color: #fefeff"</c:if>>
										<td>&nbsp;</td>
										<td><a href=""
												onClick="return updateReport('customerReport', '${reportSessionKey}', '${customerItem.key.customerId}')"
												>${customerItem.key.name}</a></td>
									</c:if>
										<td><a href=""
											onClick="return updateReport('projectReport', '${reportSessionKey}', '${projectItem.projectAssignment.project.projectId}')"
											>${projectItem.projectAssignment.project.name}</a></td>
										<td align="right"><fmt:formatNumber value="${projectItem.hours}" maxFractionDigits="2" /></td>
										<td align="right" class="lastChild"><fmt:formatNumber maxFractionDigits="2" value="${projectItem.turnOver}" type="currency" /></td>
									</tr>

									<c:set var="totalHour" value="${totalHour + projectItem.hours}" />	
									<c:set var="totalTurnOver" value="${totalTurnOver + projectItem.turnOver}" />								

									<c:set var="grandTotalHour" value="${grandTotalHour + projectItem.hours}" />	
									<c:set var="grandTotalTurnOver" value="${grandTotalTurnOver + projectItem.turnOver}" />								
								</c:forEach>
						</c:forEach>

						<tr class="totalRow">
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td align="right"><fmt:formatNumber value="${totalHour}" maxFractionDigits="2" /></td>
							<td align="right" class="lastChild"><fmt:formatNumber maxFractionDigits="2" value="${totalTurnOver}" type="currency" /></td>
						</tr>						
						
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

	<img src="showUserHoursAggregateChart.do?forId=${forId}&chartWidth=350&chartHeight=200&key=${reportSessionKey}&random=<%= new Date().getTime() %>" />
	<img src="showUserTurnoverAggregateChart.do?forId=${forId}&chartWidth=350&chartHeight=200&key=${reportSessionKey}&random=<%= new Date().getTime() %>" />
	<br><br>
	
	
	<div class="GreyFrameFooter">
		<p>
		</p>
	</div>				
</div>