<%@ tag body-content="empty" %>
<%@ attribute name="target" required="true" %>
<%@ attribute name="pages" required="true" type="java.lang.Integer" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="search" required="true" %>
<%@ attribute name="limit" required="true" %>
<%@ attribute name="order" required="true" %>
<%@ attribute name="pageNumber" required="true" type="java.lang.Integer"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="h" tagdir="/WEB-INF/tags" %>

<ul class="pagination">
	<c:if test="${pageNumber > 1}">
		<li><h:link target="${target}" limit="${limit}" search="${search}" sort="${sort}" order="${order}"><spring:message code="button.first"/></h:link></li>
		<li><h:link target="${target}" page="${pageNumber-1}" limit="${limit}" search="${search}" sort="${sort}" order="${order}">&laquo;</h:link></li>
	</c:if>
	<c:if test="${pageNumber-3 > 0}">
		<li><h:link target="${target}" page="${pageNumber-3}" limit="${limit}" search="${search}" sort="${sort}" order="${order}">${pageNumber-3}</h:link></li>
	</c:if>
	<c:if test="${pageNumber-2 > 0}">
		<li><h:link target="${target}" page="${pageNumber-2}" limit="${limit}" search="${search}" sort="${sort}" order="${order}">${pageNumber-2}</h:link></li>
	</c:if>
	<c:if test="${pageNumber-1 > 0}">
		<li><h:link target="${target}" page="${pageNumber-1}" limit="${limit}" search="${search}" sort="${sort}" order="${order}">${pageNumber-1}</h:link></li>
	</c:if>
	<c:forEach begin="${pageNumber}" end="${pageNumber+3}" var="i">
		<c:if test="${i <= pages}">
			<c:choose>
				<c:when test="${i == pageNumber}">
				<li class="active"><span>${i}</span></li>
				</c:when>
				<c:otherwise>
				<li><h:link target="${target}" page="${i}" limit="${limit}" search="${search}" sort="${sort}" order="${order}">${i}</h:link></li>
				</c:otherwise>
			</c:choose>
		</c:if>
	</c:forEach>
	<c:if test="${pageNumber < pages}">
		<li><h:link target="${target}" page="${pageNumber+1}" limit="${limit}" search="${search}" sort="${sort}" order="${order}">&raquo;</h:link></li>
		<li><h:link target="${target}" page="${pages}" limit="${limit}" search="${search}" sort="${sort}" order="${order}"><spring:message code="button.last"/></h:link></li>
	</c:if>
</ul>

<div class="btn-group btn-group-sm pull-right" role="group">
	<h:link target="${target}" limit="10" search="${search}" sort="${sort}"  order="${order}" classes="btn btn-default">10</h:link>
	<h:link target="${target}" limit="25" search="${search}" sort="${sort}"  order="${order}" classes="btn btn-default">25</h:link>
	<h:link target="${target}" limit="50" search="${search}" sort="${sort}"  order="${order}" classes="btn btn-default">50</h:link>
	<h:link target="${target}" limit="100" search="${search}" sort="${sort}"  order="${order}" classes="btn btn-default">100</h:link>
</div>