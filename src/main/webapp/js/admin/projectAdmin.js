// onChange is fired when the user switches from add to edit project, cancel if necc.
var	cancelProjectCheck = false;
var formBind;
var timer;

var adminForm = 'ProjectForm';
var adminFormSpan = 'projectFormSpan';
var adminFormUrl = 'addProjectForm.do';
var adminListReceivedSpan = 'listProjectsSpan';

// validate form
function validateForm(formId)
{
	formId = 'ProjectForm';
	form = document.getElementById(formId);
	
	var validationRules = new Array(new Array("name", "projectNameError", nameRequired),
									new Array("projectCode", "projectCodeError", codeRequired)
												);
	
	isValid = validateRequired(formId, validationRules);
	
	if (isValid)
	{
		showLoadingData();
	}

	return isValid;
}


// received after form submit, display message
function projectListWithMessage(type, xml, evt)
{
	if (type == 'error')
	{
		alert(ajaxError);
	}
	else
	{
		setStatusMessage(formSuccess);
//		ajaxEventReceived(xml, true, {list: "list"});
	}
}




function editProject(editId)
{
	cancelProjectCheck = true;
	showLoadingData();
	
    dojo.io.bind({
                   url: 'getProject.do',
                   handler: formChanged,
                   content: {projectId: editId}
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
  		cancelProjectCheck = false;
  		
		responseReceived(type, xml, evt);
	
		dojo.byId("inActiveForm").value = dojo.byId('hideInactive').checked;
		
		// DOM changed, rebind
		bindAdminForm();
	}
  }

function initFilter()
{
	dojo.event.connect(dojo.byId('hideInactive'), "onclick", "filterKeyUp");
}

function filterKeyUp(evt)
{
	dojo.byId("inActiveForm").value = dojo.byId('hideInactive').checked;
	
	dojo.io.bind({url: 'index.do',
				  handler: responseReceived,
                  content: {hideInactive: dojo.byId('hideInactive').checked,
                     		fromForm: '1'}
                     });		
}


dojo.addOnLoad(initFilter);
