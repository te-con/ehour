<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!-- spanTarget: list -->

<table class="contentTable" cellspacing="0">
<c:forEach items="${customers}" var="customer">
	<tr>
		<td><a href=""
			onClick="return editCustomer(${customer.customerId})">${customer.name}</a></td>
		<td>&nbsp;</td>
		<td align="right">${fn:length(customer.projects)} <fmt:message key="admin.customer.projects" /></td>
	</tr>
</c:forEach>
</table>