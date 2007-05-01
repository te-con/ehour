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
											{disabled: false,
											 name: "dateStart",
											 containerToggle: "fade"
										 }, replacedNode);  



	replacedNode = document.getElementById("dateEndDiv");
	dojo.widget.createWidget("DropdownDatePicker",
											{disabled: false,
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
		
		<div style="float: right">
			<span id="dateStartDiv"></span>
		</div>
	
		<br><br>
		
		<div style="float: left;vertical-align: bottom;height: 1.5em">
			<nobr><fmt:message key="admin.assignment.dateEnd" />:</nobr>
		</div>
		
		<div style="float: right">
			<div id="dateEndDiv"></div>
		</div>
		
		<br>
		<br>
		<div style="float: left;vertical-align: bottom;margin-top: 5px">
			Project:
		</div>
		
		<div style="float: right;vertical-align: bottom">
			<select name="projectId" class="textInputSmall" >
				<c:forEach items="${projects}" var="project">
					<option value="${project.projectId}">${project.name}
				</c:forEach>
			</select>
		</div>
		<br><br><br>
		
		<div style="float: right;vertical-align: bottom;height: 1.5em">
			<input type="submit" value="submit" id="submitButton">
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
	