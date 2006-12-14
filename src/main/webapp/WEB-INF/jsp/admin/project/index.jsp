<%@ page contentType="text/html; charset=ASCII"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
	var ajaxError = "<fmt:message key="errors.ajax.general" />";
	var nameRequired = "<fmt:message key="admin.project.errorNameNotNull" />";
	var codeRequired = "<fmt:message key="admin.project.errorCodeNotNull" />";
	var formSuccess = "<fmt:message key="admin.general.formSuccess" />";
	var sendingData = "<fmt:message key="general.submitting" />";
	var loadingMsg = "<fmt:message key="general.loading" />";	
</script>

<script src="../../../js/dojo.js" type="text/javascript"></script>
<script src="<c:url value="/js/validation.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/base.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/admin/baseAdmin.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/admin/projectAdmin.js" />" type="text/javascript"></script>


<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td>
			<table CLASS="contentTable" CELLSPACING=2 width="100%">
				<tr>
					<td colspan="2" valign="top"><fmt:message key="admin.project.hideInactive" />:
					<input class="normtxt" type="checkbox" id="hideInactive" name="hideInactive" checked></td>
				</tr>
			</table>
		</td>
		
		<td valign="top" align="right" style="color: #913023" id="statusMessage">
			&nbsp;
		</td>
	</tr>

	<tr>
		<td>
			<div class="adminListScroll">
				<span id="listProjectsSpan">
					<tiles:insert page="listProjects.jsp" />
				</span>
			</div>
		</td>

		<td valign="top" rowspan="2">
			<span id="projectFormSpan">
				<tiles:insert page="/eh/admin/project/addProjectForm.do" />
			</span>
		</td>
	</tr>
	<tr>
		<td align=right>
			<a href="" onClick="return showAddForm()"><fmt:message key="admin.project.addProject" /></a>
		</td>
	</tr>
</table>
