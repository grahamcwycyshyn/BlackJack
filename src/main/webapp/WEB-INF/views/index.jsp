<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Home</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootswatch/4.3.1/lux/bootstrap.min.css" />
<link href="/styles.css" rel="stylesheet" />
</head>
<body>
	<div>
		<div class="inside">
			<table id="dealerHand">
				<thead>
					<tr>
						<c:forEach var="each" items="${card}">
							<th><img src="${each.image }" /></th>
						</c:forEach>
						<th>Value</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<c:forEach var="each" items="${card}">
							<td>${each.value }</td>
						</c:forEach>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="inside"></div>
		<div class="inside">
			<table id="playerHand">
				<thead>
					<tr>
						<c:forEach var="each" items="${card}">
							<th><img src="${each.image }" /></th>
						</c:forEach>
						<th>Value</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<c:forEach var="each" items="${card}">
							<td>${each.value }</td>
						</c:forEach>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>