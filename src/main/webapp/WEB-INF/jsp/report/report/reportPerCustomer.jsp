<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>

<!-- spanTarget: report -->

<script>
	setReportName('${customerReport.reportName}');
</script>

<div class="ContentFrame">
	<div class="GreyFrame">
		<h3><fmt:message key="report.report.customerReport" />: <fmt:formatDate pattern="dd MMM yyyy" value="${customerReport.reportCriteria.reportRange.dateStart}" /> - <fmt:formatDate pattern="dd MMM yyyy" value="${customerReport.reportCriteria.reportRange.dateEnd}" /></h3>
				
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
								<th><fmt:message key="report.report.customer" /></th>
								<th><fmt:message key="report.report.project" /></th>
								<th><fmt:message key="report.report.projectCode" /></th>
								<th><fmt:message key="report.report.user" /></th>
								<th><fmt:message key="report.report.hours" /></th>
								<th><fmt:message key="report.report.turnOver" /></th>
							</tr>

					<c:set var="grandTotalHour" value="0" />
					<c:set var="grandTotalTurnOver" value="0" />					
						
					<c:forEach items="${customerReport.reportValues}" var="customerItem" varStatus="status">
						<tr class="dataRow" <c:if test="${status.count % 2 == 1}">style="background-color: #fefeff"</c:if>>
							<td>${customerItem.key.name}</td>

						<c:set var="totalHour" value="0" />
						<c:set var="totalTurnOver" value="0" />					
						
						<c:forEach items="${customerReport.reportValues[customerItem.key]}" var="projectItem" varStatus="projectStatus">
							<c:if test="${!projectStatus.first}">
								<tr class="dataRow" <c:if test="${status.count % 2 == 1}">style="background-color: #fefeff"</c:if>>
								<td>&nbsp;</td>
							</c:if>
								<td><a href=""
										onClick="return updateReport('projectReport', '${reportSessionKey}', '${projectItem.key.projectId}')"
										>${projectItem.key.name}</a></td>					
								<td>${projectItem.key.projectCode}</td>
							<c:forEach items="${customerReport.reportValues[customerItem.key][projectItem.key]}" var="userItem" varStatus="userStatus">
							
									<c:if test="${!userStatus.first}">
										<tr class="dataRow" <c:if test="${status.count % 2 == 1}">style="background-color: #fefeff"</c:if>>
										<td>&nbsp;</td>
										<td><a href=""
											onClick="return updateReport('projectReport', '${reportSessionKey}', '${projectItem.key.projectId}')"
											>${projectItem.key.name}</a></td>
										<td>${projectItem.key.projectCode}</td>											
									</c:if>
									<td><a href=""
											onClick="return updateReport('userReport', '${reportSessionKey}', '${userItem.projectAssignment.user.userId}')"
											>${userItem.projectAssignment.user.lastName}, ${userItem.projectAssignment.user.firstName}</a></td>
									<td align="right"><fmt:formatNumber value="${userItem.hours}" maxFractionDigits="2" /></td>
									<td align="right" class="lastChild"><fmt:formatNumber maxFractionDigits="2" value="${userItem.turnOver}" type="currency" currencySymbol="${currencySymbol}" /></td>
									
									<c:set var="totalHour" value="${totalHour + userItem.hours}" />	
									<c:set var="totalTurnOver" value="${totalTurnOver + userItem.turnOver}" />								

									<c:set var="grandTotalHour" value="${grandTotalHour + userItem.hours}" />	
									<c:set var="grandTotalTurnOver" value="${grandTotalTurnOver + userItem.turnOver}" />								
									
								</tr>
							</c:forEach>							
						</c:forEach>							

						<tr class="totalRow">
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td align="right"><fmt:formatNumber value="${totalHour}" maxFractionDigits="2" /></td>
							<td align="right" class="lastChild"><fmt:formatNumber maxFractionDigits="2" value="${totalTurnOver}" type="currency" currencySymbol="${currencySymbol}" /></td>
						</tr>
													
					</c:forEach>

					<tr class="totalRow">
						<td><b><fmt:message key="report.report.total" />:</b></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td align="right"><b><fmt:formatNumber value="${grandTotalHour}" maxFractionDigits="2" /></b></td>
						<td align="right" class="lastChild"><b><fmt:formatNumber maxFractionDigits="2" value="${grandTotalTurnOver}" type="currency" currencySymbol="${currencySymbol}" /></b></td>
					</tr>

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

	<img src="showCustomerHoursAggregateChart.do?forId=${forId}&chartWidth=350&chartHeight=200&key=${reportSessionKey}&random=<%= new Date().getTime() %>" />
	<img src="showCustomerTurnoverAggregateChart.do?forId=${forId}&chartWidth=350&chartHeight=200&key=${reportSessionKey}&random=<%= new Date().getTime() %>" />
	<br><br>

	<div class="GreyFrameFooter">
		<p>
		</p>
	</div>				
</div>