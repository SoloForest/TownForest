package com.ll.townforest.boundedContext.account.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ll.townforest.base.rq.Rq;
import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.account.dto.AccountDTO;
import com.ll.townforest.boundedContext.account.entity.Account;
import com.ll.townforest.boundedContext.account.service.AccountService;
import com.ll.townforest.boundedContext.apt.DTO.EditForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
	private final AccountService accountService;
	private final Rq rq;

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

	@PreAuthorize("isAnonymous()")
	@PostMapping("/join")
	public String join(@Valid AccountDTO accountDTO) {
		RsData<Account> accountRsData = accountService.join(accountDTO);

		if (accountRsData.isFail()) {
			return rq.historyBack(accountRsData);
		}
		return rq.redirectWithMsg("/account/login", accountRsData);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/edit")
	public String showEdit() {
		return "account/edit";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/edit")
	public String edit(@Valid EditForm editForm) {
		RsData<Account> accountRsData = accountService.edit(rq.getAccount(), editForm);

		if (accountRsData.isFail()) {
			return rq.historyBack(accountRsData);
		}

		return rq.redirectWithMsg("/account/edit", accountRsData);
	}

	@GetMapping("/confirmPwd")
	public @ResponseBody boolean confirmPassword(@RequestParam String password) {
		return accountService.confirmPassword(rq.getAccount(), password);
	}
}
