package com.ll.townforest.boundedContext.home.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.townforest.base.rq.Rq;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
	private final Rq rq;

	@GetMapping("")
	@PreAuthorize("isAuthenticated()")
	public String showAdmin() {
		if (rq.getAptAccount().getAuthority() == 0) {
			return "redirect:/";
		}
		return "admin/main";
	}
}
