package co.grandcircus.blackjack.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import co.grandcircus.blackjack.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	
}
