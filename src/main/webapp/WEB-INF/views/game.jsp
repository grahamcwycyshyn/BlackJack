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
<script src="function.js"></script>

</head>
<body>
	<div>
		<div class="inside">
			<table id="dealerHand">
				<thead>
					<tr>
						<c:forEach items="${dealerHand}" var="item" varStatus="loop">
						    <c:if test="${loop.first && stay == 0}">
						        <th><img class="backside" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQSVhzvnISkrvxN3Mg7uFbwKSmzg-EzcfNjnKKb5Sf90Rm8U5vd" /></th>
						    </c:if>
						    <c:if test="${loop.first && stay == 1}">
						        <th><img src="${item.image }" /></th>
						    </c:if>
						    <c:if test="${not loop.first}">
						        <th><img src="${item.image }" /></th> 
						    </c:if>
						</c:forEach>
<%-- 						<c:forEach var="each" items="${dealerHand}" var="sample" varStatus="status"> --%>
<%-- 							<th><img src="${each.image }" /></th> --%>
<%-- 						</c:forEach> --%>
					</tr>
				</thead>
				<tbody>
					<tr>
<%-- 						<c:forEach var="each" items="${dealerHand}"> --%>
<%-- 							<td>${each.value }</td> --%>
<%-- 						</c:forEach> --%>
						<c:forEach items="${dealerHand}" var="item" varStatus="loop">
						    <c:if test="${loop.first && stay == 0}">
						        <td></td>
						    </c:if>
						    <c:if test="${loop.first && stay == 1}">
						        <td>${item.value }</td>
						    </c:if>
						    <c:if test="${not loop.first}">
						        <td>${item.value }</td> 
						    </c:if>
						</c:forEach>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="bets">
			<button onclick="addFive()" id="addFive">$5</button>
			<button onclick="addTwentyFive()" id="addTwentyFive">$25</button>
			<button onclick="addHundred()" id="addHundred">$100</button>
			<button onclick="addFiveHundred()" id="addFiveHundred">$500</button>
			<button onclick="addThousand()" id="addThousand">$1000</button>
			<span class="betAmount" id="betAmount">$5</span>
			<form action="/deal" id="betDeal">
				<input type="hidden" name="betDeal" value="5">
				<button type="submit" value="Submit">Deal</button>
			</form>
		</div>
		<div class="inside">
			<table id="playerHand">
				<thead>
					<tr>
						<c:forEach var="each" items="${userHand}">
							<th><img src="${each.image }" /></th>
						</c:forEach>

					</tr>
				</thead>
				<tbody>
					<tr>
						<td>Value: ${userHandValue}</td>

					</tr>
				</tbody>
			</table>
			<c:if test="${stay == 0}">
			<form action="/hit">
				<button type="submit">Hit</button>
			</form>
			<form action="/stay">
				<button type="submit" onClick="flipCard()">Stay</button>
			</form>
			</c:if>

			<p>Bankroll: $${ user.bankroll }</p>
		</div>
	</div>
</body>
</html>