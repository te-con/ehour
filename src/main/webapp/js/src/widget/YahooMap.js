/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.YahooMap");
dojo.require("dojo.event.*");
dojo.require("dojo.math");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");

(function(){
	var yappid = djConfig["yAppId"]||djConfig["yahooAppId"]||"dojotoolkit";
	if(!dojo.hostenv.post_load_){
		if(yappid == "dojotoolkit"){
			dojo.debug("please provide a unique Yahoo App ID in djConfig.yahooAppId when using the map widget");
		}
		var tag = "<scr"+"ipt src='http://api.maps.yahoo.com/ajaxymap?v=3.0&appid="+yappid+"'></scri"+"pt>";
		if(!dj_global["YMap"]){
			document.write(tag);
		}
	}else{
		dojo.debug("cannot initialize map system after the page has been loaded! Please either manually include the script block provided by Yahoo in your page or require() the YahooMap widget before onload has fired");
	}
})();

dojo.widget.defineWidget(
	"dojo.widget.YahooMap",
	dojo.widget.HtmlWidget,
	function(){
		// summary: Creates a widget that wraps the Yahoo Map API.
		// description:
		//		Widget wrapper for the Yahoo Map API; it allows you to easily embed a Yahoo Map
		//		within your Dojo application.
		// map: YMap
		//		Reference to the Yahoo Map object.
		// datasrc: String
		//		Reference to an external (to the widget) source for point data.
		// data: Object[]
		//		Array of point information objects.
		// width: Number
		//		Width of the map control.
		// height: Number
		// 		Height of the map control
		// controls: String[]
		//		Array of strings that map to corresponding controls on a Yahoo Map.
		this.map=null;
		this.datasrc="";
		this.data=[];
		this.width=0;
		this.height=0;
		this.controls=["zoomlong","maptype","pan"];
	},
{
	isContainer: false,
	templatePath:null,
	templateCssPath:null,

	findCenter:function(/* array */aPts){
		//	summary
		//	Find the center lat/lng coordinate based on the passed points.
		var start=new YGeoPoint(37,-90);
		if(aPts.length==0) return start;
		var minLat,maxLat, minLon, maxLon, cLat, cLon;
		minLat=maxLat=aPts[0].Lat;
		minLon=maxLon=aPts[0].Lon;
		for(var i=0; i<aPts.length; i++){
			minLat=Math.min(minLat,aPts[i].Lat);
			maxLat=Math.max(maxLat,aPts[i].Lat);
			minLon=Math.min(minLon,aPts[i].Lon);
			maxLon=Math.max(maxLon,aPts[i].Lon);
		}
		cLat=dojo.math.round((minLat+maxLat)/2,6);
		cLon=dojo.math.round((minLon+maxLon)/2,6);
		return new YGeoPoint(cLat,cLon);	//	YGeoPoint
	},
	setControls:function(){
		//	summary
		//	Set the controls on the map
		var methodmap={
			maptype:"addTypeControl",
			pan:"addPanControl",
			zoomlong:"addZoomLong",
			zoomshort:"addZoomShort"
		}
		var c=this.controls;
		for(var i=0; i<c.length; i++){
			var controlMethod=methodmap[c[i].toLowerCase()];
			if(this.map[controlMethod]){
				this.map[controlMethod]();
			}
		}
	},
	
	parse:function(/* HTMLTable */table){
		//	summary
		//	Parses an HTML table for data to plot on the map.
		this.data=[];

		//	get the column indices
		var h=table.getElementsByTagName("thead")[0];
		if(!h){
			return;
		}

		var a=[];
		var cols=h.getElementsByTagName("td");
		if(cols.length==0){
			cols=h.getElementsByTagName("th");
		}
		for(var i=0; i<cols.length; i++){
			var c=cols[i].innerHTML.toLowerCase();
			if(c=="long") c="lng";
			a.push(c);
		}
		
		//	parse the data
		var b=table.getElementsByTagName("tbody")[0];
		if(!b){
			return;
		}
		for(var i=0; i<b.childNodes.length; i++){
			if(!(b.childNodes[i].nodeName&&b.childNodes[i].nodeName.toLowerCase()=="tr")){
				continue;
			}
			var cells=b.childNodes[i].getElementsByTagName("td");
			var o={};
			for(var j=0; j<a.length; j++){
				var col=a[j];
				if(col=="lat"||col=="lng"){
					o[col]=parseFloat(cells[j].innerHTML);					
				}else{
					o[col]=cells[j].innerHTML;
				}
			}
			this.data.push(o);
		}
	},
	render:function(){
		//	summary
		//	Plots all points in internal data array on the map.
		var pts=[];
		var d=this.data;
		for(var i=0; i<d.length; i++){
			var pt=new YGeoPoint(d[i].lat, d[i].lng);
			pts.push(pt);
			var icon=d[i].icon||null;
			if(icon){
				icon=new YImage(icon);
			}
			var m=new YMarker(pt,icon);
			if(d[i].description){
				m.addAutoExpand("<div>"+d[i].description+"</div>");
			}
			this.map.addOverlay(m);
		}
		var c=this.findCenter(pts);
		var z=this.map.getZoomLevel(pts);
		this.map.drawZoomAndCenter(c,z);
	},
	
	initialize:function(/* object */args, /* object */frag){
		//	summary
		//	Initialize the widget.
		if(!YMap || !YGeoPoint){
			dojo.raise("dojo.widget.YahooMap: The Yahoo Map script must be included in order to use this widget.");
		}
		if(this.datasrc){
			this.parse(dojo.byId(this.datasrc));
		}
		else if(this.domNode.getElementsByTagName("table")[0]){
			this.parse(this.domNode.getElementsByTagName("table")[0]);
		}
	},
	postCreate:function(){
		//	summary
		//	Finalize and plot all points on the widget.
		while(this.domNode.childNodes.length>0){
			this.domNode.removeChild(this.domNode.childNodes[0]);
		}

		if(this.width>0&&this.height>0){
			this.map=new YMap(this.domNode, YAHOO_MAP_REG, new YSize(this.width, this.height));
		}else{
			this.map=new YMap(this.domNode);
		}
		this.setControls();
		this.render();
	}
});
