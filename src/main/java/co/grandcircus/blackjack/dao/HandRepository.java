package co.grandcircus.blackjack.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import co.grandcircus.blackjack.entity.Hand;

public interface HandRepository extends JpaRepository<Hand, Long> {
	

}
