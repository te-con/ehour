// onChange is fired when the user switches from add to edit user, cancel if necc.
var	cancelUserCheck = false;
var formBind;

function validateForm(formId)
{
	isValid = validateRequired(formId, new Array(new Array("username", "userNameError", usernameRequired),
												 new Array("password", "passwordError", passwordRequired),
												 new Array("lastName", "lastNameError", lastNameRequired),
												 new Array("roles", "userRoleError", userRoleRequired)		 												 
												));

	isValid = isValid && validateEmail(formId, new Array(new Array("email", "emailError", emailNotValid)
													));
	
	form = document.getElementById(formId);
	
	if (form.password.value != form.confirmPassword.value)
	{	
		document.getElementById('confirmPasswordError').innerHTML = noPasswordMatch;
		isValid = false;
	}
	else
	{
		document.getElementById('confirmPasswordError').innerHTML = "";
		isValid = isValid && true;
	}
	
	// bit crappy but can't cancel an existing formbind
	if (isValid)
	{
		new dojo.io.FormBind({	formNode: dojo.byId('UserForm'),
   								handler: userListReceived
								});
		form.submit();
	}
	else
	{
		return false;
	}
}

// check if user exists
function checkUserExists(username)
{
	if (!cancelUserCheck && username != "")
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
	
    dojo.io.bind({
                   url: 'getUser.do',
                   handler: formChanged,
                   content: {userId: editId}
                });  
                   
	return false;    
}

function showAddForm()
{
       dojo.io.bind({
                      url: 'addUserForm.do',
                      handler: formChanged
                   });  
                   
	return false;    
}		
	
  function formChanged(type, xml, evt)
  {
  	if (type == 'error')
  	{
  		alert(ajaxError);
  		return;
  	}
  	else
  	{
  		cancelUserCheck = false;
  		
		dojo.byId('userFormSpan').innerHTML = xml;
	
		dojo.byId("filterForm").value = dojo.byId('filterInput').value;
		dojo.byId("inActiveForm").value = dojo.byId('hideInactive').checked;
	}
  }

function init()
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


dojo.addOnLoad(init);
