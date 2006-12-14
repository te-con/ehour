// dojo stuff
dojo.require("dojo.io.*");
dojo.require("dojo.event.*");
dojo.require("dojo.lfx.*");

// display and trigger fade in status message
function setStatusMessage(statusMsg)
{
	document.getElementById('statusMessage').innerHTML = statusMsg;
	dojo.html.setOpacity(dojo.byId('statusMessage'), 1);
	
	setTimeout("dojo.lfx.html.fadeOut('statusMessage', 800).play()", 1000);
}

function showLoadingData()
{
	dojo.html.setOpacity(dojo.byId('statusMessage'), 100);
	document.getElementById('statusMessage').innerHTML = loadingMsg;
	dojo.lfx.html.fadeIn('statusMessage', 800).play();
}

function hideLoadingData()
{
	dojo.lfx.html.fadeOut('statusMessage', 800).play();
}

// extend FormBind to add the validation call
dojo.lang.extend(dojo.io.FormBind, {onSubmit: function(/*DOMNode*/form)
									{
										return validateForm();
									}});



