<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Done</title>
</head>
<body>
    <jsp:include page="fragment/navbar.jspf" />
 
    <h1>Wynik zapytania <%= request.getAttribute("option") %></h1>
    <p>Twoje zapytanie się powiodło!</p>
    <p>Tytuł: <%= request.getAttribute("inputName") %><br>
    <a href="http://localhost:8080/weekop15/admin">Wróć</a>
</p>
    
    	<jsp:include page="fragment/footer.jspf" />

	<script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
	<script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
	<script src="resources/js/bootstrap.js"></script>
</body>
</html>