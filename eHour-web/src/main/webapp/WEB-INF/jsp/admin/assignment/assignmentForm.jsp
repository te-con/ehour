<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript">

function initDojo()
{
	var replacedNode = document.getElementById("dateStartDiv");
	dojo.widget.createWidget("DropdownDatePicker",
											{<c:if test="${assignment.dateStart != null}">
												value:"<fmt:formatDate value="${assignment.dateStart}" pattern="yyyy-MM-dd" />T00:00:00-00:00",
											</c:if>
											 disabled: false,
											 name: "dateStart",
											 containerToggle: "fade"
										 }, replacedNode);  



	replacedNode = document.getElementById("dateEndDiv");
	dojo.widget.createWidget("DropdownDatePicker",
											{<c:if test="${assignment.dateEnd != null}">
												value:"<fmt:formatDate value="${assignment.dateEndForDisplay}" pattern="yyyy-MM-dd" />T00:00:00-00:00",
											 </c:if>
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
		<th><fmt:message key="admin.assignment.projectHeader" /></th>
		<th><fmt:message key="admin.assignment.projectCodeHeader" /></th>
		<th><fmt:message key="admin.assignment.customerHeader" /></th>
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
			
			<td class="main">${assignment.project.name}</td>
			<td class="main">${assignment.project.projectCode}</td>			
			<td class="main">${assignment.project.customer.name}</td>
		
			
			<td class="main">
				<c:choose>
					<c:when test="${assignment.dateStart == null}">
						<span title="<fmt:message key="user.overview.infinite" />">&infin;</span>
					</c:when>
					<c:otherwise>
						<fmt:formatDate value="${assignment.dateStart}" pattern="dd MMM yyyy" />
					</c:otherwise>
				</c:choose>
			</td>
	
			<td class="main">
				<c:choose>
					<c:when test="${assignment.dateEnd == null}">
						<span title="<fmt:message key="user.overview.infinite" />">&infin;</span>
					</c:when>
					<c:otherwise>
						<fmt:formatDate value="${assignment.dateEndForDisplay}" pattern="dd MMM yyyy" />
					</c:otherwise>
				</c:choose>

			</td>
		</tr>
		
		<tr>
		
			<td colspan="5" class="extra">
				<fmt:message key="admin.assignment.typeHeader" />: 
					<c:choose>
						<c:when test="${assignment.assignmentType.assignmentTypeId == 0}">
							<fmt:message key="admin.assignment.dateRange" />
						</c:when>
						<c:when test="${assignment.assignmentType.assignmentTypeId == 2}">
							<fmt:message key="admin.assignment.allottedFixed" />
						</c:when>
						<c:when test="${assignment.assignmentType.assignmentTypeId == 3}">
							<fmt:message key="admin.assignment.allottedFlex" />
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
				<select class="textInputSmall" name="projectId" id="projectSelector">
					<c:forEach items="${allProjects}" var="project">
						<option value="${project.projectId}" 
							<c:if test="${project.projectId== assignment.project.projectId}">
							SELECTED
						</c:if>
					> ${project.name} - ${project.projectCode} - ${project.customer.name}
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
					<option value="2" <c:if test="${assignment.assignmentType.assignmentTypeId == 2}">SELECTED</c:if>><fmt:message key="admin.assignment.allottedFixed" />
					<option value="3" <c:if test="${assignment.assignmentType.assignmentTypeId == 3}">SELECTED</c:if>><fmt:message key="admin.assignment.allottedFlex" />
				</select>
			</td>
			
			<td></td>
		</tr>		
	
		
		<tr id="allottedTr">
			<td>
				<fmt:message key="admin.assignment.allotted" />:
			</td>
			
			<td>
				<input class="textInputSmall"  size="6"  type="text" id="allottedHours" name="allottedHours" value="${assignment.allottedHours}"> hours
			</td>
			
			<td style="color: red" id="allottedHoursError">
				<html:errors property="allottedHours" />
			</td>
		</tr>	


		<tr id="allowedOverrunTr">
			<td>
				<fmt:message key="admin.assignment.allowedOverrun" />:
			</td>
			
			<td>
				<input class="textInputSmall" 
						size="6"
						type="text" id="allowedOverrun" name="allowedOverrun" value="${assignment.allowedOverrun}"> hours
			</td>
			
			<td style="color: red" id="allowedOverrunError">
				<html:errors property="allowedOverrun" />
			</td>
		</tr>	

		<tr id="notifyPmTr">
			<td>
				<nobr><fmt:message key="admin.assignment.notifyPm" /></nobr>
			</td>
		
			<td colspan="2" style="color: #aaaaa;">
				<input class="textInputSmall" type="checkbox" id="notifyPm" name="notifyPm" <c:if test="${assignment.notifyPm}">checked</c:if>>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="notifyPmMsg">&nbsp;</span>
			</td>
		</tr>
		
		<tr>
			<td colspan="3">
				<br>
			</td>
		</tr>
		

		<tr id="dateStartTr">
			<td valign="top">
				<fmt:message key="admin.assignment.dateStart" />:
			</td>
			
			<td>
				<div id="dateStartSelect">
					<div id="dateStartDiv"></div>
					<br>
				</div>
				
				<input type="checkbox" name="infiniteStartDate"
						class="textInputSmall"
						id="infiniteStartDateId"
						<c:if test="${assignment.dateStart == null}">
							CHECKED
						</c:if>
						>
				<fmt:message key="admin.assignment.infiniteStart" />
			</td>
			
			<td style="color: red" valign="top">
				<html:errors property="dateStart" />
			</td>
		</tr>
	
		<tr id="dateEndTr">
			<td valign="top">
				<fmt:message key="admin.assignment.dateEnd" />:
			</td>
			
			<td>
				<div id="dateEndSelect">
					<div id="dateEndDiv"></div>
					<br>
				</div>

				<input type="checkbox"
						name="infiniteEndDate"
						class="textInputSmall"
						id="infiniteEndDateId"
						<c:if test="${assignment.dateEnd == null}">
							CHECKED
						</c:if>
						>
				<fmt:message key="admin.assignment.infiniteEnd" />
				<br><br>
			</td>
			
			<td style="color: red" valign="top">
				<html:errors property="dateEnd" />
			</td>
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
			<a href="" onClick="alert('<fmt:message key="admin.assignment.noDelete" />');return false">
				<img src="<c:url value="/img/icons/white_delete_disabled.png" />" border="0">
			</a>
		</c:when>
	</c:choose>
</div>

<div class="SubmitButtonPos">
	<input type="image" src="<c:url value="/img/icons/white_submit.png" />" border="0" class="submitNoBorder" alt="<c:choose><c:when test="${assignment.assignmentId == null}"><fmt:message key="general.add" /></c:when><c:otherwise><fmt:message key="general.edit" /></c:otherwise></c:choose>">
</div>
	
</form>

<script type="text/javascript">
	initDojo();
</script>
