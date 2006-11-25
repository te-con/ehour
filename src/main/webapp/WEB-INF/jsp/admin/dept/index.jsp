<%@ page contentType="text/html; charset=ASCII"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
	function submitEditDepartmentForm()
	{ 
		new dojo.io.FormBind({
    						formNode: dojo.byId('UserDepartmentForm'),
    						handler: departmentListReceived
							});
							
		resetForm();
	}
	
	function departmentListReceived(type, xml, evt)
	{
		if (type == 'error')
		{	
			alert('crap')
		}
		else
		{
			dojo.byId('listDepts').innerHTML = xml;
			resetForm();
		}
	}
	
	function resetForm()
	{
    	document.getElementById('departmentId').value = "";
    	document.getElementById('name').value = "";
    	document.getElementById('code').value = "";
    	
    	document.getElementById('attachedUsers').innerHTML  = "";
	}


	function editDepartment(editId)
	{
        dojo.io.bind({
                       url: 'getUserDepartment.do',
                       mimetype: "text/xml",
                       handler: departmentReceived,
                       content: {departmentId: editId}
                    });  
                    
		return false;    
	}
		
    function departmentReceived(type, xml, evt)
    {
    	document.getElementById('departmentId').value = xml.getElementsByTagName("DEPARTMENT")[0].getAttribute('ID');
    	document.getElementById('name').value = xml.getElementsByTagName("NAME")[0].firstChild.nodeValue;
    	document.getElementById('code').value = xml.getElementsByTagName("CODE")[0].firstChild.nodeValue;
    	
    	var users = xml.getElementsByTagName("USERS")[0];
    	
    	if (users != null)
    	{
    		var user = users.getElementsByTagName("USER");
    		var html = "<b>Users</b><br>";

			if (user.length > 0)
			{
    		
	    		for (var i = 0; i < user.length && i < 10; i++)
	    		{
	    			html += user[i].firstChild.nodeValue + "<br>";
				}
				
				if (user.length >= 10)
				{
					html += "...<br>";
				}
			}
			else
			{
				html += "none";
			}
				
			document.getElementById('attachedUsers').innerHTML = html;
			
		}
    }

	dojo.addOnLoad(submitEditDepartmentForm);
</script>



	<span id="listDepts">
		<tiles:insert page="listUserDepartments.jsp" />
	</span>

<br>
<br>


<form id="UserDepartmentForm" method="post" action="editUserDepartment.do">

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