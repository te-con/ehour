<%@ page contentType="text/html; charset=ASCII"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
	
	function submitDepartmentForm()
	{ 
		new dojo.io.FormBind({
    						formNode: dojo.byId('UserDepartmentForm'),
    						handler: departmentListReceived
							});
	}
	
	function departmentListReceived(type, xml, evt)
	{
		if (type == 'error')
		{	
    		alert("<fmt:message key="errors.ajax.general" />");
		}
		else
		{
			dojo.byId('listDeptsSpan').innerHTML = xml;
		}
	}
	
	function deleteDepartment(departmentId)
	{
		if (confirm('<fmt:message key="admin.dept.deleteConfirm" />'))
		{
			dojo.io.bind({url: 'deleteUserDepartment.do',
						  handler: departmentListReceived,
                       content: {departmentId: departmentId}
                       });
		}
		
		return false;
	}


	function editDepartment(editId)
	{
        dojo.io.bind({
                       url: 'getUserDepartment.do',
                       handler: formChanged,
                       content: {departmentId: editId}
                    });  
                    
		return false;    
	}
	
	function showAddForm()
	{
        dojo.io.bind({
                       url: 'addUserDepartmentForm.do',
                       handler: formChanged
                    });  
                    
		return false;    
	}		
		
    function formChanged(type, xml, evt)
    {
    	if (type == 'error')
    	{
    		alert("<fmt:message key="errors.ajax.general" />");
    		return;
    	}
    	else
    	{
			dojo.byId('departmentFormSpan').innerHTML = xml;
			
			// rebind
			submitDepartmentForm();
		}
    }

	dojo.addOnLoad(submitDepartmentForm);
</script>

<span id="listDeptsSpan">
	<tiles:insert page="listUserDepartments.jsp" />
</span>

<br>
<br>

<span id="departmentFormSpan">
	<tiles:insert page="addUserDepartmentForm.jsp" />
</span>
