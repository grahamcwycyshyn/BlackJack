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
	
		<div class="inside" id="dealer">
		<h1 id="black">BLACK</h1>
			<table id="dealerHand">
				<thead>
					<tr>
						<c:forEach items="${gamestate.getDealerHand()}" var="item" varStatus="loop">
						    <c:if test="${loop.first && gamestate.phase != 0}">
						        <th><img class="backside" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQSVhzvnISkrvxN3Mg7uFbwKSmzg-EzcfNjnKKb5Sf90Rm8U5vd" /></th>
						    </c:if>
						    <c:if test="${loop.first && gamestate.phase == 0}">
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
						    <c:if test="${loop.first && gamestate.phase != 0}">
						        <td></td>
						    </c:if>
						    <c:if test="${loop.first && gamestate.phase == 0}">
						        <td>${item.value }</td>
						    </c:if>
						    <c:if test="${not loop.first}">
						        <td>${item.value }</td> 
						    </c:if>
						</c:forEach>
						    <c:if test="${gamestate.phase == 0}">
						        <td>${item.value }</td>
						        <tr><td>Value: ${dealerHandValue}</td></tr>
						    </c:if>
					</tr>
				</tbody>
			</table>
			<h1 id="jack">JACK</h1>
		</div>
	  			
		<div class="bets">
		<c:if test="${gamestate.phase == 0 }">
			<button onclick="addFive()" id="addFive">$5</button>
			<button onclick="addTwentyFive()" id="addTwentyFive">$25</button>
			<button onclick="addHundred()" id="addHundred">$100</button>
			<button onclick="addFiveHundred()" id="addFiveHundred">$500</button>
			<button onclick="addThousand()" id="addThousand">$1000</button>
			<span class="betAmount" id="betAmount">0</span>
			<h2>Betting phase</h2>
		</c:if>
			<c:if test="${gamestate.phase == 0 && gamestate.userIndex == 0}">
			<form action="/deal" id="betDeal">
				<input type="hidden" name="betDeal" value="0">
				<button type="submit" value="Submit">Deal</button>
			</form>
			</c:if>
			<c:if test="${gamestate.phase == 0 && gamestate.userIndex != 0 }">
				<form action="/next" id="nextBet">
					<input type="hidden" name="betDeal" value="5">
					<button type="submit">Next</button>
				</form>
			</c:if>
		</div>
		<div class="inside">
		<c:forEach var="hand" items="${users}">
		<table id="playerHand">
			<thead>
				<tr>
					<th>${hand.name}</th><c:if test="${hand.userIndex == gamestate.userIndex}"><th><img src="http://www.freepngclipart.com/download/diamond/57419-blue-diamond-computer-file-png-file-hd.png" id="diamond"/></th></c:if>
				</tr>
				<tr>
					<th>Bank:</th><th>${hand.bankroll}</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="each" items="${hand.hands}">
					<tr>
						<c:forEach var="card" items="${each.cards}">
							<td><img src="${card.image }" /></td>
						</c:forEach>
					</tr>
					<tr>
						<td>Value: ${each.handValue}</td>
					</tr>
				</c:forEach>
			</tbody>
			</table>
			</c:forEach>
			<c:if test="${gamestate.phase == 1 || gamestate.phase == 2 || gamestate.phase == 3}">
				<a href="/hit"><button>Hit</button></a>
			</c:if>
			<c:if test="${gamestate.phase == 2}">
				<a href="/split"><button>Split</button></a>
			</c:if>
			<c:if test="${gamestate.phase == 1 || gamestate.phase == 2}">
				<a href="/double"><button onClick="flipCard()">Double</button></a>
			</c:if>
			<c:if test="${gamestate.phase == 1 || gamestate.phase == 2}"><a href="/surrender"><button>Surrender</button></a></c:if>
			<c:if test="${gamestate.phase == 1 || gamestate.phase == 2 || gamestate.phase == 3 || gamestate.phase == 4 || gamestate.phase == 5 || gamestate.phase == 6 || gamestate.phase == 7}">
				<a href="/stay"><button onClick="flipCard()">Stay</button></a>
			</c:if>
			<c:if test="${gamestate.phase == 5 || gamestate.phase == 7 }">
				<a href="/hitTop"><button>Hit Top</button></a>
			</c:if>
			<c:if test="${gamestate.phase == 5 || gamestate.phase == 6}">
				<a href="/hitBottom"><button>Hit Bottom</button></a>
			</c:if>
			<c:if test="${gamestate.phase == 5 || gamestate.phase == 6}">
				<a href="/doubleTop"><button>Double Top</button></a>
			</c:if>
			<c:if test="${gamestate.phase == 5 || gamestate.phase == 7}">
				<a href="/doubleBottom"><button>Double Bottom</button></a>
			</c:if>
<!-- 				<a href="/surrenderTop"><button>Surrender Top</button></a>
				<a href="/surrenderBottom"><button>Surrender Bottom</button></a> -->
		</div>
	</div>
	
	<c:if test="${gamestate.phase == 0 }"><a href="/lastHands"><button type="button" class="btn btn-primary">Last 5 Hands</button></a></c:if>
	<c:if test="${gamestate.numUser != 5 && gamestate.phase == 0}"><form action="/join" method="post" ><input type="text" placeholder="Enter name" name="name" required autofocus><a href="/join"><button type="submit">Join</button></a></form></c:if>

	<c:if test="${gamestate.phase == 0 }">
		<a href="/leaderBoard"><button type="button" class="btn btn-primary">Leader Board</button></a>
	</c:if>


	
</body>
</html>