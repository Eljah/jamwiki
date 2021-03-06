<%--

  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, version 2.1, dated February 1999.

  This program is free software; you can redistribute it and/or modify
  it under the terms of the latest version of the GNU Lesser General
  Public License as published by the Free Software Foundation;

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this program (LICENSE.txt); if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

--%>
<%@ page import="
        org.jamwiki.Environment,
        org.jamwiki.WikiBase
    "
    errorPage="/WEB-INF/jsp/error.jsp"
    contentType="text/html; charset=utf-8"
%>

<%@ include file="page-init.jsp" %>

<script type="text/javascript">
function onPersistenceType() {
	var disabled = true;
	if (document.getElementById("<%= Environment.PROP_BASE_PERSISTENCE_TYPE %>").options[document.getElementById("<%= Environment.PROP_BASE_PERSISTENCE_TYPE %>").selectedIndex].value == "<%= WikiBase.PERSISTENCE_EXTERNAL %>") {
		disabled = false;
	}
	document.getElementById("<%= Environment.PROP_DB_DRIVER %>").disabled = disabled;
	document.getElementById("<%= Environment.PROP_DB_TYPE %>").disabled = disabled;
	document.getElementById("<%= Environment.PROP_DB_URL %>").disabled = disabled;
	document.getElementById("<%= Environment.PROP_DB_USERNAME %>").disabled = disabled;
	document.getElementById("<%= Environment.PROP_DB_PASSWORD %>").disabled = disabled;
}
function onUploadType() {
	var whitelistDisabled = true;
	var blacklistDisabled = true;
	if (document.getElementById("<%= Environment.PROP_FILE_BLACKLIST_TYPE %>").options[document.getElementById("<%= Environment.PROP_FILE_BLACKLIST_TYPE %>").selectedIndex].value == "<%= WikiBase.UPLOAD_BLACKLIST %>") {
		blacklistDisabled = false;
	} else if (document.getElementById("<%= Environment.PROP_FILE_BLACKLIST_TYPE %>").options[document.getElementById("<%= Environment.PROP_FILE_BLACKLIST_TYPE %>").selectedIndex].value == "<%= WikiBase.UPLOAD_WHITELIST %>") {
		whitelistDisabled = false;
	}
	document.getElementById("<%= Environment.PROP_FILE_BLACKLIST %>").disabled = blacklistDisabled;
	document.getElementById("<%= Environment.PROP_FILE_WHITELIST %>").disabled = whitelistDisabled;
}
function onLdap() {
	var disabled = true;
	if (document.getElementById("<%= Environment.PROP_BASE_USER_HANDLER %>").options[document.getElementById("<%= Environment.PROP_BASE_USER_HANDLER %>").selectedIndex].value == "<%= WikiBase.USER_HANDLER_LDAP %>") {
		disabled = false;
	}
	document.getElementById("<%= Environment.PROP_LDAP_FACTORY_CLASS %>").disabled = disabled;
	document.getElementById("<%= Environment.PROP_LDAP_URL %>").disabled = disabled;
	document.getElementById("<%= Environment.PROP_LDAP_CONTEXT %>").disabled = disabled;
	document.getElementById("<%= Environment.PROP_LDAP_FIELD_EMAIL %>").disabled = disabled;
	document.getElementById("<%= Environment.PROP_LDAP_FIELD_FIRST_NAME %>").disabled = disabled;
	document.getElementById("<%= Environment.PROP_LDAP_FIELD_LAST_NAME %>").disabled = disabled;
	document.getElementById("<%= Environment.PROP_LDAP_FIELD_USERID %>").disabled = disabled;
	document.getElementById("<%= Environment.PROP_LDAP_LOGIN %>").disabled = disabled;
	document.getElementById("<%= Environment.PROP_LDAP_PASSWORD %>").disabled = disabled;
	document.getElementById("<%= Environment.PROP_LDAP_SECURITY_AUTHENTICATION %>").disabled = disabled;
}
function onRSS() {
	var disabled = true;
	if (document.getElementById("<%= Environment.PROP_RSS_ALLOWED %>").checked) {
		disabled = false;
	}
	document.getElementById("<%= Environment.PROP_RSS_TITLE %>").disabled = disabled;
}
</script>

<form name="form1" method="post" action="<jamwiki:link value="Special:Admin" />">

<div class="submenu">
<a href="#general"><f:message key="admin.header.general" /></a> | <a href="#parser"><f:message key="admin.header.parser" /></a> | <a href="#database"><f:message key="admin.header.persistence" /></a> | <a href="#upload"><f:message key="admin.header.upload" /></a><br />
<a href="#authentication"><f:message key="admin.header.ldap" /></a> | <a href="#cache"><f:message key="admin.header.cache" /></a> | <a href="#rss"><f:message key="admin.header.rss" /></a> | <a href="#save"><f:message key="admin.action.save" /></a>
</div>

<c:if test="${!empty message}">
<div class="message red"><f:message key="${message.key}"><f:param value="${message.params[0]}" /></f:message></div>
</c:if>
<c:if test="${!empty errors}">
<div class="message red"><c:forEach items="${errors}" var="message"><f:message key="${message.key}"><f:param value="${message.params[0]}" /></f:message><br /></c:forEach></div>
</c:if>

<!-- BEGIN GENERAL SETTINGS -->
<a name="general"></a>
<fieldset>
<legend><f:message key="admin.header.general" /></legend>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="general" />">
	<span class="formcaption"><label for="<%= Environment.PROP_BASE_DEFAULT_TOPIC %>"><f:message key="admin.caption.defaulttopic" /></label></span>
	<c:set var="PROP_BASE_DEFAULT_TOPIC"><%= Environment.PROP_BASE_DEFAULT_TOPIC %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_BASE_DEFAULT_TOPIC}" value="${props[PROP_BASE_DEFAULT_TOPIC]}" size="30" id="${PROP_BASE_DEFAULT_TOPIC}" /></span>
	<div class="formhelp"><f:message key="admin.help.defaulttopic" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="general" />">
	<span class="formcaption"><label for="<%= Environment.PROP_BASE_LOGO_IMAGE %>"><f:message key="admin.caption.logoimage" /></label></span>
	<c:set var="PROP_BASE_LOGO_IMAGE"><%= Environment.PROP_BASE_LOGO_IMAGE %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_BASE_LOGO_IMAGE}" value="${props[PROP_BASE_LOGO_IMAGE]}" size="30" id="${PROP_BASE_LOGO_IMAGE}" /></span>
	<div class="formhelp"><f:message key="admin.help.logoimage" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="general" />">
	<span class="formcaption"><label for="<%= Environment.PROP_IMAGE_RESIZE_INCREMENT %>"><f:message key="admin.caption.imageresize" /></label></span>
	<c:set var="PROP_IMAGE_RESIZE_INCREMENT"><%= Environment.PROP_IMAGE_RESIZE_INCREMENT %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_IMAGE_RESIZE_INCREMENT}" size="5" maxlength="4" value="${props[PROP_IMAGE_RESIZE_INCREMENT]}" id="${PROP_IMAGE_RESIZE_INCREMENT}" /></span>
	<div class="formhelp"><f:message key="admin.help.imageresize" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="general" />">
	<span class="formcaption"><label for="<%= Environment.PROP_RECENT_CHANGES_NUM %>"><f:message key="admin.caption.recentchangesdefault" /></label></span>
	<c:set var="PROP_RECENT_CHANGES_NUM"><%= Environment.PROP_RECENT_CHANGES_NUM %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_RECENT_CHANGES_NUM}" size="5" maxlength="4" value="${props[PROP_RECENT_CHANGES_NUM]}" id="${PROP_RECENT_CHANGES_NUM}" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="general" />">
	<span class="formcaption"><label for="<%= Environment.PROP_TOPIC_SPAM_FILTER %>"><f:message key="admin.caption.usespamfilter" /></label></span>
	<c:set var="PROP_TOPIC_SPAM_FILTER"><%= Environment.PROP_TOPIC_SPAM_FILTER %></c:set>
	<span class="formelement"><jamwiki:checkbox name="${PROP_TOPIC_SPAM_FILTER}" value="true" checked="${props[PROP_TOPIC_SPAM_FILTER]}" id="${PROP_TOPIC_SPAM_FILTER}" /></span>
	<div class="formhelp"><f:message key="admin.help.usespamfilter" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="general" />">
	<span class="formcaption"><label for="<%= Environment.PROP_TOPIC_USE_PREVIEW %>"><f:message key="admin.caption.usepreview" /></label></span>
	<c:set var="PROP_TOPIC_USE_PREVIEW"><%= Environment.PROP_TOPIC_USE_PREVIEW %></c:set>
	<span class="formelement"><jamwiki:checkbox name="${PROP_TOPIC_USE_PREVIEW}" value="true" checked="${props[PROP_TOPIC_USE_PREVIEW]}" id="${PROP_TOPIC_USE_PREVIEW}" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="general" />">
	<span class="formcaption"><label for="<%= Environment.PROP_TOPIC_WYSIWYG %>"><f:message key="admin.caption.wysiwyg" /></label></span>
	<c:set var="PROP_TOPIC_WYSIWYG"><%= Environment.PROP_TOPIC_WYSIWYG %></c:set>
	<span class="formelement"><jamwiki:checkbox name="${PROP_TOPIC_WYSIWYG}" value="true" checked="${props[PROP_TOPIC_WYSIWYG]}" id="${PROP_TOPIC_WYSIWYG}" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="general" />">
	<span class="formcaption"><label for="<%= Environment.PROP_PRINT_NEW_WINDOW %>"><f:message key="admin.caption.printnewwindow" /></label></span>
	<c:set var="PROP_PRINT_NEW_WINDOW"><%= Environment.PROP_PRINT_NEW_WINDOW %></c:set>
	<span class="formelement"><jamwiki:checkbox name="${PROP_PRINT_NEW_WINDOW}" value="true" checked="${props[PROP_PRINT_NEW_WINDOW]}" id="${PROP_PRINT_NEW_WINDOW}" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="general" />">
	<span class="formcaption"><label for="<%= Environment.PROP_EXTERNAL_LINK_NEW_WINDOW %>"><f:message key="admin.caption.externallinknewwindow" /></label></span>
	<c:set var="PROP_EXTERNAL_LINK_NEW_WINDOW"><%= Environment.PROP_EXTERNAL_LINK_NEW_WINDOW %></c:set>
	<span class="formelement"><jamwiki:checkbox name="${PROP_EXTERNAL_LINK_NEW_WINDOW}" value="true" checked="${props[PROP_EXTERNAL_LINK_NEW_WINDOW]}" id="${PROP_EXTERNAL_LINK_NEW_WINDOW}" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="general" />">
	<span class="formcaption"><label for="<%= Environment.PROP_BASE_META_DESCRIPTION %>"><f:message key="admin.caption.metadescription" /></label></span>
	<c:set var="PROP_BASE_META_DESCRIPTION"><%= Environment.PROP_BASE_META_DESCRIPTION %></c:set>
	<span class="formelement"><textarea class="medium" name="<%= Environment.PROP_BASE_META_DESCRIPTION %>" id="<%= Environment.PROP_BASE_META_DESCRIPTION %>"><c:out value="${props[PROP_BASE_META_DESCRIPTION]}" /></textarea></span>
	<div class="formhelp"><f:message key="admin.help.metadescription" /></div>
</div>
</fieldset>
<!-- END GENERAL SETTINGS -->

<!-- BEGIN PARSER -->
<a name="parser"></a>
<fieldset>
<legend><f:message key="admin.header.parser" /></legend>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="parser" />">
	<span class="formcaption"><label for="<%= Environment.PROP_PARSER_CLASS %>"><f:message key="admin.parser.caption" /></label></span>
	<span class="formelement">
		<select name="<%= Environment.PROP_PARSER_CLASS %>" id="<%= Environment.PROP_PARSER_CLASS %>">
		<c:set var="PROP_PARSER_CLASS"><%= Environment.PROP_PARSER_CLASS %></c:set>
		<c:forEach items="${parsers}" var="parser">
		<option value="<c:out value="${parser.clazz}" />"<c:if test="${props[PROP_PARSER_CLASS] == parser.clazz}"> selected="selected"</c:if>><c:if test="${!empty parser.key}"><f:message key="${parser.key}" /></c:if><c:if test="${empty parser.key}"><c:out value="${parser.name}" /></c:if></option>
		</c:forEach>
		</select>
	</span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="parser" />">
	<span class="formcaption"><label for="<%= Environment.PROP_PARSER_TOC %>"><f:message key="admin.parser.caption.tableofcontents" /></label></span>
	<c:set var="PROP_PARSER_TOC"><%= Environment.PROP_PARSER_TOC %></c:set>
	<span class="formelement"><jamwiki:checkbox name="${PROP_PARSER_TOC}" value="true" checked="${props[PROP_PARSER_TOC]}" id="${PROP_PARSER_TOC}" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="parser" />">
	<span class="formcaption"><label for="<%= Environment.PROP_PARSER_TOC_DEPTH %>"><f:message key="admin.parser.caption.tableofcontentsdepth" /></label></span>
	<c:set var="PROP_PARSER_TOC_DEPTH"><%= Environment.PROP_PARSER_TOC_DEPTH %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_PARSER_TOC_DEPTH}" value="${props[PROP_PARSER_TOC_DEPTH]}" size="5" maxlength="1" id="${PROP_PARSER_TOC_DEPTH}" /></span>
	<div class="formhelp"><f:message key="admin.parser.help.tableofcontentsdepth" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="parser" />">
	<span class="formcaption"><label for="<%= Environment.PROP_PARSER_ALLOW_HTML %>"><f:message key="admin.parser.caption.allowhtml" /></label></span>
	<c:set var="PROP_PARSER_ALLOW_HTML"><%= Environment.PROP_PARSER_ALLOW_HTML %></c:set>
	<span class="formelement"><jamwiki:checkbox name="${PROP_PARSER_ALLOW_HTML}" value="true" checked="${props[PROP_PARSER_ALLOW_HTML]}" id="${PROP_PARSER_ALLOW_HTML}" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="parser" />">
	<span class="formcaption"><label for="<%= Environment.PROP_PARSER_ALLOW_JAVASCRIPT %>"><f:message key="admin.parser.caption.allowjavascript" /></label></span>
	<c:set var="PROP_PARSER_ALLOW_JAVASCRIPT"><%= Environment.PROP_PARSER_ALLOW_JAVASCRIPT %></c:set>
	<span class="formelement"><jamwiki:checkbox name="${PROP_PARSER_ALLOW_JAVASCRIPT}" value="true" checked="${props[PROP_PARSER_ALLOW_JAVASCRIPT]}" id="${PROP_PARSER_ALLOW_JAVASCRIPT}" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="parser" />">
	<span class="formcaption"><label for="<%= Environment.PROP_PARSER_ALLOW_TEMPLATES %>"><f:message key="admin.parser.caption.allowtemplates" /></label></span>
	<c:set var="PROP_PARSER_ALLOW_TEMPLATES"><%= Environment.PROP_PARSER_ALLOW_TEMPLATES %></c:set>
	<span class="formelement"><jamwiki:checkbox name="${PROP_PARSER_ALLOW_TEMPLATES}" value="true" checked="${props[PROP_PARSER_ALLOW_TEMPLATES]}" id="${PROP_PARSER_ALLOW_TEMPLATES}" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="parser" />">
	<span class="formcaption"><label for="<%= Environment.PROP_PARSER_SIGNATURE_USER_PATTERN %>"><f:message key="admin.parser.caption.signatureuser" /></label></span>
	<c:set var="PROP_PARSER_SIGNATURE_USER_PATTERN"><%= Environment.PROP_PARSER_SIGNATURE_USER_PATTERN %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_PARSER_SIGNATURE_USER_PATTERN}" value="${props[PROP_PARSER_SIGNATURE_USER_PATTERN]}" size="50" id="${PROP_PARSER_SIGNATURE_USER_PATTERN}" /></span>
	<div class="formhelp"><f:message key="admin.parser.help.signatureuser" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="parser" />">
	<span class="formcaption"><label for="<%= Environment.PROP_PARSER_SIGNATURE_DATE_PATTERN %>"><f:message key="admin.parser.caption.signaturedate" /></label></span>
	<c:set var="PROP_PARSER_SIGNATURE_DATE_PATTERN"><%= Environment.PROP_PARSER_SIGNATURE_DATE_PATTERN %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_PARSER_SIGNATURE_DATE_PATTERN}" value="${props[PROP_PARSER_SIGNATURE_DATE_PATTERN]}" size="50" id="${PROP_PARSER_SIGNATURE_DATE_PATTERN}" /></span>
	<div class="formhelp"><f:message key="admin.parser.help.signaturedate" /></div>
</div>
</fieldset>
<!-- END PARSER -->

<%--
FIXME - Email not supported right now, comment this out

<!-- BEGIN EMAIL -->
<a name="email"></a>
<fieldset>
<legend><f:message key="admin.smtp.caption" /></legend>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="email" />">
	<span class="formcaption"><label for="<%= Environment.PROP_EMAIL_SMTP_HOST %>"><f:message key="admin.smtp.caption.host" /></label></span>
	<c:set var="PROP_EMAIL_SMTP_HOST"><%= Environment.PROP_EMAIL_SMTP_HOST %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_EMAIL_SMTP_HOST}" value="${props[PROP_EMAIL_SMTP_HOST]}" size="30" id="${PROP_EMAIL_SMTP_HOST}" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="email" />">
	<span class="formcaption"><label for="<%= Environment.PROP_EMAIL_SMTP_USERNAME %>"><f:message key="admin.smtp.caption.user" /></label></span>
	<c:set var="PROP_EMAIL_SMTP_USERNAME"><%= Environment.PROP_EMAIL_SMTP_USERNAME %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_EMAIL_SMTP_USERNAME}" value="${props[PROP_EMAIL_SMTP_USERNAME]}" size="30" id="${PROP_EMAIL_SMTP_USERNAME}" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="email" />">
	<span class="formcaption"><label for="<%= Environment.PROP_EMAIL_SMTP_PASSWORD %>"><f:message key="admin.smtp.caption.pass" /></label></span>
	<span class="formelement"><input type="password" name="<%= Environment.PROP_EMAIL_SMTP_PASSWORD %>" value="<c:out value="${smtpPassword}" />" size="30" id="<%= Environment.PROP_EMAIL_SMTP_PASSWORD %>" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="email" />">
	<span class="formcaption"><label for="<%= Environment.PROP_EMAIL_REPLY_ADDRESS %>"><f:message key="admin.smtp.caption.reply" /></label></span>
	<c:set var="PROP_EMAIL_REPLY_ADDRESS"><%= Environment.PROP_EMAIL_REPLY_ADDRESS %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_EMAIL_REPLY_ADDRESS}" value="${props[PROP_EMAIL_REPLY_ADDRESS]}" size="50" id="${PROP_EMAIL_REPLY_ADDRESS}" /></span>
</div>
</fieldset>
<!-- END EMAIL -->

--%>

<!-- BEGIN DATABASE PERSISTENCE -->
<a name="database"></a>
<fieldset>
<legend><f:message key="admin.header.persistence" /></legend>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="database" />">
	<span class="formcaption"><label for="<%= Environment.PROP_BASE_FILE_DIR %>"><f:message key="admin.caption.filedir" /></label></span>
	<c:set var="PROP_BASE_FILE_DIR"><%= Environment.PROP_BASE_FILE_DIR %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_BASE_FILE_DIR}" value="${props[PROP_BASE_FILE_DIR]}" size="50" id="${PROP_BASE_FILE_DIR}" /></span>
	<div class="formhelp"><f:message key="admin.help.filedir" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="database" />">
	<span class="formcaption"><label for="<%= Environment.PROP_BASE_PERSISTENCE_TYPE %>"><f:message key="admin.persistence.caption" /></label></span>
	<span class="formelement">
		<select name="<%= Environment.PROP_BASE_PERSISTENCE_TYPE %>" id="<%= Environment.PROP_BASE_PERSISTENCE_TYPE %>" onchange="onPersistenceType()">
		<c:set var="PROP_BASE_PERSISTENCE_TYPE"><%= Environment.PROP_BASE_PERSISTENCE_TYPE %></c:set>
		<c:set var="persistenceTypeInternal"><%= WikiBase.PERSISTENCE_INTERNAL %></c:set>
		<c:set var="persistenceTypeExternal"><%= WikiBase.PERSISTENCE_EXTERNAL %></c:set>
		<option value="<%= WikiBase.PERSISTENCE_INTERNAL %>"<c:if test="${props[PROP_BASE_PERSISTENCE_TYPE] == persistenceTypeInternal}"> selected="selected"</c:if>><f:message key="admin.persistencetype.internal" /></option>
		<option value="<%= WikiBase.PERSISTENCE_EXTERNAL %>"<c:if test="${props[PROP_BASE_PERSISTENCE_TYPE] == persistenceTypeExternal}"> selected="selected"</c:if>><f:message key="admin.persistencetype.database" /></option>
		</select>
	</span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="database" />">
	<span class="formcaption"><label for="<%= Environment.PROP_DB_DRIVER %>"><f:message key="admin.persistence.caption.driver" /></label></span>
	<c:set var="PROP_DB_DRIVER"><%= Environment.PROP_DB_DRIVER %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_DB_DRIVER}" id="${PROP_DB_DRIVER}" value="${props[PROP_DB_DRIVER]}" size="50" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="database" />">
	<span class="formcaption"><label for="<%= Environment.PROP_DB_TYPE %>"><f:message key="admin.persistence.caption.type" /></label></span>
	<span class="formelement">
		<select name="<%= Environment.PROP_DB_TYPE %>" id="<%= Environment.PROP_DB_TYPE %>">
		<c:set var="PROP_DB_TYPE"><%= Environment.PROP_DB_TYPE %></c:set>
		<c:forEach items="${dataHandlers}" var="dataHandler">
		<option value="<c:out value="${dataHandler.clazz}" />"<c:if test="${props[PROP_DB_TYPE] == dataHandler.clazz}"> selected="selected"</c:if>><c:if test="${!empty dataHandler.key}"><f:message key="${dataHandler.key}" /></c:if><c:if test="${empty dataHandler.key}"><c:out value="${dataHandler.name}" /></c:if><c:if test="${dataHandler.experimental}"> (<f:message key="common.caption.experimental" />)</c:if></option>
		</c:forEach>
		</select>
	</span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="database" />">
	<span class="formcaption"><label for="<%= Environment.PROP_DB_URL %>"><f:message key="admin.persistence.caption.url" /></label></span>
	<c:set var="PROP_DB_URL"><%= Environment.PROP_DB_URL %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_DB_URL}" id="${PROP_DB_URL}" value="${props[PROP_DB_URL]}" size="50" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="database" />">
	<span class="formcaption"><label for="<%= Environment.PROP_DB_USERNAME %>"><f:message key="admin.persistence.caption.user" /></label></span>
	<c:set var="PROP_DB_USERNAME"><%= Environment.PROP_DB_USERNAME %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_DB_USERNAME}" id="${PROP_DB_USERNAME}" value="${props[PROP_DB_USERNAME]}" size="30" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="database" />">
	<span class="formcaption"><label for="<%= Environment.PROP_DB_PASSWORD %>"><f:message key="admin.persistence.caption.pass" /></label></span>
	<span class="formelement"><input type="password" name="<%= Environment.PROP_DB_PASSWORD %>" id="<%= Environment.PROP_DB_PASSWORD %>" value="<c:out value="${dbPassword}" />" size="30" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="database" />">
	<span class="formcaption"><label for="<%= Environment.PROP_DBCP_MAX_ACTIVE %>"><f:message key="admin.persistence.caption.maxactive" /></label></span>
	<c:set var="PROP_DBCP_MAX_ACTIVE"><%= Environment.PROP_DBCP_MAX_ACTIVE %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_DBCP_MAX_ACTIVE}" id="${PROP_DBCP_MAX_ACTIVE}" value="${props[PROP_DBCP_MAX_ACTIVE]}" size="5" maxlength="3" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="database" />">
	<span class="formcaption"><label for="<%= Environment.PROP_DBCP_MAX_IDLE %>"><f:message key="admin.persistence.caption.maxidle" /></label></span>
	<c:set var="PROP_DBCP_MAX_IDLE"><%= Environment.PROP_DBCP_MAX_IDLE %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_DBCP_MAX_IDLE}" id="${PROP_DBCP_MAX_IDLE}" value="${props[PROP_DBCP_MAX_IDLE]}" size="5" maxlength="3" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="database" />">
	<span class="formcaption"><label for="<%= Environment.PROP_DBCP_TEST_ON_BORROW %>"><f:message key="admin.persistence.caption.testonborrow" /></label></span>
	<c:set var="PROP_DBCP_TEST_ON_BORROW"><%= Environment.PROP_DBCP_TEST_ON_BORROW %></c:set>
	<span class="formelement"><jamwiki:checkbox name="${PROP_DBCP_TEST_ON_BORROW}" value="true" checked="${props[PROP_DBCP_TEST_ON_BORROW]}" id="${PROP_DBCP_TEST_ON_BORROW}" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="database" />">
	<span class="formcaption"><label for="<%= Environment.PROP_DBCP_TEST_ON_RETURN %>"><f:message key="admin.persistence.caption.testonreturn" /></label></span>
	<c:set var="PROP_DBCP_TEST_ON_RETURN"><%= Environment.PROP_DBCP_TEST_ON_RETURN %></c:set>
	<span class="formelement"><jamwiki:checkbox name="${PROP_DBCP_TEST_ON_RETURN}" value="true" checked="${props[PROP_DBCP_TEST_ON_RETURN]}" id="${PROP_DBCP_TEST_ON_RETURN}" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="database" />">
	<span class="formcaption"><label for="<%= Environment.PROP_DBCP_TEST_WHILE_IDLE %>"><f:message key="admin.persistence.caption.testwhileidle" /></label></span>
	<c:set var="PROP_DBCP_TEST_WHILE_IDLE"><%= Environment.PROP_DBCP_TEST_WHILE_IDLE %></c:set>
	<span class="formelement"><jamwiki:checkbox name="${PROP_DBCP_TEST_WHILE_IDLE}" value="true" checked="${props[PROP_DBCP_TEST_WHILE_IDLE]}" id="${PROP_DBCP_TEST_WHILE_IDLE}" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="database" />">
	<span class="formcaption"><label for="<%= Environment.PROP_DBCP_MIN_EVICTABLE_IDLE_TIME %>"><f:message key="admin.persistence.caption.minevictableidletime" /></label></span>
	<c:set var="PROP_DBCP_MIN_EVICTABLE_IDLE_TIME"><%= Environment.PROP_DBCP_MIN_EVICTABLE_IDLE_TIME %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_DBCP_MIN_EVICTABLE_IDLE_TIME}" id="${PROP_DBCP_MIN_EVICTABLE_IDLE_TIME}" value="${props[PROP_DBCP_MIN_EVICTABLE_IDLE_TIME]}" size="5" maxlength="4" /></span>
	<div class="formhelp"><f:message key="admin.persistence.help.minevictableidletime" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="database" />">
	<span class="formcaption"><label for="<%= Environment.PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS %>"><f:message key="admin.persistence.caption.timebetweenevictionruns" /></label></span>
	<c:set var="PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS"><%= Environment.PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS}" id="${PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS}" value="${props[PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS]}" size="5" maxlength="4" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="database" />">
	<span class="formcaption"><label for="<%= Environment.PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN %>"><f:message key="admin.persistence.caption.numtestsperevictionrun" /></label></span>
	<c:set var="PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN"><%= Environment.PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN}" id="${PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN}" value="${props[PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN]}" size="5" maxlength="4" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="database" />">
	<span class="formcaption"><label for="<%= Environment.PROP_DBCP_WHEN_EXHAUSTED_ACTION %>"><f:message key="admin.persistence.caption.whenexhaustedaction" /></label></span>
	<span class="formelement">
		<select name="<%= Environment.PROP_DBCP_WHEN_EXHAUSTED_ACTION %>" id="<%= Environment.PROP_DBCP_WHEN_EXHAUSTED_ACTION %>">
		<c:set var="PROP_DBCP_WHEN_EXHAUSTED_ACTION"><%= Environment.PROP_DBCP_WHEN_EXHAUSTED_ACTION %></c:set>
		<c:forEach items="${poolExhaustedMap}" var="poolExhausted">
		<option value="<c:out value="${poolExhausted.key}" />"<c:if test="${poolExhausted.key == props[PROP_DBCP_WHEN_EXHAUSTED_ACTION]}"> selected="selected"</c:if>><f:message key="${poolExhausted.value}" /></option>
		</c:forEach>
		</select>
	</span>
</div>
</fieldset>
<!-- END DATABASE PERSISTENCE -->

<!-- BEGIN FILE UPLOAD -->
<a name="upload"></a>
<fieldset>
<legend><f:message key="admin.header.upload" /></legend>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="upload" />">
	<span class="formcaption"><label for="<%= Environment.PROP_FILE_MAX_FILE_SIZE %>"><f:message key="admin.upload.caption.maxfilesize" /></label></span>
	<span class="formelement"><input type="text" name="<%= Environment.PROP_FILE_MAX_FILE_SIZE %>" value="<c:out value="${maximumFileSize}" />" size="10" id="<%= Environment.PROP_FILE_MAX_FILE_SIZE %>" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="upload" />">
	<span class="formcaption"><label for="<%= Environment.PROP_FILE_DIR_FULL_PATH %>"><f:message key="admin.upload.caption.uploaddir" /></label></span>
	<c:set var="PROP_FILE_DIR_FULL_PATH"><%= Environment.PROP_FILE_DIR_FULL_PATH %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_FILE_DIR_FULL_PATH}" value="${props[PROP_FILE_DIR_FULL_PATH]}" size="50" id="${PROP_FILE_DIR_FULL_PATH}" /></span>
	<div class="formhelp"><f:message key="admin.upload.help.uploaddir" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="upload" />">
	<span class="formcaption"><label for="<%= Environment.PROP_FILE_DIR_RELATIVE_PATH %>"><f:message key="admin.upload.caption.uploaddirrel" /></label></span>
	<c:set var="PROP_FILE_DIR_RELATIVE_PATH"><%= Environment.PROP_FILE_DIR_RELATIVE_PATH %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_FILE_DIR_RELATIVE_PATH}" value="${props[PROP_FILE_DIR_RELATIVE_PATH]}" size="50" id="${PROP_FILE_DIR_RELATIVE_PATH}" /></span>
	<div class="formhelp"><f:message key="admin.upload.help.uploaddirrel" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="upload" />">
	<span class="formcaption"><label for="<%= Environment.PROP_FILE_BLACKLIST_TYPE %>"><f:message key="admin.upload.caption.blacklisttype" /></label></span>
	<span class="formelement">
		<c:set var="PROP_FILE_BLACKLIST_TYPE"><%= Environment.PROP_FILE_BLACKLIST_TYPE %></c:set>
		<select name="<%= Environment.PROP_FILE_BLACKLIST_TYPE %>" id="<%= Environment.PROP_FILE_BLACKLIST_TYPE %>" onchange="onUploadType()">
		<c:forEach items="${blacklistTypes}" var="blacklistType">
		<option value="<c:out value="${blacklistType.key}" />"<c:if test="${props[PROP_FILE_BLACKLIST_TYPE] == blacklistType.key}"> selected="selected"</c:if>><f:message key="${blacklistType.value}" /></option>
		</c:forEach>
		</select>
	</span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="upload" />">
	<span class="formcaption"><label for="<%= Environment.PROP_FILE_BLACKLIST %>"><f:message key="admin.upload.caption.blacklist" /></label></span>
	<c:set var="PROP_FILE_BLACKLIST"><%= Environment.PROP_FILE_BLACKLIST %></c:set>
	<span class="formelement"><textarea class="medium" name="<%= Environment.PROP_FILE_BLACKLIST %>" id="<%= Environment.PROP_FILE_BLACKLIST %>"><c:out value="${props[PROP_FILE_BLACKLIST]}" /></textarea></span>
	<div class="formhelp"><f:message key="admin.upload.help.blacklist" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="upload" />">
	<span class="formcaption"><label for="<%= Environment.PROP_FILE_WHITELIST %>"><f:message key="admin.upload.caption.whitelist" /></label></span>
	<c:set var="PROP_FILE_WHITELIST"><%= Environment.PROP_FILE_WHITELIST %></c:set>
	<span class="formelement"><textarea class="medium" name="<%= Environment.PROP_FILE_WHITELIST %>" id="<%= Environment.PROP_FILE_WHITELIST %>"><c:out value="${props[PROP_FILE_WHITELIST]}" /></textarea></span>
	<div class="formhelp"><f:message key="admin.upload.help.whitelist" /></div>
</div>
</fieldset>
<!-- END FILE UPLOAD -->

<!-- BEGIN AUTHENTICATION -->
<a name="authentication"></a>
<fieldset>
<legend><f:message key="admin.header.ldap" /></legend>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="ldap" />">
	<span class="formcaption"><label for="<%= Environment.PROP_BASE_USER_HANDLER %>"><f:message key="admin.ldap.caption" /></label></span>
	<span class="formelement">
		<select name="<%= Environment.PROP_BASE_USER_HANDLER %>" id="<%= Environment.PROP_BASE_USER_HANDLER %>" onchange="onLdap()">
		<c:set var="PROP_BASE_USER_HANDLER"><%= Environment.PROP_BASE_USER_HANDLER %></c:set>
		<c:forEach items="${userHandlers}" var="userHandler">
		<option value="<c:out value="${userHandler.clazz}" />"<c:if test="${props[PROP_BASE_USER_HANDLER] == userHandler.clazz}"> selected="selected"</c:if>><c:if test="${!empty userHandler.key}"><f:message key="${userHandler.key}" /></c:if><c:if test="${empty userHandler.key}"><c:out value="${userHandler.name}" /></c:if><c:if test="${userHandler.experimental}"> (<f:message key="common.caption.experimental" />)</c:if></option>
		</c:forEach>
		</select>
	</span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="ldap" />">
	<span class="formcaption"><label for="<%= Environment.PROP_LDAP_FACTORY_CLASS %>"><f:message key="admin.ldap.caption.factory" /></label></span>
	<c:set var="PROP_LDAP_FACTORY_CLASS"><%= Environment.PROP_LDAP_FACTORY_CLASS %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_LDAP_FACTORY_CLASS}" id="${PROP_LDAP_FACTORY_CLASS}" value="${props[PROP_LDAP_FACTORY_CLASS]}" size="50" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="ldap" />">
	<span class="formcaption"><label for="<%= Environment.PROP_LDAP_URL %>"><f:message key="admin.ldap.caption.url" /></label></span>
	<c:set var="PROP_LDAP_URL"><%= Environment.PROP_LDAP_URL %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_LDAP_URL}" id="${PROP_LDAP_URL}" value="${props[PROP_LDAP_URL]}" size="50" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="ldap" />">
	<span class="formcaption"><label for="<%= Environment.PROP_LDAP_CONTEXT %>"><f:message key="admin.ldap.caption.context" /></label></span>
	<c:set var="PROP_LDAP_CONTEXT"><%= Environment.PROP_LDAP_CONTEXT %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_LDAP_CONTEXT}" id="${PROP_LDAP_CONTEXT}" value="${props[PROP_LDAP_CONTEXT]}" size="50" /></span>
	<div class="formhelp"><f:message key="admin.ldap.help.context" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="ldap" />">
	<span class="formcaption"><label for="<%= Environment.PROP_LDAP_SECURITY_AUTHENTICATION %>"><f:message key="admin.ldap.caption.security" /></label></span>
	<c:set var="PROP_LDAP_SECURITY_AUTHENTICATION"><%= Environment.PROP_LDAP_SECURITY_AUTHENTICATION %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_LDAP_SECURITY_AUTHENTICATION}" id="${PROP_LDAP_SECURITY_AUTHENTICATION}" value="${props[PROP_LDAP_SECURITY_AUTHENTICATION]}" size="20" /></span>
	<div class="formhelp"><f:message key="admin.ldap.help.security" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="ldap" />">
	<span class="formcaption"><label for="<%= Environment.PROP_LDAP_LOGIN %>"><f:message key="admin.ldap.caption.login" /></label></span>
	<c:set var="PROP_LDAP_LOGIN"><%= Environment.PROP_LDAP_LOGIN %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_LDAP_LOGIN}" id="${PROP_LDAP_LOGIN}" value="${props[PROP_LDAP_LOGIN]}" size="30" /></span>
	<div class="formhelp"><f:message key="admin.ldap.help.login" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="ldap" />">
	<span class="formcaption"><label for="<%= Environment.PROP_LDAP_PASSWORD %>"><f:message key="admin.ldap.caption.password" /></label></span>
	<span class="formelement"><input type="password" name="<%= Environment.PROP_LDAP_PASSWORD %>" id="<%= Environment.PROP_LDAP_PASSWORD %>" value="<c:out value="${ldapPassword}" />" size="30" /></span>
	<div class="formhelp"><f:message key="admin.ldap.help.password" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="ldap" />">
	<span class="formcaption"><label for="<%= Environment.PROP_LDAP_FIELD_USERID %>"><f:message key="admin.ldap.caption.field.userid" /></label></span>
	<c:set var="PROP_LDAP_FIELD_USERID"><%= Environment.PROP_LDAP_FIELD_USERID %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_LDAP_FIELD_USERID}" id="${PROP_LDAP_FIELD_USERID}" value="${props[PROP_LDAP_FIELD_USERID]}" size="20" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="ldap" />">
	<span class="formcaption"><label for="<%= Environment.PROP_LDAP_FIELD_FIRST_NAME %>"><f:message key="admin.ldap.caption.field.firstname" /></label></span>
	<c:set var="PROP_LDAP_FIELD_FIRST_NAME"><%= Environment.PROP_LDAP_FIELD_FIRST_NAME %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_LDAP_FIELD_FIRST_NAME}" id="${PROP_LDAP_FIELD_FIRST_NAME}" value="${props[PROP_LDAP_FIELD_FIRST_NAME]}" size="20" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="ldap" />">
	<span class="formcaption"><label for="<%= Environment.PROP_LDAP_FIELD_FIRST_NAME %>"><f:message key="admin.ldap.caption.field.lastname" /></label></span>
	<c:set var="PROP_LDAP_FIELD_LAST_NAME"><%= Environment.PROP_LDAP_FIELD_LAST_NAME %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_LDAP_FIELD_LAST_NAME}" id="${PROP_LDAP_FIELD_LAST_NAME}" value="${props[PROP_LDAP_FIELD_LAST_NAME]}" size="20" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="ldap" />">
	<span class="formcaption"><label for="<%= Environment.PROP_LDAP_FIELD_EMAIL %>"><f:message key="admin.ldap.caption.field.email" /></label></span>
	<c:set var="PROP_LDAP_FIELD_EMAIL"><%= Environment.PROP_LDAP_FIELD_EMAIL %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_LDAP_FIELD_EMAIL}" id="${PROP_LDAP_FIELD_EMAIL}" value="${props[PROP_LDAP_FIELD_EMAIL]}" size="20" /></span>
</div>
<script type="text/javascript">
onPersistenceType()
onUploadType()
onLdap()
</script>
</fieldset>
<!-- END AUTHENTICATION -->

<!-- BEGIN CACHE -->
<a name="cache"></a>
<fieldset>
<legend><f:message key="admin.header.cache" /></legend>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="cache" />">
	<span class="formcaption"><label for="<%= Environment.PROP_CACHE_TOTAL_SIZE %>"><f:message key="admin.cache.caption.totalsize" /></label></span>
	<c:set var="PROP_CACHE_TOTAL_SIZE"><%= Environment.PROP_CACHE_TOTAL_SIZE %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_CACHE_TOTAL_SIZE}" id="${PROP_CACHE_TOTAL_SIZE}" value="${props[PROP_CACHE_TOTAL_SIZE]}" size="10" /></span>
	<div class="formhelp"><f:message key="admin.cache.help.totalsize" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="cache" />">
	<span class="formcaption"><label for="<%= Environment.PROP_CACHE_INDIVIDUAL_SIZE %>"><f:message key="admin.cache.caption.individualsize" /></label></span>
	<c:set var="PROP_CACHE_INDIVIDUAL_SIZE"><%= Environment.PROP_CACHE_INDIVIDUAL_SIZE %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_CACHE_INDIVIDUAL_SIZE}" id="${PROP_CACHE_INDIVIDUAL_SIZE}" value="${props[PROP_CACHE_INDIVIDUAL_SIZE]}" size="10" /></span>
	<div class="formhelp"><f:message key="admin.cache.help.individualsize" /></div>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="cache" />">
	<span class="formcaption"><label for="<%= Environment.PROP_CACHE_MAX_AGE %>"><f:message key="admin.cache.caption.maxage" /></label></span>
	<c:set var="PROP_CACHE_MAX_AGE"><%= Environment.PROP_CACHE_MAX_AGE %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_CACHE_MAX_AGE}" id="${PROP_CACHE_MAX_AGE}" value="${props[PROP_CACHE_MAX_AGE]}" size="10" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="cache" />">
	<span class="formcaption"><label for="<%= Environment.PROP_CACHE_MAX_IDLE_AGE %>"><f:message key="admin.cache.caption.idleage" /></label></span>
	<c:set var="PROP_CACHE_MAX_IDLE_AGE"><%= Environment.PROP_CACHE_MAX_IDLE_AGE %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_CACHE_MAX_IDLE_AGE}" id="${PROP_CACHE_MAX_IDLE_AGE}" value="${props[PROP_CACHE_MAX_IDLE_AGE]}" size="10" /></span>
</div>
</fieldset>
<!-- END CACHE -->

<!-- BEGIN RSS -->
<a name="rss"></a>
<fieldset>
<legend><f:message key="admin.header.rss" /></legend>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="rss" />">
	<span class="formcaption"><label for="<%= Environment.PROP_RSS_ALLOWED %>"><f:message key="admin.rss.caption.allowed" /></label></span>
	<c:set var="PROP_RSS_ALLOWED"><%= Environment.PROP_RSS_ALLOWED %></c:set>
	<span class="formelement"><jamwiki:checkbox name="${PROP_RSS_ALLOWED}" value="true" checked="${props[PROP_RSS_ALLOWED]}" id="${PROP_RSS_ALLOWED}" onclick="onRSS()" /></span>
</div>
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="rss" />">
	<span class="formcaption"><label for="<%= Environment.PROP_RSS_TITLE %>"><f:message key="admin.rss.caption.title" /></label></span>
	<c:set var="PROP_RSS_TITLE"><%= Environment.PROP_RSS_TITLE %></c:set>
	<span class="formelement"><jamwiki:text name="${PROP_RSS_TITLE}" id="${PROP_RSS_TITLE}" value="${props[PROP_RSS_TITLE]}" size="50" /></span>
</div>
</fieldset>
<!-- END RSS -->

<a name="save"></a>
<table border="0" class="contents" width="99%">
<tr><td colspan="2">&nbsp;</td></tr>
<tr><td colspan="2" class="formelement" align="center"><input type="submit" name="Submit" value="<f:message key="admin.action.save" />" /></td></tr>
<tr><td colspan="2">&nbsp;</td></tr>
</table>

<input type="hidden" name="function" value="properties" />

<%--
  Include a hidden (display:none) password field to prevent Firefox from trying to change the
  admin password.  There is currently (version 1.5 and before) an issue with Firefox where
  anytime two or more password fields are in a form it assumes the password is being
  changed if the last password is different from the saved password.
--%>

<input type="password" name="fakePassword" value="" style="display:none" />
</form>
