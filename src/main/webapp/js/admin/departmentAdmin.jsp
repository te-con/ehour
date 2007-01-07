<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

// form name
var adminForm = 'UserDepartmentForm';
var adminFormSpan = 'userDepartmentFormSpan';
var adminFormUrl = 'addUserDepartmentForm.do';
var adminListReceivedSpan = 'listDepartmentsSpan';

// validate form
function validateForm()
{
	form = document.getElementById(adminForm);
	
	var validationRules = new Array(new Array("name", "departmentNameError", '<fmt:message key="admin.dept.errorNameRequired" />'),
									new Array("code", "departmentCodeError", '<fmt:message key="admin.dept.errorCodeRequired" />')
												);
	var validationLengthRules = new Array(new Array("name", "departmentNameError", 128, '<fmt:message key="admin.dept.errorNameTooLong" />'),
											new Array("code", "departmentCodeError", 32, '<fmt:message key="admin.dept.errorCodeTooLong" />')										
											);
	isValid = validateRequired(adminForm, validationRules);
	
	isValid = isValid && validateMaxLength(adminForm, validationLengthRules);
	
	if (isValid)
	{
		showLoadingData();
	}

	return isValid;
}

// delete department event
function deleteDepartment(departmentId)
{
	if (confirm("<fmt:message key="admin.dept.deleteConfirm" />"))
	{
		showLoadingData();
		
		dojo.io.bind({url: 'deleteUserDepartment.do',
					  handler: responseReceived,
                      content: {departmentId: departmentId}
                      });
	}
	
	return false;
}

// edit user department
function editUserDepartment(editId)
{
	showLoadingData();
	
    dojo.io.bind({
                      url: 'getUserDepartment.do',
                      handler: responseReceived,
                      content: {departmentId: editId}
                   });  
                   
	return false;    
}