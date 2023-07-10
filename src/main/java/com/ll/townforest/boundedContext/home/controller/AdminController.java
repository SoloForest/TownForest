package com.ll.townforest.boundedContext.home.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
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
import com.ll.townforest.boundedContext.apt.service.AptAccountService;
import com.ll.townforest.boundedContext.gym.entity.GymMembership;
import com.ll.townforest.boundedContext.gym.service.GymService;
import com.ll.townforest.boundedContext.home.dto.SearchDTO;
import com.ll.townforest.boundedContext.library.entity.LibraryHistory;
import com.ll.townforest.boundedContext.library.entity.Seat;
import com.ll.townforest.boundedContext.library.service.LibraryService;
import com.ll.townforest.boundedContext.maintenance.form.GuestVehicleHistory;
import com.ll.townforest.boundedContext.maintenance.service.VehicleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
	private final Rq rq;
	private final LibraryService libraryService;
	private final AptAccountService aptAccountService;
	private final VehicleService vehicleService;

	private final GymService gymService;

	@GetMapping("")
	@PreAuthorize("isAuthenticated()")
	public String showAdmin() {
		if (rq.getAptAccount().getAuthority() == 0) {
			return "redirect:/";
		}
		return "admin/main";
	}

	// 독서실 관리자 페이지 시작
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

		return libraryService.adminCancel(targetUser, canCancelSeat.getData(), libraryHistoryId).getMsg();
	}
	// 독서실 관리자 페이지 끝

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/gym")
	public String adminMain(Model model) {
		AptAccount user = rq.getAptAccount();
		if (!rq.isGymAdmin())
			rq.historyBack("헬스장 관리자만 접속 가능합니다.");

		List<GymMembership> currentUsers = gymService.getMemberList(user);
		model.addAttribute("currentUsers", currentUsers);

		// 이용권 정지시킨 회원 필터링
		List<GymMembership> pauseUsers = currentUsers
			.stream()
			.filter(a -> a.getStatus() == 3)
			.collect(Collectors.toList());

		model.addAttribute("pauseUsers", pauseUsers);

		return "admin/gym/gym";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/gym/member")
	public String showMember(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
		SearchDTO searchDTO) {

		AptAccount user = rq.getAptAccount();
		if (!rq.isGymAdmin())
			rq.historyBack("헬스장 관리자만 접속 가능합니다.");

		Page<GymMembership> paging = gymService.getMemberPage(page, user, searchDTO);
		model.addAttribute("paging", paging);

		return "admin/gym/members";
	}

	@GetMapping("/management")
	public String showAptAccountManagement() {
		return "admin/aptAccount/management";
	}

	// 방문차량 조회 페이지 시작
	@GetMapping("/guest/vehicle")
	@PreAuthorize("isAuthenticated()")
	public String showGuestVehicles(
		Model model,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "1") int tab
	) {
		if (rq.getAptAccount().getAuthority() != 1) {
			return "redirect:/admin";
		}

		Page<GuestVehicleHistory> vehicles = vehicleService.findGuestByTab(page, tab);

		model.addAttribute("vehicles", vehicles);
		model.addAttribute("tab", tab);

		return "admin/guest/vehicle";
	}
	//방문자량 조회 페이지 끝
}