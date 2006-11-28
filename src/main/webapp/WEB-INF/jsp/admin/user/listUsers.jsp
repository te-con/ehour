<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table CLASS="contentTable" CELLSPACING=2>
	<c:forEach items="${users}" var="user">
		<tr>
			<td>
				<a href="">
					<c:out value="${user.lastName}" />, <c:out value="${user.firstName}" />
				</a>
			</td>
		</tr>
	</c:forEach>

</table>
	