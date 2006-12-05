<%@ page contentType="text/html; charset=ASCII"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script src="<c:url value="/js/validation.js" />" type="text/javascript"></script>

<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td>
			<table CLASS="contentTable" CELLSPACING=2 width="100%">
				<tr>
					<td colspan="2" valign="top"><fmt:message key="admin.user.hideInactive" />:
					<input class="normtxt" type="checkbox" id="hideInactive" name="hideInactive" checked></td>
				</tr>
			</table>
		</td>
		
		<td valign="top" align="right" style="color: #913023" id="statusMessage">
			&nbsp;
		</td>
	</tr>
<!-- 
	<tr>
		<td>
			<div class="userScroll">
				<span id="listUsersSpan">
					<tiles:insert page="listUsers.jsp" />
				</span>
			</div>
		</td>
		
		<td valign="top" rowspan="2">
			<span id="userFormSpan">
				<tiles:insert page="/eh/admin/user/addUserForm.do" />
			</span>
		</td>
	</tr>
	<tr>
		<td align=right>
			<a href="" onClick="return showAddForm()"><fmt:message key="admin.user.addUser" /></a>
		</td>
	</tr>
</table>
 -->	
