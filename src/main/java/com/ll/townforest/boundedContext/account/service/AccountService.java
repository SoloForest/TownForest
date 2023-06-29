package com.ll.townforest.boundedContext.account.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.boundedContext.account.entity.Account;
import com.ll.townforest.boundedContext.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {
	private final AccountRepository accountRepository;

	public Optional<Account> findByUsername(String username) {
		return accountRepository.findByUsername(username);
	}
}
