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

<div id="change">

<div class="message"><f:message key="common.caption.view" />: <jamwiki:pagination total="${numChanges}" rootUrl="Special:History?topic=${pageInfo.topicName}" /></div>

<form action="<jamwiki:link value="Special:History" />" method="get" name="historyForm">
<input type="hidden" name="topic" value='<c:out value="${pageInfo.topicName}"/>'/>

</form>

<form action="<jamwiki:link value="Special:Diff" />" method="get" name="diffForm">
<input type="hidden" name="topic" value='<c:out value="${pageInfo.topicName}" />' />

<input type="submit" value='<f:message key="history.diff" />' />

<br /><br />

<ul>
<c:forEach items="${changes}" var="change" varStatus="status">
<li<c:if test="${change.delete}"> class="deletechange"</c:if><c:if test="${change.minor}"> class="minorchange"</c:if><c:if test="${change.undelete}"> class="undeletechange"</c:if><c:if test="${change.move}"> class="movechange"</c:if><c:if test="${change.normal}"> class="standardchange"</c:if>>
	<c:if test="${numChanges > 1}">
	&#160;
	<input type="radio" name="version2" id="ver2_<c:out value="${change.topicVersionId}" />" onclick="historyRadio(this, 'version1', true)" value="<c:out value="${change.topicVersionId}" />" <c:if test="${status.index == 1}">checked="checked"</c:if> <c:if test="${status.first}">style="visibility:hidden"</c:if> />
	&#160;
	<input type="radio" name="version1" id="ver1_<c:out value="${change.topicVersionId}" />" onclick="historyRadio(this, 'version2', false)" value="<c:out value="${change.topicVersionId}" />" <c:if test="${status.first}">checked="checked"</c:if> <c:if test="${status.last}">style="visibility:hidden"</c:if> />
	</c:if>
	&#160;
	<%-- FIXME: do not hardcode date pattern --%>
	<jamwiki:link value="Special:History"><jamwiki:linkParam key="topicVersionId" value="${change.topicVersionId}" /><jamwiki:linkParam key="topic" value="${pageInfo.topicName}" /><f:formatDate value="${change.editDate}" type="both" pattern="dd-MMM-yyyy HH:mm" /></jamwiki:link>
	&#160;.&#160;.&#160;
	<jamwiki:link value="User:${change.authorName}" text="${change.authorName}" />
	(<jamwiki:link value="User comments:${change.authorName}"><f:message key="recentchanges.caption.comments" /></jamwiki:link>&#160;|&#160;<jamwiki:link value="Special:Contributions"><jamwiki:linkParam key="contributor" value="${change.authorName}" /><f:message key="recentchanges.caption.contributions" /></jamwiki:link>)
	<c:if test="${!empty change.changeTypeNotification}">&#160;<b><c:out value="${change.changeTypeNotification}" /></b></c:if>
	<c:if test="${!empty change.editComment}">
	<label for="<c:out value="diff:${change.topicVersionId}" />">&#160;(<i><c:out value="${change.editComment}" /></i>)</label>
	</c:if>
</li>
</c:forEach>
</ul>

<c:if test="${numChanges > 1}">
<script type="text/javascript">
historyRadio(document.getElementById('ver2_<c:out value="${changes[1].topicVersionId}" />'), 'version1', true)
</script>
</c:if>

<br />

<input type="submit" value='<f:message key="history.diff"/>'/>
</form>

</div>
