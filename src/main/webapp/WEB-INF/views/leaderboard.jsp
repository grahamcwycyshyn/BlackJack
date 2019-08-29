<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Leaderboard</title>
<link href="/leaderboard.css" rel="stylesheet" />
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootswatch/4.3.1/lux/bootstrap.min.css" />
</head>
<body>
	<div class="conatiner">
		<h1>LeaderBoards</h1>
		<table id="handsWon">
			<thead>
				<tr>
				  	<th>Rank</th>
					<th>Name</th>
					<th>Hands Won</th>
				  
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${mostwins}" begin="0" end="19" var="item" varStatus="loop" >
					<tr>
					<c:if test="${item.wins!=0 }">
						<td>${loop.count }</td>
						<td>${item.name }</td>
						<td>${item.wins }</td>
				  </c:if>
				</tr>
				</c:forEach>
			</tbody>
		</table>

<!-- 		<table id="winPercentage">
			<thead>
				<tr>
					<th>Rank</th>
					<th>Name</th>
					<th>Win/Loss</th>
				</tr>
			</thead>
		</table>
 -->
		<table id="mostMoney">
			<thead>
				<tr>
					<th>Rank</th>
					<th>Name</th>
					<th>Bankroll</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${mostBankroll}" begin="0" end="19" var="item" varStatus="loop" >
			<tr>
				<td>${loop.count }</td>
				<td>${item.name }</td>
				<td>${item.bankroll }</td>
			</tr>
			</c:forEach>
			</tbody>
		</table>
		
<!-- 		<table id="highestPayout">
			<thead>
				<tr>
					<th>Rank</th>
					<th>Name</th>
					<th>Payout</th>
				</tr>
			</thead>
		</table> -->
<a href="/game"><button type="button" class="btn btn-primary">Back To Game</button></a>
	</div>
</body>
</html>