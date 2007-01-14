<%@ page contentType="text/html; charset=ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<table CLASS="admin_nav_table" CELLSPACING=0>
	<tr>
		<td colspan=2><fmt:message key="admin.nav.userAdmin" />&nbsp;&nbsp;</td>
	</tr>

	<tr>
		<td rowspan=5>&nbsp;&nbsp;</td>
		<td><a href="<c:url value="/eh/admin/dept/index.do" />"><fmt:message key="admin.nav.departments" /></a></td>
	</tr>


	<tr>
		<td><a href="<c:url value="/eh/admin/user/index.do" />"><fmt:message key="admin.nav.users" /></a></td>
	</tr>

	<tr>
		<td><a href="<c:url value="/eh/admin/customer/index.do" />"><fmt:message key="admin.nav.customers" /></a></td>
	</tr>

	<tr>
		<td><a href="<c:url value="/eh/admin/project/index.do" />"><fmt:message key="admin.nav.projects" /></a></td>
	</tr>

	<tr>
		<td><a href="<c:url value="/eh/admin/assignment/index.do" />"><fmt:message key="admin.nav.assignments" /></a></td>
	</tr>
</table>
