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
	private String card;
	
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
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

	
}
