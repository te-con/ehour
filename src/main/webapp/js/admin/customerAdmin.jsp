<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

// form name
var adminForm = 'CustomerForm';
var adminFormSpan = 'customerFormSpan';
var adminFormUrl = 'addCustomerForm.do';
var adminListReceivedSpan = 'listCustomersSpan';

// validate form
function validateForm(formId)
{
	formId = 'CustomerForm';
	form = document.getElementById(formId);
	
	var validationRules = new Array(new Array("name", "customerNameError", '<fmt:message key="admin.customer.errorNameRequired" />'),
									new Array("code", "customerCodeError", '<fmt:message key="admin.customer.errorCodeRequired" />')
												);
	var validationLengthRules = new Array(new Array("name", "customerNameError", 255, '<fmt:message key="admin.customer.errorNameTooLong" />'),
											new Array("code", "customerCodeError", 32, '<fmt:message key="admin.customer.errorCodeTooLong" />')										
											);
	isValid = validateRequired(formId, validationRules);
	
	isValid = isValid && validateMaxLength(formId, validationLengthRules);
	
	if (isValid)
	{
		showLoadingData();
	}

	return isValid;
}

// delete customer event
function deleteCustomer(customerId)
{
	if (confirm("<fmt:message key="admin.customer.deleteConfirm" />"))
	{
		showLoadingData();
		
		dojo.io.bind({url: 'deleteCustomer.do',
					  handler: responseReceived,
                      content: {customerId: customerId}
                      });
	}
	
	return false;
}

// edit customer event
function editCustomer(editId)
{
	showLoadingData();
	
    dojo.io.bind({
                      url: 'getCustomer.do',
                      handler: responseReceived,
                      content: {customerId: editId}
                   });  
                   
	return false;    
}