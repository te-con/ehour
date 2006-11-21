/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.xml.XslTransform");

dojo.xml.XslTransform = function(/*String*/xsltUri){
	//	summary:
	//		dojo.xml.XslTransform is a convenience object that takes the URI
	//		String of an XSL file as a constructor argument. After each
	//		transformation all parameters will be cleared. Transformation is
	//		supported by IE, Mozilla, and partially by Opera. Other browsers
	//		(notably Safari) have not yet exposed their transformation
	//		primtives well enough to be useful.
	// xsltUri:
	//		Url of the xslt document to transform nodes with. Transformation is
	//		acheived with the transformTo* methods on instances of this class.

	dojo.debug("XslTransform is supported by Internet Explorer and Mozilla, with limited support in Opera 9 (no document function support).");
	var IS_IE = dojo.render.html.ie;
	var ACTIVEX_DOMS = [
		"Msxml2.DOMDocument.5.0", 
		"Msxml2.DOMDocument.4.0", 
		"Msxml2.DOMDocument.3.0", 
		"MSXML2.DOMDocument", 
		"MSXML.DOMDocument", 
		"Microsoft.XMLDOM"
	];
	var ACTIVEX_FT_DOMS = [
		"Msxml2.FreeThreadedDOMDocument.5.0", 
		"MSXML2.FreeThreadedDOMDocument.4.0", 
		"MSXML2.FreeThreadedDOMDocument.3.0"
	];
	var ACTIVEX_TEMPLATES = [
		"Msxml2.XSLTemplate.5.0", 
		"Msxml2.XSLTemplate.4.0", 
		"MSXML2.XSLTemplate.3.0"
	];
  
	function getActiveXImpl(activeXArray){
		for(var i=0; i < activeXArray.length; i++){
			try{
				var testObj = new ActiveXObject(activeXArray[i]);
				if(testObj){
					return activeXArray[i];
				}
			}catch(e){}
		}
		dojo.raise("Could not find an ActiveX implementation in:\n\n " + activeXArray);
	}
    
    if(xsltUri == null || xsltUri == undefined){
        dojo.raise("You must pass the URI String for the XSL file to be used!");
        return false;
    }
    
	var xsltDocument = null;
	var xsltProcessor = null;
	if(IS_IE){
		xsltDocument = new ActiveXObject(getActiveXImpl(ACTIVEX_FT_DOMS));
		xsltDocument.async = false;
	}else{
		xsltProcessor = new XSLTProcessor();
		xsltDocument = document.implementation.createDocument("", "", null);
		xsltDocument.addEventListener("load", onXslLoad, false);
	}
	xsltDocument.load(xsltUri);

	if(IS_IE){
		var xslt = new ActiveXObject(getActiveXImpl(ACTIVEX_TEMPLATES));
		xslt.stylesheet = xsltDocument;  
		xsltProcessor = xslt.createProcessor();
	}

	function onXslLoad(){
		xsltProcessor.importStylesheet(xsltDocument); 
	}

	function getResultDom(xmlDoc, params){
		if(IS_IE){
			addIeParams(params);
			var result = getIeResultDom(xmlDoc);
			removeIeParams(params);   
			return result;
		}else{
			return getMozillaResultDom(xmlDoc, params);
		}
	}

	function addIeParams(params){
		if(!params){ return; }
		for(var i=0; i<params.length; i++){
			xsltProcessor.addParameter(params[i][0], params[i][1]);
		}
	}

    function removeIeParams(params){
		if(!params){ return; }
		for(var i=0; i<params.length; i++){
			xsltProcessor.addParameter(params[i][0], "");
		}
    }

	function getIeResultDom(xmlDoc){
		xsltProcessor.input = xmlDoc;
		var outDoc = new ActiveXObject(getActiveXImpl(ACTIVEX_DOMS));
		outDoc.async = false;  
		outDoc.validateOnParse = false;
		xsltProcessor.output = outDoc;
		xsltProcessor.transform();
		if(outDoc.parseError.errorCode != 0){
			var err = outDoc.parseError;
			dojo.raise("err.errorCode: " + err.errorCode + "\n\nerr.reason: " + err.reason + "\n\nerr.url: " + err.url + "\n\nerr.srcText: " + err.srcText);
		}
		return outDoc;
	}

	function getIeResultStr(xmlDoc, params){
		xsltProcessor.input = xmlDoc;
		xsltProcessor.transform();    
		return xsltProcessor.output;
	}

	function addMozillaParams(params){
		if(!params){ return; }
		for(var i=0; i<params.length; i++){
			xsltProcessor.setParameter(null, params[i][0], params[i][1]);
		}
	}

	function getMozillaResultDom(xmlDoc, params){
		addMozillaParams(params);
		var resultDoc = xsltProcessor.transformToDocument(xmlDoc);
		xsltProcessor.clearParameters();
		return resultDoc;
	}

	function getMozillaResultStr(xmlDoc, params, parentDoc){
		addMozillaParams(params);
		var resultDoc = xsltProcessor.transformToFragment(xmlDoc, parentDoc);
		var serializer = new XMLSerializer();
		xsltProcessor.clearParameters();
		return serializer.serializeToString(resultDoc);
	}

	this.getResultString = function(	/*XMLDocument*/xmlDoc, 
										/*2 Dimensional Array*/params, 
										/*HTMLDocument*/parentDoc){
		// summary:
		//		transform the xmlDoc and return the result as a string.
		// xmlDoc: an XML Document to transform
		// params:
		//		a set of configuration parameters to pass to the transformation
		//		engine.
		// parentDoc: The HTML docuemnt to transform the subdocument "under"
		var content = null;
		if(IS_IE){
			addIeParams(params);
			content = getIeResultStr(xmlDoc, params);
			removeIeParams(params);  
		}else{
			content = getMozillaResultStr(xmlDoc, params, parentDoc);
		} 
		//dojo.debug(content);
		return content;
	};

	this.transformToContentPane = function(	/*XMLDocument*/xmlDoc,
											/*2 Dimensional Array*/params, 
											/*ContentPane*/contentPane, 
											/*HTMLDocument*/parentDoc){
		// summary:
		//		transform the xmlDoc and put the result into the passed
		//		ContentPane instance
		// xmlDoc: an XML Document to transform
		// params:
		//		a set of configuration parameters to pass to the transformation
		//		engine.
		// contentPane:
		//		instance of dojo.widget.ContentPane to assign the transform
		//		results to
		// parentDoc: The HTML docuemnt to transform the subdocument "under"

		// FIXME: do we need this function?
		var content = this.getResultString(xmlDoc, params, parentDoc);
		contentPane.setContent(content);
	};

	this.transformToRegion = function(	/*XMLDocument*/xmlDoc, 
										/*2 Dimensional Array*/params, 
										/*HTMLElement*/region, 
										/*HTMLDocument*/parentDoc){
		// summary:
		//		transform the xmlDoc and put the result into the passed
		//		DomNode using innerHTML
		// xmlDoc: an XML Document to transform
		// params:
		//		a set of configuration parameters to pass to the transformation
		//		engine.
		// region: node to put transform results in
		// parentDoc: The HTML docuemnt to transform the subdocument "under"
		try{
			var content = this.getResultString(xmlDoc, params, parentDoc);
			region.innerHTML = content;
		}catch (e){
			dojo.raise(e.message + "\n\n xsltUri: " + xsltUri)
		}
	};

	this.transformToDocument = function(	/*XMLDocument*/ xmlDoc, 
											/*2 Dimensional Array*/params){
		// summary:
		//		transform the xmlDoc and return a new XML document containing
		//		the result
		// xmlDoc: an XML Document to transform
		// params:
		//		a set of configuration parameters to pass to the transformation
		//		engine.
		return getResultDom(xmlDoc, params); // XMLDocument
	}

	this.transformToWindow = function(	/*XMLDocument*/ xmlDoc,
										/*2 Dimensional Array*/params, 
										/*HTMLDocument*/windowDoc, 
										/*HTMLDocument*/parentDoc){
		// summary:
		//		transform the xmlDoc and put the contents in the passed
		//		windowDoc, blowing away any previous contents.
		// xmlDoc: an XML Document to transform
		// params:
		//		a set of configuration parameters to pass to the transformation
		//		engine.
		// windowDoc: the HTMLDocument to assign the contents to
		// parentDoc: The HTML docuemnt to transform the subdocument "under"
		try{
			windowDoc.open();
			windowDoc.write(this.getResultString(xmlDoc, params, parentDoc));
			windowDoc.close();
		}catch(e){
			dojo.raise(e.message + "\n\n xsltUri: " + xsltUri)
		}
	};
};
