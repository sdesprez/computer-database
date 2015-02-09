<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="includes/header.jsp" />
<script type="text/javascript" src="js/jquery.min.js"></script>	
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<style>
.error{
	color: red;
}
</style>


    <section id="main">
    
        <div class="container">
        	<span style="float: right"> 
				<a href="?lang=en">en</a> 
				| 
				<a href="?lang=fr">fr</a>
			</span>
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <h1><spring:message code="title.add"/></h1>

                    
                    <form:form id="form" action="add" method="POST" commandName="computerDTO">
                        <fieldset>
                            <div class="form-group">
                            	<spring:message code="placeholder.name" var="placeholder"/>
                                <label for="computerName"><spring:message code="label.name"/></label>
                                <form:input path="name" type="text" class="form-control" id="computerName" placeholder="${placeholder}" required="required"/>
                            	<form:errors path="name" cssClass="error"/>
                            </div>
                            <div class="form-group">
                                <label for="introduced"><spring:message code="label.introduced"/></label>
                                <form:input path="introduced" type="date" class="form-control" id="introduced" placeholder="yyyy-MM-dd"/>
                            	<form:errors path="introduced" cssClass="error"/>
                            </div>
                            <div class="form-group">
                                <label for="discontinued"><spring:message code="label.discontinued"/></label>
                                <form:input path="discontinued" class="form-control" id="discontinued" placeholder="yyyy-MM-dd"/>
                            	<form:errors path="discontinued" cssClass="error"/>
                            </div>
                            <div class="form-group">
                                <label for="company"><spring:message code="label.company"/></label>
                                <form:select path="company" class="form-control" id="company">
                                    <option value="0">--</option>
                                    <c:forEach items="${companies}" var="company">
                                   	<c:choose>
                                   		<c:when test="${company.id == computerDTO.company}">
                                   			<option value="${company.id}" selected="selected">${company.name}</option>
                                 		</c:when>
                                   		<c:otherwise>
                                   			<option value="${company.id}"><c:out value="${company.name}"/></option>
                                   		</c:otherwise>
                                  	</c:choose>
                                   	</c:forEach>
                                </form:select>
                                <form:errors path="company" cssClass="error"/>
                            </div>                  
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="<spring:message code="button.add"/>" class="btn btn-primary">
                            <spring:message code="text.or"/>
                            <a href="dashboard" class="btn btn-default"><spring:message code="button.cancel"/></a>
                        </div>
                    </form:form>
                    
                </div>
            </div>
        </div>
    </section>
    <script type="text/javascript">
    $("#form").validate({
		messages: {
            name: "<spring:message code='error.name' javaScriptEscape='true'/>",
            introduced: "<spring:message code='error.date' javaScriptEscape='true'/>",
            discontinued: "<spring:message code='error.date' javaScriptEscape='true'/>"
        }
	});
	</script>
</body>
</html>