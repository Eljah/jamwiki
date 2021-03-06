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

<div id="configuration" class="admin">

<c:if test="${!empty pageInfo.messages}">
<div class="message green"><c:forEach items="${pageInfo.messages}" var="message"><jamwiki_t:wikiMessage message="${message}" /><br /></c:forEach></div>
</c:if>
<c:if test="${!empty pageInfo.errors}">
<div class="message red"><c:forEach items="${pageInfo.errors}" var="message"><jamwiki_t:wikiMessage message="${message}" /><br /></c:forEach></div>
</c:if>

<%-- sub-menu tabs --%>
<ul class="tab-menu" id="tab_submenu">
<li><a href="#general"><fmt:message key="admin.header.general" /></a></li>
<li><a href="#parser"><fmt:message key="admin.header.parser" /></a></li>
<li><a href="#database"><fmt:message key="admin.header.persistence" /></a></li>
<li><a href="#upload"><fmt:message key="admin.header.upload" /></a></li>
<li><a href="#spam"><fmt:message key="admin.header.spam" /></a></li>
<li><a href="#other"><fmt:message key="admin.header.other" /></a></li>
</ul>
<div class="submenu-tab-content">

<form name="form1" method="post" action="<jamwiki:link value="Special:Admin" />">

<div class="callout">
	<table>
	<tr>
		<td><input type="submit" name="Submit" value="<fmt:message key="admin.action.save" />" /></td>
		<td><fmt:message key="admin.message.savechanges" /></td>
	</tr>
	</table>
</div>

<%-- BEGIN GENERAL SETTINGS --%>
<div id="general" class="submenu-tab-item">
<fieldset>
<legend><fmt:message key="admin.header.general" /></legend>
<div class="row">
	<label for="<%= Environment.PROP_BASE_FILE_DIR %>"><fmt:message key="admin.caption.filedir" /></label>
	<c:set var="PROP_BASE_FILE_DIR"><%= Environment.PROP_BASE_FILE_DIR %></c:set>
	<span><jamwiki:text name="${PROP_BASE_FILE_DIR}" value="${props[PROP_BASE_FILE_DIR]}" size="50" id="${PROP_BASE_FILE_DIR}" /></span>
	<div class="formhelp"><fmt:message key="admin.help.filedir" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_SERVER_URL %>"><fmt:message key="admin.caption.serverurl" /></label>
	<c:set var="PROP_SERVER_URL"><%= Environment.PROP_SERVER_URL %></c:set>
	<span><jamwiki:text name="${PROP_SERVER_URL}" value="${props[PROP_SERVER_URL]}" size="50" id="${PROP_SERVER_URL}" /></span>
	<div class="formhelp"><fmt:message key="admin.help.serverurl" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_SITE_NAME %>"><fmt:message key="admin.caption.sitename" /></label>
	<c:set var="PROP_SITE_NAME"><%= Environment.PROP_SITE_NAME %></c:set>
	<span><jamwiki:text name="${PROP_SITE_NAME}" value="${props[PROP_SITE_NAME]}" size="50" id="${PROP_SITE_NAME}" /></span>
	<div class="formhelp"><fmt:message key="admin.help.sitename" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_BASE_LOGO_IMAGE %>"><fmt:message key="admin.caption.logoimage" /></label>
	<c:set var="PROP_BASE_LOGO_IMAGE"><%= Environment.PROP_BASE_LOGO_IMAGE %></c:set>
	<span><jamwiki:text name="${PROP_BASE_LOGO_IMAGE}" value="${props[PROP_BASE_LOGO_IMAGE]}" size="30" id="${PROP_BASE_LOGO_IMAGE}" /></span>
	<div class="formhelp"><fmt:message key="admin.help.logoimage" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_BASE_DEFAULT_TOPIC %>"><fmt:message key="admin.caption.defaulttopic" /></label>
	<c:set var="PROP_BASE_DEFAULT_TOPIC"><%= Environment.PROP_BASE_DEFAULT_TOPIC %></c:set>
	<span><jamwiki:text name="${PROP_BASE_DEFAULT_TOPIC}" value="${props[PROP_BASE_DEFAULT_TOPIC]}" size="30" id="${PROP_BASE_DEFAULT_TOPIC}" /></span>
	<div class="formhelp"><fmt:message key="admin.help.defaulttopic" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_RECENT_CHANGES_NUM %>"><fmt:message key="admin.caption.recentchangesdefault" /></label>
	<c:set var="PROP_RECENT_CHANGES_NUM"><%= Environment.PROP_RECENT_CHANGES_NUM %></c:set>
	<span><jamwiki:text name="${PROP_RECENT_CHANGES_NUM}" size="5" maxlength="4" value="${props[PROP_RECENT_CHANGES_NUM]}" id="${PROP_RECENT_CHANGES_NUM}" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_TOPIC_USE_PREVIEW %>"><fmt:message key="admin.caption.usepreview" /></label>
	<c:set var="PROP_TOPIC_USE_PREVIEW"><%= Environment.PROP_TOPIC_USE_PREVIEW %></c:set>
	<span><jamwiki:checkbox name="${PROP_TOPIC_USE_PREVIEW}" value="true" checked="${props[PROP_TOPIC_USE_PREVIEW]}" id="${PROP_TOPIC_USE_PREVIEW}" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_TOPIC_USE_SHOW_CHANGES %>"><fmt:message key="admin.caption.useshowchanges" /></label>
	<c:set var="PROP_TOPIC_USE_SHOW_CHANGES"><%= Environment.PROP_TOPIC_USE_SHOW_CHANGES %></c:set>
	<span><jamwiki:checkbox name="${PROP_TOPIC_USE_SHOW_CHANGES}" value="true" checked="${props[PROP_TOPIC_USE_SHOW_CHANGES]}" id="${PROP_TOPIC_USE_SHOW_CHANGES}" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_BASE_META_DESCRIPTION %>"><fmt:message key="admin.caption.metadescription" /></label>
	<c:set var="PROP_BASE_META_DESCRIPTION"><%= Environment.PROP_BASE_META_DESCRIPTION %></c:set>
	<span><textarea class="medium" name="<%= Environment.PROP_BASE_META_DESCRIPTION %>" id="<%= Environment.PROP_BASE_META_DESCRIPTION %>"><c:out value="${props[PROP_BASE_META_DESCRIPTION]}" /></textarea></span>
	<div class="formhelp"><fmt:message key="admin.help.metadescription" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_BASE_SEARCH_ENGINE %>"><fmt:message key="admin.caption.searchengine" /></label>
	<c:set var="PROP_BASE_SEARCH_ENGINE"><%= Environment.PROP_BASE_SEARCH_ENGINE %></c:set>
	<span>
		<select name="<%= Environment.PROP_BASE_SEARCH_ENGINE %>" id="<%= Environment.PROP_BASE_SEARCH_ENGINE %>">
		<c:set var="PROP_BASE_SEARCH_ENGINE"><%= Environment.PROP_BASE_SEARCH_ENGINE %></c:set>
		<c:forEach items="${searchEngines}" var="searchEngine">
		<option value="<c:out value="${searchEngine.clazz}" />"<c:if test="${props[PROP_BASE_SEARCH_ENGINE] == searchEngine.clazz}"> selected="selected"</c:if>><c:if test="${!empty searchEngine.key}"><fmt:message key="${searchEngine.key}" /></c:if><c:if test="${empty searchEngine.key}"><c:out value="${searchEngine.name}" /></c:if><c:if test="${searchEngine.experimental}"> (<fmt:message key="common.caption.experimental" />)</c:if></option>
		</c:forEach>
		</select>
	</span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_TOPIC_EDITOR %>"><fmt:message key="admin.caption.editor" /></label>
	<c:set var="PROP_TOPIC_EDITOR"><%= Environment.PROP_TOPIC_EDITOR %></c:set>
	<span>
		<select name="<%= Environment.PROP_TOPIC_EDITOR %>" id="<%= Environment.PROP_TOPIC_EDITOR %>">
		<c:set var="PROP_TOPIC_EDITOR"><%= Environment.PROP_TOPIC_EDITOR %></c:set>
		<c:forEach items="${editors}" var="editor">
		<option value="<c:out value="${editor.key}" />"<c:if test="${props[PROP_TOPIC_EDITOR] == editor.key}"> selected="selected"</c:if>><c:out value="${editor.value}" /></option>
		</c:forEach>
		</select>
	</span>
	<div class="formhelp"><fmt:message key="admin.help.editor" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_MAX_TOPIC_VERSION_EXPORT %>"><fmt:message key="admin.caption.maxversionexport" /></label>
	<c:set var="PROP_MAX_TOPIC_VERSION_EXPORT"><%= Environment.PROP_MAX_TOPIC_VERSION_EXPORT %></c:set>
	<span><jamwiki:text name="${PROP_MAX_TOPIC_VERSION_EXPORT}" size="5" maxlength="4" value="${props[PROP_MAX_TOPIC_VERSION_EXPORT]}" id="${PROP_MAX_TOPIC_VERSION_EXPORT}" /></span>
	<div class="formhelp"><fmt:message key="admin.help.maxversionexport" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_DATE_PATTERN_DATE_AND_TIME %>"><fmt:message key="admin.caption.date.dateandtime" /></label>
	<c:set var="PROP_DATE_PATTERN_DATE_AND_TIME"><%= Environment.PROP_DATE_PATTERN_DATE_AND_TIME %></c:set>
	<span><jamwiki:text name="${PROP_DATE_PATTERN_DATE_AND_TIME}" size="30" value="${props[PROP_DATE_PATTERN_DATE_AND_TIME]}" id="${PROP_DATE_PATTERN_DATE_AND_TIME}" /></span>
	<div class="formhelp"><fmt:message key="admin.help.date.dateandtime" /> <fmt:message key="admin.help.date.common" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_DATE_PATTERN_DATE_ONLY %>"><fmt:message key="admin.caption.date.dateonly" /></label>
	<c:set var="PROP_DATE_PATTERN_DATE_ONLY"><%= Environment.PROP_DATE_PATTERN_DATE_ONLY %></c:set>
	<span><jamwiki:text name="${PROP_DATE_PATTERN_DATE_ONLY}" size="30" value="${props[PROP_DATE_PATTERN_DATE_ONLY]}" id="${PROP_DATE_PATTERN_DATE_ONLY}" /></span>
	<div class="formhelp"><fmt:message key="admin.help.date.dateonly" /> <fmt:message key="admin.help.date.common" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_DATE_PATTERN_TIME_ONLY %>"><fmt:message key="admin.caption.date.timeonly" /></label>
	<c:set var="PROP_DATE_PATTERN_TIME_ONLY"><%= Environment.PROP_DATE_PATTERN_TIME_ONLY %></c:set>
	<span><jamwiki:text name="${PROP_DATE_PATTERN_TIME_ONLY}" size="30" value="${props[PROP_DATE_PATTERN_TIME_ONLY]}" id="${PROP_DATE_PATTERN_TIME_ONLY}" /></span>
	<div class="formhelp"><fmt:message key="admin.help.date.timeonly" /> <fmt:message key="admin.help.date.common" /></div>
</div>
</fieldset>
</div>

<%-- BEGIN PARSER --%>
<div id="parser" class="submenu-tab-item">
<fieldset>
<legend><fmt:message key="admin.header.parser" /></legend>
<div class="row">
	<label for="<%= Environment.PROP_PARSER_CLASS %>"><fmt:message key="admin.parser.caption" /></label>
	<span>
		<select name="<%= Environment.PROP_PARSER_CLASS %>" id="<%= Environment.PROP_PARSER_CLASS %>">
		<c:set var="PROP_PARSER_CLASS"><%= Environment.PROP_PARSER_CLASS %></c:set>
		<c:forEach items="${parsers}" var="parser">
		<option value="<c:out value="${parser.clazz}" />"<c:if test="${props[PROP_PARSER_CLASS] == parser.clazz}"> selected="selected"</c:if>><c:if test="${!empty parser.key}"><fmt:message key="${parser.key}" /></c:if><c:if test="${empty parser.key}"><c:out value="${parser.name}" /></c:if></option>
		</c:forEach>
		</select>
	</span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_PARSER_ALLOW_HTML %>"><fmt:message key="admin.parser.caption.allowhtml" /></label>
	<c:set var="PROP_PARSER_ALLOW_HTML"><%= Environment.PROP_PARSER_ALLOW_HTML %></c:set>
	<span><jamwiki:checkbox name="${PROP_PARSER_ALLOW_HTML}" value="true" checked="${props[PROP_PARSER_ALLOW_HTML]}" id="${PROP_PARSER_ALLOW_HTML}" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_PARSER_ALLOW_JAVASCRIPT %>"><fmt:message key="admin.parser.caption.allowjavascript" /></label>
	<c:set var="PROP_PARSER_ALLOW_JAVASCRIPT"><%= Environment.PROP_PARSER_ALLOW_JAVASCRIPT %></c:set>
	<span><jamwiki:checkbox name="${PROP_PARSER_ALLOW_JAVASCRIPT}" value="true" checked="${props[PROP_PARSER_ALLOW_JAVASCRIPT]}" id="${PROP_PARSER_ALLOW_JAVASCRIPT}" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_PARSER_ALLOW_TEMPLATES %>"><fmt:message key="admin.parser.caption.allowtemplates" /></label>
	<c:set var="PROP_PARSER_ALLOW_TEMPLATES"><%= Environment.PROP_PARSER_ALLOW_TEMPLATES %></c:set>
	<span><jamwiki:checkbox name="${PROP_PARSER_ALLOW_TEMPLATES}" value="true" checked="${props[PROP_PARSER_ALLOW_TEMPLATES]}" id="${PROP_PARSER_ALLOW_TEMPLATES}" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_PRINT_NEW_WINDOW %>"><fmt:message key="admin.caption.printnewwindow" /></label>
	<c:set var="PROP_PRINT_NEW_WINDOW"><%= Environment.PROP_PRINT_NEW_WINDOW %></c:set>
	<span><jamwiki:checkbox name="${PROP_PRINT_NEW_WINDOW}" value="true" checked="${props[PROP_PRINT_NEW_WINDOW]}" id="${PROP_PRINT_NEW_WINDOW}" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_EXTERNAL_LINK_NEW_WINDOW %>"><fmt:message key="admin.caption.externallinknewwindow" /></label>
	<c:set var="PROP_EXTERNAL_LINK_NEW_WINDOW"><%= Environment.PROP_EXTERNAL_LINK_NEW_WINDOW %></c:set>
	<span><jamwiki:checkbox name="${PROP_EXTERNAL_LINK_NEW_WINDOW}" value="true" checked="${props[PROP_EXTERNAL_LINK_NEW_WINDOW]}" id="${PROP_EXTERNAL_LINK_NEW_WINDOW}" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_PARSER_USE_NUMBERED_HTML_LINKS %>"><fmt:message key="admin.parser.caption.numberedhtmllinks" /></label>
	<c:set var="PROP_PARSER_USE_NUMBERED_HTML_LINKS"><%= Environment.PROP_PARSER_USE_NUMBERED_HTML_LINKS %></c:set>
	<span><jamwiki:checkbox name="${PROP_PARSER_USE_NUMBERED_HTML_LINKS}" value="true" checked="${props[PROP_PARSER_USE_NUMBERED_HTML_LINKS]}" id="${PROP_PARSER_USE_NUMBERED_HTML_LINKS}" /></span>
	<div class="formhelp"><fmt:message key="admin.parser.help.numberedhtmllinks" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_PARSER_TOC %>"><fmt:message key="admin.parser.caption.tableofcontents" /></label>
	<c:set var="PROP_PARSER_TOC"><%= Environment.PROP_PARSER_TOC %></c:set>
	<span><jamwiki:checkbox name="${PROP_PARSER_TOC}" value="true" checked="${props[PROP_PARSER_TOC]}" id="${PROP_PARSER_TOC}" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_PARSER_TOC_DEPTH %>"><fmt:message key="admin.parser.caption.tableofcontentsdepth" /></label>
	<c:set var="PROP_PARSER_TOC_DEPTH"><%= Environment.PROP_PARSER_TOC_DEPTH %></c:set>
	<span><jamwiki:text name="${PROP_PARSER_TOC_DEPTH}" value="${props[PROP_PARSER_TOC_DEPTH]}" size="5" maxlength="1" id="${PROP_PARSER_TOC_DEPTH}" /></span>
	<div class="formhelp"><fmt:message key="admin.parser.help.tableofcontentsdepth" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_PARSER_ALLOW_CAPITALIZATION %>"><fmt:message key="admin.parser.caption.allowcapitalized" /></label>
	<c:set var="PROP_PARSER_ALLOW_CAPITALIZATION"><%= Environment.PROP_PARSER_ALLOW_CAPITALIZATION %></c:set>
	<span><jamwiki:checkbox name="${PROP_PARSER_ALLOW_CAPITALIZATION}" value="true" checked="${props[PROP_PARSER_ALLOW_CAPITALIZATION]}" id="${PROP_PARSER_ALLOW_CAPITALIZATION}" /></span>
	<div class="formhelp"><fmt:message key="admin.parser.help.allowcapitalized" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_PARSER_DISPLAY_INTERWIKI_LINKS_INLINE %>"><fmt:message key="admin.parser.caption.interwikiinline" /></label>
	<c:set var="PROP_PARSER_DISPLAY_INTERWIKI_LINKS_INLINE"><%= Environment.PROP_PARSER_DISPLAY_INTERWIKI_LINKS_INLINE %></c:set>
	<span><jamwiki:checkbox name="${PROP_PARSER_DISPLAY_INTERWIKI_LINKS_INLINE}" value="true" checked="${props[PROP_PARSER_DISPLAY_INTERWIKI_LINKS_INLINE]}" id="${PROP_PARSER_DISPLAY_INTERWIKI_LINKS_INLINE}" /></span>
	<div class="formhelp"><fmt:message key="admin.parser.help.interwikiinline" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_PARSER_DISPLAY_SPECIAL_PAGE_VIRTUAL_WIKI_LINKS %>"><fmt:message key="admin.parser.caption.specialvirtualwiki" /></label>
	<c:set var="PROP_PARSER_DISPLAY_SPECIAL_PAGE_VIRTUAL_WIKI_LINKS"><%= Environment.PROP_PARSER_DISPLAY_SPECIAL_PAGE_VIRTUAL_WIKI_LINKS %></c:set>
	<span><jamwiki:checkbox name="${PROP_PARSER_DISPLAY_SPECIAL_PAGE_VIRTUAL_WIKI_LINKS}" value="true" checked="${props[PROP_PARSER_DISPLAY_SPECIAL_PAGE_VIRTUAL_WIKI_LINKS]}" id="${PROP_PARSER_DISPLAY_SPECIAL_PAGE_VIRTUAL_WIKI_LINKS}" /></span>
	<div class="formhelp"><fmt:message key="admin.parser.help.specialvirtualwiki" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_PARSER_DISPLAY_VIRTUALWIKI_LINKS_INLINE %>"><fmt:message key="admin.parser.caption.virtualwikiinline" /></label>
	<c:set var="PROP_PARSER_DISPLAY_VIRTUALWIKI_LINKS_INLINE"><%= Environment.PROP_PARSER_DISPLAY_VIRTUALWIKI_LINKS_INLINE %></c:set>
	<span><jamwiki:checkbox name="${PROP_PARSER_DISPLAY_VIRTUALWIKI_LINKS_INLINE}" value="true" checked="${props[PROP_PARSER_DISPLAY_VIRTUALWIKI_LINKS_INLINE]}" id="${PROP_PARSER_DISPLAY_VIRTUALWIKI_LINKS_INLINE}" /></span>
	<div class="formhelp"><fmt:message key="admin.parser.help.virtualwikiinline" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_IMAGE_RESIZE_INCREMENT %>"><fmt:message key="admin.caption.imageresize" /></label>
	<c:set var="PROP_IMAGE_RESIZE_INCREMENT"><%= Environment.PROP_IMAGE_RESIZE_INCREMENT %></c:set>
	<span><jamwiki:text name="${PROP_IMAGE_RESIZE_INCREMENT}" size="5" maxlength="4" value="${props[PROP_IMAGE_RESIZE_INCREMENT]}" id="${PROP_IMAGE_RESIZE_INCREMENT}" /></span>
	<div class="formhelp"><fmt:message key="admin.help.imageresize" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_PARSER_SIGNATURE_USER_PATTERN %>"><fmt:message key="admin.parser.caption.signatureuser" /></label>
	<c:set var="PROP_PARSER_SIGNATURE_USER_PATTERN"><%= Environment.PROP_PARSER_SIGNATURE_USER_PATTERN %></c:set>
	<span><jamwiki:text name="${PROP_PARSER_SIGNATURE_USER_PATTERN}" value="${props[PROP_PARSER_SIGNATURE_USER_PATTERN]}" size="50" id="${PROP_PARSER_SIGNATURE_USER_PATTERN}" /></span>
	<div class="formhelp"><fmt:message key="admin.parser.help.signatureuser" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_PARSER_SIGNATURE_DATE_PATTERN %>"><fmt:message key="admin.parser.caption.signaturedate" /></label>
	<c:set var="PROP_PARSER_SIGNATURE_DATE_PATTERN"><%= Environment.PROP_PARSER_SIGNATURE_DATE_PATTERN %></c:set>
	<span><jamwiki:text name="${PROP_PARSER_SIGNATURE_DATE_PATTERN}" value="${props[PROP_PARSER_SIGNATURE_DATE_PATTERN]}" size="50" id="${PROP_PARSER_SIGNATURE_DATE_PATTERN}" /></span>
	<div class="formhelp"><fmt:message key="admin.parser.help.signaturedate" /> <fmt:message key="admin.help.date.common" /></div>
</div>
</fieldset>
</div>

<%-- BEGIN EMAIL
FIXME - Email not supported right now, comment this out

<div id="email" class="submenu-tab-item">
<fieldset>
<legend><fmt:message key="admin.smtp.caption" /></legend>
<div class="row">
	<label for="<%= Environment.PROP_EMAIL_SMTP_HOST %>"><fmt:message key="admin.smtp.caption.host" /></label>
	<c:set var="PROP_EMAIL_SMTP_HOST"><%= Environment.PROP_EMAIL_SMTP_HOST %></c:set>
	<span><jamwiki:text name="${PROP_EMAIL_SMTP_HOST}" value="${props[PROP_EMAIL_SMTP_HOST]}" size="30" id="${PROP_EMAIL_SMTP_HOST}" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_EMAIL_SMTP_USERNAME %>"><fmt:message key="admin.smtp.caption.user" /></label>
	<c:set var="PROP_EMAIL_SMTP_USERNAME"><%= Environment.PROP_EMAIL_SMTP_USERNAME %></c:set>
	<span><jamwiki:text name="${PROP_EMAIL_SMTP_USERNAME}" value="${props[PROP_EMAIL_SMTP_USERNAME]}" size="30" id="${PROP_EMAIL_SMTP_USERNAME}" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_EMAIL_SMTP_PASSWORD %>"><fmt:message key="admin.smtp.caption.pass" /></label>
	<span><input type="password" name="<%= Environment.PROP_EMAIL_SMTP_PASSWORD %>" value="<c:out value="${smtpPassword}" />" size="30" id="<%= Environment.PROP_EMAIL_SMTP_PASSWORD %>" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_EMAIL_REPLY_ADDRESS %>"><fmt:message key="admin.smtp.caption.reply" /></label>
	<c:set var="PROP_EMAIL_REPLY_ADDRESS"><%= Environment.PROP_EMAIL_REPLY_ADDRESS %></c:set>
	<span><jamwiki:text name="${PROP_EMAIL_REPLY_ADDRESS}" value="${props[PROP_EMAIL_REPLY_ADDRESS]}" size="50" id="${PROP_EMAIL_REPLY_ADDRESS}" /></span>
</div>
</fieldset>
</div>

--%>

<%-- BEGIN DATABASE PERSISTENCE --%>
<div id="database" class="submenu-tab-item">
<fieldset>
<legend><fmt:message key="admin.header.persistence" /></legend>
<div class="row">
	<label for="<%= Environment.PROP_BASE_PERSISTENCE_TYPE %>"><fmt:message key="admin.persistence.caption" /></label>
	<span>
		<select name="<%= Environment.PROP_BASE_PERSISTENCE_TYPE %>" id="<%= Environment.PROP_BASE_PERSISTENCE_TYPE %>">
		<c:set var="PROP_BASE_PERSISTENCE_TYPE"><%= Environment.PROP_BASE_PERSISTENCE_TYPE %></c:set>
		<c:set var="persistenceTypeInternal"><%= WikiBase.PERSISTENCE_INTERNAL %></c:set>
		<c:set var="persistenceTypeExternal"><%= WikiBase.PERSISTENCE_EXTERNAL %></c:set>
		<option value="<%= WikiBase.PERSISTENCE_INTERNAL %>"<c:if test="${props[PROP_BASE_PERSISTENCE_TYPE] == persistenceTypeInternal}"> selected="selected"</c:if>><fmt:message key="admin.persistencetype.internal" /></option>
		<option value="<%= WikiBase.PERSISTENCE_EXTERNAL %>"<c:if test="${props[PROP_BASE_PERSISTENCE_TYPE] == persistenceTypeExternal}"> selected="selected"</c:if>><fmt:message key="admin.persistencetype.database" /></option>
		</select>
	</span>
	<div class="formhelp"><fmt:message key="admin.persistence.help" /></div>
</div>
<div id="db-details" class="expander expander-open">
	<div class="row">
		<label for="<%= Environment.PROP_DB_TYPE %>"><fmt:message key="admin.persistence.caption.type" /></label>
		<span>
			<select name="<%= Environment.PROP_DB_TYPE %>" id="<%= Environment.PROP_DB_TYPE %>">
			<option value=""></option>
			<c:set var="PROP_DB_TYPE"><%= Environment.PROP_DB_TYPE %></c:set>
			<c:forEach items="${queryHandlers}" var="queryHandler">
			<option value="<c:out value="${queryHandler.clazz}" />"<c:if test="${props[PROP_DB_TYPE] == queryHandler.clazz}"> selected="selected"</c:if>><c:if test="${!empty queryHandler.key}"><fmt:message key="${queryHandler.key}" /></c:if><c:if test="${empty queryHandler.key}"><c:out value="${queryHandler.name}" /></c:if><c:if test="${queryHandler.experimental}"> (<fmt:message key="common.caption.experimental" />)</c:if></option>
			</c:forEach>
			</select>
		</span>
	</div>
	<div class="row">
		<label for="<%= Environment.PROP_DB_DRIVER %>"><fmt:message key="admin.persistence.caption.driver" /></label>
		<c:set var="PROP_DB_DRIVER"><%= Environment.PROP_DB_DRIVER %></c:set>
		<span><jamwiki:text name="${PROP_DB_DRIVER}" id="${PROP_DB_DRIVER}" value="${props[PROP_DB_DRIVER]}" size="50" /></span>
	</div>
	<div class="row">
		<label for="<%= Environment.PROP_DB_URL %>"><fmt:message key="admin.persistence.caption.url" /></label>
		<c:set var="PROP_DB_URL"><%= Environment.PROP_DB_URL %></c:set>
		<span><jamwiki:text name="${PROP_DB_URL}" id="${PROP_DB_URL}" value="${props[PROP_DB_URL]}" size="50" /></span>
	</div>
	<div class="row">
		<label for="<%= Environment.PROP_DB_USERNAME %>"><fmt:message key="admin.persistence.caption.user" /></label>
		<c:set var="PROP_DB_USERNAME"><%= Environment.PROP_DB_USERNAME %></c:set>
		<span><jamwiki:text name="${PROP_DB_USERNAME}" id="${PROP_DB_USERNAME}" value="${props[PROP_DB_USERNAME]}" size="30" /></span>
	</div>
	<div class="row">
		<label for="<%= Environment.PROP_DB_PASSWORD %>"><fmt:message key="admin.persistence.caption.pass" /></label>
		<span><input type="password" name="<%= Environment.PROP_DB_PASSWORD %>" id="<%= Environment.PROP_DB_PASSWORD %>" value="<c:out value="${dbPassword}" />" size="30" /></span>
	</div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_DBCP_MAX_ACTIVE %>"><fmt:message key="admin.persistence.caption.maxactive" /></label>
	<c:set var="PROP_DBCP_MAX_ACTIVE"><%= Environment.PROP_DBCP_MAX_ACTIVE %></c:set>
	<span><jamwiki:text name="${PROP_DBCP_MAX_ACTIVE}" id="${PROP_DBCP_MAX_ACTIVE}" value="${props[PROP_DBCP_MAX_ACTIVE]}" size="5" maxlength="3" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_DBCP_MAX_IDLE %>"><fmt:message key="admin.persistence.caption.maxidle" /></label>
	<c:set var="PROP_DBCP_MAX_IDLE"><%= Environment.PROP_DBCP_MAX_IDLE %></c:set>
	<span><jamwiki:text name="${PROP_DBCP_MAX_IDLE}" id="${PROP_DBCP_MAX_IDLE}" value="${props[PROP_DBCP_MAX_IDLE]}" size="5" maxlength="3" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_DBCP_TEST_ON_BORROW %>"><fmt:message key="admin.persistence.caption.testonborrow" /></label>
	<c:set var="PROP_DBCP_TEST_ON_BORROW"><%= Environment.PROP_DBCP_TEST_ON_BORROW %></c:set>
	<span><jamwiki:checkbox name="${PROP_DBCP_TEST_ON_BORROW}" value="true" checked="${props[PROP_DBCP_TEST_ON_BORROW]}" id="${PROP_DBCP_TEST_ON_BORROW}" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_DBCP_TEST_ON_RETURN %>"><fmt:message key="admin.persistence.caption.testonreturn" /></label>
	<c:set var="PROP_DBCP_TEST_ON_RETURN"><%= Environment.PROP_DBCP_TEST_ON_RETURN %></c:set>
	<span><jamwiki:checkbox name="${PROP_DBCP_TEST_ON_RETURN}" value="true" checked="${props[PROP_DBCP_TEST_ON_RETURN]}" id="${PROP_DBCP_TEST_ON_RETURN}" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_DBCP_TEST_WHILE_IDLE %>"><fmt:message key="admin.persistence.caption.testwhileidle" /></label>
	<c:set var="PROP_DBCP_TEST_WHILE_IDLE"><%= Environment.PROP_DBCP_TEST_WHILE_IDLE %></c:set>
	<span><jamwiki:checkbox name="${PROP_DBCP_TEST_WHILE_IDLE}" value="true" checked="${props[PROP_DBCP_TEST_WHILE_IDLE]}" id="${PROP_DBCP_TEST_WHILE_IDLE}" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_DBCP_MIN_EVICTABLE_IDLE_TIME %>"><fmt:message key="admin.persistence.caption.minevictableidletime" /></label>
	<c:set var="PROP_DBCP_MIN_EVICTABLE_IDLE_TIME"><%= Environment.PROP_DBCP_MIN_EVICTABLE_IDLE_TIME %></c:set>
	<span><jamwiki:text name="${PROP_DBCP_MIN_EVICTABLE_IDLE_TIME}" id="${PROP_DBCP_MIN_EVICTABLE_IDLE_TIME}" value="${props[PROP_DBCP_MIN_EVICTABLE_IDLE_TIME]}" size="5" maxlength="4" /></span>
	<div class="formhelp"><fmt:message key="admin.persistence.help.minevictableidletime" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS %>"><fmt:message key="admin.persistence.caption.timebetweenevictionruns" /></label>
	<c:set var="PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS"><%= Environment.PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS %></c:set>
	<span><jamwiki:text name="${PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS}" id="${PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS}" value="${props[PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS]}" size="5" maxlength="4" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN %>"><fmt:message key="admin.persistence.caption.numtestsperevictionrun" /></label>
	<c:set var="PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN"><%= Environment.PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN %></c:set>
	<span><jamwiki:text name="${PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN}" id="${PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN}" value="${props[PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN]}" size="5" maxlength="4" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_DBCP_WHEN_EXHAUSTED_ACTION %>"><fmt:message key="admin.persistence.caption.whenexhaustedaction" /></label>
	<span>
		<select name="<%= Environment.PROP_DBCP_WHEN_EXHAUSTED_ACTION %>" id="<%= Environment.PROP_DBCP_WHEN_EXHAUSTED_ACTION %>">
		<c:set var="PROP_DBCP_WHEN_EXHAUSTED_ACTION"><%= Environment.PROP_DBCP_WHEN_EXHAUSTED_ACTION %></c:set>
		<c:forEach items="${poolExhaustedMap}" var="poolExhausted">
		<option value="<c:out value="${poolExhausted.key}" />"<c:if test="${poolExhausted.key == props[PROP_DBCP_WHEN_EXHAUSTED_ACTION]}"> selected="selected"</c:if>><fmt:message key="${poolExhausted.value}" /></option>
		</c:forEach>
		</select>
	</span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_DBCP_POOL_PREPARED_STATEMENTS %>"><fmt:message key="admin.persistence.caption.poolpreparedstatements" /></label>
	<c:set var="PROP_DBCP_POOL_PREPARED_STATEMENTS"><%= Environment.PROP_DBCP_POOL_PREPARED_STATEMENTS %></c:set>
	<span><jamwiki:checkbox name="${PROP_DBCP_POOL_PREPARED_STATEMENTS}" value="true" checked="${props[PROP_DBCP_POOL_PREPARED_STATEMENTS]}" id="${PROP_DBCP_POOL_PREPARED_STATEMENTS}" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_DBCP_MAX_OPEN_PREPARED_STATEMENTS %>"><fmt:message key="admin.persistence.caption.maxopenpreparedstatements" /></label>
	<c:set var="PROP_DBCP_MAX_OPEN_PREPARED_STATEMENTS"><%= Environment.PROP_DBCP_MAX_OPEN_PREPARED_STATEMENTS %></c:set>
	<span><jamwiki:text name="${PROP_DBCP_MAX_OPEN_PREPARED_STATEMENTS}" id="${PROP_DBCP_MAX_OPEN_PREPARED_STATEMENTS}" value="${props[PROP_DBCP_MAX_OPEN_PREPARED_STATEMENTS]}" size="5" maxlength="4" /></span>
	<div class="formhelp"><fmt:message key="admin.persistence.help.maxopenpreparedstatements" /></div>
</div>
</fieldset>
</div>

<%-- BEGIN FILE UPLOAD --%>
<div id="upload" class="submenu-tab-item">
<fieldset>
<legend><fmt:message key="admin.header.upload" /></legend>
<div class="row">
	<label for="<%= Environment.PROP_FILE_UPLOAD_STORAGE %>"><fmt:message key="admin.upload.caption.storage" /></label>
	<span>
		<select name="<%= Environment.PROP_FILE_UPLOAD_STORAGE %>" id="<%= Environment.PROP_FILE_UPLOAD_STORAGE %>">
		<c:set var="PROP_FILE_UPLOAD_STORAGE"><%= Environment.PROP_FILE_UPLOAD_STORAGE %></c:set>
		<option value="JAMWIKI"<c:if test="${props[PROP_FILE_UPLOAD_STORAGE] == 'JAMWIKI'}"> selected="selected"</c:if>><fmt:message key="admin.upload.storage.default" /> (<fmt:message key="common.caption.default" />)</option>
		<option value="DOCROOT"<c:if test="${props[PROP_FILE_UPLOAD_STORAGE] == 'DOCROOT'}"> selected="selected"</c:if>><fmt:message key="admin.upload.storage.docroot" /></option>
		<option value="DATABASE"<c:if test="${props[PROP_FILE_UPLOAD_STORAGE] == 'DATABASE'}"> selected="selected"</c:if>><fmt:message key="admin.upload.storage.database" /> (<fmt:message key="common.caption.experimental" />)</option>
		</select>
	</span>
	<div class="formhelp"><fmt:message key="admin.upload.help.storage" /> <fmt:message key="admin.upload.help.storage.note" /></div>
</div>
<div id="upload-details" class="expander expander-open">
	<div class="row">
		<label for="<%= Environment.PROP_FILE_DIR_FULL_PATH %>"><fmt:message key="admin.upload.caption.uploaddir" /></label>
		<c:set var="PROP_FILE_DIR_FULL_PATH"><%= Environment.PROP_FILE_DIR_FULL_PATH %></c:set>
		<span><jamwiki:text name="${PROP_FILE_DIR_FULL_PATH}" value="${props[PROP_FILE_DIR_FULL_PATH]}" size="50" id="${PROP_FILE_DIR_FULL_PATH}" /></span>
		<div class="formhelp"><fmt:message key="admin.upload.help.uploaddir" /></div>
	</div>
	<div class="row">
		<label for="<%= Environment.PROP_FILE_DIR_RELATIVE_PATH %>"><fmt:message key="admin.upload.caption.uploaddirrel" /></label>
		<c:set var="PROP_FILE_DIR_RELATIVE_PATH"><%= Environment.PROP_FILE_DIR_RELATIVE_PATH %></c:set>
		<span><jamwiki:text name="${PROP_FILE_DIR_RELATIVE_PATH}" value="${props[PROP_FILE_DIR_RELATIVE_PATH]}" size="50" id="${PROP_FILE_DIR_RELATIVE_PATH}" /></span>
		<div class="formhelp"><fmt:message key="admin.upload.help.uploaddirrel" /></div>
	</div>
	<div class="row">
		<label for="<%= Environment.PROP_FILE_SERVER_URL %>"><fmt:message key="admin.upload.caption.serverurl" /></label>
		<c:set var="PROP_FILE_SERVER_URL"><%= Environment.PROP_FILE_SERVER_URL %></c:set>
		<span><jamwiki:text name="${PROP_FILE_SERVER_URL}" value="${props[PROP_FILE_SERVER_URL]}" size="50" id="${PROP_FILE_SERVER_URL}" /></span>
		<div class="formhelp"><fmt:message key="admin.upload.help.serverurl" /></div>
	</div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_FILE_MAX_FILE_SIZE %>"><fmt:message key="admin.upload.caption.maxfilesize" /></label>
	<span><input type="text" name="<%= Environment.PROP_FILE_MAX_FILE_SIZE %>" value="<c:out value="${maximumFileSize}" />" size="10" id="<%= Environment.PROP_FILE_MAX_FILE_SIZE %>" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_SHARED_UPLOAD_VIRTUAL_WIKI %>"><fmt:message key="admin.upload.caption.sharedrepository" /></label>
	<span>
		<c:set var="PROP_SHARED_UPLOAD_VIRTUAL_WIKI"><%= Environment.PROP_SHARED_UPLOAD_VIRTUAL_WIKI %></c:set>
		<select name="<%= Environment.PROP_SHARED_UPLOAD_VIRTUAL_WIKI %>" id="<%= Environment.PROP_SHARED_UPLOAD_VIRTUAL_WIKI %>" onchange="onUploadType()">
		<option value=""></option>
		<c:forEach items="${virtualwikis}" var="virtualwiki">
		<option value="<c:out value="${virtualwiki.name}" />"<c:if test="${props[PROP_SHARED_UPLOAD_VIRTUAL_WIKI] == virtualwiki.name}"> selected="selected"</c:if>>${virtualwiki.name}</option>
		</c:forEach>
		</select>
	</span>
	<div class="formhelp"><fmt:message key="admin.upload.help.sharedrepository" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_FILE_BLACKLIST_TYPE %>"><fmt:message key="admin.upload.caption.blacklisttype" /></label>
	<span>
		<c:set var="PROP_FILE_BLACKLIST_TYPE"><%= Environment.PROP_FILE_BLACKLIST_TYPE %></c:set>
		<select name="<%= Environment.PROP_FILE_BLACKLIST_TYPE %>" id="<%= Environment.PROP_FILE_BLACKLIST_TYPE %>" onchange="onUploadType()">
		<c:forEach items="${blacklistTypes}" var="blacklistType">
		<option value="<c:out value="${blacklistType.key}" />"<c:if test="${props[PROP_FILE_BLACKLIST_TYPE] == blacklistType.key}"> selected="selected"</c:if>><fmt:message key="${blacklistType.value}" /></option>
		</c:forEach>
		</select>
	</span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_FILE_BLACKLIST %>"><fmt:message key="admin.upload.caption.blacklist" /></label>
	<c:set var="PROP_FILE_BLACKLIST"><%= Environment.PROP_FILE_BLACKLIST %></c:set>
	<span><textarea class="medium" name="<%= Environment.PROP_FILE_BLACKLIST %>" id="<%= Environment.PROP_FILE_BLACKLIST %>"><c:out value="${props[PROP_FILE_BLACKLIST]}" /></textarea></span>
	<div class="formhelp"><fmt:message key="admin.upload.help.blacklist" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_FILE_WHITELIST %>"><fmt:message key="admin.upload.caption.whitelist" /></label>
	<c:set var="PROP_FILE_WHITELIST"><%= Environment.PROP_FILE_WHITELIST %></c:set>
	<span><textarea class="medium" name="<%= Environment.PROP_FILE_WHITELIST %>" id="<%= Environment.PROP_FILE_WHITELIST %>"><c:out value="${props[PROP_FILE_WHITELIST]}" /></textarea></span>
	<div class="formhelp"><fmt:message key="admin.upload.help.whitelist" /></div>
</div>
</fieldset>
</div>

<%-- BEGIN SPAM --%>
<div id="spam" class="submenu-tab-item">
<fieldset>
<legend><fmt:message key="admin.header.spam" /></legend>
<div class="row">
	<label for="<%= Environment.PROP_TOPIC_SPAM_FILTER %>"><fmt:message key="admin.spam.caption.usespamfilter" /></label>
	<c:set var="PROP_TOPIC_SPAM_FILTER"><%= Environment.PROP_TOPIC_SPAM_FILTER %></c:set>
	<span><jamwiki:checkbox name="${PROP_TOPIC_SPAM_FILTER}" value="true" checked="${props[PROP_TOPIC_SPAM_FILTER]}" id="${PROP_TOPIC_SPAM_FILTER}" /></span>
	<div class="formhelp"><fmt:message key="admin.spam.help.usespamfilter" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_HONEYPOT_FILTER_ENABLED %>"><fmt:message key="admin.spam.caption.usehoneypotfilter" /> (<fmt:message key="common.caption.experimental" />)</label>
	<c:set var="PROP_HONEYPOT_FILTER_ENABLED"><%= Environment.PROP_HONEYPOT_FILTER_ENABLED %></c:set>
	<span><jamwiki:checkbox name="${PROP_HONEYPOT_FILTER_ENABLED}" value="true" checked="${props[PROP_HONEYPOT_FILTER_ENABLED]}" id="${PROP_HONEYPOT_FILTER_ENABLED}" /></span>
	<div class="formhelp"><fmt:message key="admin.spam.help.usehoneypotfilter" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_HONEYPOT_ACCESS_KEY %>"><fmt:message key="admin.spam.caption.honeypotkey" /></label>
	<c:set var="PROP_HONEYPOT_ACCESS_KEY"><%= Environment.PROP_HONEYPOT_ACCESS_KEY %></c:set>
	<span><jamwiki:text name="${PROP_HONEYPOT_ACCESS_KEY}" value="${props[PROP_HONEYPOT_ACCESS_KEY]}" size="60" id="${PROP_HONEYPOT_ACCESS_KEY}" /></span>
	<div class="formhelp"><fmt:message key="admin.spam.help.honeypotkey" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_RECAPTCHA_EDIT %>"><fmt:message key="admin.spam.caption.recaptchaedit" /></label>
	<c:set var="PROP_RECAPTCHA_EDIT"><%= Environment.PROP_RECAPTCHA_EDIT %></c:set>
	<span>
		<select name="<%= Environment.PROP_RECAPTCHA_EDIT %>" id="<%= Environment.PROP_RECAPTCHA_EDIT %>">
		<c:set var="PROP_RECAPTCHA_EDIT"><%= Environment.PROP_RECAPTCHA_EDIT %></c:set>
		<option value="0"<c:if test="${props[PROP_RECAPTCHA_EDIT] == 0}"> selected="selected"</c:if>><fmt:message key="admin.spam.option.never" /></option>
		<option value="1"<c:if test="${props[PROP_RECAPTCHA_EDIT] == 1}"> selected="selected"</c:if>><fmt:message key="admin.spam.option.unregistered" /></option>
		<option value="2"<c:if test="${props[PROP_RECAPTCHA_EDIT] == 2}"> selected="selected"</c:if>><fmt:message key="admin.spam.option.always" /></option>
		</select>
	</span>
	<div class="formhelp"><fmt:message key="admin.spam.help.recaptcha" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_RECAPTCHA_REGISTER %>"><fmt:message key="admin.spam.caption.recaptcharegister" /></label>
	<c:set var="PROP_RECAPTCHA_REGISTER"><%= Environment.PROP_RECAPTCHA_REGISTER %></c:set>
	<span>
		<select name="<%= Environment.PROP_RECAPTCHA_REGISTER %>" id="<%= Environment.PROP_RECAPTCHA_REGISTER %>">
		<c:set var="PROP_RECAPTCHA_REGISTER"><%= Environment.PROP_RECAPTCHA_REGISTER %></c:set>
		<option value="0"<c:if test="${props[PROP_RECAPTCHA_REGISTER] == 0}"> selected="selected"</c:if>><fmt:message key="admin.spam.option.never" /></option>
		<option value="2"<c:if test="${props[PROP_RECAPTCHA_REGISTER] == 2}"> selected="selected"</c:if>><fmt:message key="admin.spam.option.always" /></option>
		</select>
	</span>
	<div class="formhelp"><fmt:message key="admin.spam.help.recaptcha" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_RECAPTCHA_PUBLIC_KEY %>"><fmt:message key="admin.spam.caption.recaptchapublic" /></label>
	<c:set var="PROP_RECAPTCHA_PUBLIC_KEY"><%= Environment.PROP_RECAPTCHA_PUBLIC_KEY %></c:set>
	<span><jamwiki:text name="${PROP_RECAPTCHA_PUBLIC_KEY}" value="${props[PROP_RECAPTCHA_PUBLIC_KEY]}" size="60" id="${PROP_RECAPTCHA_PUBLIC_KEY}" /></span>
	<div class="formhelp"><fmt:message key="admin.spam.help.recaptchakey" /></div>
</div>
<div class="row">
	<label for="<%= Environment.PROP_RECAPTCHA_PRIVATE_KEY %>"><fmt:message key="admin.spam.caption.recaptchaprivate" /></label>
	<c:set var="PROP_RECAPTCHA_PRIVATE_KEY"><%= Environment.PROP_RECAPTCHA_PRIVATE_KEY %></c:set>
	<span><jamwiki:text name="${PROP_RECAPTCHA_PRIVATE_KEY}" value="${props[PROP_RECAPTCHA_PRIVATE_KEY]}" size="60" id="${PROP_RECAPTCHA_PRIVATE_KEY}" /></span>
	<div class="formhelp"><fmt:message key="admin.spam.help.recaptchakey" /></div>
</div>
</fieldset>
</div>

<div id="other" class="submenu-tab-item">
<%-- BEGIN RSS --%>
<fieldset>
<legend><fmt:message key="admin.header.rss" /></legend>
<div class="row">
	<label for="<%= Environment.PROP_RSS_ALLOWED %>"><fmt:message key="admin.rss.caption.allowed" /></label>
	<c:set var="PROP_RSS_ALLOWED"><%= Environment.PROP_RSS_ALLOWED %></c:set>
	<span><jamwiki:checkbox name="${PROP_RSS_ALLOWED}" value="true" checked="${props[PROP_RSS_ALLOWED]}" id="${PROP_RSS_ALLOWED}" onclick="onRSS()" /></span>
</div>
<div class="row">
	<label for="<%= Environment.PROP_RSS_TITLE %>"><fmt:message key="admin.rss.caption.title" /></label>
	<c:set var="PROP_RSS_TITLE"><%= Environment.PROP_RSS_TITLE %></c:set>
	<span><jamwiki:text name="${PROP_RSS_TITLE}" id="${PROP_RSS_TITLE}" value="${props[PROP_RSS_TITLE]}" size="50" /></span>
</div>
</fieldset>
</div>

<input type="hidden" name="function" value="properties" />

<%--
  Include a hidden (display:none) password field to prevent Firefox from trying to change the
  admin password.  There is currently (version 1.5 and before) an issue with Firefox where
  anytime two or more password fields are in a form it assumes the password is being
  changed if the last password is different from the saved password.
--%>

<input type="password" name="fakePassword" value="" style="display:none" />
</form>

</div>

</div>

<%@ include file="shared-db-javascript.jsp" %>

<script type="text/javascript">
// <![CDATA[
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
function onRSS() {
	var disabled = true;
	if (document.getElementById("<%= Environment.PROP_RSS_ALLOWED %>").checked) {
		disabled = false;
	}
	document.getElementById("<%= Environment.PROP_RSS_TITLE %>").disabled = disabled;
}
// initialize
onUploadType();
onRSS();
JAMWiki.Admin.toggleDisableOnSelect(document.getElementById("<%= Environment.PROP_BASE_PERSISTENCE_TYPE %>"), "<%= WikiBase.PERSISTENCE_EXTERNAL %>", DATABASE_ELEMENT_IDS, document.getElementById("db-details"), "expander-open");
JAMWiki.Admin.sampleDatabaseValues(document.getElementById("<%= Environment.PROP_DB_TYPE %>"), "<%= Environment.PROP_DB_DRIVER %>", "<%= Environment.PROP_DB_URL %>", DATABASE_SAMPLE_VALUES);
JAMWiki.Admin.toggleDisableOnSelect(document.getElementById("<%= Environment.PROP_FILE_UPLOAD_STORAGE %>"), "<%= WikiBase.UPLOAD_STORAGE.DOCROOT %>", UPLOAD_ELEMENT_IDS, document.getElementById("upload-details"), "expander-open");
// ]]>
</script>
