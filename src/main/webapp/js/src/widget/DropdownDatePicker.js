/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.DropdownDatePicker");

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.DropdownContainer");
dojo.require("dojo.widget.DatePicker");
dojo.require("dojo.event.*");
dojo.require("dojo.html.*");
dojo.require("dojo.date.format");
dojo.require("dojo.date.serialize");
dojo.require("dojo.string.common");
dojo.require("dojo.i18n.common");
dojo.requireLocalization("dojo.widget", "DropdownDatePicker", null, "ROOT");


dojo.widget.defineWidget(
	"dojo.widget.DropdownDatePicker",
	dojo.widget.DropdownContainer,
	{
		/*
		summary: 
			A form input for entering dates with a pop-up dojo.widget.DatePicker to aid in selection
		
			description: 
				This is DatePicker in a DropdownContainer, it supports all features of DatePicker.
		
				The value displayed in the widget is localized according to the default algorithm provided
				in dojo.date.format and dojo.date.parse.  It is possible to customize the user-visible formatting
				with either the formatLength or displayFormat attributes.  The value sent to the server is
				typically a locale-independent value in a hidden field as defined by the name attribute.
				RFC3339 representation is used by default, but other options are available with saveFormat.
		
			usage: 
				var ddp = dojo.widget.createWidget("DropdownDatePicker", {},   
				dojo.byId("DropdownDatePickerNode")); 
		 	 
				<input dojoType="DropdownDatePicker">
		*/

		iconURL: dojo.uri.dojoUri("src/widget/templates/images/dateIcon.gif"),

		// formatLength: String
		// 	Type of formatting used for visual display, appropriate to locale (choice of long, short, medium or full)
		//  See dojo.date.format for details.
		formatLength: "short",

		// displayFormat: String
		//	A pattern used for the visual display of the formatted date, e.g. dd/MM/yyyy.
		//	Setting this overrides the default locale-specific settings as well as the formatLength
		//	attribute.  See dojo.date.format for a reference which defines the formatting patterns.
		displayFormat: "",

		dateFormat: "", // deprecated, will be removed for 0.5

		// saveFormat: String
		//	Formatting scheme used when submitting the form element.  This formatting is used in a hidden
		//  field (name) intended for server use, and is therefore typically locale-independent.
		//  By default, uses rfc3339 style date formatting (rfc)
		//	Use a pattern string like displayFormat or one of the following:
		//	rfc|iso|posix|unix
		saveFormat: "",

		// value: String|Date
		//	form value property in rfc3339 format. If =='today', will use today's date
		value: "",

		// name: String
		// 	name of the form element, used to create a hidden field by this name for form element submission.
		name: "",

		// Implement various attributes from DatePicker

		// displayWeeks: Integer
		//	number of weeks to display 
		displayWeeks: 6,

		// adjustWeeks: Boolean
		//	if true, weekly size of calendar changes to accomodate the month if false, 42 day format is used
		adjustWeeks: false,

		// startDate: String|Date
		//	first available date in the calendar set
		startDate: "1492-10-12",

		// endDate: String|Date
		//	last available date in the calendar set
		endDate: "2941-10-12",

		// weekStartsOn: Integer
		//	adjusts the first day of the week 0==Sunday..6==Saturday
		weekStartsOn: "",

		// staticDisplay: Boolean
		//	disable all incremental controls, must pick a date in the current display
		staticDisplay: false,
		
		postMixInProperties: function(localProperties, frag){
			// summary: see dojo.widget.DomWidget

			dojo.widget.DropdownDatePicker.superclass.postMixInProperties.apply(this, arguments);
			var messages = dojo.i18n.getLocalization("dojo.widget", "DropdownDatePicker", this.lang);
			this.iconAlt = messages.selectDate;

			//FIXME: should this be if/else/else?
			if(typeof(this.value)=='string'&&this.value.toLowerCase()=='today'){
				this.value = new Date();
			}
			if(this.value && isNaN(this.value)){
				var orig = this.value;
				this.value = dojo.date.fromRfc3339(this.value);
				if(!this.value){this.value = new Date(orig); dojo.deprecated("dojo.widget.DropdownDatePicker", "date attributes must be passed in Rfc3339 format", "0.5");}
			}
			if(this.value && !isNaN(this.value)){
				this.value = new Date(this.value);
			}
		},

		fillInTemplate: function(args, frag){
			// summary: see dojo.widget.DomWidget
			dojo.widget.DropdownDatePicker.superclass.fillInTemplate.call(this, args, frag);
			//attributes to be passed on to DatePicker
			var dpArgs = {widgetContainerId: this.widgetId, lang: this.lang, value: this.value,
				startDate: this.startDate, endDate: this.endDate, displayWeeks: this.displayWeeks,
				weekStartsOn: this.weekStartsOn, adjustWeeks: this.adjustWeeks, staticDisplay: this.staticDisplay};

			//build the args for DatePicker based on the public attributes of DropdownDatePicker
			this.datePicker = dojo.widget.createWidget("DatePicker", dpArgs, this.containerNode, "child");
			dojo.event.connect(this.datePicker, "onValueChanged", this, "_updateText");
			
			if(this.value){
				this._updateText();
			}
			this.containerNode.explodeClassName = "calendarBodyContainer";
			this.valueNode.name=this.name;
		},

		getValue: function(){
			// summary: return current date in RFC 3339 format
			return this.valueNode.value; /*String*/
		},

		getDate: function(){
			// summary: return current date as a Date object
			return this.datePicker.value; /*Date*/
		},

		setValue: function(/*Date|String*/rfcDate){
			//summary: set the current date from RFC 3339 formatted string or a date object, synonymous with setDate
			this.setDate(rfcDate);
		},

		setDate: function(/*Date|String*/dateObj){
			// summary: set the current date and update the UI
			this.datePicker.setDate(dateObj);
			this._syncValueNode();
		},
	
		_updateText: function(){
			// summary: updates the <input> field according to the current value (ie, displays the formatted date)
			if(this.dateFormat){
				dojo.deprecated("dojo.widget.DropdownDatePicker",
				"Must use displayFormat attribute instead of dateFormat.  See dojo.date.format for specification.", "0.5");
				this.inputNode.value = dojo.date.strftime(this.datePicker.value, this.dateFormat, this.lang);
			}else if(this.datePicker.value){
				this.inputNode.value = dojo.date.format(this.datePicker.value,
					{formatLength:this.formatLength, datePattern:this.displayFormat, selector:'dateOnly', locale:this.lang});
			}else{
				this.inputNode.value = "";
			}

			if(this.value < this.datePicker.startDate||this.value>this.datePicker.endDate){
				this.inputNode.value = "";
			}
			this._syncValueNode();
			this.onValueChanged(this.getDate());
			this.hideContainer();
		},

		onValueChanged: function(/*Date*/dateObj){
			//summary: triggered when this.value is changed
		},
		
		onInputChange: function(){
			// summary: callback when user manually types a date into the <input> field
			if(this.dateFormat){
				dojo.deprecated("dojo.widget.DropdownDatePicker",
				"Cannot parse user input.  Must use displayFormat attribute instead of dateFormat.  See dojo.date.format for specification.", "0.5");
			}else{
				var input = dojo.string.trim(this.inputNode.value);
				if(input){
					var inputDate = dojo.date.parse(input,
							{formatLength:this.formatLength, datePattern:this.displayFormat, selector:'dateOnly', locale:this.lang});			
					if(inputDate){
						this.setDate(inputDate);
					}
				} else {
					this.valueNode.value = input;
				}
			}
			// If the date entered didn't parse, reset to the old date.  KISS, for now.
			//TODO: usability?  should we provide more feedback somehow? an error notice?
			// seems redundant to do this if the parse failed, but at least until we have validation,
			// this will fix up the display of entries like 01/32/2006
			if(input){ this._updateText(); }
		},

		_syncValueNode: function(){
			var date = this.datePicker.value;
				var value = "";
			switch(this.saveFormat.toLowerCase()){
				case "rfc": case "iso": case "":
					value = dojo.date.toRfc3339(date, 'dateOnly');
					break;
				case "posix": case "unix":
					value = Number(date);
					break;
				default:
					if(date){
						value = dojo.date.format(date, {datePattern:this.saveFormat, selector:'dateOnly', locale:this.lang});
					}
			}
			this.valueNode.value = value;
		},
		
		destroy: function(/*Boolean*/finalize){
			this.datePicker.destroy(finalize);
			dojo.widget.DropdownDatePicker.superclass.destroy.apply(this, arguments);
		}
	}
);
