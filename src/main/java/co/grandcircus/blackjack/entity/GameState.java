package co.grandcircus.blackjack.entity;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable{

	private Deck deck;
	private List<Card> dealerHand;
	private List<User> users;
	private List<Hand> hands;
	private List<Integer> handValues;
	private List<Integer> bets;
	private List<Boolean> turn;
	private Integer userIndex;
	
	public List<Boolean> getTurn() {
		return turn;
	}
	public void setTurn(List<Boolean> turn) {
		this.turn = turn;
	}
	public List<Integer> getBets() {
		return bets;
	}
	public void setBets(List<Integer> bets) {
		this.bets = bets;
	}
	public Deck getDeck() {
		return deck;
	}
	public void setDeck(Deck deck) {
		this.deck = deck;
	}
	public List<Card> getDealerHand() {
		return dealerHand;
	}
	public void setDealerHand(List<Card> dealerHand) {
		this.dealerHand = dealerHand;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public List<Hand> getHands() {
		return hands;
	}
	public void setHands(List<Hand> hands) {
		this.hands = hands;
	}
	public Integer getUserIndex() {
		return userIndex;
	}
	public void setUserIndex(Integer userIndex) {
		this.userIndex = userIndex;
	}
	public List<Integer> getHandValues() {
		return handValues;
	}
	public void setHandValues(List<Integer> handValues) {
		this.handValues = handValues;
	}
	
	
}
