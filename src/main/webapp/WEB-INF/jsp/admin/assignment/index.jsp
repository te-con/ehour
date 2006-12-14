<%@ page contentType="text/html; charset=ASCII"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
	var ajaxError = "<fmt:message key="errors.ajax.general" />";
	var formSuccess = "<fmt:message key="admin.general.formSuccess" />";
	var sendingData = "<fmt:message key="general.submitting" />";
	var loadingMsg = "<fmt:message key="general.loading" />";	
</script>

<script src="../../../js/dojo.js" type="text/javascript"></script>

<script type="text/javascript">
	dojo.require("dojo.widget.*");
	dojo.require("dojo.widget.DropdownDatePicker");
</script>

<script src="<c:url value="/js/validation.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/base.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/admin/baseAdmin.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/admin/assignmentAdmin.js" />" type="text/javascript"></script>

<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td valign="top"><fmt:message key="admin.user.filter" />:</td>
		<td><form onSubmit="return false;"><input class="normtxt" type="text" name="filter"
					size="30" id="filterInput"></form>
		</td>
		<td valign="top" align="left" style="color: #913023" id="statusMessage">
			&nbsp;
		</td>
	</tr>
	
	<tr>
		<td valign="top" colspan="2">
			<div class="adminListScroll">
				<span id="listUsersSpan">
					<tiles:insert page="../user/listUsers.jsp" />
				</span>
			</div>
		</td>
		
		<td valign="top">
			<span id="assignmentFormSpan">
				&nbsp;	
			</span>
		</td>
	</tr>
</table>


