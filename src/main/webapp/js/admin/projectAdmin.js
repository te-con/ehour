// onChange is fired when the user switches from add to edit project, cancel if necc.
var	cancelProjectCheck = false;
var formBind;
var timer;

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

// bind ProjectForm to validation and ajax submit
function bindProjectForm()
{
	new dojo.io.FormBind({	formNode: dojo.byId('ProjectForm'),
  								handler: projectListWithMessage
							});
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
		projectListReceived(type, xml, evt);
	}
}


// projectlist received
function projectListReceived(type, xml, evt)
{
	if (type == 'error')
	{	
   		alert(ajaxError);
	}
	else
	{
		dojo.byId('listProjectsSpan').innerHTML = xml;
	}

	showAddForm();
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

function showAddForm()
{
	showLoadingData();
       dojo.io.bind({
                      url: 'addProjectForm.do',
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
  		cancelProjectCheck = false;
  		
		dojo.byId('projectFormSpan').innerHTML = xml;
	
		dojo.byId("inActiveForm").value = dojo.byId('hideInactive').checked;
		
		// DOM changed, rebind
		bindProjectForm();
	}
  }

function init()
{
	dojo.event.connect(dojo.byId('hideInactive'), "onclick", "filterKeyUp");
	
	bindProjectForm();
}

function filterKeyUp(evt)
{
	dojo.byId("inActiveForm").value = dojo.byId('hideInactive').checked;
	
	dojo.io.bind({url: 'index.do',
				  handler: projectListReceived,
                  content: {hideInactive: dojo.byId('hideInactive').checked,
                     		fromForm: '1'}
                     });		
}


dojo.addOnLoad(init);
