<%@ page contentType="text/html; charset=ASCII"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script src="../../../js/dojo.js" type="text/javascript"></script>

<script src="<c:url value="/js/validation.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/base.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/admin/departmentAdmin.jsp" />" type="text/javascript"></script>
<script src="<c:url value="/js/admin/baseAdmin.js" />" type="text/javascript"></script>

<table CLASS="contentTable" CELLSPACING=2>
	<tr>	
		<td>
			<div class="adminListScroll">
				<span id="listDepartmentsSpan">
					<tiles:insert page="listUserDepartments.jsp" />
				</span>
			</div>
		</td>
		
		<td>
			&nbsp;
		</td>
		
		<td valign="top">
			<span id="userDepartmentFormSpan">
				<tiles:insert page="userDepartmentForm.jsp" />
			</span>
		</td>		
	</tr>
	
	<tr>
		<td align="right">
			<a href="" onClick="return showAddForm()"><fmt:message key="admin.dept.addDepartment" /></a>
		</td>
		
		<td valign="top" align="right" colspan="2" style="color: #913023" id="statusMessage">
			&nbsp;
		</td>
	</tr>
</table>
	
<br>
<br>
