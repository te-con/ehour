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
					<td><div id="dateStartDiv"></div></td>
					<td><fmt:message key="report.criteria.until" /></td>
					<td><div id="dateEndDiv"></div></td>
				</tr>
			</table>
			
			<br>
			
			<table class="ReportCriteriaTableTable" style="width: auto">
				<tr>
					<td><fmt:message key="report.criteria.customers" />:</td>
					<td><select multiple="multiple" name="customerId" id="customerId" size="4" class="textInput">
							<c:forEach items="${criteria.availableCriteria.customers}" var="customer">
								<option value="${customer.customerId}"
								<c:set var="cId" value="${customer.customerId}-" />
								<c:if test="${fn:contains(criteria.userCriteria.customersAsString,  cId)}">
										SELECTED
								</c:if>
								>${customer.name}
							</c:forEach>
						</select></td>

					<td><fmt:message key="report.criteria.projects" />:</td>
					<td><select multiple="multiple" name="projectId" id="projectId" size="4"  class="textInput">
							<c:forEach items="${criteria.availableCriteria.projects}" var="project">
								<option value="${project.projectId}">${project.name}
							</c:forEach>
						</select></td>
				</tr>
				
				<tr>
					<td colspan="2">
						<fmt:message key="report.criteria.onlyActive" />
							<input type="checkbox" name="onlyActiveCustomers" id="onlyActiveCustomers"
								<c:if test="${criteria.userCriteria.onlyActiveCustomers}">checked</c:if>>
					</td>
				</tr>
				
				<tr>
					<td colspan="4">
						<input type="submit" id="criteriaSubmit" value=">>">
					</td>
				</tr>
			</table>
			
			</form>
		</div>
		
		<div class="GreyFrameFooter">
			<p />
		</div>	
	</div>
</div>
<br><br>		

<script>
	init();
</script>