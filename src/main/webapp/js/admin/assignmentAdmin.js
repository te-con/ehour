// Dojo dropdown widget
var formBind;

var adminForm = '';

// validate form 
function validateForm(formId)
{
	formId = 'AssignmentForm';
	
	var validationRules = new Array(new Array("hourlyRate", "hourlyRateError", rateNotValid)
											);
	
	isValid = validateFloat(formId, validationRules);
	
	if (isValid)
	{
		showLoadingData();
	}

	document.getElementById('statusMessage').innerHTML = sendingData;

	return isValid;
}

// delete event, ask for confirm and whistle off
function deleteAssignment(assignmentId)
{
	if (confirm(noDeleteMessage))
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

function showAddForm()
{
	showLoadingData();
	
    dojo.io.bind({
                      url: 'getAssignmentsForUser.do',
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
		dojo.byId('assignmentFormSpan').innerHTML = xml;

		evalScript(xml);

		// DOM changed, rebind
		bindAssignmentForm();
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
				  handler: userListReceivedFromFilter,
                  content: {filterPattern: filterInput,
			      			hideInactive: true,
                     		fromForm: '1'}
		                     });		
}


dojo.addOnLoad(init);
