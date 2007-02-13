<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/ehour-common.tld" prefix="ehour" %>

<!-- spanTarget:navCalendar -->
<script src="../../js/dojo.js" type="text/javascript"></script>
<script src="../../js/userReport.js" type="text/javascript"></script>

<script type="text/javascript">
	dojo.require("dojo.widget.*");
	dojo.require("dojo.widget.DropdownDatePicker");

function init()
{
	var replacedNode = document.getElementById("dateStartDiv");
	dojo.widget.createWidget("DropdownDatePicker",
											{value:"<fmt:formatDate value="${criteria.reportRange.dateStart}" pattern="yyyy-MM-dd" />T00:00:00-00:00",
											 disabled: false,
											 name: "dateStart",
											 containerToggle: "fade"
										 }, replacedNode);  



	replacedNode = document.getElementById("dateEndDiv");
	dojo.widget.createWidget("DropdownDatePicker",
											{value:"<fmt:formatDate value="${criteria.reportRange.dateEnd}" pattern="yyyy-MM-dd" />T00:00:00-00:00",
											 disabled: false,
											 name: "dateEnd",
											 containerToggle: "fade"
										 }, replacedNode);  
		
	bindCriteriaForm();										 
}
</script>

<div id="NavCalTarget">
<div class="NavCalFrame">
	<div class="NavCalFrameLeftTop">
		<div class="NavCalFrameRightTop">
			&nbsp;
		</div>
	</div>

	<form method="post" action="projectReport.do" id="criteriaForm">
	
	<table class="criteriaTable" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<fmt:message key="admin.assignment.dateStart" />:
				<div id="dateStartDiv"></div>
			</td>
		</tr>	
	
		<tr>
			<td>
				<fmt:message key="admin.assignment.dateEnd" />:
				<div id="dateEndDiv"></div>
			</td>
		</tr>	
	
	
		<tr>	
			<td valign="top">
				Project:
				<select size="2" name="projectId" multiple="multiple" class="textInput">
					<option value="-1">(All)
					<c:forEach items="${criteria.availableCriteria.projects}" var="project">
						<option value="${project.projectId}">${project.name}
					</c:forEach>
				</select>
			</td>
		</tr>
		
		<tr>
			<td colspan="2" align="right">
				<input type="submit" value="submit" id="submitButton">
			</td>
		</tr>
	</table>
	</form>

	<div class="NavCalFrameLeftBottom">
		<div class="NavCalFrameRightBottom">
			&nbsp;
		</div>
	</div>		
</div>
</div>

<script type="text/javascript">
	init();
</script>
	