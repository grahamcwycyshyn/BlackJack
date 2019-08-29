package co.grandcircus.blackjack.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

@Entity
@Table(name = "hand")
public class Hand {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long handId;
	private Long userId;
	private String value;
	@Column(length = 900)
	private String hand;
	transient List<Card> cards;
	transient int handValue;
	


	public int getHandValue() {
		return handValue;
	}
	public void setHandValue(int handValue) {
		this.handValue = handValue;
	}
	@Transient
	public List<Card> getCards() {
		return cards;
	}
	@Transient
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
//	public String getCard() {
//		return card;
//	}
//	public void setCard(String card) {
//		this.card = card;
//	}
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
	public boolean bust(List<Card> hand) {
		if(getHandValue(hand) > 21) {
			return true;
		}else {
			return false;
		}
	}
	public boolean canHit(List<Card> hand) {
		if(!bust(hand)) {
			return true;
		}else {
			return false;
		}
	}
	public String getHand() {
		return hand;
	}
	public void setHand(String hand) {
		this.hand = hand;
	}
}
