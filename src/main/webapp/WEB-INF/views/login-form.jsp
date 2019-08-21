<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link href="/login-form.css" rel="stylesheet" />
</head>
<body>

	<h1>Log in</h1>
	
	<c:if test="${ not empty message }">
		<p class="message">${ message }</p>
	</c:if>
	
	<form action="/login" method="post" autocomplete="off" >
	
	<p>
		<label>Name</label> <input type="name" name="name" required />
	</p>
	
	
	<p>
		<button type="submit">Log in</button>
	</p>
	
	</form>

</body>
</html>