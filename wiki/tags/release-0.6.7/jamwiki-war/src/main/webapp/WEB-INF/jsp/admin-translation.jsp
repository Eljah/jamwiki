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

<div class="message"><f:message key="translation.caption.instructions" /></div>

<fieldset>
<legend><f:message key="translation.title" /></legend>

<table>
<tr>
<td>
<form name="adminTranslation" method="get" action="<jamwiki:link value="Special:Translation" />">
<input type="hidden" name="hideTranslated" value="<c:out value="${hideTranslated}" />" />
<select name="language" onchange="this.form.submit()">
<option value=""></option>
<c:forEach items="${codes}" var="code">
<option value="<c:out value="${code}" />" <c:if test="${language == code}">selected="selected"</c:if>><c:out value="${code}" /></option>
</c:forEach>
</select>
</form>
</td>
<td>&#160;&#160;&#160;&#160;&#160;</td>
<td>
<form name="adminTranslation" method="get" action="<jamwiki:link value="Special:Translation" />">
<input type="hidden" name="hideTranslated" value="<c:out value="${hideTranslated}" />" />
<input type="text" name="language" size="5" value="<c:out value="${language}" />" />&#160;&#160;<input type="submit" name="submit" value="<f:message key="common.change" />" />
</form>
</td>
</tr>
<tr>
<form name="adminTranslation" method="post" action="<jamwiki:link value="Special:Translation" />">
<input type="hidden" name="language" value="<c:out value="${language}" />" />
<td colspan="3"><input type="checkbox" name="hideTranslated" value="true" onchange="this.form.submit()" <c:if test="${hideTranslated}">checked="checked"</c:if> />&#160;<f:message key="translation.caption.hidetranslated" /></td>
</form>
</tr>
</table>

<form name="adminTranslation" method="post" action="<jamwiki:link value="Special:Translation" />">
<input type="hidden" name="language" value="<c:out value="${language}" />" />
<input type="hidden" name="hideTranslated" value="<c:out value="${hideTranslated}" />" />
<div class="formentry darkbg">
	<span class="translationElement"><f:message key="translation.caption.key" /></span>
	<span class="translationElement"><f:message key="translation.caption.translation"><f:param value="${language}" /></f:message></span>
	<div class="clear"></div>
</div>
<c:forEach items="${translations}" var="translation">
<div class="formentry <jamwiki:alternate value1="mediumbg" value2="lightbg" attributeName="translation" />">
	<span class="translationElement">
		<label for="<c:out value="${translation.key}" />">
		<code><c:out value="${translation.key}" /></code>
		<br />
		<span style="overflow:hidden"><c:out value="${defaultTranslations[translation.key]}" /></span>
		</label>
	</span>
	<span class="translationElement"><textarea name="translations[<c:out value="${translation.key}" />]" class="translation" id="<c:out value="${translation.key}" />"><c:out value="${translation.value}" /></textarea></span>
	<div class="clear"></div>
</div>
</c:forEach>
<div class="formentry">
	<span class="formcaption">&#160;</span>
	<span class="formelement"><input type="submit" name="function" value="<f:message key="common.save" />" /></span>
</div>
</form>

</fieldset>
