package com.ll.townforest.boundedContext.apt.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.boundedContext.account.entity.Account;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.apt.repository.AptAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AptAccountService {
	private final AptAccountRepository aptAccountRepository;

	public Optional<AptAccount> findByAccount(Account account) {
		return aptAccountRepository.findByAccount(account);
	}
}
