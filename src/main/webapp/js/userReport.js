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
}

// handler for bindCriteriaForm
function reportReceived(type, xml, evt)
{
	if (type == 'error')
	{	
   		alert(ajaxError);
	}
	else
	{
		ajaxEventReceived(xml, true, {reportContent: "reportContent"});
	}
}