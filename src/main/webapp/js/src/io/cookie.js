/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.io.cookie");

dojo.io.cookie.setCookie = function(/*String*/name, /*String*/value, 
									/*Number?*/days, /*String?*/path, 
									/*String?*/domain, /*boolean?*/secure){
	//summary: sets a cookie.
	var expires = -1;
	if((typeof days == "number")&&(days >= 0)){
		var d = new Date();
		d.setTime(d.getTime()+(days*24*60*60*1000));
		expires = d.toGMTString();
	}
	value = escape(value);
	document.cookie = name + "=" + value + ";"
		+ (expires != -1 ? " expires=" + expires + ";" : "")
		+ (path ? "path=" + path : "")
		+ (domain ? "; domain=" + domain : "")
		+ (secure ? "; secure" : "");
}

dojo.io.cookie.set = dojo.io.cookie.setCookie;

dojo.io.cookie.getCookie = function(/*String*/name){
	//summary: Gets a cookie with the given name.

	// FIXME: Which cookie should we return?
	//        If there are cookies set for different sub domains in the current
	//        scope there could be more than one cookie with the same name.
	//        I think taking the last one in the list takes the one from the
	//        deepest subdomain, which is what we're doing here.
	var idx = document.cookie.lastIndexOf(name+'=');
	if(idx == -1) { return null; }
	var value = document.cookie.substring(idx+name.length+1);
	var end = value.indexOf(';');
	if(end == -1) { end = value.length; }
	value = value.substring(0, end);
	value = unescape(value);
	return value; //String
}

dojo.io.cookie.get = dojo.io.cookie.getCookie;

dojo.io.cookie.deleteCookie = function(/*String*/name){
	//summary: Deletes a cookie with the given name.
	dojo.io.cookie.setCookie(name, "-", 0);
}

dojo.io.cookie.setObjectCookie = function(	/*String*/name, /*Object*/obj, 
											/*Number?*/days, /*String?*/path, 
											/*String?*/domain, /*boolean?*/secure, 
											/*boolean?*/clearCurrent){
	//summary: Takes an object, serializes it to a cookie value, and either
	//sets a cookie with the serialized value.
	//description: If clearCurrent is true, then any current cookie value
	//for this object will be replaced with the the new serialized object value.
	//If clearCurrent is false, then the existing cookie value will be modified
	//with any changes from the new object value.
	//Objects must be simple name/value pairs where the value is either a string
	//or a number. Any other value will be ignored.
	if(arguments.length == 5){ // for backwards compat
		clearCurrent = domain;
		domain = null;
		secure = null;
	}
	var pairs = [], cookie, value = "";
	if(!clearCurrent){
		cookie = dojo.io.cookie.getObjectCookie(name);
	}
	if(days >= 0){
		if(!cookie){ cookie = {}; }
		for(var prop in obj){
			if(obj[prop] == null){
				delete cookie[prop];
			}else if((typeof obj[prop] == "string")||(typeof obj[prop] == "number")){
				cookie[prop] = obj[prop];
			}
		}
		prop = null;
		for(var prop in cookie){
			pairs.push(escape(prop) + "=" + escape(cookie[prop]));
		}
		value = pairs.join("&");
	}
	dojo.io.cookie.setCookie(name, value, days, path, domain, secure);
}

dojo.io.cookie.getObjectCookie = function(/*String*/name){
	//summary: Gets an object value for the given cookie name. The complement of
	//dojo.io.cookie.setObjectCookie().
	var values = null, cookie = dojo.io.cookie.getCookie(name);
	if(cookie){
		values = {};
		var pairs = cookie.split("&");
		for(var i = 0; i < pairs.length; i++){
			var pair = pairs[i].split("=");
			var value = pair[1];
			if( isNaN(value) ){ value = unescape(pair[1]); }
			values[ unescape(pair[0]) ] = value;
		}
	}
	return values;
}

dojo.io.cookie.isSupported = function(){
	//summary: Tests the browser to see if cookies are enabled.
	if(typeof navigator.cookieEnabled != "boolean"){
		dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__",
			"CookiesAllowed", 90, null);
		var cookieVal = dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");
		navigator.cookieEnabled = (cookieVal == "CookiesAllowed");
		if(navigator.cookieEnabled){
			// FIXME: should we leave this around?
			this.deleteCookie("__TestingYourBrowserForCookieSupport__");
		}
	}
	return navigator.cookieEnabled; //boolean
}

// need to leave this in for backwards-compat from 0.1 for when it gets pulled in by dojo.io.*
if(!dojo.io.cookies){ dojo.io.cookies = dojo.io.cookie; }
