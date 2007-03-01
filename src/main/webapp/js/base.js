dojo.require("dojo.event.*");
dojo.require("dojo.lfx.*");

var fadeTimer = null;

// evaluate any script in the loaded html snippet
function evalScript(html)
{	
	var scripts = new Array();
	
	var re_scripts = /<script([^>]*?)>([\w\W]*?)<\/script>/g;
	
	html.replace(re_scripts,
			 		function(match, attributes, script)
			 		{
						scripts.push(script);
					}
				);	
							
	self.eval(scripts.join('\n'));
}

// ajax event received - parse out the logging form
function ajaxEventReceived(html, postParseJS, spanTargets)
{
	var spanTarget;

	if (!postParseJS)
	{
		evalScript(html);
	}
	
	spanTarget = parseSpanTarget(html);
	
	if (spanTarget == 'loginForm')
	{
		postParseJS = false;
		location.href = contextRoot;
	}
	else
	{
		document.getElementById(spanTargets[spanTarget]).innerHTML = html;
	}
	
	if (postParseJS)
	{
		evalScript(html);
	}
}

// parse the span target out of the html (<!-- spanTarget: xxx -->)
function parseSpanTarget(html)
{
	var spanTarget;

	var regexp = /<\!-- spanTarget: *([\w\W]*?) -->/g;
	
	
	html.replace(regexp,
			 		function(match, attributes, script)
			 		{
						spanTarget = attributes;
					}
				);	
	
	return spanTarget;
}


// help returned from server
function helpChanged(type, xml, evt)
{
	if (type == 'error')
 	{
 		alert(ajaxError);
 	}
 	else
 	{
 		ajaxEventReceived(xml, true, {HelpTarget: 'HelpTarget'}); 	
	}
}

// had a nice fade but pages load too fast so it's annoying... :D
function showLoadingData()
{
	// set it to full
//	dojo.html.setOpacity(dojo.byId('LoggedInAs'), 0);
	//dojo.lfx.html.fadeOut('LoggedInAs', 50).play();
	
//	if (fadeTimer != null)
//	{
//		fadeTimer = null;
//	}
	
//	fadeTimer = setTimeout("fadeInMessage(loadingMsg)", 55);
	document.getElementById('LoggedInAs').innerHTML = loadingMsg + '&nbsp;';
}

function fadeInMessage(msg)
{
	document.getElementById('LoggedInAs').innerHTML = msg + '&nbsp;';
//	dojo.lfx.html.fadeIn('LoggedInAs', 50).play();
}

function hideLoadingData()
{
	document.getElementById('LoggedInAs').innerHTML = loginAs + '&nbsp;';
//	dojo.lfx.html.fadeOut('LoggedInAs', 50).play();

	//if (fadeTimer != null)
//	{
	//	fadeTimer = null;
//	}
	
	//fadeTimer = setTimeout("fadeInMessage(loginAs)", 55);
}

	

function ltrim(str) { 
	for(var k = 0; k < str.length && isWhitespace(str.charAt(k)); k++);
	return str.substring(k, str.length);
}
function rtrim(str) {
	for(var j=str.length-1; j>=0 && isWhitespace(str.charAt(j)) ; j--) ;
	return str.substring(0,j+1);
}
function trim(str) {
	return ltrim(rtrim(str));
}
function isWhitespace(charToCheck) {
	var whitespaceChars = " \t\n\r\f";
	return (whitespaceChars.indexOf(charToCheck) != -1);
}