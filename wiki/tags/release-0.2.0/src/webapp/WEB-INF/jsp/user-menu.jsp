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

<table class="menu-user-table">
<tr>
<c:choose>
<c:when test="${empty user}">
	<td class="menu-user"><jamwiki:link value="Special:Login"><f:message key="common.login" /></jamwiki:link> / <jamwiki:link value="Special:Account"><f:message key="usermenu.register" /></jamwiki:link></td>
</c:when>
<c:otherwise>
	<td class="menu-user"><jamwiki:link value="${userpage}"><c:if test="${!empty user.displayName}"><c:out value="${user.displayName}" /></c:if><c:if test="${empty user.displayName}"><c:out value="${user.login}" /></c:if></jamwiki:link></td>
	<td class="menu-user"><jamwiki:link value="${usercomments}"><f:message key="usermenu.usercomments" /></jamwiki:link></td>
	<td class="menu-user"><jamwiki:link value="Special:Account"><f:message key="usermenu.account" /></jamwiki:link></td>
	<td class="menu-user"><jamwiki:link value="Special:Logout"><f:message key="common.logout" /></jamwiki:link></td>
	<c:if test="${adminUser}"><td class="menu-user"><jamwiki:link value="Special:Admin"><f:message key="usermenu.admin" /></jamwiki:link></c:if>
</c:otherwise>
</c:choose>
</tr>
</table>
