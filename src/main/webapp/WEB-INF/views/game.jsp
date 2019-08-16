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
		<div class="bets">
		<button onclick="">5</button>
		<button onclick="">25</button>
		<button onclick="">100</button>
		<button onclick="">500</button>
		<button onclick="">1000</button>
		</div>
		
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
			<p>Bankroll: ${ user.bankroll }</p>
		</div>
	</div>
</body>
</html>