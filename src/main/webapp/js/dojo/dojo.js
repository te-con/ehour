/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

/*
	This is a compiled version of Dojo, built for deployment and not for
	development. To get an editable version, please visit:

		http://dojotoolkit.org

	for documentation and information on getting the source.
*/

if(typeof dojo=="undefined"){
var dj_global=this;
var dj_currentContext=this;
function dj_undef(_1,_2){
return (typeof (_2||dj_currentContext)[_1]=="undefined");
}
if(dj_undef("djConfig",this)){
var djConfig={};
}
if(dj_undef("dojo",this)){
var dojo={};
}
dojo.global=function(){
return dj_currentContext;
};
dojo.locale=djConfig.locale;
dojo.version={major:0,minor:4,patch:1,flag:"rc1",revision:Number("$Rev: 6631 $".match(/[0-9]+/)[0]),toString:function(){
with(dojo.version){
return major+"."+minor+"."+patch+flag+" ("+revision+")";
}
}};
dojo.evalProp=function(_3,_4,_5){
if((!_4)||(!_3)){
return undefined;
}
if(!dj_undef(_3,_4)){
return _4[_3];
}
return (_5?(_4[_3]={}):undefined);
};
dojo.parseObjPath=function(_6,_7,_8){
var _9=(_7||dojo.global());
var _a=_6.split(".");
var _b=_a.pop();
for(var i=0,l=_a.length;i<l&&_9;i++){
_9=dojo.evalProp(_a[i],_9,_8);
}
return {obj:_9,prop:_b};
};
dojo.evalObjPath=function(_e,_f){
if(typeof _e!="string"){
return dojo.global();
}
if(_e.indexOf(".")==-1){
return dojo.evalProp(_e,dojo.global(),_f);
}
var ref=dojo.parseObjPath(_e,dojo.global(),_f);
if(ref){
return dojo.evalProp(ref.prop,ref.obj,_f);
}
return null;
};
dojo.errorToString=function(_11){
if(!dj_undef("message",_11)){
return _11.message;
}else{
if(!dj_undef("description",_11)){
return _11.description;
}else{
return _11;
}
}
};
dojo.raise=function(_12,_13){
if(_13){
_12=_12+": "+dojo.errorToString(_13);
}else{
_12=dojo.errorToString(_12);
}
try{
if(djConfig.isDebug){
dojo.hostenv.println("FATAL exception raised: "+_12);
}
}
catch(e){
}
throw _13||Error(_12);
};
dojo.debug=function(){
};
dojo.debugShallow=function(obj){
};
dojo.profile={start:function(){
},end:function(){
},stop:function(){
},dump:function(){
}};
function dj_eval(_15){
return dj_global.eval?dj_global.eval(_15):eval(_15);
}
dojo.unimplemented=function(_16,_17){
var _18="'"+_16+"' not implemented";
if(_17!=null){
_18+=" "+_17;
}
dojo.raise(_18);
};
dojo.deprecated=function(_19,_1a,_1b){
var _1c="DEPRECATED: "+_19;
if(_1a){
_1c+=" "+_1a;
}
if(_1b){
_1c+=" -- will be removed in version: "+_1b;
}
dojo.debug(_1c);
};
dojo.render=(function(){
function vscaffold(_1d,_1e){
var tmp={capable:false,support:{builtin:false,plugin:false},prefixes:_1d};
for(var i=0;i<_1e.length;i++){
tmp[_1e[i]]=false;
}
return tmp;
}
return {name:"",ver:dojo.version,os:{win:false,linux:false,osx:false},html:vscaffold(["html"],["ie","opera","khtml","safari","moz"]),svg:vscaffold(["svg"],["corel","adobe","batik"]),vml:vscaffold(["vml"],["ie"]),swf:vscaffold(["Swf","Flash","Mm"],["mm"]),swt:vscaffold(["Swt"],["ibm"])};
})();
dojo.hostenv=(function(){
var _21={isDebug:false,allowQueryConfig:false,baseScriptUri:"",baseRelativePath:"",libraryScriptUri:"",iePreventClobber:false,ieClobberMinimal:true,preventBackButtonFix:true,delayMozLoadingFix:false,searchIds:[],parseWidgets:true};
if(typeof djConfig=="undefined"){
djConfig=_21;
}else{
for(var _22 in _21){
if(typeof djConfig[_22]=="undefined"){
djConfig[_22]=_21[_22];
}
}
}
return {name_:"(unset)",version_:"(unset)",getName:function(){
return this.name_;
},getVersion:function(){
return this.version_;
},getText:function(uri){
dojo.unimplemented("getText","uri="+uri);
}};
})();
dojo.hostenv.getBaseScriptUri=function(){
if(djConfig.baseScriptUri.length){
return djConfig.baseScriptUri;
}
var uri=new String(djConfig.libraryScriptUri||djConfig.baseRelativePath);
if(!uri){
dojo.raise("Nothing returned by getLibraryScriptUri(): "+uri);
}
var _25=uri.lastIndexOf("/");
djConfig.baseScriptUri=djConfig.baseRelativePath;
return djConfig.baseScriptUri;
};
(function(){
var _26={pkgFileName:"__package__",loading_modules_:{},loaded_modules_:{},addedToLoadingCount:[],removedFromLoadingCount:[],inFlightCount:0,modulePrefixes_:{dojo:{name:"dojo",value:"src"}},setModulePrefix:function(_27,_28){
this.modulePrefixes_[_27]={name:_27,value:_28};
},moduleHasPrefix:function(_29){
var mp=this.modulePrefixes_;
return Boolean(mp[_29]&&mp[_29].value);
},getModulePrefix:function(_2b){
if(this.moduleHasPrefix(_2b)){
return this.modulePrefixes_[_2b].value;
}
return _2b;
},getTextStack:[],loadUriStack:[],loadedUris:[],post_load_:false,modulesLoadedListeners:[],unloadListeners:[],loadNotifying:false};
for(var _2c in _26){
dojo.hostenv[_2c]=_26[_2c];
}
})();
dojo.hostenv.loadPath=function(_2d,_2e,cb){
var uri;
if(_2d.charAt(0)=="/"||_2d.match(/^\w+:/)){
uri=_2d;
}else{
uri=this.getBaseScriptUri()+_2d;
}
if(djConfig.cacheBust&&dojo.render.html.capable){
uri+="?"+String(djConfig.cacheBust).replace(/\W+/g,"");
}
try{
return !_2e?this.loadUri(uri,cb):this.loadUriAndCheck(uri,_2e,cb);
}
catch(e){
dojo.debug(e);
return false;
}
};
dojo.hostenv.loadUri=function(uri,cb){
if(this.loadedUris[uri]){
return true;
}
var _33=this.getText(uri,null,true);
if(!_33){
return false;
}
this.loadedUris[uri]=true;
if(cb){
_33="("+_33+")";
}
var _34=dj_eval(_33);
if(cb){
cb(_34);
}
return true;
};
dojo.hostenv.loadUriAndCheck=function(uri,_36,cb){
var ok=true;
try{
ok=this.loadUri(uri,cb);
}
catch(e){
dojo.debug("failed loading ",uri," with error: ",e);
}
return Boolean(ok&&this.findModule(_36,false));
};
dojo.loaded=function(){
};
dojo.unloaded=function(){
};
dojo.hostenv.loaded=function(){
this.loadNotifying=true;
this.post_load_=true;
var mll=this.modulesLoadedListeners;
for(var x=0;x<mll.length;x++){
mll[x]();
}
this.modulesLoadedListeners=[];
this.loadNotifying=false;
dojo.loaded();
};
dojo.hostenv.unloaded=function(){
var mll=this.unloadListeners;
while(mll.length){
(mll.pop())();
}
dojo.unloaded();
};
dojo.addOnLoad=function(obj,_3d){
var dh=dojo.hostenv;
if(arguments.length==1){
dh.modulesLoadedListeners.push(obj);
}else{
if(arguments.length>1){
dh.modulesLoadedListeners.push(function(){
obj[_3d]();
});
}
}
if(dh.post_load_&&dh.inFlightCount==0&&!dh.loadNotifying){
dh.callLoaded();
}
};
dojo.addOnUnload=function(obj,_40){
var dh=dojo.hostenv;
if(arguments.length==1){
dh.unloadListeners.push(obj);
}else{
if(arguments.length>1){
dh.unloadListeners.push(function(){
obj[_40]();
});
}
}
};
dojo.hostenv.modulesLoaded=function(){
if(this.post_load_){
return;
}
if(this.loadUriStack.length==0&&this.getTextStack.length==0){
if(this.inFlightCount>0){
dojo.debug("files still in flight!");
return;
}
dojo.hostenv.callLoaded();
}
};
dojo.hostenv.callLoaded=function(){
if(typeof setTimeout=="object"){
setTimeout("dojo.hostenv.loaded();",0);
}else{
dojo.hostenv.loaded();
}
};
dojo.hostenv.getModuleSymbols=function(_42){
var _43=_42.split(".");
for(var i=_43.length;i>0;i--){
var _45=_43.slice(0,i).join(".");
if((i==1)&&!this.moduleHasPrefix(_45)){
_43[0]="../"+_43[0];
}else{
var _46=this.getModulePrefix(_45);
if(_46!=_45){
_43.splice(0,i,_46);
break;
}
}
}
return _43;
};
dojo.hostenv._global_omit_module_check=false;
dojo.hostenv.loadModule=function(_47,_48,_49){
if(!_47){
return;
}
_49=this._global_omit_module_check||_49;
var _4a=this.findModule(_47,false);
if(_4a){
return _4a;
}
if(dj_undef(_47,this.loading_modules_)){
this.addedToLoadingCount.push(_47);
}
this.loading_modules_[_47]=1;
var _4b=_47.replace(/\./g,"/")+".js";
var _4c=_47.split(".");
var _4d=this.getModuleSymbols(_47);
var _4e=((_4d[0].charAt(0)!="/")&&!_4d[0].match(/^\w+:/));
var _4f=_4d[_4d.length-1];
var ok;
if(_4f=="*"){
_47=_4c.slice(0,-1).join(".");
while(_4d.length){
_4d.pop();
_4d.push(this.pkgFileName);
_4b=_4d.join("/")+".js";
if(_4e&&_4b.charAt(0)=="/"){
_4b=_4b.slice(1);
}
ok=this.loadPath(_4b,!_49?_47:null);
if(ok){
break;
}
_4d.pop();
}
}else{
_4b=_4d.join("/")+".js";
_47=_4c.join(".");
var _51=!_49?_47:null;
ok=this.loadPath(_4b,_51);
if(!ok&&!_48){
_4d.pop();
while(_4d.length){
_4b=_4d.join("/")+".js";
ok=this.loadPath(_4b,_51);
if(ok){
break;
}
_4d.pop();
_4b=_4d.join("/")+"/"+this.pkgFileName+".js";
if(_4e&&_4b.charAt(0)=="/"){
_4b=_4b.slice(1);
}
ok=this.loadPath(_4b,_51);
if(ok){
break;
}
}
}
if(!ok&&!_49){
dojo.raise("Could not load '"+_47+"'; last tried '"+_4b+"'");
}
}
if(!_49&&!this["isXDomain"]){
_4a=this.findModule(_47,false);
if(!_4a){
dojo.raise("symbol '"+_47+"' is not defined after loading '"+_4b+"'");
}
}
return _4a;
};
dojo.hostenv.startPackage=function(_52){
var _53=String(_52);
var _54=_53;
var _55=_52.split(/\./);
if(_55[_55.length-1]=="*"){
_55.pop();
_54=_55.join(".");
}
var _56=dojo.evalObjPath(_54,true);
this.loaded_modules_[_53]=_56;
this.loaded_modules_[_54]=_56;
return _56;
};
dojo.hostenv.findModule=function(_57,_58){
var lmn=String(_57);
if(this.loaded_modules_[lmn]){
return this.loaded_modules_[lmn];
}
if(_58){
dojo.raise("no loaded module named '"+_57+"'");
}
return null;
};
dojo.kwCompoundRequire=function(_5a){
var _5b=_5a["common"]||[];
var _5c=_5a[dojo.hostenv.name_]?_5b.concat(_5a[dojo.hostenv.name_]||[]):_5b.concat(_5a["default"]||[]);
for(var x=0;x<_5c.length;x++){
var _5e=_5c[x];
if(_5e.constructor==Array){
dojo.hostenv.loadModule.apply(dojo.hostenv,_5e);
}else{
dojo.hostenv.loadModule(_5e);
}
}
};
dojo.require=function(_5f){
dojo.hostenv.loadModule.apply(dojo.hostenv,arguments);
};
dojo.requireIf=function(_60,_61){
var _62=arguments[0];
if((_62===true)||(_62=="common")||(_62&&dojo.render[_62].capable)){
var _63=[];
for(var i=1;i<arguments.length;i++){
_63.push(arguments[i]);
}
dojo.require.apply(dojo,_63);
}
};
dojo.requireAfterIf=dojo.requireIf;
dojo.provide=function(_65){
return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);
};
dojo.registerModulePath=function(_66,_67){
return dojo.hostenv.setModulePrefix(_66,_67);
};
dojo.setModulePrefix=function(_68,_69){
dojo.deprecated("dojo.setModulePrefix(\""+_68+"\", \""+_69+"\")","replaced by dojo.registerModulePath","0.5");
return dojo.registerModulePath(_68,_69);
};
dojo.exists=function(obj,_6b){
var p=_6b.split(".");
for(var i=0;i<p.length;i++){
if(!obj[p[i]]){
return false;
}
obj=obj[p[i]];
}
return true;
};
dojo.hostenv.normalizeLocale=function(_6e){
return _6e?_6e.toLowerCase():dojo.locale;
};
dojo.hostenv.searchLocalePath=function(_6f,_70,_71){
_6f=dojo.hostenv.normalizeLocale(_6f);
var _72=_6f.split("-");
var _73=[];
for(var i=_72.length;i>0;i--){
_73.push(_72.slice(0,i).join("-"));
}
_73.push(false);
if(_70){
_73.reverse();
}
for(var j=_73.length-1;j>=0;j--){
var loc=_73[j]||"ROOT";
var _77=_71(loc);
if(_77){
break;
}
}
};
dojo.hostenv.localesGenerated;
dojo.hostenv.registerNlsPrefix=function(){
dojo.registerModulePath("nls","nls");
};
dojo.hostenv.preloadLocalizations=function(){
if(dojo.hostenv.localesGenerated){
dojo.hostenv.registerNlsPrefix();
function preload(_78){
_78=dojo.hostenv.normalizeLocale(_78);
dojo.hostenv.searchLocalePath(_78,true,function(loc){
for(var i=0;i<dojo.hostenv.localesGenerated.length;i++){
if(dojo.hostenv.localesGenerated[i]==loc){
dojo["require"]("nls.dojo_"+loc);
return true;
}
}
return false;
});
}
preload();
var _7b=djConfig.extraLocale||[];
for(var i=0;i<_7b.length;i++){
preload(_7b[i]);
}
}
dojo.hostenv.preloadLocalizations=function(){
};
};
dojo.requireLocalization=function(_7d,_7e,_7f){
dojo.hostenv.preloadLocalizations();
var _80=[_7d,"nls",_7e].join(".");
var _81=dojo.hostenv.findModule(_80);
if(_81){
if(djConfig.localizationComplete&&_81._built){
return;
}
var _82=dojo.hostenv.normalizeLocale(_7f).replace("-","_");
var _83=_80+"."+_82;
if(dojo.hostenv.findModule(_83)){
return;
}
}
_81=dojo.hostenv.startPackage(_80);
var _84=dojo.hostenv.getModuleSymbols(_7d);
var _85=_84.concat("nls").join("/");
var _86;
dojo.hostenv.searchLocalePath(_7f,false,function(loc){
var _88=loc.replace("-","_");
var _89=_80+"."+_88;
var _8a=false;
if(!dojo.hostenv.findModule(_89)){
dojo.hostenv.startPackage(_89);
var _8b=[_85];
if(loc!="ROOT"){
_8b.push(loc);
}
_8b.push(_7e);
var _8c=_8b.join("/")+".js";
_8a=dojo.hostenv.loadPath(_8c,null,function(_8d){
var _8e=function(){
};
_8e.prototype=_86;
_81[_88]=new _8e();
for(var j in _8d){
_81[_88][j]=_8d[j];
}
});
}else{
_8a=true;
}
if(_8a&&_81[_88]){
_86=_81[_88];
}else{
_81[_88]=_86;
}
});
};
(function(){
var _90=djConfig.extraLocale;
if(_90){
if(!_90 instanceof Array){
_90=[_90];
}
var req=dojo.requireLocalization;
dojo.requireLocalization=function(m,b,_94){
req(m,b,_94);
if(_94){
return;
}
for(var i=0;i<_90.length;i++){
req(m,b,_90[i]);
}
};
}
})();
}
if(typeof window!="undefined"){
(function(){
if(djConfig.allowQueryConfig){
var _96=document.location.toString();
var _97=_96.split("?",2);
if(_97.length>1){
var _98=_97[1];
var _99=_98.split("&");
for(var x in _99){
var sp=_99[x].split("=");
if((sp[0].length>9)&&(sp[0].substr(0,9)=="djConfig.")){
var opt=sp[0].substr(9);
try{
djConfig[opt]=eval(sp[1]);
}
catch(e){
djConfig[opt]=sp[1];
}
}
}
}
}
if(((djConfig["baseScriptUri"]=="")||(djConfig["baseRelativePath"]==""))&&(document&&document.getElementsByTagName)){
var _9d=document.getElementsByTagName("script");
var _9e=/(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;
for(var i=0;i<_9d.length;i++){
var src=_9d[i].getAttribute("src");
if(!src){
continue;
}
var m=src.match(_9e);
if(m){
var _a2=src.substring(0,m.index);
if(src.indexOf("bootstrap1")>-1){
_a2+="../";
}
if(!this["djConfig"]){
djConfig={};
}
if(djConfig["baseScriptUri"]==""){
djConfig["baseScriptUri"]=_a2;
}
if(djConfig["baseRelativePath"]==""){
djConfig["baseRelativePath"]=_a2;
}
break;
}
}
}
var dr=dojo.render;
var drh=dojo.render.html;
var drs=dojo.render.svg;
var dua=(drh.UA=navigator.userAgent);
var dav=(drh.AV=navigator.appVersion);
var t=true;
var f=false;
drh.capable=t;
drh.support.builtin=t;
dr.ver=parseFloat(drh.AV);
dr.os.mac=dav.indexOf("Macintosh")>=0;
dr.os.win=dav.indexOf("Windows")>=0;
dr.os.linux=dav.indexOf("X11")>=0;
drh.opera=dua.indexOf("Opera")>=0;
drh.khtml=(dav.indexOf("Konqueror")>=0)||(dav.indexOf("Safari")>=0);
drh.safari=dav.indexOf("Safari")>=0;
var _aa=dua.indexOf("Gecko");
drh.mozilla=drh.moz=(_aa>=0)&&(!drh.khtml);
if(drh.mozilla){
drh.geckoVersion=dua.substring(_aa+6,_aa+14);
}
drh.ie=(document.all)&&(!drh.opera);
drh.ie50=drh.ie&&dav.indexOf("MSIE 5.0")>=0;
drh.ie55=drh.ie&&dav.indexOf("MSIE 5.5")>=0;
drh.ie60=drh.ie&&dav.indexOf("MSIE 6.0")>=0;
drh.ie70=drh.ie&&dav.indexOf("MSIE 7.0")>=0;
var cm=document["compatMode"];
drh.quirks=(cm=="BackCompat")||(cm=="QuirksMode")||drh.ie55||drh.ie50;
dojo.locale=dojo.locale||(drh.ie?navigator.userLanguage:navigator.language).toLowerCase();
dr.vml.capable=drh.ie;
drs.capable=f;
drs.support.plugin=f;
drs.support.builtin=f;
var _ac=window["document"];
var tdi=_ac["implementation"];
if((tdi)&&(tdi["hasFeature"])&&(tdi.hasFeature("org.w3c.dom.svg","1.0"))){
drs.capable=t;
drs.support.builtin=t;
drs.support.plugin=f;
}
if(drh.safari){
var tmp=dua.split("AppleWebKit/")[1];
var ver=parseFloat(tmp.split(" ")[0]);
if(ver>=420){
drs.capable=t;
drs.support.builtin=t;
drs.support.plugin=f;
}
}else{
}
})();
dojo.hostenv.startPackage("dojo.hostenv");
dojo.render.name=dojo.hostenv.name_="browser";
dojo.hostenv.searchIds=[];
dojo.hostenv._XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];
dojo.hostenv.getXmlhttpObject=function(){
var _b0=null;
var _b1=null;
try{
_b0=new XMLHttpRequest();
}
catch(e){
}
if(!_b0){
for(var i=0;i<3;++i){
var _b3=dojo.hostenv._XMLHTTP_PROGIDS[i];
try{
_b0=new ActiveXObject(_b3);
}
catch(e){
_b1=e;
}
if(_b0){
dojo.hostenv._XMLHTTP_PROGIDS=[_b3];
break;
}
}
}
if(!_b0){
return dojo.raise("XMLHTTP not available",_b1);
}
return _b0;
};
dojo.hostenv._blockAsync=false;
dojo.hostenv.getText=function(uri,_b5,_b6){
if(!_b5){
this._blockAsync=true;
}
var _b7=this.getXmlhttpObject();
function isDocumentOk(_b8){
var _b9=_b8["status"];
return Boolean((!_b9)||((200<=_b9)&&(300>_b9))||(_b9==304));
}
if(_b5){
var _ba=this,_bb=null,gbl=dojo.global();
var xhr=dojo.evalObjPath("dojo.io.XMLHTTPTransport");
_b7.onreadystatechange=function(){
if(_bb){
gbl.clearTimeout(_bb);
_bb=null;
}
if(_ba._blockAsync||(xhr&&xhr._blockAsync)){
_bb=gbl.setTimeout(function(){
_b7.onreadystatechange.apply(this);
},10);
}else{
if(4==_b7.readyState){
if(isDocumentOk(_b7)){
_b5(_b7.responseText);
}
}
}
};
}
_b7.open("GET",uri,_b5?true:false);
try{
_b7.send(null);
if(_b5){
return null;
}
if(!isDocumentOk(_b7)){
var err=Error("Unable to load "+uri+" status:"+_b7.status);
err.status=_b7.status;
err.responseText=_b7.responseText;
throw err;
}
}
catch(e){
this._blockAsync=false;
if((_b6)&&(!_b5)){
return null;
}else{
throw e;
}
}
this._blockAsync=false;
return _b7.responseText;
};
dojo.hostenv.defaultDebugContainerId="dojoDebug";
dojo.hostenv._println_buffer=[];
dojo.hostenv._println_safe=false;
dojo.hostenv.println=function(_bf){
if(!dojo.hostenv._println_safe){
dojo.hostenv._println_buffer.push(_bf);
}else{
try{
var _c0=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);
if(!_c0){
_c0=dojo.body();
}
var div=document.createElement("div");
div.appendChild(document.createTextNode(_bf));
_c0.appendChild(div);
}
catch(e){
try{
document.write("<div>"+_bf+"</div>");
}
catch(e2){
window.status=_bf;
}
}
}
};
dojo.addOnLoad(function(){
dojo.hostenv._println_safe=true;
while(dojo.hostenv._println_buffer.length>0){
dojo.hostenv.println(dojo.hostenv._println_buffer.shift());
}
});
function dj_addNodeEvtHdlr(_c2,_c3,fp){
var _c5=_c2["on"+_c3]||function(){
};
_c2["on"+_c3]=function(){
fp.apply(_c2,arguments);
_c5.apply(_c2,arguments);
};
return true;
}
function dj_load_init(e){
var _c7=(e&&e.type)?e.type.toLowerCase():"load";
if(arguments.callee.initialized||(_c7!="domcontentloaded"&&_c7!="load")){
return;
}
arguments.callee.initialized=true;
if(typeof (_timer)!="undefined"){
clearInterval(_timer);
delete _timer;
}
var _c8=function(){
if(dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
};
if(dojo.hostenv.inFlightCount==0){
_c8();
dojo.hostenv.modulesLoaded();
}else{
dojo.hostenv.modulesLoadedListeners.unshift(_c8);
}
}
if(document.addEventListener){
if(dojo.render.html.opera||(dojo.render.html.moz&&!djConfig.delayMozLoadingFix)){
document.addEventListener("DOMContentLoaded",dj_load_init,null);
}
window.addEventListener("load",dj_load_init,null);
}
if(dojo.render.html.ie&&dojo.render.os.win){
document.attachEvent("onreadystatechange",function(e){
if(document.readyState=="complete"){
dj_load_init();
}
});
}
if(/(WebKit|khtml)/i.test(navigator.userAgent)){
var _timer=setInterval(function(){
if(/loaded|complete/.test(document.readyState)){
dj_load_init();
}
},10);
}
if(dojo.render.html.ie){
dj_addNodeEvtHdlr(window,"beforeunload",function(){
dojo.hostenv._unloading=true;
window.setTimeout(function(){
dojo.hostenv._unloading=false;
},0);
});
}
dj_addNodeEvtHdlr(window,"unload",function(){
dojo.hostenv.unloaded();
if((!dojo.render.html.ie)||(dojo.render.html.ie&&dojo.hostenv._unloading)){
dojo.hostenv.unloaded();
}
});
dojo.hostenv.makeWidgets=function(){
var _ca=[];
if(djConfig.searchIds&&djConfig.searchIds.length>0){
_ca=_ca.concat(djConfig.searchIds);
}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){
_ca=_ca.concat(dojo.hostenv.searchIds);
}
if((djConfig.parseWidgets)||(_ca.length>0)){
if(dojo.evalObjPath("dojo.widget.Parse")){
var _cb=new dojo.xml.Parse();
if(_ca.length>0){
for(var x=0;x<_ca.length;x++){
var _cd=document.getElementById(_ca[x]);
if(!_cd){
continue;
}
var _ce=_cb.parseElement(_cd,null,true);
dojo.widget.getParser().createComponents(_ce);
}
}else{
if(djConfig.parseWidgets){
var _ce=_cb.parseElement(dojo.body(),null,true);
dojo.widget.getParser().createComponents(_ce);
}
}
}
}
};
dojo.addOnLoad(function(){
if(!dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
});
try{
if(dojo.render.html.ie){
document.namespaces.add("v","urn:schemas-microsoft-com:vml");
document.createStyleSheet().addRule("v\\:*","behavior:url(#default#VML)");
}
}
catch(e){
}
dojo.hostenv.writeIncludes=function(){
};
if(!dj_undef("document",this)){
dj_currentDocument=this.document;
}
dojo.doc=function(){
return dj_currentDocument;
};
dojo.body=function(){
return dojo.doc().body||dojo.doc().getElementsByTagName("body")[0];
};
dojo.byId=function(id,doc){
if((id)&&((typeof id=="string")||(id instanceof String))){
if(!doc){
doc=dj_currentDocument;
}
var ele=doc.getElementById(id);
if(ele&&(ele.id!=id)&&doc.all){
ele=null;
eles=doc.all[id];
if(eles){
if(eles.length){
for(var i=0;i<eles.length;i++){
if(eles[i].id==id){
ele=eles[i];
break;
}
}
}else{
ele=eles;
}
}
}
return ele;
}
return id;
};
dojo.setContext=function(_d3,_d4){
dj_currentContext=_d3;
dj_currentDocument=_d4;
};
dojo._fireCallback=function(_d5,_d6,_d7){
if((_d6)&&((typeof _d5=="string")||(_d5 instanceof String))){
_d5=_d6[_d5];
}
return (_d6?_d5.apply(_d6,_d7||[]):_d5());
};
dojo.withGlobal=function(_d8,_d9,_da,_db){
var _dc;
var _dd=dj_currentContext;
var _de=dj_currentDocument;
try{
dojo.setContext(_d8,_d8.document);
_dc=dojo._fireCallback(_d9,_da,_db);
}
finally{
dojo.setContext(_dd,_de);
}
return _dc;
};
dojo.withDoc=function(_df,_e0,_e1,_e2){
var _e3;
var _e4=dj_currentDocument;
try{
dj_currentDocument=_df;
_e3=dojo._fireCallback(_e0,_e1,_e2);
}
finally{
dj_currentDocument=_e4;
}
return _e3;
};
}
(function(){
if(typeof dj_usingBootstrap!="undefined"){
return;
}
var _e5=false;
var _e6=false;
var _e7=false;
if((typeof this["load"]=="function")&&((typeof this["Packages"]=="function")||(typeof this["Packages"]=="object"))){
_e5=true;
}else{
if(typeof this["load"]=="function"){
_e6=true;
}else{
if(window.widget){
_e7=true;
}
}
}
var _e8=[];
if((this["djConfig"])&&((djConfig["isDebug"])||(djConfig["debugAtAllCosts"]))){
_e8.push("debug.js");
}
if((this["djConfig"])&&(djConfig["debugAtAllCosts"])&&(!_e5)&&(!_e7)){
_e8.push("browser_debug.js");
}
var _e9=djConfig["baseScriptUri"];
if((this["djConfig"])&&(djConfig["baseLoaderUri"])){
_e9=djConfig["baseLoaderUri"];
}
for(var x=0;x<_e8.length;x++){
var _eb=_e9+"src/"+_e8[x];
if(_e5||_e6){
load(_eb);
}else{
try{
document.write("<scr"+"ipt type='text/javascript' src='"+_eb+"'></scr"+"ipt>");
}
catch(e){
var _ec=document.createElement("script");
_ec.src=_eb;
document.getElementsByTagName("head")[0].appendChild(_ec);
}
}
}
})();
dojo.provide("dojo.string.common");
dojo.string.trim=function(str,wh){
if(!str.replace){
return str;
}
if(!str.length){
return str;
}
var re=(wh>0)?(/^\s+/):(wh<0)?(/\s+$/):(/^\s+|\s+$/g);
return str.replace(re,"");
};
dojo.string.trimStart=function(str){
return dojo.string.trim(str,1);
};
dojo.string.trimEnd=function(str){
return dojo.string.trim(str,-1);
};
dojo.string.repeat=function(str,_f3,_f4){
var out="";
for(var i=0;i<_f3;i++){
out+=str;
if(_f4&&i<_f3-1){
out+=_f4;
}
}
return out;
};
dojo.string.pad=function(str,len,c,dir){
var out=String(str);
if(!c){
c="0";
}
if(!dir){
dir=1;
}
while(out.length<len){
if(dir>0){
out=c+out;
}else{
out+=c;
}
}
return out;
};
dojo.string.padLeft=function(str,len,c){
return dojo.string.pad(str,len,c,1);
};
dojo.string.padRight=function(str,len,c){
return dojo.string.pad(str,len,c,-1);
};
dojo.provide("dojo.string");
dojo.provide("dojo.lang.common");
dojo.lang.inherits=function(_102,_103){
if(typeof _103!="function"){
dojo.raise("dojo.inherits: superclass argument ["+_103+"] must be a function (subclass: ["+_102+"']");
}
_102.prototype=new _103();
_102.prototype.constructor=_102;
_102.superclass=_103.prototype;
_102["super"]=_103.prototype;
};
dojo.lang._mixin=function(obj,_105){
var tobj={};
for(var x in _105){
if((typeof tobj[x]=="undefined")||(tobj[x]!=_105[x])){
obj[x]=_105[x];
}
}
if(dojo.render.html.ie&&(typeof (_105["toString"])=="function")&&(_105["toString"]!=obj["toString"])&&(_105["toString"]!=tobj["toString"])){
obj.toString=_105.toString;
}
return obj;
};
dojo.lang.mixin=function(obj,_109){
for(var i=1,l=arguments.length;i<l;i++){
dojo.lang._mixin(obj,arguments[i]);
}
return obj;
};
dojo.lang.extend=function(_10c,_10d){
for(var i=1,l=arguments.length;i<l;i++){
dojo.lang._mixin(_10c.prototype,arguments[i]);
}
return _10c;
};
dojo.inherits=dojo.lang.inherits;
dojo.mixin=dojo.lang.mixin;
dojo.extend=dojo.lang.extend;
dojo.lang.find=function(_110,_111,_112,_113){
if(!dojo.lang.isArrayLike(_110)&&dojo.lang.isArrayLike(_111)){
dojo.deprecated("dojo.lang.find(value, array)","use dojo.lang.find(array, value) instead","0.5");
var temp=_110;
_110=_111;
_111=temp;
}
var _115=dojo.lang.isString(_110);
if(_115){
_110=_110.split("");
}
if(_113){
var step=-1;
var i=_110.length-1;
var end=-1;
}else{
var step=1;
var i=0;
var end=_110.length;
}
if(_112){
while(i!=end){
if(_110[i]===_111){
return i;
}
i+=step;
}
}else{
while(i!=end){
if(_110[i]==_111){
return i;
}
i+=step;
}
}
return -1;
};
dojo.lang.indexOf=dojo.lang.find;
dojo.lang.findLast=function(_119,_11a,_11b){
return dojo.lang.find(_119,_11a,_11b,true);
};
dojo.lang.lastIndexOf=dojo.lang.findLast;
dojo.lang.inArray=function(_11c,_11d){
return dojo.lang.find(_11c,_11d)>-1;
};
dojo.lang.isObject=function(it){
if(typeof it=="undefined"){
return false;
}
return (typeof it=="object"||it===null||dojo.lang.isArray(it)||dojo.lang.isFunction(it));
};
dojo.lang.isArray=function(it){
return (it&&it instanceof Array||typeof it=="array");
};
dojo.lang.isArrayLike=function(it){
if((!it)||(dojo.lang.isUndefined(it))){
return false;
}
if(dojo.lang.isString(it)){
return false;
}
if(dojo.lang.isFunction(it)){
return false;
}
if(dojo.lang.isArray(it)){
return true;
}
if((it.tagName)&&(it.tagName.toLowerCase()=="form")){
return false;
}
if(dojo.lang.isNumber(it.length)&&isFinite(it.length)){
return true;
}
return false;
};
dojo.lang.isFunction=function(it){
if(!it){
return false;
}
if((typeof (it)=="function")&&(it=="[object NodeList]")){
return false;
}
return (it instanceof Function||typeof it=="function");
};
dojo.lang.isString=function(it){
return (typeof it=="string"||it instanceof String);
};
dojo.lang.isAlien=function(it){
if(!it){
return false;
}
return !dojo.lang.isFunction()&&/\{\s*\[native code\]\s*\}/.test(String(it));
};
dojo.lang.isBoolean=function(it){
return (it instanceof Boolean||typeof it=="boolean");
};
dojo.lang.isNumber=function(it){
return (it instanceof Number||typeof it=="number");
};
dojo.lang.isUndefined=function(it){
return ((typeof (it)=="undefined")&&(it==undefined));
};
dojo.provide("dojo.lang.extras");
dojo.lang.setTimeout=function(func,_128){
var _129=window,_12a=2;
if(!dojo.lang.isFunction(func)){
_129=func;
func=_128;
_128=arguments[2];
_12a++;
}
if(dojo.lang.isString(func)){
func=_129[func];
}
var args=[];
for(var i=_12a;i<arguments.length;i++){
args.push(arguments[i]);
}
return dojo.global().setTimeout(function(){
func.apply(_129,args);
},_128);
};
dojo.lang.clearTimeout=function(_12d){
dojo.global().clearTimeout(_12d);
};
dojo.lang.getNameInObj=function(ns,item){
if(!ns){
ns=dj_global;
}
for(var x in ns){
if(ns[x]===item){
return new String(x);
}
}
return null;
};
dojo.lang.shallowCopy=function(obj,deep){
var i,ret;
if(obj===null){
return null;
}
if(dojo.lang.isObject(obj)){
ret=new obj.constructor();
for(i in obj){
if(dojo.lang.isUndefined(ret[i])){
ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];
}
}
}else{
if(dojo.lang.isArray(obj)){
ret=[];
for(i=0;i<obj.length;i++){
ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];
}
}else{
ret=obj;
}
}
return ret;
};
dojo.lang.firstValued=function(){
for(var i=0;i<arguments.length;i++){
if(typeof arguments[i]!="undefined"){
return arguments[i];
}
}
return undefined;
};
dojo.lang.getObjPathValue=function(_136,_137,_138){
with(dojo.parseObjPath(_136,_137,_138)){
return dojo.evalProp(prop,obj,_138);
}
};
dojo.lang.setObjPathValue=function(_139,_13a,_13b,_13c){
if(arguments.length<4){
_13c=true;
}
with(dojo.parseObjPath(_139,_13b,_13c)){
if(obj&&(_13c||(prop in obj))){
obj[prop]=_13a;
}
}
};
dojo.provide("dojo.io.common");
dojo.io.transports=[];
dojo.io.hdlrFuncNames=["load","error","timeout"];
dojo.io.Request=function(url,_13e,_13f,_140){
if((arguments.length==1)&&(arguments[0].constructor==Object)){
this.fromKwArgs(arguments[0]);
}else{
this.url=url;
if(_13e){
this.mimetype=_13e;
}
if(_13f){
this.transport=_13f;
}
if(arguments.length>=4){
this.changeUrl=_140;
}
}
};
dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,load:function(type,data,_143,_144){
},error:function(type,_146,_147,_148){
},timeout:function(type,_14a,_14b,_14c){
},handle:function(type,data,_14f,_150){
},timeoutSeconds:0,abort:function(){
},fromKwArgs:function(_151){
if(_151["url"]){
_151.url=_151.url.toString();
}
if(_151["formNode"]){
_151.formNode=dojo.byId(_151.formNode);
}
if(!_151["method"]&&_151["formNode"]&&_151["formNode"].method){
_151.method=_151["formNode"].method;
}
if(!_151["handle"]&&_151["handler"]){
_151.handle=_151.handler;
}
if(!_151["load"]&&_151["loaded"]){
_151.load=_151.loaded;
}
if(!_151["changeUrl"]&&_151["changeURL"]){
_151.changeUrl=_151.changeURL;
}
_151.encoding=dojo.lang.firstValued(_151["encoding"],djConfig["bindEncoding"],"");
_151.sendTransport=dojo.lang.firstValued(_151["sendTransport"],djConfig["ioSendTransport"],false);
var _152=dojo.lang.isFunction;
for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){
var fn=dojo.io.hdlrFuncNames[x];
if(_151[fn]&&_152(_151[fn])){
continue;
}
if(_151["handle"]&&_152(_151["handle"])){
_151[fn]=_151.handle;
}
}
dojo.lang.mixin(this,_151);
}});
dojo.io.Error=function(msg,type,num){
this.message=msg;
this.type=type||"unknown";
this.number=num||0;
};
dojo.io.transports.addTransport=function(name){
this.push(name);
this[name]=dojo.io[name];
};
dojo.io.bind=function(_159){
if(!(_159 instanceof dojo.io.Request)){
try{
_159=new dojo.io.Request(_159);
}
catch(e){
dojo.debug(e);
}
}
var _15a="";
if(_159["transport"]){
_15a=_159["transport"];
if(!this[_15a]){
dojo.io.sendBindError(_159,"No dojo.io.bind() transport with name '"+_159["transport"]+"'.");
return _159;
}
if(!this[_15a].canHandle(_159)){
dojo.io.sendBindError(_159,"dojo.io.bind() transport with name '"+_159["transport"]+"' cannot handle this type of request.");
return _159;
}
}else{
for(var x=0;x<dojo.io.transports.length;x++){
var tmp=dojo.io.transports[x];
if((this[tmp])&&(this[tmp].canHandle(_159))){
_15a=tmp;
break;
}
}
if(_15a==""){
dojo.io.sendBindError(_159,"None of the loaded transports for dojo.io.bind()"+" can handle the request.");
return _159;
}
}
this[_15a].bind(_159);
_159.bindSuccess=true;
return _159;
};
dojo.io.sendBindError=function(_15d,_15e){
if((typeof _15d.error=="function"||typeof _15d.handle=="function")&&(typeof setTimeout=="function"||typeof setTimeout=="object")){
var _15f=new dojo.io.Error(_15e);
setTimeout(function(){
_15d[(typeof _15d.error=="function")?"error":"handle"]("error",_15f,null,_15d);
},50);
}else{
dojo.raise(_15e);
}
};
dojo.io.queueBind=function(_160){
if(!(_160 instanceof dojo.io.Request)){
try{
_160=new dojo.io.Request(_160);
}
catch(e){
dojo.debug(e);
}
}
var _161=_160.load;
_160.load=function(){
dojo.io._queueBindInFlight=false;
var ret=_161.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
var _163=_160.error;
_160.error=function(){
dojo.io._queueBindInFlight=false;
var ret=_163.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
dojo.io._bindQueue.push(_160);
dojo.io._dispatchNextQueueBind();
return _160;
};
dojo.io._dispatchNextQueueBind=function(){
if(!dojo.io._queueBindInFlight){
dojo.io._queueBindInFlight=true;
if(dojo.io._bindQueue.length>0){
dojo.io.bind(dojo.io._bindQueue.shift());
}else{
dojo.io._queueBindInFlight=false;
}
}
};
dojo.io._bindQueue=[];
dojo.io._queueBindInFlight=false;
dojo.io.argsFromMap=function(map,_166,last){
var enc=/utf/i.test(_166||"")?encodeURIComponent:dojo.string.encodeAscii;
var _169=[];
var _16a=new Object();
for(var name in map){
var _16c=function(elt){
var val=enc(name)+"="+enc(elt);
_169[(last==name)?"push":"unshift"](val);
};
if(!_16a[name]){
var _16f=map[name];
if(dojo.lang.isArray(_16f)){
dojo.lang.forEach(_16f,_16c);
}else{
_16c(_16f);
}
}
}
return _169.join("&");
};
dojo.io.setIFrameSrc=function(_170,src,_172){
try{
var r=dojo.render.html;
if(!_172){
if(r.safari){
_170.location=src;
}else{
frames[_170.name].location=src;
}
}else{
var idoc;
if(r.ie){
idoc=_170.contentWindow.document;
}else{
if(r.safari){
idoc=_170.document;
}else{
idoc=_170.contentWindow;
}
}
if(!idoc){
_170.location=src;
return;
}else{
idoc.location.replace(src);
}
}
}
catch(e){
dojo.debug(e);
dojo.debug("setIFrameSrc: "+e);
}
};
dojo.provide("dojo.lang.array");
dojo.lang.has=function(obj,name){
try{
return typeof obj[name]!="undefined";
}
catch(e){
return false;
}
};
dojo.lang.isEmpty=function(obj){
if(dojo.lang.isObject(obj)){
var tmp={};
var _179=0;
for(var x in obj){
if(obj[x]&&(!tmp[x])){
_179++;
break;
}
}
return _179==0;
}else{
if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){
return obj.length==0;
}
}
};
dojo.lang.map=function(arr,obj,_17d){
var _17e=dojo.lang.isString(arr);
if(_17e){
arr=arr.split("");
}
if(dojo.lang.isFunction(obj)&&(!_17d)){
_17d=obj;
obj=dj_global;
}else{
if(dojo.lang.isFunction(obj)&&_17d){
var _17f=obj;
obj=_17d;
_17d=_17f;
}
}
if(Array.map){
var _180=Array.map(arr,_17d,obj);
}else{
var _180=[];
for(var i=0;i<arr.length;++i){
_180.push(_17d.call(obj,arr[i]));
}
}
if(_17e){
return _180.join("");
}else{
return _180;
}
};
dojo.lang.reduce=function(arr,_183,obj,_185){
var _186=_183;
var ob=obj?obj:dj_global;
dojo.lang.map(arr,function(val){
_186=_185.call(ob,_186,val);
});
return _186;
};
dojo.lang.forEach=function(_189,_18a,_18b){
if(dojo.lang.isString(_189)){
_189=_189.split("");
}
if(Array.forEach){
Array.forEach(_189,_18a,_18b);
}else{
if(!_18b){
_18b=dj_global;
}
for(var i=0,l=_189.length;i<l;i++){
_18a.call(_18b,_189[i],i,_189);
}
}
};
dojo.lang._everyOrSome=function(_18e,arr,_190,_191){
if(dojo.lang.isString(arr)){
arr=arr.split("");
}
if(Array.every){
return Array[_18e?"every":"some"](arr,_190,_191);
}else{
if(!_191){
_191=dj_global;
}
for(var i=0,l=arr.length;i<l;i++){
var _194=_190.call(_191,arr[i],i,arr);
if(_18e&&!_194){
return false;
}else{
if((!_18e)&&(_194)){
return true;
}
}
}
return Boolean(_18e);
}
};
dojo.lang.every=function(arr,_196,_197){
return this._everyOrSome(true,arr,_196,_197);
};
dojo.lang.some=function(arr,_199,_19a){
return this._everyOrSome(false,arr,_199,_19a);
};
dojo.lang.filter=function(arr,_19c,_19d){
var _19e=dojo.lang.isString(arr);
if(_19e){
arr=arr.split("");
}
var _19f;
if(Array.filter){
_19f=Array.filter(arr,_19c,_19d);
}else{
if(!_19d){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_19d=dj_global;
}
_19f=[];
for(var i=0;i<arr.length;i++){
if(_19c.call(_19d,arr[i],i,arr)){
_19f.push(arr[i]);
}
}
}
if(_19e){
return _19f.join("");
}else{
return _19f;
}
};
dojo.lang.unnest=function(){
var out=[];
for(var i=0;i<arguments.length;i++){
if(dojo.lang.isArrayLike(arguments[i])){
var add=dojo.lang.unnest.apply(this,arguments[i]);
out=out.concat(add);
}else{
out.push(arguments[i]);
}
}
return out;
};
dojo.lang.toArray=function(_1a4,_1a5){
var _1a6=[];
for(var i=_1a5||0;i<_1a4.length;i++){
_1a6.push(_1a4[i]);
}
return _1a6;
};
dojo.provide("dojo.lang.func");
dojo.lang.hitch=function(_1a8,_1a9){
var fcn=(dojo.lang.isString(_1a9)?_1a8[_1a9]:_1a9)||function(){
};
return function(){
return fcn.apply(_1a8,arguments);
};
};
dojo.lang.anonCtr=0;
dojo.lang.anon={};
dojo.lang.nameAnonFunc=function(_1ab,_1ac,_1ad){
var nso=(_1ac||dojo.lang.anon);
if((_1ad)||((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true))){
for(var x in nso){
try{
if(nso[x]===_1ab){
return x;
}
}
catch(e){
}
}
}
var ret="__"+dojo.lang.anonCtr++;
while(typeof nso[ret]!="undefined"){
ret="__"+dojo.lang.anonCtr++;
}
nso[ret]=_1ab;
return ret;
};
dojo.lang.forward=function(_1b1){
return function(){
return this[_1b1].apply(this,arguments);
};
};
dojo.lang.curry=function(ns,func){
var _1b4=[];
ns=ns||dj_global;
if(dojo.lang.isString(func)){
func=ns[func];
}
for(var x=2;x<arguments.length;x++){
_1b4.push(arguments[x]);
}
var _1b6=(func["__preJoinArity"]||func.length)-_1b4.length;
function gather(_1b7,_1b8,_1b9){
var _1ba=_1b9;
var _1bb=_1b8.slice(0);
for(var x=0;x<_1b7.length;x++){
_1bb.push(_1b7[x]);
}
_1b9=_1b9-_1b7.length;
if(_1b9<=0){
var res=func.apply(ns,_1bb);
_1b9=_1ba;
return res;
}else{
return function(){
return gather(arguments,_1bb,_1b9);
};
}
}
return gather([],_1b4,_1b6);
};
dojo.lang.curryArguments=function(ns,func,args,_1c1){
var _1c2=[];
var x=_1c1||0;
for(x=_1c1;x<args.length;x++){
_1c2.push(args[x]);
}
return dojo.lang.curry.apply(dojo.lang,[ns,func].concat(_1c2));
};
dojo.lang.tryThese=function(){
for(var x=0;x<arguments.length;x++){
try{
if(typeof arguments[x]=="function"){
var ret=(arguments[x]());
if(ret){
return ret;
}
}
}
catch(e){
dojo.debug(e);
}
}
};
dojo.lang.delayThese=function(farr,cb,_1c8,_1c9){
if(!farr.length){
if(typeof _1c9=="function"){
_1c9();
}
return;
}
if((typeof _1c8=="undefined")&&(typeof cb=="number")){
_1c8=cb;
cb=function(){
};
}else{
if(!cb){
cb=function(){
};
if(!_1c8){
_1c8=0;
}
}
}
setTimeout(function(){
(farr.shift())();
cb();
dojo.lang.delayThese(farr,cb,_1c8,_1c9);
},_1c8);
};
dojo.provide("dojo.string.extras");
dojo.string.substituteParams=function(_1ca,hash){
var map=(typeof hash=="object")?hash:dojo.lang.toArray(arguments,1);
return _1ca.replace(/\%\{(\w+)\}/g,function(_1cd,key){
if(typeof (map[key])!="undefined"&&map[key]!=null){
return map[key];
}
dojo.raise("Substitution not found: "+key);
});
};
dojo.string.capitalize=function(str){
if(!dojo.lang.isString(str)){
return "";
}
if(arguments.length==0){
str=this;
}
var _1d0=str.split(" ");
for(var i=0;i<_1d0.length;i++){
_1d0[i]=_1d0[i].charAt(0).toUpperCase()+_1d0[i].substring(1);
}
return _1d0.join(" ");
};
dojo.string.isBlank=function(str){
if(!dojo.lang.isString(str)){
return true;
}
return (dojo.string.trim(str).length==0);
};
dojo.string.encodeAscii=function(str){
if(!dojo.lang.isString(str)){
return str;
}
var ret="";
var _1d5=escape(str);
var _1d6,re=/%u([0-9A-F]{4})/i;
while((_1d6=_1d5.match(re))){
var num=Number("0x"+_1d6[1]);
var _1d9=escape("&#"+num+";");
ret+=_1d5.substring(0,_1d6.index)+_1d9;
_1d5=_1d5.substring(_1d6.index+_1d6[0].length);
}
ret+=_1d5.replace(/\+/g,"%2B");
return ret;
};
dojo.string.escape=function(type,str){
var args=dojo.lang.toArray(arguments,1);
switch(type.toLowerCase()){
case "xml":
case "html":
case "xhtml":
return dojo.string.escapeXml.apply(this,args);
case "sql":
return dojo.string.escapeSql.apply(this,args);
case "regexp":
case "regex":
return dojo.string.escapeRegExp.apply(this,args);
case "javascript":
case "jscript":
case "js":
return dojo.string.escapeJavaScript.apply(this,args);
case "ascii":
return dojo.string.encodeAscii.apply(this,args);
default:
return str;
}
};
dojo.string.escapeXml=function(str,_1de){
str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");
if(!_1de){
str=str.replace(/'/gm,"&#39;");
}
return str;
};
dojo.string.escapeSql=function(str){
return str.replace(/'/gm,"''");
};
dojo.string.escapeRegExp=function(str){
return str.replace(/\\/gm,"\\\\").replace(/([\f\b\n\t\r[\^$|?*+(){}])/gm,"\\$1");
};
dojo.string.escapeJavaScript=function(str){
return str.replace(/(["'\f\b\n\t\r])/gm,"\\$1");
};
dojo.string.escapeString=function(str){
return ("\""+str.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r");
};
dojo.string.summary=function(str,len){
if(!len||str.length<=len){
return str;
}
return str.substring(0,len).replace(/\.+$/,"")+"...";
};
dojo.string.endsWith=function(str,end,_1e7){
if(_1e7){
str=str.toLowerCase();
end=end.toLowerCase();
}
if((str.length-end.length)<0){
return false;
}
return str.lastIndexOf(end)==str.length-end.length;
};
dojo.string.endsWithAny=function(str){
for(var i=1;i<arguments.length;i++){
if(dojo.string.endsWith(str,arguments[i])){
return true;
}
}
return false;
};
dojo.string.startsWith=function(str,_1eb,_1ec){
if(_1ec){
str=str.toLowerCase();
_1eb=_1eb.toLowerCase();
}
return str.indexOf(_1eb)==0;
};
dojo.string.startsWithAny=function(str){
for(var i=1;i<arguments.length;i++){
if(dojo.string.startsWith(str,arguments[i])){
return true;
}
}
return false;
};
dojo.string.has=function(str){
for(var i=1;i<arguments.length;i++){
if(str.indexOf(arguments[i])>-1){
return true;
}
}
return false;
};
dojo.string.normalizeNewlines=function(text,_1f2){
if(_1f2=="\n"){
text=text.replace(/\r\n/g,"\n");
text=text.replace(/\r/g,"\n");
}else{
if(_1f2=="\r"){
text=text.replace(/\r\n/g,"\r");
text=text.replace(/\n/g,"\r");
}else{
text=text.replace(/([^\r])\n/g,"$1\r\n").replace(/\r([^\n])/g,"\r\n$1");
}
}
return text;
};
dojo.string.splitEscaped=function(str,_1f4){
var _1f5=[];
for(var i=0,_1f7=0;i<str.length;i++){
if(str.charAt(i)=="\\"){
i++;
continue;
}
if(str.charAt(i)==_1f4){
_1f5.push(str.substring(_1f7,i));
_1f7=i+1;
}
}
_1f5.push(str.substr(_1f7));
return _1f5;
};
dojo.provide("dojo.dom");
dojo.dom.ELEMENT_NODE=1;
dojo.dom.ATTRIBUTE_NODE=2;
dojo.dom.TEXT_NODE=3;
dojo.dom.CDATA_SECTION_NODE=4;
dojo.dom.ENTITY_REFERENCE_NODE=5;
dojo.dom.ENTITY_NODE=6;
dojo.dom.PROCESSING_INSTRUCTION_NODE=7;
dojo.dom.COMMENT_NODE=8;
dojo.dom.DOCUMENT_NODE=9;
dojo.dom.DOCUMENT_TYPE_NODE=10;
dojo.dom.DOCUMENT_FRAGMENT_NODE=11;
dojo.dom.NOTATION_NODE=12;
dojo.dom.dojoml="http://www.dojotoolkit.org/2004/dojoml";
dojo.dom.xmlns={svg:"http://www.w3.org/2000/svg",smil:"http://www.w3.org/2001/SMIL20/",mml:"http://www.w3.org/1998/Math/MathML",cml:"http://www.xml-cml.org",xlink:"http://www.w3.org/1999/xlink",xhtml:"http://www.w3.org/1999/xhtml",xul:"http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",xbl:"http://www.mozilla.org/xbl",fo:"http://www.w3.org/1999/XSL/Format",xsl:"http://www.w3.org/1999/XSL/Transform",xslt:"http://www.w3.org/1999/XSL/Transform",xi:"http://www.w3.org/2001/XInclude",xforms:"http://www.w3.org/2002/01/xforms",saxon:"http://icl.com/saxon",xalan:"http://xml.apache.org/xslt",xsd:"http://www.w3.org/2001/XMLSchema",dt:"http://www.w3.org/2001/XMLSchema-datatypes",xsi:"http://www.w3.org/2001/XMLSchema-instance",rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs:"http://www.w3.org/2000/01/rdf-schema#",dc:"http://purl.org/dc/elements/1.1/",dcq:"http://purl.org/dc/qualifiers/1.0","soap-env":"http://schemas.xmlsoap.org/soap/envelope/",wsdl:"http://schemas.xmlsoap.org/wsdl/",AdobeExtensions:"http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"};
dojo.dom.isNode=function(wh){
if(typeof Element=="function"){
try{
return wh instanceof Element;
}
catch(e){
}
}else{
return wh&&!isNaN(wh.nodeType);
}
};
dojo.dom.getUniqueId=function(){
var _1f9=dojo.doc();
do{
var id="dj_unique_"+(++arguments.callee._idIncrement);
}while(_1f9.getElementById(id));
return id;
};
dojo.dom.getUniqueId._idIncrement=0;
dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_1fb,_1fc){
var node=_1fb.firstChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.nextSibling;
}
if(_1fc&&node&&node.tagName&&node.tagName.toLowerCase()!=_1fc.toLowerCase()){
node=dojo.dom.nextElement(node,_1fc);
}
return node;
};
dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_1fe,_1ff){
var node=_1fe.lastChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.previousSibling;
}
if(_1ff&&node&&node.tagName&&node.tagName.toLowerCase()!=_1ff.toLowerCase()){
node=dojo.dom.prevElement(node,_1ff);
}
return node;
};
dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_202){
if(!node){
return null;
}
do{
node=node.nextSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_202&&_202.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.nextElement(node,_202);
}
return node;
};
dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_204){
if(!node){
return null;
}
if(_204){
_204=_204.toLowerCase();
}
do{
node=node.previousSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_204&&_204.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.prevElement(node,_204);
}
return node;
};
dojo.dom.moveChildren=function(_205,_206,trim){
var _208=0;
if(trim){
while(_205.hasChildNodes()&&_205.firstChild.nodeType==dojo.dom.TEXT_NODE){
_205.removeChild(_205.firstChild);
}
while(_205.hasChildNodes()&&_205.lastChild.nodeType==dojo.dom.TEXT_NODE){
_205.removeChild(_205.lastChild);
}
}
while(_205.hasChildNodes()){
_206.appendChild(_205.firstChild);
_208++;
}
return _208;
};
dojo.dom.copyChildren=function(_209,_20a,trim){
var _20c=_209.cloneNode(true);
return this.moveChildren(_20c,_20a,trim);
};
dojo.dom.replaceChildren=function(node,_20e){
dojo.dom.removeChildren(node);
node.appendChild(_20e);
};
dojo.dom.removeChildren=function(node){
var _210=node.childNodes.length;
while(node.hasChildNodes()){
dojo.dom.removeNode(node.firstChild);
}
return _210;
};
dojo.dom.replaceNode=function(node,_212){
if(dojo.render.html.ie){
node.parentNode.insertBefore(_212,node);
return dojo.dom.removeNode(node);
}else{
return node.parentNode.replaceChild(_212,node);
}
};
dojo.dom._ieRemovedNodes=[];
dojo.dom.removeNode=function(node,_214){
if(node&&node.parentNode){
try{
if(_214&&dojo.evalObjPath("dojo.event.browser.clean",false)){
dojo.event.browser.clean(node);
}
}
catch(e){
}
if(dojo.render.html.ie){
if(_214){
dojo.dom._discardElement(node);
}else{
dojo.dom._ieRemovedNodes.push(node);
}
}
if(_214){
return null;
}
return node.parentNode.removeChild(node);
}
};
dojo.dom._discardElement=function(_215){
var _216=document.getElementById("IELeakGarbageBin");
if(!_216){
_216=document.createElement("DIV");
_216.id="IELeakGarbageBin";
_216.style.display="none";
document.body.appendChild(_216);
}
_216.appendChild(_215);
_216.innerHTML="";
};
dojo.dom.getAncestors=function(node,_218,_219){
var _21a=[];
var _21b=(_218&&(_218 instanceof Function||typeof _218=="function"));
while(node){
if(!_21b||_218(node)){
_21a.push(node);
}
if(_219&&_21a.length>0){
return _21a[0];
}
node=node.parentNode;
}
if(_219){
return null;
}
return _21a;
};
dojo.dom.getAncestorsByTag=function(node,tag,_21e){
tag=tag.toLowerCase();
return dojo.dom.getAncestors(node,function(el){
return ((el.tagName)&&(el.tagName.toLowerCase()==tag));
},_21e);
};
dojo.dom.getFirstAncestorByTag=function(node,tag){
return dojo.dom.getAncestorsByTag(node,tag,true);
};
dojo.dom.isDescendantOf=function(node,_223,_224){
if(_224&&node){
node=node.parentNode;
}
while(node){
if(node==_223){
return true;
}
node=node.parentNode;
}
return false;
};
dojo.dom.innerXML=function(node){
if(node.innerXML){
return node.innerXML;
}else{
if(node.xml){
return node.xml;
}else{
if(typeof XMLSerializer!="undefined"){
return (new XMLSerializer()).serializeToString(node);
}
}
}
};
dojo.dom.createDocument=function(){
var doc=null;
var _227=dojo.doc();
if(!dj_undef("ActiveXObject")){
var _228=["MSXML2","Microsoft","MSXML","MSXML3"];
for(var i=0;i<_228.length;i++){
try{
doc=new ActiveXObject(_228[i]+".XMLDOM");
}
catch(e){
}
if(doc){
break;
}
}
}else{
if((_227.implementation)&&(_227.implementation.createDocument)){
doc=_227.implementation.createDocument("","",null);
}
}
return doc;
};
dojo.dom.createDocumentFromText=function(str,_22b){
if(!_22b){
_22b="text/xml";
}
if(!dj_undef("DOMParser")){
var _22c=new DOMParser();
return _22c.parseFromString(str,_22b);
}else{
if(!dj_undef("ActiveXObject")){
var _22d=dojo.dom.createDocument();
if(_22d){
_22d.async=false;
_22d.loadXML(str);
return _22d;
}else{
dojo.debug("toXml didn't work?");
}
}else{
var _22e=dojo.doc();
if(_22e.createElement){
var tmp=_22e.createElement("xml");
tmp.innerHTML=str;
if(_22e.implementation&&_22e.implementation.createDocument){
var _230=_22e.implementation.createDocument("foo","",null);
for(var i=0;i<tmp.childNodes.length;i++){
_230.importNode(tmp.childNodes.item(i),true);
}
return _230;
}
return ((tmp.document)&&(tmp.document.firstChild?tmp.document.firstChild:tmp));
}
}
}
return null;
};
dojo.dom.prependChild=function(node,_233){
if(_233.firstChild){
_233.insertBefore(node,_233.firstChild);
}else{
_233.appendChild(node);
}
return true;
};
dojo.dom.insertBefore=function(node,ref,_236){
if((_236!=true)&&(node===ref||node.nextSibling===ref)){
return false;
}
var _237=ref.parentNode;
_237.insertBefore(node,ref);
return true;
};
dojo.dom.insertAfter=function(node,ref,_23a){
var pn=ref.parentNode;
if(ref==pn.lastChild){
if((_23a!=true)&&(node===ref)){
return false;
}
pn.appendChild(node);
}else{
return this.insertBefore(node,ref.nextSibling,_23a);
}
return true;
};
dojo.dom.insertAtPosition=function(node,ref,_23e){
if((!node)||(!ref)||(!_23e)){
return false;
}
switch(_23e.toLowerCase()){
case "before":
return dojo.dom.insertBefore(node,ref);
case "after":
return dojo.dom.insertAfter(node,ref);
case "first":
if(ref.firstChild){
return dojo.dom.insertBefore(node,ref.firstChild);
}else{
ref.appendChild(node);
return true;
}
break;
default:
ref.appendChild(node);
return true;
}
};
dojo.dom.insertAtIndex=function(node,_240,_241){
var _242=_240.childNodes;
if(!_242.length){
_240.appendChild(node);
return true;
}
var _243=null;
for(var i=0;i<_242.length;i++){
var _245=_242.item(i)["getAttribute"]?parseInt(_242.item(i).getAttribute("dojoinsertionindex")):-1;
if(_245<_241){
_243=_242.item(i);
}
}
if(_243){
return dojo.dom.insertAfter(node,_243);
}else{
return dojo.dom.insertBefore(node,_242.item(0));
}
};
dojo.dom.textContent=function(node,text){
if(arguments.length>1){
var _248=dojo.doc();
dojo.dom.replaceChildren(node,_248.createTextNode(text));
return text;
}else{
if(node.textContent!=undefined){
return node.textContent;
}
var _249="";
if(node==null){
return _249;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
_249+=dojo.dom.textContent(node.childNodes[i]);
break;
case 3:
case 2:
case 4:
_249+=node.childNodes[i].nodeValue;
break;
default:
break;
}
}
return _249;
}
};
dojo.dom.hasParent=function(node){
return Boolean(node&&node.parentNode&&dojo.dom.isNode(node.parentNode));
};
dojo.dom.isTag=function(node){
if(node&&node.tagName){
for(var i=1;i<arguments.length;i++){
if(node.tagName==String(arguments[i])){
return String(arguments[i]);
}
}
}
return "";
};
dojo.dom.setAttributeNS=function(elem,_24f,_250,_251){
if(elem==null||((elem==undefined)&&(typeof elem=="undefined"))){
dojo.raise("No element given to dojo.dom.setAttributeNS");
}
if(!((elem.setAttributeNS==undefined)&&(typeof elem.setAttributeNS=="undefined"))){
elem.setAttributeNS(_24f,_250,_251);
}else{
var _252=elem.ownerDocument;
var _253=_252.createNode(2,_250,_24f);
_253.nodeValue=_251;
elem.setAttributeNode(_253);
}
};
dojo.provide("dojo.undo.browser");
try{
if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){
document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"'></iframe>");
}
}
catch(e){
}
if(dojo.render.html.opera){
dojo.debug("Opera is not supported with dojo.undo.browser, so back/forward detection will not work.");
}
dojo.undo.browser={initialHref:(!dj_undef("window"))?window.location.href:"",initialHash:(!dj_undef("window"))?window.location.hash:"",moveForward:false,historyStack:[],forwardStack:[],historyIframe:null,bookmarkAnchor:null,locationTimer:null,setInitialState:function(args){
this.initialState=this._createState(this.initialHref,args,this.initialHash);
},addToHistory:function(args){
this.forwardStack=[];
var hash=null;
var url=null;
if(!this.historyIframe){
this.historyIframe=window.frames["djhistory"];
}
if(!this.bookmarkAnchor){
this.bookmarkAnchor=document.createElement("a");
dojo.body().appendChild(this.bookmarkAnchor);
this.bookmarkAnchor.style.display="none";
}
if(args["changeUrl"]){
hash="#"+((args["changeUrl"]!==true)?args["changeUrl"]:(new Date()).getTime());
if(this.historyStack.length==0&&this.initialState.urlHash==hash){
this.initialState=this._createState(url,args,hash);
return;
}else{
if(this.historyStack.length>0&&this.historyStack[this.historyStack.length-1].urlHash==hash){
this.historyStack[this.historyStack.length-1]=this._createState(url,args,hash);
return;
}
}
this.changingUrl=true;
setTimeout("window.location.href = '"+hash+"'; dojo.undo.browser.changingUrl = false;",1);
this.bookmarkAnchor.href=hash;
if(dojo.render.html.ie){
url=this._loadIframeHistory();
var _258=args["back"]||args["backButton"]||args["handle"];
var tcb=function(_25a){
if(window.location.hash!=""){
setTimeout("window.location.href = '"+hash+"';",1);
}
_258.apply(this,[_25a]);
};
if(args["back"]){
args.back=tcb;
}else{
if(args["backButton"]){
args.backButton=tcb;
}else{
if(args["handle"]){
args.handle=tcb;
}
}
}
var _25b=args["forward"]||args["forwardButton"]||args["handle"];
var tfw=function(_25d){
if(window.location.hash!=""){
window.location.href=hash;
}
if(_25b){
_25b.apply(this,[_25d]);
}
};
if(args["forward"]){
args.forward=tfw;
}else{
if(args["forwardButton"]){
args.forwardButton=tfw;
}else{
if(args["handle"]){
args.handle=tfw;
}
}
}
}else{
if(dojo.render.html.moz){
if(!this.locationTimer){
this.locationTimer=setInterval("dojo.undo.browser.checkLocation();",200);
}
}
}
}else{
url=this._loadIframeHistory();
}
this.historyStack.push(this._createState(url,args,hash));
},checkLocation:function(){
if(!this.changingUrl){
var hsl=this.historyStack.length;
if((window.location.hash==this.initialHash||window.location.href==this.initialHref)&&(hsl==1)){
this.handleBackButton();
return;
}
if(this.forwardStack.length>0){
if(this.forwardStack[this.forwardStack.length-1].urlHash==window.location.hash){
this.handleForwardButton();
return;
}
}
if((hsl>=2)&&(this.historyStack[hsl-2])){
if(this.historyStack[hsl-2].urlHash==window.location.hash){
this.handleBackButton();
return;
}
}
}
},iframeLoaded:function(evt,_260){
if(!dojo.render.html.opera){
var _261=this._getUrlQuery(_260.href);
if(_261==null){
if(this.historyStack.length==1){
this.handleBackButton();
}
return;
}
if(this.moveForward){
this.moveForward=false;
return;
}
if(this.historyStack.length>=2&&_261==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){
this.handleBackButton();
}else{
if(this.forwardStack.length>0&&_261==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){
this.handleForwardButton();
}
}
}
},handleBackButton:function(){
var _262=this.historyStack.pop();
if(!_262){
return;
}
var last=this.historyStack[this.historyStack.length-1];
if(!last&&this.historyStack.length==0){
last=this.initialState;
}
if(last){
if(last.kwArgs["back"]){
last.kwArgs["back"]();
}else{
if(last.kwArgs["backButton"]){
last.kwArgs["backButton"]();
}else{
if(last.kwArgs["handle"]){
last.kwArgs.handle("back");
}
}
}
}
this.forwardStack.push(_262);
},handleForwardButton:function(){
var last=this.forwardStack.pop();
if(!last){
return;
}
if(last.kwArgs["forward"]){
last.kwArgs.forward();
}else{
if(last.kwArgs["forwardButton"]){
last.kwArgs.forwardButton();
}else{
if(last.kwArgs["handle"]){
last.kwArgs.handle("forward");
}
}
}
this.historyStack.push(last);
},_createState:function(url,args,hash){
return {"url":url,"kwArgs":args,"urlHash":hash};
},_getUrlQuery:function(url){
var _269=url.split("?");
if(_269.length<2){
return null;
}else{
return _269[1];
}
},_loadIframeHistory:function(){
var url=dojo.hostenv.getBaseScriptUri()+"iframe_history.html?"+(new Date()).getTime();
this.moveForward=true;
dojo.io.setIFrameSrc(this.historyIframe,url,false);
return url;
}};
dojo.provide("dojo.io.BrowserIO");
if(!dj_undef("window")){
dojo.io.checkChildrenForFile=function(node){
var _26c=false;
var _26d=node.getElementsByTagName("input");
dojo.lang.forEach(_26d,function(_26e){
if(_26c){
return;
}
if(_26e.getAttribute("type")=="file"){
_26c=true;
}
});
return _26c;
};
dojo.io.formHasFile=function(_26f){
return dojo.io.checkChildrenForFile(_26f);
};
dojo.io.updateNode=function(node,_271){
node=dojo.byId(node);
var args=_271;
if(dojo.lang.isString(_271)){
args={url:_271};
}
args.mimetype="text/html";
args.load=function(t,d,e){
while(node.firstChild){
if(dojo["event"]){
try{
dojo.event.browser.clean(node.firstChild);
}
catch(e){
}
}
node.removeChild(node.firstChild);
}
node.innerHTML=d;
};
dojo.io.bind(args);
};
dojo.io.formFilter=function(node){
var type=(node.type||"").toLowerCase();
return !node.disabled&&node.name&&!dojo.lang.inArray(["file","submit","image","reset","button"],type);
};
dojo.io.encodeForm=function(_278,_279,_27a){
if((!_278)||(!_278.tagName)||(!_278.tagName.toLowerCase()=="form")){
dojo.raise("Attempted to encode a non-form element.");
}
if(!_27a){
_27a=dojo.io.formFilter;
}
var enc=/utf/i.test(_279||"")?encodeURIComponent:dojo.string.encodeAscii;
var _27c=[];
for(var i=0;i<_278.elements.length;i++){
var elm=_278.elements[i];
if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_27a(elm)){
continue;
}
var name=enc(elm.name);
var type=elm.type.toLowerCase();
if(type=="select-multiple"){
for(var j=0;j<elm.options.length;j++){
if(elm.options[j].selected){
_27c.push(name+"="+enc(elm.options[j].value));
}
}
}else{
if(dojo.lang.inArray(["radio","checkbox"],type)){
if(elm.checked){
_27c.push(name+"="+enc(elm.value));
}
}else{
_27c.push(name+"="+enc(elm.value));
}
}
}
var _282=_278.getElementsByTagName("input");
for(var i=0;i<_282.length;i++){
var _283=_282[i];
if(_283.type.toLowerCase()=="image"&&_283.form==_278&&_27a(_283)){
var name=enc(_283.name);
_27c.push(name+"="+enc(_283.value));
_27c.push(name+".x=0");
_27c.push(name+".y=0");
}
}
return _27c.join("&")+"&";
};
dojo.io.FormBind=function(args){
this.bindArgs={};
if(args&&args.formNode){
this.init(args);
}else{
if(args){
this.init({formNode:args});
}
}
};
dojo.lang.extend(dojo.io.FormBind,{form:null,bindArgs:null,clickedButton:null,init:function(args){
var form=dojo.byId(args.formNode);
if(!form||!form.tagName||form.tagName.toLowerCase()!="form"){
throw new Error("FormBind: Couldn't apply, invalid form");
}else{
if(this.form==form){
return;
}else{
if(this.form){
throw new Error("FormBind: Already applied to a form");
}
}
}
dojo.lang.mixin(this.bindArgs,args);
this.form=form;
this.connect(form,"onsubmit","submit");
for(var i=0;i<form.elements.length;i++){
var node=form.elements[i];
if(node&&node.type&&dojo.lang.inArray(["submit","button"],node.type.toLowerCase())){
this.connect(node,"onclick","click");
}
}
var _289=form.getElementsByTagName("input");
for(var i=0;i<_289.length;i++){
var _28a=_289[i];
if(_28a.type.toLowerCase()=="image"&&_28a.form==form){
this.connect(_28a,"onclick","click");
}
}
},onSubmit:function(form){
return true;
},submit:function(e){
e.preventDefault();
if(this.onSubmit(this.form)){
dojo.io.bind(dojo.lang.mixin(this.bindArgs,{formFilter:dojo.lang.hitch(this,"formFilter")}));
}
},click:function(e){
var node=e.currentTarget;
if(node.disabled){
return;
}
this.clickedButton=node;
},formFilter:function(node){
var type=(node.type||"").toLowerCase();
var _291=false;
if(node.disabled||!node.name){
_291=false;
}else{
if(dojo.lang.inArray(["submit","button","image"],type)){
if(!this.clickedButton){
this.clickedButton=node;
}
_291=node==this.clickedButton;
}else{
_291=!dojo.lang.inArray(["file","submit","reset","button"],type);
}
}
return _291;
},connect:function(_292,_293,_294){
if(dojo.evalObjPath("dojo.event.connect")){
dojo.event.connect(_292,_293,this,_294);
}else{
var fcn=dojo.lang.hitch(this,_294);
_292[_293]=function(e){
if(!e){
e=window.event;
}
if(!e.currentTarget){
e.currentTarget=e.srcElement;
}
if(!e.preventDefault){
e.preventDefault=function(){
window.event.returnValue=false;
};
}
fcn(e);
};
}
}});
dojo.io.XMLHTTPTransport=new function(){
var _297=this;
var _298={};
this.useCache=false;
this.preventCache=false;
function getCacheKey(url,_29a,_29b){
return url+"|"+_29a+"|"+_29b.toLowerCase();
}
function addToCache(url,_29d,_29e,http){
_298[getCacheKey(url,_29d,_29e)]=http;
}
function getFromCache(url,_2a1,_2a2){
return _298[getCacheKey(url,_2a1,_2a2)];
}
this.clearCache=function(){
_298={};
};
function doLoad(_2a3,http,url,_2a6,_2a7){
if(((http.status>=200)&&(http.status<300))||(http.status==304)||(location.protocol=="file:"&&(http.status==0||http.status==undefined))||(location.protocol=="chrome:"&&(http.status==0||http.status==undefined))){
var ret;
if(_2a3.method.toLowerCase()=="head"){
var _2a9=http.getAllResponseHeaders();
ret={};
ret.toString=function(){
return _2a9;
};
var _2aa=_2a9.split(/[\r\n]+/g);
for(var i=0;i<_2aa.length;i++){
var pair=_2aa[i].match(/^([^:]+)\s*:\s*(.+)$/i);
if(pair){
ret[pair[1]]=pair[2];
}
}
}else{
if(_2a3.mimetype=="text/javascript"){
try{
ret=dj_eval(http.responseText);
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=null;
}
}else{
if(_2a3.mimetype=="text/json"||_2a3.mimetype=="application/json"){
try{
ret=dj_eval("("+http.responseText+")");
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=false;
}
}else{
if((_2a3.mimetype=="application/xml")||(_2a3.mimetype=="text/xml")){
ret=http.responseXML;
if(!ret||typeof ret=="string"||!http.getResponseHeader("Content-Type")){
ret=dojo.dom.createDocumentFromText(http.responseText);
}
}else{
ret=http.responseText;
}
}
}
}
if(_2a7){
addToCache(url,_2a6,_2a3.method,http);
}
_2a3[(typeof _2a3.load=="function")?"load":"handle"]("load",ret,http,_2a3);
}else{
var _2ad=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);
_2a3[(typeof _2a3.error=="function")?"error":"handle"]("error",_2ad,http,_2a3);
}
}
function setHeaders(http,_2af){
if(_2af["headers"]){
for(var _2b0 in _2af["headers"]){
if(_2b0.toLowerCase()=="content-type"&&!_2af["contentType"]){
_2af["contentType"]=_2af["headers"][_2b0];
}else{
http.setRequestHeader(_2b0,_2af["headers"][_2b0]);
}
}
}
}
this.inFlight=[];
this.inFlightTimer=null;
this.startWatchingInFlight=function(){
if(!this.inFlightTimer){
this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);
}
};
this.watchInFlight=function(){
var now=null;
if(!dojo.hostenv._blockAsync&&!_297._blockAsync){
for(var x=this.inFlight.length-1;x>=0;x--){
try{
var tif=this.inFlight[x];
if(!tif||tif.http._aborted||!tif.http.readyState){
this.inFlight.splice(x,1);
continue;
}
if(4==tif.http.readyState){
this.inFlight.splice(x,1);
doLoad(tif.req,tif.http,tif.url,tif.query,tif.useCache);
}else{
if(tif.startTime){
if(!now){
now=(new Date()).getTime();
}
if(tif.startTime+(tif.req.timeoutSeconds*1000)<now){
if(typeof tif.http.abort=="function"){
tif.http.abort();
}
this.inFlight.splice(x,1);
tif.req[(typeof tif.req.timeout=="function")?"timeout":"handle"]("timeout",null,tif.http,tif.req);
}
}
}
}
catch(e){
try{
var _2b4=new dojo.io.Error("XMLHttpTransport.watchInFlight Error: "+e);
tif.req[(typeof tif.req.error=="function")?"error":"handle"]("error",_2b4,tif.http,tif.req);
}
catch(e2){
dojo.debug("XMLHttpTransport error callback failed: "+e2);
}
}
}
}
clearTimeout(this.inFlightTimer);
if(this.inFlight.length==0){
this.inFlightTimer=null;
return;
}
this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);
};
var _2b5=dojo.hostenv.getXmlhttpObject()?true:false;
this.canHandle=function(_2b6){
return _2b5&&dojo.lang.inArray(["text/plain","text/html","application/xml","text/xml","text/javascript","text/json","application/json"],(_2b6["mimetype"].toLowerCase()||""))&&!(_2b6["formNode"]&&dojo.io.formHasFile(_2b6["formNode"]));
};
this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";
this.bind=function(_2b7){
if(!_2b7["url"]){
if(!_2b7["formNode"]&&(_2b7["backButton"]||_2b7["back"]||_2b7["changeUrl"]||_2b7["watchForURL"])&&(!djConfig.preventBackButtonFix)){
dojo.deprecated("Using dojo.io.XMLHTTPTransport.bind() to add to browser history without doing an IO request","Use dojo.undo.browser.addToHistory() instead.","0.4");
dojo.undo.browser.addToHistory(_2b7);
return true;
}
}
var url=_2b7.url;
var _2b9="";
if(_2b7["formNode"]){
var ta=_2b7.formNode.getAttribute("action");
if((ta)&&(!_2b7["url"])){
url=ta;
}
var tp=_2b7.formNode.getAttribute("method");
if((tp)&&(!_2b7["method"])){
_2b7.method=tp;
}
_2b9+=dojo.io.encodeForm(_2b7.formNode,_2b7.encoding,_2b7["formFilter"]);
}
if(url.indexOf("#")>-1){
dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);
url=url.split("#")[0];
}
if(_2b7["file"]){
_2b7.method="post";
}
if(!_2b7["method"]){
_2b7.method="get";
}
if(_2b7.method.toLowerCase()=="get"){
_2b7.multipart=false;
}else{
if(_2b7["file"]){
_2b7.multipart=true;
}else{
if(!_2b7["multipart"]){
_2b7.multipart=false;
}
}
}
if(_2b7["backButton"]||_2b7["back"]||_2b7["changeUrl"]){
dojo.undo.browser.addToHistory(_2b7);
}
var _2bc=_2b7["content"]||{};
if(_2b7.sendTransport){
_2bc["dojo.transport"]="xmlhttp";
}
do{
if(_2b7.postContent){
_2b9=_2b7.postContent;
break;
}
if(_2bc){
_2b9+=dojo.io.argsFromMap(_2bc,_2b7.encoding);
}
if(_2b7.method.toLowerCase()=="get"||!_2b7.multipart){
break;
}
var t=[];
if(_2b9.length){
var q=_2b9.split("&");
for(var i=0;i<q.length;++i){
if(q[i].length){
var p=q[i].split("=");
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);
}
}
}
if(_2b7.file){
if(dojo.lang.isArray(_2b7.file)){
for(var i=0;i<_2b7.file.length;++i){
var o=_2b7.file[i];
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}else{
var o=_2b7.file;
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}
if(t.length){
t.push("--"+this.multipartBoundary+"--","");
_2b9=t.join("\r\n");
}
}while(false);
var _2c2=_2b7["sync"]?false:true;
var _2c3=_2b7["preventCache"]||(this.preventCache==true&&_2b7["preventCache"]!=false);
var _2c4=_2b7["useCache"]==true||(this.useCache==true&&_2b7["useCache"]!=false);
if(!_2c3&&_2c4){
var _2c5=getFromCache(url,_2b9,_2b7.method);
if(_2c5){
doLoad(_2b7,_2c5,url,_2b9,false);
return;
}
}
var http=dojo.hostenv.getXmlhttpObject(_2b7);
var _2c7=false;
if(_2c2){
var _2c8=this.inFlight.push({"req":_2b7,"http":http,"url":url,"query":_2b9,"useCache":_2c4,"startTime":_2b7.timeoutSeconds?(new Date()).getTime():0});
this.startWatchingInFlight();
}else{
_297._blockAsync=true;
}
if(_2b7.method.toLowerCase()=="post"){
if(!_2b7.user){
http.open("POST",url,_2c2);
}else{
http.open("POST",url,_2c2,_2b7.user,_2b7.password);
}
setHeaders(http,_2b7);
http.setRequestHeader("Content-Type",_2b7.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_2b7.contentType||"application/x-www-form-urlencoded"));
try{
http.send(_2b9);
}
catch(e){
if(typeof http.abort=="function"){
http.abort();
}
doLoad(_2b7,{status:404},url,_2b9,_2c4);
}
}else{
var _2c9=url;
if(_2b9!=""){
_2c9+=(_2c9.indexOf("?")>-1?"&":"?")+_2b9;
}
if(_2c3){
_2c9+=(dojo.string.endsWithAny(_2c9,"?","&")?"":(_2c9.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();
}
if(!_2b7.user){
http.open(_2b7.method.toUpperCase(),_2c9,_2c2);
}else{
http.open(_2b7.method.toUpperCase(),_2c9,_2c2,_2b7.user,_2b7.password);
}
setHeaders(http,_2b7);
try{
http.send(null);
}
catch(e){
if(typeof http.abort=="function"){
http.abort();
}
doLoad(_2b7,{status:404},url,_2b9,_2c4);
}
}
if(!_2c2){
doLoad(_2b7,http,url,_2b9,_2c4);
_297._blockAsync=false;
}
_2b7.abort=function(){
try{
http._aborted=true;
}
catch(e){
}
return http.abort();
};
return;
};
dojo.io.transports.addTransport("XMLHTTPTransport");
};
}
dojo.provide("dojo.io.cookie");
dojo.io.cookie.setCookie=function(name,_2cb,days,path,_2ce,_2cf){
var _2d0=-1;
if(typeof days=="number"&&days>=0){
var d=new Date();
d.setTime(d.getTime()+(days*24*60*60*1000));
_2d0=d.toGMTString();
}
_2cb=escape(_2cb);
document.cookie=name+"="+_2cb+";"+(_2d0!=-1?" expires="+_2d0+";":"")+(path?"path="+path:"")+(_2ce?"; domain="+_2ce:"")+(_2cf?"; secure":"");
};
dojo.io.cookie.set=dojo.io.cookie.setCookie;
dojo.io.cookie.getCookie=function(name){
var idx=document.cookie.lastIndexOf(name+"=");
if(idx==-1){
return null;
}
var _2d4=document.cookie.substring(idx+name.length+1);
var end=_2d4.indexOf(";");
if(end==-1){
end=_2d4.length;
}
_2d4=_2d4.substring(0,end);
_2d4=unescape(_2d4);
return _2d4;
};
dojo.io.cookie.get=dojo.io.cookie.getCookie;
dojo.io.cookie.deleteCookie=function(name){
dojo.io.cookie.setCookie(name,"-",0);
};
dojo.io.cookie.setObjectCookie=function(name,obj,days,path,_2db,_2dc,_2dd){
if(arguments.length==5){
_2dd=_2db;
_2db=null;
_2dc=null;
}
var _2de=[],_2df,_2e0="";
if(!_2dd){
_2df=dojo.io.cookie.getObjectCookie(name);
}
if(days>=0){
if(!_2df){
_2df={};
}
for(var prop in obj){
if(prop==null){
delete _2df[prop];
}else{
if(typeof obj[prop]=="string"||typeof obj[prop]=="number"){
_2df[prop]=obj[prop];
}
}
}
prop=null;
for(var prop in _2df){
_2de.push(escape(prop)+"="+escape(_2df[prop]));
}
_2e0=_2de.join("&");
}
dojo.io.cookie.setCookie(name,_2e0,days,path,_2db,_2dc);
};
dojo.io.cookie.getObjectCookie=function(name){
var _2e3=null,_2e4=dojo.io.cookie.getCookie(name);
if(_2e4){
_2e3={};
var _2e5=_2e4.split("&");
for(var i=0;i<_2e5.length;i++){
var pair=_2e5[i].split("=");
var _2e8=pair[1];
if(isNaN(_2e8)){
_2e8=unescape(pair[1]);
}
_2e3[unescape(pair[0])]=_2e8;
}
}
return _2e3;
};
dojo.io.cookie.isSupported=function(){
if(typeof navigator.cookieEnabled!="boolean"){
dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__","CookiesAllowed",90,null);
var _2e9=dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");
navigator.cookieEnabled=(_2e9=="CookiesAllowed");
if(navigator.cookieEnabled){
this.deleteCookie("__TestingYourBrowserForCookieSupport__");
}
}
return navigator.cookieEnabled;
};
if(!dojo.io.cookies){
dojo.io.cookies=dojo.io.cookie;
}
dojo.provide("dojo.io.*");
dojo.provide("dojo.io");
dojo.deprecated("dojo.io","replaced by dojo.io.*","0.5");
dojo.provide("dojo.event.common");
dojo.event=new function(){
this._canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);
function interpolateArgs(args,_2eb){
var dl=dojo.lang;
var ao={srcObj:dj_global,srcFunc:null,adviceObj:dj_global,adviceFunc:null,aroundObj:null,aroundFunc:null,adviceType:(args.length>2)?args[0]:"after",precedence:"last",once:false,delay:null,rate:0,adviceMsg:false};
switch(args.length){
case 0:
return;
case 1:
return;
case 2:
ao.srcFunc=args[0];
ao.adviceFunc=args[1];
break;
case 3:
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
}else{
if((dl.isString(args[1]))&&(dl.isString(args[2]))){
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
}else{
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isFunction(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
var _2ee=dl.nameAnonFunc(args[2],ao.adviceObj,_2eb);
ao.adviceFunc=_2ee;
}else{
if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=dj_global;
var _2ee=dl.nameAnonFunc(args[0],ao.srcObj,_2eb);
ao.srcFunc=_2ee;
ao.adviceObj=args[1];
ao.adviceFunc=args[2];
}
}
}
}
break;
case 4:
if((dl.isObject(args[0]))&&(dl.isObject(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isString(args[1]))&&(dl.isObject(args[2]))){
ao.adviceType=args[0];
ao.srcObj=dj_global;
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isFunction(args[1]))&&(dl.isObject(args[2]))){
ao.adviceType=args[0];
ao.srcObj=dj_global;
var _2ee=dl.nameAnonFunc(args[1],dj_global,_2eb);
ao.srcFunc=_2ee;
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){
ao.srcObj=args[1];
ao.srcFunc=args[2];
var _2ee=dl.nameAnonFunc(args[3],dj_global,_2eb);
ao.adviceObj=dj_global;
ao.adviceFunc=_2ee;
}else{
if(dl.isObject(args[1])){
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=dj_global;
ao.adviceFunc=args[3];
}else{
if(dl.isObject(args[2])){
ao.srcObj=dj_global;
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
ao.srcObj=ao.adviceObj=ao.aroundObj=dj_global;
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
ao.aroundFunc=args[3];
}
}
}
}
}
}
break;
case 6:
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=args[3];
ao.adviceFunc=args[4];
ao.aroundFunc=args[5];
ao.aroundObj=dj_global;
break;
default:
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=args[3];
ao.adviceFunc=args[4];
ao.aroundObj=args[5];
ao.aroundFunc=args[6];
ao.once=args[7];
ao.delay=args[8];
ao.rate=args[9];
ao.adviceMsg=args[10];
break;
}
if(dl.isFunction(ao.aroundFunc)){
var _2ee=dl.nameAnonFunc(ao.aroundFunc,ao.aroundObj,_2eb);
ao.aroundFunc=_2ee;
}
if(dl.isFunction(ao.srcFunc)){
ao.srcFunc=dl.getNameInObj(ao.srcObj,ao.srcFunc);
}
if(dl.isFunction(ao.adviceFunc)){
ao.adviceFunc=dl.getNameInObj(ao.adviceObj,ao.adviceFunc);
}
if((ao.aroundObj)&&(dl.isFunction(ao.aroundFunc))){
ao.aroundFunc=dl.getNameInObj(ao.aroundObj,ao.aroundFunc);
}
if(!ao.srcObj){
dojo.raise("bad srcObj for srcFunc: "+ao.srcFunc);
}
if(!ao.adviceObj){
dojo.raise("bad adviceObj for adviceFunc: "+ao.adviceFunc);
}
if(!ao.adviceFunc){
dojo.debug("bad adviceFunc for srcFunc: "+ao.srcFunc);
dojo.debugShallow(ao);
}
return ao;
}
this.connect=function(){
if(arguments.length==1){
var ao=arguments[0];
}else{
var ao=interpolateArgs(arguments,true);
}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){
if(dojo.render.html.ie){
ao.srcFunc="onkeydown";
this.connect(ao);
}
ao.srcFunc="onkeypress";
}
if(dojo.lang.isArray(ao.srcObj)&&ao.srcObj!=""){
var _2f0={};
for(var x in ao){
_2f0[x]=ao[x];
}
var mjps=[];
dojo.lang.forEach(ao.srcObj,function(src){
if((dojo.render.html.capable)&&(dojo.lang.isString(src))){
src=dojo.byId(src);
}
_2f0.srcObj=src;
mjps.push(dojo.event.connect.call(dojo.event,_2f0));
});
return mjps;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);
if(ao.adviceFunc){
var mjp2=dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj,ao.adviceFunc);
}
mjp.kwAddAdvice(ao);
return mjp;
};
this.log=function(a1,a2){
var _2f8;
if((arguments.length==1)&&(typeof a1=="object")){
_2f8=a1;
}else{
_2f8={srcObj:a1,srcFunc:a2};
}
_2f8.adviceFunc=function(){
var _2f9=[];
for(var x=0;x<arguments.length;x++){
_2f9.push(arguments[x]);
}
dojo.debug("("+_2f8.srcObj+")."+_2f8.srcFunc,":",_2f9.join(", "));
};
this.kwConnect(_2f8);
};
this.connectBefore=function(){
var args=["before"];
for(var i=0;i<arguments.length;i++){
args.push(arguments[i]);
}
return this.connect.apply(this,args);
};
this.connectAround=function(){
var args=["around"];
for(var i=0;i<arguments.length;i++){
args.push(arguments[i]);
}
return this.connect.apply(this,args);
};
this.connectOnce=function(){
var ao=interpolateArgs(arguments,true);
ao.once=true;
return this.connect(ao);
};
this._kwConnectImpl=function(_300,_301){
var fn=(_301)?"disconnect":"connect";
if(typeof _300["srcFunc"]=="function"){
_300.srcObj=_300["srcObj"]||dj_global;
var _303=dojo.lang.nameAnonFunc(_300.srcFunc,_300.srcObj,true);
_300.srcFunc=_303;
}
if(typeof _300["adviceFunc"]=="function"){
_300.adviceObj=_300["adviceObj"]||dj_global;
var _303=dojo.lang.nameAnonFunc(_300.adviceFunc,_300.adviceObj,true);
_300.adviceFunc=_303;
}
_300.srcObj=_300["srcObj"]||dj_global;
_300.adviceObj=_300["adviceObj"]||_300["targetObj"]||dj_global;
_300.adviceFunc=_300["adviceFunc"]||_300["targetFunc"];
return dojo.event[fn](_300);
};
this.kwConnect=function(_304){
return this._kwConnectImpl(_304,false);
};
this.disconnect=function(){
if(arguments.length==1){
var ao=arguments[0];
}else{
var ao=interpolateArgs(arguments,true);
}
if(!ao.adviceFunc){
return;
}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){
if(dojo.render.html.ie){
ao.srcFunc="onkeydown";
this.disconnect(ao);
}
ao.srcFunc="onkeypress";
}
if(!ao.srcObj[ao.srcFunc]){
return null;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc,true);
mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);
return mjp;
};
this.kwDisconnect=function(_307){
return this._kwConnectImpl(_307,true);
};
};
dojo.event.MethodInvocation=function(_308,obj,args){
this.jp_=_308;
this.object=obj;
this.args=[];
for(var x=0;x<args.length;x++){
this.args[x]=args[x];
}
this.around_index=-1;
};
dojo.event.MethodInvocation.prototype.proceed=function(){
this.around_index++;
if(this.around_index>=this.jp_.around.length){
return this.jp_.object[this.jp_.methodname].apply(this.jp_.object,this.args);
}else{
var ti=this.jp_.around[this.around_index];
var mobj=ti[0]||dj_global;
var meth=ti[1];
return mobj[meth].call(mobj,this);
}
};
dojo.event.MethodJoinPoint=function(obj,_310){
this.object=obj||dj_global;
this.methodname=_310;
this.methodfunc=this.object[_310];
this.squelch=false;
};
dojo.event.MethodJoinPoint.getForMethod=function(obj,_312){
if(!obj){
obj=dj_global;
}
if(!obj[_312]){
obj[_312]=function(){
};
if(!obj[_312]){
dojo.raise("Cannot set do-nothing method on that object "+_312);
}
}else{
if((!dojo.lang.isFunction(obj[_312]))&&(!dojo.lang.isAlien(obj[_312]))){
return null;
}
}
var _313=_312+"$joinpoint";
var _314=_312+"$joinpoint$method";
var _315=obj[_313];
if(!_315){
var _316=false;
if(dojo.event["browser"]){
if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){
_316=true;
dojo.event.browser.addClobberNodeAttrs(obj,[_313,_314,_312]);
}
}
var _317=obj[_312].length;
obj[_314]=obj[_312];
_315=obj[_313]=new dojo.event.MethodJoinPoint(obj,_314);
obj[_312]=function(){
var args=[];
if((_316)&&(!arguments.length)){
var evt=null;
try{
if(obj.ownerDocument){
evt=obj.ownerDocument.parentWindow.event;
}else{
if(obj.documentElement){
evt=obj.documentElement.ownerDocument.parentWindow.event;
}else{
if(obj.event){
evt=obj.event;
}else{
evt=window.event;
}
}
}
}
catch(e){
evt=window.event;
}
if(evt){
args.push(dojo.event.browser.fixEvent(evt,this));
}
}else{
for(var x=0;x<arguments.length;x++){
if((x==0)&&(_316)&&(dojo.event.browser.isEvent(arguments[x]))){
args.push(dojo.event.browser.fixEvent(arguments[x],this));
}else{
args.push(arguments[x]);
}
}
}
return _315.run.apply(_315,args);
};
obj[_312].__preJoinArity=_317;
}
return _315;
};
dojo.lang.extend(dojo.event.MethodJoinPoint,{unintercept:function(){
this.object[this.methodname]=this.methodfunc;
this.before=[];
this.after=[];
this.around=[];
},disconnect:dojo.lang.forward("unintercept"),run:function(){
var obj=this.object||dj_global;
var args=arguments;
var _31d=[];
for(var x=0;x<args.length;x++){
_31d[x]=args[x];
}
var _31f=function(marr){
if(!marr){
dojo.debug("Null argument to unrollAdvice()");
return;
}
var _321=marr[0]||dj_global;
var _322=marr[1];
if(!_321[_322]){
dojo.raise("function \""+_322+"\" does not exist on \""+_321+"\"");
}
var _323=marr[2]||dj_global;
var _324=marr[3];
var msg=marr[6];
var _326;
var to={args:[],jp_:this,object:obj,proceed:function(){
return _321[_322].apply(_321,to.args);
}};
to.args=_31d;
var _328=parseInt(marr[4]);
var _329=((!isNaN(_328))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));
if(marr[5]){
var rate=parseInt(marr[5]);
var cur=new Date();
var _32c=false;
if((marr["last"])&&((cur-marr.last)<=rate)){
if(dojo.event._canTimeout){
if(marr["delayTimer"]){
clearTimeout(marr.delayTimer);
}
var tod=parseInt(rate*2);
var mcpy=dojo.lang.shallowCopy(marr);
marr.delayTimer=setTimeout(function(){
mcpy[5]=0;
_31f(mcpy);
},tod);
}
return;
}else{
marr.last=cur;
}
}
if(_324){
_323[_324].call(_323,to);
}else{
if((_329)&&((dojo.render.html)||(dojo.render.svg))){
dj_global["setTimeout"](function(){
if(msg){
_321[_322].call(_321,to);
}else{
_321[_322].apply(_321,args);
}
},_328);
}else{
if(msg){
_321[_322].call(_321,to);
}else{
_321[_322].apply(_321,args);
}
}
}
};
var _32f=function(){
if(this.squelch){
try{
return _31f.apply(this,arguments);
}
catch(e){
dojo.debug(e);
}
}else{
return _31f.apply(this,arguments);
}
};
if((this["before"])&&(this.before.length>0)){
dojo.lang.forEach(this.before.concat(new Array()),_32f);
}
var _330;
try{
if((this["around"])&&(this.around.length>0)){
var mi=new dojo.event.MethodInvocation(this,obj,args);
_330=mi.proceed();
}else{
if(this.methodfunc){
_330=this.object[this.methodname].apply(this.object,args);
}
}
}
catch(e){
if(!this.squelch){
dojo.debug(e,"when calling",this.methodname,"on",this.object,"with arguments",args);
dojo.raise(e);
}
}
if((this["after"])&&(this.after.length>0)){
dojo.lang.forEach(this.after.concat(new Array()),_32f);
}
return (this.methodfunc)?_330:null;
},getArr:function(kind){
var type="after";
if((typeof kind=="string")&&(kind.indexOf("before")!=-1)){
type="before";
}else{
if(kind=="around"){
type="around";
}
}
if(!this[type]){
this[type]=[];
}
return this[type];
},kwAddAdvice:function(args){
this.addAdvice(args["adviceObj"],args["adviceFunc"],args["aroundObj"],args["aroundFunc"],args["adviceType"],args["precedence"],args["once"],args["delay"],args["rate"],args["adviceMsg"]);
},addAdvice:function(_335,_336,_337,_338,_339,_33a,once,_33c,rate,_33e){
var arr=this.getArr(_339);
if(!arr){
dojo.raise("bad this: "+this);
}
var ao=[_335,_336,_337,_338,_33c,rate,_33e];
if(once){
if(this.hasAdvice(_335,_336,_339,arr)>=0){
return;
}
}
if(_33a=="first"){
arr.unshift(ao);
}else{
arr.push(ao);
}
},hasAdvice:function(_341,_342,_343,arr){
if(!arr){
arr=this.getArr(_343);
}
var ind=-1;
for(var x=0;x<arr.length;x++){
var aao=(typeof _342=="object")?(new String(_342)).toString():_342;
var a1o=(typeof arr[x][1]=="object")?(new String(arr[x][1])).toString():arr[x][1];
if((arr[x][0]==_341)&&(a1o==aao)){
ind=x;
}
}
return ind;
},removeAdvice:function(_349,_34a,_34b,once){
var arr=this.getArr(_34b);
var ind=this.hasAdvice(_349,_34a,_34b,arr);
if(ind==-1){
return false;
}
while(ind!=-1){
arr.splice(ind,1);
if(once){
break;
}
ind=this.hasAdvice(_349,_34a,_34b,arr);
}
return true;
}});
dojo.provide("dojo.event.topic");
dojo.event.topic=new function(){
this.topics={};
this.getTopic=function(_34f){
if(!this.topics[_34f]){
this.topics[_34f]=new this.TopicImpl(_34f);
}
return this.topics[_34f];
};
this.registerPublisher=function(_350,obj,_352){
var _350=this.getTopic(_350);
_350.registerPublisher(obj,_352);
};
this.subscribe=function(_353,obj,_355){
var _353=this.getTopic(_353);
_353.subscribe(obj,_355);
};
this.unsubscribe=function(_356,obj,_358){
var _356=this.getTopic(_356);
_356.unsubscribe(obj,_358);
};
this.destroy=function(_359){
this.getTopic(_359).destroy();
delete this.topics[_359];
};
this.publishApply=function(_35a,args){
var _35a=this.getTopic(_35a);
_35a.sendMessage.apply(_35a,args);
};
this.publish=function(_35c,_35d){
var _35c=this.getTopic(_35c);
var args=[];
for(var x=1;x<arguments.length;x++){
args.push(arguments[x]);
}
_35c.sendMessage.apply(_35c,args);
};
};
dojo.event.topic.TopicImpl=function(_360){
this.topicName=_360;
this.subscribe=function(_361,_362){
var tf=_362||_361;
var to=(!_362)?dj_global:_361;
return dojo.event.kwConnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this.unsubscribe=function(_365,_366){
var tf=(!_366)?_365:_366;
var to=(!_366)?null:_365;
return dojo.event.kwDisconnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this._getJoinPoint=function(){
return dojo.event.MethodJoinPoint.getForMethod(this,"sendMessage");
};
this.setSquelch=function(_369){
this._getJoinPoint().squelch=_369;
};
this.destroy=function(){
this._getJoinPoint().disconnect();
};
this.registerPublisher=function(_36a,_36b){
dojo.event.connect(_36a,_36b,this,"sendMessage");
};
this.sendMessage=function(_36c){
};
};
dojo.provide("dojo.event.browser");
dojo._ie_clobber=new function(){
this.clobberNodes=[];
function nukeProp(node,prop){
try{
node[prop]=null;
}
catch(e){
}
try{
delete node[prop];
}
catch(e){
}
try{
node.removeAttribute(prop);
}
catch(e){
}
}
this.clobber=function(_36f){
var na;
var tna;
if(_36f){
tna=_36f.all||_36f.getElementsByTagName("*");
na=[_36f];
for(var x=0;x<tna.length;x++){
if(tna[x]["__doClobber__"]){
na.push(tna[x]);
}
}
}else{
try{
window.onload=null;
}
catch(e){
}
na=(this.clobberNodes.length)?this.clobberNodes:document.all;
}
tna=null;
var _373={};
for(var i=na.length-1;i>=0;i=i-1){
var el=na[i];
try{
if(el&&el["__clobberAttrs__"]){
for(var j=0;j<el.__clobberAttrs__.length;j++){
nukeProp(el,el.__clobberAttrs__[j]);
}
nukeProp(el,"__clobberAttrs__");
nukeProp(el,"__doClobber__");
}
}
catch(e){
}
}
na=null;
};
};
if(dojo.render.html.ie){
dojo.addOnUnload(function(){
dojo._ie_clobber.clobber();
try{
if((dojo["widget"])&&(dojo.widget["manager"])){
dojo.widget.manager.destroyAll();
}
}
catch(e){
}
for(var name in dojo.widget._templateCache){
if(dojo.widget._templateCache[name].node){
dojo.dom.removeNode(dojo.widget._templateCache[name].node);
dojo.widget._templateCache[name].node=null;
delete dojo.widget._templateCache[name].node;
}
}
while(dojo.dom._ieRemovedNodes.length>0){
var node=dojo.dom._ieRemovedNodes.pop();
dojo.dom._discardElement(node);
node=null;
}
try{
window.onload=null;
}
catch(e){
}
try{
window.onunload=null;
}
catch(e){
}
dojo._ie_clobber.clobberNodes=[];
});
}
dojo.event.browser=new function(){
var _379=0;
this.normalizedEventName=function(_37a){
switch(_37a){
case "CheckboxStateChange":
case "DOMAttrModified":
case "DOMMenuItemActive":
case "DOMMenuItemInactive":
case "DOMMouseScroll":
case "DOMNodeInserted":
case "DOMNodeRemoved":
case "RadioStateChange":
return _37a;
break;
default:
return _37a.toLowerCase();
break;
}
};
this.clean=function(node){
if(dojo.render.html.ie){
dojo._ie_clobber.clobber(node);
}
};
this.addClobberNode=function(node){
if(!dojo.render.html.ie){
return;
}
if(!node["__doClobber__"]){
node.__doClobber__=true;
dojo._ie_clobber.clobberNodes.push(node);
node.__clobberAttrs__=[];
}
};
this.addClobberNodeAttrs=function(node,_37e){
if(!dojo.render.html.ie){
return;
}
this.addClobberNode(node);
for(var x=0;x<_37e.length;x++){
node.__clobberAttrs__.push(_37e[x]);
}
};
this.removeListener=function(node,_381,fp,_383){
if(!_383){
var _383=false;
}
_381=dojo.event.browser.normalizedEventName(_381);
if((_381=="onkey")||(_381=="key")){
if(dojo.render.html.ie){
this.removeListener(node,"onkeydown",fp,_383);
}
_381="onkeypress";
}
if(_381.substr(0,2)=="on"){
_381=_381.substr(2);
}
if(node.removeEventListener){
node.removeEventListener(_381,fp,_383);
}
};
this.addListener=function(node,_385,fp,_387,_388){
if(!node){
return;
}
if(!_387){
var _387=false;
}
_385=dojo.event.browser.normalizedEventName(_385);
if((_385=="onkey")||(_385=="key")){
if(dojo.render.html.ie){
this.addListener(node,"onkeydown",fp,_387,_388);
}
_385="onkeypress";
}
if(_385.substr(0,2)!="on"){
_385="on"+_385;
}
if(!_388){
var _389=function(evt){
if(!evt){
evt=window.event;
}
var ret=fp(dojo.event.browser.fixEvent(evt,this));
if(_387){
dojo.event.browser.stopEvent(evt);
}
return ret;
};
}else{
_389=fp;
}
if(node.addEventListener){
node.addEventListener(_385.substr(2),_389,_387);
return _389;
}else{
if(typeof node[_385]=="function"){
var _38c=node[_385];
node[_385]=function(e){
_38c(e);
return _389(e);
};
}else{
node[_385]=_389;
}
if(dojo.render.html.ie){
this.addClobberNodeAttrs(node,[_385]);
}
return _389;
}
};
this.isEvent=function(obj){
return (typeof obj!="undefined")&&(typeof Event!="undefined")&&(obj.eventPhase);
};
this.currentEvent=null;
this.callListener=function(_38f,_390){
if(typeof _38f!="function"){
dojo.raise("listener not a function: "+_38f);
}
dojo.event.browser.currentEvent.currentTarget=_390;
return _38f.call(_390,dojo.event.browser.currentEvent);
};
this._stopPropagation=function(){
dojo.event.browser.currentEvent.cancelBubble=true;
};
this._preventDefault=function(){
dojo.event.browser.currentEvent.returnValue=false;
};
this.keys={KEY_BACKSPACE:8,KEY_TAB:9,KEY_CLEAR:12,KEY_ENTER:13,KEY_SHIFT:16,KEY_CTRL:17,KEY_ALT:18,KEY_PAUSE:19,KEY_CAPS_LOCK:20,KEY_ESCAPE:27,KEY_SPACE:32,KEY_PAGE_UP:33,KEY_PAGE_DOWN:34,KEY_END:35,KEY_HOME:36,KEY_LEFT_ARROW:37,KEY_UP_ARROW:38,KEY_RIGHT_ARROW:39,KEY_DOWN_ARROW:40,KEY_INSERT:45,KEY_DELETE:46,KEY_HELP:47,KEY_LEFT_WINDOW:91,KEY_RIGHT_WINDOW:92,KEY_SELECT:93,KEY_NUMPAD_0:96,KEY_NUMPAD_1:97,KEY_NUMPAD_2:98,KEY_NUMPAD_3:99,KEY_NUMPAD_4:100,KEY_NUMPAD_5:101,KEY_NUMPAD_6:102,KEY_NUMPAD_7:103,KEY_NUMPAD_8:104,KEY_NUMPAD_9:105,KEY_NUMPAD_MULTIPLY:106,KEY_NUMPAD_PLUS:107,KEY_NUMPAD_ENTER:108,KEY_NUMPAD_MINUS:109,KEY_NUMPAD_PERIOD:110,KEY_NUMPAD_DIVIDE:111,KEY_F1:112,KEY_F2:113,KEY_F3:114,KEY_F4:115,KEY_F5:116,KEY_F6:117,KEY_F7:118,KEY_F8:119,KEY_F9:120,KEY_F10:121,KEY_F11:122,KEY_F12:123,KEY_F13:124,KEY_F14:125,KEY_F15:126,KEY_NUM_LOCK:144,KEY_SCROLL_LOCK:145};
this.revKeys=[];
for(var key in this.keys){
this.revKeys[this.keys[key]]=key;
}
this.fixEvent=function(evt,_393){
if(!evt){
if(window["event"]){
evt=window.event;
}
}
if((evt["type"])&&(evt["type"].indexOf("key")==0)){
evt.keys=this.revKeys;
for(var key in this.keys){
evt[key]=this.keys[key];
}
if(evt["type"]=="keydown"&&dojo.render.html.ie){
switch(evt.keyCode){
case evt.KEY_SHIFT:
case evt.KEY_CTRL:
case evt.KEY_ALT:
case evt.KEY_CAPS_LOCK:
case evt.KEY_LEFT_WINDOW:
case evt.KEY_RIGHT_WINDOW:
case evt.KEY_SELECT:
case evt.KEY_NUM_LOCK:
case evt.KEY_SCROLL_LOCK:
case evt.KEY_NUMPAD_0:
case evt.KEY_NUMPAD_1:
case evt.KEY_NUMPAD_2:
case evt.KEY_NUMPAD_3:
case evt.KEY_NUMPAD_4:
case evt.KEY_NUMPAD_5:
case evt.KEY_NUMPAD_6:
case evt.KEY_NUMPAD_7:
case evt.KEY_NUMPAD_8:
case evt.KEY_NUMPAD_9:
case evt.KEY_NUMPAD_PERIOD:
break;
case evt.KEY_NUMPAD_MULTIPLY:
case evt.KEY_NUMPAD_PLUS:
case evt.KEY_NUMPAD_ENTER:
case evt.KEY_NUMPAD_MINUS:
case evt.KEY_NUMPAD_DIVIDE:
break;
case evt.KEY_PAUSE:
case evt.KEY_TAB:
case evt.KEY_BACKSPACE:
case evt.KEY_ENTER:
case evt.KEY_ESCAPE:
case evt.KEY_PAGE_UP:
case evt.KEY_PAGE_DOWN:
case evt.KEY_END:
case evt.KEY_HOME:
case evt.KEY_LEFT_ARROW:
case evt.KEY_UP_ARROW:
case evt.KEY_RIGHT_ARROW:
case evt.KEY_DOWN_ARROW:
case evt.KEY_INSERT:
case evt.KEY_DELETE:
case evt.KEY_F1:
case evt.KEY_F2:
case evt.KEY_F3:
case evt.KEY_F4:
case evt.KEY_F5:
case evt.KEY_F6:
case evt.KEY_F7:
case evt.KEY_F8:
case evt.KEY_F9:
case evt.KEY_F10:
case evt.KEY_F11:
case evt.KEY_F12:
case evt.KEY_F12:
case evt.KEY_F13:
case evt.KEY_F14:
case evt.KEY_F15:
case evt.KEY_CLEAR:
case evt.KEY_HELP:
evt.key=evt.keyCode;
break;
default:
if(evt.ctrlKey||evt.altKey){
var _395=evt.keyCode;
if(_395>=65&&_395<=90&&evt.shiftKey==false){
_395+=32;
}
if(_395>=1&&_395<=26&&evt.ctrlKey){
_395+=96;
}
evt.key=String.fromCharCode(_395);
}
}
}else{
if(evt["type"]=="keypress"){
if(dojo.render.html.opera){
if(evt.which==0){
evt.key=evt.keyCode;
}else{
if(evt.which>0){
switch(evt.which){
case evt.KEY_SHIFT:
case evt.KEY_CTRL:
case evt.KEY_ALT:
case evt.KEY_CAPS_LOCK:
case evt.KEY_NUM_LOCK:
case evt.KEY_SCROLL_LOCK:
break;
case evt.KEY_PAUSE:
case evt.KEY_TAB:
case evt.KEY_BACKSPACE:
case evt.KEY_ENTER:
case evt.KEY_ESCAPE:
evt.key=evt.which;
break;
default:
var _395=evt.which;
if((evt.ctrlKey||evt.altKey||evt.metaKey)&&(evt.which>=65&&evt.which<=90&&evt.shiftKey==false)){
_395+=32;
}
evt.key=String.fromCharCode(_395);
}
}
}
}else{
if(dojo.render.html.ie){
if(!evt.ctrlKey&&!evt.altKey&&evt.keyCode>=evt.KEY_SPACE){
evt.key=String.fromCharCode(evt.keyCode);
}
}else{
if(dojo.render.html.safari){
switch(evt.keyCode){
case 25:
evt.key=evt.KEY_TAB;
evt.shift=true;
break;
case 63232:
evt.key=evt.KEY_UP_ARROW;
break;
case 63233:
evt.key=evt.KEY_DOWN_ARROW;
break;
case 63234:
evt.key=evt.KEY_LEFT_ARROW;
break;
case 63235:
evt.key=evt.KEY_RIGHT_ARROW;
break;
case 63236:
evt.key=evt.KEY_F1;
break;
case 63237:
evt.key=evt.KEY_F2;
break;
case 63238:
evt.key=evt.KEY_F3;
break;
case 63239:
evt.key=evt.KEY_F4;
break;
case 63240:
evt.key=evt.KEY_F5;
break;
case 63241:
evt.key=evt.KEY_F6;
break;
case 63242:
evt.key=evt.KEY_F7;
break;
case 63243:
evt.key=evt.KEY_F8;
break;
case 63244:
evt.key=evt.KEY_F9;
break;
case 63245:
evt.key=evt.KEY_F10;
break;
case 63246:
evt.key=evt.KEY_F11;
break;
case 63247:
evt.key=evt.KEY_F12;
break;
case 63250:
evt.key=evt.KEY_PAUSE;
break;
case 63272:
evt.key=evt.KEY_DELETE;
break;
case 63273:
evt.key=evt.KEY_HOME;
break;
case 63275:
evt.key=evt.KEY_END;
break;
case 63276:
evt.key=evt.KEY_PAGE_UP;
break;
case 63277:
evt.key=evt.KEY_PAGE_DOWN;
break;
case 63302:
evt.key=evt.KEY_INSERT;
break;
case 63248:
case 63249:
case 63289:
break;
default:
evt.key=evt.charCode>=evt.KEY_SPACE?String.fromCharCode(evt.charCode):evt.keyCode;
}
}else{
evt.key=evt.charCode>0?String.fromCharCode(evt.charCode):evt.keyCode;
}
}
}
}
}
}
if(dojo.render.html.ie){
if(!evt.target){
evt.target=evt.srcElement;
}
if(!evt.currentTarget){
evt.currentTarget=(_393?_393:evt.srcElement);
}
if(!evt.layerX){
evt.layerX=evt.offsetX;
}
if(!evt.layerY){
evt.layerY=evt.offsetY;
}
var doc=(evt.srcElement&&evt.srcElement.ownerDocument)?evt.srcElement.ownerDocument:document;
var _397=((dojo.render.html.ie55)||(doc["compatMode"]=="BackCompat"))?doc.body:doc.documentElement;
if(!evt.pageX){
evt.pageX=evt.clientX+(_397.scrollLeft||0);
}
if(!evt.pageY){
evt.pageY=evt.clientY+(_397.scrollTop||0);
}
if(evt.type=="mouseover"){
evt.relatedTarget=evt.fromElement;
}
if(evt.type=="mouseout"){
evt.relatedTarget=evt.toElement;
}
this.currentEvent=evt;
evt.callListener=this.callListener;
evt.stopPropagation=this._stopPropagation;
evt.preventDefault=this._preventDefault;
}
return evt;
};
this.stopEvent=function(evt){
if(window.event){
evt.cancelBubble=true;
evt.returnValue=false;
}else{
evt.preventDefault();
evt.stopPropagation();
}
};
};
dojo.provide("dojo.event.*");
dojo.provide("dojo.gfx.color");
dojo.gfx.color.Color=function(r,g,b,a){
if(dojo.lang.isArray(r)){
this.r=r[0];
this.g=r[1];
this.b=r[2];
this.a=r[3]||1;
}else{
if(dojo.lang.isString(r)){
var rgb=dojo.gfx.color.extractRGB(r);
this.r=rgb[0];
this.g=rgb[1];
this.b=rgb[2];
this.a=g||1;
}else{
if(r instanceof dojo.gfx.color.Color){
this.r=r.r;
this.b=r.b;
this.g=r.g;
this.a=r.a;
}else{
this.r=r;
this.g=g;
this.b=b;
this.a=a;
}
}
}
};
dojo.gfx.color.Color.fromArray=function(arr){
return new dojo.gfx.color.Color(arr[0],arr[1],arr[2],arr[3]);
};
dojo.extend(dojo.gfx.color.Color,{toRgb:function(_39f){
if(_39f){
return this.toRgba();
}else{
return [this.r,this.g,this.b];
}
},toRgba:function(){
return [this.r,this.g,this.b,this.a];
},toHex:function(){
return dojo.gfx.color.rgb2hex(this.toRgb());
},toCss:function(){
return "rgb("+this.toRgb().join()+")";
},toString:function(){
return this.toHex();
},blend:function(_3a0,_3a1){
var rgb=null;
if(dojo.lang.isArray(_3a0)){
rgb=_3a0;
}else{
if(_3a0 instanceof dojo.gfx.color.Color){
rgb=_3a0.toRgb();
}else{
rgb=new dojo.gfx.color.Color(_3a0).toRgb();
}
}
return dojo.gfx.color.blend(this.toRgb(),rgb,_3a1);
}});
dojo.gfx.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],lime:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};
dojo.gfx.color.blend=function(a,b,_3a5){
if(typeof a=="string"){
return dojo.gfx.color.blendHex(a,b,_3a5);
}
if(!_3a5){
_3a5=0;
}
_3a5=Math.min(Math.max(-1,_3a5),1);
_3a5=((_3a5+1)/2);
var c=[];
for(var x=0;x<3;x++){
c[x]=parseInt(b[x]+((a[x]-b[x])*_3a5));
}
return c;
};
dojo.gfx.color.blendHex=function(a,b,_3aa){
return dojo.gfx.color.rgb2hex(dojo.gfx.color.blend(dojo.gfx.color.hex2rgb(a),dojo.gfx.color.hex2rgb(b),_3aa));
};
dojo.gfx.color.extractRGB=function(_3ab){
var hex="0123456789abcdef";
_3ab=_3ab.toLowerCase();
if(_3ab.indexOf("rgb")==0){
var _3ad=_3ab.match(/rgba*\((\d+), *(\d+), *(\d+)/i);
var ret=_3ad.splice(1,3);
return ret;
}else{
var _3af=dojo.gfx.color.hex2rgb(_3ab);
if(_3af){
return _3af;
}else{
return dojo.gfx.color.named[_3ab]||[255,255,255];
}
}
};
dojo.gfx.color.hex2rgb=function(hex){
var _3b1="0123456789ABCDEF";
var rgb=new Array(3);
if(hex.indexOf("#")==0){
hex=hex.substring(1);
}
hex=hex.toUpperCase();
if(hex.replace(new RegExp("["+_3b1+"]","g"),"")!=""){
return null;
}
if(hex.length==3){
rgb[0]=hex.charAt(0)+hex.charAt(0);
rgb[1]=hex.charAt(1)+hex.charAt(1);
rgb[2]=hex.charAt(2)+hex.charAt(2);
}else{
rgb[0]=hex.substring(0,2);
rgb[1]=hex.substring(2,4);
rgb[2]=hex.substring(4);
}
for(var i=0;i<rgb.length;i++){
rgb[i]=_3b1.indexOf(rgb[i].charAt(0))*16+_3b1.indexOf(rgb[i].charAt(1));
}
return rgb;
};
dojo.gfx.color.rgb2hex=function(r,g,b){
if(dojo.lang.isArray(r)){
g=r[1]||0;
b=r[2]||0;
r=r[0]||0;
}
var ret=dojo.lang.map([r,g,b],function(x){
x=new Number(x);
var s=x.toString(16);
while(s.length<2){
s="0"+s;
}
return s;
});
ret.unshift("#");
return ret.join("");
};
dojo.provide("dojo.lfx.Animation");
dojo.lfx.Line=function(_3ba,end){
this.start=_3ba;
this.end=end;
if(dojo.lang.isArray(_3ba)){
var diff=[];
dojo.lang.forEach(this.start,function(s,i){
diff[i]=this.end[i]-s;
},this);
this.getValue=function(n){
var res=[];
dojo.lang.forEach(this.start,function(s,i){
res[i]=(diff[i]*n)+s;
},this);
return res;
};
}else{
var diff=end-_3ba;
this.getValue=function(n){
return (diff*n)+this.start;
};
}
};
dojo.lfx.easeDefault=function(n){
if(dojo.render.html.khtml){
return (parseFloat("0.5")+((Math.sin((n+parseFloat("1.5"))*Math.PI))/2));
}else{
return (0.5+((Math.sin((n+1.5)*Math.PI))/2));
}
};
dojo.lfx.easeIn=function(n){
return Math.pow(n,3);
};
dojo.lfx.easeOut=function(n){
return (1-Math.pow(1-n,3));
};
dojo.lfx.easeInOut=function(n){
return ((3*Math.pow(n,2))-(2*Math.pow(n,3)));
};
dojo.lfx.IAnimation=function(){
};
dojo.lang.extend(dojo.lfx.IAnimation,{curve:null,duration:1000,easing:null,repeatCount:0,rate:25,handler:null,beforeBegin:null,onBegin:null,onAnimate:null,onEnd:null,onPlay:null,onPause:null,onStop:null,play:null,pause:null,stop:null,connect:function(evt,_3c9,_3ca){
if(!_3ca){
_3ca=_3c9;
_3c9=this;
}
_3ca=dojo.lang.hitch(_3c9,_3ca);
var _3cb=this[evt]||function(){
};
this[evt]=function(){
var ret=_3cb.apply(this,arguments);
_3ca.apply(this,arguments);
return ret;
};
return this;
},fire:function(evt,args){
if(this[evt]){
this[evt].apply(this,(args||[]));
}
return this;
},repeat:function(_3cf){
this.repeatCount=_3cf;
return this;
},_active:false,_paused:false});
dojo.lfx.Animation=function(_3d0,_3d1,_3d2,_3d3,_3d4,rate){
dojo.lfx.IAnimation.call(this);
if(dojo.lang.isNumber(_3d0)||(!_3d0&&_3d1.getValue)){
rate=_3d4;
_3d4=_3d3;
_3d3=_3d2;
_3d2=_3d1;
_3d1=_3d0;
_3d0=null;
}else{
if(_3d0.getValue||dojo.lang.isArray(_3d0)){
rate=_3d3;
_3d4=_3d2;
_3d3=_3d1;
_3d2=_3d0;
_3d1=null;
_3d0=null;
}
}
if(dojo.lang.isArray(_3d2)){
this.curve=new dojo.lfx.Line(_3d2[0],_3d2[1]);
}else{
this.curve=_3d2;
}
if(_3d1!=null&&_3d1>0){
this.duration=_3d1;
}
if(_3d4){
this.repeatCount=_3d4;
}
if(rate){
this.rate=rate;
}
if(_3d0){
dojo.lang.forEach(["handler","beforeBegin","onBegin","onEnd","onPlay","onStop","onAnimate"],function(item){
if(_3d0[item]){
this.connect(item,_3d0[item]);
}
},this);
}
if(_3d3&&dojo.lang.isFunction(_3d3)){
this.easing=_3d3;
}
};
dojo.inherits(dojo.lfx.Animation,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Animation,{_startTime:null,_endTime:null,_timer:null,_percent:0,_startRepeatCount:0,play:function(_3d7,_3d8){
if(_3d8){
clearTimeout(this._timer);
this._active=false;
this._paused=false;
this._percent=0;
}else{
if(this._active&&!this._paused){
return this;
}
}
this.fire("handler",["beforeBegin"]);
this.fire("beforeBegin");
if(_3d7>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_3d8);
}),_3d7);
return this;
}
this._startTime=new Date().valueOf();
if(this._paused){
this._startTime-=(this.duration*this._percent/100);
}
this._endTime=this._startTime+this.duration;
this._active=true;
this._paused=false;
var step=this._percent/100;
var _3da=this.curve.getValue(step);
if(this._percent==0){
if(!this._startRepeatCount){
this._startRepeatCount=this.repeatCount;
}
this.fire("handler",["begin",_3da]);
this.fire("onBegin",[_3da]);
}
this.fire("handler",["play",_3da]);
this.fire("onPlay",[_3da]);
this._cycle();
return this;
},pause:function(){
clearTimeout(this._timer);
if(!this._active){
return this;
}
this._paused=true;
var _3db=this.curve.getValue(this._percent/100);
this.fire("handler",["pause",_3db]);
this.fire("onPause",[_3db]);
return this;
},gotoPercent:function(pct,_3dd){
clearTimeout(this._timer);
this._active=true;
this._paused=true;
this._percent=pct;
if(_3dd){
this.play();
}
return this;
},stop:function(_3de){
clearTimeout(this._timer);
var step=this._percent/100;
if(_3de){
step=1;
}
var _3e0=this.curve.getValue(step);
this.fire("handler",["stop",_3e0]);
this.fire("onStop",[_3e0]);
this._active=false;
this._paused=false;
return this;
},status:function(){
if(this._active){
return this._paused?"paused":"playing";
}else{
return "stopped";
}
return this;
},_cycle:function(){
clearTimeout(this._timer);
if(this._active){
var curr=new Date().valueOf();
var step=(curr-this._startTime)/(this._endTime-this._startTime);
if(step>=1){
step=1;
this._percent=100;
}else{
this._percent=step*100;
}
if((this.easing)&&(dojo.lang.isFunction(this.easing))){
step=this.easing(step);
}
var _3e3=this.curve.getValue(step);
this.fire("handler",["animate",_3e3]);
this.fire("onAnimate",[_3e3]);
if(step<1){
this._timer=setTimeout(dojo.lang.hitch(this,"_cycle"),this.rate);
}else{
this._active=false;
this.fire("handler",["end"]);
this.fire("onEnd");
if(this.repeatCount>0){
this.repeatCount--;
this.play(null,true);
}else{
if(this.repeatCount==-1){
this.play(null,true);
}else{
if(this._startRepeatCount){
this.repeatCount=this._startRepeatCount;
this._startRepeatCount=0;
}
}
}
}
}
return this;
}});
dojo.lfx.Combine=function(_3e4){
dojo.lfx.IAnimation.call(this);
this._anims=[];
this._animsEnded=0;
var _3e5=arguments;
if(_3e5.length==1&&(dojo.lang.isArray(_3e5[0])||dojo.lang.isArrayLike(_3e5[0]))){
_3e5=_3e5[0];
}
dojo.lang.forEach(_3e5,function(anim){
this._anims.push(anim);
anim.connect("onEnd",dojo.lang.hitch(this,"_onAnimsEnded"));
},this);
};
dojo.inherits(dojo.lfx.Combine,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Combine,{_animsEnded:0,play:function(_3e7,_3e8){
if(!this._anims.length){
return this;
}
this.fire("beforeBegin");
if(_3e7>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_3e8);
}),_3e7);
return this;
}
if(_3e8||this._anims[0].percent==0){
this.fire("onBegin");
}
this.fire("onPlay");
this._animsCall("play",null,_3e8);
return this;
},pause:function(){
this.fire("onPause");
this._animsCall("pause");
return this;
},stop:function(_3e9){
this.fire("onStop");
this._animsCall("stop",_3e9);
return this;
},_onAnimsEnded:function(){
this._animsEnded++;
if(this._animsEnded>=this._anims.length){
this.fire("onEnd");
}
return this;
},_animsCall:function(_3ea){
var args=[];
if(arguments.length>1){
for(var i=1;i<arguments.length;i++){
args.push(arguments[i]);
}
}
var _3ed=this;
dojo.lang.forEach(this._anims,function(anim){
anim[_3ea](args);
},_3ed);
return this;
}});
dojo.lfx.Chain=function(_3ef){
dojo.lfx.IAnimation.call(this);
this._anims=[];
this._currAnim=-1;
var _3f0=arguments;
if(_3f0.length==1&&(dojo.lang.isArray(_3f0[0])||dojo.lang.isArrayLike(_3f0[0]))){
_3f0=_3f0[0];
}
var _3f1=this;
dojo.lang.forEach(_3f0,function(anim,i,_3f4){
this._anims.push(anim);
if(i<_3f4.length-1){
anim.connect("onEnd",dojo.lang.hitch(this,"_playNext"));
}else{
anim.connect("onEnd",dojo.lang.hitch(this,function(){
this.fire("onEnd");
}));
}
},this);
};
dojo.inherits(dojo.lfx.Chain,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Chain,{_currAnim:-1,play:function(_3f5,_3f6){
if(!this._anims.length){
return this;
}
if(_3f6||!this._anims[this._currAnim]){
this._currAnim=0;
}
var _3f7=this._anims[this._currAnim];
this.fire("beforeBegin");
if(_3f5>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_3f6);
}),_3f5);
return this;
}
if(_3f7){
if(this._currAnim==0){
this.fire("handler",["begin",this._currAnim]);
this.fire("onBegin",[this._currAnim]);
}
this.fire("onPlay",[this._currAnim]);
_3f7.play(null,_3f6);
}
return this;
},pause:function(){
if(this._anims[this._currAnim]){
this._anims[this._currAnim].pause();
this.fire("onPause",[this._currAnim]);
}
return this;
},playPause:function(){
if(this._anims.length==0){
return this;
}
if(this._currAnim==-1){
this._currAnim=0;
}
var _3f8=this._anims[this._currAnim];
if(_3f8){
if(!_3f8._active||_3f8._paused){
this.play();
}else{
this.pause();
}
}
return this;
},stop:function(){
var _3f9=this._anims[this._currAnim];
if(_3f9){
_3f9.stop();
this.fire("onStop",[this._currAnim]);
}
return _3f9;
},_playNext:function(){
if(this._currAnim==-1||this._anims.length==0){
return this;
}
this._currAnim++;
if(this._anims[this._currAnim]){
this._anims[this._currAnim].play(null,true);
}
return this;
}});
dojo.lfx.combine=function(_3fa){
var _3fb=arguments;
if(dojo.lang.isArray(arguments[0])){
_3fb=arguments[0];
}
if(_3fb.length==1){
return _3fb[0];
}
return new dojo.lfx.Combine(_3fb);
};
dojo.lfx.chain=function(_3fc){
var _3fd=arguments;
if(dojo.lang.isArray(arguments[0])){
_3fd=arguments[0];
}
if(_3fd.length==1){
return _3fd[0];
}
return new dojo.lfx.Chain(_3fd);
};
dojo.provide("dojo.uri.Uri");
dojo.uri=new function(){
this.dojoUri=function(uri){
return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(),uri);
};
this.moduleUri=function(_3ff,uri){
var loc=dojo.hostenv.getModuleSymbols(_3ff).join("/");
if(!loc){
return null;
}
if(loc.lastIndexOf("/")!=loc.length-1){
loc+="/";
}
return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri()+loc,uri);
};
this.Uri=function(){
var uri=arguments[0];
for(var i=1;i<arguments.length;i++){
if(!arguments[i]){
continue;
}
var _404=new dojo.uri.Uri(arguments[i].toString());
var _405=new dojo.uri.Uri(uri.toString());
if((_404.path=="")&&(_404.scheme==null)&&(_404.authority==null)&&(_404.query==null)){
if(_404.fragment!=null){
_405.fragment=_404.fragment;
}
_404=_405;
}else{
if(_404.scheme==null){
_404.scheme=_405.scheme;
if(_404.authority==null){
_404.authority=_405.authority;
if(_404.path.charAt(0)!="/"){
var path=_405.path.substring(0,_405.path.lastIndexOf("/")+1)+_404.path;
var segs=path.split("/");
for(var j=0;j<segs.length;j++){
if(segs[j]=="."){
if(j==segs.length-1){
segs[j]="";
}else{
segs.splice(j,1);
j--;
}
}else{
if(j>0&&!(j==1&&segs[0]=="")&&segs[j]==".."&&segs[j-1]!=".."){
if(j==segs.length-1){
segs.splice(j,1);
segs[j-1]="";
}else{
segs.splice(j-1,2);
j-=2;
}
}
}
}
_404.path=segs.join("/");
}
}
}
}
uri="";
if(_404.scheme!=null){
uri+=_404.scheme+":";
}
if(_404.authority!=null){
uri+="//"+_404.authority;
}
uri+=_404.path;
if(_404.query!=null){
uri+="?"+_404.query;
}
if(_404.fragment!=null){
uri+="#"+_404.fragment;
}
}
this.uri=uri.toString();
var _409="^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$";
var r=this.uri.match(new RegExp(_409));
this.scheme=r[2]||(r[1]?"":null);
this.authority=r[4]||(r[3]?"":null);
this.path=r[5];
this.query=r[7]||(r[6]?"":null);
this.fragment=r[9]||(r[8]?"":null);
if(this.authority!=null){
_409="^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$";
r=this.authority.match(new RegExp(_409));
this.user=r[3]||null;
this.password=r[4]||null;
this.host=r[5];
this.port=r[7]||null;
}
this.toString=function(){
return this.uri;
};
};
};
dojo.provide("dojo.html.style");
dojo.html.getClass=function(node){
node=dojo.byId(node);
if(!node){
return "";
}
var cs="";
if(node.className){
cs=node.className;
}else{
if(dojo.html.hasAttribute(node,"class")){
cs=dojo.html.getAttribute(node,"class");
}
}
return cs.replace(/^\s+|\s+$/g,"");
};
dojo.html.getClasses=function(node){
var c=dojo.html.getClass(node);
return (c=="")?[]:c.split(/\s+/g);
};
dojo.html.hasClass=function(node,_410){
return (new RegExp("(^|\\s+)"+_410+"(\\s+|$)")).test(dojo.html.getClass(node));
};
dojo.html.prependClass=function(node,_412){
_412+=" "+dojo.html.getClass(node);
return dojo.html.setClass(node,_412);
};
dojo.html.addClass=function(node,_414){
if(dojo.html.hasClass(node,_414)){
return false;
}
_414=(dojo.html.getClass(node)+" "+_414).replace(/^\s+|\s+$/g,"");
return dojo.html.setClass(node,_414);
};
dojo.html.setClass=function(node,_416){
node=dojo.byId(node);
var cs=new String(_416);
try{
if(typeof node.className=="string"){
node.className=cs;
}else{
if(node.setAttribute){
node.setAttribute("class",_416);
node.className=cs;
}else{
return false;
}
}
}
catch(e){
dojo.debug("dojo.html.setClass() failed",e);
}
return true;
};
dojo.html.removeClass=function(node,_419,_41a){
try{
if(!_41a){
var _41b=dojo.html.getClass(node).replace(new RegExp("(^|\\s+)"+_419+"(\\s+|$)"),"$1$2");
}else{
var _41b=dojo.html.getClass(node).replace(_419,"");
}
dojo.html.setClass(node,_41b);
}
catch(e){
dojo.debug("dojo.html.removeClass() failed",e);
}
return true;
};
dojo.html.replaceClass=function(node,_41d,_41e){
dojo.html.removeClass(node,_41e);
dojo.html.addClass(node,_41d);
};
dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};
dojo.html.getElementsByClass=function(_41f,_420,_421,_422,_423){
_423=false;
var _424=dojo.doc();
_420=dojo.byId(_420)||_424;
var _425=_41f.split(/\s+/g);
var _426=[];
if(_422!=1&&_422!=2){
_422=0;
}
var _427=new RegExp("(\\s|^)(("+_425.join(")|(")+"))(\\s|$)");
var _428=_425.join(" ").length;
var _429=[];
if(!_423&&_424.evaluate){
var _42a=".//"+(_421||"*")+"[contains(";
if(_422!=dojo.html.classMatchType.ContainsAny){
_42a+="concat(' ',@class,' '), ' "+_425.join(" ') and contains(concat(' ',@class,' '), ' ")+" ')";
if(_422==2){
_42a+=" and string-length(@class)="+_428+"]";
}else{
_42a+="]";
}
}else{
_42a+="concat(' ',@class,' '), ' "+_425.join(" ') or contains(concat(' ',@class,' '), ' ")+" ')]";
}
var _42b=_424.evaluate(_42a,_420,null,XPathResult.ANY_TYPE,null);
var _42c=_42b.iterateNext();
while(_42c){
try{
_429.push(_42c);
_42c=_42b.iterateNext();
}
catch(e){
break;
}
}
return _429;
}else{
if(!_421){
_421="*";
}
_429=_420.getElementsByTagName(_421);
var node,i=0;
outer:
while(node=_429[i++]){
var _42f=dojo.html.getClasses(node);
if(_42f.length==0){
continue outer;
}
var _430=0;
for(var j=0;j<_42f.length;j++){
if(_427.test(_42f[j])){
if(_422==dojo.html.classMatchType.ContainsAny){
_426.push(node);
continue outer;
}else{
_430++;
}
}else{
if(_422==dojo.html.classMatchType.IsOnly){
continue outer;
}
}
}
if(_430==_425.length){
if((_422==dojo.html.classMatchType.IsOnly)&&(_430==_42f.length)){
_426.push(node);
}else{
if(_422==dojo.html.classMatchType.ContainsAll){
_426.push(node);
}
}
}
}
return _426;
}
};
dojo.html.getElementsByClassName=dojo.html.getElementsByClass;
dojo.html.toCamelCase=function(_432){
var arr=_432.split("-"),cc=arr[0];
for(var i=1;i<arr.length;i++){
cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);
}
return cc;
};
dojo.html.toSelectorCase=function(_436){
return _436.replace(/([A-Z])/g,"-$1").toLowerCase();
};
dojo.html.getComputedStyle=function(node,_438,_439){
node=dojo.byId(node);
var _438=dojo.html.toSelectorCase(_438);
var _43a=dojo.html.toCamelCase(_438);
if(!node||!node.style){
return _439;
}else{
if(document.defaultView&&dojo.html.isDescendantOf(node,node.ownerDocument)){
try{
var cs=document.defaultView.getComputedStyle(node,"");
if(cs){
return cs.getPropertyValue(_438);
}
}
catch(e){
if(node.style.getPropertyValue){
return node.style.getPropertyValue(_438);
}else{
return _439;
}
}
}else{
if(node.currentStyle){
return node.currentStyle[_43a];
}
}
}
if(node.style.getPropertyValue){
return node.style.getPropertyValue(_438);
}else{
return _439;
}
};
dojo.html.getStyleProperty=function(node,_43d){
node=dojo.byId(node);
return (node&&node.style?node.style[dojo.html.toCamelCase(_43d)]:undefined);
};
dojo.html.getStyle=function(node,_43f){
var _440=dojo.html.getStyleProperty(node,_43f);
return (_440?_440:dojo.html.getComputedStyle(node,_43f));
};
dojo.html.setStyle=function(node,_442,_443){
node=dojo.byId(node);
if(node&&node.style){
var _444=dojo.html.toCamelCase(_442);
node.style[_444]=_443;
}
};
dojo.html.setStyleText=function(_445,text){
try{
_445.style.cssText=text;
}
catch(e){
_445.setAttribute("style",text);
}
};
dojo.html.copyStyle=function(_447,_448){
if(!_448.style.cssText){
_447.setAttribute("style",_448.getAttribute("style"));
}else{
_447.style.cssText=_448.style.cssText;
}
dojo.html.addClass(_447,dojo.html.getClass(_448));
};
dojo.html.getUnitValue=function(node,_44a,_44b){
var s=dojo.html.getComputedStyle(node,_44a);
if((!s)||((s=="auto")&&(_44b))){
return {value:0,units:"px"};
}
var _44d=s.match(/(\-?[\d.]+)([a-z%]*)/i);
if(!_44d){
return dojo.html.getUnitValue.bad;
}
return {value:Number(_44d[1]),units:_44d[2].toLowerCase()};
};
dojo.html.getUnitValue.bad={value:NaN,units:""};
dojo.html.getPixelValue=function(node,_44f,_450){
var _451=dojo.html.getUnitValue(node,_44f,_450);
if(isNaN(_451.value)){
return 0;
}
if((_451.value)&&(_451.units!="px")){
return NaN;
}
return _451.value;
};
dojo.html.setPositivePixelValue=function(node,_453,_454){
if(isNaN(_454)){
return false;
}
node.style[_453]=Math.max(0,_454)+"px";
return true;
};
dojo.html.styleSheet=null;
dojo.html.insertCssRule=function(_455,_456,_457){
if(!dojo.html.styleSheet){
if(document.createStyleSheet){
dojo.html.styleSheet=document.createStyleSheet();
}else{
if(document.styleSheets[0]){
dojo.html.styleSheet=document.styleSheets[0];
}else{
return null;
}
}
}
if(arguments.length<3){
if(dojo.html.styleSheet.cssRules){
_457=dojo.html.styleSheet.cssRules.length;
}else{
if(dojo.html.styleSheet.rules){
_457=dojo.html.styleSheet.rules.length;
}else{
return null;
}
}
}
if(dojo.html.styleSheet.insertRule){
var rule=_455+" { "+_456+" }";
return dojo.html.styleSheet.insertRule(rule,_457);
}else{
if(dojo.html.styleSheet.addRule){
return dojo.html.styleSheet.addRule(_455,_456,_457);
}else{
return null;
}
}
};
dojo.html.removeCssRule=function(_459){
if(!dojo.html.styleSheet){
dojo.debug("no stylesheet defined for removing rules");
return false;
}
if(dojo.render.html.ie){
if(!_459){
_459=dojo.html.styleSheet.rules.length;
dojo.html.styleSheet.removeRule(_459);
}
}else{
if(document.styleSheets[0]){
if(!_459){
_459=dojo.html.styleSheet.cssRules.length;
}
dojo.html.styleSheet.deleteRule(_459);
}
}
return true;
};
dojo.html._insertedCssFiles=[];
dojo.html.insertCssFile=function(URI,doc,_45c,_45d){
if(!URI){
return;
}
if(!doc){
doc=document;
}
var _45e=dojo.hostenv.getText(URI,false,_45d);
if(_45e===null){
return;
}
_45e=dojo.html.fixPathsInCssText(_45e,URI);
if(_45c){
var idx=-1,node,ent=dojo.html._insertedCssFiles;
for(var i=0;i<ent.length;i++){
if((ent[i].doc==doc)&&(ent[i].cssText==_45e)){
idx=i;
node=ent[i].nodeRef;
break;
}
}
if(node){
var _463=doc.getElementsByTagName("style");
for(var i=0;i<_463.length;i++){
if(_463[i]==node){
return;
}
}
dojo.html._insertedCssFiles.shift(idx,1);
}
}
var _464=dojo.html.insertCssText(_45e,doc);
dojo.html._insertedCssFiles.push({"doc":doc,"cssText":_45e,"nodeRef":_464});
if(_464&&djConfig.isDebug){
_464.setAttribute("dbgHref",URI);
}
return _464;
};
dojo.html.insertCssText=function(_465,doc,URI){
if(!_465){
return;
}
if(!doc){
doc=document;
}
if(URI){
_465=dojo.html.fixPathsInCssText(_465,URI);
}
var _468=doc.createElement("style");
_468.setAttribute("type","text/css");
var head=doc.getElementsByTagName("head")[0];
if(!head){
dojo.debug("No head tag in document, aborting styles");
return;
}else{
head.appendChild(_468);
}
if(_468.styleSheet){
_468.styleSheet.cssText=_465;
}else{
var _46a=doc.createTextNode(_465);
_468.appendChild(_46a);
}
return _468;
};
dojo.html.fixPathsInCssText=function(_46b,URI){
if(!_46b||!URI){
return;
}
var _46d,str="",url="",_470="[\\t\\s\\w\\(\\)\\/\\.\\\\'\"-:#=&?~]+";
var _471=new RegExp("url\\(\\s*("+_470+")\\s*\\)");
var _472=/(file|https?|ftps?):\/\//;
regexTrim=new RegExp("^[\\s]*(['\"]?)("+_470+")\\1[\\s]*?$");
if(dojo.render.html.ie55||dojo.render.html.ie60){
var _473=new RegExp("AlphaImageLoader\\((.*)src=['\"]("+_470+")['\"]");
while(_46d=_473.exec(_46b)){
url=_46d[2].replace(regexTrim,"$2");
if(!_472.exec(url)){
url=(new dojo.uri.Uri(URI,url).toString());
}
str+=_46b.substring(0,_46d.index)+"AlphaImageLoader("+_46d[1]+"src='"+url+"'";
_46b=_46b.substr(_46d.index+_46d[0].length);
}
_46b=str+_46b;
str="";
}
while(_46d=_471.exec(_46b)){
url=_46d[1].replace(regexTrim,"$2");
if(!_472.exec(url)){
url=(new dojo.uri.Uri(URI,url).toString());
}
str+=_46b.substring(0,_46d.index)+"url("+url+")";
_46b=_46b.substr(_46d.index+_46d[0].length);
}
return str+_46b;
};
dojo.html.setActiveStyleSheet=function(_474){
var i=0,a,els=dojo.doc().getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){
a.disabled=true;
if(a.getAttribute("title")==_474){
a.disabled=false;
}
}
}
};
dojo.html.getActiveStyleSheet=function(){
var i=0,a,els=dojo.doc().getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")&&!a.disabled){
return a.getAttribute("title");
}
}
return null;
};
dojo.html.getPreferredStyleSheet=function(){
var i=0,a,els=dojo.doc().getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("rel").indexOf("alt")==-1&&a.getAttribute("title")){
return a.getAttribute("title");
}
}
return null;
};
dojo.html.applyBrowserClass=function(node){
var drh=dojo.render.html;
var _480={dj_ie:drh.ie,dj_ie55:drh.ie55,dj_ie6:drh.ie60,dj_ie7:drh.ie70,dj_iequirks:drh.ie&&drh.quirks,dj_opera:drh.opera,dj_opera8:drh.opera&&(Math.floor(dojo.render.version)==8),dj_opera9:drh.opera&&(Math.floor(dojo.render.version)==9),dj_khtml:drh.khtml,dj_safari:drh.safari,dj_gecko:drh.mozilla};
for(var p in _480){
if(_480[p]){
dojo.html.addClass(node,p);
}
}
};
dojo.provide("dojo.html.display");
dojo.html._toggle=function(node,_483,_484){
node=dojo.byId(node);
_484(node,!_483(node));
return _483(node);
};
dojo.html.show=function(node){
node=dojo.byId(node);
if(dojo.html.getStyleProperty(node,"display")=="none"){
dojo.html.setStyle(node,"display",(node.dojoDisplayCache||""));
node.dojoDisplayCache=undefined;
}
};
dojo.html.hide=function(node){
node=dojo.byId(node);
if(typeof node["dojoDisplayCache"]=="undefined"){
var d=dojo.html.getStyleProperty(node,"display");
if(d!="none"){
node.dojoDisplayCache=d;
}
}
dojo.html.setStyle(node,"display","none");
};
dojo.html.setShowing=function(node,_489){
dojo.html[(_489?"show":"hide")](node);
};
dojo.html.isShowing=function(node){
return (dojo.html.getStyleProperty(node,"display")!="none");
};
dojo.html.toggleShowing=function(node){
return dojo.html._toggle(node,dojo.html.isShowing,dojo.html.setShowing);
};
dojo.html.displayMap={tr:"",td:"",th:"",img:"inline",span:"inline",input:"inline",button:"inline"};
dojo.html.suggestDisplayByTagName=function(node){
node=dojo.byId(node);
if(node&&node.tagName){
var tag=node.tagName.toLowerCase();
return (tag in dojo.html.displayMap?dojo.html.displayMap[tag]:"block");
}
};
dojo.html.setDisplay=function(node,_48f){
dojo.html.setStyle(node,"display",((_48f instanceof String||typeof _48f=="string")?_48f:(_48f?dojo.html.suggestDisplayByTagName(node):"none")));
};
dojo.html.isDisplayed=function(node){
return (dojo.html.getComputedStyle(node,"display")!="none");
};
dojo.html.toggleDisplay=function(node){
return dojo.html._toggle(node,dojo.html.isDisplayed,dojo.html.setDisplay);
};
dojo.html.setVisibility=function(node,_493){
dojo.html.setStyle(node,"visibility",((_493 instanceof String||typeof _493=="string")?_493:(_493?"visible":"hidden")));
};
dojo.html.isVisible=function(node){
return (dojo.html.getComputedStyle(node,"visibility")!="hidden");
};
dojo.html.toggleVisibility=function(node){
return dojo.html._toggle(node,dojo.html.isVisible,dojo.html.setVisibility);
};
dojo.html.setOpacity=function(node,_497,_498){
node=dojo.byId(node);
var h=dojo.render.html;
if(!_498){
if(_497>=1){
if(h.ie){
dojo.html.clearOpacity(node);
return;
}else{
_497=0.999999;
}
}else{
if(_497<0){
_497=0;
}
}
}
if(h.ie){
if(node.nodeName.toLowerCase()=="tr"){
var tds=node.getElementsByTagName("td");
for(var x=0;x<tds.length;x++){
tds[x].style.filter="Alpha(Opacity="+_497*100+")";
}
}
node.style.filter="Alpha(Opacity="+_497*100+")";
}else{
if(h.moz){
node.style.opacity=_497;
node.style.MozOpacity=_497;
}else{
if(h.safari){
node.style.opacity=_497;
node.style.KhtmlOpacity=_497;
}else{
node.style.opacity=_497;
}
}
}
};
dojo.html.clearOpacity=function(node){
node=dojo.byId(node);
var ns=node.style;
var h=dojo.render.html;
if(h.ie){
try{
if(node.filters&&node.filters.alpha){
ns.filter="";
}
}
catch(e){
}
}else{
if(h.moz){
ns.opacity=1;
ns.MozOpacity=1;
}else{
if(h.safari){
ns.opacity=1;
ns.KhtmlOpacity=1;
}else{
ns.opacity=1;
}
}
}
};
dojo.html.getOpacity=function(node){
node=dojo.byId(node);
var h=dojo.render.html;
if(h.ie){
var opac=(node.filters&&node.filters.alpha&&typeof node.filters.alpha.opacity=="number"?node.filters.alpha.opacity:100)/100;
}else{
var opac=node.style.opacity||node.style.MozOpacity||node.style.KhtmlOpacity||1;
}
return opac>=0.999999?1:Number(opac);
};
dojo.provide("dojo.html.color");
dojo.html.getBackgroundColor=function(node){
node=dojo.byId(node);
var _4a3;
do{
_4a3=dojo.html.getStyle(node,"background-color");
if(_4a3.toLowerCase()=="rgba(0, 0, 0, 0)"){
_4a3="transparent";
}
if(node==document.getElementsByTagName("body")[0]){
node=null;
break;
}
node=node.parentNode;
}while(node&&dojo.lang.inArray(["transparent",""],_4a3));
if(_4a3=="transparent"){
_4a3=[255,255,255,0];
}else{
_4a3=dojo.gfx.color.extractRGB(_4a3);
}
return _4a3;
};
dojo.provide("dojo.html.common");
dojo.lang.mixin(dojo.html,dojo.dom);
dojo.html.body=function(){
dojo.deprecated("dojo.html.body() moved to dojo.body()","0.5");
return dojo.body();
};
dojo.html.getEventTarget=function(evt){
if(!evt){
evt=dojo.global().event||{};
}
var t=(evt.srcElement?evt.srcElement:(evt.target?evt.target:null));
while((t)&&(t.nodeType!=1)){
t=t.parentNode;
}
return t;
};
dojo.html.getViewport=function(){
var _4a6=dojo.global();
var _4a7=dojo.doc();
var w=0;
var h=0;
if(dojo.render.html.mozilla){
w=_4a7.documentElement.clientWidth;
h=_4a6.innerHeight;
}else{
if(!dojo.render.html.opera&&_4a6.innerWidth){
w=_4a6.innerWidth;
h=_4a6.innerHeight;
}else{
if(!dojo.render.html.opera&&dojo.exists(_4a7,"documentElement.clientWidth")){
var w2=_4a7.documentElement.clientWidth;
if(!w||w2&&w2<w){
w=w2;
}
h=_4a7.documentElement.clientHeight;
}else{
if(dojo.body().clientWidth){
w=dojo.body().clientWidth;
h=dojo.body().clientHeight;
}
}
}
}
return {width:w,height:h};
};
dojo.html.getScroll=function(){
var _4ab=dojo.global();
var _4ac=dojo.doc();
var top=_4ab.pageYOffset||_4ac.documentElement.scrollTop||dojo.body().scrollTop||0;
var left=_4ab.pageXOffset||_4ac.documentElement.scrollLeft||dojo.body().scrollLeft||0;
return {top:top,left:left,offset:{x:left,y:top}};
};
dojo.html.getParentByType=function(node,type){
var _4b1=dojo.doc();
var _4b2=dojo.byId(node);
type=type.toLowerCase();
while((_4b2)&&(_4b2.nodeName.toLowerCase()!=type)){
if(_4b2==(_4b1["body"]||_4b1["documentElement"])){
return null;
}
_4b2=_4b2.parentNode;
}
return _4b2;
};
dojo.html.getAttribute=function(node,attr){
node=dojo.byId(node);
if((!node)||(!node.getAttribute)){
return null;
}
var ta=typeof attr=="string"?attr:new String(attr);
var v=node.getAttribute(ta.toUpperCase());
if((v)&&(typeof v=="string")&&(v!="")){
return v;
}
if(v&&v.value){
return v.value;
}
if((node.getAttributeNode)&&(node.getAttributeNode(ta))){
return (node.getAttributeNode(ta)).value;
}else{
if(node.getAttribute(ta)){
return node.getAttribute(ta);
}else{
if(node.getAttribute(ta.toLowerCase())){
return node.getAttribute(ta.toLowerCase());
}
}
}
return null;
};
dojo.html.hasAttribute=function(node,attr){
return dojo.html.getAttribute(dojo.byId(node),attr)?true:false;
};
dojo.html.getCursorPosition=function(e){
e=e||dojo.global().event;
var _4ba={x:0,y:0};
if(e.pageX||e.pageY){
_4ba.x=e.pageX;
_4ba.y=e.pageY;
}else{
var de=dojo.doc().documentElement;
var db=dojo.body();
_4ba.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);
_4ba.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);
}
return _4ba;
};
dojo.html.isTag=function(node){
node=dojo.byId(node);
if(node&&node.tagName){
for(var i=1;i<arguments.length;i++){
if(node.tagName.toLowerCase()==String(arguments[i]).toLowerCase()){
return String(arguments[i]).toLowerCase();
}
}
}
return "";
};
if(dojo.render.html.ie&&!dojo.render.html.ie70){
if(window.location.href.substr(0,6).toLowerCase()!="https:"){
(function(){
var _4bf=dojo.doc().createElement("script");
_4bf.src="javascript:'dojo.html.createExternalElement=function(doc, tag){ return doc.createElement(tag); }'";
dojo.doc().getElementsByTagName("head")[0].appendChild(_4bf);
})();
}
}else{
dojo.html.createExternalElement=function(doc,tag){
return doc.createElement(tag);
};
}
dojo.html._callDeprecated=function(_4c2,_4c3,args,_4c5,_4c6){
dojo.deprecated("dojo.html."+_4c2,"replaced by dojo.html."+_4c3+"("+(_4c5?"node, {"+_4c5+": "+_4c5+"}":"")+")"+(_4c6?"."+_4c6:""),"0.5");
var _4c7=[];
if(_4c5){
var _4c8={};
_4c8[_4c5]=args[1];
_4c7.push(args[0]);
_4c7.push(_4c8);
}else{
_4c7=args;
}
var ret=dojo.html[_4c3].apply(dojo.html,args);
if(_4c6){
return ret[_4c6];
}else{
return ret;
}
};
dojo.html.getViewportWidth=function(){
return dojo.html._callDeprecated("getViewportWidth","getViewport",arguments,null,"width");
};
dojo.html.getViewportHeight=function(){
return dojo.html._callDeprecated("getViewportHeight","getViewport",arguments,null,"height");
};
dojo.html.getViewportSize=function(){
return dojo.html._callDeprecated("getViewportSize","getViewport",arguments);
};
dojo.html.getScrollTop=function(){
return dojo.html._callDeprecated("getScrollTop","getScroll",arguments,null,"top");
};
dojo.html.getScrollLeft=function(){
return dojo.html._callDeprecated("getScrollLeft","getScroll",arguments,null,"left");
};
dojo.html.getScrollOffset=function(){
return dojo.html._callDeprecated("getScrollOffset","getScroll",arguments,null,"offset");
};
dojo.provide("dojo.html.layout");
dojo.html.sumAncestorProperties=function(node,prop){
node=dojo.byId(node);
if(!node){
return 0;
}
var _4cc=0;
while(node){
if(dojo.html.getComputedStyle(node,"position")=="fixed"){
return 0;
}
var val=node[prop];
if(val){
_4cc+=val-0;
if(node==dojo.body()){
break;
}
}
node=node.parentNode;
}
return _4cc;
};
dojo.html.setStyleAttributes=function(node,_4cf){
node=dojo.byId(node);
var _4d0=_4cf.replace(/(;)?\s*$/,"").split(";");
for(var i=0;i<_4d0.length;i++){
var _4d2=_4d0[i].split(":");
var name=_4d2[0].replace(/\s*$/,"").replace(/^\s*/,"").toLowerCase();
var _4d4=_4d2[1].replace(/\s*$/,"").replace(/^\s*/,"");
switch(name){
case "opacity":
dojo.html.setOpacity(node,_4d4);
break;
case "content-height":
dojo.html.setContentBox(node,{height:_4d4});
break;
case "content-width":
dojo.html.setContentBox(node,{width:_4d4});
break;
case "outer-height":
dojo.html.setMarginBox(node,{height:_4d4});
break;
case "outer-width":
dojo.html.setMarginBox(node,{width:_4d4});
break;
default:
node.style[dojo.html.toCamelCase(name)]=_4d4;
}
}
};
dojo.html.boxSizing={MARGIN_BOX:"margin-box",BORDER_BOX:"border-box",PADDING_BOX:"padding-box",CONTENT_BOX:"content-box"};
dojo.html.getAbsolutePosition=dojo.html.abs=function(node,_4d6,_4d7){
node=dojo.byId(node,node.ownerDocument);
var ret={x:0,y:0};
var bs=dojo.html.boxSizing;
if(!_4d7){
_4d7=bs.CONTENT_BOX;
}
var _4da=2;
var _4db;
switch(_4d7){
case bs.MARGIN_BOX:
_4db=3;
break;
case bs.BORDER_BOX:
_4db=2;
break;
case bs.PADDING_BOX:
default:
_4db=1;
break;
case bs.CONTENT_BOX:
_4db=0;
break;
}
var h=dojo.render.html;
var db=document["body"]||document["documentElement"];
if(h.ie){
with(node.getBoundingClientRect()){
ret.x=left-2;
ret.y=top-2;
}
}else{
if(document.getBoxObjectFor){
_4da=1;
try{
var bo=document.getBoxObjectFor(node);
ret.x=bo.x-dojo.html.sumAncestorProperties(node,"scrollLeft");
ret.y=bo.y-dojo.html.sumAncestorProperties(node,"scrollTop");
}
catch(e){
}
}else{
if(node["offsetParent"]){
var _4df;
if((h.safari)&&(node.style.getPropertyValue("position")=="absolute")&&(node.parentNode==db)){
_4df=db;
}else{
_4df=db.parentNode;
}
if(node.parentNode!=db){
var nd=node;
if(dojo.render.html.opera){
nd=db;
}
ret.x-=dojo.html.sumAncestorProperties(nd,"scrollLeft");
ret.y-=dojo.html.sumAncestorProperties(nd,"scrollTop");
}
var _4e1=node;
do{
var n=_4e1["offsetLeft"];
if(!h.opera||n>0){
ret.x+=isNaN(n)?0:n;
}
var m=_4e1["offsetTop"];
ret.y+=isNaN(m)?0:m;
_4e1=_4e1.offsetParent;
}while((_4e1!=_4df)&&(_4e1!=null));
}else{
if(node["x"]&&node["y"]){
ret.x+=isNaN(node.x)?0:node.x;
ret.y+=isNaN(node.y)?0:node.y;
}
}
}
}
if(_4d6){
var _4e4=dojo.html.getScroll();
ret.y+=_4e4.top;
ret.x+=_4e4.left;
}
var _4e5=[dojo.html.getPaddingExtent,dojo.html.getBorderExtent,dojo.html.getMarginExtent];
if(_4da>_4db){
for(var i=_4db;i<_4da;++i){
ret.y+=_4e5[i](node,"top");
ret.x+=_4e5[i](node,"left");
}
}else{
if(_4da<_4db){
for(var i=_4db;i>_4da;--i){
ret.y-=_4e5[i-1](node,"top");
ret.x-=_4e5[i-1](node,"left");
}
}
}
ret.top=ret.y;
ret.left=ret.x;
return ret;
};
dojo.html.isPositionAbsolute=function(node){
return (dojo.html.getComputedStyle(node,"position")=="absolute");
};
dojo.html._sumPixelValues=function(node,_4e9,_4ea){
var _4eb=0;
for(var x=0;x<_4e9.length;x++){
_4eb+=dojo.html.getPixelValue(node,_4e9[x],_4ea);
}
return _4eb;
};
dojo.html.getMargin=function(node){
return {width:dojo.html._sumPixelValues(node,["margin-left","margin-right"],(dojo.html.getComputedStyle(node,"position")=="absolute")),height:dojo.html._sumPixelValues(node,["margin-top","margin-bottom"],(dojo.html.getComputedStyle(node,"position")=="absolute"))};
};
dojo.html.getBorder=function(node){
return {width:dojo.html.getBorderExtent(node,"left")+dojo.html.getBorderExtent(node,"right"),height:dojo.html.getBorderExtent(node,"top")+dojo.html.getBorderExtent(node,"bottom")};
};
dojo.html.getBorderExtent=function(node,side){
return (dojo.html.getStyle(node,"border-"+side+"-style")=="none"?0:dojo.html.getPixelValue(node,"border-"+side+"-width"));
};
dojo.html.getMarginExtent=function(node,side){
return dojo.html._sumPixelValues(node,["margin-"+side],dojo.html.isPositionAbsolute(node));
};
dojo.html.getPaddingExtent=function(node,side){
return dojo.html._sumPixelValues(node,["padding-"+side],true);
};
dojo.html.getPadding=function(node){
return {width:dojo.html._sumPixelValues(node,["padding-left","padding-right"],true),height:dojo.html._sumPixelValues(node,["padding-top","padding-bottom"],true)};
};
dojo.html.getPadBorder=function(node){
var pad=dojo.html.getPadding(node);
var _4f8=dojo.html.getBorder(node);
return {width:pad.width+_4f8.width,height:pad.height+_4f8.height};
};
dojo.html.getBoxSizing=function(node){
var h=dojo.render.html;
var bs=dojo.html.boxSizing;
if((h.ie)||(h.opera)){
var cm=document["compatMode"];
if((cm=="BackCompat")||(cm=="QuirksMode")){
return bs.BORDER_BOX;
}else{
return bs.CONTENT_BOX;
}
}else{
if(arguments.length==0){
node=document.documentElement;
}
var _4fd=dojo.html.getStyle(node,"-moz-box-sizing");
if(!_4fd){
_4fd=dojo.html.getStyle(node,"box-sizing");
}
return (_4fd?_4fd:bs.CONTENT_BOX);
}
};
dojo.html.isBorderBox=function(node){
return (dojo.html.getBoxSizing(node)==dojo.html.boxSizing.BORDER_BOX);
};
dojo.html.getBorderBox=function(node){
node=dojo.byId(node);
return {width:node.offsetWidth,height:node.offsetHeight};
};
dojo.html.getPaddingBox=function(node){
var box=dojo.html.getBorderBox(node);
var _502=dojo.html.getBorder(node);
return {width:box.width-_502.width,height:box.height-_502.height};
};
dojo.html.getContentBox=function(node){
node=dojo.byId(node);
var _504=dojo.html.getPadBorder(node);
return {width:node.offsetWidth-_504.width,height:node.offsetHeight-_504.height};
};
dojo.html.setContentBox=function(node,args){
node=dojo.byId(node);
var _507=0;
var _508=0;
var isbb=dojo.html.isBorderBox(node);
var _50a=(isbb?dojo.html.getPadBorder(node):{width:0,height:0});
var ret={};
if(typeof args.width!="undefined"){
_507=args.width+_50a.width;
ret.width=dojo.html.setPositivePixelValue(node,"width",_507);
}
if(typeof args.height!="undefined"){
_508=args.height+_50a.height;
ret.height=dojo.html.setPositivePixelValue(node,"height",_508);
}
return ret;
};
dojo.html.getMarginBox=function(node){
var _50d=dojo.html.getBorderBox(node);
var _50e=dojo.html.getMargin(node);
return {width:_50d.width+_50e.width,height:_50d.height+_50e.height};
};
dojo.html.setMarginBox=function(node,args){
node=dojo.byId(node);
var _511=0;
var _512=0;
var isbb=dojo.html.isBorderBox(node);
var _514=(!isbb?dojo.html.getPadBorder(node):{width:0,height:0});
var _515=dojo.html.getMargin(node);
var ret={};
if(typeof args.width!="undefined"){
_511=args.width-_514.width;
_511-=_515.width;
ret.width=dojo.html.setPositivePixelValue(node,"width",_511);
}
if(typeof args.height!="undefined"){
_512=args.height-_514.height;
_512-=_515.height;
ret.height=dojo.html.setPositivePixelValue(node,"height",_512);
}
return ret;
};
dojo.html.getElementBox=function(node,type){
var bs=dojo.html.boxSizing;
switch(type){
case bs.MARGIN_BOX:
return dojo.html.getMarginBox(node);
case bs.BORDER_BOX:
return dojo.html.getBorderBox(node);
case bs.PADDING_BOX:
return dojo.html.getPaddingBox(node);
case bs.CONTENT_BOX:
default:
return dojo.html.getContentBox(node);
}
};
dojo.html.toCoordinateObject=dojo.html.toCoordinateArray=function(_51a,_51b,_51c){
if(_51a instanceof Array||typeof _51a=="array"){
dojo.deprecated("dojo.html.toCoordinateArray","use dojo.html.toCoordinateObject({left: , top: , width: , height: }) instead","0.5");
while(_51a.length<4){
_51a.push(0);
}
while(_51a.length>4){
_51a.pop();
}
var ret={left:_51a[0],top:_51a[1],width:_51a[2],height:_51a[3]};
}else{
if(!_51a.nodeType&&!(_51a instanceof String||typeof _51a=="string")&&("width" in _51a||"height" in _51a||"left" in _51a||"x" in _51a||"top" in _51a||"y" in _51a)){
var ret={left:_51a.left||_51a.x||0,top:_51a.top||_51a.y||0,width:_51a.width||0,height:_51a.height||0};
}else{
var node=dojo.byId(_51a);
var pos=dojo.html.abs(node,_51b,_51c);
var _520=dojo.html.getMarginBox(node);
var ret={left:pos.left,top:pos.top,width:_520.width,height:_520.height};
}
}
ret.x=ret.left;
ret.y=ret.top;
return ret;
};
dojo.html.setMarginBoxWidth=dojo.html.setOuterWidth=function(node,_522){
return dojo.html._callDeprecated("setMarginBoxWidth","setMarginBox",arguments,"width");
};
dojo.html.setMarginBoxHeight=dojo.html.setOuterHeight=function(){
return dojo.html._callDeprecated("setMarginBoxHeight","setMarginBox",arguments,"height");
};
dojo.html.getMarginBoxWidth=dojo.html.getOuterWidth=function(){
return dojo.html._callDeprecated("getMarginBoxWidth","getMarginBox",arguments,null,"width");
};
dojo.html.getMarginBoxHeight=dojo.html.getOuterHeight=function(){
return dojo.html._callDeprecated("getMarginBoxHeight","getMarginBox",arguments,null,"height");
};
dojo.html.getTotalOffset=function(node,type,_525){
return dojo.html._callDeprecated("getTotalOffset","getAbsolutePosition",arguments,null,type);
};
dojo.html.getAbsoluteX=function(node,_527){
return dojo.html._callDeprecated("getAbsoluteX","getAbsolutePosition",arguments,null,"x");
};
dojo.html.getAbsoluteY=function(node,_529){
return dojo.html._callDeprecated("getAbsoluteY","getAbsolutePosition",arguments,null,"y");
};
dojo.html.totalOffsetLeft=function(node,_52b){
return dojo.html._callDeprecated("totalOffsetLeft","getAbsolutePosition",arguments,null,"left");
};
dojo.html.totalOffsetTop=function(node,_52d){
return dojo.html._callDeprecated("totalOffsetTop","getAbsolutePosition",arguments,null,"top");
};
dojo.html.getMarginWidth=function(node){
return dojo.html._callDeprecated("getMarginWidth","getMargin",arguments,null,"width");
};
dojo.html.getMarginHeight=function(node){
return dojo.html._callDeprecated("getMarginHeight","getMargin",arguments,null,"height");
};
dojo.html.getBorderWidth=function(node){
return dojo.html._callDeprecated("getBorderWidth","getBorder",arguments,null,"width");
};
dojo.html.getBorderHeight=function(node){
return dojo.html._callDeprecated("getBorderHeight","getBorder",arguments,null,"height");
};
dojo.html.getPaddingWidth=function(node){
return dojo.html._callDeprecated("getPaddingWidth","getPadding",arguments,null,"width");
};
dojo.html.getPaddingHeight=function(node){
return dojo.html._callDeprecated("getPaddingHeight","getPadding",arguments,null,"height");
};
dojo.html.getPadBorderWidth=function(node){
return dojo.html._callDeprecated("getPadBorderWidth","getPadBorder",arguments,null,"width");
};
dojo.html.getPadBorderHeight=function(node){
return dojo.html._callDeprecated("getPadBorderHeight","getPadBorder",arguments,null,"height");
};
dojo.html.getBorderBoxWidth=dojo.html.getInnerWidth=function(){
return dojo.html._callDeprecated("getBorderBoxWidth","getBorderBox",arguments,null,"width");
};
dojo.html.getBorderBoxHeight=dojo.html.getInnerHeight=function(){
return dojo.html._callDeprecated("getBorderBoxHeight","getBorderBox",arguments,null,"height");
};
dojo.html.getContentBoxWidth=dojo.html.getContentWidth=function(){
return dojo.html._callDeprecated("getContentBoxWidth","getContentBox",arguments,null,"width");
};
dojo.html.getContentBoxHeight=dojo.html.getContentHeight=function(){
return dojo.html._callDeprecated("getContentBoxHeight","getContentBox",arguments,null,"height");
};
dojo.html.setContentBoxWidth=dojo.html.setContentWidth=function(node,_537){
return dojo.html._callDeprecated("setContentBoxWidth","setContentBox",arguments,"width");
};
dojo.html.setContentBoxHeight=dojo.html.setContentHeight=function(node,_539){
return dojo.html._callDeprecated("setContentBoxHeight","setContentBox",arguments,"height");
};
dojo.provide("dojo.lfx.html");
dojo.lfx.html._byId=function(_53a){
if(!_53a){
return [];
}
if(dojo.lang.isArrayLike(_53a)){
if(!_53a.alreadyChecked){
var n=[];
dojo.lang.forEach(_53a,function(node){
n.push(dojo.byId(node));
});
n.alreadyChecked=true;
return n;
}else{
return _53a;
}
}else{
var n=[];
n.push(dojo.byId(_53a));
n.alreadyChecked=true;
return n;
}
};
dojo.lfx.html.propertyAnimation=function(_53d,_53e,_53f,_540,_541){
_53d=dojo.lfx.html._byId(_53d);
var _542={"propertyMap":_53e,"nodes":_53d,"duration":_53f,"easing":_540||dojo.lfx.easeDefault};
var _543=function(args){
if(args.nodes.length==1){
var pm=args.propertyMap;
if(!dojo.lang.isArray(args.propertyMap)){
var parr=[];
for(var _547 in pm){
pm[_547].property=_547;
parr.push(pm[_547]);
}
pm=args.propertyMap=parr;
}
dojo.lang.forEach(pm,function(prop){
if(dj_undef("start",prop)){
if(prop.property!="opacity"){
prop.start=parseInt(dojo.html.getComputedStyle(args.nodes[0],prop.property));
}else{
prop.start=dojo.html.getOpacity(args.nodes[0]);
}
}
});
}
};
var _549=function(_54a){
var _54b=[];
dojo.lang.forEach(_54a,function(c){
_54b.push(Math.round(c));
});
return _54b;
};
var _54d=function(n,_54f){
n=dojo.byId(n);
if(!n||!n.style){
return;
}
for(var s in _54f){
try{
if(s=="opacity"){
dojo.html.setOpacity(n,_54f[s]);
}else{
n.style[s]=_54f[s];
}
}
catch(e){
dojo.debug(e);
}
}
};
var _551=function(_552){
this._properties=_552;
this.diffs=new Array(_552.length);
dojo.lang.forEach(_552,function(prop,i){
if(dojo.lang.isFunction(prop.start)){
prop.start=prop.start(prop,i);
}
if(dojo.lang.isFunction(prop.end)){
prop.end=prop.end(prop,i);
}
if(dojo.lang.isArray(prop.start)){
this.diffs[i]=null;
}else{
if(prop.start instanceof dojo.gfx.color.Color){
prop.startRgb=prop.start.toRgb();
prop.endRgb=prop.end.toRgb();
}else{
this.diffs[i]=prop.end-prop.start;
}
}
},this);
this.getValue=function(n){
var ret={};
dojo.lang.forEach(this._properties,function(prop,i){
var _559=null;
if(dojo.lang.isArray(prop.start)){
}else{
if(prop.start instanceof dojo.gfx.color.Color){
_559=(prop.units||"rgb")+"(";
for(var j=0;j<prop.startRgb.length;j++){
_559+=Math.round(((prop.endRgb[j]-prop.startRgb[j])*n)+prop.startRgb[j])+(j<prop.startRgb.length-1?",":"");
}
_559+=")";
}else{
_559=((this.diffs[i])*n)+prop.start+(prop.property!="opacity"?prop.units||"px":"");
}
}
ret[dojo.html.toCamelCase(prop.property)]=_559;
},this);
return ret;
};
};
var anim=new dojo.lfx.Animation({beforeBegin:function(){
_543(_542);
anim.curve=new _551(_542.propertyMap);
},onAnimate:function(_55c){
dojo.lang.forEach(_542.nodes,function(node){
_54d(node,_55c);
});
}},_542.duration,null,_542.easing);
if(_541){
for(var x in _541){
if(dojo.lang.isFunction(_541[x])){
anim.connect(x,anim,_541[x]);
}
}
}
return anim;
};
dojo.lfx.html._makeFadeable=function(_55f){
var _560=function(node){
if(dojo.render.html.ie){
if((node.style.zoom.length==0)&&(dojo.html.getStyle(node,"zoom")=="normal")){
node.style.zoom="1";
}
if((node.style.width.length==0)&&(dojo.html.getStyle(node,"width")=="auto")){
node.style.width="auto";
}
}
};
if(dojo.lang.isArrayLike(_55f)){
dojo.lang.forEach(_55f,_560);
}else{
_560(_55f);
}
};
dojo.lfx.html.fade=function(_562,_563,_564,_565,_566){
_562=dojo.lfx.html._byId(_562);
var _567={property:"opacity"};
if(!dj_undef("start",_563)){
_567.start=_563.start;
}else{
_567.start=function(){
return dojo.html.getOpacity(_562[0]);
};
}
if(!dj_undef("end",_563)){
_567.end=_563.end;
}else{
dojo.raise("dojo.lfx.html.fade needs an end value");
}
var anim=dojo.lfx.propertyAnimation(_562,[_567],_564,_565);
anim.connect("beforeBegin",function(){
dojo.lfx.html._makeFadeable(_562);
});
if(_566){
anim.connect("onEnd",function(){
_566(_562,anim);
});
}
return anim;
};
dojo.lfx.html.fadeIn=function(_569,_56a,_56b,_56c){
return dojo.lfx.html.fade(_569,{end:1},_56a,_56b,_56c);
};
dojo.lfx.html.fadeOut=function(_56d,_56e,_56f,_570){
return dojo.lfx.html.fade(_56d,{end:0},_56e,_56f,_570);
};
dojo.lfx.html.fadeShow=function(_571,_572,_573,_574){
_571=dojo.lfx.html._byId(_571);
dojo.lang.forEach(_571,function(node){
dojo.html.setOpacity(node,0);
});
var anim=dojo.lfx.html.fadeIn(_571,_572,_573,_574);
anim.connect("beforeBegin",function(){
if(dojo.lang.isArrayLike(_571)){
dojo.lang.forEach(_571,dojo.html.show);
}else{
dojo.html.show(_571);
}
});
return anim;
};
dojo.lfx.html.fadeHide=function(_577,_578,_579,_57a){
var anim=dojo.lfx.html.fadeOut(_577,_578,_579,function(){
if(dojo.lang.isArrayLike(_577)){
dojo.lang.forEach(_577,dojo.html.hide);
}else{
dojo.html.hide(_577);
}
if(_57a){
_57a(_577,anim);
}
});
return anim;
};
dojo.lfx.html.wipeIn=function(_57c,_57d,_57e,_57f){
_57c=dojo.lfx.html._byId(_57c);
var _580=[];
dojo.lang.forEach(_57c,function(node){
var _582={};
dojo.html.show(node);
var _583=dojo.html.getBorderBox(node).height;
dojo.html.hide(node);
var anim=dojo.lfx.propertyAnimation(node,{"height":{start:1,end:function(){
return _583;
}}},_57d,_57e);
anim.connect("beforeBegin",function(){
_582.overflow=node.style.overflow;
_582.height=node.style.height;
with(node.style){
overflow="hidden";
_583="1px";
}
dojo.html.show(node);
});
anim.connect("onEnd",function(){
with(node.style){
overflow=_582.overflow;
_583=_582.height;
}
if(_57f){
_57f(node,anim);
}
});
_580.push(anim);
});
return dojo.lfx.combine(_580);
};
dojo.lfx.html.wipeOut=function(_585,_586,_587,_588){
_585=dojo.lfx.html._byId(_585);
var _589=[];
dojo.lang.forEach(_585,function(node){
var _58b={};
var anim=dojo.lfx.propertyAnimation(node,{"height":{start:function(){
return dojo.html.getContentBox(node).height;
},end:1}},_586,_587,{"beforeBegin":function(){
_58b.overflow=node.style.overflow;
_58b.height=node.style.height;
with(node.style){
overflow="hidden";
}
dojo.html.show(node);
},"onEnd":function(){
dojo.html.hide(node);
with(node.style){
overflow=_58b.overflow;
height=_58b.height;
}
if(_588){
_588(node,anim);
}
}});
_589.push(anim);
});
return dojo.lfx.combine(_589);
};
dojo.lfx.html.slideTo=function(_58d,_58e,_58f,_590,_591){
_58d=dojo.lfx.html._byId(_58d);
var _592=[];
var _593=dojo.html.getComputedStyle;
if(dojo.lang.isArray(_58e)){
dojo.deprecated("dojo.lfx.html.slideTo(node, array)","use dojo.lfx.html.slideTo(node, {top: value, left: value});","0.5");
_58e={top:_58e[0],left:_58e[1]};
}
dojo.lang.forEach(_58d,function(node){
var top=null;
var left=null;
var init=(function(){
var _598=node;
return function(){
var pos=_593(_598,"position");
top=(pos=="absolute"?node.offsetTop:parseInt(_593(node,"top"))||0);
left=(pos=="absolute"?node.offsetLeft:parseInt(_593(node,"left"))||0);
if(!dojo.lang.inArray(["absolute","relative"],pos)){
var ret=dojo.html.abs(_598,true);
dojo.html.setStyleAttributes(_598,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");
top=ret.y;
left=ret.x;
}
};
})();
init();
var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:(_58e.top||0)},"left":{start:left,end:(_58e.left||0)}},_58f,_590,{"beforeBegin":init});
if(_591){
anim.connect("onEnd",function(){
_591(_58d,anim);
});
}
_592.push(anim);
});
return dojo.lfx.combine(_592);
};
dojo.lfx.html.slideBy=function(_59c,_59d,_59e,_59f,_5a0){
_59c=dojo.lfx.html._byId(_59c);
var _5a1=[];
var _5a2=dojo.html.getComputedStyle;
if(dojo.lang.isArray(_59d)){
dojo.deprecated("dojo.lfx.html.slideBy(node, array)","use dojo.lfx.html.slideBy(node, {top: value, left: value});","0.5");
_59d={top:_59d[0],left:_59d[1]};
}
dojo.lang.forEach(_59c,function(node){
var top=null;
var left=null;
var init=(function(){
var _5a7=node;
return function(){
var pos=_5a2(_5a7,"position");
top=(pos=="absolute"?node.offsetTop:parseInt(_5a2(node,"top"))||0);
left=(pos=="absolute"?node.offsetLeft:parseInt(_5a2(node,"left"))||0);
if(!dojo.lang.inArray(["absolute","relative"],pos)){
var ret=dojo.html.abs(_5a7,true);
dojo.html.setStyleAttributes(_5a7,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");
top=ret.y;
left=ret.x;
}
};
})();
init();
var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:top+(_59d.top||0)},"left":{start:left,end:left+(_59d.left||0)}},_59e,_59f).connect("beforeBegin",init);
if(_5a0){
anim.connect("onEnd",function(){
_5a0(_59c,anim);
});
}
_5a1.push(anim);
});
return dojo.lfx.combine(_5a1);
};
dojo.lfx.html.explode=function(_5ab,_5ac,_5ad,_5ae,_5af){
var h=dojo.html;
_5ab=dojo.byId(_5ab);
_5ac=dojo.byId(_5ac);
var _5b1=h.toCoordinateObject(_5ab,true);
var _5b2=document.createElement("div");
h.copyStyle(_5b2,_5ac);
if(_5ac.explodeClassName){
_5b2.className=_5ac.explodeClassName;
}
with(_5b2.style){
position="absolute";
display="none";
var _5b3=h.getStyle(_5ab,"background-color");
backgroundColor=_5b3?_5b3.toLowerCase():"transparent";
backgroundColor=(backgroundColor=="transparent")?"rgb(221, 221, 221)":backgroundColor;
}
dojo.body().appendChild(_5b2);
with(_5ac.style){
visibility="hidden";
display="block";
}
var _5b4=h.toCoordinateObject(_5ac,true);
with(_5ac.style){
display="none";
visibility="visible";
}
var _5b5={opacity:{start:0.5,end:1}};
dojo.lang.forEach(["height","width","top","left"],function(type){
_5b5[type]={start:_5b1[type],end:_5b4[type]};
});
var anim=new dojo.lfx.propertyAnimation(_5b2,_5b5,_5ad,_5ae,{"beforeBegin":function(){
h.setDisplay(_5b2,"block");
},"onEnd":function(){
h.setDisplay(_5ac,"block");
_5b2.parentNode.removeChild(_5b2);
}});
if(_5af){
anim.connect("onEnd",function(){
_5af(_5ac,anim);
});
}
return anim;
};
dojo.lfx.html.implode=function(_5b8,end,_5ba,_5bb,_5bc){
var h=dojo.html;
_5b8=dojo.byId(_5b8);
end=dojo.byId(end);
var _5be=dojo.html.toCoordinateObject(_5b8,true);
var _5bf=dojo.html.toCoordinateObject(end,true);
var _5c0=document.createElement("div");
dojo.html.copyStyle(_5c0,_5b8);
if(_5b8.explodeClassName){
_5c0.className=_5b8.explodeClassName;
}
dojo.html.setOpacity(_5c0,0.3);
with(_5c0.style){
position="absolute";
display="none";
backgroundColor=h.getStyle(_5b8,"background-color").toLowerCase();
}
dojo.body().appendChild(_5c0);
var _5c1={opacity:{start:1,end:0.5}};
dojo.lang.forEach(["height","width","top","left"],function(type){
_5c1[type]={start:_5be[type],end:_5bf[type]};
});
var anim=new dojo.lfx.propertyAnimation(_5c0,_5c1,_5ba,_5bb,{"beforeBegin":function(){
dojo.html.hide(_5b8);
dojo.html.show(_5c0);
},"onEnd":function(){
_5c0.parentNode.removeChild(_5c0);
}});
if(_5bc){
anim.connect("onEnd",function(){
_5bc(_5b8,anim);
});
}
return anim;
};
dojo.lfx.html.highlight=function(_5c4,_5c5,_5c6,_5c7,_5c8){
_5c4=dojo.lfx.html._byId(_5c4);
var _5c9=[];
dojo.lang.forEach(_5c4,function(node){
var _5cb=dojo.html.getBackgroundColor(node);
var bg=dojo.html.getStyle(node,"background-color").toLowerCase();
var _5cd=dojo.html.getStyle(node,"background-image");
var _5ce=(bg=="transparent"||bg=="rgba(0, 0, 0, 0)");
while(_5cb.length>3){
_5cb.pop();
}
var rgb=new dojo.gfx.color.Color(_5c5);
var _5d0=new dojo.gfx.color.Color(_5cb);
var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:rgb,end:_5d0}},_5c6,_5c7,{"beforeBegin":function(){
if(_5cd){
node.style.backgroundImage="none";
}
node.style.backgroundColor="rgb("+rgb.toRgb().join(",")+")";
},"onEnd":function(){
if(_5cd){
node.style.backgroundImage=_5cd;
}
if(_5ce){
node.style.backgroundColor="transparent";
}
if(_5c8){
_5c8(node,anim);
}
}});
_5c9.push(anim);
});
return dojo.lfx.combine(_5c9);
};
dojo.lfx.html.unhighlight=function(_5d2,_5d3,_5d4,_5d5,_5d6){
_5d2=dojo.lfx.html._byId(_5d2);
var _5d7=[];
dojo.lang.forEach(_5d2,function(node){
var _5d9=new dojo.gfx.color.Color(dojo.html.getBackgroundColor(node));
var rgb=new dojo.gfx.color.Color(_5d3);
var _5db=dojo.html.getStyle(node,"background-image");
var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:_5d9,end:rgb}},_5d4,_5d5,{"beforeBegin":function(){
if(_5db){
node.style.backgroundImage="none";
}
node.style.backgroundColor="rgb("+_5d9.toRgb().join(",")+")";
},"onEnd":function(){
if(_5d6){
_5d6(node,anim);
}
}});
_5d7.push(anim);
});
return dojo.lfx.combine(_5d7);
};
dojo.lang.mixin(dojo.lfx,dojo.lfx.html);
dojo.provide("dojo.lfx.*");

