/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.Editor2Plugin.FindReplace");

dojo.require("dojo.widget.Editor2");

//TODO replace, better GUI

dojo.declare("dojo.widget.Editor2Plugin.FindCommand", dojo.widget.Editor2DialogCommand,{
	SearchOption: {
		CaseSensitive: 4,
		SearchBackwards: 64,
		WholeWord: 2,
		WrapSearch: 128
	},
	find: function(text, option){
		this._editor.focus();
		if(window.find){ //moz
			this._editor.window.find(text, 
				option & this.SearchOption.CaseSensitive ? true : false,
				option & this.SearchOption.SearchBackwards ? true : false,
				option & this.SearchOption.WrapSearch ? true : false,
				option & this.SearchOption.WholeWord ? true : false
				);
		}else if(dojo.body().createTextRange){ //IE
			var range = this._editor.document.body.createTextRange();
			var found = range.findText(text, (option&this.SearchOption.SearchBackwards)?1:-1, option );
			if(found){
				range.scrollIntoView() ;
				range.select() ;
			}else{
				alert("Can not find "+text+" in the document");
			}
		}else{
			alert("No idea how to search in this browser. Please submit patch if you know.");
		}
	},
	getText: function(){
		return 'Find';
	}
});

dojo.widget.Editor2Plugin.FindReplace ={
	getCommand: function(editor, name){
		var name = name.toLowerCase();
		var command;
		if(name == 'find'){
			command = new dojo.widget.Editor2Plugin.FindCommand(editor, 'find', 
				{contentFile: "dojo.widget.Editor2Plugin.FindReplaceDialog", 
				contentClass: "Editor2FindDialog",
				title: "Find", width: "350px", height: "150px", modal: false});
		}else if(name == 'replace') {
			command = new dojo.widget.Editor2DialogCommand(editor, 'replace', 
				{contentFile: "dojo.widget.Editor2Plugin.FindReplaceDialog", 
				contentClass: "Editor2ReplaceDialog",
				href: dojo.uri.dojoUri("src/widget/templates/Editor2/Dialog/replace.html"), 
				title: "Replace", width: "350px", height: "200px", modal: false});
		}
	
		return command;
	},
	getToolbarItem: function(name){
		var name = name.toLowerCase();
	
		var item;
		if(name == 'replace'){
			item = new dojo.widget.Editor2ToolbarButton('Replace');
		}else if(name == 'find') {
			item = new dojo.widget.Editor2ToolbarButton('Find');
		}
	
		return item;
	}
}
dojo.widget.Editor2Manager.registerHandler(dojo.widget.Editor2Plugin.FindReplace.getCommand);
dojo.widget.Editor2ToolbarItemManager.registerHandler(dojo.widget.Editor2Plugin.FindReplace.getToolbarItem);