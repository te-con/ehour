<%@ page contentType="text/html; charset=ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
	function editDepartment(editId)
	{
        dojo.io.bind({
                       url: 'getUserDepartment.do',
                       handler: departmentReceived,
                       content: {departmentId: editId}
                    });  
                    
		return false;    
	}
		
    function departmentReceived(type, data, evt)
    {
		if (type == 'error')
      	{
        	alert('Error when retrieving data from the server!');
      	}
      	else
      	{
        	alert(data);
        }
    }
			
</script>

<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td><fmt:message key="general.department" /></td>
		<td>&nbsp;</td>
		<td><fmt:message key="admin.dept.users" /></td>
		<td>&nbsp;</td>
		<td colspan=2></td>
	</tr>

	<tr>
		<td colspan="5"><img src="<c:url  value="/img/eh_pixel.gif" />"
			alt="pix" height="1" width="100%"><br>
		</td>
	</tr>

	<c:forEach items="${userDepartments}" var="userDept">
		<tr>
			<td><c:out value="${userDept.name}" /></td>
			<td>&nbsp;</td>
			<td><c:out value="${fn:length(userDept.users)}" /></td>
			<td><c:if test="${fn:length(userDept.users) == 0}">
				<fmt:message key="general.delete" />
			</c:if></td>
			<td>&nbsp;</td>
			<td><a href="" onClick="return editDepartment(<c:out value="${userDept.departmentId}" />)">
				<fmt:message key="general.edit" /></a></td>
		</tr>
	</c:forEach>
</table>

<br>
<br>


<form name="UserDepartmentForm" method="post"
	onSubmit=">

<input type="hidden" id="departmentId" name="departmentId" value="-1">

<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td colspan="3"><fmt:message key="admin.dept.editDepartment" /></td>
	</tr>

	<tr>
    	<td colspan="3"><img src="<c:url  value="/img/eh_pixel.gif" />" alt="pix" height="1" width="100%"><br></td>
	</tr>

	
	<tr>
		<td><fmt:message key="admin.dept.name" />:</td>
		<td><input class="normtxt"  type="text" id="name" name="name"></td>

		<td rowspan=2>
			&nbsp;&nbsp;
		</td>
		
		<td rowspan="3" valign="top">
			<span id="attachedUsers">
				&nbsp;
			</span>
		</td>
	</tr>

	<tr>
		<td><fmt:message key="admin.dept.code" />:</td>
		<td><input class="normtxt"  type="text" id="code" name="code"></td>
	</tr>
	
	<tr>
		<td colspan="2" align="right">
			<input type="submit" class="redSubmit" value="<fmt:message key="general.edit" />">
		</td>
	</tr>

</table>
</form>