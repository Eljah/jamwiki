<%@ tag body-content="empty" description="Render the standard link to a user page, talk page, and contributions" %>

<%@ taglib prefix="jamwiki" uri="http://jamwiki.org/taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="ApplicationResources" />

<%@ attribute name="pageInfo" required="true" rtexprvalue="true" type="org.jamwiki.servlets.WikiPageInfo" description="The current PageInfo object" %>
<%@ attribute name="userDisplay" required="true" rtexprvalue="true" description="The user name or IP address" %>

<jamwiki:link value="${pageInfo.namespaces[pageInfo.virtualWikiName]['User']}:${userDisplay}" text="${userDisplay}" />
(<jamwiki:link value="${pageInfo.namespaces[pageInfo.virtualWikiName]['User comments']}:${userDisplay}"><fmt:message key="recentchanges.caption.comments" /></jamwiki:link>&#160;|&#160;<jamwiki:link value="Special:Contributions"><jamwiki:linkParam key="contributor" value="${userDisplay}" /><fmt:message key="recentchanges.caption.contributions" /></jamwiki:link>)
