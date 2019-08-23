package co.grandcircus.blackjack.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Long bankroll;
	private Integer wins;
	private Integer losses;
	private Long highestPayout;
	private Double winLoss;
	
	
	public void setWinLoss(Double winLoss) {
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
	public void setWins(Integer wins) {
		this.wins = wins;
	}
	public int getLosses() {
		return losses;
	}
	public void setLosses(Integer losses) {
		this.losses = losses;
	}
	public Long getHighestPayout() {
		return highestPayout;
	}
	public void setHighestPayout(Long highestPayout) {
		this.highestPayout = highestPayout;
	}
	public double getWinLoss() {
		return winLoss;
	}
	

}
