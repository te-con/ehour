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

	var dateStart = "<fmt:formatDate value="${initRange.dateStart}" pattern="yyyy-MM-dd" />T00:00:00-00:00";
	var dateEnd = "<fmt:formatDate value="${initRange.dateEndForDisplay}" pattern="yyyy-MM-dd" />T00:00:00-00:00";

	dojo.widget.createWidget("DropdownDatePicker",
											{disabled: false,
		  									value:dateStart,
											 name: "dateStart",
											 containerToggle: "fade"
										 }, replacedNode);  



	replacedNode = document.getElementById("dateEndDiv");
	dojo.widget.createWidget("DropdownDatePicker",
											{disabled: false,
		  									value:dateEnd,
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
	
		<div style="float: left;vertical-align: bottom;">
			<nobr><fmt:message key="admin.assignment.dateStart" />:</nobr>
		</div>
		
		<div style="float: right;width: 9em">
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
		
		<div style="float: left;vertical-align: bottom;">
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
		<br>
		
		<div style="float: left;vertical-align: bottom">
			Project:
		</div>
		
		<div style="float: right;vertical-align: bottom">
			<select name="projectId" class="textInputSmall" >
				<c:forEach items="${projects}" var="project">
					<option value="${project.projectId}">${project.name}
				</c:forEach>
			</select>
		</div>

		<br clear="all">
		<br>
		
		<div style="float: right;vertical-align: bottom;height: 1.5em">
			<input type="submit" value="submit" id="submitButton">
		</div>
	</div>

	<div class="NavCalFrameLeftBottom">
		<div class="NavCalFrameRightBottom">
			&nbsp;
		</div>
	</div>		
</div>
</div>
	</form>
	
<script>
	init();
</script>