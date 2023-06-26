package com.ll.townforest.boundedContext.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.account.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
