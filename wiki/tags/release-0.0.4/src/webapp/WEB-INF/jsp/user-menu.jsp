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
	<%-- FIXME: do not hardcode --%>
<c:choose>
<c:when test="${empty user}">
	<td class="menu-user"><a href="<jamwiki:link value="Special:Login" />">Login</a> / <a href="<jamwiki:link value="Special:Account" />">Register</a></td>
</c:when>
<c:otherwise>
	<td class="menu-user"><jamwiki:link value="${userpage}"><c:out value="${user.displayName}" /></jamwiki:link></td>
	<td class="menu-user"><jamwiki:link value="${usercomments}">User Comments</jamwiki:link></td>
	<td class="menu-user"><a href="<jamwiki:link value="Special:Account" />">Account</a></td>
	<td class="menu-user"><a href="<jamwiki:link value="Special:Logout" />">Logout</a></td>
</c:otherwise>
</c:choose>
</tr>
</table>
