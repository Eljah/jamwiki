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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title><f:message key="${pageInfo.pageTitle.key}"><f:param value="${pageInfo.pageTitle.params[0]}" /></f:message> - <f:message key="common.sitename" /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="DC.Title" content="<f:message key="${pageInfo.pageTitle.key}"><f:param value="${pageInfo.pageTitle.params[0]}" /></f:message> - <f:message key="common.sitename" />" />
<c:if test="${!empty pageInfo.metaDescription}">
	<meta name="description" content="<c:out value="${pageInfo.metaDescription}" />" />
</c:if>
<c:if test="${!empty pageInfo.defaultTopic}">
	<link rel="start" title="<c:out value="${pageInfo.defaultTopic}" />" href="<jamwiki:link value="${pageInfo.defaultTopic}" />" />
	<link rel="home" title="<c:out value="${pageInfo.defaultTopic}" />" href="<jamwiki:link value="${pageInfo.defaultTopic}" />" />
</c:if>
	<link href="<jamwiki:link value="jamwiki.css" />" type="text/css" rel="stylesheet" />
</head>
<body>