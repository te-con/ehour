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
	dojo.event.connect(dojo.byId('onlyActiveCustomers'), "onclick", "updateCriteria");
	dojo.event.connect(dojo.byId('onlyActiveProjects'), "onclick", "updateCriteria");
	
	new dojo.io.FormBind({formNode: dojo.byId('criteriaForm'),
	  					  handler: criteriaSubmitted
						});
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
	
	criteriaForm.action = contextRoot + '/updateCriteria.do';
	
	dojo.byId('criteriaSubmit').click();

}
