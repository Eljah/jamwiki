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

<jamwiki:authmsg css="message red" />

<fieldset>
<legend><f:message key="${pageInfo.pageTitle.key}" /></legend>

<form method="post" action=""<jamwiki:link value="Special:EmailConfirmation" />">
<input type="hidden" name="code" value="<c:out value="${code}"/>" />
<input type="hidden" name="username" value="<c:out value="${username}"/>" />
<c:if test="${!empty errors}">
<div class="message red"><c:forEach items="${errors}" var="message"><f:message key="${message.key}"><f:param value="${message.params[0]}" /></f:message><br /></c:forEach></div>
</c:if>

<table>
<tr>
	<td><label for="registerNewPassword"><f:message key="register.caption.newpassword" /></label>:</td>
	<td><input type="password" name="newPassword" value="<c:out value="${newPassword}" />" id="registerNewPassword" /></td>
</tr>
<tr>
	<td><label for="registerConfirmPassword"><f:message key="register.caption.confirmpassword" /></label>:</td>
	<td><input type="password" name="confirmPassword" value="<c:out value="${confirmPassword}" />" id="registerConfirmPassword" /></td>
</tr>

<tr><td colspan="2" align="center"><input type="submit" name="newPasswordEntered" value="<f:message key="common.save" />"></td></tr>
</table>
</form>

</fieldset>
