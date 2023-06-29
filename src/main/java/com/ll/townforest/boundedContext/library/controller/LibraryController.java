package com.ll.townforest.boundedContext.library.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ll.townforest.boundedContext.apt.entity.AptAccount;
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
		LibraryHistory isUsing = libraryService.usingHistory(aptAccountId);

		List<Seat> seats = libraryService.findUseableList(1L);

		model.addAttribute("isUsing", isUsing);
		model.addAttribute("seats", seats);
		return "library/booking";
	}

	@PostMapping("/booking")
	@ResponseBody
	//@PreAuthorize("isAuthenticated()")
	public String Booking(@RequestParam("selectedSeat") int selectedSeat) {
		Long aptAccountId = 5L;
		AptAccount user = libraryService.canBooking(aptAccountId);
		if (user == null) {
			return "해당 아파트 독서실 이용권한이 없습니다.";
		}

		Seat seat = libraryService.canBooking(selectedSeat);
		if (seat == null) {
			return "사용 불가능한 자리입니다.";
		}

		return libraryService.booking(user, seat, selectedSeat);
	}
}
