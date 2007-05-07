<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- spanTarget: content -->

<script>
	function checkForm()
	{
		var form = document.getElementById('printForm');

		var selected = false;
		
		for (i = 0; i < form.projectId.length; i++)
		{
			if (form.projectId[i].checked)
			{
				selected = true;
				break;
			}
		}

		if (!selected)
		{
			alert("<fmt:message key="user.timesheet.print.noProjectSelected" />.");
			return false;
		}		

		return true;		
	}
</script>

<div class="ContentFrame">
	<h1><fmt:message key="user.timesheet.print.header" /> <fmt:formatDate value="${printDate.time}" pattern="MMMMM yyyy" /></h1>
	
	<div class="GreyFrame">
		<h3>&nbsp;</h3>
		
		<div class="BlueFrame">
			<div class="BlueLeftTop">
				<div class="BlueRightTop">
					&nbsp;
				</div>
			</div>	

			<form method="post" action="printTimesheet.do" target="_new" onsubmit="return checkForm()" id="printForm">
			
				<input type="hidden" name="fromForm" value="Y">

			<table class="TimesheetPrintFormTable">
			
				<tr>
					<td style="border: 0;padding-right: 20px">
						<nobr><fmt:message key="user.timesheet.print.selectProjects" /></nobr>
					</td>

					<td style="padding-left: 5px">					
						<c:forEach items="${projectAssignments}" var="projectAssignment">
							<input type="checkbox" 
									name="projectId"
									value="${projectAssignment.assignmentId}"
								>
									${projectAssignment.project.name} 
									<c:if test="${projectAssignment.role != null && projectAssignment.role != ''}">
										(${projectAssignment.role})
									</c:if>
										<br>
						</c:forEach>
					</td>
				</tr>

				<tr>
					<Td colspan="2" style="border: 0">
						<br>
					</Td>
				</tr>
	
				<tr>
					<td style="border: 0;padding-right: 20px">
						<nobr><fmt:message key="user.timesheet.print.inclSignatureSpace" /></nobr>
					</td>
					
					<td style="padding-left: 5px">
						<input type="checkbox" name="signatureSpace">
					</td>
				</tr>
				
				<tr>
					<td style="border: 0" colspan="2" align="right">
						<input type="submit" id="submitButton" value="<fmt:message key="user.timesheet.print.submit" /> >>"></td>
				</tr>
					

			</table>			
			</form>
			
				<div class="BlueLeftBottom">
					<div class="BlueRightBottom">
						&nbsp;
					</div>
				</div>			
			</div>

		<br>		
		<div class="GreyFrameFooter">
			<p>
			</p>
		</div>				
	</div>	
</div>				