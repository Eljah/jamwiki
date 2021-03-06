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
        org.jamwiki.utils.Utilities
    "
    errorPage="/WEB-INF/jsp/error.jsp"
    contentType="text/html; charset=UTF-8"
%>

<%@ include file="page-init.jsp" %>

<ul>
<c:forEach items="${wikis}" var="wiki">
<li><a href="../<c:out value="${wiki}"/>/<%= Utilities.encodeURL(Environment.getValue(Environment.PROP_BASE_DEFAULT_TOPIC)) %>"><c:out value="${wiki}"/></a></li>
</c:forEach>
</ul>
