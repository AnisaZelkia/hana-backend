package com.simple.demo.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simple.demo.persistence.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
	boolean existsByEmail(String email);

	/** Use Drive Name Query */
	Optional<User> findByEmail(String email);
}
