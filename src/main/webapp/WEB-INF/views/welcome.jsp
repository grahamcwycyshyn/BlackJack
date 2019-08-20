<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<h1>Welcome, ${ user.name } get ready to play!</h1>
	
	
	
	  <a href="/game?id=${ deck.id }"><button>Play!</button></a> 

</body>
</html>