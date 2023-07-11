package com.ll.townforest.boundedContext.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.ll.townforest.base.rq.Rq;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	private final Rq rq;

	@GetMapping("/")
	public String root() {
		if (rq.isLogout())
			return "redirect:/account/login";

		if (rq.getAptAccount() == null)
			return "redirect:/aptAccount/register";

		if (rq.isAdmin())
			return "redirect:/admin";

		if (!rq.getAptAccount().isStatus())
			return "aptAccount/awaiting_approval";

		return "home/main";
	}
}
