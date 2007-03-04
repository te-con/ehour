<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>

<!-- spanTarget: reportContent -->
<div class="ContentFrame">
	<h1><fmt:message key="userReport.criteria.projectReport" />: <fmt:formatDate pattern="dd MMM yyyy" value="${customerReport.reportCriteria.reportRange.dateStart}" /> - <fmt:formatDate pattern="dd MMM yyyy" value="${customerReport.reportCriteria.reportRange.dateEnd}" /></h1>

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
							<th><fmt:message key="userReport.report.customer" /></th>
							<th><fmt:message key="userReport.report.project" /></th>
							<th><fmt:message key="userReport.report.hours" /></th>
							<c:if test="${config.showTurnover}">										
								<th><fmt:message key="userReport.report.turnover" /></th>
							</c:if>
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
								<td>${projectItem.key.name}</td>					
							<c:forEach items="${customerReport.reportValues[customerItem.key][projectItem.key]}" var="userItem" varStatus="userStatus">
							
									<c:if test="${!userStatus.first}">
										<tr class="dataRow" <c:if test="${status.count % 2 == 1}">style="background-color: #fefeff"</c:if>>
										<td>&nbsp;</td>
										<td>${projectItem.key.name}</td>
									</c:if>
									<td align="right"><fmt:formatNumber value="${userItem.hours}" maxFractionDigits="2" /></td>
									<td align="right" class="lastChild"><fmt:formatNumber maxFractionDigits="2" value="${userItem.turnOver}" type="currency" /></td>
									
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
							<td align="right" class="lastChild"><fmt:formatNumber maxFractionDigits="2" value="${totalTurnOver}" type="currency" /></td>
						</tr>
													
					</c:forEach>

					<tr class="totalRow">
						<td><b><fmt:message key="report.report.total" />:</b></td>
						<td>&nbsp;</td>
						<td align="right"><b><fmt:formatNumber value="${grandTotalHour}" maxFractionDigits="2" /></b></td>
						<td align="right" class="lastChild"><b><fmt:formatNumber maxFractionDigits="2" value="${grandTotalTurnOver}" type="currency" /></b></td>
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
	<br>
	<img src="showProjectHoursAggregateChart.do?forId=${forId}&chartWidth=350&chartHeight=200&key=${reportSessionKey}&random=<%= new Date().getTime() %>" />
	<img src="showProjectTurnoverAggregateChart.do?forId=${forId}&chartWidth=350&chartHeight=200&key=${reportSessionKey}&random=<%= new Date().getTime() %>" />
	<br>
	
	<br>
	
	<div class="GreyFrameFooter">
		<p>
		</p>
	</div>				
</div>