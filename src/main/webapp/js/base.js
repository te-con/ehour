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