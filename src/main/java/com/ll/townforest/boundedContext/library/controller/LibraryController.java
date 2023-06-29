package com.ll.townforest.boundedContext.library.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ll.townforest.base.rsData.RsData;
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
		RsData<AptAccount> canBookingUser = libraryService.canBooking(aptAccountId);
		if (canBookingUser.isFail()) {
			return canBookingUser.getMsg();
		}

		RsData<Seat> canBookingSeat = libraryService.canBooking(selectedSeat);
		if (canBookingSeat.isFail()) {
			return canBookingSeat.getMsg();
		}

		return libraryService.booking(canBookingUser.getData(), canBookingSeat.getData(), selectedSeat).getMsg();
	}
}
