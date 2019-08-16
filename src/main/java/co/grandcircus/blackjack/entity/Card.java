package co.grandcircus.blackjack.entity;

import java.util.List;

public class Card {

	private String value;
	private String image;
	private String suit;
	
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
	public int getHandValue(List<Card> hand) {
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
			
		}
		
		
		return score;
	}
	public int acesInHand(List<Card> hand) {
		int aceCount = 0;
		for(int i = 0; i < hand.size(); i++) {
			if(hand.get(i).getValue().equalsIgnoreCase("ACE")) {
				aceCount++;
			}
		}
		return aceCount;
	}
	
}

