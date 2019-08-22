<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Last5</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootswatch/4.3.1/lux/bootstrap.min.css" />
<link href="/styles.css" rel="stylesheet" />
</head>
<body>
<div>
<table>
	<thead>
		<tr>
			<th>Last 5 Hands</th>
		</tr>
	</thead>
	<tbody>
			<tr>
		<c:forEach items="${hand1}" var="item1">
				<td><img class="lastfive" src="${item1.image}"></td>
		</c:forEach>
			</tr>
			<tr>
		<c:forEach items="${hand2}" var="item2">
				<td><img class="lastfive" src="${item2.image}"></td>
		</c:forEach>
			</tr>
			<tr>
		<c:forEach items="${hand3}" var="item3">
				<td><img class="lastfive" src="${item3.image}"></td>
		</c:forEach>
			</tr>
			<tr>
		<c:forEach items="${hand4}" var="item4">
				<td><img class="lastfive" src="${item4.image}"></td>
		</c:forEach>
			</tr>
			<tr>
		<c:forEach items="${hand5}" var="item5">
				<td><img class="lastfive" src="${item5.image}"></td>
		</c:forEach>
			</tr>
			<tr>
				<a href="/game"><button type="button" class="btn btn-primary">Back To Game</button></a>
			</tr>
	</tbody>
</table>
</div>
</body>
</html>