<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<meta charset="ISO-8859-1">
<title>BlackJack Instructions</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootswatch/4.3.1/lux/bootstrap.min.css" />
</head>
<body>

	<div>
		<h1>OBJECT OF THE GAME</h1>
		<p> 
			Each participant attempts to beat the dealer by getting a count as close to 21 as possible, 
			without going over 21.
		</p>
	</div>
	
	<div>
		<h1>CARDS VALUE/SCORING</h1>
		<p>
			The computer will decide if an ace is worth 1 or 11. 
			Face cards are 10 and any other card is its pip value.
	    </p>
	 </div>
	   
	  <div>	
	    <h1>BETTING</h1>
	    <p>Before the deal begins, each player places a bet, in front of them in the designated area. 
	    Minimum and maximum limits are established on the betting, 
	    and the general limits are from $5 to $1000.
	    </p>
	
	</div>
	
	<div>
		<h1>GAMEPLAY</h1>
		<p>
			Each player is dealt two cards face up, 
			while the dealer is dealt one card face up and one card face down.
			Starting on the dealer's left, players are asked if they would like to hit-
			receive another card, or stay- keep their current cards.  If a player hits and their 
			score goes over 21, they bust- lose their bet.  After all players have
			had their turn, the dealer turns their face down card up and takes their turn.  After 
			the dealer is done with their turn, players whose score is higher than the dealers win 
			their bets, players who have the same score as the dealer do not win or lose their bets,
			and players whose score is below the dealers lose their bets. 
	    </p>	
	 </div>
	
	<div>
		<h1>SPECIAL CASES</h1>
		<p>
			If a player is dealt a blackjack- an ace and a face card, on the first deal, they 
			automatically win 1.5 times their bet.  If the player gets two of the same card 
			on the first deal, they have the option to split their hand- they place a second
			bet equal to their original bet and they split their cards
			into two hands, each of which will receive another card on top to be played as 
			an independent hand and bet. Any player may choose to double- double their bet and 
			receive one more card, but 
			only before they have chosen to hit or stay.  Any player may choose to surrender-
			lose half of their bet and drop out of the hand, but only before they have chosen to 
			hit or stay. 
	    </p>	
	 
	
	</div>
	<tr>
				<a href="/login-confirmation"><button type="button" class="btn btn-primary">Back</button></a>
			</tr>
	
	

</body>
</html>