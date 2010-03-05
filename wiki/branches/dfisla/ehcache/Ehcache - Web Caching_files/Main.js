
if(!window.WIDGETBOX)(function(){var initialized=false;var pageLoadCallback=function(){WIDGETBOX.setPageLoaded();};var pageUnloadCallback=function(){WIDGETBOX.setPageUnloaded();};WIDGETBOX={libs:{},globals:{token:"",tokenTime:0,widgets:[],widgetCount:0,pageLoaded:false,pageUnloaded:false,pageLoadListeners:[],pageUnloadListeners:[],panels:[],panelCount:0,showPanelMarks:true,suppressGetWidget:false,disableGetWidget:false,suppressQuantcast:false,enableLogging:false,disableHitTracking:false,log:"",trustedPage:false,disableInstallerMenu:false,renderInstallerMenuInline:false,anchorEl:null},init:function(){if(!initialized){initialized=true;if(window.WIDGETBOXLOADLISTENERS){for(var i=0;i<WIDGETBOXLOADLISTENERS.length;i++){var listener=WIDGETBOXLOADLISTENERS[i];self.addPageLoadListener(listener);}}
self.addEvent(window,"load",pageLoadCallback);self.addEvent(window,"unload",pageUnloadCallback);if(window.WIDGETBOXINITLISTENERS){for(var i=0;i<WIDGETBOXINITLISTENERS.length;i++){var listener=WIDGETBOXINITLISTENERS[i];try{listener();}
catch(e){WIDGETBOX.logMessage(e);}}}}},namespace:function(ns){ns=ns.replace(".","/");if(ns.indexOf("WIDGETBOX/")==0)ns=ns.substr(10);if(ns.indexOf("POSTAPP/")==0)ns=ns.substr(8);var nodes=ns.split("/");var last=WIDGETBOX;if(nodes){for(var i=0;i<nodes.length;i++){var node=nodes[i];if(!last[node])last[node]=new Object();last=last[node];}}
return ns;},logMessage:function(msg){if(self.globals.enableLogging){self.globals.log+=(msg+"\n");}},newLibInfo:function(libPath){libPath=libPath.replace(".","/");var libName=libPath;var ns="";var nameDelim=libPath.lastIndexOf("/");if(nameDelim>=0){ns=libPath.substr(0,nameDelim);libName=libPath.substr(nameDelim+1);}
ns=self.namespace(ns);libPath=ns+"/"+libName;var libID=libPath.replace("/","_").toLowerCase();var result={ns:ns,name:libName,path:libPath,id:libID,status:null,callback:null};return result;},load:function(libPath,onLoadListener,useCdn){var libInfo=self.newLibInfo(libPath);if(!WIDGETBOX.libs[libInfo.id]){WIDGETBOX.libs[libInfo.id]=libInfo;libInfo.status="loading";if(onLoadListener){if(!libInfo.listeners)libInfo.listeners=new Array();libInfo.listeners.push(onLoadListener);}
var script=document.createElement("script");script.id="widgetbox_lib_"+libInfo.id;script.type="text/javascript";script.setAttribute('async','true');if(!useCdn){script.src="http://widgetserver.com/syndication/"+libInfo.path+".js?33124";}else{script.src="http://cdn.widgetserver.com/syndication/"+libInfo.path+".js?33124";}
var head=document.documentElement.firstChild;if(!head)head=document.getElementsByName("head")[0];head.appendChild(script);}
else{libInfo=WIDGETBOX.libs[libInfo.id];if(onLoadListener){if(libInfo.status=="ready"){try{onLoadListener(libInfo);}
catch(e){WIDGETBOX.logMessage(e);}}
else{if(!libInfo.listeners)libInfo.listeners=new Array();libInfo.listeners.push(onLoadListener);}}}},ready:function(libPath){var libInfo=self.newLibInfo(libPath);if(!WIDGETBOX.libs[libInfo.id]){WIDGETBOX.libs[libInfo.id]=libInfo;}
libInfo=WIDGETBOX.libs[libInfo.id];return libInfo.status=="ready";},setReady:function(libPath){var libInfo=self.newLibInfo(libPath);if(!WIDGETBOX.libs[libInfo.id]){WIDGETBOX.libs[libInfo.id]=libInfo;}
libInfo=WIDGETBOX.libs[libInfo.id];libInfo.status="ready";if(libInfo.listeners){for(var i=0;i<libInfo.listeners.length;i++){var listener=libInfo.listeners[i];try{listener(libInfo);}
catch(e){WIDGETBOX.logMessage(e);}}}},addEvent:function(obj,evType,fn,useCapture){if(obj.addEventListener){obj.addEventListener(evType,fn,useCapture);return true;}else if(obj.attachEvent){var r=obj.attachEvent("on"+evType,fn);return r;}else{alert("Handler could not be attached");}},removeEvent:function(obj,evType,fn,useCapture){if(obj.removeEventListener){obj.removeEventListener(evType,fn,useCapture);return true;}else if(obj.detachEvent){var r=obj.detachEvent("on"+evType,fn);return r;}else{alert("Handler could not be removed");}},addPageLoadListener:function(onLoadListener){if(onLoadListener){if(!self.globals.pageLoaded){self.globals.pageLoadListeners.push(onLoadListener);}
else{try{onLoadListener();}
catch(e){WIDGETBOX.logMessage(e);}}}},addPageUnloadListener:function(onUnloadListener){if(onUnloadListener){if(!self.globals.pageUnloaded){self.globals.pageUnloadListeners.push(onUnloadListener);}
else{try{onUnloadListener();}
catch(e){WIDGETBOX.logMessage(e);}}}},setPageLoaded:function(){if(self.globals.pageLoaded)return;self.globals.pageLoaded=true;self.removeEvent(window,"load",pageLoadCallback);var listeners=self.globals.pageLoadListeners;for(var i=0;i<listeners.length;i++){var listener=listeners[i];try{listener();}
catch(e){WIDGETBOX.logMessage(e);}}},setPageUnloaded:function(){if(self.globals.pageUnloaded)return;self.globals.pageUnloaded=true;self.removeEvent(window,"unload",pageUnloadCallback);var listeners=self.globals.pageUnloadListeners;for(var i=0;i<listeners.length;i++){var listener=listeners[i];try{listener();}
catch(e){WIDGETBOX.logMessage(e);}}}};POSTAPP=WIDGETBOX;var self=WIDGETBOX;self.init();})();WIDGETBOX.namespace("flash");if(!WIDGETBOX.flash.Utils)(function(){var isIE=(navigator.appVersion.indexOf("MSIE")!=-1)?true:false;var isWin=(navigator.appVersion.toLowerCase().indexOf("win")!=-1)?true:false;var isOpera=(navigator.userAgent.indexOf("Opera")!=-1)?true:false;WIDGETBOX.flash.Utils={isIE:function(){return isIE;},isWin:function(){return isWin;},isOpera:function(){return isOpera;},getMovie:function(movieName){if(isIE){return window[movieName]}
else{return document[movieName]}}}
function ControlVersion()
{var version;var axo;var e;try{axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash.7");version=axo.GetVariable("$version");}catch(e){}
if(!version)
{try{axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash.6");version="WIN 6,0,21,0";axo.AllowScriptAccess="always";version=axo.GetVariable("$version");}catch(e){}}
if(!version)
{try{axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash.3");version=axo.GetVariable("$version");}catch(e){}}
if(!version)
{try{axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash.3");version="WIN 3,0,18,0";}catch(e){}}
if(!version)
{try{axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash");version="WIN 2,0,0,11";}catch(e){version=-1;}}
return version;}
function GetSwfVer(){var flashVer=-1;if(navigator.plugins!=null&&navigator.plugins.length>0){if(navigator.plugins["Shockwave Flash 2.0"]||navigator.plugins["Shockwave Flash"]){var swVer2=navigator.plugins["Shockwave Flash 2.0"]?" 2.0":"";var flashDescription=navigator.plugins["Shockwave Flash"+swVer2].description;var descArray=flashDescription.split(" ");var tempArrayMajor=descArray[2].split(".");var versionMajor=tempArrayMajor[0];var versionMinor=tempArrayMajor[1];if(descArray[3]!=""){tempArrayMinor=descArray[3].split("r");}else{tempArrayMinor=descArray[4].split("r");}
var versionRevision=tempArrayMinor[1]>0?tempArrayMinor[1]:0;var flashVer=versionMajor+"."+versionMinor+"."+versionRevision;}}
else if(navigator.userAgent.toLowerCase().indexOf("webtv/2.6")!=-1)flashVer=4;else if(navigator.userAgent.toLowerCase().indexOf("webtv/2.5")!=-1)flashVer=3;else if(navigator.userAgent.toLowerCase().indexOf("webtv")!=-1)flashVer=2;else if(isIE&&isWin&&!isOpera){flashVer=ControlVersion();}
return flashVer;}
function DetectFlashVer(reqMajorVer,reqMinorVer,reqRevision)
{versionStr=GetSwfVer();if(versionStr==-1){return false;}else if(versionStr!=0){if(isIE&&isWin&&!isOpera){tempArray=versionStr.split(" ");tempString=tempArray[1];versionArray=tempString.split(",");}else{versionArray=versionStr.split(".");}
var versionMajor=versionArray[0];var versionMinor=versionArray[1];var versionRevision=versionArray[2];if(versionMajor>parseFloat(reqMajorVer)){return true;}else if(versionMajor==parseFloat(reqMajorVer)){if(versionMinor>parseFloat(reqMinorVer))
return true;else if(versionMinor==parseFloat(reqMinorVer)){if(versionRevision>=parseFloat(reqRevision))
return true;}}
return false;}}
function GetFlashVersionArray(){var version=new Array(3);version[0]=-1;version[1]=-1;version[2]=-1;var versionStr=GetSwfVer();if(isIE){var versionParts=versionStr.split(" ");if(versionParts.length==2){versionParts=versionParts[1].split(",");if(versionParts.length==4){version[0]=parseInt(versionParts[0]);version[1]=parseInt(versionParts[1]);version[2]=parseInt(versionParts[2]);}}}else{var versionParts=versionStr.split(".");if(versionParts.length==3){version[0]=parseInt(versionParts[0]);version[1]=parseInt(versionParts[1]);version[2]=parseInt(versionParts[2]);}}
return version;}
function Exception(name,message)
{if(name)
this.name=name;if(message)
this.message=message;}
Exception.prototype.setName=function(name)
{this.name=name;}
Exception.prototype.getName=function()
{return this.name;}
Exception.prototype.setMessage=function(msg)
{this.message=msg;}
Exception.prototype.getMessage=function()
{return this.message;}
function FlashTag(src,width,height,version)
{if(arguments.length<4)
{throw new Exception('RequiredParameterException','You must pass in a src, width, height, and version when creating a FlashTag.');}
this.src=src;this.width=width;this.height=height;this.version=version;this.id=null;this.flashVars=null;this.flashVarsStr=null;this.genericParam=new Object();this.ie=(navigator.appName.indexOf("Microsoft")!=-1)?1:0;this.enableFSCommand=false;}
FlashTag.prototype.setSource=function(src)
{this.src=src;}
FlashTag.prototype.setWidth=function(w)
{this.width=w;}
FlashTag.prototype.setHeight=function(h)
{this.height=h;}
FlashTag.prototype.setVersion=function(v)
{this.version=v;}
FlashTag.prototype.setId=function(id)
{this.id=id;}
FlashTag.prototype.setBgcolor=function(bgc)
{if(bgc.charAt(0)!='#')
{bgc='#'+bgc;}
this.genericParam['bgcolor']=bgc;}
FlashTag.prototype.addParams=function(params)
{for(var paramName in params){var paramValue=params[paramName];this.genericParam[paramName]=paramValue;}}
FlashTag.prototype.addFlashVars=function(fvs)
{this.flashVarsStr=fvs;}
FlashTag.prototype.addFlashVarsParams=function(params)
{if(!this.flashVars)this.flashVars=new Object();for(var paramName in params){var paramValue=params[paramName];this.flashVars[paramName]=paramValue;}}
FlashTag.prototype.addFlashVar=function(n,v)
{if(!this.flashVars)this.flashVars=new Object();this.flashVars[n]=v;}
FlashTag.prototype.removeFlashVar=function(n)
{if(this.flashVars)
{this.flashVars[n]=null;}}
FlashTag.prototype.setSwliveconnect=function(swlc)
{this.genericParam['swliveconnect']=swlc;}
FlashTag.prototype.setPlay=function(p)
{this.genericParam['play']=p;}
FlashTag.prototype.setLoop=function(l)
{this.genericParam['loop']=l;}
FlashTag.prototype.setMenu=function(m)
{this.genericParam['menu']=m;}
FlashTag.prototype.setQuality=function(q)
{if(q!='low'&&q!='high'&&q!='autolow'&&q!='autohigh'&&q!='best')
{throw new Exception('UnsupportedValueException','Supported values are "low", "high", "autolow", "autohigh", and "best".');}
this.genericParam['quality']=q;}
FlashTag.prototype.setScale=function(sc)
{if(sc!='showall'&&sc!='noborder'&&sc!='exactfit'&&sc!='noscale')
{throw new Exception('UnsupportedValueException','Supported values are "showall", "noborder", and "exactfit".');}
this.genericParam['scale']=sc;}
FlashTag.prototype.setAlign=function(a)
{if(a!='l'&&a!='t'&&a!='r'&&a!='b')
{throw new Exception('UnsupportedValueException','Supported values are "l", "t", "r" and "b".');}
this.genericParam['align']=a;}
FlashTag.prototype.setSalign=function(sa)
{if(sa!='l'&&sa!='t'&&sa!='r'&&sa!='b'&&sa!='tl'&&sa!='tr'&&sa!='bl'&&sa!='br')
{throw new Exception('UnsupportedValueException','Supported values are "l", "t", "r", "b", "tl", "tr", "bl" and "br".');}
this.genericParam['salign']=sa;}
FlashTag.prototype.setWmode=function(wm)
{if(wm!='window'&&wm!='opaque'&&wm!='transparent')
{throw new Exception('UnsupportedValueException','Supported values are "window", "opaque", and "transparent".');}
this.genericParam['wmode']=wm;}
FlashTag.prototype.setBase=function(base)
{this.genericParam['base']=base;}
FlashTag.prototype.setAllowScriptAccess=function(sa)
{if(sa!='never'&&sa!='always'&&sa!='sameDomain')
{throw new Exception('UnsupportedValueException','Supported values are "never" and "always", and "sameDomain".');}
this.genericParam['allowScriptAccess']=sa;}
FlashTag.prototype.setAllowNetworking=function(na)
{if(na!='all'&&na!='internal'&&na!='none')
{throw new Exception('UnsupportedValueException','Supported values are "all", "internal", and "none".');}
this.genericParam['allowNetworking']=na;}
FlashTag.prototype.setEnableFSCommand=function(enableFSCommand)
{this.enableFSCommand=enableFSCommand;}
FlashTag.prototype.toString=function()
{var flashTag=new String();if(this.ie)
{flashTag+='<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" ';if(this.id)
{flashTag+='id="'+this.id+'" ';}
flashTag+='codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version='+this.version+'" ';if(String(this.width).indexOf("%")>=0||this.width>=0){flashTag+='width="'+this.width+'" ';flashTag+='height="'+this.height+'"';}
flashTag+='>';flashTag+='<param name="movie" value="'+this.src+'"/>';var wmodeExists=false;for(var n in this.genericParam)
{if(this.genericParam[n])
{if(n.toLowerCase()=="wmode"&&WIDGETBOX.globals.wmodeOverride){if(WIDGETBOX.globals.wmodeOverride.toLowerCase()=="best"){if(this.genericParam[n].toLowerCase()=="opaque"||this.genericParam[n].toLowerCase()=="window"){flashTag+='<param name="wmode" value="opaque"/>';}else{flashTag+='<param name="wmode" value="transparent"/>';}}else{flashTag+='<param name="wmode" value="'+WIDGETBOX.globals.wmodeOverride.toLowerCase()+'"/>';}
wmodeExists=true;}else{if(n.toLowerCase()=="wmode"){wmodeExists=true;}
flashTag+='<param name="'+n+'" value="'+this.genericParam[n]+'"/>';}}}
if(!wmodeExists&&WIDGETBOX.globals.wmodeOverride){if(WIDGETBOX.globals.wmodeOverride.toLowerCase()=="best"){flashTag+='<param name="wmode" value="opaque"/>';}else{flashTag+='<param name="wmode" value="'+WIDGETBOX.globals.wmodeOverride.toLowerCase()+'"/>';}
wmodeExists=true;}
if(this.flashVars||this.flashVarsStr)
{var fv=this.getFlashVarsAsString();if(fv.length>0)
{flashTag+='<param name="flashvars" value="'+fv+'"/>';}}
flashTag+='</object>';if(this.enableFSCommand&&navigator.platform.indexOf('Win')!=-1&&navigator.userAgent.indexOf('Opera')==-1)
{flashTag+="<script event=\"FSCommand(command,args)\" for=\""+this.id+"\">\n"+"FlashProxy.callJS(command, args);\n"+"</sc"+"ript>";}}
else
{flashTag+='<embed src="'+this.src+'"';if(String(this.width).indexOf("%")>=0||this.width>=0){flashTag+=' width="'+this.width+'"';flashTag+=' height="'+this.height+'"';}
flashTag+=' type="application/x-shockwave-flash"';if(this.id)
{flashTag+=' id="'+this.id+'"';flashTag+=' name="'+this.id+'"';}
var wmodeExists=false;for(var n in this.genericParam)
{if(this.genericParam[n])
{if(n.toLowerCase()=="wmode"&&WIDGETBOX.globals.wmodeOverride){if(WIDGETBOX.globals.wmodeOverride.toLowerCase()=="best"){if(this.genericParam[n].toLowerCase()=="opaque"||this.genericParam[n].toLowerCase()=="window"){flashTag+=(' wmode="opaque"');}else{flashTag+=(' wmode="transparent"');}}else{flashTag+=(' wmode="'+WIDGETBOX.globals.wmodeOverride.toLowerCase()+'"');}
wmodeExists=true;}else{if(n.toLowerCase()=="wmode"){wmodeExists=true;}
flashTag+=(' '+n+'="'+this.genericParam[n]+'"');}}}
if(!wmodeExists&&WIDGETBOX.globals.wmodeOverride){if(WIDGETBOX.globals.wmodeOverride.toLowerCase()=="best"){flashTag+=(' wmode="opaque"');}else{flashTag+=(' wmode="'+WIDGETBOX.globals.wmodeOverride.toLowerCase()+'"');}
wmodeExists=true;}
if(this.flashVars||this.flashVarsStr)
{var fv=this.getFlashVarsAsString();if(fv.length>0)
{flashTag+=' flashvars="'+fv+'"';}}
flashTag+=' pluginspage="http://www.macromedia.com/go/getflashplayer">';flashTag+='</embed>';}
return flashTag;}
FlashTag.prototype.write=function(doc)
{doc.write(this.toString());}
FlashTag.prototype.getFlashVarsAsString=function()
{var qs=new String();for(var n in this.flashVars)
{if(this.flashVars[n])
{qs+=(encodeURIComponent(n)+'='+encodeURIComponent(this.flashVars[n])+'&');}}
if(this.flashVarsStr)
{return qs+this.flashVarsStr;}
return qs.substring(0,qs.length-1);}
function FlashSerializer(useCdata)
{this.useCdata=useCdata;}
FlashSerializer.prototype.serialize=function(args)
{var qs=new String();for(var i=0;i<args.length;++i)
{switch(typeof(args[i]))
{case'undefined':qs+='t'+(i)+'=undf';break;case'string':qs+='t'+(i)+'=str&d'+(i)+'='+escape(args[i]);break;case'number':qs+='t'+(i)+'=num&d'+(i)+'='+escape(args[i]);break;case'boolean':qs+='t'+(i)+'=bool&d'+(i)+'='+escape(args[i]);break;case'object':if(!args[i])
{qs+='t'+(i)+'=null';}
else if(args[i]instanceof Date)
{qs+='t'+(i)+'=date&d'+(i)+'='+escape(args[i].getTime());}
else
{try
{qs+='t'+(i)+'=xser&d'+(i)+'='+escape(this._serializeXML(args[i]));}
catch(exception)
{throw new Exception("FlashSerializationException","The following error occurred during complex object serialization: "+exception.getMessage());}}
break;default:throw new Exception("FlashSerializationException","You can only serialize strings, numbers, booleans, dates, objects, arrays, nulls, and undefined.");}
if(i!=(args.length-1))
{qs+='&';}}
return qs;}
FlashSerializer.prototype._serializeXML=function(obj)
{var doc=new Object();doc.xml='<fp>';try
{this._serializeNode(obj,doc,null);}
catch(exception)
{if(exception.message)
{throw new Exception("FlashSerializationException","Unable to serialize object because: "+exception.message);}
throw exception;}
doc.xml+='</fp>';return doc.xml;}
FlashSerializer.prototype._serializeNode=function(obj,doc,name)
{switch(typeof(obj))
{case'undefined':doc.xml+='<undf'+this._addName(name)+'/>';break;case'string':doc.xml+='<str'+this._addName(name)+'>'+this._escapeXml(obj)+'</str>';break;case'number':doc.xml+='<num'+this._addName(name)+'>'+obj+'</num>';break;case'boolean':doc.xml+='<bool'+this._addName(name)+' val="'+obj+'"/>';break;case'object':if(!obj)
{doc.xml+='<null'+this._addName(name)+'/>';}
else if(obj instanceof Date)
{doc.xml+='<date'+this._addName(name)+'>'+obj.getTime()+'</date>';}
else if(obj instanceof Array)
{doc.xml+='<array'+this._addName(name)+'>';for(var i=0;i<obj.length;++i)
{this._serializeNode(obj[i],doc,null);}
doc.xml+='</array>';}
else
{doc.xml+='<obj'+this._addName(name)+'>';for(var n in obj)
{if(typeof(obj[n])=='function')
continue;this._serializeNode(obj[n],doc,n);}
doc.xml+='</obj>';}
break;default:throw new Exception("FlashSerializationException","You can only serialize strings, numbers, booleans, objects, dates, arrays, nulls and undefined");break;}}
FlashSerializer.prototype._addName=function(name)
{if(name)
{return' name="'+name+'"';}
return'';}
FlashSerializer.prototype._escapeXml=function(str)
{if(this.useCdata)
return'<![CDATA['+str+']]>';else
return str.replace(/&/g,'&amp;').replace(/</g,'&lt;');}
function FlashProxy(lcId,flashId,proxySwfName,callbackScope)
{FlashProxy.fpmap[lcId]=this;this.uid=lcId;this.proxySwfName=proxySwfName;this.callbackScope=callbackScope;this.flashSerializer=new FlashSerializer(false);this.q=new Array();}
FlashProxy.prototype.call=function()
{if(arguments.length==0)
{throw new Exception("Flash Proxy Exception","The first argument should be the function name followed by any number of additional arguments.");}
this.q.push(arguments);if(this.q.length==1)
{this._execute(arguments);}}
FlashProxy.prototype._execute=function(args)
{var ft=new FlashTag(this.proxySwfName,1,1,'6,0,65,0');ft.addFlashVar('lcId',this.uid);ft.addFlashVar('functionName',args[0]);if(args.length>1)
{var justArgs=new Array();for(var i=1;i<args.length;++i)
{justArgs.push(args[i]);}
ft.addFlashVars(this.flashSerializer.serialize(justArgs));}
var divName='_flash_proxy_'+this.uid;if(!document.getElementById(divName))
{var newTarget=document.createElement("div");newTarget.id=divName;document.body.appendChild(newTarget);}
var target=document.getElementById(divName);target.innerHTML=ft.toString();}
FlashProxy.callJS=function(command,args)
{var argsArray=eval(args);var fp=FlashProxy.fpmap[argsArray.shift()];var scope=null;if(fp)scope=fp.callbackScope;if(scope&&(command.indexOf('.')<0))
{var functionToCall=scope[command];functionToCall.apply(scope,argsArray);}
else
{var functionToCall=eval(command);functionToCall.apply(functionToCall,argsArray);}}
FlashProxy.callComplete=function(uid)
{var fp=FlashProxy.fpmap[uid];if(fp)
{fp.q.shift();if(fp.q.length>0)
{fp._execute(fp.q[0]);}}}
FlashProxy.fpmap=new Object();WIDGETBOX.flash.Utils.GetSwfVer=GetSwfVer;WIDGETBOX.flash.Utils.DetectFlashVer=DetectFlashVer;WIDGETBOX.flash.Utils.GetFlashVersionArray=GetFlashVersionArray;WIDGETBOX.flash.Utils.FlashTag=FlashTag;WIDGETBOX.flash.Utils.FlashSerializer=FlashSerializer;WIDGETBOX.flash.Utils.FlashProxy=FlashProxy;window.FlashProxy=FlashProxy;WIDGETBOX.setReady("flash.Utils");})()
WIDGETBOX.namespace("flash");if(!WIDGETBOX.flash.Helper)(function(){var initialized=false;var flashHelperInstalled=false;var flashHelperStarted=false;var msgHandlers=new Object();var helperId=null;var startListeners=new Array();var useV8=false;WIDGETBOX.flash.Helper={init:function(){if(!initialized){initialized=true;useV8=WIDGETBOX.flash.Utils.DetectFlashVer(8,0,0);}},addStartListener:function(onStartListener){if(onStartListener){if(!flashHelperStarted){startListeners[startListeners.length]=onStartListener;}
else{try{onStartListener();}
catch(e){WIDGETBOX.logMessage(e);}}}},startHelper:function(token,widgetId,onStartListener){if(flashHelperInstalled&&flashHelperStarted){try{onStartListener();}
catch(e){WIDGETBOX.logMessage(e);}
return;}
self.addStartListener(onStartListener);if(!flashHelperInstalled){flashHelperInstalled=true;helperId="widgetbox_jsproxy_"+widgetId;var helperSwf="http://cdn.widgetserver.com/syndication/flash/v6/Helper.swf";var helperVer="6,0,65,0";if(useV8){helperSwf="http://cdn.widgetserver.com/syndication/flash/v8/Helper.swf";helperVer="8,0,0,0";}
var ft=new WIDGETBOX.flash.Utils.FlashTag(helperSwf,1,1,helperVer);ft.addFlashVar("token",token);ft.addFlashVar("widgetId",widgetId);ft.setId(helperId);ft.setAllowScriptAccess("always");ft.setAllowNetworking("all");ft.setWmode("transparent");if(!useV8){ft.setEnableFSCommand(true);}
var divName='widgetbox_helper_div_'+token;if(document.body){var target=document.getElementById(divName);if(!target)
{target=document.createElement("div");target.id=divName;target.style.position="absolute";target.style.left="0";target.style.top="0";if(WIDGETBOX.globals.firstParentNode){WIDGETBOX.globals.firstParentNode.parentNode.appendChild(target);}else{document.body.appendChild(target);}}
target.innerHTML=ft.toString();}
else{document.write("<div id=\""+divName+"\">"+ft.toString()+"</div>");}}},setHelperStarted:function(){if(!flashHelperStarted){flashHelperStarted=true;for(var i=0;i<startListeners.length;i++){var listener=startListeners[i];try{listener();}
catch(e){WIDGETBOX.logMessage(e);}}}},isHelperStarted:function(){return flashHelperStarted;},sendMessage:function(token,widgetId,msgName,msgPayload){if(flashHelperStarted){if(useV8){var movie=document.getElementById(helperId);if(movie){movie.sendMessage(token,widgetId,msgName,msgPayload);}}
else{var jsProxyId="jsproxy_"+token+"_"+widgetId;var jsContainerId="widgetbox_jsproxy_"+widgetId;var flashProxy=new FlashProxy(jsProxyId,jsContainerId,'http://cdn.widgetserver.com/syndication/flash/v6/JavaScriptFlashGateway.swf',null);flashProxy.call("sendMessage",token,widgetId,msgName,msgPayload);}}},setMessageHandler:function(msgName,handler){msgHandlers[msgName]=handler;},messageListener:function(msgName,msgPayload){var handler=msgHandlers[msgName];if(handler){try{handler(msgPayload);}
catch(e){WIDGETBOX.logMessage(e);}}
else{alert("Message received: "+msgName);}}}
var self=WIDGETBOX.flash.Helper;self.init();WIDGETBOX.setReady("flash.Helper");})()
WIDGETBOX.namespace("behaviour");if(!WIDGETBOX.behaviour.Behaviour)(function(){var isIE=(navigator.appVersion.indexOf("MSIE")!=-1)?true:false;var Behaviour={list:new Array,register:function(sheet){self.list.push(sheet);},start:function(){self.addLoadEvent(function(){self.apply();});},apply:function(){for(var h=0;h<self.list.length;h++){var sheet=self.list[h];for(var selector in sheet){var list=self.getElementsBySelector(selector);if(!list){continue;}
for(var i=0;i<list.length;i++){var element=list[i];try{sheet[selector](element);}
catch(e){WIDGETBOX.logMessage(e);}}}}},addLoadEvent:function(func){WIDGETBOX.addPageLoadListener(func);},getAllChildren:function(e){return e.all?e.all:e.getElementsByTagName('*');},getElementsBySelector:function(selector){if(!document.getElementsByTagName){return new Array();}
var tokens=selector.split(' ');var currentContext=new Array(document);for(var i=0;i<tokens.length;i++){token=tokens[i].replace(/^\s+/,'').replace(/\s+$/,'');;if(token.indexOf('#')>-1){var bits=token.split('#');var tagName=bits[0];var id=bits[1];var element=document.getElementById(id);if(tagName&&element.nodeName.toLowerCase()!=tagName){return new Array();}
currentContext=new Array(element);continue;}
if(token.indexOf('.')>-1){var bits=token.split('.');var tagName=bits[0];var className=bits[1];if(!tagName){tagName='*';}
var found=new Array;var foundCount=0;for(var h=0;h<currentContext.length;h++){var elements;if(tagName=='*'){elements=self.getAllChildren(currentContext[h]);}else{elements=self.getElementsByTagName(currentContext[h],tagName);}
for(var j=0;j<elements.length;j++){found[foundCount++]=elements[j];}}
currentContext=new Array;var currentContextIndex=0;for(var k=0;k<found.length;k++){if(found[k].className){var foundClasses=found[k].className.split(' ');for(var l=0;l<foundClasses.length;l++){if(foundClasses[l]==className){currentContext[currentContextIndex++]=found[k];}}}}
continue;}
if(token.match(/^(\w*)\[(\w+)([=~\|\^\$\*]?)=?"?([^\]"]*)"?\]$/)){var tagName=RegExp.$1;var attrName=RegExp.$2;var attrOperator=RegExp.$3;var attrValue=RegExp.$4;if(!tagName){tagName='*';}
var found=new Array;var foundCount=0;for(var h=0;h<currentContext.length;h++){var elements;if(tagName=='*'){elements=self.getAllChildren(currentContext[h]);}else{elements=self.getElementsByTagName(currentContext[h],tagName);}
for(var j=0;j<elements.length;j++){found[foundCount++]=elements[j];}}
currentContext=new Array;var currentContextIndex=0;var checkFunction;switch(attrOperator){case'=':if(isIE&&attrName=='class'){checkFunction=function(e){return(e.className==attrValue);}}else{checkFunction=function(e){return(e.getAttribute(attrName)==attrValue);};}
break;case'~':checkFunction=function(e){return(e.getAttribute(attrName).match(new RegExp('\\b'+attrValue+'\\b')));};break;case'|':checkFunction=function(e){return(e.getAttribute(attrName).match(new RegExp('^'+attrValue+'-?')));};break;case'^':checkFunction=function(e){return(e.getAttribute(attrName).indexOf(attrValue)==0);};break;case'$':checkFunction=function(e){return(e.getAttribute(attrName).lastIndexOf(attrValue)==e.getAttribute(attrName).length-attrValue.length);};break;case'*':checkFunction=function(e){return(e.getAttribute(attrName).indexOf(attrValue)>-1);};break;default:checkFunction=function(e){return e.getAttribute(attrName);};}
currentContext=new Array;var currentContextIndex=0;for(var k=0;k<found.length;k++){if(checkFunction(found[k])){currentContext[currentContextIndex++]=found[k];}}
continue;}
if(!currentContext[0]){return;}
tagName=token;var found=new Array;var foundCount=0;for(var h=0;h<currentContext.length;h++){var elements=self.getElementsByTagName(currentContext[h],tagName);for(var j=0;j<elements.length;j++){found[foundCount++]=elements[j];}}
currentContext=found;}
return currentContext;},getElementsByTagName:function(parentnode,tagname){if(tagname.indexOf(':')>-1){var parts=tagname.split(':');var nsprefix=parts[0];tagname=parts[1];return self.getElementsByTagNameNS(parentnode,nsprefix,tagname);}
else return parentnode.getElementsByTagName(tagname);},getElementsByTagNameNS:function(parentnode,nsprefix,tagname){if(isIE){var unscopedNodes=parentnode.getElementsByTagName(tagname);var scopedNodes=[];for(var i=0;i<unscopedNodes.length;i++){var node=unscopedNodes[i];var scopeName=node.scopeName;if(scopeName)scopeName=scopeName.toLowerCase();if(scopeName==nsprefix)scopedNodes[scopedNodes.length]=node;}
return scopedNodes;}
else{return parentnode.getElementsByTagName(nsprefix+':'+tagname);}},getInnerText:function(node){if(typeof node.textContent!='undefined'){return node.textContent;}
else if(typeof node.innerText!='undefined'){return node.innerText;}
else if(typeof node.text!='undefined'){return node.text;}
else{switch(node.nodeType){case 3:case 4:return node.nodeValue;break;case 1:case 11:var innerText='';for(var i=0;i<node.childNodes.length;i++){innerText+=getInnerText(node.childNodes[i]);}
return innerText;break;default:return'';}}}}
var self=Behaviour;WIDGETBOX.behaviour.Behaviour=Behaviour;WIDGETBOX.setReady("behaviour.Behaviour");})()
WIDGETBOX.namespace("net");if(!WIDGETBOX.net.Utils)(function(){var lastTime=(new Date()).getTime();WIDGETBOX.net.Utils={newTimestamp:function(){var newTime=(new Date()).getTime();if(newTime==lastTime)newTime++;lastTime=newTime;return newTime;},createXMLHttpRequest:function(){try{return new ActiveXObject("Msxml2.XMLHTTP");}catch(e){}
try{return new ActiveXObject("Microsoft.XMLHTTP");}catch(e){}
try{return new XMLHttpRequest();}catch(e){}
alert("XMLHttpRequest not supported");return null;},urlEncode:function(str){if(!str)return str;return encodeURIComponent(str).replace(/\~/g,'%7E').replace(/\!/g,'%21').replace(/\(/g,'%28').replace(/\)/g,'%29').replace(/\'/g,'%27');},newURLWithParams:function(url,params){var paramString="";var paramDelim="";if(params){for(var paramName in params){var paramValue=params[paramName];paramString+=paramDelim+
self.urlEncode(paramName)+"="+self.urlEncode(paramValue);paramDelim="&";}
if(paramString.length>0){var lastChar=url.charAt(url.length-1);if(url.indexOf("?")<0&&url.indexOf("&")<0)url+="?";else if((lastChar!="&")&&(lastChar!="?"))url+="&";url+=paramString;}}
return url;}}
var self=WIDGETBOX.net.Utils;WIDGETBOX.setReady("net.Utils");})()
WIDGETBOX.namespace("net");if(!WIDGETBOX.net.JSONRequest)(function(){var requests=new Array();WIDGETBOX.net.JSONRequest={get:function(url,send,done,timeout){var d=document;var timestamp=WIDGETBOX.net.Utils.newTimestamp();var requestNumber=requests.length;if(!send)send=new Object();send.timestamp=timestamp;send.callback="WIDGETBOX.net.JSONRequest.handleCallback("+requestNumber+")";send.output="json";var request=new Object();request.readyState=0;request.url=url;request.send=send;request.done=done;request.timestamp=timestamp;request.number=requestNumber;request.timeout=timeout;var scriptUrl=WIDGETBOX.net.Utils.newURLWithParams(url,send);var scriptId="widgetbox_jsonrequest_"+requestNumber;var head=d.getElementsByTagName("head").item(0);var scriptElement=d.createElement("script");request.script=scriptElement;request.id=scriptId;requests[requestNumber]=request;scriptElement.setAttribute("src",scriptUrl);scriptElement.setAttribute("id",scriptId);head.appendChild(scriptElement);},handleCallback:function(requestNumber){var d=document;if(requests[requestNumber]){try{var request=requests[requestNumber];requests[requestNumber]=null;request.readyState=4;var head=d.getElementsByTagName("head").item(0);if(request.script){try{head.removeChild(request.script);}
catch(e){WIDGETBOX.logMessage(e);}}
var doneFunc=request.done;if(doneFunc){var doneCallback=function(value){try{doneFunc(requestNumber,value,null);}
catch(e){WIDGETBOX.logMessage(e);}}
return doneCallback;}}
catch(e){WIDGETBOX.logMessage(e);}}
var dummyCallback=function(value){}
return dummyCallback;}}
var self=WIDGETBOX.net.JSONRequest;WIDGETBOX.setReady("net.JSONRequest");})()
WIDGETBOX.namespace("net");if(!WIDGETBOX.net.ProxiedHTTPRequest)(function(){function getDOMFromXML(xml){var dom=null;try{if(typeof ActiveXObject!='undefined'){dom=new ActiveXObject("Microsoft.XMLDOM");dom.async=false;dom.loadXML(xml);}
else{var parser=new DOMParser();dom=parser.parseFromString(xml,"text/xml");}}
catch(e){WIDGETBOX.logMessage(e);}
return dom;}
var requests=new Array();var lastTime=(new Date()).getTime();function ProxiedHTTPRequest(){this.initialized=false;this.onreadystatechange=null;this.onload=null;this.onerror=null;this.onprogress=null;this.readyState=0;this.status=0;this.statusText=null;this.requestHeaders=null;this.responseHeaders=null;this.responseXML=null;this.responseText=null;this.responseObj=null;this.parseAtProxyServer=false;this.format="raw";}
ProxiedHTTPRequest.prototype={getResponseXML:function(){if(!this.responseXML&&this.responseText){this.responseXML=getDOMFromXML(this.responseText);}
return this.responseXML;},abort:function(){this.initialized=false;},getAllResponseHeaders:function(){var allHeaders="";if(this.initialized){for(var name in this.responseHeaders){var value=this.responseHeaders[name];allHeaders+=name+": "+value+"\r\n";}}
return allHeaders;},getResponseHeader:function(header){if(this.initialized){return this.responseHeaders[header];}
return null;},setFormat:function(format){this.format=format;},setParseAtProxyServer:function(parse,format){this.parseAtProxyServer=parse;if(format)this.format=format;},open:function(method,url,async,user,password){this.url=url;this.method=method;this.initialized=true;this.readyState=0;this.status=0;this.statusText=null;this.requestHeaders=new Object();this.responseHeaders=null;this.responseXML=null;this.responseText=null;this.responseObj=null;this.parseAtProxyServer=false;this.format="raw";},send:function(body){if(this.initialized){var self=this;var callback=function(requestNumber,value,exception){self.readyState=4;if(value&&(!value.error)){if(value.responseText)self.responseText=value.responseText;if(value.responseObj)self.responseObj=value.responseObj;if(value.headers)self.responseHeaders=value.headers;self.status=value.status;self.statusText=value.statusText;var ctype=self.getResponseHeader("Content-Type");if(ctype&&(ctype.indexOf("xml")>=0)){self.getResponseXML();}
if(self.onreadystatechange)self.onreadystatechange();if(self.onload)self.onload();}
else{if(value&&value.status){if(value.headers)self.responseHeaders=value.headers;self.status=value.status;self.statusText=value.statusText;}
else{self.status=502;self.statusText="Bad Gateway (Irrecoverable Error at PostApp Proxy)";}
if(self.onreadystatechange)self.onreadystatechange();if(self.onerror)self.onerror();}}
var send=new Object();send.target=this.url;send.method=this.method;if(body)send.body=body;var headerStr="";for(var name in this.requestHeaders){var value=this.requestHeaders[name];headerStr+=name+":"+value+"\r\n";}
if(headerStr)send.headers=headerStr;send.parse=this.parseAtProxyServer;send.format=this.format;send.token=WIDGETBOX.globals.token;WIDGETBOX.net.JSONRequest.get("http://widgetserver.com/proxy/ws_proxy.js",send,callback,0);}},setRequestHeader:function(header,value){if(this.initialized){this.requestHeaders[header]=value;}}}
WIDGETBOX.net.ProxiedHTTPRequest=ProxiedHTTPRequest;WIDGETBOX.setReady("net.ProxiedHTTPRequest");})()
WIDGETBOX.namespace("dom");if(!WIDGETBOX.dom.Utils)(function(){var docWriteFunc=document.write;var docWritelnFunc=document.writeln;var outputBuf="";var docWritePatch=function(markup){outputBuf+=markup;}
var docWritelnPatch=function(markup){outputBuf+=markup+"\n";}
WIDGETBOX.dom.Utils={lTrim:function(value){var re=/\s*((\S+\s*)*)/;return value.replace(re,"$1");},rTrim:function(value){var re=/((\s*\S+)*)\s*/;return value.replace(re,"$1");},trim:function(value){return this.lTrim(this.rTrim(value));},startWriteCapture:function(){document.write=docWritePatch;document.writeln=docWritelnPatch;},stopWriteCapture:function(){document.write=docWriteFunc;document.writeln=docWritelnFunc;},removeAllChildren:function(parentNode,loop){if(!loop)loop=0;if(loop<5){try{if(parentNode.childNodes){while(parentNode.childNodes.length>0){var toRemove=parentNode.childNodes[parentNode.childNodes.length-1];WIDGETBOX.dom.Utils.removeAllChildren(toRemove);parentNode.removeChild(toRemove,loop++);}}}catch(e){WIDGETBOX.logMessage(e);}}},insertHtmlText:function(elem,htmlStr){var headNodes=new Array();try{var re=new RegExp("<\\s*?script[\\s\\S]*?<\\/script\\s*?>","gi");var filteredHtml=htmlStr.replace(re,"");elem.innerHTML=filteredHtml;var head=document.getElementsByTagName("head").item(0);re=new RegExp("<\\s*?script([^>]*?)>([\\s\\S]*?)<\\/script","gi");var match=re.exec(htmlStr);while(match!=null)
{var scriptContent=RegExp.$2;if(scriptContent)scriptContent=WIDGETBOX.dom.Utils.trim(scriptContent);if(scriptContent&&scriptContent.length>0){eval(scriptContent);}else{var scriptNode=document.createElement("script");scriptNode.setAttribute("type","text/javascript");scriptNode.setAttribute("defer",true);var reSrc=new RegExp("src=\"([\\s\\S]*?)\"","gi");var strSrc=reSrc.exec(match[1]);if(strSrc){scriptNode.setAttribute("src",strSrc[1]);}else{scriptNode.text=match[2];}
headNodes.push(scriptNode);head.appendChild(scriptNode);}
match=re.exec(htmlStr);}}
catch(e){WIDGETBOX.logMessage(e);}
return headNodes;},insertCssFile:function(URI,doc,checkDuplicates){if(!URI){return;}
var doc=document;var req=new WIDGETBOX.net.ProxiedHTTPRequest();req.onload=function(){var cssStr=req.responseText;cssStr=self.fixPathsInCssText(cssStr,URI);if(checkDuplicates){var styles=doc.getElementsByTagName("style");var cssText="";for(var i=0;i<styles.length;i++){cssText=(styles[i].styleSheet&&styles[i].styleSheet.cssText)?styles[i].styleSheet.cssText:styles[i].innerHTML;if(cssStr==cssText){return;}}}
var style=self.insertCssText(cssStr);}
req.open("get",URI);req.send();},insertCssText:function(cssStr,doc,URI,id){if(!cssStr){return;}
var doc=document;if(URI){cssStr=self.fixPathsInCssText(cssStr,URI);}
var style;if(id){style=doc.getElementById(id);}
if(!style){style=doc.createElement("style");style.setAttribute("type","text/css");if(id)style.setAttribute("id",id);var head=doc.getElementsByTagName("head")[0];if(!head){return;}else{head.appendChild(style);}}else{WIDGETBOX.dom.Utils.removeAllChildren(style);}
if(style.styleSheet){style.styleSheet.cssText=cssStr;}else{var cssText=doc.createTextNode(cssStr);style.appendChild(cssText);}
return style;},fixPathsInCssText:function(cssStr,URI){if(!cssStr||!URI){return;}
var pos=0;var str="";var url="";while(pos!=-1){pos=0;url="";pos=cssStr.indexOf("url(",pos);if(pos<0){break;}
str+=cssStr.slice(0,pos+4);cssStr=cssStr.substring(pos+4,cssStr.length);url+=cssStr.match(/^[\t\s\w()\/.\\'"-:#=&?]*\)/)[0];cssStr=cssStr.substring(url.length-1,cssStr.length);url=url.replace(/^[\s\t]*(['"]?)([\w()\/.\\'"-:#=&?]*)\1[\s\t]*?\)/,"$2");if(url.search(/(file|https?|ftps?):\/\//)==-1){url=(new WIDGETBOX.net.Uri(URI,url).toString());}
str+=url;}
return str+cssStr;}}
var self=WIDGETBOX.dom.Utils;WIDGETBOX.setReady("dom.Utils");})()
WIDGETBOX.namespace("subscriber");if(!WIDGETBOX.subscriber.Widget)(function(){var JSONRequest=WIDGETBOX.net.JSONRequest;var netUtils=WIDGETBOX.net.Utils;function Widget(appId,regId,parentNode){if(!appId)appId="00000000-0000-0000-0000-000000000000";this.appId=appId;this.regId=regId;this.fId=this.regId;this.providerId=null;this.sid=WIDGETBOX.subscriber.Main.generateGUID();this.id=WIDGETBOX.globals.widgets.length;this.loaded=false;this.widgetInfoRequestPending=false;this.hasError=false;this.sendsMessages=false;this.subscribeMode=null;this.buttonMode=0;this.defer=false;this.info=null;this.eventListeners=null;this.activityImage=null;this.widgetImage=null;this.iframe=null;this.adDiv=null;this.divNode=null;this.headNodes=null;this.parentNode=parentNode;this.divId="widgetbox_widget_div_"+this.id;this.divNode=document.createElement("div");this.divNode.setAttribute("id",this.divId);this.getWidgetLoaded=false;this.quantcastLoaded=false;this.adLoaded=false;this.adFriendly=false;this.adEnabled=false;this.isSubscriber=false;this.channels=null;this.adPlacement=null;this.isPreviewInstance=false;this.isConfigurable=false;this.inventoryInstId=false;this.inventoryRegId=false;this.appPK=null;this.regPK=null;this.providerPK=null;this.userPK=null;this.fromGalleryPK=null;this.installPartnerNetworkPK=null;this.isUseDefaultDomain=false;this.defaultDomain=null;this.categories=null;this.detailsUrl=null;this.configUrl=null;this.configParams=null;this.partnerNetwork=null;this.fromPartnerNetwork=null;this.fromCustomGallery=null;this.suppressEditWidget=false;this.suppressQuantcast=false;this.serviceLevel="0";this.providerServiceLevel="0";this.instServiceLevel="0";this.counterUrl=null;this.cfgString=null;parentNode.appendChild(this.divNode);WIDGETBOX.globals.widgets[this.id]=this;}
Widget.prototype={addEventListener:function(eventType,listener){if(listener){if((eventType=="load")&&this.loaded){try{listener(this);}
catch(e){WIDGETBOX.logMessage(e);}}
else{if(!this.eventListeners)this.eventListeners=new Object();if(!this.eventListeners[eventType])this.eventListeners[eventType]=new Array();var listeners=this.eventListeners[eventType];listeners.push(listener);}}},notifyEventListeners:function(eventType){if(!this.eventListeners)return;var listeners=this.eventListeners[eventType];if(!listeners)return;for(var i=0;i<listeners.length;i++){var listener=listeners[i];try{listener(this);}
catch(e){WIDGETBOX.logMessage(e);}}},loadInfo:function(info){if(!info&&!this.info)return;if(info&&!this.info)this.info=info;else info=this.info;if(!this.loaded){this.notifyEventListeners("loadInfo");this.regId=info.regId;this.fId=info.fId;if(info.hasDynamicSize||info.hasDynamicStyle||info.sendsMessages)this.sendsMessages=true;if(info.isContextAware)this.defer=true;if(info.defer)this.defer=true;if(this.sendsMessages)this.defer=true;if(this.sendsMessages){if(!WIDGETBOX.subscriber.Main.isHelperSetupForSubscriber()){WIDGETBOX.subscriber.Main.setupHelperForSubscriber();return;}}
if(this.defer&&!WIDGETBOX.globals.pageLoaded)return;var divNode=this.divNode;if(!divNode)return;this.loaded=true;if(info.name=="Widgetbox Error Widget"){this.hasError=true;}
if(!this.subscribeMode)this.subscribeMode=info.subscribeMode;this.configUrl=info.configUrl;this.adFriendly=info.isAdFriendly;this.adEnabled=info.isAdEnabled;this.channels=info.channels;this.adPlacement=info.adPlacement;this.isPreviewInstance=info.isPreviewInstance;this.isConfigurable=(this.isConfigurable||this.isPreviewInstance);this.inventoryInstId=info.inventoryInstId;this.inventoryRegId=info.inventoryRegId;this.providerId=info.providerId;this.appPK=info.appPK;this.regPK=info.regPK;this.providerPK=info.providerPK;this.userPK=info.userPK;this.fromGalleryPK=info.fromGalleryPK;this.installPartnerNetworkPK=info.installPartnerNetworkPK;this.isUseDefaultDomain=info.isUseDefaultDomain;this.defaultDomain=info.defaultDomain;this.categories=info.categories;this.serviceLevel=info.serviceLevel;this.providerServiceLevel=info.providerServiceLevel;this.instServiceLevel=info.instServiceLevel;this.fromCustomGallery=info.fromGalleryCode;this.domainWhitelist=info.domainWhitelist;if(this.domainWhitelist){var domainOk=false;var location=window.location.toString();if(location){var endProtocolIndex=location.indexOf("://")+3;var endHostnameIndex=location.indexOf("/",endProtocolIndex);if(endHostnameIndex==-1)endHostnameIndex=location.length;var currentHostname=location.substring(endProtocolIndex,endHostnameIndex);var colonPosition=currentHostname.indexOf(":");if(colonPosition>0){currentHostname=currentHostname.substring(0,colonPosition);}
for(var d=0;d<this.domainWhitelist.length;d++){var domain=this.domainWhitelist[d];if(currentHostname.indexOf(domain)>-1){domainOk=true;}}
if(!domainOk){return;}}}
if(info.installPartnerNetworkCode){this.partnerNetwork=info.installPartnerNetworkCode;}else{this.partnerNetwork="code";}
if(info.fromPartnerNetworkCode){this.fromPartnerNetwork=info.fromPartnerNetworkCode;}else{this.fromPartnerNetwork="wbx";}
if(!this.detailsUrl){this.detailsUrl="http://www.widgetbox.com";if(this.info.widgetPrototype=="TEMPLATE"){this.detailsUrl+="/i/"+this.appId;}else{this.detailsUrl+="/widget/"+this.fId;this.detailsUrl+="?wbx.refer=1"
+"&wbx.refer.instId="+this.appId
+"&wbx.refer.targetRegId="+this.regId;if(this.partnerNetwork!=null&&this.partnerNetwork!=""){this.detailsUrl+="&wbx.refer.pn="+this.partnerNetwork;}
this.detailsUrl+="&wbx.refer.location="+escape(window.location.href);}}
var urlParams=new Object();var configParams=info.configParams;var pluginParams=new Object();var flashVars=new Object();var wbxParams=new Object();var widgetParams=new Object();var hasTagAware=false;var hasWMode=false;if(this.cfgString){var paramPairs=this.cfgString.split('&');for(var i=0;i<paramPairs.length;i++){var nameParts=paramPairs[i].split('=');var name=nameParts[0];var val=null;if(nameParts.length>1){val=unescape(nameParts[1]);}
var param=configParams[name];if(param){param.value=val;}}}
if(configParams){var keywordMarkup=WIDGETBOX.globals.keywordMarkup;var taggingMarkup=WIDGETBOX.globals.taggingMarkup;for(var configName in configParams){var configParam=configParams[configName];var contextValue=null;if(keywordMarkup&&configParam.useContext){hasTagAware=true;var keywordRef=keywordMarkup[configParam.contextName];if(keywordRef){contextValue=keywordRef.keywords;}}
if(taggingMarkup&&!contextValue&&(configParam.useRelTags)&&(configParam.contextName=="*")){var keywordRef=taggingMarkup[0];if(keywordRef){contextValue=keywordRef.keywords;}}
if(contextValue)configParam.value=contextValue;if(!configParam.value)configParam.value="";if(configName.indexOf("plugin.")==0){var pluginParamName=configName.substring(7);if(pluginParamName.toLowerCase()=="allowscriptaccess"){continue;}
if(info.widgetType=='FLASH'&&pluginParamName.toLowerCase()=="wmode"&&info.isAdFriendly&&info.isAdEnabled){hasWMode=true;if(configParam.value.toLowerCase()!="transparent"){pluginParams[pluginParamName]="opaque";continue;}}
pluginParams[pluginParamName]=configParam.value;continue;}
if(configName.indexOf("flashvar.")==0){var flashVarName=configName.substring(9);flashVars[flashVarName]=configParam.value;continue;}
if(configName.indexOf("loadvar.")==0)continue;if(configName.indexOf("wbx.")==0){var wbxParamName=configName.substring(4);var wbxParamValue=configParam.value;if((!info.isPreviewInstance&&wbxParamName=="counter")||(info.isPreviewInstance&&wbxParamName=="counter_preview")){var counterUrl=configParam.value;this.counterUrl=wbxParamValue=counterUrl.replace(/__WBX_REFERER__/g,WIDGETBOX.net.Utils.urlEncode(window.location.href));}else if(wbxParamName=="quantcast"){if(info.providerServiceLevel>="1"){WIDGETBOX.subscriber.Main.loadQuantcast(divNode.parentNode,this.regId,null,wbxParamValue);}}else if(wbxParamName=="suppressQuantcast"){this.suppressQuantcast=true;}else{wbxParams[wbxParamName]=wbxParamValue;}
continue;}
if(info.widgetPrototype=="BLIDGET"){flashVars[configName]=configParam.value;urlParams["cb"]="33124";}else{urlParams[configName]=configParam.value;}}
if(info.widgetType=='FLASH'&&!hasWMode&&info.isAdFriendly&&info.isAdEnabled){pluginParams["wmode"]="opaque";}
this.configParams=configParams;}
var wbxParamsValue=wbxParams["paramMode"];var isMobileWeb=(info.widgetType=="HTML"&&info.widgetPrototype=="MOBILE_HTML5");var hideAllParams=(wbxParamsValue&&wbxParamsValue=="suppressAll"&&info.widgetType!="HTML");var hideWidgetParams=(wbxParamsValue&&(wbxParamsValue=="suppressAll"||wbxParamsValue=="configOnly"));var hidePKParams=(info.widgetPrototype!="BLIDGET")&&(!wbxParamsValue||wbxParamsValue!="all");this.buttonMode=wbxParams["buttonMode"]?wbxParams["buttonMode"]:0;widgetParams["appId"]=this.appId;widgetParams["regId"]=this.regId;widgetParams["friendlyId"]=this.fId;widgetParams["name"]=info.name;widgetParams["token"]=WIDGETBOX.subscriber.Main.getToken();widgetParams["sid"]=this.sid;widgetParams["id"]=""+this.id;widgetParams["location"]=window.location.href;widgetParams["timestamp"]=""+netUtils.newTimestamp();widgetParams["serviceLevel"]=""+this.serviceLevel;widgetParams["provServiceLevel"]=""+this.providerServiceLevel;widgetParams["instServiceLevel"]=""+this.instServiceLevel;widgetParams["width"]=""+info.appWidth;widgetParams["height"]=""+info.appHeight;widgetParams["wrapper"]="JAVASCRIPT";widgetParams["isAdFriendly"]=""+info.isAdFriendly;widgetParams["isAdEnabled"]=""+info.isAdEnabled;widgetParams["adChannels"]=""+WIDGETBOX.subscriber.Main.getAdChannelsAsQueryString(this);widgetParams["adPlacement"]=""+info.adPlacement;if(info.hasDynamicSize&&info.widgetType!="FLASH")widgetParams["hasDynamicSize"]=""+info.hasDynamicSize;if(info.widgetType=="HTML")widgetParams["output"]="htmlcontent";if(!hidePKParams){widgetParams["appPK"]=this.appPK;widgetParams["regPK"]=this.regPK;widgetParams["providerPK"]=this.providerPK;widgetParams["userPK"]=this.userPK;if(this.partnerNetworkPK){widgetParams["partnerNetworkPK"]=this.partnerNetworkPK;}
if(this.fromGalleryPK){widgetParams["fromGalleryPK"]=this.fromGalleryPK;}}
if(info.widgetType=='FLASH'){if(!hideWidgetParams){for(var sKey in widgetParams){flashVars["widget_"+sKey]=widgetParams[sKey];}}}else{if(info.widgetType=='HTML'||!hideWidgetParams){for(var wKey in widgetParams){urlParams["widget."+wKey]=widgetParams[wKey];}}}
var url=info.url;if((info.widgetType=='HTML')&&info.isRenderedInline){try{var htmlContent=info.htmlContent;var paramFunc=function(str,p1,offset,s){var result="";var p=p1.split('.');if(p.length==2){var paramScope=p[0];var paramName=p[1];if(paramScope&&paramName){if(paramScope=="config"&&configParams[paramName]){result=configParams[paramName].value;}
else if(paramScope=="widget"){result=widgetParams[paramName];}}}
if(!result)result="";return result;};var re=new RegExp("\\\$\\s*?{\\s*?(.*?)\\s*?}","gi");htmlContent=htmlContent.replace(re,paramFunc);htmlContent=htmlContent.replace("__MODULE_ID__",this.id);this.headNodes=WIDGETBOX.dom.Utils.insertHtmlText(divNode,htmlContent);}
catch(e){WIDGETBOX.logMessage(e);}
if(!this.hasError){WIDGETBOX.subscriber.Main.recordWidgetLoad(WIDGETBOX.globals.widgets[this.id]);}
this.notifyEventListeners("load");return;}
if(isMobileWeb){url="http://widgetserver.com/syndication/html5/"+info.appId;}else if(info.widgetType=='HTML'){url="http://markup.widgetserver.com/syndication/get_widget.html";}else if(info.widgetType=='URL'){try{var urlParamFunc=function(str,p1,offset,s){var result="";var p=p1.split('.');if(p.length==2){var paramScope=p[0];var paramName=p[1];if(paramScope&&paramName){if(paramScope=="config"&&configParams[paramName]){result=configParams[paramName].value;}
else if(paramScope=="widget"){result=widgetParams[paramName];}}}
if(!result)result="";return result;};var re=new RegExp("\\\$\\s*?{\\s*?(.*?)\\s*?}","gi");url=url.replace(re,urlParamFunc);}
catch(e){WIDGETBOX.logMessage(e);}}
if(!hideAllParams){var params;if(info.widgetType=="HTML"&&!hasTagAware&&!this.isConfigurable&&!this.cfgString){params=new Object;for(paramName in widgetParams){params["widget."+paramName]=widgetParams[paramName];}}else{params=urlParams;}
url=netUtils.newURLWithParams(url,params);}
var imgNode=null;if(info.widgetType=="IMG"){imgNode=document.createElement("img");imgNode.setAttribute("id","widgetbox_widget_img_"+this.id);imgNode.setAttribute("src",url);if(!info.hasDynamicSize){imgNode.setAttribute("width",info.appWidth);imgNode.setAttribute("height",info.appHeight);}
imgNode.setAttribute("alt",info.name);divNode.appendChild(imgNode);this.widgetImage=imgNode;this.notifyEventListeners("load");if(info.hasDynamicSize)this.notifyEventListeners("resize");if(!this.hasError){WIDGETBOX.subscriber.Main.recordWidgetLoad(WIDGETBOX.globals.widgets[this.id]);}
return;}
if(info.widgetType=="FLASH"){var ft=null;if(!info.hasDynamicSize){ft=new WIDGETBOX.flash.Utils.FlashTag(url,info.appWidth,info.appHeight,"6,0,65,0");}
else{ft=new WIDGETBOX.flash.Utils.FlashTag(url,-1,-1,"6,0,65,0");}
if(info.isRenderedInline||WIDGETBOX.globals.trustedPage)ft.setAllowScriptAccess("always");else ft.setAllowScriptAccess("sameDomain");var tagId="widgetbox_widget_flash_"+this.id;ft.setId(tagId);ft.addParams(pluginParams);ft.addFlashVarsParams(flashVars);divNode.innerHTML=ft.toString();this.iframe=document.getElementById(tagId);this.notifyEventListeners("load");if(info.hasDynamicSize)this.notifyEventListeners("resize");if(!this.hasError){WIDGETBOX.subscriber.Main.recordWidgetLoad(WIDGETBOX.globals.widgets[this.id]);}
return;}
var widgetVisibility="hidden";var w=1;var h=1;if(!info.hasDynamicSize){w=info.appWidth;h=info.appHeight;widgetVisibility="visible";}
var widgetScrolling="no";if(info.scrolling)widgetScrolling="auto";var iframeNode=this.iframe;if(!iframeNode)iframeNode=document.createElement("iframe");iframeNode.setAttribute("name","widgetbox_widget_iframe_"+this.id);iframeNode.setAttribute("id","widgetbox_widget_iframe_"+this.id);iframeNode.setAttribute("width",w);iframeNode.setAttribute("height",h);iframeNode.setAttribute("border","0");iframeNode.setAttribute("frameBorder","0");iframeNode.setAttribute("src","");iframeNode.setAttribute("marginWidth","0");iframeNode.setAttribute("marginHeight","0");iframeNode.setAttribute("vspace","0");iframeNode.setAttribute("hspace","0");iframeNode.setAttribute("allowTransparency","true");iframeNode.setAttribute("scrolling",widgetScrolling);if(!this.iframe){divNode.appendChild(iframeNode);this.iframe=iframeNode;}
var doWidgetPost=false;if(url.length>2000)doWidgetPost=true;if(doWidgetPost){if(isMobileWeb){url="http://widgetserver.com/syndication/html5/"+info.appId;}else if(info.widgetType=='HTML'){url="http://markup.widgetserver.com/syndication/get_widget.html";}else{url=info.url;}}
var formString=null;if(doWidgetPost){formString="<form name=\"widgetForm\" method=\"POST\" action=\""+url+"\">";for(var paramName in urlParams){var paramValue=urlParams[paramName];paramValue=paramValue.replace(/</g,"&lt;");paramValue=paramValue.replace(/>/g,"&gt;");paramValue=paramValue.replace(/"/g,"&quot;");formString+="<input type=\"hidden\" name=\""+
paramName+"\" value=\""+paramValue+"\" />";}
formString+="</form>";}
if(info.hasDynamicSize){if(!this.activityImage){imgNode=document.createElement("img");imgNode.setAttribute("id","widgetbox_widget_activity_img_"+this.id);imgNode.setAttribute("src","http://cdn.widgetserver.com/syndication/images/indicator.gif");imgNode.setAttribute("width","16");imgNode.setAttribute("height","16");imgNode.setAttribute("alt","Loading...");divNode.appendChild(imgNode);this.activityImage=imgNode;}
window["widgetbox_widget_iframe_"+this.id].location=url;}else{var jsUrl=url.replace(/"/g,"\" + '\"' + \"");jsUrl=jsUrl.replace(/'/g,"\" + \"'\" + \"");if(formString){var fdoc=window["widgetbox_widget_iframe_"+this.id].document;fdoc.open();fdoc.write("<html><body onload=\"document.widgetForm.submit();\"><table width=\"100%\" height=\"100%\"><tr><td align=\"center\"><img src=\"http://cdn.widgetserver.com/syndication/images/indicator.gif\" width=\"16\" height=\"16\" alt=\"Loading...\" /></td></tr></table>");fdoc.write(formString);fdoc.write("</body></html>");fdoc.close();}else{document.getElementById('widgetbox_widget_iframe_'+this.id).src=jsUrl;}}
this.notifyEventListeners("load");if(!this.hasError){WIDGETBOX.subscriber.Main.recordWidgetLoad(WIDGETBOX.globals.widgets[this.id]);}}},resize:function(w,h){var info=this.info;if(!info)return;info.appWidth=w;info.appHeight=h;info.wasResized=true;if(this.activityImage){this.activityImage.style.visibility="hidden";this.activityImage.style.display="none";}
if(this.iframe&&info.hasDynamicSize){this.iframe.style.visibility="visible";this.iframe.width=w;this.iframe.height=h;this.notifyEventListeners("resize");}},reloadInfo:function(info){if(!this.loaded)return;this.widgetImage=null;this.info=null;this.loaded=false;var removeContents=false;if(((info.widgetType=='HTML')&&info.isRenderedInline)||info.widgetType=="IMG"||info.widgetType=="FLASH"){removeContents=true;}else if((document.all||navigator.userAgent.toLowerCase().indexOf('safari')!=-1)&&this.iframe){this.iframe=null;removeContents=true;}
if(this.headNodes){var head=document.getElementsByTagName("head").item(0);for(var i=0;i<this.headNodes.length;i++){var headNode=this.headNodes[i];if(headNode)head.removeChild(headNode);this.headNodes[i]=null;}}
this.headNodes=null;if(removeContents)WIDGETBOX.dom.Utils.removeAllChildren(this.divNode);this.loadInfo(info);},unload:function(){this.activityImage=null;this.widgetImage=null;this.iframe=null;this.headNodes=null;this.info=null;this.loaded=false;this.eventListeners=null;}};WIDGETBOX.subscriber.Widget=Widget;WIDGETBOX.setReady("subscriber.Widget");})();WIDGETBOX.namespace("subscriber");if(!WIDGETBOX.subscriber.Panel)(function(){var JSONRequest=WIDGETBOX.net.JSONRequest;var netUtils=WIDGETBOX.net.Utils;function Panel(panelId,parentNode){if(!panelId)panelId="00000000-0000-0000-0000-000000000000";this.defer=false;this.panelId=panelId;this.id=WIDGETBOX.globals.panels.length;this.loaded=false;this.panelInfoRequestPending=false;this.showMark=WIDGETBOX.globals.showPanelMarks;this.info=null;this.activityImage=null;this.panelImage=null;this.divNode=null;this.markNode=null;this.markNodeContainer=null;this.parentNode=parentNode;this.divId="widgetbox_panel_div_"+this.id;this.divNode=document.createElement("div");this.divNode.setAttribute("id",this.divId);this.divNode.className="p"+this.panelId;this.deferRendering=false;parentNode.appendChild(this.divNode);WIDGETBOX.globals.panels[this.id]=this;}
Panel.prototype={addEventListener:function(eventType,listener){if(listener){if((eventType=="load")&&this.loaded){try{listener(this);}
catch(e){WIDGETBOX.logMessage(e);}}
else{if(!this.eventListeners)this.eventListeners=new Object();if(!this.eventListeners[eventType])this.eventListeners[eventType]=new Array();var listeners=this.eventListeners[eventType];listeners.push(listener);}}},notifyEventListeners:function(eventType){if(!this.eventListeners)return;var listeners=this.eventListeners[eventType];if(!listeners)return;for(var i=0;i<listeners.length;i++){var listener=listeners[i];try{listener(this);}
catch(e){WIDGETBOX.logMessage(e);}}},loadInfo:function(info){if(!info&&!this.info)return;if(info&&!this.info)this.info=info;else info=this.info;this.orientation=info.orientation;this.name=info.name;this.title=info.title;this.token=info.token;this.panelId=info.panelId;this.styleContent=info.styleContent;this.heightSize=info.heightSize;this.heightUnit=info.heightUnit;this.widthSize=info.widthSize;this.widthUnit=info.widthUnit;this.overflowStyle=info.overflowStyle;this.showWidgetLinks=info.widgetLinkOff!=true;this.userAccountServiceLevel=info.userAccountServiceLevel;this.markNodeContainer=null;this.headNodes=null;if(!this.loaded){if(this.deferRendering){this.loaded=true;this.notifyEventListeners("load");return;}
var divNode=this.divNode;if(!divNode)return;if(divNode.parentNode&&divNode.parentNode.className=="module-content"){divNode.parentNode.style.marginRight="0";divNode.parentNode.style.marginLeft="0";divNode.parentNode.style.overflow="auto";}
this.loaded=true;var isHorizontal=this.orientation=="HORIZONTAL";if(this.heightSize){divNode.style.height=this.heightSize+this.heightUnit;}
if(this.widthSize){divNode.style.width=this.widthSize+this.widthUnit;}
if(this.overflowStyle==1){divNode.style.overflow="auto";}else if(this.overflowStyle==2){divNode.style.overflow="hidden";}else{divNode.style.overflow="";}
var outsideTable=null;var outsideTr=null;if(isHorizontal){outsideTable=document.createElement("table");outsideTable.cellSpacing="0";var outsideTBody=document.createElement("tbody");outsideTable.appendChild(outsideTBody);outsideTr=document.createElement("tr");outsideTBody.appendChild(outsideTr);divNode.appendChild(outsideTable);}
var panelItems=info.panelItems;try{var htmlContent="";var showOnlyIndex=-1;for(var i=0;i<panelItems.length;i++){var style=null;if(isHorizontal){style=true;}
var item=panelItems[i];if(item.textContent){var textContent=item.textContent;if(i==0&&textContent=='WBX:RANDOMIZE'){var tries=0;while(showOnlyIndex<0){showOnlyIndex=Math.floor(Math.random()*panelItems.length);if(!panelItems[showOnlyIndex].appId&&tries++<100)showOnlyIndex=-1;}}else{if(item.textType=="PLAIN"){textContent=escape(textContent);}
var textNode=document.createElement("div");textNode.setAttribute("id","panel-item-"+item.index);textNode.className="wbx-panel-textblock";textNode.innerHTML=textContent;if(isHorizontal){var outsideTd=document.createElement("td");outsideTd.vAlign="top";outsideTd.appendChild(textNode);outsideTr.appendChild(outsideTd);}else{divNode.appendChild(textNode);}}}else if(item.appId){if(showOnlyIndex<0||showOnlyIndex==i){var parentNodeId="widgetbox_widget_parent_"+WIDGETBOX.globals.widgetCount++;var wrapperNode=document.createElement("div");wrapperNode.className="wbx-panel-widget";var appNode=document.createElement("div");appNode.setAttribute("id",parentNodeId);appNode.className="wbx-widget-div";wrapperNode.appendChild(appNode);if(isHorizontal){var outsideTd=document.createElement("td");outsideTd.vAlign="top";outsideTr.appendChild(outsideTd);outsideTd.appendChild(wrapperNode);}else{divNode.appendChild(wrapperNode);}
WIDGETBOX.subscriber.Main.insertWidget(item.appId,null,appNode,!this.showWidgetLinks);}}}
if(WIDGETBOX.globals.showPanelMarks){if(!this.markNode&&!this.showWidgetLinks&&this.userAccountServiceLevel==0){this.markNode=document.createElement("div");var anchorNode=document.createElement("a");anchorNode.setAttribute("href","http://www.widgetbox.com");anchorNode.setAttribute("target","_blank");var imgNode=document.createElement("img");imgNode.setAttribute("id","widgetbox_widget_mark_img_"+this.id);imgNode.setAttribute("src","http://widgetserver.com/syndication/images/logomark.gif");imgNode.setAttribute("width","81");imgNode.setAttribute("height","18");imgNode.setAttribute("alt","wbx");imgNode.setAttribute("title","Powered by Widgetbox");imgNode.setAttribute("border","0");imgNode.style.border="none";imgNode.style.paddingTop="2px";anchorNode.appendChild(imgNode);this.markNode.appendChild(anchorNode);var markNodeParent=null;if(this.overflowStyle>0){markNodeParent=this.parentNode;if(this.widthSize){this.markNode.style.width=this.widthSize+this.widthUnit;}}else{if(this.markNodeContainer){markNodeParent=this.markNodeContainer;}else{markNodeParent=this.divNode;}}
markNodeParent.appendChild(this.markNode);}}
if(this.styleContent){var head=document.getElementsByTagName("head").item(0);var styleElement=document.createElement("style");styleElement.setAttribute("type","text/css");head.appendChild(styleElement);if(styleElement.styleSheet){styleElement.styleSheet.cssText=this.styleContent;}else{var cssText=document.createTextNode(this.styleContent);styleElement.appendChild(cssText);}
if(!this.styles)this.styles=new Array();this.styles.push(styleElement);}
if(this.headNodes){var head=document.getElementsByTagName("head").item(0);}}
catch(e){WIDGETBOX.logMessage(e);}
this.notifyEventListeners("load");return;}},reloadInfo:function(info){if(!this.loaded)return;this.panelImage=null;this.info=null;this.loaded=false;if(this.markNode){var markNode=this.markNode;this.markNode=null;if(markNode.parentNode){markNode.parentNode.removeChild(markNode);}}
if(this.styles){var head=document.getElementsByTagName("head").item(0);for(var i=0;i<this.styles.length;i++){var styleNode=this.styles[i];if(styleNode)head.removeChild(styleNode);this.styles[i]=null;}}
if(this.headNodes){var head=document.getElementsByTagName("head").item(0);for(var i=0;i<this.headNodes.length;i++){var headNode=this.headNodes[i];if(headNode)head.removeChild(headNode);this.headNodes[i]=null;}}
this.headNodes=null;this.styles=null;WIDGETBOX.dom.Utils.removeAllChildren(this.divNode);this.loadInfo(info);},unload:function(){this.activityImage=null;this.widgetImage=null;this.markNode=null;this.markNodeContainer=null;this.divNode=null;this.eventListeners=null;this.info=null;this.loaded=false;this.styles=null;this.headNodes=null;}};WIDGETBOX.subscriber.Panel=Panel;WIDGETBOX.setReady("subscriber.Panel");})();WIDGETBOX.namespace("subscriber");if(!WIDGETBOX.subscriber.Main)(function(){var globals=WIDGETBOX.globals;var initialized=false;var helperSetupRequested=false;var helperSetup=false;var behaviourSetup=false;var panelInfoRequestPending=false;var JSONRequest=WIDGETBOX.net.JSONRequest;var netUtils=WIDGETBOX.net.Utils;var Behaviour=WIDGETBOX.behaviour.Behaviour;var Helper=WIDGETBOX.flash.Helper;var ua=navigator.userAgent.toLowerCase();var bv=parseFloat(navigator.appVersion);var isOpera=(ua.indexOf('opera')>-1),isSafari=(ua.indexOf('safari')>-1),isGecko=(!isOpera&&!isSafari&&ua.indexOf('gecko')>-1),isIE=(!isOpera&&ua.indexOf('msie')>-1),isOldIE=(isIE&&(ua.indexOf('msie 6')>-1||ua.indexOf('msie 5')>-1)),isFirefox=(ua.indexOf('firefox')>-1),isOldFirefox=(isFirefox&&(ua.indexOf('firefox/2')>-1||ua.indexOf('firefox/1')>-1)),isCamino=(ua.indexOf('camino')),isMac=(ua.indexOf('mac')>-1);var onScrollFn=null;var onResizeFn=null;var isListening=false;var onCloseFn=null;var onShowFn=null;var installWin=null;var lightboxedElement=null;WIDGETBOX.subscriber.Main={init:function(){if(!initialized){initialized=true;var unloader=function(){self.unloadWidgets();};WIDGETBOX.addPageUnloadListener(unloader);self.setupBehaviourForSubscriber();self.loadWidgets();}},getToken:function(){return globals.token;},setupHelperForSubscriber:function(){if(!helperSetupRequested&&!helperSetup){helperSetupRequested=true;var onHelperSetup=function(){if(!helperSetup){helperSetup=true;helperSetupRequested=false;self.loadWidgets();}};var resizeHandler=function(msgPayload){self.resizeWidget(msgPayload.widgetId,msgPayload.width,msgPayload.height);};var requestStyleHandler=function(msgPayload){self.sendStyleMesssageToWidget(msgPayload.widgetId);};Helper.startHelper(globals.token,1001,onHelperSetup);Helper.setMessageHandler("resizeWidget",resizeHandler);Helper.setMessageHandler("requestStyle",requestStyleHandler);}},isHelperSetupForSubscriber:function(){return helperSetup;},setupBehaviourForSubscriber:function(){if(!behaviourSetup){behaviourSetup=true;var tagRules={"a[rel=tag]":function(element){var tagRef=new Object();var href=element.getAttribute("href");tagRef.href=href;tagRef.keywords=unescape(href.substring(href.lastIndexOf('/')+1));tagRef.text=Behaviour.getInnerText(element);if(!WIDGETBOX.globals.taggingMarkup)WIDGETBOX.globals.taggingMarkup=new Array();WIDGETBOX.globals.taggingMarkup.push(tagRef);},"a[rel=wbx-keywords]":function(element){var keywordRef=new Object();var name=element.getAttribute("name");if(!name)name="*";keywordRef.name=name;keywordRef.keywords=element.getAttribute("title");keywordRef.text=Behaviour.getInnerText(element);if(!WIDGETBOX.globals.keywordMarkup)WIDGETBOX.globals.keywordMarkup=new Object();if(!WIDGETBOX.globals.keywordMarkup[name])WIDGETBOX.globals.keywordMarkup[name]=keywordRef;},"wbx:keywords":function(element){var keywordRef=new Object();var name=element.getAttribute("name");if(!name)name="*";keywordRef.name=name;keywordRef.keywords=element.getAttribute("value");keywordRef.text=Behaviour.getInnerText(element);if(!WIDGETBOX.globals.keywordMarkup)WIDGETBOX.globals.keywordMarkup=new Object();if(!WIDGETBOX.globals.keywordMarkup[name])WIDGETBOX.globals.keywordMarkup[name]=keywordRef;},"wbx:widget":function(element){var appId=element.getAttribute("appId");if(appId)self.insertWidget(appId,null,element);},"wbx:panel":function(element){var panelId=element.getAttribute("panelId");if(panelId)self.insertPanel(panelId,element);},"div.wbx-widget":function(element){var appId=element.getAttribute("id");if(appId)self.insertWidget(appId,null,element);},"div.wbx-panel":function(element){var panelId=element.getAttribute("id");if(panelId)self.insertPanel(panelId,element);},"span.wbx-widget":function(element){var appId=element.getAttribute("id");if(appId)self.insertWidget(appId,null,element);},"span.wbx-panel":function(element){var panelId=element.getAttribute("id");if(panelId)self.insertPanel(panelId,element);}};Behaviour.register(tagRules);WIDGETBOX.addPageLoadListener(function(){Behaviour.apply();self.loadWidgets();});}},insertWidget:function(appId,regId,parentNode,suppressGetWidget,suppressEditWidget,loadListener){if(!WIDGETBOX.globals.firstParentNode)WIDGETBOX.globals.firstParentNode=parentNode;WIDGETBOX.dom.Utils.removeAllChildren(parentNode);var widget=new WIDGETBOX.subscriber.Widget(appId,regId,parentNode);if(!suppressGetWidget&&!WIDGETBOX.globals.suppressGetWidget){var getWidgetAppender=function(wgt){if(wgt&&!wgt.getWidgetLoaded){var gw=WIDGETBOX.subscriber.Main.buildGetWidgetElement(wgt,suppressEditWidget);wgt.parentNode.appendChild(gw);wgt.getWidgetLoaded=true;}};widget.addEventListener("load",getWidgetAppender);}else{var loadUserChecker=function(wgt){if(wgt&&wgt.adFriendly){if(wgt.isPreviewInstance){WIDGETBOX.subscriber.Main.setIsSubscriber(wgt.id,false);}else{WIDGETBOX.subscriber.Main.loadUchkr(self.getWidgetDiv(wgt.id),wgt.id,appId);}}};widget.addEventListener("load",loadUserChecker);}
if(!WIDGETBOX.globals.suppressQuantcast){var quantcastAppender=function(wgt){if(wgt&&!wgt.quantcastLoaded&&!wgt.suppressQuantcast){WIDGETBOX.subscriber.Main.loadQuantcast(wgt.parentNode,wgt.regId,wgt.categories);wgt.quantcastLoaded=true;}};widget.addEventListener("load",quantcastAppender);}
if(loadListener)widget.addEventListener("load",loadListener);self.loadWidgets();return widget;},loadUchkr:function(parentDiv,widgetId,appId){var uchkrUrl="http://pub.widgetbox.com/flash/uchkr.swf";var ftdiv=document.createElement("DIV");ft=new WIDGETBOX.flash.Utils.FlashTag(uchkrUrl,1,1,"9,0,0,0");ft.addFlashVar('js',"1");ft.addFlashVar('widgetId',widgetId);ft.addFlashVar('appId',appId);ft.addFlashVar('wmode',"transparent");ft.setAllowScriptAccess("always");if(ft)ftdiv.innerHTML=ft.toString();parentDiv.appendChild(ftdiv);},loadAd:function(wgt){if(!(isMac&&isFirefox)){if(!wgt.info.hasDynamicSize||wgt.info.wasResized){if(wgt&&wgt.adLoaded){var wrapperDivId="wbx-ad-wrapper-"+wgt.id;var existingDiv=document.getElementById(wrapperDivId);if(existingDiv)existingDiv.parentNode.removeChild(existingDiv);}
if(wgt&&wgt.info.isAdFriendly){wgt.adDiv=self.buildAdUnitElement(wgt);var widgetDiv=self.getWidgetDiv(wgt.id);widgetDiv.appendChild(wgt.adDiv);widgetDiv.style.position="relative";wgt.adLoaded=true;self.updateAdFrameContent(wgt);}}}},getWidget:function(widgetId){var widget=null;try{widget=globals.widgets[widgetId];}
catch(e){WIDGETBOX.logMessage(e);}
return widget;},getWidgetCfgString:function(widgetId){var widgetCfg=null;try{var widget=globals.widgets[widgetId];if(widget)widgetCfg=widget.cfgString;}
catch(e){WIDGETBOX.logMessage(e);}
return widgetCfg;},getWidgetDiv:function(widgetId){var divNode=document.getElementById("widgetbox_widget_div_"+widgetId);return divNode;},resizeWidget:function(widgetId,w,h){var widget=self.getWidget(widgetId);if(!widget)return;widget.resize(w,h);if(widget.info.isAdFriendly){if(widget.adEnabled||(widget.isSubscriber&&widget.providerServiceLevel=="0")){WIDGETBOX.subscriber.Main.loadAd(widget);}}},sendStyleMesssageToWidget:function(widgetId){var externalSheets=new Array();for(var i=0;i<document.styleSheets.length;i++){var sheet=document.styleSheets[i];if(sheet&&sheet.href)externalSheets.push(sheet.href);}
Helper.sendMessage(globals.token,widgetId,"sendStyle",{widgetId:widgetId,externalSheets:externalSheets});},unloadWidgets:function(){var widgets=globals.widgets;for(var i=0;i<widgets.length;i++){var widget=widgets[i];if(widget)widget.unload();}},loadWidgets:function(){var widgets=globals.widgets;var all_widgets_loaded=true;for(var i=0;i<widgets.length;i++){var widget=widgets[i];if(widget!=null){if(!widget.loaded&&!widget.defer){all_widgets_loaded=false;}
if(widget.defer)widget.loadInfo();}}
if(all_widgets_loaded){return;}
self.startWidgetInfoRequest();},reloadWidgets:function(){self.unloadWidgets();self.loadWidgets();},startWidgetInfoRequest:function(){var d=document;var pollUrl="http://widgetserver.com/syndication/get_widget.js?callback=WIDGETBOX.subscriber.Main.onWidgetInfoResponse"+"&output=json"+"&location="+WIDGETBOX.net.Utils.urlEncode(window.location.href)+"&timestamp="+netUtils.newTimestamp();var widgets=globals.widgets;for(var i=0;i<widgets.length;i++){var widget=widgets[i];if(widget!=null&&!widget.widgetInfoRequestPending&&!widget.loaded&&!widget.defer){var wScriptId="widgetbox_getwidgetinfo_script_"+widget.appId;var getWidgetInfoScript=d.getElementById(wScriptId);var head=document.documentElement.firstChild;if(!head)head=document.getElementsByName("head")[0];if(getWidgetInfoScript){try{head.removeChild(getWidgetInfoScript);}catch(e){}}
getWidgetInfoScript=d.createElement("script");var wPollUrl=pollUrl+"&appId."+i+"="+widget.appId;if(widget.regId){wPollUrl+="&regId."+i+"="+widget.regId;}
getWidgetInfoScript.setAttribute("src",wPollUrl);getWidgetInfoScript.setAttribute("id",wScriptId);getWidgetInfoScript.setAttribute("async","true");head.appendChild(getWidgetInfoScript);widget.widgetInfoRequestPending=true;}}},onWidgetInfoResponse:function(infoResponse){try{if(!globals.token){globals.tokenTime=(new Date()).getTime();globals.token=infoResponse.token;}else{infoResponse.token=globals.token;}
if(infoResponse.isBatch){WIDGETBOX.logMessage("Batch response not supported.");}else{var infoSet=infoResponse.widgets;if(infoSet){for(var i=0;i<infoSet.length;i++){var widgetInfo=infoSet[i];for(var i=0;i<globals.widgets.length;i++){var widget=globals.widgets[i];if(widget.appId==widgetInfo.appId){try{if(widget){widget.widgetInfoRequestPending=false;widget.loadInfo(widgetInfo);}}catch(e){widget.loaded=true;WIDGETBOX.logMessage(e);}}}}
self.loadWidgets();}}}catch(e){WIDGETBOX.logMessage(e);}},buildGetWidgetElement:function(widget,suppressEditWidget){var wrapperDiv=document.createElement("div");wrapperDiv.setAttribute("class","wbx-gw-wrapper");wrapperDiv.setAttribute("id","wbx-gw-button-wrapper-"+widget.id)
widget.suppressEditWidget=suppressEditWidget;WIDGETBOX.subscriber.Main.renderGetWidgetButton(wrapperDiv,widget,false);return wrapperDiv;},buildAdUnitElement:function(widget){var wrapperDiv=document.createElement("div");wrapperDiv.setAttribute("class","wbx-ad-wrapper");wrapperDiv.setAttribute("id","wbx-ad-wrapper-"+widget.id);wrapperDiv.style.height="1px";wrapperDiv.style.width=widget.info.appWidth+"px";wrapperDiv.style.position="absolute";wrapperDiv.style.top="0px";wrapperDiv.style.left="0px";wrapperDiv.style.textAlign="center";wrapperDiv.style.overflow="hidden";var adFrameHTML="<iframe src=\"\" width=\"100%\" scrolling=\"no\" height=\"1\" frameborder=\"0\" name=\"wbx-adframe-"+widget.id+"\" id=\"wbx-adframe-"+widget.id+"\" border=\"0\" allowtransparency=\"true\" style=\"position:absolute; top:0px; left:0px; visibility: visible;\"/>";wrapperDiv.innerHTML=adFrameHTML;return wrapperDiv;},updateAdFrameContent:function(widget){var adFrameHead='<head><script>var widget = parent.WIDGETBOX.subscriber.Main.getWidget('+widget.id+');function fetchPromoScript(){'+'var headID = document.getElementsByTagName("head")[0];'+'var newScript = document.createElement("script");'+'newScript.type = "text/javascript";'+'newScript.src = "http://cdn.widgetserver.com/syndication/promo/promo/";'+'headID.appendChild(newScript);}</script><style type="text/css">'+'img{border:none;}'+'a{outline:none;}'+'#ad-loader-div{position:absolute;}'+'</style><\/head>';var adFrameBody='<body style="background-color:transparent;padding:0px;margin:0px;" onload="fetchPromoScript();">'+'<div id="ad-loader-div"><\/div>'+'<\/body>';var iframe=document.getElementById("wbx-adframe-"+widget.id);var iframeDoc;if(iframe.contentDocument){iframeDoc=iframe.contentDocument;}
else if(iframe.contentWindow){iframeDoc=iframe.contentWindow.document;}
else if(window.frames[iframe.name]){iframeDoc=window.frames[iframe.name].document;}
if(iframeDoc){iframeDoc.open();iframeDoc.write('<html>'+adFrameHead+adFrameBody+'<\/html>');iframeDoc.close();}},getAdChannelsAsQueryString:function(wgt){var channels=wgt.channels;var channelString="";for(var channelId in channels){if(channelString!=""){channelString+="&";}
if(channels[channelId].channelName){channelString+=WIDGETBOX.net.Utils.urlEncode(channelId)+"="+WIDGETBOX.net.Utils.urlEncode(channels[channelId].channelName);}}
return channelString;},loadQuantcast:function(container,regId,labels,queryString){var qUrl='http://cdn.widgetserver.com/syndication/flash/wrapper/quantcast.swf';var ft=new WIDGETBOX.flash.Utils.FlashTag(qUrl,1,1,"8,0,0,0");ft.setAllowScriptAccess("always");ft.setWmode("transparent");if(regId)ft.addFlashVar("id",regId);if(queryString){ft.addFlashVars(queryString);}else{if(labels)ft.addFlashVar("cat",labels);}
var quantcastDiv=document.createElement("div");quantcastDiv.style.height="1px";quantcastDiv.style.width="1px";quantcastDiv.style.bottom="0px";quantcastDiv.style.right="0px";quantcastDiv.innerHTML=ft.toString();container.appendChild(quantcastDiv);},renderGetWidgetButton:function(parentDiv,widget,isOwner,isReload){var ft=null;if("DISABLE_GW"!=widget.subscribeMode&&widget.buttonMode!=2){var gwUrl="http://cdn.widgetserver.com/syndication/flash/install/getwidget.swf";var buttonWidth=widget.info.hasDynamicSize?141:widget.info.appWidth;ft=new WIDGETBOX.flash.Utils.FlashTag(gwUrl,buttonWidth,23,"9,0,0,0");ft.setAllowScriptAccess("always");ft.setId("wbx-gw-button-"+widget.id);ft.setWmode("transparent");ft.addFlashVar("w",buttonWidth);if(!WIDGETBOX.globals.disableGetWidget){if("REQUIRE_CONFIG"==widget.subscribeMode){if(!widget.configUrl){ft.addFlashVar('gwUrl',widget.detailsUrl);}else{ft.addFlashVar('gwUrl',widget.configUrl);}}else{ft.addFlashVar('gwArg',new String(widget.id));}
if(!widget.suppressEditWidget&&isOwner){ft.addFlashVar('cw',"true");}
ft.addFlashVar('instanceId',widget.appId);}
ft.addFlashVar('widgetId',new String(widget.id));ft.addFlashVar('cdnRuntimeUrl','http://cdn.widgetserver.com/syndication');ft.addFlashVar('cdnGalleryUrl','http://pub.widgetbox.com');if(!widget.suppressEditWidget){var configUrl="http://www.widgetbox.com/dash/subscription.jsp?id="+widget.appId;if(widget.configUrl){configUrl=widget.configUrl;configUrl+=(configUrl.indexOf('?')>0)?"&":"?";configUrl+="wbx.edit=true&wbx.id="+widget.appId;configUrl+="&appInstance.name="+escape(widget.name);configUrl+="&appInstance.width="+widget.width;configUrl+="&appInstance.height="+widget.height;if(widget.configParams){for(var configName in widget.configParams){configUrl+="&"+configName+"="+escape(widget.configParams[configName].value);}}}
ft.addFlashVar('cwUrl',configUrl);}else{ft.addFlashVar('suppressCW','true')}
if(ft)parentDiv.innerHTML=ft.toString();}else if(widget.info.isAdFriendly){if(widget.isPreviewInstance){WIDGETBOX.subscriber.Main.setIsSubscriber(widget.id,false);}else{WIDGETBOX.subscriber.Main.loadUchkr(parentDiv,widget.id,widget.appId);}}},setIsSubscriber:function(widgetId,isSubscriber){if(!widgetId)widgetId=0;var widget=WIDGETBOX.globals.widgets[widgetId];widget.isSubscriber=isSubscriber;if(widget.info.isAdFriendly){if(widget.adEnabled||(widget.isSubscriber&&(widget.providerServiceLevel=="0"||widget.info.widgetPrototype=="TEMPLATE"))){WIDGETBOX.subscriber.Main.loadAd(widget);}}},unloadPanels:function(){var panels=globals.panels;for(var i=0;i<panels.length;i++){var panel=panels[i];if(panel)panel.unload();}},loadPanels:function(){var panels=globals.panels;var all_panels_loaded=true;for(var i=0;i<panels.length;i++){var panel=panels[i];if(panel&&!panel.loaded&&!panel.defer){all_panels_loaded=false;}
if(panel.defer)panel.loadInfo();}
if(all_panels_loaded)return;self.startPanelInfoRequest();},startPanelInfoRequest:function(){var d=document;var pollUrl="http://widgetserver.com/syndication/get_panel.js?callback=WIDGETBOX.subscriber.Main.onPanelInfoResponse"+"&output=json"+"&location="+escape(window.location.href)+"&timestamp="+netUtils.newTimestamp();var panels=globals.panels;for(var i=0;i<panels.length;i++){var panel=panels[i];if(!panel.panelInfoRequestPending&&!panel.loaded){var scriptId="widgetbox_getpanelinfo_script_"+panel.panelId;var getPanelInfoScript=d.getElementById(scriptId);if(getPanelInfoScript){try{var head=document.documentElement.firstChild;if(!head)head=document.getElementsByName("head")[0];head(getPanelInfoScript);}catch(e){}}
getPanelInfoScript=d.createElement("script");var pPollUrl=pollUrl+"&panelId."+i+"="+panel.panelId;getPanelInfoScript.setAttribute("src",pPollUrl);getPanelInfoScript.setAttribute("id",scriptId);getPanelInfoScript.setAttribute("async","true");var head=document.documentElement.firstChild;if(!head)head=document.getElementsByName("head")[0];head.appendChild(getPanelInfoScript);panel.panelInfoRequestPending=true;}}},insertPanel:function(panelId,parentNode){WIDGETBOX.dom.Utils.removeAllChildren(parentNode);var panel=new WIDGETBOX.subscriber.Panel(panelId,parentNode);self.loadPanels();return panel;},getPanelDiv:function(panelId){var divNode=document.getElementById("widgetbox_panel_div_"+panelId);return divNode;},getPanel:function(panelId){var panel=null;try{panel=globals.panels[panelId];}
catch(e){WIDGETBOX.logMessage(e);}
return panel;},onPanelInfoResponse:function(infoResponse){try{var infoSet=infoResponse.panels;if(infoResponse.isBatch){WIDGETBOX.logMessage("Batch response not supported.");}else{var infoSet=infoResponse.panels;if(infoSet){for(var i=0;i<infoSet.length;i++){var panelInfo=infoSet[i];for(var i=0;i<globals.panels.length;i++){var panel=globals.panels[i];if(panel.panelId==panelInfo.panelId){if(!panelInfo.isError){panel.loadInfo(panelInfo);}else{panel.loaded=true;panel.panelInfoRequestPending=false;}}}}}
self.loadPanels();}}
catch(e){WIDGETBOX.logMessage(e);}},getViewportHeight:function(){var height=-1;var mode=document.compatMode;if((mode||isIE)&&!isOpera){switch(mode){case'CSS1Compat':height=document.documentElement.clientHeight;break;default:height=document.body.clientHeight;}}else{height=(window.innerHeight)?window.innerHeight:0;}
return height;},getViewportWidth:function(){var width=-1;var mode=document.compatMode;if(mode||isIE){switch(mode){case'CSS1Compat':width=document.documentElement.clientWidth;break;default:width=document.body.clientWidth;}}else{width=(window.innerWidth)?window.innerWidth:0;}
return width;},findPosX:function(obj){var curleft=0;if(obj.offsetParent){while(1){curleft+=obj.offsetLeft;if(!obj.offsetParent){break;}
obj=obj.offsetParent;}}else if(obj.x){curleft+=obj.x;}
return curleft;},findPosY:function(obj){var curtop=0;if(obj.offsetParent){while(1){curtop+=obj.offsetTop;if(!obj.offsetParent){break;}
obj=obj.offsetParent;}}else if(obj.y){curtop+=obj.y;}
return curtop;},getMarginValues:function(){var marginTop=parseInt(document.body.style.marginTop,10);var marginRight=parseInt(document.body.style.marginRight,10);var marginBottom=parseInt(document.body.style.marginBottom,10);var marginLeft=parseInt(document.body.style.marginLeft,10);var valuesArray=new Array();valuesArray[0]=isNaN(marginTop)?0:marginTop;valuesArray[1]=isNaN(marginRight)?0:marginRight;valuesArray[2]=isNaN(marginBottom)?0:marginBottom;valuesArray[3]=isNaN(marginLeft)?0:marginLeft;return valuesArray;},getDocumentHeight:function(){var mode=document.compatMode;var scrollHeight=(mode!='CSS1Compat')?document.body.scrollHeight:document.documentElement.scrollHeight;var viewportHeight=self.innerHeight;if(isSafari){viewportHeight=document.body.scrollHeight;}else if((mode||isIE)&&!isOpera){viewportHeight=(mode=='CSS1Compat')?document.documentElement.clientHeight:document.body.clientHeight;}
return Math.max(scrollHeight,viewportHeight);},getDocumentWidth:function(){var mode=document.compatMode;var scrollWidth=(mode!='CSS1Compat')?document.body.scrollWidth:document.documentElement.scrollWidth;var viewportWidth=self.innerWidth;if(isSafari){viewportWidth=document.body.scrollWidth;}else if(mode||isIE){viewportWidth=(mode=='CSS1Compat')?document.documentElement.clientWidth:document.body.clientWidth;}
return Math.max(scrollWidth,viewportWidth);},getScrollTop:function(){var scrollTop=(document.documentElement&&document.documentElement.scrollTop)?document.documentElement.scrollTop:document.body.scrollTop;return scrollTop;},getScrollLeft:function(){var scrollTop=(document.documentElement&&document.documentElement.scrollLeft)?document.documentElement.scrollLeft:document.body.scrollLeft;return scrollTop;},displayMask:function(useDark,hideElements){var maskDiv=document.createElement("div");maskDiv.id="wbx-mask-div";if(!useDark){if(isMac&&(isFirefox||isCamino)){maskDiv.style.backgroundImage="url(http://www.widgetbox.com/images/light-bg.png)";maskDiv.style.backgroundRepeat="repeat";WIDGETBOX.dom.Utils.insertCssText('body * { opacity: 1.0 !important; }',document,null,"wbx-hide-opacity-style");}else{maskDiv.style.opacity="0.4";maskDiv.style.MozOpacity="0.4";maskDiv.style.filter="alpha(opacity=40)";maskDiv.style.backgroundColor="#FFF";}}else{if(isMac&&(isFirefox||isCamino)){maskDiv.style.backgroundImage="url(http://www.widgetbox.com/images/dark-bg.png?v=1)";maskDiv.style.backgroundRepeat="repeat";WIDGETBOX.dom.Utils.insertCssText('body * { opacity: 1.0 !important; }',document,null,"wbx-hide-opacity-style");}else{maskDiv.style.opacity="0.8";maskDiv.style.MozOpacity="0.8";maskDiv.style.filter="alpha(opacity=80)";maskDiv.style.backgroundColor="#000";}}
maskDiv.style.left="0";maskDiv.style.top="0";maskDiv.style.position="absolute";maskDiv.style.zIndex="5001";document.getElementsByTagName("body")[0].appendChild(maskDiv);var maskDivObj=document.getElementById("wbx-mask-div");self.resizeMask();if(maskDivObj)maskDivObj.onclick=function(){WIDGETBOX.subscriber.Main.closeGetWidgetDiv();};if(!self.isListening){setTimeout(function(){WIDGETBOX.subscriber.Main.onScrollResizeCallback()},50);self.isListening=true;}
if(hideElements){var objectTags=document.getElementsByTagName("object");for(var i=0;i<objectTags.length;i++){objectTags[i].style.visibility="hidden";}
var embedTags=document.getElementsByTagName("embed");for(var i=0;i<embedTags.length;i++){embedTags[i].style.visibility="hidden";}
for(var i=0;i<globals.widgets.length;i++){if(globals.widgets[i].iframe){globals.widgets[i].iframe.style.visibility="hidden";}}
for(var i=0;i<globals.widgets.length;i++){if(globals.widgets[i].adDiv){globals.widgets[i].adDiv.style.display="none";}}}},hideMask:function(){var closeMaskObj=document.getElementById("wbx-close-mask");if(closeMaskObj){closeMaskObj.style.display="none";closeMaskObj.parentNode.removeChild(closeMaskObj);}
var maskDivObj=document.getElementById("wbx-mask-div");if(maskDivObj){maskDivObj.style.display="none";maskDivObj.parentNode.removeChild(maskDivObj);var objectTags=document.getElementsByTagName("object");for(var i=0;i<objectTags.length;i++){var objectObj=objectTags[i]
objectObj.style.visibility="visible";}
var embedTags=document.getElementsByTagName("embed");for(var i=0;i<embedTags.length;i++){var embedObj=embedTags[i];embedObj.style.visibility="visible";}
for(var i=0;i<globals.widgets.length;i++){if(globals.widgets[i].iframe){globals.widgets[i].iframe.style.visibility="visible";}}
for(var i=0;i<globals.widgets.length;i++){if(globals.widgets[i].adDiv){globals.widgets[i].adDiv.style.display="";}}
if(isMac&&isFirefox){WIDGETBOX.dom.Utils.insertCssText('/* unhide */',document,null,"wbx-hide-opacity-style");}}
if(window.scrollBy){window.scrollBy(0,1);window.scrollBy(0,-1);}},resizeMask:function(){var maskDivObj=document.getElementById("wbx-mask-div");if(maskDivObj){var docWidth=self.getDocumentWidth();var docHeight=self.getDocumentHeight();if(docWidth+"px"!=maskDivObj.style.width)maskDivObj.style.width=docWidth+"px";if(docHeight+"px"!=maskDivObj.style.height)maskDivObj.style.height=docHeight+"px";}},onScrollResizeCallback:function(){self.positionGetWidgetDiv();self.resizeMask();if(self.isListening){setTimeout(function(){WIDGETBOX.subscriber.Main.onScrollResizeCallback()},50);}},setAdUnitSizeAndLocation:function(widgetId,x,y,w,h,ww,wh){if(widgetId){var adFrame=document.getElementById("wbx-adframe-"+widgetId);var adDiv=document.getElementById("wbx-ad-wrapper-"+widgetId);if(adDiv&&adFrame){var firstWidgetElement;var widgetDiv=self.getWidgetDiv(widgetId);try{firstWidgetElement=widgetDiv.childNodes[0];}catch(e){WIDGETBOX.logMessage(e);}
var newWidth="0px";if(!isNaN(w)){newWidth=w+"px";}
var newHeight="0px";if(!isNaN(h)){newHeight=h+"px";}
var leftPosition=(isNaN(x))?"0":x;var topPosition=(isNaN(y))?"0":y;if(!isOldIE){var parentNode=WIDGETBOX.globals.widgets[widgetId].parentNode;var wasParentNodeGenerated=(parentNode.id=="widgetbox_widget_parent_"+widgetId);var isDivNodeWider=(widgetDiv.scrollWidth>firstWidgetElement.width);var useOffset=firstWidgetElement&&(!isIE||(!wasParentNodeGenerated&&isDivNodeWider));if(useOffset){leftPosition=Number(leftPosition)+Number(firstWidgetElement.offsetLeft);topPosition=Number(topPosition)+Number(firstWidgetElement.offsetTop);}}
adDiv.style.width=newWidth;adFrame.style.width=newWidth;adDiv.style.left=leftPosition+"px";adFrame.style.height=wh+"px";adDiv.style.height=newHeight;if(topPosition<0){topPosition=0;}
adDiv.style.top=topPosition+"px";adFrame.style.top=(-topPosition)+"px";}}},injectAd:function(widgetId,payload){var widget=WIDGETBOX.globals.widgets[widgetId];if(widget&&widget.info.widgetPrototype=="BLIDGET"&&widget.iframe&&widget.iframe.loadAdFromXML){widget.iframe.loadAdFromXML(payload,widget.isSubscriber);}},makeLightbox:function(hideElements){self.displayMask(true,hideElements);var divObj=document.getElementById('wbx-lb-cont-div');if(!divObj){divObj=document.createElement("div");divObj.id='wbx-lb-cont-div';divObj.style.cssFloat="left";divObj.style.styleFloat="left";divObj.style.position="absolute";divObj.style.zIndex="5002";divObj.style.border="0px";divObj.style.padding="5px";divObj.style.margin="0px";divObj.style.border="2px solid #999";divObj.style.background="#FFF";document.getElementsByTagName("body")[0].appendChild(divObj);}
lightboxedElement=divObj;return lightboxedElement;},renderWidgetInLightbox:function(appId,cfgString,showGW){var divObj=self.makeLightbox(true);var wrapper=document.createElement("div");wrapper.id="wbxLightboxedElement";divObj.appendChild(wrapper);var widget=self.insertWidget(appId,null,wrapper,!showGW,true,self.positionLightbox);widget.cfgString=cfgString;},positionLightbox:function(widget){var remoteIframeObj=document.getElementById("wbxLightboxedElement");self.positionGetWidgetDiv(widget.info.appWidth,widget.info.appHeight);var closeX=Number(lightboxedElement.offsetLeft)+Number(widget.info.appWidth)+3;var closeY=Number(lightboxedElement.offsetTop)-13;self.renderCloseButton(lightboxedElement,"outside","right","WIDGETBOX.subscriber.Main.closeGetWidgetDiv()","Turn the lights on!");},renderCloseButton:function(parentNode,placement,location,action,title){if(!placement)placement="outside";if(!location)location="right";if(!title)title="Close"
if(!action)action="WIDGETBOX.subscriber.Main.closeGetWidgetDiv()";var closeAdDiv=document.createElement("div");closeAdDiv.setAttribute("id","wbx-close-mask");closeAdDiv.setAttribute("class","wbx-close-mask");closeAdDiv.style.position="absolute";closeAdDiv.style.zIndex="6000";if(placement=="outside"){closeAdDiv.style.top="-18px";if(location=="left"){closeAdDiv.style.left="-20px";}else{closeAdDiv.style.right="-20px";}}else{closeAdDiv.style.top="0px";if(location=="left"){closeAdDiv.style.left="0px";}else{closeAdDiv.style.right="0px";}}
var bgStyle="";if(isOldIE){bgStyle=' filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src="http://cdn.widgetserver.com/syndication/images/closebox.png?cb='+new Date().getTime()+'", sizingMethod="scale");';}else{bgStyle=' background:url(http://cdn.widgetserver.com/syndication/images/closebox.png?cb='+new Date().getTime()+');';}
closeAdDiv.innerHTML="<a style='text-decoration:none;' title='"+title+"' href='javascript:"+action+"'><div id='wbx-close-btn' style='width:30px; height:30px;"+bgStyle+"'>&nbsp;</div></a>";parentNode.appendChild(closeAdDiv)},openGetWidgetDiv:function(widgetId){self.renderGetWidget(new WIDGETBOX.subscriber.WidgetMenuConfig(WIDGETBOX.globals.widgets[widgetId]));},renderGetWidget:function(config){WIDGETBOX.subscriber.Main.closeGetWidgetDiv();if(WIDGETBOX.globals.disableInstallerMenu)return;if(!WIDGETBOX.globals.renderInstallerMenuInline)self.displayMask(false,true);var divObj=document.getElementById("wbx-gw-menu-div");if(divObj==null){divObj=document.createElement("div");divObj.id="wbx-gw-menu-div";divObj.style.cssFloat="left";divObj.style.styleFloat="left";divObj.style.position="absolute";divObj.style.zIndex="5003";divObj.style.border="0px";divObj.style.backgroundColor="transparent";divObj.style.padding="0px";divObj.style.margin="0px";divObj.style.width="255px";divObj.style.height="458px";divObj.style.display="none";divObj.style.border="0";document.getElementsByTagName("body")[0].appendChild(divObj);}else{divObj.innerHTML="";}
try{var version=WIDGETBOX.flash.Utils.GetFlashVersionArray();if(!config.panelId&&version[0]<9){var versionStr=version[0]+"."+version[1]+"r"+version[2];var iframeId="wbxLightboxedElement";var iframeObj=document.createElement("iframe");iframeObj.id=iframeId;iframeObj.name=iframeId;iframeObj.width="255";iframeObj.height="458";iframeObj.frameBorder="0";iframeObj.scrolling="no";iframeObj.marginWidth="0";iframeObj.marginHeight="0";iframeObj.style.border="1px solid #000";divObj.appendChild(iframeObj);if(window.frames[iframeId]&&window.frames[iframeId].name!=iframeId){window.frames[iframeId].name=iframeId;}
var closeObj=document.createElement("a");closeObj.id="closebtn";closeObj.innerHTML="Close";closeObj.onclick=function(){self.closeGetWidgetDiv();};closeObj.style.background="url(/css/images/close-btn.png) no-repeat";closeObj.style.display="block";closeObj.style.position="absolute";closeObj.style.top="3px";closeObj.style.right="2px";closeObj.style.width="24px";closeObj.style.height="20px";closeObj.style.textIndent="-9999px";closeObj.style.overflow="hidden";divObj.appendChild(closeObj);var formObj=document.createElement("form");formObj.id="wbx_flash_upgrade_form";formObj.target="wbxLightboxedElement";formObj.action="http://www.widgetbox.com/getwidget/flash_update.jsp";formObj.method="POST";divObj.appendChild(formObj);var versionField=document.createElement("input");versionField.type="hidden";versionField.name="version";versionField.value=versionStr;formObj.appendChild(versionField);var appRegIdField=document.createElement("input");appRegIdField.type="hidden";appRegIdField.name="appRegId";appRegIdField.value=config.regId;formObj.appendChild(appRegIdField);var locationField=document.createElement("input");locationField.type="hidden";locationField.name="location";locationField.value=location.href;formObj.appendChild(locationField);if(config.configData){var appConfigField=document.createElement("input");appConfigField.type="hidden";appConfigField.name="appConfig";appConfigField.value=config.configData;formObj.appendChild(appConfigField);}
if(config.sourceId){var sourceIdField=document.createElement("input");sourceIdField.type="hidden";sourceIdField.name="sourceId";sourceIdField.value=config.sourceId;formObj.appendChild(sourceIdField);}
if(config.instId){var instIdField=document.createElement("input");instIdField.type="hidden";instIdField.name="instId";instIdField.value=config.instId;formObj.appendChild(instIdField);}
formObj.submit();}else{var gwUrl="http://cdn.widgetserver.com/syndication/flash/install/menu.swf";var menuWidth=(WIDGETBOX.globals.renderInstallerMenuInline)?281:255;var menuHeight=458;var docWidth=WIDGETBOX.subscriber.Main.getDocumentWidth();var docHeight=WIDGETBOX.subscriber.Main.getDocumentHeight();var needsScale=false;if(docHeight<menuHeight){menuHeight=docHeight;needsScale=true;}
if(docWidth<menuWidth){menuWidth=docWidth;needsScale=true;}
var ft=null;try{ft=new WIDGETBOX.flash.Utils.FlashTag(gwUrl,menuWidth,menuHeight,"9,0,0,0");}catch(e){ft=new WIDGETBOX.flash.Utils.FlashTag(gwUrl,menuWidth,menuHeight,"9,0,0,0");}
ft.setAllowScriptAccess("always");ft.setId("wbxLightboxedElement");ft.setSalign("tl");ft.setWmode("transparent");ft.addFlashVar('syndicationHost',config.syndUrl);ft.addFlashVar('galleryHost',config.galleryUrl);ft.addFlashVar('cdnRuntimeURL',config.cdnRuntimeUrl);ft.addFlashVar('appRegId',config.regId);ft.addFlashVar('sourceId',config.sourceId);ft.addFlashVar('instanceId',config.instId);ft.addFlashVar('panelId',config.panelId);ft.addFlashVar('context',config.context);ft.addFlashVar('locationURL',config.location);ft.addFlashVar('fromPartnerCode',config.fromPn);ft.addFlashVar('toPartnerCode',config.toPn);ft.addFlashVar('fromGallery',config.galleryCode);ft.addFlashVar('appConfig',config.configData);if(config.jsOnly!=null){ft.addFlashVar('jsOnly',config.jsOnly);}
if(WIDGETBOX.globals.renderInstallerMenuInline){ft.addFlashVar('onPage',"true");}
if(needsScale){ft.addFlashVar('w',menuWidth-20);ft.addFlashVar('h',menuHeight-20);}else if(WIDGETBOX.globals.renderInstallerMenuInline){ft.addFlashVar('w',280);}
divObj.style.display="";divObj.innerHTML=ft.toString();if(self.onShowFn)self.onShowFn();}
if(divObj.style.position=="absolute"){self.positionGetWidgetDiv();lightboxedElement=divObj;}}catch(e){}},positionGetWidgetDiv:function(w,h){var remoteIframeObj=document.getElementById("wbxLightboxedElement");if(remoteIframeObj!=null){w=remoteIframeObj.offsetWidth;h=remoteIframeObj.offsetHeight;}
var divObj=lightboxedElement;if(divObj){divObj.style.display="";var leftPos=(self.getViewportWidth()/2)-(w/2)+self.getScrollLeft();var topPos=(self.getViewportHeight()/2)-(h/2)+self.getScrollTop();if(leftPos<0){divObj.style.left="0px";}else if(leftPos+"px"!=divObj.style.left){divObj.style.left=leftPos+"px";}
if(topPos<0){divObj.style.top="0px";}else if(topPos+"px"!=divObj.style.top){divObj.style.top=topPos+"px";}}},closeGetWidgetDiv:function(){self.isListening=false;var objToRemove=lightboxedElement;if(WIDGETBOX.globals.renderInstallerMenuInline){if(objToRemove!=null)objToRemove.style.display="none";}else{if(objToRemove!=null){setTimeout(function(){if(objToRemove.parentNode&&objToRemove.parentNode.hasChildNodes()){WIDGETBOX.dom.Utils.removeAllChildren(objToRemove);var objParentNode=objToRemove.parentNode;objParentNode.removeChild(objToRemove);}
objToRemove=null;},100);if(self.onCloseFn)self.onCloseFn();}
self.hideMask();}},openLoadingPopup:function(winName){self.installWin=window.open('http://www.widgetbox.com/getwidget/progress.jsp',winName,'toolbar=yes, resizable=yes, location=yes, status=no, width=800, height=600, scrollbars=yes');},focusLoadingPopup:function(){if(self.installWin)self.installWin.focus();},recordWidgetLoad:function(widget){if(!WIDGETBOX.globals.disableHitTracking){var tracker=new Image(1,1);var tSrc='http://widgetserver.com/metrics/image.gif?';tSrc+='w=1&i='+widget.appId;tSrc+='&sid='+widget.sid;tSrc+='&aik='+widget.appPK;tSrc+='&ark='+widget.regPK;tSrc+='&apk='+widget.providerPK;tSrc+='&uid='+widget.userPK;tSrc+='&pnk='+widget.installPartnerNetworkPK;if(widget.fromGalleryPK)tSrc+='&gk='+widget.fromGalleryPK;tSrc+='&ts='+new Date().getTime();tSrc+='&CXNID=2000003.0000000001NXC';tSrc+='&udd='+widget.isUseDefaultDomain;if(widget.defaultDomain){tSrc+='&dd='+widget.defaultDomain;}
tSrc+='&l='+WIDGETBOX.net.Utils.urlEncode(window.location.href);tracker.alt="";tracker.onload=function(){return;}
tracker.src=tSrc;self.fireWidgetCounter(widget);}},fireWidgetCounter:function(widget){if(widget.counterUrl){var tracker=new Image(1,1);var tSrc=widget.counterUrl;tracker.alt="";tracker.onload=function(){return;}
tracker.src=tSrc;}},getElementsByClass:function(searchClass,node,tag){var classElements=new Array();if(node==null)node=document;if(tag==null)tag='*';var els=node.getElementsByTagName(tag);var elsLen=els.length;var pattern=new RegExp("(^|\\s)"+searchClass+"(\\s|$)");for(i=0,j=0;i<elsLen;i++){if(pattern.test(els[i].className)){classElements[j]=els[i];j++;}}
return classElements;},generateGUID:function(rfc4122){var len=(rfc4122)?36:32,chars='0123456789abcdef'.split(''),r,uuid=[];if(rfc4122){uuid[8]=uuid[13]=uuid[18]=uuid[23]='-';uuid[14]='4';}
for(var i=0;i<len;i++){if(!uuid[i]){r=0|Math.random()*16;uuid[i]=chars[(i==19)?(r&0x3)|0x8:r];}}
return uuid.join('');}};var self=WIDGETBOX.subscriber.Main;self.init();WIDGETBOX.setReady("subscriber.Main");})();if(!WIDGETBOX.subscriber.WidgetMenuConfig)(function(){function WidgetMenuConfig(widget){this.regId;this.sourceId;this.instId;this.panelId;this.context;this.toPn;this.fromPn;this.galleryUrl;this.syndUrl;this.cdnRuntimeUrl;this.location;this.galleryCode;this.configData;this.jsOnly;this.widgetId=0;if(widget){this.regId=widget.regId;this.sourceId=widget.appId;this.instId=null;this.panelId=null;this.context="wild";this.toPn=null;this.fromPn=widget.partnerNetwork;this.syndUrl='http://widgetserver.com/syndication';this.galleryUrl='http://www.widgetbox.com';this.cdnRuntimeUrl='http://cdn.widgetserver.com/syndication';this.location=window.location.href;this.galleryCode=null;this.configData=widget.cfgString;this.jsOnly=null;this.widgetId=widget.id;}}
WIDGETBOX.subscriber.WidgetMenuConfig=WidgetMenuConfig;})();