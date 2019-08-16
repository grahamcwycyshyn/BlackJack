package co.grandcircus.blackjack.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Long bankroll;
	private int wins;
	private int losses;
	private Long highestPayout;
	private double winLoss;
	
	
	public void setWinLoss(double winLoss) {
		this.winLoss = winLoss;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getBankroll() {
		return bankroll;
	}
	public void setBankroll(Long bankroll) {
		this.bankroll = bankroll;
	}
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public int getLosses() {
		return losses;
	}
	public void setLosses(int losses) {
		this.losses = losses;
	}
	public Long getHighestPayout() {
		return highestPayout;
	}
	public void setHighestPayout(Long highestPayout) {
		this.highestPayout = highestPayout;
	}
	public double getWinLoss() {
		return wins/losses;
	}
	

}
