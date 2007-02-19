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

//  bind the form and add onchange and onclick events on dropdowns and checkboxes
function connectEvents()
{
	dojo.event.connect(dojo.byId('customerId'), "onchange", "updateCriteria");
	dojo.event.connect(dojo.byId('departmentId'), "onchange", "updateCriteria");
	dojo.event.connect(dojo.byId('onlyActiveCustomers'), "onclick", "updateCriteria");
	dojo.event.connect(dojo.byId('onlyActiveProjects'), "onclick", "updateCriteria");
	dojo.event.connect(dojo.byId('onlyActiveUsers'), "onclick", "updateCriteria");
	dojo.event.connect(dojo.byId('userFilter'), "onclick", "hideDefaultText");
	dojo.event.connect(dojo.byId('userFilter'), "onblur", "showDefaultText");
	dojo.event.connect(dojo.byId('userFilter'), "onkeyup", "updateCriteriaFromFilter");	
	
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
	if (type == 'error')
	{
		alert(ajaxError);
	}
	else
	{
		ajaxEventReceived(xml, true, {report: "reportTarget",
									  criteria: "criteriaTarget"});
	}
}

// update the criteria
function updateCriteria(evt)
{
	var criteriaForm = dojo.byId('criteriaForm');
	var userFilterInput = dojo.byId('userFilter');
	
	if (userFilterInput.value == defaultText)
	{
		userFilterInput.value = "";
	}
	
	criteriaForm.action = contextRoot + '/updateCriteria.do';
	
	dojo.byId('criteriaSubmit').click();

}
