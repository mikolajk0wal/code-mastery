package com.macias34.codemastery.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.macias34.codemastery.user.entity.ConfirmationToken;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {
	ConfirmationToken findByConfirmationToken(String confirmationToken);
	ConfirmationToken findByEmail(String email);
}
