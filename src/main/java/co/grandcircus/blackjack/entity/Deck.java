package co.grandcircus.blackjack.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Deck {

	@JsonProperty("deck_id")
	private String id;
	private List<Card> cards;
	private Integer remaining;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Card> getCards() {
		return cards;
	}
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	public Integer getRemaining() {
		return remaining;
	}
	public void setRemaining(Integer remaining) {
		this.remaining = remaining;
	}
}

