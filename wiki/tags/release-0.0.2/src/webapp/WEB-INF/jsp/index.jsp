<%--
This program is free software; you can redistribute it and/or modify
it under the terms of the latest version of the GNU Lesser General
Public License as published by the Free Software Foundation;

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program (gpl.txt); if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
--%>

<%@ page import="
        org.jamwiki.Environment,
        org.jamwiki.WikiBase
    "
    errorPage="error.jsp"
%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<c:set var="initialized"><%= Environment.getValue(Environment.PROP_BASE_INITIALIZED) %></c:set>
<c:set var="defaultTopic"><%= Environment.getValue(Environment.PROP_BASE_DEFAULT_TOPIC) %></c:set>
<c:set var="defaultVirtualWiki"><%= WikiBase.DEFAULT_VWIKI %></c:set>
<c:choose>
	<c:when test="${initialized && !empty defaultTopic}">
		<%-- FIXME - URL escape defaultTopic --%>
		<c:redirect url="${defaultVirtualWiki}/${defaultTopic}" />
	</c:when>
	<c:otherwise>
		<%-- FIXME - remove hard-coding, support virtual wiki, URL escape defaultTopic --%>
		<c:redirect url="${defaultVirtualWiki}/Special:Setup" />
	</c:otherwise>
</c:choose>
