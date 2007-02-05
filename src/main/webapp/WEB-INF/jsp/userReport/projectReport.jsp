<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- spanTarget: reportContent -->
<div class="GreyFrame">
	<h3>&nbsp;</h3>
	
	<table width="100%">
		<tr>
			<td valign="top">
	
	<div class="BlueFrame">
		<div class="BlueLeftTop">
			<div class="BlueRightTop">
				&nbsp;&nbsp;Project report: <fmt:formatDate pattern="dd MMM yyyy" value="${report.reportCriteria.userCriteria.reportRange.dateStart}" /> - <fmt:formatDate pattern="dd MMM yyyy" value="${report.reportCriteria.userCriteria.reportRange.dateEnd}" />
			</div>
		</div>	

		<table class="reportTable" cellpadding="0" cellspacing="0">
			<tr>
				<th>Customer</th>
				<th>Project</th>
				<th>Hours booked</th>
				<th>Turn over</th>
			</tr>

		<c:forEach items="${report.customers}" var="customer">
			<tr>
				<td>${customer.name}</td>
				
			<c:forEach items="${report.reportValues[customer]}" var="pag">
				<td>${pag.projectAssignment.project.name}</td>
				<td><fmt:formatNumber value="${pag.hours}" maxFractionDigits="2" /></td>
				<td><fmt:formatNumber  maxFractionDigits="2" value="${pag.turnOver}" type="currency" /> </td>
				</tr>
				<tr><td>&nbsp;</td>
			</c:forEach>
				<td colspan="3">
					&nbsp;
				</td>
			</tr>
		</c:forEach>
		</table>

		<div class="BlueLeftBottom">
			<div class="BlueRightBottom">
				&nbsp;
			</div>
		</div>			
	</div>
	</td><td valign="top" width="270">
	<img style="float: right; margin-right: 5px;" src="projectReportTotalHoursChart.do?key=${reportSessionKey}"><br>
	<img style="float: right; margin-right: 5px;" src="projectReportTotalTurnoverChart.do?key=${reportSessionKey}">
</td></tr></table>
	<br>
	
	<img src="projectReportPerWeekChart.do?chartWidth=700&chartHeight=200" />
	
	<div class="GreyFrameFooter">
		<p>
		</p>
	</div>				
</div>			
		
		

