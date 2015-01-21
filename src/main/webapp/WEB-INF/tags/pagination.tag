<%@ tag body-content="empty" %>
<%@ attribute name="target" required="true" %>
<%@ attribute name="pages" required="true" %>
<%@ attribute name="order" required="true" %>
<%@ attribute name="search" required="true" %>
<%@ attribute name="limit" required="true" %>
<%@ attribute name="pageNumber" required="true" %>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="h" tagdir="/WEB-INF/tags" %>

<ul class="pagination">
	<c:if test="${pageNumber > 1}">
		<li><h:link target="dashboard" limit="${limit}" search="${search}" order="${order}">First</h:link></li>
		<li><h:link target="dashboard" page="${pageNumber-1}" limit="${limit}" search="${search}" order="${order}">&laquo;</h:link></li>
	</c:if>
	<c:if test="${pageNumber-3 > 0}">
		<li><h:link target="dashboard" page="${pageNumber-3}" limit="${limit}" search="${search}" order="${order}">${pageNumber-3}</h:link></li>
	</c:if>
	<c:if test="${pageNumber-2 > 0}">
		<li><h:link target="dashboard" page="${pageNumber-2}" limit="${limit}" search="${search}" order="${order}">${pageNumber-2}</h:link></li>
	</c:if>
	<c:if test="${pageNumber-1 > 0}">
		<li><h:link target="dashboard" page="${pageNumber-1}" limit="${limit}" search="${search}" order="${order}">${pageNumber-1}</h:link></li>
	</c:if>
	<c:forEach begin="${pageNumber}" end="${pageNumber+3}" var="i">
		<c:if test="${i <= pages}">
			<c:choose>
				<c:when test="${i == pageNumber}">
				<li class="disabled"><span>${i}</span></li>
				</c:when>
				<c:otherwise>
				<li><h:link target="dashboard" page="${i}" limit="${limit}" search="${search}" order="${order}">${i}</h:link></li>
				</c:otherwise>
			</c:choose>
		</c:if>
	</c:forEach>
		<c:if test="${pageNumber < pages}">
		<li><h:link target="dashboard" page="${pageNumber+1}" limit="${limit}" search="${search}" order="${order}">&raquo;</h:link></li>
		<li><h:link target="dashboard" page="${pages}" limit="${limit}" search="${search}" order="${order}">Last</h:link></li>
	</c:if>
</ul>

<div class="btn-group btn-group-sm pull-right" role="group">
	<h:link target="dashboard" limit="10" search="${search}" order="${order}" classes="btn btn-default">10</h:link>
	<h:link target="dashboard" limit="25" search="${search}" order="${order}" classes="btn btn-default">25</h:link>
	<h:link target="dashboard" limit="50" search="${search}" order="${order}" classes="btn btn-default">50</h:link>
	<h:link target="dashboard" limit="100" search="${search}" order="${order}" classes="btn btn-default">100</h:link>
</div>