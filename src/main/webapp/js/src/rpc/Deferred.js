/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.rpc.Deferred");
dojo.require("dojo.Deferred");

dojo.deprecated("dojo.rpc.Deferred", "replaced by dojo.Deferred", "0.6");
dojo.rpc.Deferred = dojo.Deferred;
dojo.rpc.Deferred.prototype = dojo.Deferred.prototype;
