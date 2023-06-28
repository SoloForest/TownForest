package com.ll.townforest.boundedContext.library.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.townforest.boundedContext.library.entity.LibraryHistory;
import com.ll.townforest.boundedContext.library.entity.Seat;
import com.ll.townforest.boundedContext.library.service.LibraryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/library")
public class LibraryController {
	private final LibraryService libraryService;

	@GetMapping("/booking")
	//@PreAuthorize("isAuthenticated()")
	public String showBooking(Model model) {
		Long aptAccountId = 5L;
		Optional<LibraryHistory> isUsing = libraryService.lastUsingOfDay(aptAccountId);

		List<Seat> seats = libraryService.findUseableList(1L);

		model.addAttribute("isUsing", isUsing.orElse(null));
		model.addAttribute("seats", seats);
		return "library/booking";
	}
}
