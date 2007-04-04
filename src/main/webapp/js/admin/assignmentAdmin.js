// Dojo dropdown widget
var formBind;

var adminForm = '';
var adminFormSpan = 'assignmentFormSpan';
var adminFormUrl = 'getAssignmentsForUser.do';
var adminListReceivedSpan = 'listUsersSpan';

// validate form 
function validateForm(formId)
{
	formId = 'AssignmentForm';

	var isValid = true;
	
	var asgTypeId = dojo.byId('assignmentTypeId').value;
	
	// default
	if (asgTypeId != 1)
	{
		// date range
		if (asgTypeId == 0)
		{
			var validationRules = new Array(new Array("hourlyRate", "hourlyRateError", notAFloat));
			isValid = validateFloat(formId, validationRules);
		}
		// time alloted fixed
		else if (asgTypeId == 2)
		{
			var validationRules = new Array(new Array("hourlyRate", "hourlyRateError", notAFloat),
											new Array("allottedHours", "allottedHoursError", notAFloat));
											
			isValid = validateFloat(formId, validationRules);											
			
			validationRules = new Array(new Array("allottedHours", "allottedHoursError", required));
			isValid = isValid && validateRequired(formId, validationRules);
		}
		// time alloted flex
		else if (asgTypeId == 3)
		{
			var validationRules = new Array(new Array("hourlyRate", "hourlyRateError", notAFloat),
											new Array("allottedHours", "allottedHoursError", notAFloat),
											new Array("allowedOverrun", "allowedOverrunError", notAFloat)											
													);

			isValid = validateFloat(formId, validationRules);											
			
			validationRules = new Array(new Array("allottedHours", "allottedHoursError", required),
										new Array("allowedOverrun", "allowedOverrunError", required));
			isValid = isValid && validateRequired(formId, validationRules);
		}		
	}
		
	if (isValid)
	{
		showLoadingData();
	}

	return isValid;
}

// delete event, ask for confirm and whistle off
function deleteAssignment(assignmentId)
{
	if (confirm(deleteConfirm))
	{
		showLoadingData();	
	    dojo.io.bind({
	                   url: 'deleteAssignment.do',
	                   handler: formChanged,
	                   content: {assignmentId: assignmentId}
	                });  		
	}
	
	return false;
}

// bind AssignmentForm to validation and ajax submit
function bindAssignmentForm()
{
	new dojo.io.FormBind({	formNode: dojo.byId('AssignmentForm'),
  							handler: formChanged
							});
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

// show assignments list + new form
function editUser(editId)
{
	showLoadingData();
    dojo.io.bind({
                   url: 'getAssignmentsForUser.do',
                   handler: formChanged,
                   content: {userId: editId}
                });  
                   
	return false;    
}

function editAssignment(editId, assignmentId)
{
	showLoadingData();	
    dojo.io.bind({
                   url: 'getAssignment.do',
                   handler: formChanged,
                   content: {userId: editId,
                   			 assignmentId: assignmentId
                   }
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
		dojo.byId('assignmentFormSpan').innerHTML = xml;

		evalScript(xml);

		// DOM changed, rebind
		initForm();
	}
}

// initialize form after new load
function initForm()
{
	bindAssignmentForm();

	dojo.event.connect(dojo.byId('assignmentTypeId'), "onchange", "hideRows");
	dojo.event.connect(dojo.byId('projectSelector'), "onchange", "fetchProject");
	
	hideRows('');
	
	fetchProject('');
}

// hide rows based on assignment type
function hideRows(evt)
{
	var asgTypeId = dojo.byId('assignmentTypeId').value;
	
	dojo.byId('allottedTr').style.display = (asgTypeId == 2 || asgTypeId == 3) ? "" : "none";
	dojo.byId('notifyPmTr').style.display = (asgTypeId == 2 || asgTypeId == 3) ? "" : "none";	
	dojo.byId('allowedOverrunTr').style.display = (asgTypeId == 3) ? "" : "none";	
	
	dojo.byId('dateStartTr').style.display = (asgTypeId == 1) ? "none" : "";
	dojo.byId('dateEndTr').style.display = (asgTypeId == 1) ? "none" : "";	
}	

// fetch project
function fetchProject(evt)
{
	showLoadingData();	
	
	var projectId = dojo.byId('projectSelector').value;
	
    dojo.io.bind({
                   url: 'getProject.do',
                   handler: projectFetched,
                   content: {projectId: projectId}
                });  		
}

// project fetched from server
function projectFetched(type, xml, evt)
{
  	hideLoadingData();
  	
	if (type == 'error')
	{	
   		alert(ajaxError);
	}
	else
	{
		// JSON response
		var project = eval('(' + xml + ')');	
		
		if (project.pm == '')
		{
			dojo.byId('notifyPm').disabled = true;
			dojo.byId('notifyPm').checked = false;
			dojo.byId('notifyPmMsg').innerHTML = cantNotify;
		}
		else
		{
			dojo.byId('notifyPm').disabled = false;
			dojo.byId('notifyPmMsg').innerHTML = '&nbsp;';
		}

	}
}

// connect all events
function init()
{
	dojo.event.connect(dojo.byId('filterInput'), "onkeyup", "filterKeyUp");
	dojo.event.connect(dojo.byId('filterInput'), "onclick", "hideDefaultText");
	dojo.event.connect(dojo.byId('filterInput'), "onblur", "showDefaultText");
}

// hide default text from user filter
function hideDefaultText(evt)
{
	var userFilterInput = dojo.byId('filterInput');
	
	if (userFilterInput.value == defaultText)
	{
		userFilterInput.value = '';
		userFilterInput.style.color = '#233e55';
		userFilterInput.focus();
	}
}

// show default text in user filter if value is empty
function showDefaultText(evt)
{
	var userFilterInput = dojo.byId('filterInput');
	
	if (userFilterInput.value == "" && userFilterInput.value != defaultText)
	{
		userFilterInput.value = defaultText;
		userFilterInput.style.color = '#aaaaaa';
	}
}


function filterKeyUp(evt)
{
	var filterInput = dojo.byId('filterInput').value;
	
	dojo.io.bind({url: 'index.do',
				  handler: userListReceivedFromFilter,
                  content: {filterPattern: filterInput,
			      			hideInactive: true,
                     		fromForm: '1'}
		                     });		
}


dojo.addOnLoad(init);
