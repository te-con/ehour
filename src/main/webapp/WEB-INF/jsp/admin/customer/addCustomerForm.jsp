<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<form id="CustomerForm" method="post" action="editCustomer.do">

<input type="hidden" name="userId" value="">

<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td colspan="2"><fmt:message key="admin.user.addUser" /></td>
	</tr>

	<tr>
    	<td colspan="2"><img src="<c:url  value="/img/eh_pixel.gif" />" alt="pix" height="1" width="100%"><br></td>
	</tr>

	
	<tr>
		<td><fmt:message key="admin.customer.name" />:</td>
		<td><input class="normtxt"  type="text" name="name" size="30"></td>
	</tr>

	<tr>
		<td><fmt:message key="admin.customer.code" />:</td>
		<td><input class="normtxt"  type="text" name="code" size="30"></td>
	</tr>

	<tr>
		<td valign="top"><fmt:message key="admin.customer.description" />:</td>
		<td><textarea class="normtxt" name="description" cols="27" rows="3" wrap="virtual"></textarea></td>
	</tr>

	
	<tr>
		<td colspan="2" align="right">
			<input type="submit" class="redSubmit" value="<fmt:message key="general.add" />">
		</td>
	</tr>
</table>
</form>