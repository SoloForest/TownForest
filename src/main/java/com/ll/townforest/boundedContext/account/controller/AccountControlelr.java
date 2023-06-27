package com.ll.townforest.boundedContext.account.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountControlelr {

	@GetMapping("/login")
	public String showLogin() {
		return "account/login";
	}
}
