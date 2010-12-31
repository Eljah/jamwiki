// variables needed to support the fckeditor integration
var wgScriptPath = "";
var wgScript = "/index.php";
var wgServer = "";

// re-implementation of the Mediawiki function to support the fckeditor integration
function addOnloadHook(hookFunct) {
	addEvent(window, "load", hookFunct);
}

// provide functionality to allow triggering functions when events fire
function addEvent(obj, evType, fn){
	if (obj.addEventListener){
		obj.addEventListener(evType, fn, false);
	} else if (obj.attachEvent){
		obj.attachEvent("on" + evType, fn);
	}
}

/** ==== from ajax.js (BSD license) ==== */

// remote scripting library
// (c) copyright 2005 modernmethod, inc
var sajax_request_type = "GET";

/**
* compatibility wrapper for creating a new XMLHttpRequest object.
*/
function sajax_init_object() {
	var A;
	try {
		// Try the new style before ActiveX so we don't
		// unnecessarily trigger warnings in IE 7 when
		// set to prompt about ActiveX usage
		A = new XMLHttpRequest();
	} catch (e) {
		try {
			A=new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try {
				A=new ActiveXObject("Microsoft.XMLHTTP");
			} catch (oc) {
				A=null;
			}
		}
	}

	return A;
}

/**
* Perform an ajax call to mediawiki. Calls are handeled by AjaxDispatcher.php
*   func_name - the name of the function to call. Must be registered in $wgAjaxExportList
*   args - an array of arguments to that function
*   target - the target that will handle the result of the call. If this is a function,
*            if will be called with the XMLHttpRequest as a parameter; if it's an input
*            element, its value will be set to the resultText; if it's another type of
*            element, its innerHTML will be set to the resultText.
*
* Example:
*    sajax_do_call('doFoo', [1, 2, 3], document.getElementById("showFoo"));
*
* This will call the doFoo function via MediaWiki's AjaxDispatcher, with
* (1, 2, 3) as the parameter list, and will show the result in the element
* with id = showFoo
*/
function sajax_do_call(func_name, args, target) {
	var i, x, n;
	var uri;
	var post_data;
	uri = wgServer +
		((wgScript == null) ? (wgScriptPath + "/index.php") : wgScript) +
		"?action=ajax";
	if (sajax_request_type == "GET") {
		if (uri.indexOf("?") == -1)
			uri = uri + "?rs=" + encodeURIComponent(func_name);
		else
			uri = uri + "&rs=" + encodeURIComponent(func_name);
		for (i = 0; i < args.length; i++)
			uri = uri + "&rsargs[]=" + encodeURIComponent(args[i]);
		//uri = uri + "&rsrnd=" + new Date().getTime();
		post_data = null;
	} else {
		post_data = "rs=" + encodeURIComponent(func_name);
		for (i = 0; i < args.length; i++)
			post_data = post_data + "&rsargs[]=" + encodeURIComponent(args[i]);
	}
	x = sajax_init_object();
	if (!x) {
		alert("AJAX not supported");
		return false;
	}

	try {
		x.open(sajax_request_type, uri, true);
	} catch (e) {
		if (window.location.hostname == "localhost") {
			alert("Your browser blocks XMLHttpRequest to 'localhost', try using a real hostname for development/testing.");
		}
		throw e;
	}
	if (sajax_request_type == "POST") {
		x.setRequestHeader("Method", "POST " + uri + " HTTP/1.1");
		x.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	}
	x.setRequestHeader("Pragma", "cache=yes");
	x.setRequestHeader("Cache-Control", "no-transform");
	x.onreadystatechange = function() {
		if (x.readyState != 4)
			return;

		//if (x.status != 200)
		//	alert("Error: " + x.status + " " + x.statusText + ": " + x.responseText);
		//else

		if ( typeof( target ) == 'function' ) {
			target( x );
		}
		else if ( typeof( target ) == 'object' ) {
			if ( target.tagName == 'INPUT' ) {
				if (x.status == 200) target.value= x.responseText;
				//else alert("Error: " + x.status + " " + x.statusText + " (" + x.responseText + ")");
			}
			else {
				if (x.status == 200) target.innerHTML = x.responseText;
				else target.innerHTML= "<div class='error'>Error: " + x.status + " " + x.statusText + " (" + x.responseText + ")</div>";
			}
		}
		else {
			alert("bad target for sajax_do_call: not a function or object: " + target);
		}

		return;
	}

	x.send(post_data);
	delete x;

	return true;
}

/** ==== from FCKeditor.body.php ==== */

var RTE_VISIBLE = 1;
var RTE_TOGGLE_LINK = 2;
var RTE_POPUP = 4;
var showFCKEditor = RTE_VISIBLE;
var popup = false;		//pointer to popup document
var firstLoad = true;
var editorMsgOn = "Rich Editor";
var editorMsgOff = "Disable rich editor";
var editorLink = "Rich Editor";
var saveSetting = 1;


var oFCKeditor = new FCKeditor( "wpTextbox1" ) ;

//Set config
oFCKeditor.BasePath = "/wiki/js/fckeditor/fckeditor/" ;
oFCKeditor.Config["CustomConfigurationsPath"] = "/wiki/js/fckeditor/fckeditor_config.js" ;oFCKeditor.Config["EditorAreaCSS"] = "/wiki/js/fckeditor/css/fckeditor.css" ;
oFCKeditor.ToolbarSet = "Wiki" ;
oFCKeditor.ready = true;
oFCKeditor.Config["showreferences"] = true;
oFCKeditor.Config["showsource"] = true;

//IE hack to call func from popup
function FCK_sajax(func_name, args, target) {
	sajax_request_type = 'POST' ;
	sajax_do_call(func_name, args, function (x) {
		// I know this is function, not object
		target(x);
		}
	);
}

function onLoadFCKeditor()
{
	if (!(showFCKEditor & RTE_VISIBLE))
		showFCKEditor += RTE_VISIBLE;
	firstLoad = false;
	realTextarea = document.getElementById('wpTextbox1');
	if ( realTextarea )
	{
		var height = 0 ;
		realTextarea.style.display = 'none';
		if ( height == 0 )
		{
			// Get the window (inner) size.
			var height = window.innerHeight || ( document.documentElement && document.documentElement.clientHeight ) || 550 ;

			// Reduce the height to the offset of the toolbar.
			var offset = document.getElementById('wikiPreview') || document.getElementById('toolbar') ;
			while ( offset )
			{
				height -= offset.offsetTop ;
				offset = offset.offsetParent ;
			}

			// Add a small space to be left in the bottom.
			height -= 20 ;
		}

		// Enforce a minimum height.
		height = ( !height || height < 300 ) ? 300 : height ;

		// Create the editor instance and replace the textarea.
		oFCKeditor.Height = height ;
		oFCKeditor.ReplaceTextarea() ;

		// Hide the default toolbar.
		document.getElementById('toolbar').style.display = 'none' ;
		// do things with CharInsert for example

		var edittools_markup = document.getElementById ('editpage-specialchars') ;
		if (edittools_markup) {
			edittools_markup.style.display = 'none' ;
		}
		FCKeditorInsertTags = function (tagOpen, tagClose, sampleText, oDoc)
		{
			var txtarea;

			if ( !(typeof(oDoc.FCK) == "undefined") && !(typeof(oDoc.FCK.EditingArea) == "undefined") )
			{
				txtarea = oDoc.FCK.EditingArea.Textarea ;
			}
			else if (oDoc.editform)
			{
				// if we have FCK enabled, behave differently...
				if ( showFCKEditor & RTE_VISIBLE )
				{
					SRCiframe = oDoc.getElementById ('wpTextbox1___Frame') ;
					if ( SRCiframe )
					{
						if (window.frames[SRCiframe])
							SRCdoc = window.frames[SRCiframe].oDoc ;
						else
							SRCdoc = SRCiframe.contentDocument ;

						var SRCarea = SRCdoc.getElementById ('xEditingArea').firstChild ;

						if (SRCarea)
							txtarea = SRCarea ;
						else
							return false ;

					}
					else
					{
						return false ;
					}
				}
				else
				{
					txtarea = oDoc.editform.wpTextbox1 ;
				}
			}
			else
			{
				// some alternate form? take the first one we can find
				var areas = oDoc.getElementsByTagName( 'textarea' ) ;
				txtarea = areas[0] ;
			}

			var selText, isSample = false ;

			if ( oDoc.selection  && oDoc.selection.createRange )
			{ // IE/Opera

				//save window scroll position
				if ( oDoc.documentElement && oDoc.documentElement.scrollTop )
					var winScroll = oDoc.documentElement.scrollTop ;
				else if ( oDoc.body )
					var winScroll = oDoc.body.scrollTop ;

				//get current selection
				txtarea.focus() ;
				var range = oDoc.selection.createRange() ;
				selText = range.text ;
				//insert tags
				checkSelected();
				range.text = tagOpen + selText + tagClose ;
				//mark sample text as selected
				if ( isSample && range.moveStart )
				{
					if (window.opera)
						tagClose = tagClose.replace(/\n/g,'') ; //check it out one more time
					range.moveStart('character', - tagClose.length - selText.length) ;
					range.moveEnd('character', - tagClose.length) ;
				}
				range.select();
				//restore window scroll position
				if ( oDoc.documentElement && oDoc.documentElement.scrollTop )
					oDoc.documentElement.scrollTop = winScroll ;
				else if ( oDoc.body )
					oDoc.body.scrollTop = winScroll ;

			}
			else if ( txtarea.selectionStart || txtarea.selectionStart == '0' )
			{ // Mozilla

				//save textarea scroll position
				var textScroll = txtarea.scrollTop ;
				//get current selection
				txtarea.focus() ;
				var startPos = txtarea.selectionStart ;
				var endPos = txtarea.selectionEnd ;
				selText = txtarea.value.substring( startPos, endPos ) ;

				//insert tags
				if (!selText)
				{
					selText = sampleText ;
					isSample = true ;
				}
				else if (selText.charAt(selText.length - 1) == ' ')
				{ //exclude ending space char
					selText = selText.substring(0, selText.length - 1) ;
					tagClose += ' ' ;
				}
				txtarea.value = txtarea.value.substring(0, startPos) + tagOpen + selText + tagClose +
								txtarea.value.substring(endPos, txtarea.value.length) ;
				//set new selection
				if (isSample)
				{
					txtarea.selectionStart = startPos + tagOpen.length ;
					txtarea.selectionEnd = startPos + tagOpen.length + selText.length ;
				}
				else
				{
					txtarea.selectionStart = startPos + tagOpen.length + selText.length + tagClose.length ;
					txtarea.selectionEnd = txtarea.selectionStart;
				}
				//restore textarea scroll position
				txtarea.scrollTop = textScroll;
			}
		}
	}
}
function checkSelected()
{
	if (!selText) {
		selText = sampleText;
		isSample = true;
	} else if (selText.charAt(selText.length - 1) == ' ') { //exclude ending space char
		selText = selText.substring(0, selText.length - 1);
		tagClose += ' '
	}
}
function initEditor()
{
	var toolbar = document.getElementById('toolbar') ;
	//show popup or toogle link
	if (showFCKEditor & (RTE_POPUP|RTE_TOGGLE_LINK)){
		//add new toolbar before wiki toolbar
		var fckTools = document.createElement('div');
		fckTools.setAttribute('id', 'fckTools');
		toolbar.parentNode.insertBefore( fckTools, toolbar );

		var SRCtextarea = document.getElementById( "wpTextbox1" ) ;
		if (showFCKEditor & RTE_VISIBLE) SRCtextarea.style.display = "none";
	}
	if (showFCKEditor & RTE_TOGGLE_LINK)
	{
		fckTools.innerHTML='[<a class="fckToogle" id="toggle_wpTextbox1" href="javascript:void(0)" onclick="ToggleFCKEditor(\'toggle\',\'wpTextbox1\')">'+ editorLink +'</a>] ';
	}
	if (showFCKEditor & RTE_POPUP)
	{
		var style = (showFCKEditor & RTE_VISIBLE) ? 'style="display:none"' : "";
		fckTools.innerHTML+='<span ' + style + ' id="popup_wpTextbox1">[<a class="fckPopup" href="javascript:void(0)" onclick="ToggleFCKEditor(\'popup\',\'wpTextbox1\')">Open Rich editor in new window</a>]</span>';
	}

	if (showFCKEditor & RTE_VISIBLE)
	{
		onLoadFCKeditor();
	}
	return true;
}
addOnloadHook( initEditor );

function ToggleFCKEditor(mode, objId)
{
	var SRCtextarea = document.getElementById( objId ) ;
	if(mode == 'popup'){
		if (( showFCKEditor & RTE_VISIBLE) && ( FCKeditorAPI ))	//if FCKeditor is up-to-date
		{
			var oEditorIns = FCKeditorAPI.GetInstance( objId );
			var text = oEditorIns.GetData( oEditorIns.Config.FormatSource );
			SRCtextarea.value = text;			//copy text to textarea
		}
		FCKeditor_OpenPopup('oFCKeditor',objId);
		return true;
	}

	var oToggleLink = document.getElementById('toggle_'+ objId );
	var oPopupLink = document.getElementById('popup_'+ objId );

	if ( firstLoad )
	{
		// firstLoad = true => FCKeditor start invisible
		if (oToggleLink) oToggleLink.innerHTML = "Loading...";
		sajax_request_type = 'POST' ;
		oFCKeditor.ready = false;
		/* ==== TODO ==== */
//		sajax_do_call('wfSajaxWikiToHTML', [SRCtextarea.value], function ( result ){
//			if ( firstLoad )	//still
//			{
//				SRCtextarea.value = result.responseText; //insert parsed text
				SRCtextarea.value = document.getElementById("wysiwyg_html").innerHTML; //insert parsed text
				onLoadFCKeditor();
				if (oToggleLink) oToggleLink.innerHTML = editorMsgOff;
				oFCKeditor.ready = true;
//			}
//		});
		return true;
	}

	if (!oFCKeditor.ready) return false;		//sajax_do_call in action
	if (!FCKeditorAPI) return false;			//not loaded yet
	var oEditorIns = FCKeditorAPI.GetInstance( objId );
	var oEditorIframe  = document.getElementById( objId+'___Frame' );
	var FCKtoolbar = document.getElementById('toolbar');
	var bIsWysiwyg = ( oEditorIns.EditMode == FCK_EDITMODE_WYSIWYG );

	//FCKeditor visible -> hidden
	if ( showFCKEditor & RTE_VISIBLE)
	{
		var text = oEditorIns.GetData( oEditorIns.Config.FormatSource );
		SRCtextarea.value = text;
		if ( bIsWysiwyg ) oEditorIns.SwitchEditMode();		//switch to plain
		var text = oEditorIns.GetData( oEditorIns.Config.FormatSource );
		//copy from FCKeditor to textarea
		SRCtextarea.value = text;
		if (saveSetting)
		{
			sajax_request_type = 'GET' ;
			sajax_do_call( 'wfSajaxToggleFCKeditor', ['hide'], function(){} ) ;		//remember closing in session
		}
		if (oToggleLink) oToggleLink.innerHTML = editorMsgOn;
		if (oPopupLink) oPopupLink.style.display = '';
		showFCKEditor -= RTE_VISIBLE;
		oEditorIframe.style.display = 'none';
		FCKtoolbar.style.display = '';
		SRCtextarea.style.display = '';
	}
	//FCKeditor hidden -> visible
	else
	{
		if ( bIsWysiwyg ) oEditorIns.SwitchEditMode();		//switch to plain
		SRCtextarea.style.display = 'none';
		//copy from textarea to FCKeditor
		oEditorIns.EditingArea.Textarea.value = SRCtextarea.value
		FCKtoolbar.style.display = 'none';
		oEditorIframe.style.display = '';
		if ( !bIsWysiwyg ) oEditorIns.SwitchEditMode();		//switch to WYSIWYG
		showFCKEditor += RTE_VISIBLE; // showFCKEditor+=RTE_VISIBLE
		if (oToggleLink) oToggleLink.innerHTML = editorMsgOff;
		if (oPopupLink) oPopupLink.style.display = 'none';
	}
	return true;
}

function FCKeditor_OpenPopup(jsID, textareaID)
{
	popupUrl = 'FCKeditor/FCKeditor.popup.html';
	popupUrl = popupUrl + '?var='+ jsID + '&el=' + textareaID;
	window.open(popupUrl, null, 'toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=1,dependent=yes');
	return 0;
}

function JAMWiki_loadEditor() {
	if (typeof FCKeditorAPI == "undefined") {
		// not loaded yet, try again in a bit
		setTimeout(JAMWiki_loadEditor, 200);
	} else {
		console.log("Loading textarea");
		var SRCtextarea = document.getElementById( 'wpTextbox1' ) ;
		SRCtextarea.value = document.getElementById("wysiwyg_html").innerHTML; //insert parsed text
		onLoadFCKeditor();
	}
}
addOnloadHook( JAMWiki_loadEditor );
