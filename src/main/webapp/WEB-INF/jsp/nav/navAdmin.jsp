<%@ page contentType="text/html; charset=ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<table CLASS="cps_table" CELLSPACING=0>
	<tr>
		<th><fmt:message key="admin.nav.userAdmin" />&nbsp;&nbsp;</th>
	</tr>

	<tr>
		<td class="cps_admin"><a href="<c:url value="/eh/admin/dept/index.do" />"><fmt:message
			key="admin.nav.departments" /></a></td>
	</tr>


	<tr>
		<td class="cps_admin"><a href="<c:url value="/eh/admin/users/index.do" />"><fmt:message
			key="admin.nav.users" /></a></td>
	</tr>

	<tr>
		<td class="cps_admin"><a href="<c:url value="/eh/admin/customers/index.do" />"><fmt:message
			key="admin.nav.clients" /></a></td>
	</tr>

	<tr>
		<td class="cps_admin"><a href="<c:url value="/eh/admin/projects/index.do" />"><fmt:message
			key="admin.nav.projects" /></a></td>
	</tr>

	<tr>
		<td class="cps_admin"><a href="<c:url value="/eh/admin/assignments/index.do" />"><fmt:message
			key="admin.nav.assignments" /></a></td>
	</tr>
</table>
