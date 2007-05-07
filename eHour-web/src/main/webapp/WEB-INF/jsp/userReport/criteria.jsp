<%@ page contentType="text/html; charset=UTF-8"%>
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
											{value:"<fmt:formatDate value="${criteria.reportRange.dateEndForDisplay}" pattern="yyyy-MM-dd" />T00:00:00-00:00",
											 disabled: false,
											 name: "dateEnd",
											 containerToggle: "fade"
										 }, replacedNode);  
		
	bindCriteriaForm();										 
}
</script>

<form method="post" action="projectReport.do" id="criteriaForm">

<div id="NavCalTarget">
<div class="NavCalFrame">
	<div class="NavCalFrameLeftTop">
		<div class="NavCalFrameRightTop">
			&nbsp;
		</div>
	</div>

	
	<div class="UserReportCriteria">
	
		<div style="float: left;vertical-align: bottom;height: 1.5em">
			<fmt:message key="admin.assignment.dateStart" />:
		</div>
		
		<div style="float: right;width :9em">
			<div id="dateStartId"><span id="dateStartDiv"></span><br></div>
				<input type="checkbox"
						name="infiniteStartDate"
						class="textInputSmall"
						id="infiniteStartDateId"
						CHECKED
						>
				<nobr><fmt:message key="general.useEarliestAvailDate" /></nobr>
			</div>	

		<br clear="all">
		<br>
				
		<div style="float: left;vertical-align: bottom;height: 1.5em">
			<nobr><fmt:message key="admin.assignment.dateEnd" />:</nobr>
		</div>
		
		<div style="float: right;width: 9em">
			<div id="dateEndId"><span id="dateEndDiv"></span><br></div>
				<input type="checkbox"
						name="infiniteEndDate"
						class="textInputSmall"
						id="infiniteEndDateId"
						CHECKED
						>
				<nobr><fmt:message key="general.useLastAvailDate" /></nobr>
		</div>
		
		<br clear="all">
		
		<div style="float: left;vertical-align: bottom;margin-top: 5px">
			Project:
		</div>
		
		<div style="float: right;vertical-align: bottom">
			<select size="2" name="projectId" multiple="multiple" class="textInputSmall" >
				<option value="-1">(All)
				<c:forEach items="${criteria.availableCriteria.projects}" var="project">
					<option value="${project.projectId}">${project.name}
				</c:forEach>
			</select>
		</div>
		
		<br clear="all">
		<br>
		
		<div style="float: right;vertical-align: bottom;height: 1.5em">
			<input type="submit" value="<fmt:message key="userReport.criteria.showReport" />" id="submitButton">
		</div>
		<Br>
	</div>

	<div class="NavCalFrameLeftBottom">
		<div class="NavCalFrameRightBottom">
			&nbsp;
		</div>
	</div>		
</div>
</div>
	</form>

<script type="text/javascript">
	init();
</script>
	