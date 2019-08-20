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
						    <c:if test="${loop.first && stay != 5}">
						        <th><img class="backside" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQSVhzvnISkrvxN3Mg7uFbwKSmzg-EzcfNjnKKb5Sf90Rm8U5vd" /></th>
						    </c:if>
						    <c:if test="${loop.first && stay == 5}">
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
						    <c:if test="${loop.first && stay != 5}">
						        <td></td>
						    </c:if>
						    <c:if test="${loop.first && stay == 5}">
						        <td>${item.value }</td>
						    </c:if>
						    <c:if test="${not loop.first}">
						        <td>${item.value }</td> 
						    </c:if>
						</c:forEach>
						    <c:if test="${stay == 5}">
						        <td>${item.value }</td>
						        <tr><td>Value: ${dealerHandValue}</td></tr>
						    </c:if>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="bets">
			<c:if test="${stay == 5|| stay == 0}">
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
			</c:if>
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
			
			<c:if test="${stay == 1||stay == 2||stay == 3}">
				<a href="/hit"><button>Hit</button></a>
			</c:if>
			<c:if test="${stay == 3}">
				<a href="/split"><button>Split</button></a>
			</c:if>
			<c:if test="${stay == 2||stay == 3}">
			<a href="/double"><button onClick="flipCard()">Double</button></a>
			</c:if>
			<c:if test="${stay == 1||stay == 2||stay == 3||stay == 4}">
			<a href="/stay"><button onClick="flipCard()">Stay</button></a>
			</c:if>
			<p>Bankroll: $${ user.bankroll }</p>
		</div>
	</div>
</body>
</html>