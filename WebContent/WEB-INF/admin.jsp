<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>Weekop</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link
	href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css"
	type="text/css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/styles.css"
	type="text/css" rel="stylesheet">
</head>

<body>

	<jsp:include page="fragment/navbar.jspf" />

	<c:if test="${not empty requestScope.discoveries}">
		<c:forEach var="discovery" items="${requestScope.discoveries}">
			<div class="container">
				<div class="row bs-callout bs-callout-primary">
					<div class="col col-md-1 col-sm-2">
						<div class="well well-sm centered">
							<c:out value="${discovery.upVote - discovery.downVote}" />
						</div>
					</div>
				<form 	action="admin" method="post">
					<div class="col col-md-11 col-sm-10">
						<h3 class="centered">
							<input name="inputName" type="text" value="${discovery.name}"/>
							
						</h3>
						<h6>
							<small>Dodane przez: <c:out
									value="${discovery.user.username}" />, Dnia: <fmt:formatDate
									value="${discovery.timestamp}" pattern="dd/mm/YYYY" /></small>
						</h6>
						<p>
							<input name="inputDescription" type="text"
								value="${discovery.description}" />
						<p>
							<input hidden="true" name="id" value="${discovery.id}"></input>

							Edytuj: <input checked="checked" type="radio" name="option" value="edit">
							Usuń: <input type="radio" name="option" value="delete">
							<input class="btn btn-default btn-xs" type="submit" value="Wykonaj!" />
			 					
					</div>
					</form>
				</div>
			</div>
		</c:forEach>
	</c:if>

	<jsp:include page="fragment/footer.jspf" />

	<script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
	<script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
	<script src="resources/js/bootstrap.js"></script>
</body>
</html>