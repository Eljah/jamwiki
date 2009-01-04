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
<%@ page errorPage="/WEB-INF/jsp/error.jsp"
    contentType="text/html; charset=utf-8"
%>

<%@ include file="page-init.jsp" %>

<c:if test="${!empty editResolve}">
<div class="message red"><f:message key="edit.exception.conflict" /></div>
</c:if>

<c:if test="${!empty topicVersionId}"><div class="message red"><f:message key="edit.warning.oldversion" /></div></c:if>

<c:if test="${!empty editSpam}">
<div class="message red"><f:message key="${spam.key}"><f:param value="${spam.params[0]}" /></f:message></div>
</c:if>
<jamwiki:enabled property="PROP_TOPIC_USE_PREVIEW">
<c:if test="${!empty editPreview}">
<div class="message red"><f:message key="edit.warning.preview" /></div>
</c:if>
</jamwiki:enabled>

<%@ include file="category-include.jsp" %>

<jamwiki:enabled property="PROP_TOPIC_USE_PREVIEW">
<c:if test="${!empty editPreview}">
<%@ include file="view-topic-include.jsp" %>
</c:if>
</jamwiki:enabled>

<fieldset>
<legend><f:message key="topic.caption.editlegend" /></legend>

<form name="form" method="post" name="editform" action="<jamwiki:link value="Special:Edit" />">
<input type="hidden" name="topic" value="<c:out value="${pageInfo.topicName}"/>" />
<input type="hidden" name="lastTopicVersionId" value="<c:out value="${lastTopicVersionId}"/>" />
<input type="hidden" name="section" value="<c:out value="${section}"/>" />
<input type="hidden" name="topicVersionId" value="<c:out value="${topicVersionId}"/>" />

<c:choose>
	<c:when test="${editor == 'toolbar'}">
		<%@ include file="editor-toolbar-include.jsp" %>
	</c:when>
</c:choose>

<p>
<textarea id="topicContents" name="contents" rows="25" cols="80" accesskey=","><c:out value="${contents}" escapeXml="true" /></textarea>
</p>
<p><label for="editComment"><f:message key="edit.caption.comment" /></label>: <input type="text" name="editComment" value="<c:out value="${editComment}" />" size="60" id="editComment" /></p>
<p>
<input type="submit" name="save" value="<f:message key="common.save"/>"  accesskey="s"/>

<jamwiki:enabled property="PROP_TOPIC_USE_PREVIEW"><input type="submit" name="preview" value="<f:message key="edit.action.preview"/>" accesskey="p"/></jamwiki:enabled>

&nbsp;&nbsp;&nbsp;
<input type="checkbox" value="true" name="minorEdit"<c:if test="${minorEdit}"> checked</c:if> id="minorEdit" accesskey="i" />
<label for="minorEdit"><f:message key="edit.caption.minor" /></label>
<security:authorize ifNotGranted="ROLE_ANONYMOUS">
&nbsp;&nbsp;&nbsp;
<input type="checkbox" value="true" name="watchTopic"<c:if test="${watchTopic}"> checked</c:if> id="watchTopic" accesskey="w" />
<label for="watchTopic"><f:message key="edit.caption.watch" /></label>
</security:authorize>
</p>

<c:if test="${!empty editResolve}">
<%@ include file="diff-include.jsp" %>
<p>
<textarea name="contentsResolve" rows="25" cols="80"><c:out value="${contentsResolve}" escapeXml="true" /></textarea>
</p>
</c:if>

</form>

</fieldset>
