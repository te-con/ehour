<%@ page contentType="text/html; charset=ASCII"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
	
	function submitForm()
	{ 
		new dojo.io.FormBind({
    						formNode: dojo.byId('CustomerForm'),
    						handler: userListReceived
							});
	}
	
	function userListReceived(type, xml, evt)
	{
		if (type == 'error')
		{	
    		alert("<fmt:message key="errors.ajax.general" />");
		}
		else
		{
			dojo.byId('listUsersSpan').innerHTML = xml;
			dojo.byId('filterInput').focus();
//			showAddForm();
		}
	}
	
	function deleteCustomer(customerId)
	{
		if (confirm('<fmt:message key="admin.customer.deleteConfirm" />'))
		{
			dojo.io.bind({url: 'deleteCustomer.do',
						  handler: customerListReceived,
                       content: {customerId: customerId}
                       });
		}
		
		return false;
	}


	function editCustomer(editId)
	{
        dojo.io.bind({
                       url: 'getCustomer.do',
                       handler: formChanged,
                       content: {customerId: editId}
                    });  
                    
		return false;    
	}
	
	function showAddForm()
	{
        dojo.io.bind({
                       url: 'addCustomerForm.do',
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
			dojo.byId('customerFormSpan').innerHTML = xml;
			
			// rebind
			submitForm();
		}
    }

	function init()
	{
		dojo.event.connect(dojo.byId('filterInput'), "onkeyup", "filterKeyUp");
	}
	
	function filterKeyUp(evt)
	{
		var filterInput = dojo.byId('filterInput').value;
		
		dojo.io.bind({url: 'index.do',
					  handler: userListReceived,
                      content: {filterPattern: filterInput,
                      			fromForm: '1'}
                      });		
	}

	dojo.addOnLoad(init);
</script>

<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td rowspan="3" valign="top"><fmt:message key="admin.user.filter" />:</td>
		<td><form><input class="normtxt" type="text" name="filter"
					size="30" id="filterInput"></form>
		</td>
		<td rowspan=3>&nbsp;&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	
	<tr>
		<td>
			<div class="userScroll">
			<span id="listUsersSpan">
				<tiles:insert page="listUsers.jsp" />
			</span>
			</div>
		</td>
		
		<td  valign="top" rowspan="2">
			<span id="userFormSpan">
				<tiles:insert page="/eh/admin/user/addUserForm.do" />
			</span>
		</td>
	</tr>
	<tr>
		<td align=right>
			<a href="" onClick="return showAddForm()"><fmt:message key="admin.user.addUser" /></a>
		</td>
		
	</tr>
</table>
