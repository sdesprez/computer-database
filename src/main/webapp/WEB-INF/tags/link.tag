<%@ tag body-content="scriptless" %>
<%@ attribute name="target" required="true" %>
<%@ attribute name="page" required="false" %>
<%@ attribute name="sort" required="false" %>
<%@ attribute name="search" required="false" %>
<%@ attribute name="limit" required="false" %>
<%@ attribute name="classes" required="false" %>
<%@ attribute name="order" required="false" %>
<jsp:doBody var="body" />

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${pageScope.page == null}">
	<c:set var="page" value="1"/>
</c:if>
<c:if test="${pageScope.limit == null }">
	<c:set var="limit" value="10"/>
</c:if>
<c:if test="${pageScope.search != null }">
	<c:set var="search" value="&search=${search}"/>
</c:if>
<c:if test="${pageScope.sort != null }">
	<c:set var="sort" value="&sort=${sort}"/>
</c:if>
<c:if test="${pageScope.order != null }">
	<c:set var="order" value="&order=${order}"/>
</c:if>

<a class="${classes}" href="${pageScope.target}?page=${pageScope.page}&nbResults=${pageScope.limit}${pageScope.search}${pageScope.sort}${pageScope.order}">${body}</a>

