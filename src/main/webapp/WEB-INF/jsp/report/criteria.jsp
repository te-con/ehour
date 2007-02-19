<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- spanTarget: criteria -->
<script src="../../js/dojo.js" type="text/javascript"></script>
<script src="../../js/report.js" type="text/javascript"></script>

<script type="text/javascript">
	dojo.require("dojo.widget.*");
	dojo.require("dojo.widget.DropdownDatePicker");

	var dateStart = "<fmt:formatDate value="${criteria.reportRange.dateStart}" pattern="yyyy-MM-dd" />T00:00:00-00:00";
	var dateEnd = "<fmt:formatDate value="${criteria.reportRange.dateEnd}" pattern="yyyy-MM-dd" />T00:00:00-00:00";
	var contextRoot = "<c:url value="/eh/report" />";
</script>

<div class="ContentFrame">
	<h1><fmt:message key="report.criteria.header" /></h1>
	<div class="GreyFrame">
		<h3>&nbsp;</h3>
		
		<div class="GreyFrameBody">
		
			<form method="post" action="report.do" id="criteriaForm">
			<input type="hidden" name="fromForm" value="yes">

			<table class="ReportCriteriaTableTable" style="width: auto">
			
				<tr>
					<td><fmt:message key="report.criteria.reportRange" />:</td>
					<td><div id="dateStartDiv" class="textInputSmall"></div></td>
					<td><fmt:message key="report.criteria.until" /></td>
					<td><div id="dateEndDiv"></div></td>
				</tr>
			</table>
			
			<br>
			<Table><tr><td>	
			
<!-- customers -->			
			<div class="BlueFrameContainer"  style="float: left">
				<fmt:message key="report.criteria.customers" />
				
				<div class="BlueFrame"  style="margin-left: -11px">
					<div class="BlueLeftTop">
						<div class="BlueRightTop">
							&nbsp;
						</div>
					</div>	
					
					<table class="ReportCriteriaTableTable">
						<tr>
							<td colspan="2"><select multiple="multiple" name="customerId" id="customerId" size="4" class="textInputSmall" style="width: 200px">
									<option value="-1"
										<c:if test="${criteria.userCriteria.emptyCustomers}">
											SELECTED
										</c:if>>{<fmt:message key="report.criteria.all" />}
									
									<c:forEach items="${criteria.availableCriteria.customers}" var="customer">
										<option value="${customer.customerId}"
										<c:set var="cId" value="${customer.customerId}-" />
										<c:if test="${fn:contains(criteria.userCriteria.customersAsString,  cId)}">
												SELECTED
										</c:if>
										>${customer.name}
									</c:forEach>
								</select></td>
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

<!-- projects -->
			<div class="BlueFrameContainer"  style="float: left">
				<fmt:message key="report.criteria.projects" />
					
				<div class="BlueFrame" style="margin-left: -11px">
					<div class="BlueLeftTop">
						<div class="BlueRightTop">
							&nbsp;
						</div>
					</div>		

					<table class="ReportCriteriaTableTable">
						<tr>
							<td colspan="2"><select multiple="multiple" name="projectId" id="projectId" size="4"  class="textInputSmall" style="width: 200px">
									<option value="-1"
										<c:if test="${criteria.userCriteria.emptyProjects}">
											SELECTED
										</c:if>>{<fmt:message key="report.criteria.all" />}
									<c:forEach items="${criteria.availableCriteria.projects}" var="project">
										<option value="${project.projectId}"
										<c:set var="pId" value="${project.projectId}-" />
										<c:if test="${fn:contains(criteria.userCriteria.projectsAsString,  pId)}">
												SELECTED
										</c:if>
										>${project.name}
									</c:forEach>
								</select></td>
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

			<br>

<!-- departments -->
			<div class="BlueFrameContainer"  style="float: left;height: 200px">
				<fmt:message key="report.criteria.departments" />
				
				<div class="BlueFrame" style="margin-left: -11px">
					<div class="BlueLeftTop">
						<div class="BlueRightTop">
							&nbsp;
						</div>
					</div>	
					
						<table class="ReportCriteriaTableTable">
							<tr>
								<td><select multiple="multiple" name="departmentId" id="departmentId" size="4" class="textInputSmall" style="width: 200px">
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
									</select></td>
							</tr>
						</table>
				
					<div class="BlueLeftBottom">
						<div class="BlueRightBottom">
							&nbsp;
						</div>
					</div>			
				</div>
			</div>			
			

<!-- employees -->
			<div class="BlueFrameContainer" style="float: left">
				<fmt:message key="report.criteria.users" />:
				
				<div class="BlueFrame" style="margin-left: -11px">
					<div class="BlueLeftTop">
						<div class="BlueRightTop">
							&nbsp;
						</div>
					</div>	
					
						<table class="ReportCriteriaTableTable">
							<tr>
								<td colspan="2">
									<input type="text" name="userFilter"
											id="userFilter"
											class="textInputSmall"
											style="width: 200px; <c:if test="${!report.criteria.emptyUserFilter}">color: #aaaaaa</c:if>"
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
							</tr>
							
							<tr>
								<td colspan="2"><select multiple="multiple" name="userId" id="userId" size="4" class="textInputSmall" style="width: 200px">
										<option value="-1"
											<c:if test="${criteria.userCriteria.emptyUsers}">
												SELECTED
											</c:if>>{<fmt:message key="report.criteria.all" />}
										
										<c:forEach items="${criteria.availableCriteria.users}" var="user">
											<option value="${user.userId}"
											<c:set var="uId" value="${user.userId}-" />
											<c:if test="${fn:contains(criteria.userCriteria.usersAsString,  uId)}">
													SELECTED
											</c:if>
											>${user.lastName}, ${user.firstName}
										</c:forEach>
									</select>
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
		</td></tr></Table>
		<input style="float: bottom" type="submit" id="criteriaSubmit" value=">>">
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