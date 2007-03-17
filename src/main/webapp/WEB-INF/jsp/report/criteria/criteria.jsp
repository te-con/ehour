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
	var dateEnd = "<fmt:formatDate value="${criteria.reportRange.dateEnd}" pattern="yyyy-MM-dd" />T00:00:00-00:00";
	var contextRoot = "<c:url value="/eh/report" />";
</script>

<div dojoType="TitlePane" id="criteriaPane" label="<fmt:message key="report.criteria.header" />" labelNodeClass="reportCriteriaLabel" containerNodeClass="ContentFrame">

	<div class="GreyFrame">
		<h3>&nbsp;</h3>
		
		<div class="GreyFrameBody">
		
			<form method="post" id="criteriaForm">
			<input type="hidden" name="fromForm" value="yes">
			<input type="hidden" name="updateType" value="0">	

			<table class="reportCriteriaTableTable">
			
				<tr>
					<td style="padding-left: 20px"><fmt:message key="report.criteria.reportRange" />:
						<div id="dateStartDiv" class="textInputSmall"></div>
					    <fmt:message key="report.criteria.until" />
						<div id="dateEndDiv"></div>
					</td>
					<td align="right" style="padding-right: 20px"><fmt:message key="report.criteria.report" />:
					<select name="reportName"  class="textInputSmall">
							<option value="customerReport"><fmt:message key="report.report.customerReport" />
							<option value="userReport"><fmt:message key="report.report.userReport" />
							<option value="projectReport"><fmt:message key="report.report.projectReport" />														
						</select>
					</td>
					
				</tr>
			</table>
			
			<br>
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
			<input style="float: bottom" type="submit" class="submitButtonBlue" id="criteriaSubmit" value="show report >>">
		</td>
	</tr>
</table>
		</div>				
		</form>
		
		<div class="GreyFrameFooter">
			<p />
		</div>	
	</div>
</div>
<br><br>		

<script>
	init();
</script>