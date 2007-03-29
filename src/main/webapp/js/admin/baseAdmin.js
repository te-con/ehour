// dojo stuff
dojo.require("dojo.io.*");
dojo.require("dojo.event.*");
dojo.require("dojo.lfx.*");

// bindAdminForm
function bindAdminForm()
{ 
	if (adminForm != "")
	{
		new dojo.io.FormBind({
	   						formNode: dojo.byId(adminForm),
	   						handler: responseReceived
							});
	}
}

// handler for bindAdminForm
// TODO merge this with ajaxEventReceived
function responseReceived(type, xml, evt)
{
	hideLoadingData();
	
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
			evalScript(xml);
		}
		else
		{
			listReceived(xml);
		}		
	}
}

// change the form span
function changeForm(htmlSnippet)
{
	dojo.byId(adminFormSpan).innerHTML = htmlSnippet;
	
	// rebind
	bindAdminForm();
}

// list received
function listReceived(htmlSnippet)
{
	dojo.byId(adminListReceivedSpan).innerHTML = htmlSnippet;
		
	showAddForm();
}

// show add form
function showAddForm()
{
	showLoadingData();
	
	    dojo.io.bind({
	                   url: adminFormUrl,
	                   handler: responseReceived
	                });  
                   
	return false;    
}


// display and trigger fade in status message
function setStatusMessage(statusMsg)
{
	document.getElementById('statusMessage').innerHTML = statusMsg;
	dojo.html.setOpacity(dojo.byId('statusMessage'), 1);
	
	setTimeout("dojo.lfx.html.fadeOut('statusMessage', 800).play()", 1000);
}

// extend FormBind to add the validation call
dojo.lang.extend(dojo.io.FormBind, {onSubmit: function(/*DOMNode*/form)
									{
										return validateForm();
									}});


dojo.addOnLoad(bindAdminForm);

