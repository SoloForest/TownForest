package com.ll.townforest.boundedContext.account.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
	@PreAuthorize("isAnonymous()")
	@GetMapping("/login")
	public String showLogin() {
		return "account/login";
	}

	@PreAuthorize("isAnonymous()")
	@GetMapping("/join")
	public String showJoin() {
		return "account/join";
	}
}
