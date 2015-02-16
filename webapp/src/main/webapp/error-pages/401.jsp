<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
</head>
<body>
	<a href="<c:url value="/dashboard"/>"> Application - Computer Database </a>
	<section id="main">
		<div class="container">
			<div class="alert alert-danger">
				Error 401: Access Denied!
				<br/>
				<!-- stacktrace -->
			</div>
		</div>
	</section>
</body>
</html>