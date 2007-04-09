<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/ehour-common.tld" prefix="ehour" %>

<!-- spanTarget: overview -->

<!-- project overview -->
<div class="ContentFrame">
	<h1><fmt:message key="user.overview.aggregatedProject" /></h1>
	<div class="GreyFrame">
		<h3>&nbsp;</h3>
		
		<div class="GreyFrameBody">
			<table class="MonthAggregateTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="foldCell">&nbsp;</td>
				<th class="projectCell"><fmt:message key="user.overview.project" /></th>
				<th><nobr><fmt:message key="user.overview.projectCode" /></nobr></th>
				<th><fmt:message key="user.overview.customer" /></th>
				<c:if test="${config.showTurnover}">			
					<th><fmt:message key="user.overview.rate" /></th>
				</c:if>
				<th><nobr><fmt:message key="user.overview.bookedHours" /></nobr></th>
				<c:if test="${config.showTurnover}">			
					<th><fmt:message key="user.overview.turnover" /></th>
				</c:if>
			</tr>
					
			<c:forEach items="${timesheetOverview.projectStatus}" var="projectReport">
			
			<tr>
				<td><a href="" onClick="return toggleProjectInfo(${projectReport.projectAssignment.assignmentId})"><img id="foldImg${projectReport.projectAssignment.assignmentId}" src="<c:url value="/img/fold_down.gif" />" border="0"></a></td>
				<td><a href="" onClick="return toggleProjectInfo(${projectReport.projectAssignment.assignmentId})">${projectReport.projectAssignment.project.name}</a></td>
				<td>${projectReport.projectAssignment.project.projectCode}</td>				
				<td>${projectReport.projectAssignment.project.customer.name}</td>
				<c:if test="${config.showTurnover}">
					<td>
						<c:if test="${projectReport.projectAssignment.hourlyRate == '' || projectReport.projectAssignment.hourlyRate == null}">
							--
						</c:if>
					<nobr><fmt:formatNumber type="currency" 
											value="${projectReport.projectAssignment.hourlyRate}" 
											currencySymbol="${currencySymbol} " /></nobr></td>
				</c:if>			
				<td><fmt:formatNumber value="${projectReport.hours}" maxFractionDigits="2" /></td>
				<c:if test="${config.showTurnover}">
					<td><c:if test="${projectReport.projectAssignment.hourlyRate == '' || projectReport.projectAssignment.hourlyRate == null}">
						--
					</c:if>
					<nobr>
				<fmt:formatNumber type="currency"
										value="${projectReport.turnOver}" 
										currencySymbol="${currencySymbol} " /></nobr></td>
				</c:if>
			</tr>
			
			<tr id="project${projectReport.projectAssignment.assignmentId}" style="display: none">
				<td>&nbsp;</td>
				<td style="color: #999999;font-size: 0.8em;padding-top: 2px" colspan="<c:if test="${config.showTurnover}">6</c:if><c:if test="${!config.showTurnover}">4</c:if>">
					<fmt:message key="user.overview.validFrom" />:
						<c:choose>
							<c:when test="${projectReport.projectAssignment.dateStart == null}">
								<span title="<fmt:message key="user.overview.infinite" />">&infin;</span>
							</c:when>
							<c:otherwise>
								<fmt:formatDate pattern="dd-MMM-yyyy" value="${projectReport.projectAssignment.dateStart}" />
							</c:otherwise>
						</c:choose>
						&nbsp;<fmt:message key="user.overview.to" />&nbsp;
						<c:choose>
							<c:when test="${projectReport.projectAssignment.dateEnd == null}">
								<span title="<fmt:message key="user.overview.infinite" />">&infin;</span>
							</c:when>
							<c:otherwise>
								<fmt:formatDate pattern="dd-MMM-yyyy" value="${projectReport.projectAssignment.dateEnd}" />
							</c:otherwise>
						</c:choose>.
						
						<c:if test="${projectReport.projectAssignment.assignmentType.allottedType}">
							<fmt:message key="user.overview.totalBookedHours" />: <fmt:formatNumber value="${projectReport.totalBookedHours}" maxFractionDigits="2" /><fmt:message key="user.overview.hourShort" />.&nbsp;
							<fmt:message key="user.overview.remainingHours" />: <fmt:formatNumber value="${projectReport.hoursRemaining}" maxFractionDigits="2" /><fmt:message key="user.overview.hourShort" />.
						</c:if>
				</td>
			</tr>					
			</c:forEach>
		</table>
		
		<br>
		</div>
		
		<div class="GreyFrameFooter">
			<p />
		</div>	
	</div>
</div>
<br><br>

<div class="ContentFrame">
	<div style="float: left; margin: 0 0 -5px 0; padding: 0">
		<h1><fmt:message key="user.overview.monthOverview" /></h1>
	</div>

	<div style="text-align: right;width: 719px;margin: -5px 0 -5px 0; padding: 0;">
		<a href="printTimesheet.do?zeroBased=0&month=<fmt:formatDate value="${timesheetOverviewMonth.time}" pattern="M" />&year=<fmt:formatDate value="${timesheetOverviewMonth.time}" pattern="yyyy" />">
			<img src="<c:url value="/img/print_off.gif" />"
				onMouseover="this.src='<c:url value="/img/print_on.gif" />'"
				onMouseout="this.src='<c:url value="/img/print_off.gif" />'"
				alt="<fmt:message key="user.overview.print" />"
			 border="0">
		</a>
	</div>
	

	<div class="GreyFrame">
		<h3>&nbsp;</h3>
		
		<div class="BlueFrame">
			<div class="BlueLeftTop">
				<div class="BlueRightTop">
					&nbsp;
				</div>
			</div>	
	
			<table cellpadding="0" cellspacing="0" class="MonthOverviewIEWorkAround">
				<tr>
					<td>
					<table class="MonthOverviewMonthTable" cellpadding="0" cellspacing="0">
						<tr class="weekColumnRow">
							<td class="weekNumber">&nbsp;</td>
							<td><fmt:formatDate pattern="EEE" value="${day_0}" /></td>
							<td><fmt:formatDate pattern="EEE" value="${day_1}" /></td>							
							<td><fmt:formatDate pattern="EEE" value="${day_2}" /></td>
							<td><fmt:formatDate pattern="EEE" value="${day_3}" /></td>							
							<td><fmt:formatDate pattern="EEE" value="${day_4}" /></td>							
							<td><fmt:formatDate pattern="EEE" value="${day_5}" /></td>							
							<td class="lastChild"><fmt:formatDate pattern="EEE" value="${day_6}" /></td>							
						</tr>	
						
					<ehour:overviewCalendar calendar="${timesheetOverviewMonth}"
											timesheetEntries="${timesheetOverview.timesheetEntries}" />
					</table>
					<td width="5">&nbsp</td>
				</tr>
			</table>
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
</div>