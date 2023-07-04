package com.ll.townforest.boundedContext.home.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ll.townforest.base.rq.Rq;
import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.library.entity.LibraryHistory;
import com.ll.townforest.boundedContext.library.entity.Seat;
import com.ll.townforest.boundedContext.library.service.LibraryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
	private final Rq rq;
	private final LibraryService libraryService;

	@GetMapping("")
	@PreAuthorize("isAuthenticated()")
	public String showAdmin() {
		if (rq.getAptAccount().getAuthority() == 0) {
			return "redirect:/";
		}
		return "admin/main";
	}

	@GetMapping("/library/histories")
	@PreAuthorize("isAuthenticated()")
	public String showLibraryHistories(Model model) {
		if (rq.getAptAccount().getAuthority() == 0) {
			return "redirect:/";
		}

		if (rq.getAptAccount().getAuthority() == 3) {
			return "redirect:/admin";
		}

		Slice<LibraryHistory> histories = libraryService.findAllHistories(PageRequest.of(0, 25));
		model.addAttribute("histories", histories);
		return "admin/library/histories";
	}

	@PostMapping("/library/histories")
	@ResponseBody
	public Slice<LibraryHistory> libraryHistories(@RequestParam int page, @RequestParam int size) {
		return libraryService.findAllHistories(PageRequest.of(page, size));
	}

	@PostMapping("/library/cancel")
	@ResponseBody
	public String libraryBookingCancel(@RequestParam Long libraryHistoryId) {
		RsData<AptAccount> canCancelUser = libraryService.canAdminCancel(rq.getAptAccount());
		if (canCancelUser.isFail()) {
			return canCancelUser.getMsg();
		}

		RsData<Seat> canCancelSeat = libraryService.canAdminCancel(libraryHistoryId);
		if (canCancelSeat.isFail()) {
			return canCancelSeat.getMsg();
		}

		AptAccount targetUser = libraryService.getTargetUserForAdminCancel(libraryHistoryId);

		return libraryService.AdminCancel(targetUser, canCancelSeat.getData(), libraryHistoryId).getMsg();
	}
}