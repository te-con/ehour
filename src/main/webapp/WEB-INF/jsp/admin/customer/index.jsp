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
    						handler: customerListReceived
							});
	}
	
	function customerListReceived(type, xml, evt)
	{
		if (type == 'error')
		{	
    		alert("<fmt:message key="errors.ajax.general" />");
		}
		else
		{
			dojo.byId('listCustomersSpan').innerHTML = xml;
			
			showAddForm();
		}
	}
	
	function deleteCustomer(customerId)
	{
		if (confirm("<fmt:message key="admin.customer.deleteConfirm" />"))
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

	dojo.addOnLoad(submitForm);
</script>


	<span id="listCustomersSpan">
		<tiles:insert page="listCustomers.jsp" />
	</span>
	
<br>
<br>

<span id="customerFormSpan">
	<tiles:insert page="addCustomerForm.jsp" />
</span>
