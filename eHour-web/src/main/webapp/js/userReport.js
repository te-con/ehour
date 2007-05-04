// dojo stuff
dojo.require("dojo.io.*");
dojo.require("dojo.event.*");

// bindCriteriaForm
function bindCriteriaForm()
{ 
	new dojo.io.FormBind({
   						formNode: dojo.byId("criteriaForm"),
   						handler: reportReceived
						});
						
	dojo.event.connect(dojo.byId('infiniteStartDateId'), "onclick", "toggleStartDate");
	dojo.event.connect(dojo.byId('infiniteEndDateId'), "onclick", "toggleEndDate");	
	
	toggleStartDate(null);
	toggleEndDate(null);
}

// toggle start date
function toggleStartDate(evt)
{
	var infiniteStartDate = dojo.byId('infiniteStartDateId');
	
	if (infiniteStartDate.checked)
	{
		dojo.byId('dateStartId').style.display='none';
	}
	else
	{
		dojo.byId('dateStartId').style.display='';
	}
}

// toggle end date
function toggleEndDate(evt)
{
	var infiniteEndDate = dojo.byId('infiniteEndDateId');
	
	if (infiniteEndDate.checked)
	{
		dojo.byId('dateEndId').style.display='none';
	}
	else
	{
		dojo.byId('dateEndId').style.display='';
	}
}

// handler for bindCriteriaForm
function reportReceived(type, xml, evt)
{
	hideLoadingData();
	
	if (type == 'error')
	{	
   		alert(ajaxError);
	}
	else
	{
		ajaxEventReceived(xml, true, {reportContent: "reportContent"});
	}
}

// extend FormBind to add the validation call
dojo.lang.extend(dojo.io.FormBind, {onSubmit: function(/*DOMNode*/form)
									{
										showLoadingData();
										return true;
									}})