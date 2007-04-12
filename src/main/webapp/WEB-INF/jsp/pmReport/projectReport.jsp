<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>

<!-- spanTarget: reportContent -->
<div class="ContentFrame">
	<h1><fmt:message key="pmReport.title">
			<fmt:param>${pmReport.project.fullname}</fmt:param>
			<fmt:param><fmt:formatDate pattern="dd MMM yyyy" value="${pmReport.reportRange.dateStart}" /></fmt:param>
			<fmt:param><fmt:formatDate pattern="dd MMM yyyy" value="${pmReport.reportRange.dateEnd}" /></fmt:param>			
		</fmt:message></h1>

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
									<th>&nbsp;<fmt:message key="pmReport.user.user" /></th>
									<th>&nbsp;<fmt:message key="pmReport.user.role" /></th>
									<th style="text-align: right"><fmt:message key="pmReport.user.booked" />&nbsp;</th>
									<th style="text-align: right"><fmt:message key="pmReport.user.avail" />&nbsp;</th>
									<th style="text-align: right"><fmt:message key="pmReport.user.overrun" />&nbsp;</th>
									<th style="text-align: right"><fmt:message key="pmReport.user.percentageUsed" />&nbsp;</th>
								</tr>

								<c:forEach items="${pmReport.aggregates}" var="aggregate" varStatus="status">
								<tr class="dataRow" <c:if test="${status.count % 2 == 1}">style="background-color: #fefeff"</c:if>>
									<td>${aggregate.projectAssignment.user.lastName}</td>
									<td><c:out value="${aggregate.projectAssignment.role}" default="--"/></td>
									<td style="text-align: right"><fmt:formatNumber value="${aggregate.hours}" maxFractionDigits="2" />&nbsp;</td>
									<td style="text-align: right"><fmt:formatNumber value="${aggregate.projectAssignment.allottedHours}" maxFractionDigits="2" /></td>
									<td style="text-align: right"><fmt:formatNumber value="${aggregate.projectAssignment.allowedOverrun}" maxFractionDigits="2" /></td>
									<td style="text-align: right"><fmt:formatNumber value="${aggregate.progressPercentage}" minFractionDigits="2" maxFractionDigits="2" />%</td>
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

	<div class="GreyFrameFooter">
		<p>
		</p>
	</div>		

	</div>

	<br>
	
</div>					