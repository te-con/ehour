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