// onChange is fired when the user switches from add to edit user, cancel if necc.
var	cancelUserCheck = false;

var adminForm = 'UserForm';
var adminFormSpan = 'userFormSpan';
var adminFormUrl = 'addUserForm.do';
var adminListReceivedSpan = 'listUsersSpan';

// validate form
function validateForm()
{
	form = document.getElementById(adminForm);
	
	var validationRules = new Array(new Array("username", "userNameError", usernameRequired),
												 new Array("lastName", "lastNameError", lastNameRequired),
												 new Array("roles", "userRoleError", userRoleRequired)		 												 
												);
	
	// edit user doesn't require password
	if (form.userId.value == "")
	{
		validationRules[3] =  new Array("password", "passwordError", passwordRequired);
	}

	isValid = validateRequired(adminForm, validationRules);

	isValid = isValid && validateEmail(adminForm, new Array(new Array("email", "emailError", emailNotValid)
													));
	
	if (form.password.value != form.confirmPassword.value)
	{	
		document.getElementById('confirmPasswordError').innerHTML = noPasswordMatch;
		isValid = false;
	}
	else
	{
		document.getElementById('confirmPasswordError').innerHTML = "";
	}

	if (isValid)
	{
		showLoadingData();
	}

	return isValid;
}

// check if user exists
// orgUsername is the orignal username when editing an existing user
// of course if the value is the original username, don't bother to check
function checkUserExists(username, orgUsername)
{
	if (username == orgUsername)
	{
		dojo.byId('userNameError').innerHTML =  "&nbsp;";	
	}
	else if (!cancelUserCheck && username != "")
	{
        dojo.io.bind({
                       url: 'userExistsCheck.do',
                       handler: userCheckDone,
                       mimetype: "text/xml",
                       content: {username: username}
                    }); 
	}
	                    
	return false;	
}

// handler for checkUserExists
function userCheckDone(type, xml, evt)
{
	if (type == 'error')
	{	
   		alert(ajaxError);
	}
	else
	{	
		var usr = xml.getElementsByTagName("userExists");
		
		if (usr[0].firstChild.nodeValue == "true")
		{
			document.getElementById('userNameError').innerHTML = userExistError;
		}
		else
		{
			dojo.byId('userNameError').innerHTML =  "&nbsp;";
		}
		
	}
}

// received after form submit, display message
function userListReceivedWithMessage(type, xml, evt)
{
	if (type == 'error')
	{
		alert(ajaxError);
	}
	else
	{
		setStatusMessage(formSuccess);
		userListReceived(type, xml, evt);
	}
}

// filtered userlist received
function userListReceivedFromFilter(type, xml, evt)
{
	if (type == 'error')
	{	
   		alert(ajaxError);
	}
	else
	{
		dojo.byId('listUsersSpan').innerHTML = xml;
		dojo.byId('filterInput').focus();
	}
}

// full userlist received
function userListReceived(type, xml, evt)
{
	userListReceivedFromFilter(type, xml, evt);
	
	showAddForm();
}


function editUser(editId)
{
	cancelUserCheck = true;
	showLoadingData();
    dojo.io.bind({
                   url: 'getUser.do',
                   handler: formChanged,
                   content: {userId: editId}
                });  
                   
	return false;    
}

function showAddForm()
{
	showLoadingData();
	
    dojo.io.bind({url: 'addUserForm.do',
                  handler: formChanged
                   });  
                   
	return false;    
}		
	
// form changed	
  function formChanged(type, xml, evt)
  {
  	hideLoadingData();
  	
  	if (type == 'error')
  	{
  		alert(ajaxError);
  		return;
  	}
  	else
  	{
  		cancelUserCheck = false;
  		
  		responseReceived(type, xml, evt);
  		
		dojo.byId("filterForm").value = dojo.byId('filterInput').value;
		dojo.byId("inActiveForm").value = dojo.byId('hideInactive').checked;
	}
  }

function initUserAdmin()
{
	dojo.event.connect(dojo.byId('filterInput'), "onkeyup", "filterKeyUp");
	dojo.event.connect(dojo.byId('hideInactive'), "onclick", "filterKeyUp");
}

function filterKeyUp(evt)
{
	var filterInput = dojo.byId('filterInput').value;
	
	dojo.byId("filterForm").value = dojo.byId('filterInput').value;
	dojo.byId("inActiveForm").value = dojo.byId('hideInactive').checked;
	
	dojo.io.bind({url: 'index.do',
				  handler: userListReceivedFromFilter,
                     content: {filterPattern: filterInput,
                     			hideInactive: dojo.byId('hideInactive').checked,
                     			fromForm: '1'}
                     });		
}


dojo.addOnLoad(initUserAdmin);
