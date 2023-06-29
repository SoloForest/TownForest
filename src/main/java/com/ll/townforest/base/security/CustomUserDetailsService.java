package com.ll.townforest.base.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.boundedContext.account.entity.Account;
import com.ll.townforest.boundedContext.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
	private final AccountRepository accountRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("username(%s) not found".formatted(username)));

		return new User(account.getUsername(), account.getPassword(), account.getGrantedAuthorities());
	}
}
