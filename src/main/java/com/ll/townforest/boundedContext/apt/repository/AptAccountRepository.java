package com.ll.townforest.boundedContext.apt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.account.entity.Account;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;

public interface AptAccountRepository extends JpaRepository<AptAccount, Long> {
	Optional<AptAccount> findByAccount(Account account);
}
