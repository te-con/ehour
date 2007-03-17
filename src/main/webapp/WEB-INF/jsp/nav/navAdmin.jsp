<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!-- spanTarget:navCalendar -->
<div class="GreyNavFrame" style="margin-top: 1em;">
	<h3><fmt:message key="admin.nav.userAdmin" /></h3>

	<div class="GreyNavBody">
		<ul>
			<li><a href="<c:url value="/eh/admin/user/index.do" />"><fmt:message key="admin.nav.users" /></a>
			<li><a href="<c:url value="/eh/admin/dept/index.do" />"><fmt:message key="admin.nav.departments" />
			<br><br>
			<li><a href="<c:url value="/eh/admin/customer/index.do" />"><fmt:message key="admin.nav.customers" /></a>
			<li><a href="<c:url value="/eh/admin/project/index.do" />"><fmt:message key="admin.nav.projects" /></a>
			<li><a href="<c:url value="/eh/admin/assignment/index.do" />"><fmt:message key="admin.nav.assignments" /></a>
			<br><br>
			<li><a href="<c:url value="/eh/admin/config/index.do" />"><fmt:message key="admin.nav.config" /></a>
		</ul>
	</div>
	<div class="GreyNavFrameFooter">
		<p>
		</p>
	</div>	
</div>	

