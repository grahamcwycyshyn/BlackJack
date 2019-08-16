package co.grandcircus.blackjack.entity;

import java.util.List;

public class Card {

	private String value;
	private String image;
	private String suit;
	
	public Card() {}
	
	public Card(String value) {
		super();
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getSuit() {
		return suit;
	}
	public void setSuit(String suit) {
		this.suit = suit;
	}
	public static int getHandValue(List<Card> hand) {
		int score = 0;
		int aceCount = acesInHand(hand);
		for(int i = 0; i < hand.size(); i++) {
			if(hand.get(i).getValue().equalsIgnoreCase("ACE")) {
				score += 1;
			} else if(hand.get(i).getValue().equalsIgnoreCase("KING")||hand.get(i).getValue().equalsIgnoreCase("QUEEN")||hand.get(i).getValue().equalsIgnoreCase("JACK")) {
				score += 10;
			} else {
				score += Integer.parseInt(hand.get(i).getValue());
			}
		}
		for(int i = aceCount; i > 0; i--) {
			if(score + (i * 10) < 22) {
				score += 10;
			}
		}
		return score;
	}
	public static int acesInHand(List<Card> hand) {
		int aceCount = 0;
		for(int i = 0; i < hand.size(); i++) {
			if(hand.get(i).getValue().equalsIgnoreCase("ACE")) {
				aceCount++;
			}
		}
		return aceCount;
	}
	public static boolean isValueSoft(List<Card> hand) {
		int score = 0;
		int aceCount = acesInHand(hand);
		for(int i = 0; i < hand.size(); i++) {
			if(hand.get(i).getValue().equalsIgnoreCase("ACE")) {
				score += 1;
			} else if(hand.get(i).getValue().equalsIgnoreCase("KING")||hand.get(i).getValue().equalsIgnoreCase("QUEEN")||hand.get(i).getValue().equalsIgnoreCase("JACK")) {
				score += 10;
			} else {
				score += Integer.parseInt(hand.get(i).getValue());
			}
		}
		for(int i = aceCount; i > 0; i--) {
			if(score + (i * 10) < 22) {
				return true;
			}
		}
		return false;
	}
	public static boolean dealerHit(List<Card> hand) {
		boolean isSoft = isValueSoft(hand);
		int score = getHandValue(hand);
		if(isSoft && score < 18) {
			return true;
		}else if(score < 17) {
			return true;
		}else {			
			return true;
		}
	}
	public static boolean canSplit(List<Card> hand) {
		if(hand.size() > 2) {
			return false;
		}else if(hand.get(0).getValue().equalsIgnoreCase(hand.get(1).getValue()) ) {
			return true;
		}else {
			return false;
		}
	}
	public static boolean canDouble(List<Card> hand) {
		if(hand.size() > 2) {
			return false;
		}else {
			return true;
		}
		
	}
	public static boolean bust(List<Card> hand) {
		if(Card.getHandValue(hand) > 21) {
			return true;
		}else {
			return false;
		}
	}
	public static boolean canHit(List<Card> hand) {
		if(!Card.bust(hand)) {
			return true;
		}else {
			return false;
		}
	}

	public String toString() {
		return value + "," + image + "," + suit;
	}
	
	
}

