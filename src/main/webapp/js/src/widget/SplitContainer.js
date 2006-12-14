/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.SplitContainer");

//
// TODO
// make it prettier
// active dragging upwards doesn't always shift other bars (direction calculation is wrong in this case)
//

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.ContentPane");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.html.style");
dojo.require("dojo.html.layout");
dojo.require("dojo.html.selection");
dojo.require("dojo.io.cookie");

dojo.widget.defineWidget(
	"dojo.widget.SplitContainer",
	dojo.widget.HtmlWidget,
	function(){
		this.sizers = [];
	},
{
	// summary
	//		Contains multiple children widgets, all of which are displayed side by side
	//		(either horizontally or vertically); there's a bar between each of the children,
	//		and you can adjust the relative size of each child by dragging the bars.
	//
	//		You must specify a size (width and height) for the SplitContainer.

	isContainer: true,

	templateCssPath: dojo.uri.dojoUri("src/widget/templates/SplitContainer.css"),

	// activeSizing: Boolean
	//		If true, the children's size changes as you drag the bar;
	//		otherwise, the sizes don't change until you drop the bar (by mouse-up)
	activeSizing: false,
	
	// sizerWidget: Integer
	//		Size in pixels of the bar between each child
	sizerWidth: 15,
	
	// orientation: String
	//		either 'horizontal' or vertical; indicates whether the children are
	//		arranged side-by-side or up/down.
	orientation: 'horizontal',
	
	// persist: Boolean
	//		Save splitter positions in a cookie
	persist: true,

	postMixInProperties: function(){
		dojo.widget.SplitContainer.superclass.postMixInProperties.apply(this, arguments);
		this.isHorizontal = (this.orientation == 'horizontal');
	},

	fillInTemplate: function(){
		dojo.widget.SplitContainer.superclass.fillInTemplate.apply(this, arguments);
		dojo.html.addClass(this.domNode, "dojoSplitContainer");
		// overflow has to be explicitly hidden for splitContainers using gekko (trac #1435)
		// to keep other combined css classes from inadvertantly making the overflow visible
		if (dojo.render.html.moz) {
		        this.domNode.style.overflow = '-moz-scrollbars-none'; // hidden doesn't work
		}
		
		var content = dojo.html.getContentBox(this.domNode);
		this.paneWidth = content.width;
		this.paneHeight = content.height;
	},

	onResized: function(e){
		var content = dojo.html.getContentBox(this.domNode);
		this.paneWidth = content.width;
		this.paneHeight = content.height;
		this._layoutPanels();
	},

	postCreate: function(args, fragment, parentComp){
		dojo.widget.SplitContainer.superclass.postCreate.apply(this, arguments);
		// attach the children and create the draggers
		for(var i=0; i<this.children.length; i++){
            with(this.children[i].domNode.style){
                position = "absolute";
            }
            dojo.html.addClass(this.children[i].domNode,
                "dojoSplitPane");

            if(i == this.children.length-1){
                break;
            }

            this._addSizer();
		}

		// create the fake dragger
		if (typeof this.sizerWidth == "object") { 
			try {
				this.sizerWidth = parseInt(this.sizerWidth.toString()); 
			} catch(e) { this.sizerWidth = 15; }
		}
		this.virtualSizer = document.createElement('div');
		this.virtualSizer.style.position = 'absolute';
		this.virtualSizer.style.display = 'none';
		//this.virtualSizer.style.backgroundColor = 'lime';
		this.virtualSizer.style.zIndex = 10;
		this.virtualSizer.className = this.isHorizontal ? 'dojoSplitContainerVirtualSizerH' : 'dojoSplitContainerVirtualSizerV';
		this.domNode.appendChild(this.virtualSizer);

		dojo.html.disableSelection(this.virtualSizer);

		if(this.persist){
			this._restoreState();
		}

		// size the panels once the browser has caught up
		this.resizeSoon();
	},

    _injectChild: function(child) {
        with(child.domNode.style){
            position = "absolute";
        }
        dojo.html.addClass(child.domNode,
            "dojoSplitPane");
    },

    _addSizer: function() {
        var i = this.sizers.length;

        this.sizers[i] = document.createElement('div');
        this.sizers[i].style.position = 'absolute';
        this.sizers[i].className = this.isHorizontal ? 'dojoSplitContainerSizerH' : 'dojoSplitContainerSizerV';

        var self = this;
        var handler = (function(){ var sizer_i = i; return function(e){ self.beginSizing(e, sizer_i); } })();
        dojo.event.connect(this.sizers[i], "onmousedown", handler);

        this.domNode.appendChild(this.sizers[i]);
        dojo.html.disableSelection(this.sizers[i]);
    },

    removeChild: function(widget){
        // Remove sizer, but only if widget is really our child and
        // we have at least one sizer to throw away
        if (this.sizers.length > 0) {
            for(var x=0; x<this.children.length; x++){
                if(this.children[x] === widget){
                    var i = this.sizers.length - 1;
                    this.domNode.removeChild(this.sizers[i]);
                    this.sizers.length = i;
                    break;
                }
            }
        }

        // Remove widget and repaint
        dojo.widget.SplitContainer.superclass.removeChild.call(this, widget, arguments);
        this.onResized();
    },

    addChild: function(widget){
        dojo.widget.SplitContainer.superclass.addChild.apply(this, arguments);
        this._injectChild(widget);

        if (this.children.length > 1) {
            this._addSizer();
        }

        this._layoutPanels();
    },

    _layoutPanels: function(){
        if (this.children.length == 0){ return; }

		//
		// calculate space
		//

		var space = this.isHorizontal ? this.paneWidth : this.paneHeight;
		if (this.children.length > 1){
			space -= this.sizerWidth * (this.children.length - 1);
		}


		//
		// calculate total of SizeShare values
		//

		var out_of = 0;
		for(var i=0; i<this.children.length; i++){
			out_of += this.children[i].sizeShare;
		}


		//
		// work out actual pixels per sizeshare unit
		//

		var pix_per_unit = space / out_of;


		//
		// set the SizeActual member of each pane
		//

		var total_size = 0;

		for(var i=0; i<this.children.length-1; i++){
			var size = Math.round(pix_per_unit * this.children[i].sizeShare);
			this.children[i].sizeActual = size;
			total_size += size;
		}
		this.children[this.children.length-1].sizeActual = space - total_size;

		//
		// make sure the sizes are ok
		//

		this._checkSizes();


		//
		// now loop, positioning each pane and letting children resize themselves
		//

		var pos = 0;
		var size = this.children[0].sizeActual;
		this._movePanel(this.children[0], pos, size);
		this.children[0].position = pos;
		pos += size;

		for(var i=1; i<this.children.length; i++){

			// first we position the sizing handle before this pane
			this._moveSlider(this.sizers[i-1], pos, this.sizerWidth);
			this.sizers[i-1].position = pos;
			pos += this.sizerWidth;

			size = this.children[i].sizeActual;
			this._movePanel(this.children[i], pos, size);
			this.children[i].position = pos;
			pos += size;
		}
	},

	_movePanel: function(/*Widget*/ panel, pos, size){
		if (this.isHorizontal){
			panel.domNode.style.left = pos + 'px';
			panel.domNode.style.top = 0;
			panel.resizeTo(size, this.paneHeight);
		}else{
			panel.domNode.style.left = 0;
			panel.domNode.style.top = pos + 'px';
			panel.resizeTo(this.paneWidth, size);
		}
	},

	_moveSlider: function(/*DomNode*/ slider, pos, size){
		if (this.isHorizontal){
			slider.style.left = pos + 'px';
			slider.style.top = 0;
			dojo.html.setMarginBox(slider, { width: size, height: this.paneHeight });
		}else{
			slider.style.left = 0;
			slider.style.top = pos + 'px';
			dojo.html.setMarginBox(slider, { width: this.paneWidth, height: size });
		}
	},

	_growPane: function(growth, pane){
		if (growth > 0){
			if (pane.sizeActual > pane.sizeMin){
				if ((pane.sizeActual - pane.sizeMin) > growth){

					// stick all the growth in this pane
					pane.sizeActual = pane.sizeActual - growth;
					growth = 0;
				}else{
					// put as much growth in here as we can
					growth -= pane.sizeActual - pane.sizeMin;
					pane.sizeActual = pane.sizeMin;
				}
			}
		}
		return growth;
	},

	_checkSizes: function(){

		var total_min_size = 0;
		var total_size = 0;

		for(var i=0; i<this.children.length; i++){

			total_size += this.children[i].sizeActual;
			total_min_size += this.children[i].sizeMin;
		}

		// only make adjustments if we have enough space for all the minimums

		if (total_min_size <= total_size){

			var growth = 0;

			for(var i=0; i<this.children.length; i++){

				if (this.children[i].sizeActual < this.children[i].sizeMin){

					growth += this.children[i].sizeMin - this.children[i].sizeActual;
					this.children[i].sizeActual = this.children[i].sizeMin;
				}
			}

			if (growth > 0){
				if (this.isDraggingLeft){
					for(var i=this.children.length-1; i>=0; i--){
						growth = this._growPane(growth, this.children[i]);
					}
				}else{
					for(var i=0; i<this.children.length; i++){
						growth = this._growPane(growth, this.children[i]);
					}
				}
			}
		}else{

			for(var i=0; i<this.children.length; i++){
				this.children[i].sizeActual = Math.round(total_size * (this.children[i].sizeMin / total_min_size));
			}
		}
	},

	beginSizing: function(e, i){
		this.paneBefore = this.children[i];
		this.paneAfter = this.children[i+1];

		this.isSizing = true;
		this.sizingSplitter = this.sizers[i];
		this.originPos = dojo.html.getAbsolutePosition(this.children[0].domNode, true, dojo.html.boxSizing.MARGIN_BOX);
		if (this.isHorizontal){
			var client = (e.layerX ? e.layerX : e.offsetX);
			var screen = e.pageX;
			this.originPos = this.originPos.x;
		}else{
			var client = (e.layerY ? e.layerY : e.offsetY);
			var screen = e.pageY;
			this.originPos = this.originPos.y;
		}
		this.startPoint = this.lastPoint = screen;
		this.screenToClientOffset = screen - client;
		this.dragOffset = this.lastPoint - this.paneBefore.sizeActual - this.originPos - this.paneBefore.position;

		if (!this.activeSizing){
			this._showSizingLine();
		}

		//
		// attach mouse events
		//

		dojo.event.connect(document.documentElement, "onmousemove", this, "changeSizing");
		dojo.event.connect(document.documentElement, "onmouseup", this, "endSizing");
		dojo.event.browser.stopEvent(e);
	},

	changeSizing: function(e){
		this.lastPoint = this.isHorizontal ? e.pageX : e.pageY;
		if (this.activeSizing){
			this.movePoint();
			this._updateSize();
		}else{
			this.movePoint();
			this._moveSizingLine();
		}
		dojo.event.browser.stopEvent(e);
	},

	endSizing: function(e){

		if (!this.activeSizing){
			this._hideSizingLine();
		}

		this._updateSize();

		this.isSizing = false;

		dojo.event.disconnect(document.documentElement, "onmousemove", this, "changeSizing");
		dojo.event.disconnect(document.documentElement, "onmouseup", this, "endSizing");
		
		if(this.persist){
			this._saveState(this);
		}
	},

	movePoint: function(){

		// make sure lastPoint is a legal point to drag to
		var p = this.lastPoint - this.screenToClientOffset;

		var a = p - this.dragOffset;
		a = this.legaliseSplitPoint(a);
		p = a + this.dragOffset;

		this.lastPoint = p + this.screenToClientOffset;
	},

	legaliseSplitPoint: function(a){

		a += this.sizingSplitter.position;

		this.isDraggingLeft = (a > 0) ? true : false;

		if (!this.activeSizing){

			if (a < this.paneBefore.position + this.paneBefore.sizeMin){

				a = this.paneBefore.position + this.paneBefore.sizeMin;
			}

			if (a > this.paneAfter.position + (this.paneAfter.sizeActual - (this.sizerWidth + this.paneAfter.sizeMin))){

				a = this.paneAfter.position + (this.paneAfter.sizeActual - (this.sizerWidth + this.paneAfter.sizeMin));
			}
		}

		a -= this.sizingSplitter.position;

		this._checkSizes();

		return a;
	},

	_updateSize: function(){
		var pos = this.lastPoint - this.dragOffset - this.originPos;

		var start_region = this.paneBefore.position;
		var end_region   = this.paneAfter.position + this.paneAfter.sizeActual;

		this.paneBefore.sizeActual = pos - start_region;
		this.paneAfter.position    = pos + this.sizerWidth;
		this.paneAfter.sizeActual  = end_region - this.paneAfter.position;

		for(var i=0; i<this.children.length; i++){

			this.children[i].sizeShare = this.children[i].sizeActual;
		}

		this._layoutPanels();
	},

	_showSizingLine: function(){

		this._moveSizingLine();

		if (this.isHorizontal){
			dojo.html.setMarginBox(this.virtualSizer, { width: this.sizerWidth, height: this.paneHeight });
		}else{
			dojo.html.setMarginBox(this.virtualSizer, { width: this.paneWidth, height: this.sizerWidth });
		}

		this.virtualSizer.style.display = 'block';
	},

	_hideSizingLine: function(){
		this.virtualSizer.style.display = 'none';
	},

	_moveSizingLine: function(){
		var pos = this.lastPoint - this.startPoint + this.sizingSplitter.position;
		if (this.isHorizontal){
			this.virtualSizer.style.left = pos + 'px';
		}else{
			var pos = (this.lastPoint - this.startPoint) + this.sizingSplitter.position;
			this.virtualSizer.style.top = pos + 'px';
		}

	},
	
	_getCookieName: function(i) {
		return this.widgetId + "_" + i;
	},

	_restoreState: function () {
		for(var i=0; i<this.children.length; i++) {
			var cookieName = this._getCookieName(i);
			var cookieValue = dojo.io.cookie.getCookie(cookieName);
			if (cookieValue != null) {
				var pos = parseInt(cookieValue);
				if (typeof pos == "number") {
					this.children[i].sizeShare=pos;
				}
			}
		}
	},

	_saveState: function (){
		for(var i=0; i<this.children.length; i++) {
			var cookieName = this._getCookieName(i);
			dojo.io.cookie.setCookie(cookieName, this.children[i].sizeShare, null, null, null, null);
		}
	}
});

// These arguments can be specified for the children of a SplitContainer.
// Since any widget can be specified as a SplitContainer child, mix them
// into the base widget class.  (This is a hack, but it's effective.)
dojo.lang.extend(dojo.widget.Widget, {
	// sizeMin: Integer
	//	Minimum size (width or height) of a child of a SplitContainer.
	//	The value is relative to other children's sizeShare properties.
	sizeMin: 10,

	// sizeShare: Integer
	//	Size (width or height) of a child of a SplitContainer.
	//	The value is relative to other children's sizeShare properties.
	//	For example, if there are two children and each has sizeShare=10, then
	//	each takes up 50% of the available space.
	sizeShare: 10
});

// Deprecated class for split pane children.
// Actually any widget can be the child of a split pane
dojo.widget.defineWidget(
	"dojo.widget.SplitContainerPanel",
	dojo.widget.ContentPane,
	{}
);

