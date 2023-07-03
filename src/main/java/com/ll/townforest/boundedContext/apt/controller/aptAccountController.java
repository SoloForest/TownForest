package com.ll.townforest.boundedContext.apt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/aptAccount")
public class aptAccountController {
	//@PreAuthorize("isAuthenticated()")
	@GetMapping("/register")
	public String showRegister() {
		return "aptAccount/register";
	}
}
