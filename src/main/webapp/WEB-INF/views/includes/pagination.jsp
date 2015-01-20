<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.excilys.computerdatabase.domain.Page"%>


<ul class="pagination">
	<c:if test="${page.pageNumber != 1}">
		<li><a href="dashboard?page=1&nbResults=${page.nbResultsPerPage}&search=${page.search}&order=${page.order}"
			aria-label="First"> 
			<span aria-hidden="true">First</span>
		</a></li>
		<li><a href="dashboard?page=${page.pageNumber-1}&nbResults=${page.nbResultsPerPage}&search=${page.search}&order=${page.order}" 
			aria-label="Previous"> 
			<span aria-hidden="true">&laquo;</span>
		</a></li>
	</c:if>
	<c:if test="${page.pageNumber-3 > 0}">
		<li><a href="dashboard?page=${page.pageNumber-3}&nbResults=${page.nbResultsPerPage}&search=${page.search}&order=${page.order}">${page.pageNumber-3}</a></li>
	</c:if>
	<c:if test="${page.pageNumber-2 > 0}">
		<li><a href="dashboard?page=${page.pageNumber-2}&nbResults=${page.nbResultsPerPage}&search=${page.search}&order=${page.order}">${page.pageNumber-2}</a></li>
	</c:if>
	<c:if test="${page.pageNumber-1 > 0}">
		<li><a href="dashboard?page=${page.pageNumber-1}&nbResults=${page.nbResultsPerPage}&search=${page.search}&order=${page.order}">${page.pageNumber-1}</a></li>
	</c:if>
	<c:forEach begin="${page.pageNumber}" end="${page.pageNumber+3}" var="i">
		<c:if test="${i <= page.nbPages}">
			<c:choose>
				<c:when test="${i == page.pageNumber}">
				<li class="disabled"><span>${i}</span></li>
				</c:when>
				<c:otherwise>
				<li><a href="dashboard?page=${i}&nbResults=${page.nbResultsPerPage}&search=${page.search}&order=${page.order}" >${i}</a></li>
				</c:otherwise>
			</c:choose>
		</c:if>
	</c:forEach>
		<c:if test="${page.pageNumber != page.nbPages}">
		<li><a href="dashboard?page=${page.pageNumber+1}&nbResults=${page.nbResultsPerPage}&search=${page.search}&order=${page.order}"
			aria-label="Next"> 
			<span aria-hidden="true">&raquo;</span>
		</a></li>
		<li><a href="dashboard?page=${page.nbPages}&nbResults=${page.nbResultsPerPage}&search=${page.search}&order=${page.order}"
			aria-label="Last"> 
			<span aria-hidden="true">Last</span>
		</a></li>
	</c:if>
</ul>

<div class="btn-group btn-group-sm pull-right" role="group">
	<a type="button" class="btn btn-default" href="dashboard?page=1&nbResults=10&search=${page.search}&order=${page.order}">10</a>
	<a type="button" class="btn btn-default" href="dashboard?page=1&nbResults=25&search=${page.search}&order=${page.order}">25</a>
	<a type="button" class="btn btn-default" href="dashboard?page=1&nbResults=50&search=${page.search}&order=${page.order}">50</a>
	<a type="button" class="btn btn-default" href="dashboard?page=1&nbResults=100&search=${page.search}&order=${page.order}">100</a>
</div>