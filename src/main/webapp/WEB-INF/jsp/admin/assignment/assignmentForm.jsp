<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<script type="text/javascript">

function init()
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

<h1><fmt:message key="admin.assignment.assignmentsFor" /> ${user.firstName} ${user.lastName}</h1>


<div class="GreyFrame">
	<h3>&nbsp;</h3>

	<div class="GreyFrameBody">			

<table class="contentTable" cellspacing="2">
	<tr>
		<th><fmt:message key="admin.assignment.projectHeader" /></th>

		<th><fmt:message key="admin.assignment.descShort" /></th>

		<th><fmt:message key="admin.assignment.startEndDate" /></th>
		
		<th><fmt:message key="admin.assignment.rateHeader" /></th>
	</tr>
	
	<c:forEach items="${assignments}" var="assignment">
	<tr>
		<td>
			<a href="" onClick="return editAssignment(${user.userId}, ${assignment.assignmentId})">${assignment.project.name}
		</td>
		
		<td>
			<c:out value="${assignment.description}" default="--"/>
		</td>
		
		<td>
			<fmt:formatDate value="${assignment.dateStart}" pattern="dd MMM yyyy" /> - <fmt:formatDate value="${assignment.dateEnd}" pattern="dd MMM yyyy" />
		</td>

		<td>
			<fmt:formatNumber type="currency" value="${assignment.hourlyRate}" />
		</td>
	</tr>
	
	</c:forEach>
	
	<tr>
		<td colspan="4" align="right">
			<c:if test="${assignment.assignmentId != null}">
				<a href="" onClick="return showAddForm()"><fmt:message key="admin.assignment.newAssignmentLink" /></a>	
			</c:if>
			<br>
		</td>
	</tr>
</table>

<br><br>

<form id="AssignmentForm" method="post" action="editAssignment.do">

<input type="hidden" name="assignmentId"  value="${assignment.assignmentId}">

<table CLASS="contentTable" CELLSPACING=2>

<tr>
	<th colspan="2">
		<c:choose>
			<c:when test="${assignment.assignmentId == null}">
				<fmt:message key="admin.assignment.newAssignment" />
			</c:when>
			
			<c:otherwise>
				<fmt:message key="admin.assignment.editAssignment" />
			</c:otherwise>
		</c:choose>	
	</th>
</tr>

<tr>
	<td>
		<fmt:message key="admin.assignment.descLong" />:
	</td>
	
	<td>
		<input class="textInputSmall" type="text" name="description" value="${assignment.description}">
	</td>
	
	<td></td>
</tr>

<tr>
	<td>
		<fmt:message key="admin.assignment.project" />:
	</td>

	<td>
		<select class="textInputSmall" name="projectId">
<c:forEach items="${allProjects}" var="project">
<option value="${project.projectId}" 
	<c:if test="${project.projectId== assignment.project.projectId}">
		SELECTED
	</c:if>
> ${project.name}
</c:forEach>
		</select>
	</td>
	
	<td style="color: red">
		<html:errors property="project" />
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
		<input class="textInputSmall" type="text" name="hourlyRate" value="${assignment.hourlyRate}">
	</td>
	
	<td id="hourlyRateError" style="color: red"></td>
</tr>

<tr>
	<td>
		<c:if test="${assignment.assignmentId != null && assignment.deletable}">
			<a href="" onClick="return deleteAssignment(${assignment.assignmentId})"><fmt:message key="general.delete" /></a>	
		</c:if>
		&nbsp;
	</td>
	
	<td align="right">
		<c:choose>
			<c:when test="${assignment.assignmentId == null}">
				<input type="submit" class="submitButtonBlue" value="<fmt:message key="general.add" />">
			</c:when>
			
			<c:otherwise>
				<input type="submit" class="submitButtonBlue" value="<fmt:message key="general.edit" />">
			</c:otherwise>
		</c:choose>		
	</td>	
</tr>

<c:if test="${assignment.assignmentId != null && !assignment.deletable}">
<tr>
	<td colspan="2">
		<br>
		<fmt:message key="admin.assignment.noDelete" />
	</td>
</tr>
</c:if>		


</table>


</form>

	</div>
						
	<div class="GreyFrameFooter">
		<p>
		</p>
	</div>				
</div>						


<script type="text/javascript">
	init();
</script>
