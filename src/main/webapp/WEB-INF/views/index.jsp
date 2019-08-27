<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<link href="/login-form.css" rel="stylesheet" />

<link href="/login.css" rel="stylesheet" />

</head>
<body>
<h1>Blackjack Log In</h1>
	
	<c:if test="${ not empty message }">
		<p class="message">${ message }</p>
	</c:if>
	<div class="container">
	
	<form action="/login-confirmation" method="post" autocomplete="off" >
	
	<p>
		<strong><label>Name</label></strong>
	</p>
	<p>
		<input type="text" placeholder="Enter name" name="name" required>
	</p>
	
	
	<p>
		<button type="submit">Submit</button>
	</p>
		
	</form>
	</div>


<!--  <a href="/game?id=${ deck.id }"><button>Play!</button></a> -->

</body>
</html>