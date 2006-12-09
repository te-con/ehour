// onChange is fired when the user switches from add to edit project, cancel if necc.
var	cancelProjectCheck = false;
var formBind;
var timer;

// extend FormBind to add the validation call
dojo.lang.extend(dojo.io.FormBind, {
	onSubmit: function(/*DOMNode*/form)
	{
		return validateForm('ProjectForm');
	}});

// display and trigger fade in status message
function setStatusMessage(statusMsg)
{
	document.getElementById('statusMessage').innerHTML = statusMsg;
	dojo.html.setOpacity(dojo.byId('statusMessage'), 1);
	
	setTimeout("dojo.lfx.html.fadeOut('statusMessage', 800).play()", 1000);
}



// validate form
function validateForm(formId)
{
	form = document.getElementById(formId);
	
	var validationRules = new Array(new Array("name", "projectNameError", nameRequired),
									new Array("projectCode", "projectCodeError", codeRequired)
												);
	
	isValid = validateRequired(formId, validationRules);
	
	if (isValid)
	{
		document.getElementById('statusMessage').innerHTML = sendingData;
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
	
    dojo.io.bind({
                   url: 'getProject.do',
                   handler: formChanged,
                   content: {projectId: editId}
                });  
                   
	return false;    
}

function showAddForm()
{
       dojo.io.bind({
                      url: 'addProjectForm.do',
                      handler: formChanged
                   });  
                   
	return false;    
}		
	
// form changed	
  function formChanged(type, xml, evt)
  {
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
