package com.ll.townforest.boundedContext.apt.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.townforest.base.rq.Rq;
import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.apt.DTO.AptAccountDTO;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.apt.entity.AptAccountHouse;
import com.ll.townforest.boundedContext.apt.service.AptAccountHouseService;
import com.ll.townforest.boundedContext.apt.service.AptAccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/aptAccount")
public class aptAccountController {
	private final Rq rq;
	private final AptAccountService aptAccountService;
	private final AptAccountHouseService aptAccountHouseService;

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/register")
	public String showRegister() {
		Optional<AptAccount> aptAccount = aptAccountService.findByAccount(rq.getAccount());
		if (aptAccount.isPresent()) {
			return "redirect:/";
		}
		return "aptAccount/register";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/register")
	public String register(@Valid AptAccountDTO aptAccountDTO) {
		RsData<AptAccount> aptAccountRsData = aptAccountService.canRegister(rq.getAccount(), aptAccountDTO);

		if (aptAccountRsData.isFail()) {
			return rq.historyBack(aptAccountRsData);
		}

		return rq.redirectWithMsg("/", aptAccountRsData);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/me")
	public String showMe(Model model) {
		AptAccountHouse aptAccountHouse = aptAccountHouseService.findByAptAccount(rq.getAptAccount()).orElse(null);
		List<AptAccountHouse> household = aptAccountService.findAllByHouse(aptAccountHouse);

		model.addAttribute("aptAccountHouse", aptAccountHouse);
		model.addAttribute("household", household);
		return "aptAccount/me";
	}
}
