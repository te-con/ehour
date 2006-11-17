<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:forEach items="${userDepartments}" var="userDept">
	<c:out value="${userDept.name}" /> - 	<c:out value="${userDept.users}" /> - 
</c:forEach>