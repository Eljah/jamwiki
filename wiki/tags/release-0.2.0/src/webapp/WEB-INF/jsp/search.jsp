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

<form name="searchForm" method="post" action="<jamwiki:link value="Special:Search" />">
<label for="searchTerm"><f:message key="search.for"/></label><input type="text" name="text" value="<c:out value="${text}" />" id="searchTerm" />  <input type="submit" name="Submit" value="<f:message key="search.search"/>" />
<p>&nbsp;</p>
<f:message key="search.hints"/>
<input type="hidden" name="action" value="<%= JAMWikiServlet.ACTION_SEARCH %>" />
</form>
<p>&nbsp;</p>
<font size="-1"><i><f:message key="search.poweredby" /></i></font> <a href="http://lucene.apache.org/java/"><img src="../images/lucene_green_100.gif" alt="Lucene" border="0" /></a>
<script language="JavaScript">document.searchForm.text.focus();</script>
