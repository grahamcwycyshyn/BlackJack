<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<meta charset="ISO-8859-1">
<title>Log In</title>
<link href="/login-form.css" rel="stylesheet" />
</head>
<body>

		<h1>Log In</h1>
		
		<c:if test="${ not empty message }">
				<p class="message">${ message }</p>
			</c:if>
			
			<form action="/login" method="post" autocomplete="off" >
			
			</form>

</body>
</html>