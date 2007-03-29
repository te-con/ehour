<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript">

function initDojo()
{
	var replacedNode = document.getElementById("dateStartDiv");
	dojo.widget.createWidget("DropdownDatePicker",
											{value:"<fmt:formatDate value="${assignment.dateStart}" pattern="yyyy-MM-dd" />T00:00:00-00:00",
											 disabled: false,
											 name: "dateStart",
											 containerToggle: "fade"
										 }, replacedNode);  



	replacedNode = document.getElementById("dateEndDiv");
	dojo.widget.createWidget("DropdownDatePicker",
											{value:"<fmt:formatDate value="${assignment.dateEnd}" pattern="yyyy-MM-dd" />T00:00:00-00:00",
											 disabled: false,
											 name: "dateEnd",
											 containerToggle: "fade"
										 }, replacedNode);  
										 
}
</script>

<!-- spanTarget:form -->

<!-- List -->

<h1><fmt:message key="admin.assignment.assignmentsFor" /> ${user.firstName} ${user.lastName}</h1>


<div class="GreyFrame">
	<h3>&nbsp;</h3>

	<div class="GreyFrameBody">			


<table class="contentTable" cellspacing="2">
	<tr>
		<th></th>
		<th><fmt:message key="admin.assignment.customerHeader" /></th>

		<th><fmt:message key="admin.assignment.projectCodeHeader" /></th>

		<th><fmt:message key="admin.assignment.projectHeader" /></th>

		<th><fmt:message key="admin.assignment.startHeader" /></th>
		
		<th><fmt:message key="admin.assignment.endHeader" /></th>		
	</tr>
	
	<c:forEach items="${assignments}" var="assignment">
		<tr>
			<td valign="top" rowspan="2">
				<a href="" onClick="return editAssignment(${user.userId}, ${assignment.assignmentId})" title="Edit assignment">
					<img src="<c:url value="/img/icons/grey_edit_assignment.png" />" alt="Edit assignment" border="0">
				</a>
			</td>
			
			<td class="main">${assignment.project.customer.name}</td>

			<td class="main">${assignment.project.projectCode}</td>			
		
			<td class="main">${assignment.project.name}</td>
			
			<td class="main">
				<fmt:formatDate value="${assignment.dateStart}" pattern="dd MMMM yyyy" />
			</td>
	
			<td class="main">
				<fmt:formatDate value="${assignment.dateEnd}" pattern="dd MMMM yyyy" />
			</td>
		</tr>
		
		<tr>
		
			<td colspan="5" class="extra">
				<fmt:message key="admin.assignment.typeHeader" />: 
					<c:choose>
						<c:when test="${assignment.assignmentType.assignmentTypeId == 0}">
							<fmt:message key="admin.assignment.dateRange" />
						</c:when>
						<c:when test="${assignment.assignmentType.assignmentTypeId == 1}">
							<fmt:message key="admin.assignment.default" />
						</c:when>
						<c:when test="${assignment.assignmentType.assignmentTypeId == 2}">
							<fmt:message key="admin.assignment.allotted" />
						</c:when>
					</c:choose>;&nbsp;&nbsp;
					
				<fmt:message key="admin.assignment.roleHeader" />: <c:out value="${assignment.role}" default="--"/>
				<c:if test="${assignment.assignmentType.assignmentTypeId != 1}">
					;&nbsp;&nbsp;<fmt:message key="admin.assignment.rateHeader" />: <fmt:formatNumber type="currency" currencySymbol="${currencySymbol}" value="${assignment.hourlyRate}" />
				</c:if>
			</td>
		</tr>
		
	</c:forEach>
</table>

<br><br>

	</div>
						
	<div class="GreyFrameFooter">
		<p>
		</p>
	</div>				
</div>	

	<div style="text-align: right; margin: 3px 16px 0 0; max-width: 730px;">
			<c:if test="${assignment.assignmentId != null}">
				<a href="" onClick="return showAddForm()" title="Add assignment"><img src="<c:url value="/img/icons/grey_add_assignment.png" />" border="0"></a>	
			</c:if>
	</div>

<br>

<!-- Form -->

<h1>
	<c:choose>
		<c:when test="${assignment.assignmentId == null}">
			<fmt:message key="admin.assignment.newAssignment" />
		</c:when>
		
		<c:otherwise>
			<fmt:message key="admin.assignment.editAssignment" />
		</c:otherwise>
	</c:choose>
</h1>

<form id="AssignmentForm" method="post" action="editAssignment.do">
<input type="hidden" name="assignmentId"  value="${assignment.assignmentId}">
	
<div class="GreyFrame">
	<h3>&nbsp;</h3>

	<div class="GreyFrameBody">			
		<table CLASS="contentTable" CELLSPACING=2>
		<tr>
			<td width="30%">
				<fmt:message key="admin.assignment.project" />:
			</td>
		
			<td>
				<select class="textInputSmall" name="projectId">
					<c:forEach items="${allProjects}" var="project">
						<option value="${project.projectId}" 
							<c:if test="${project.projectId== assignment.project.projectId}">
							SELECTED
						</c:if>
					> ${project.projectCode} - ${project.name}
					</c:forEach>
				</select>
			</td>
		
			<td style="color: red">
				<html:errors property="project" />
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="admin.assignment.role" />:
			</td>
			
			<td>
				<input class="textInputSmall" type="text" name="role" value="${assignment.role}">
			</td>
			
			<td></td>
		</tr>

		<tr><td colspan="3"><br></td></tr>
		
		<tr>
			<td>
				<fmt:message key="admin.assignment.type" />:
			</td>
			
			<td>
				<select id="assignmentTypeId" name="assignmentTypeId" class="textInputSmall" >
					<option value="0" <c:if test="${assignment.assignmentType.assignmentTypeId == 0}">SELECTED</c:if>><fmt:message key="admin.assignment.dateRange" />
					<option value="1" <c:if test="${assignment.assignmentType.assignmentTypeId == 1}">SELECTED</c:if>><fmt:message key="admin.assignment.default" />
					<option value="2" <c:if test="${assignment.assignmentType.assignmentTypeId == 2}">SELECTED</c:if>><fmt:message key="admin.assignment.allotted" />
				</select>
			</td>
			
			<td></td>
		</tr>		
	
		
		<tr id="allottedTr">
			<td>
				<fmt:message key="admin.assignment.allotted" />:
			</td>
			
			<td>
				<input class="textInputSmall"  size="6"  type="text" name="hourlyRate" value="${assignment.hourlyRate}"> hours
			</td>
			
			<td style="color: red">
			</td>
		</tr>	

		<tr>
			<td>
				<fmt:message key="admin.assignment.dateStart" />:
			</td>
			
			<td>
				<div id="dateStartDiv"></div>
			</td>
			
			<td style="color: red">
				<html:errors property="dateStart" />
			</td>
		</tr>
	
		<tr>
			<td>
				<fmt:message key="admin.assignment.dateEnd" />:
			</td>
			
			<td>
				<div id="dateEndDiv" />
			</td>
			
			<td></td>
		</tr>
	
		<tr>
			<td>
				<fmt:message key="admin.assignment.rate" />:
			</td>
			
			<td>
				<input class="textInputSmall"  size="6"  type="text" name="hourlyRate" value="${assignment.hourlyRate}"> ${currencySymbol}
			</td>
			
			<td id="hourlyRateError" style="color: red"></td>
		</tr>
	</table>
	</div>
						
	<div class="GreyFrameFooter">
		<p>
		</p>
	</div>				
</div>

<div style="float: left; margin: 3px 0 0 11px">
	<c:choose>
		<c:when test="${assignment.assignmentId != null && assignment.deletable}">
			<a href="" onClick="return deleteAssignment(${assignment.assignmentId})">
				<img src="<c:url value="/img/icons/white_delete.png" />"
					 alt="<fmt:message key="general.delete" />" 
					 title="<fmt:message key="general.delete" />" border="0">
			</a>
		</c:when>

		<c:when test="${assignment.assignmentId != null && !assignment.deletable}">
			<a href="" javascript:alert('<fmt:message key="admin.assignment.noDelete" />');return false">
				<img src="<c:url value="/img/icons/white_delete_disabled.png" />" border="0">
			</a>
		</c:when>
	</c:choose>
</div>

<div style="float: right; text-align: right; margin: 3px 16px 0 0; max-width: 730px;border: 0">					
	<input type="image" src="<c:url value="/img/icons/white_submit.png" />" border="0" class="submitNoBorder" alt="<c:choose>
					<c:when test="${assignment.assignmentId == null}">
						<fmt:message key="general.add" />
					</c:when>
					
					<c:otherwise>
						<fmt:message key="general.edit" />
					</c:otherwise>
				</c:choose>">
</div>
	
</form>

<script type="text/javascript">
	initDojo();
</script>
