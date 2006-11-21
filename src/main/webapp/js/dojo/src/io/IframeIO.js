/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.io.IframeIO");
dojo.require("dojo.io.BrowserIO");
dojo.require("dojo.uri.*");

// FIXME: is it possible to use the Google htmlfile hack to prevent the
// background click with this transport?

dojo.io.createIFrame = function(fname, onloadstr, uri){
	if(window[fname]){ return window[fname]; }
	if(window.frames[fname]){ return window.frames[fname]; }
	var r = dojo.render.html;
	var cframe = null;
	var turi = uri||dojo.uri.dojoUri("iframe_history.html?noInit=true");
	var ifrstr = ((r.ie)&&(dojo.render.os.win)) ? '<iframe name="'+fname+'" src="'+turi+'" onload="'+onloadstr+'">' : 'iframe';
	cframe = document.createElement(ifrstr);
	with(cframe){
		name = fname;
		setAttribute("name", fname);
		id = fname;
	}
	dojo.body().appendChild(cframe);
	window[fname] = cframe;

	with(cframe.style){
		if(!r.safari){
			//We can't change the src in Safari 2.0.3 if absolute position. Bizarro.
			position = "absolute";
		}
		left = top = "0px";
		height = width = "1px";
		visibility = "hidden";
		/*
		if(djConfig.isDebug){
			position = "relative";
			height = "300px";
			width = "600px";
			visibility = "visible";
		}
		*/
	}

	if(!r.ie){
		dojo.io.setIFrameSrc(cframe, turi, true);
		cframe.onload = new Function(onloadstr);
	}
	
	return cframe;
}

dojo.io.IframeTransport = new function(){
	var _this = this;
	this.currentRequest = null;
	this.requestQueue = [];
	this.iframeName = "dojoIoIframe";

	this.fireNextRequest = function(){
		try{
			if((this.currentRequest)||(this.requestQueue.length == 0)){ return; }
			// dojo.debug("fireNextRequest");
			var cr = this.currentRequest = this.requestQueue.shift();
			cr._contentToClean = [];
			var fn = cr["formNode"];
			var content = cr["content"] || {};
			if(cr.sendTransport) {
				content["dojo.transport"] = "iframe";
			}
			if(fn){
				if(content){
					// if we have things in content, we need to add them to the form
					// before submission
					for(var x in content){
						if(!fn[x]){
							var tn;
							if(dojo.render.html.ie){
								tn = document.createElement("<input type='hidden' name='"+x+"' value='"+content[x]+"'>");
								fn.appendChild(tn);
							}else{
								tn = document.createElement("input");
								fn.appendChild(tn);
								tn.type = "hidden";
								tn.name = x;
								tn.value = content[x];
							}
							cr._contentToClean.push(x);
						}else{
							fn[x].value = content[x];
						}
					}
				}
				if(cr["url"]){
					cr._originalAction = fn.getAttribute("action");
					fn.setAttribute("action", cr.url);
				}
				if(!fn.getAttribute("method")){
					fn.setAttribute("method", (cr["method"]) ? cr["method"] : "post");
				}
				cr._originalTarget = fn.getAttribute("target");
				fn.setAttribute("target", this.iframeName);
				fn.target = this.iframeName;
				fn.submit();
			}else{
				// otherwise we post a GET string by changing URL location for the
				// iframe
				var query = dojo.io.argsFromMap(this.currentRequest.content);
				var tmpUrl = cr.url + (cr.url.indexOf("?") > -1 ? "&" : "?") + query;
				dojo.io.setIFrameSrc(this.iframe, tmpUrl, true);
			}
		}catch(e){
			this.iframeOnload(e);
		}
	}

	this.canHandle = function(kwArgs){
		return (
			(
				dojo.lang.inArray([	"text/plain", "text/html", "text/javascript", "text/json", "application/json"], kwArgs["mimetype"])
			)&&(
				dojo.lang.inArray(["post", "get"], kwArgs["method"].toLowerCase())
			)&&(
				// never handle a sync request
				!  ((kwArgs["sync"])&&(kwArgs["sync"] == true))
			)
		);
	}

	this.bind = function(kwArgs){
		if(!this["iframe"]){ this.setUpIframe(); }
		this.requestQueue.push(kwArgs);
		this.fireNextRequest();
		return;
	}

	this.setUpIframe = function(){

		// NOTE: IE 5.0 and earlier Mozilla's don't support an onload event for
		//       iframes. OTOH, we don't care.
		this.iframe = dojo.io.createIFrame(this.iframeName, "dojo.io.IframeTransport.iframeOnload();");
	}

	this.iframeOnload = function(errorObject /* Object */){
		if(!_this.currentRequest){
			_this.fireNextRequest();
			return;
		}

		var req = _this.currentRequest;

		if(req.formNode){
			// remove all the hidden content inputs
			var toClean = req._contentToClean;
			for(var i = 0; i < toClean.length; i++) {
				var key = toClean[i];
				if(dojo.render.html.safari){
					//In Safari (at least 2.0.3), can't use formNode[key] syntax to find the node,
					//for nodes that were dynamically added.
					var fNode = req.formNode;
					for(var j = 0; j < fNode.childNodes.length; j++){
						var chNode = fNode.childNodes[j];
						if(chNode.name == key){
							var pNode = chNode.parentNode;
							pNode.removeChild(chNode);
							break;
						}
					}
				}else{
					var input = req.formNode[key];
					req.formNode.removeChild(input);
					req.formNode[key] = null;
				}
			}
	
			// restore original action + target
			if(req["_originalAction"]){
				req.formNode.setAttribute("action", req._originalAction);
			}
			if(req["_originalTarget"]){
				req.formNode.setAttribute("target", req._originalTarget);
				req.formNode.target = req._originalTarget;
			}
		}

		var contentDoc = function(iframe_el){
			var doc = iframe_el.contentDocument || // W3
				(
					(iframe_el.contentWindow)&&(iframe_el.contentWindow.document)
				) ||  // IE
				(
					(iframe_el.name)&&(document.frames[iframe_el.name])&&
					(document.frames[iframe_el.name].document)
				) || null;
			return doc;
		};

		var value;
		var success = false;

		if (errorObject){
				this._callError(req, "IframeTransport Request Error: " + errorObject);
		}else{
			var ifd = contentDoc(_this.iframe);
			// handle successful returns
			// FIXME: how do we determine success for iframes? Is there an equiv of
			// the "status" property?
	
			try{
				var cmt = req.mimetype;
				if((cmt == "text/javascript")||(cmt == "text/json")||(cmt == "application/json")){
					// FIXME: not sure what to do here? try to pull some evalulable
					// text from a textarea or cdata section? 
					// how should we set up the contract for that?
					var js = ifd.getElementsByTagName("textarea")[0].value;
					if(cmt == "text/json" || cmt == "application/json") { js = "(" + js + ")"; }
					value = dj_eval(js);
				}else if(cmt == "text/html"){
					value = ifd;
				}else{ // text/plain
					value = ifd.getElementsByTagName("textarea")[0].value;
				}
				success = true;
			}catch(e){ 
				// looks like we didn't get what we wanted!
				this._callError(req, "IframeTransport Error: " + e);
			}
		}

		// don't want to mix load function errors with processing errors, thus
		// a separate try..catch
		try {
			if(success && dojo.lang.isFunction(req["load"])){
				req.load("load", value, req);
			}
		} catch(e) {
			throw e;
		} finally {
			_this.currentRequest = null;
			_this.fireNextRequest();
		}
	}
	
	this._callError = function(req /* Object */, message /* String */){
		var errObj = new dojo.io.Error(message);
		if(dojo.lang.isFunction(req["error"])){
			req.error("error", errObj, req);
		}
	}

	dojo.io.transports.addTransport("IframeTransport");
}
