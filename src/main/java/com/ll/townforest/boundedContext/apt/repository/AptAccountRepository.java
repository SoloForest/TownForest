package com.ll.townforest.boundedContext.apt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.apt.entity.AptAccount;

public interface AptAccountRepository extends JpaRepository<AptAccount, Long> {
}
