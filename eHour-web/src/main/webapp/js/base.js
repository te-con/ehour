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
	document.getElementById('NavLoading').style.visibility = 'visible';
}

function fadeInMessage(msg)
{
	document.getElementById('LoggedInAs').innerHTML = msg + '&nbsp;';
}

function hideLoadingData()
{
	document.getElementById('NavLoading').style.visibility = 'hidden';
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