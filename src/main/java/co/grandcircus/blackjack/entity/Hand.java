package co.grandcircus.blackjack.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hand")
public class Hand {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
