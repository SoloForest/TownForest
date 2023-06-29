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
import com.ll.townforest.boundedContext.apt.repository.AptAccountRepository;
import com.ll.townforest.boundedContext.apt.repository.AptRepository;
import com.ll.townforest.boundedContext.library.entity.LibraryHistory;
import com.ll.townforest.boundedContext.library.entity.Seat;
import com.ll.townforest.boundedContext.library.repository.LibraryHistoryRepository;
import com.ll.townforest.boundedContext.library.repository.LibraryRepository;
import com.ll.townforest.boundedContext.library.repository.SeatRepository;
import com.ll.townforest.boundedContext.library.service.LibraryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/library")
public class LibraryController {
	private final LibraryService libraryService;
	private final AptAccountRepository aptAccountRepository;
	private final SeatRepository seatRepository;
	private final LibraryHistoryRepository libraryHistoryRepository;
	private final AptRepository aptRepository;
	private final LibraryRepository libraryRepository;

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
		// Optional<AptAccount> optAptAccount = aptAccountRepository.findById(aptAccountId);
		// if (optAptAccount.isEmpty() || !optAptAccount.get().getApt().getId().equals(1L)) {
		// 	return "해당 아파트 독서실 이용권한이 없습니다.";
		// }

		Seat seat = libraryService.canBooking(selectedSeat);
		// Optional<Seat> optSeat = seatRepository.findBySeatNumber(selectedSeat);
		// if (optSeat.isEmpty() || optSeat.get().getStatus() != 0) {
		// 	return "사용 불가능한 자리입니다.";
		// }

		return libraryService.booking(user, seat, selectedSeat);
	}
}
