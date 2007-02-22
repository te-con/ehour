<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>

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
							<th>Customer</th>
							<th>Project</th>
							<th>Hours booked</th>
							<c:if test="${config.showTurnover}">										
								<th>Turn over</th>
							</c:if>
						</tr>
						
					<c:set var="totalHour" value="0" />
					<c:set var="totalTurnOver" value="0" />					
			
					<c:forEach items="${customerReport.customers}" var="customer" varStatus="status">
						<tr class="customerRow" <c:if test="${status.count % 2 == 0}">style="background-color: #fefeff"</c:if>>
							<td>${customer.name}</td>
							
						<c:forEach items="${customerReport.reportValues[customer]}" var="pag" varStatus="pagStatus">
							<c:set var="totalHour" value="${totalHour + pag.hours}" />	
							<c:set var="totalTurnOver" value="${totalTurnOver + pag.turnOver}" />								
						
							<c:if test="${pagStatus.count > 1}">
								<tr class="customerRow" <c:if test="${status.count % 2 == 0}">style="background-color: #fefeff"</c:if>><td>&nbsp;</td>						
							</c:if>

							<td>${pag.projectAssignment.project.name}</td>
							<td align="right" <c:if test="${!config.showTurnover}">class="lastChild"</c:if>><fmt:formatNumber value="${pag.hours}" maxFractionDigits="2" /></td>
							
							<c:if test="${config.showTurnover}">			
								<td class="lastChild" align="right"><fmt:formatNumber maxFractionDigits="2" value="${pag.turnOver}" type="currency" /></td>
							</c:if>
							</tr>
						</c:forEach>
					</c:forEach>
					
						<tr class="reportTotal">
							<td colspan="2">
								<fmt:message key="userReport.total" />:
							</td>
							
							<td align="right">
								<fmt:formatNumber value="${totalHour}" maxFractionDigits="2" />
							</td>
							
							<td align="right">
								<fmt:formatNumber maxFractionDigits="2" value="${totalTurnOver}" type="currency" />
							</td>
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

			<td valign="top" width="270">
				<img style="float: right; margin-right: 5px;" src="projectReportTotalHoursChart.do?key=${reportSessionKey}&chartWidth=0&chartHeight=0&random=<%= new Date().getTime() %>"><br>
				<img style="float: right; margin-right: 5px;" src="projectReportTotalTurnoverChart.do?key=${reportSessionKey}&chartWidth=0&chartHeight=0&random=<%= new Date().getTime() %>">
			</td>
		</tr>
	</table>
	<br>
	
	<img src="projectReportPerWeekChart.do?chartWidth=700&chartHeight=200&random=<%= new Date().getTime() %>" />
	
	<div class="GreyFrameFooter">
		<p>
		</p>
	</div>				
</div>