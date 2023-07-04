package com.ll.townforest.boundedContext.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
	@GetMapping("")
	public String showAdmin() {
		return "admin/main";
	}

	@GetMapping("/library/histories")
	public String showLibraryHistories() {
		return "admin/library/histories";
	}
}
