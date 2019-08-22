package co.grandcircus.blackjack.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.grandcircus.blackjack.entity.Hand;

public interface HandRepository extends JpaRepository<Hand, Long> {
	List<Hand> findFirst5ByUserIdOrderByHandId(Long id);
	List<Hand> findFirst5ByUserIdOrderByHandIdDesc(Long id);
}
