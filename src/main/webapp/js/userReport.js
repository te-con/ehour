// dojo stuff
dojo.require("dojo.io.*");
dojo.require("dojo.event.*");
dojo.require("dojo.lfx.*");

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
		var spanTarget = parseSpanTarget(xml);

		if (spanTarget == 'loginForm')
		{
			location.href = contextRoot;
		} else if (spanTarget == 'form')
		{
			changeForm(xml);
		}
		else
		{
			listReceived(xml);
		}		
	}
}