package com.ll.townforest.boundedContext.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.townforest.boundedContext.library.service.LibraryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/library")
public class LibraryController {
	private final LibraryService libraryService;

	@GetMapping("/booking")
	//@PreAuthorize("isAuthenticated()")
	public String showBooking() {
		return "library/booking";
	}
}
