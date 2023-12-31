package com.ll.townforest.boundedContext.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.account.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Optional<Account> findByUsername(String username);

	Optional<Account> findByEmail(String email);

	Optional<Account> findByPhoneNumber(String phoneNumber);
}
