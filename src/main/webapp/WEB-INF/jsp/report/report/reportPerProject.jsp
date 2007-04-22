<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>

<!-- spanTarget: report -->

<script>
	setReportName('${projectReport.reportName}');
</script>

<div class="ContentFrame">
	<div class="GreyFrame">
		<h3><fmt:message key="report.report.projectReport" />: <fmt:formatDate pattern="dd MMM yyyy" value="${projectReport.reportCriteria.reportRange.dateStart}" /> - <fmt:formatDate pattern="dd MMM yyyy" value="${projectReport.reportCriteria.reportRange.dateEnd}" /></h3>
				
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
							<th><fmt:message key="report.report.project" /></th>
							<th><fmt:message key="report.report.projectCode" /></th>							
							<th><fmt:message key="report.report.user" /></th>
							<th><fmt:message key="report.report.hours" /></th>										
							<th><fmt:message key="report.report.turnOver" /></th>
						</tr>
					
					<c:set var="grandTotalHour" value="0" />
					<c:set var="grandTotalTurnOver" value="0" />					

						
					<c:forEach items="${projectReport.reportValues}" var="projectItem" varStatus="status">
						<tr class="dataRow" <c:if test="${status.count % 2 == 1}">style="background-color: #fefeff"</c:if>>
							<td>${projectItem.key.name}</td>
							<td>${projectItem.key.projectCode}</td>

						<c:set var="totalHour" value="0" />
						<c:set var="totalTurnOver" value="0" />					
						
						<c:forEach items="${projectReport.reportValues[projectItem.key]}" var="customerItem" varStatus="customerStatus">
							<c:if test="${!customerStatus.first}">
								<tr class="dataRow" <c:if test="${status.count % 2 == 1}">style="background-color: #fefeff"</c:if>>
								<td>&nbsp;</td>
							</c:if>

							<c:forEach items="${projectReport.reportValues[projectItem.key][customerItem.key]}" var="userItem" varStatus="userStatus">
									<c:if test="${!userStatus.first}">
										<tr class="dataRow" <c:if test="${status.count % 2 == 1}">style="background-color: #fefeff"</c:if>>
										<td>&nbsp;</td>
									</c:if>
									<td><a href=""
											onClick="return updateReport('userReport',
																			 '${reportSessionKey}', '${userItem.projectAssignment.user.userId}')"
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
							<td align="right"><fmt:formatNumber value="${totalHour}" maxFractionDigits="2" /></td>
							<td align="right" class="lastChild"><fmt:formatNumber maxFractionDigits="2" value="${totalTurnOver}" type="currency" currencySymbol="${currencySymbol}" /></td>
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
	
	<img src="showProjectHoursAggregateChart.do?forId=${forId}&chartWidth=350&chartHeight=200&key=${reportSessionKey}&random=<%= new Date().getTime() %>" />
	<img src="showProjectTurnoverAggregateChart.do?forId=${forId}&chartWidth=350&chartHeight=200&key=${reportSessionKey}&random=<%= new Date().getTime() %>" />
	<br><br>
	
	
	<div class="GreyFrameFooter">
		<p>
		</p>
	</div>				
</div>