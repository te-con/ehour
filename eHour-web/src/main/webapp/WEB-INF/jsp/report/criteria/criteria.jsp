<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<!-- spanTarget: criteria -->
<script src="../../js/dojo.js" type="text/javascript"></script>
<script src="../../js/report.js" type="text/javascript"></script>

<script type="text/javascript">
	var dateStart = "<fmt:formatDate value="${criteria.reportRange.dateStart}" pattern="yyyy-MM-dd" />T00:00:00-00:00";
	var dateEnd = "<fmt:formatDate value="${criteria.reportRange.dateEndForDisplay}" pattern="yyyy-MM-dd" />T00:00:00-00:00";
	var contextRoot = "<c:url value="/eh/report" />";
</script>

<form method="post" id="criteriaForm">
<input type="hidden" name="fromForm" value="yes">
<input type="hidden" name="updateType" value="0">	


<div dojoType="TitlePane" id="criteriaPane" label="<fmt:message key="report.criteria.header" />" labelNodeClass="reportCriteriaLabel" containerNodeClass="ContentFrame">

	<div class="GreyFrame">
		<h3>&nbsp;</h3>
		
		<div class="GreyFrameBody">

			<div style="float: left;vertical-align: bottom;padding-left: 20px;margin: 0;">
				<fmt:message key="report.criteria.reportRange" />:
				<div id="dateStartDiv" class="textInputSmall"></div>
			    <fmt:message key="report.criteria.until" />
				<div id="dateEndDiv"></div>
			</div>
			
			<div style="float: right;padding-right: 20px;margin: 0;">
				<fmt:message key="report.criteria.report" />:
				<select name="reportName"  class="textInputSmall">
<!-- 							<option value="customerReport"><fmt:message key="report.report.overallReport" /> -->
						<option value="customerReport"><fmt:message key="report.report.customerReport" /></option>
						<option value="userReport"><fmt:message key="report.report.userReport" /></option>
						<option value="projectReport"><fmt:message key="report.report.projectReport" /></option>
				</select>
			</div>
			
			<br clear="all">

			<div style="float: left;vertical-align: bottom;padding-left: 20px;margin: 0;padding-top: 5px">
				<fmt:message key="report.quickDate" />
				<select class="textInputSmall" id="quickDateWeekId">
					<c:forEach items="${prevWeeks}" var="week" varStatus="weekStatus">
						<option value="${(18 - weekStatus.count) * -1}"><fmt:message key="report.week" /> ${week}</option>
					</c:forEach>
					<option value="-1"><fmt:message key="report.prevWeek" /></option>
					<option value="0"><fmt:message key="report.currWeek" /></option>
					<option style="color: #999999" SELECTED><fmt:message key="report.week" /></option>
					<option value="1"><fmt:message key="report.nextWeek" /></option>
					<c:forEach items="${nextWeeks}" var="week" varStatus="weekStatus">
						<option value="${1 + weekStatus.count}"><fmt:message key="report.week" /> ${week}</option>
					</c:forEach>
				</select>
				&nbsp;&nbsp;
				<select class="textInputSmall" id="quickDateMonthId">
					<c:forEach items="${prevMonths}" var="month" varStatus="monthStatus">
						<option value="${(8 - monthStatus.count) * -1}"><fmt:formatDate pattern="MMMM yyyy" value="${month}" /></option>
					</c:forEach>

					<option value="-1"><fmt:message key="report.prevMonth" /></option>
					<option value="0"><fmt:message key="report.currMonth" /></option>
					<option style="color: #999999" SELECTED><fmt:message key="report.month" /></option>
					<option value="1"><fmt:message key="report.nextMonth" /></option>
					<c:forEach items="${nextMonths}" var="month" varStatus="monthStatus">
						<option value="${(1 + monthStatus.count)}"><fmt:formatDate pattern="MMMM yyyy" value="${month}" /></option>
					</c:forEach>
				</select>
				&nbsp;&nbsp;
				<select class="textInputSmall" id="quickDateQuarterId">
					<option value="-1"><fmt:message key="report.prevQuarter" /></option>
					<option value="0"><fmt:message key="report.currQuarter" /></option>
					<option style="color: #999999" SELECTED><fmt:message key="report.quarter" /></option>
					<option value="1"><fmt:message key="report.nextQuarter" /></option>
				</select>
			</div>

			<br>
		</div>
		
		<div class="GreyFrameFooter">
			<p />
		</div>	
	</div>
			<br>
			
	<div class="GreyFrame">
		<h3>&nbsp;</h3>
		
		<div class="GreyFrameBody">
			
			<table cellpadding="0" cellspacing="0">
				<tr>
				<td valign="top">
			
<!-- customers -->			
				<div class="BlueFrameContainer">
					<fmt:message key="report.criteria.customers" />
					
					<div class="BlueFrame" style="margin-left: -11px;">
						<div class="BlueLeftTop">
							<div class="BlueRightTop">
								&nbsp;
							</div>
						</div>	
						
						<table class="reportCriteriaTableTable">
							<tr>
								<td colspan="2">
									<div id="criteriaCustomerList">
										<tiles:insert page="criteriaCustomerList.jsp" />
									</div>							
								</td>
								<td rowpspan="2" width="8">
									&nbsp;
								</td>
							</tr>
							
							<tr>
								<td>
									<fmt:message key="report.criteria.onlyActive" />
								</td>
								
								<td align="right">
									<input type="checkbox" name="onlyActiveCustomers" id="onlyActiveCustomers"
										<c:if test="${criteria.userCriteria.onlyActiveCustomers}">checked</c:if>>
								</td>					
							</tr>
						</table>
					
						<div class="BlueLeftBottom">
							<div class="BlueRightBottom">
								&nbsp;
							</div>
						</div>			
					</div>		
				</div>
			</td>
				
			<td width="50%">
<!-- projects -->
			<div class="BlueFrameContainer">
				<fmt:message key="report.criteria.projects" />
					
				<div class="BlueFrame" style="margin-left: -11px">
					<div class="BlueLeftTop">
						<div class="BlueRightTop">
							&nbsp;
						</div>
					</div>		

					<table class="reportCriteriaTableTable">
						<tr>
							<td colspan="2">
								<div id="criteriaProjectList">
									<tiles:insert page="criteriaProjectList.jsp" />
								</div>
							</td>
							<td rowpspan="2" width="11">
								&nbsp;
							</td>							
						</tr>
					
						<tr>
							<td>
								<fmt:message key="report.criteria.onlyActive" />
							</td>
							
							<td align="right">
								<input type="checkbox" name="onlyActiveProjects" id="onlyActiveProjects"
								<c:if test="${criteria.userCriteria.onlyActiveProjects}">checked</c:if>>
							</td>					
						</tr>
						
					</table>
				
					<div class="BlueLeftBottom">
						<div class="BlueRightBottom">
							&nbsp;
						</div>
					</div>			
				</div>				
			</div>

			</td>
		</tr>

		<tr>
			<td colspan="2">
				<br>
			</td>
		</tr>
				
		<tr>
			<td valign="top">

<!-- departments -->
			<div class="BlueFrameContainer">
				<fmt:message key="report.criteria.departments" />
				
				<div class="BlueFrame" style="margin-left: -11px">
					<div class="BlueLeftTop">
						<div class="BlueRightTop">
							&nbsp;
						</div>
					</div>	
					
					<table class="reportCriteriaTableTable">
						<tr>
							<td><div>
									<select multiple="multiple" name="departmentId" id="departmentId" size="4" class="criteriaInput">
										<option value="-1"
											<c:if test="${criteria.userCriteria.emptyDepartments}">
												SELECTED
											</c:if>>{<fmt:message key="report.criteria.all" />}
										
										<c:forEach items="${criteria.availableCriteria.userDepartments}" var="dept">
											<option value="${dept.departmentId}"
											<c:set var="dId" value="${dept.departmentId}-" />
											<c:if test="${fn:contains(criteria.userCriteria.departmentsAsString,  dId)}">
													SELECTED
											</c:if>
											>${dept.name}
										</c:forEach>
									</select>
								</div>
							</td>
							<td width="8">
								&nbsp;
							</td>								
						</tr>
					</table>
				
					<div class="BlueLeftBottom">
						<div class="BlueRightBottom">
							&nbsp;
						</div>
					</div>			
				</div>
			</div>			
		</td>
		<td width="50%">

<!-- employees -->
			<div class="BlueFrameContainer">
				<fmt:message key="report.criteria.users" />:
				
				<div class="BlueFrame" style="margin-left: -11px">
					<div class="BlueLeftTop">
						<div class="BlueRightTop">
							&nbsp;
						</div>
					</div>	
					
						<table class="reportCriteriaTableTable">
							<tr>
								<td colspan="2">
									<input type="text" name="userFilter"
											id="userFilter"
											class="criteriaInput"
											style="width: 15em;<c:if test="${!report.criteria.emptyUserFilter}">color: #aaaaaa</c:if>"
											value="<fmt:message key="report.criteria.filterUsersOn" /><c:choose>
														<c:when test="${report.criteria.emptyUserFilter}">
															<fmt:message key="report.criteria.filterUsersOn" />
														</c:when>
														<c:otherwise>
															${report.criteria.emptyUserFilter}
															${report.criteria.userFilter}
														</c:otherwise>
													</c:choose>">
								</td>
							<td width="11" rowspan="3"> 
								&nbsp;
							</td>									
							</tr>
							
							<tr>
								<td colspan="2">
									<div id="criteriaUserList">
										<tiles:insert page="criteriaUserList.jsp" />
									</div>
								</td>
							</tr>
							
							<tr>
								<td>
									<fmt:message key="report.criteria.onlyActive" />
								</td>
								
								<td align="right">
									<input type="checkbox" name="onlyActiveUsers" id="onlyActiveUsers"
									<c:if test="${criteria.userCriteria.onlyActiveUsers}">checked</c:if>>
								</td>					
							</tr>							
						</table>
				
					<div class="BlueLeftBottom">
						<div class="BlueRightBottom">
							&nbsp;
						</div>
					</div>			
				</div>		
			</div>	
		</td>
	</tr>

	<tr>
		<td colspan="2" align="right">
			<br>
			<input style="float: bottom" type="submit" class="submitButtonBlue" id="criteriaSubmit" value="<fmt:message key="userReport.criteria.showReport" /> >>">
		</td>
	</tr>
</table>
		</div>				
		
		<div class="GreyFrameFooter">
			<p />
		</div>	
	</div>
</div>
		</form>

<br><br>		

<script>
	init();
</script>