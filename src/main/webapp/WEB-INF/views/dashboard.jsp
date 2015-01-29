<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<jsp:include page="includes/header.jsp" />

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/dashboard.js"></script>

	
	<section id="main">
		<div class="container">
			<h1 id="homeTitle">${page.nbResults} Computers found</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="dashboard" method="GET" class="form-inline">
						<input type="hidden" name="nbResults" value="${page.nbResultsPerPage}">
						<input type="search" id="searchbox" name="search"
							class="form-control" placeholder="Search name" /> <input
							type="submit" id="searchsubmit" value="Filter by name"
							class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="add-computer" href="add-computer">Add
						Computer</a> <a class="btn btn-default" id="edit-computer" href="#"
						onclick="$.fn.toggleEditMode();">Edit</a>
				</div>
			</div>
		</div>

		<form id="deleteForm" action="delete" method="POST">
			<input type="hidden" name="selection" value="">
		</form>

		<div class="container" style="margin-top: 10px;">
			<table class="table table-striped table-bsorted">
				<thead>
					<tr>
						<!-- Variable declarations for passing labels as parameters -->
						<!-- Table header for Computer Name -->

						<th class="editMode" style="width: 60px; height: 22px;"><input
							type="checkbox" id="selectall" /> <span
							style="vertical-align: top;"> - <a href="#"
								id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
									class="fa fa-trash-o fa-lg"></i>
							</a>
						</span></th>
						<%String[][] columns = {{"name","Computer name"}, {"introduced", "Introduced date"}, 
								{"discontinued", "Discontinued date"}, {"company_name","Company name"}}; 
							pageContext.setAttribute("columns", columns);%>
						<c:forEach items="${columns}" var="col">
							<c:choose>
							<c:when test="${col[0].equals(page.sort.id) && page.order.equals(\"ASC\") }">
								<th><h:link target="dashboard" limit="${page.nbResultsPerPage}" search="${page.search}" sort="${col[0]}" order="desc">${col[1]}</h:link></th>
							</c:when>
							<c:when test="${col[0].equals(page.sort.id) && page.order.equals(\"DESC\") }">
								<th><h:link target="dashboard" limit="${page.nbResultsPerPage}" search="${page.search}" sort="${col[0]}" order="asc">${col[1]}</h:link></th>
							</c:when>
							<c:otherwise>
								<th><h:link target="dashboard" limit="${page.nbResultsPerPage}" search="${page.search}" sort="${col[0]}" order="asc">${col[1]}</h:link></th>
							</c:otherwise>
							</c:choose>
						</c:forEach>				
						</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">
				<c:forEach items="${page.list}" var="computer">
					<tr>
						<td class="editMode"><input type="checkbox" name="cb" class="cb" value="${computer.id}"></td>
						<td><a href="edit-computer?id=${computer.id}"><c:out value="${computer.name}"/> </a></td>
						<td>${computer.introducedDate}</td>
						<td>${computer.discontinuedDate}</td>
						<td>${computer.company.name}</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<h:pagination target="dashboard" pages="${page.nbPages}" pageNumber="${page.pageNumber}" limit="${page.nbResultsPerPage}" search="${page.search}" sort="${page.sort.id}" order="${page.order}"/>
		</div>
	</footer>

</body>
</html>