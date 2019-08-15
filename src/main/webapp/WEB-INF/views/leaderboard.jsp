<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Leaderboard</title>
</head>
<body>
	<div class="conatiner">

		<table id="handsWon">
			<thead>
				<tr>
					<th>Rank</th>
					<th>Name</th>
					<th>Hands Won</th>
				</tr>
			</thead>
		</table>

		<table id="winPercentage">
			<thead>
				<tr>
					<th>Rank</th>
					<th>Name</th>
					<th>Win/Loss</th>
				</tr>
			</thead>
		</table>

		<table id="mostMoney">
			<thead>
				<tr>
					<th>Rank</th>
					<th>Name</th>
					<th>Bankroll</th>
				</tr>
			</thead>
		</table>
		
		<table id="highestPayout">
			<thead>
				<tr>
					<th>Rank</th>
					<th>Name</th>
					<th>Payout</th>
				</tr>
			</thead>
		</table>

	</div>
</body>
</html>