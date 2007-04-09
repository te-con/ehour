// if true then submit to report, if false update criteria 
var clickIsSubmit = true;

// initialized
function init()
{
	var replacedNode = document.getElementById("dateStartDiv");
	dojo.widget.createWidget("DropdownDatePicker",
											{value:dateStart,
											 disabled: false,
											 name: "dateStart",
											 containerToggle: "fade"
										 }, replacedNode);  



	replacedNode = document.getElementById("dateEndDiv");
	dojo.widget.createWidget("DropdownDatePicker",
											{value:dateEnd,
											 disabled: false,
											 name: "dateEnd",
											 containerToggle: "fade"
										 }, replacedNode);  

	connectEvents();
}

// set the report to create when the criteria form is submitted
// reportName should match AggregateReport.getReportName
function setReportName(reportName)
{
	var form = dojo.byId('criteriaForm');
	form.reportName.value = reportName;
}

//  bind the form and add onchange and onclick events on dropdowns and checkboxes
//  event types are from ReportCriteria constants
function connectEvents()
{
	dojo.event.connect(dojo.byId('customerId'), "onchange", "updateProjects");
	dojo.event.connect(dojo.byId('departmentId'), "onchange", "updateUsers");
	dojo.event.connect(dojo.byId('onlyActiveCustomers'), "onclick", "updateCustomers");
	dojo.event.connect(dojo.byId('onlyActiveProjects'), "onclick", "updateProjects");
	dojo.event.connect(dojo.byId('onlyActiveUsers'), "onclick", "updateUsers");
	dojo.event.connect(dojo.byId('userFilter'), "onclick", "hideDefaultText");
	dojo.event.connect(dojo.byId('userFilter'), "onblur", "showDefaultText");
	dojo.event.connect(dojo.byId('userFilter'), "onkeyup", "updateUsers");	
	
	new dojo.io.FormBind({formNode: dojo.byId('criteriaForm'),
	  					  handler: criteriaSubmitted
						});
}

// hide default text from user filter
function hideDefaultText(evt)
{
	var userFilterInput = dojo.byId('userFilter');
	
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
	var userFilterInput = dojo.byId('userFilter');
	
	if (userFilterInput.value == "" && userFilterInput.value != defaultText)
	{
		userFilterInput.value = defaultText;
		userFilterInput.style.color = '#aaaaaa';
	}
}

// criteria submitted
function criteriaSubmitted(type, xml, evt)
{
	hideLoadingData();
	if (type == 'error')
	{
		alert(ajaxError);
	}
	else
	{
		ajaxEventReceived(xml, true, {report: "reportTarget",
									  criteria: "criteriaTarget",
									  customerList: "criteriaCustomerList",
									  projectList: "criteriaProjectList",
  									  userList: "criteriaUserList"
									  });
									  
		showDefaultText();
	}
}

// update the customers (1=ReportCriteria.UPDATE_CUSTOMERS)
function updateCustomers(evt)
{
	updateCriteria(1);
}

// update the projects (2=ReportCriteria.UPDATE_PROJECTS)
function updateProjects(evt)
{
	updateCriteria(2);
}

// update the users (3=ReportCriteria.UPDATE_USERS)
function updateUsers(evt)
{
	updateCriteria(3);
}


function updateCriteria(updateType)
{
	var criteriaForm = dojo.byId('criteriaForm');
	var userFilterInput = dojo.byId('userFilter');
	
	criteriaForm.updateType.value = updateType;
	
	if (trim(userFilterInput.value) == defaultText)
	{
		userFilterInput.value = "";
	}
	
	
	clickIsSubmit = false;
	
	criteriaForm.action = contextRoot + '/updateCriteria.do';
	
	dojo.byId('criteriaSubmit').click();
}

// update the report, re-using the reportdata
function updateReport(reportName, key, forId)
{
    dojo.io.bind({
                   url: 'showReport.do',
                   handler: criteriaSubmitted,
                   content: {key: key,
                   			 reportName: reportName,
                   			 forId: forId}
                });  		
	
	return false;
}

// Set report
function setReportName(rn)
{
	var form = dojo.byId('criteriaForm');
	form.reportName.value = rn;
}

// Show criteria
function showCriteria()
{
	var criteriaPane = dojo.widget.getWidgetById("criteriaPane");

	if (!criteriaPane.open)
	{
		criteriaPane.onLabelClick();
	}
	
	return false;
}

// change form's action according to event
function changeFormAction()
{
	var criteriaForm = dojo.byId('criteriaForm');
	
	if (clickIsSubmit)
	{
		criteriaForm.action = contextRoot + '/createReport.do';
		var criteriaPane = dojo.widget.getWidgetById("criteriaPane");
		criteriaPane.onLabelClick();
		showLoadingData();
	}
	else
	{
		criteriaForm.action = contextRoot + '/updateCriteria.do';
	}
	
	// reset to default value
	clickIsSubmit = true;

	return true;
}
	
// extend FormBind onSubmit to change the form's action
dojo.lang.extend(dojo.io.FormBind, {onSubmit: function(/*DOMNode*/form)
									{
										return changeFormAction();
									}});
									
dojo.lang.extend(dojo.widget.TitlePane, {onLabelClick: function()
											{
											if (this.open)
											{
												dojo.html.setClass(this.labelNode, 'reportCriteriaLabelFolded');
												dojo.lfx.wipeOut(this.containerNode, 250).play();
												this.open=false;
											} else {
												dojo.html.setClass(this.labelNode, 'reportCriteriaLabel');
												dojo.lfx.wipeIn(this.containerNode, 250).play();
												this.open=true;
											}
										}});