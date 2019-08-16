package co.grandcircus.blackjack.entity;

import java.util.List;

public class Hand {
	
	private Long handId;
	private Long userId;
	private List<Card> cards;
	
	public Long getHandId() {
		return handId;
	}
	public void setHandId(Long handId) {
		this.handId = handId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<Card> getCards() {
		return cards;
	}
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	
}
