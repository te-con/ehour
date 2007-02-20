<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- spanTarget: customerList -->

<select multiple="multiple" name="customerId" id="customerId" size="4" class="textInputSmall" style="width: 100%">
	<option value="-1"
		<c:if test="${criteria.userCriteria.emptyCustomers}">
			SELECTED
		</c:if>>{<fmt:message key="report.criteria.all" />}
	
	<c:forEach items="${criteria.availableCriteria.customers}" var="customer">
		<option value="${customer.customerId}"
		<c:set var="cId" value="${customer.customerId}-" />
		<c:if test="${fn:contains(criteria.userCriteria.customersAsString,  cId)}">
				SELECTED
		</c:if>
		>${customer.name}
	</c:forEach>
</select>