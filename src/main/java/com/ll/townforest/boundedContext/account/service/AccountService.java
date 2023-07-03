package com.ll.townforest.boundedContext.account.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.account.dto.AccountDTO;
import com.ll.townforest.boundedContext.account.entity.Account;
import com.ll.townforest.boundedContext.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {
	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;

	public Optional<Account> findByUsername(String username) {
		return accountRepository.findByUsername(username);
	}

	private Optional<Account> findByEmail(String email) {
		return accountRepository.findByEmail(email);
	}

	private Optional<Account> findByPhoneNumber(String phoneNumber) {
		return accountRepository.findByPhoneNumber(phoneNumber);
	}

	public RsData<Account> join(AccountDTO dto) {
		if (findByUsername(dto.getUsername()).isPresent()) {
			return RsData.of("F-1", "사용할 수 없는 아이디입니다.<br>다른 아이디를 입력해 주세요.");
		}

		if (findByEmail(dto.getEmail()).isPresent()) {
			return RsData.of("F-2", "사용할 수 없는 이메일입니다.<br>다른 이메일을 입력해 주세요.");
		}

		if (findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
			return RsData.of("F-3", "사용할 수 없는 휴대전화번호입니다.<br>다른 휴대전화번호를 입력해 주세요.");
		}

		String password = passwordEncoder.encode(dto.getPassword());

		Account account = Account
			.builder()
			.username(dto.getUsername())
			.password(password)
			.fullName(dto.getFullName())
			.email(dto.getEmail())
			.phoneNumber(dto.getPhoneNumber())
			.build();

		accountRepository.save(account);

		return RsData.of("S-1", "회원 가입이 완료되었습니다.", account);
	}
}
