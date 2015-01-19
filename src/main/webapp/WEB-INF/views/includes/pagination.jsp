<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.excilys.computerdatabase.domain.Page"%>


<ul class="pagination">
	<c:if test="${page.pageNumber != 1}">
		<li><a href="dashboard?page=${page.pageNumber-1}&nbResults=${page.nbResultsPerPage}" 
			aria-label="Previous"> 
			<span aria-hidden="true">&laquo;</span>
		</a></li>
	</c:if>
	<c:if test="${page.pageNumber-3 > 0}">
		<li><a href="dashboard?page=${page.pageNumber-3}&nbResults=${page.nbResultsPerPage}">${page.pageNumber-3}</a></li>
	</c:if>
	<c:if test="${page.pageNumber-2 > 0}">
		<li><a href="dashboard?page=${page.pageNumber-2}&nbResults=${page.nbResultsPerPage}">${page.pageNumber-2}</a></li>
	</c:if>
	<c:if test="${page.pageNumber-1 > 0}">
		<li><a href="dashboard?page=${page.pageNumber-1}&nbResults=${page.nbResultsPerPage}">${page.pageNumber-1}</a></li>
	</c:if>
	<c:forEach begin="${page.pageNumber}" end="${page.pageNumber+3}" var="i">
		<c:if test="${i <= page.nbPages}">
			<li><a href="dashboard?page=${i}&nbResults=${page.nbResultsPerPage}" >${i}</a></li>
		</c:if>
	</c:forEach>
		<c:if test="${page.pageNumber != page.nbPages}">
		<li><a href="dashboard?page=${page.pageNumber+1}&nbResults=${page.nbResultsPerPage}"
			aria-label="Next"> 
			<span aria-hidden="true">&raquo;</span>
		</a></li>
	</c:if>
</ul>

<div class="btn-group btn-group-sm pull-right" role="group">
	<a type="button" class="btn btn-default" href="dashboard?page=1&nbResults=10">10</a>
	<a type="button" class="btn btn-default" href="dashboard?page=1&nbResults=25">25</a>
	<a type="button" class="btn btn-default" href="dashboard?page=1&nbResults=50">50</a>
	<a type="button" class="btn btn-default" href="dashboard?page=1&nbResults=100">100</a>
</div>