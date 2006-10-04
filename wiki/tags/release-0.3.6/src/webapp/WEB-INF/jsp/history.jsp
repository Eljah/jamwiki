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

<script type="text/javascript">
// enable/disable checkboxes before or after the current element
<%-- FIXME: might be better moved to a jamwiki.js file --%>
function inactive(element) {
  var found = 0;
  var totalChecked = 0;
  for (i=0; i < document.diffForm.length; i++) {
    if (element.type != document.diffForm.elements[i].type) continue;
    if (document.diffForm.elements[i].checked) totalChecked++;
  }
  for (i=0; i < document.diffForm.length; i++) {
    if (element.type != document.diffForm.elements[i].type) continue;
    if (document.diffForm.elements[i].checked && found < 2) {
      found++;
      continue;
    }
    if (totalChecked == 0) {
      // enable everything
      document.diffForm.elements[i].checked = false;
      document.diffForm.elements[i].disabled = false;
      continue;
    }
    if (found == 0 && totalChecked == 1) {
      // disable everything up to the first one
      document.diffForm.elements[i].checked = false;
      document.diffForm.elements[i].disabled = true;
      continue;
    }
    if (found == 1 && totalChecked >= 1) {
      // un-select everything after the first one
      document.diffForm.elements[i].checked = false;
      document.diffForm.elements[i].disabled = false;
      continue;
    }
    if (found == 2 && totalChecked >= 2) {
      // disable elements after the second one
      document.diffForm.elements[i].checked = false;
      document.diffForm.elements[i].disabled = true;
      continue;
    }
  }
}
</script>

<div id="change">

<p><f:message key="common.caption.view" />: <jamwiki:pagination total="${numChanges}" rootUrl="Special:History?topic=${pageInfo.topicName}" /></p>

<form action="<jamwiki:link value="Special:History" />" method="get" name="historyForm">
<input type="hidden" name="topic" value='<c:out value="${pageInfo.topicName}"/>'/>

</form>

<form action="<jamwiki:link value="Special:Diff" />" method="get" name="diffForm">
<input type="hidden" name="type" value="arbitrary"/>
<input type="hidden" name="topic" value='<c:out value="${pageInfo.topicName}" />' />

<input type="submit" value='<f:message key="history.diff" />' />

<br /><br />

<ul>
<c:forEach items="${changes}" var="change">
<li<c:if test="${change.delete}"> class="deletechange"</c:if><c:if test="${change.minor}"> class="minorchange"</c:if><c:if test="${change.undelete}"> class="undeletechange"</c:if><c:if test="${change.move}"> class="movechange"</c:if><c:if test="${change.normal}"> class="standardchange"</c:if>>
	&#160;
	<input type="checkbox" name="<c:out value="diff:${change.topicVersionId}" />" onclick="inactive(this)" id="<c:out value="diff:${change.topicVersionId}" />" />
	&#160;
	<%-- FIXME: do not hardcode date pattern --%>
	<jamwiki:link value="Special:History"><jamwiki:linkParam key="topicVersionId" value="${change.topicVersionId}" /><jamwiki:linkParam key="topic" value="${pageInfo.topicName}" /><f:formatDate value="${change.editDate}" type="both" pattern="dd-MMM-yyyy HH:mm" /></jamwiki:link>
	&#160;.&#160;.&#160;
	<%-- FIXME: ugly --%>
	<jamwiki:link value="User:${change.authorName}" text="${change.authorName}" />
	(<jamwiki:link value="User comments:${change.authorName}"><f:message key="recentchanges.caption.comments" /></jamwiki:link>&#160;|&#160;<jamwiki:link value="Special:Contributions"><jamwiki:linkParam key="contributor" value="${change.authorName}" /><f:message key="recentchanges.caption.contributions" /></jamwiki:link>)
	<c:if test="${!empty change.changeTypeNotification}">&#160;<b><c:out value="${change.changeTypeNotification}" /></b></c:if>
	<c:if test="${!empty change.editComment}">
	<label for="<c:out value="diff:${change.topicVersionId}" />">&#160;(<i><c:out value="${change.editComment}" /></i>)</label>
	</c:if>
</li>
</c:forEach>
</ul>

<br />

<input type="submit" value='<f:message key="history.diff"/>'/>
</form>

</div>
